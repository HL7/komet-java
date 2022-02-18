package org.hl7.komet.framework.panel;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.controlsfx.control.action.Action;
import org.controlsfx.control.action.ActionUtils;
import org.eclipse.collections.api.list.ImmutableList;
import org.hl7.komet.framework.PseudoClasses;
import org.hl7.komet.framework.graphics.Icon;
import org.hl7.komet.framework.observable.*;
import org.hl7.komet.framework.performance.Measures;
import org.hl7.komet.framework.performance.StatementStore;
import org.hl7.komet.framework.performance.Topic;
import org.hl7.komet.framework.performance.impl.ObservationRecord;
import org.hl7.komet.framework.rulebase.Consequence;
import org.hl7.komet.framework.rulebase.ConsequenceAction;
import org.hl7.komet.framework.rulebase.RuleBase;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.tinkar.common.alert.AlertObject;
import org.hl7.tinkar.common.alert.AlertStreams;
import org.hl7.tinkar.common.service.TinkExecutor;
import org.hl7.tinkar.common.util.time.DateTimeUtil;
import org.hl7.tinkar.coordinate.Coordinates;
import org.hl7.tinkar.coordinate.stamp.calculator.Latest;
import org.hl7.tinkar.entity.*;
import org.hl7.tinkar.entity.transaction.*;
import org.hl7.tinkar.terms.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.concurrent.Future;

import static org.hl7.komet.framework.StyleClasses.*;

public abstract class ComponentVersionIsFinalPanel<OV extends ObservableVersion> {
    private static final Logger LOG = LoggerFactory.getLogger(ComponentVersionIsFinalPanel.class);
    protected final BorderPane versionDetailsPane = new BorderPane();
    protected final TitledPane collapsiblePane = new TitledPane("version", versionDetailsPane);
    private final OV observableVersion;
    private final ViewProperties viewProperties;

