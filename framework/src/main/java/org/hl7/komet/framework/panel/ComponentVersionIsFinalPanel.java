package org.hl7.komet.framework.panel;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import org.controlsfx.control.action.Action;
import org.controlsfx.control.action.ActionUtils;
import org.eclipse.collections.api.list.ImmutableList;
import org.hl7.komet.framework.PseudoClasses;
import org.hl7.komet.framework.graphics.Icon;
import org.hl7.komet.framework.rulebase.*;
import org.hl7.komet.framework.rulebase.statement.ObservationRecord;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.tinkar.common.util.time.DateTimeUtil;
import org.hl7.tinkar.entity.EntityVersion;
import org.hl7.tinkar.entity.StampEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hl7.komet.framework.StyleClasses.*;

public abstract class ComponentVersionIsFinalPanel<V extends EntityVersion> {
    private static final Logger LOG = LoggerFactory.getLogger(ComponentVersionIsFinalPanel.class);
    protected final BorderPane versionDetailsPane = new BorderPane();
    protected final TitledPane collapsiblePane = new TitledPane("version", versionDetailsPane);
    private final V version;
    private final ViewProperties viewProperties;

    public ComponentVersionIsFinalPanel(V version, ViewProperties viewProperties) {
        this.version = version;
        this.viewProperties = viewProperties;
        Node versionNode = makeCenterNode(version, viewProperties);
        if (versionNode != null) {
            BorderPane.setAlignment(versionNode, Pos.TOP_LEFT);
            versionNode.pseudoClassStateChanged(PseudoClasses.INACTIVE_PSEUDO_CLASS, !version.isActive());
        }
        this.versionDetailsPane.getStyleClass().add(COMPONENT_VERSION_BORDER_PANEL.toString());
        this.versionDetailsPane.setCenter(versionNode);
        //this.versionDetailsPane.setBottom(new StampPanel<V>(version, viewProperties));
        StampEntity stampEntity = version.stamp();
        Label stampLabel = new Label(stampEntity.state() + " as of " + DateTimeUtil.format(stampEntity.time()) +
                " on " + viewProperties.calculator().getPreferredDescriptionTextWithFallbackOrNid(stampEntity.pathNid()) +
                " in " + viewProperties.calculator().getPreferredDescriptionTextWithFallbackOrNid(stampEntity.moduleNid()) +
                " by " + viewProperties.calculator().getPreferredDescriptionTextWithFallbackOrNid(stampEntity.authorNid()));
        stampLabel.getStyleClass().add(STAMP_LABEL.toString());
        this.collapsiblePane.setText("");
        this.collapsiblePane.pseudoClassStateChanged(PseudoClasses.INACTIVE_PSEUDO_CLASS, !version.isActive());
        this.collapsiblePane.getStyleClass().add(COMPONENT_VERSION_PANEL.toString());
        ObservationRecord observation = new ObservationRecord(Topic.COMPONENT_FOCUSED, version, Measures.present());
        ObservationStore observationStore = ObservationStore.make(observation);
        ImmutableList<Consequence<?>> consequences = RuleBase.execute(observationStore, viewProperties.calculator());
        if (!consequences.isEmpty()) {
            MenuButton menuButton = new MenuButton("", Icon.EDIT_PENCIL.makeIcon());
            menuButton.getStyleClass().add(EDIT_COMPONENT_BUTTON.toString());
            for (Consequence<?> consequence : consequences) {
                if (consequence instanceof ConsequenceAction consequenceAction) {
                    if (consequenceAction.suggestedAction() instanceof Action action) {
                        menuButton.getItems().add(ActionUtils.createMenuItem(action));
                    } else {
                        LOG.error("Can't handle action of type: " + consequenceAction.suggestedAction().getClass().getName() + "\n\n" + consequenceAction.suggestedAction());
                    }
                }
            }
            if (!menuButton.getItems().isEmpty()) {
                stampLabel.setGraphic(menuButton);
            }
        }
        this.collapsiblePane.setGraphic(stampLabel);
        LOG.info(consequences.toString());
    }

    protected abstract Node makeCenterNode(V version, ViewProperties viewProperties);

    public TitledPane getVersionDetailsPane() {
        return collapsiblePane;
    }
}