    public ComponentVersionIsFinalPanel(OV observableVersion, ViewProperties viewProperties) {
        this.observableVersion = observableVersion;
        this.viewProperties = viewProperties;
        Node versionNode = makeCenterNode(observableVersion, viewProperties);
        if (versionNode != null) {
            BorderPane.setAlignment(versionNode, Pos.TOP_LEFT);
            versionNode.pseudoClassStateChanged(PseudoClasses.INACTIVE_PSEUDO_CLASS, !observableVersion.active());
        }
        this.versionDetailsPane.getStyleClass().add(COMPONENT_VERSION_BORDER_PANEL.toString());
        this.versionDetailsPane.pseudoClassStateChanged(PseudoClasses.UNCOMMITTED_PSEUDO_CLASS, observableVersion.uncommitted());
        this.versionDetailsPane.setCenter(versionNode);
        //this.versionDetailsPane.setBottom(new StampPanel<V>(version, viewProperties));
        StampEntity stampEntity = observableVersion.stamp();
        Label stampLabel = new Label(stampEntity.state() + " as of " + DateTimeUtil.format(stampEntity.time()) +
                " on " + viewProperties.calculator().getPreferredDescriptionTextWithFallbackOrNid(stampEntity.pathNid()) +
                " in " + viewProperties.calculator().getPreferredDescriptionTextWithFallbackOrNid(stampEntity.moduleNid()) +
                " by " + viewProperties.calculator().getPreferredDescriptionTextWithFallbackOrNid(stampEntity.authorNid()));
        stampLabel.getStyleClass().add(STAMP_LABEL.toString());
        this.collapsiblePane.setText("");
        this.collapsiblePane.pseudoClassStateChanged(PseudoClasses.INACTIVE_PSEUDO_CLASS, !observableVersion.active());
        this.collapsiblePane.pseudoClassStateChanged(PseudoClasses.UNCOMMITTED_PSEUDO_CLASS, observableVersion.uncommitted());
        this.collapsiblePane.getStyleClass().add(COMPONENT_VERSION_PANEL.toString());
        ObservationRecord observation = new ObservationRecord(Topic.COMPONENT_FOCUSED, observableVersion, Measures.present());
        StatementStore statementStore = StatementStore.make(observation);
        ImmutableList<Consequence<?>> consequences = RuleBase.execute(statementStore, viewProperties.calculator(), Coordinates.Edit.Default());
        ArrayList<Node> buttonList = new ArrayList<>(3);
        if (!consequences.isEmpty()) {
            MenuButton menuButton = new MenuButton("", Icon.EDIT_PENCIL.makeIcon());
            menuButton.getStyleClass().add(EDIT_COMPONENT_BUTTON.toString());
            for (Consequence<?> consequence : consequences) {
                if (consequence instanceof ConsequenceAction consequenceAction) {
                    if (consequenceAction.generatedAction() instanceof Action action) {
                        menuButton.getItems().add(ActionUtils.createMenuItem(action));
                    } else {
                        LOG.error("Can't handle action of type: " + consequenceAction.generatedAction().getClass().getName() + "\n\n" + consequenceAction.generatedAction());
                    }
                }
            }
            if (!menuButton.getItems().isEmpty()) {
                buttonList.add(menuButton);
            }
        }
        if (observableVersion.uncommitted()) {
            buttonList.add(newCancelComponentButton(observableVersion));
            if (observableVersion instanceof SemanticEntityVersion semanticEntityVersion) {
                Latest<EntityVersion> latestReferencedEntity = viewProperties.calculator().latest(semanticEntityVersion.referencedComponentNid());
                if (latestReferencedEntity.isPresentAnd(entityVersion -> entityVersion.committed())) {
                    buttonList.add(newCommitVersionButton(observableVersion));
                }
            } else {
                buttonList.add(newCommitVersionButton(observableVersion));
            }
            buttonList.add(newCancelTransactionButton(observableVersion));
            buttonList.add(newCommitTransactionButton(observableVersion));
        }
        if (!buttonList.isEmpty()) {
            if (buttonList.size() == 1) {
                stampLabel.setGraphic(buttonList.get(0));
            } else {
                HBox buttonsBox = new HBox();
                buttonsBox.getChildren().addAll(buttonList);
                stampLabel.setGraphic(buttonsBox);
            }

        }
        this.collapsiblePane.setGraphic(stampLabel);
        LOG.info(consequences.toString());
    }

    protected abstract Node makeCenterNode(OV version, ViewProperties viewProperties);

    private Button newCancelComponentButton(OV version) {
        Button button = new Button("cancel version");
        button.setOnAction(event -> {
            Transaction.forVersion(version).ifPresentOrElse(transaction -> {
                CancelVersionTask cancelVersionTask = switch (version) {
                    case ObservableConceptVersion observableConceptVersion -> new CancelVersionTask(observableConceptVersion.getVersionRecord());
                    case ObservablePatternVersion observablePatternVersion -> new CancelVersionTask(observablePatternVersion.getVersionRecord());
                    case ObservableSemanticVersion observableSemanticVersion -> new CancelVersionTask(observableSemanticVersion.getVersionRecord());
                    case ObservableStampVersion observableStampVersion -> new CancelVersionTask(observableStampVersion.getVersionRecord());
                    default -> throw new IllegalStateException("Unexpected value: " + version);
                };
                Future<Void> future = TinkExecutor.threadPool().submit(cancelVersionTask);
                TinkExecutor.threadPool().execute(() -> {
                    try {
                        future.get();
                    } catch (Exception e) {
                        AlertStreams.getRoot().dispatch(AlertObject.makeError(e));
                    }
                });
            }, () -> {
                AlertStreams.getRoot().dispatch(AlertObject.makeError("No transaction found. ", "Canceling Stamp",
                        new IllegalStateException("No transaction for version: " + version)));
                StampRecord stampRecord = (StampRecord) version.getVersionRecord().stamp();
                StampRecord canceledStamp = stampRecord.withAndBuild(stampRecord.lastVersion().withStateNid(State.CANCELED.nid()));
                Entity.provider().putEntity(canceledStamp);
            });
        });
        return button;
    }

    private Button newCommitVersionButton(OV version) {
        Button button = new Button("commit version");
        button.setOnAction(event -> {
            Transaction.forVersion(version).ifPresentOrElse(transaction -> {
                CommitVersionTask commitVersionTask = switch (version) {
                    case ObservableConceptVersion observableConceptVersion -> new CommitVersionTask(observableConceptVersion.getVersionRecord());
                    case ObservablePatternVersion observablePatternVersion -> new CommitVersionTask(observablePatternVersion.getVersionRecord());
                    case ObservableSemanticVersion observableSemanticVersion -> new CommitVersionTask(observableSemanticVersion.getVersionRecord());
                    case ObservableStampVersion observableStampVersion -> new CommitVersionTask(observableStampVersion.getVersionRecord());
                    default -> throw new IllegalStateException("Unexpected value: " + version);
                };
                Future<Void> future = TinkExecutor.threadPool().submit(commitVersionTask);
                TinkExecutor.threadPool().execute(() -> {
                    try {
                        future.get();
                    } catch (Exception e) {
                        AlertStreams.getRoot().dispatch(AlertObject.makeError(e));
                    }
                });
            }, () -> {
                AlertStreams.getRoot().dispatch(AlertObject.makeError("No transaction found. ", "Canceling Stamp",
                        new IllegalStateException("No transaction for version: " + version)));
                StampRecord stampRecord = (StampRecord) version.getVersionRecord().stamp();
                StampRecord canceledStamp = stampRecord.withAndBuild(stampRecord.lastVersion().withStateNid(State.CANCELED.nid()));
                Entity.provider().putEntity(canceledStamp);
            });
        });
        return button;
    }

    private Button newCancelTransactionButton(OV version) {
        Button button = new Button("cancel transaction");
        button.setOnAction(event -> {
            Transaction.forVersion(version).ifPresentOrElse(transaction -> {
                CancelTransactionTask cancelTransactionTask = new CancelTransactionTask(transaction);
                Future<Void> future = TinkExecutor.threadPool().submit(cancelTransactionTask);
                TinkExecutor.threadPool().execute(() -> {
                    try {
                        future.get();
                    } catch (Exception e) {
                        AlertStreams.getRoot().dispatch(AlertObject.makeError(e));
                    }
                });
            }, () -> {
                AlertStreams.getRoot().dispatch(AlertObject.makeError("No transaction found. ", "Canceling Stamp",
                        new IllegalStateException("No transaction for version: " + version)));
                StampRecord stampRecord = (StampRecord) version.getVersionRecord().stamp();
                StampRecord canceledStamp = stampRecord.withAndBuild(stampRecord.lastVersion().with().stateNid(State.CANCELED.nid()).time(Long.MIN_VALUE).build());
                Entity.provider().putEntity(canceledStamp);
            });
        });
        return button;
    }

    private Button newCommitTransactionButton(OV version) {
        Button button = new Button("commit transaction");
        button.setOnAction(event -> {
            Transaction.forVersion(version).ifPresentOrElse(transaction -> {
                CommitTransactionTask commitTransactionTask = new CommitTransactionTask(transaction);
                Future<Void> future = TinkExecutor.threadPool().submit(commitTransactionTask);
                TinkExecutor.threadPool().execute(() -> {
                    try {
                        future.get();
                    } catch (Exception e) {
                        AlertStreams.getRoot().dispatch(AlertObject.makeError(e));
                    }
                });
            }, () -> {
                AlertStreams.getRoot().dispatch(AlertObject.makeError("No transaction found. ", "Canceling Stamp",
                        new IllegalStateException("No transaction for version: " + version)));
                StampRecord stampRecord = (StampRecord) version.getVersionRecord().stamp();
                StampRecord canceledStamp = stampRecord.withAndBuild(stampRecord.lastVersion().withStateNid(State.CANCELED.nid()));
                Entity.provider().putEntity(canceledStamp);
            });
        });
        return button;
    }

    public TitledPane getVersionDetailsPane() {
        return collapsiblePane;
    }
}
