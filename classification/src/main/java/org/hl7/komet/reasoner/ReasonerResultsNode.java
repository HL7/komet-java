package org.hl7.komet.reasoner;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.eclipse.collections.api.list.ImmutableList;
import org.hl7.komet.framework.ExplorationNodeAbstract;
import org.hl7.komet.framework.TopPanelFactory;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.komet.preferences.KometPreferences;
import org.hl7.tinkar.common.alert.AlertStreams;
import org.hl7.tinkar.common.service.TinkExecutor;
import org.hl7.tinkar.terms.EntityFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.hl7.tinkar.terms.TinkarTerm.EL_PLUS_PLUS_INFERRED_AXIOMS_PATTERN;
import static org.hl7.tinkar.terms.TinkarTerm.EL_PLUS_PLUS_STATED_AXIOMS_PATTERN;

public class ReasonerResultsNode extends ExplorationNodeAbstract {
    private static final Logger LOG = LoggerFactory.getLogger(ReasonerResultsNode.class);
    protected static final String STYLE_ID = "classification-results-node";
    protected static final String TITLE = "Reasoner Results";
    private final BorderPane contentPane = new BorderPane();
    private final HBox centerBox;

    public ReasonerResultsNode(ViewProperties viewProperties, KometPreferences nodePreferences) {
        super(viewProperties, nodePreferences);
        this.centerBox = new HBox(5, new Label("   reasoner "));

        Platform.runLater(() -> {
            TopPanelFactory.TopPanelParts topPanelParts = TopPanelFactory.make(viewProperties,
                    activityStreamKeyProperty, optionForActivityStreamKeyProperty, centerBox);
            this.contentPane.setTop(topPanelParts.topPanel());
            Platform.runLater(() -> {
                ArrayList<MenuItem> collectionMenuItems = new ArrayList<>();
                collectionMenuItems.add(new SeparatorMenuItem());
                MenuItem copySelectedItemsMenuItem = new MenuItem("Run reasoner");
                copySelectedItemsMenuItem.setOnAction(this::classify);
                collectionMenuItems.add(copySelectedItemsMenuItem);

                ObservableList<MenuItem> topMenuItems = topPanelParts.viewPropertiesMenuButton().getItems();
                topMenuItems.addAll(collectionMenuItems);
            });
        });
    }

    private void classify(ActionEvent actionEvent) {
        TinkExecutor.threadPool().execute(() -> {
            RunReasonerTask runReasonerTask =
                    new RunReasonerTask(getViewProperties().calculator(), EL_PLUS_PLUS_STATED_AXIOMS_PATTERN, EL_PLUS_PLUS_INFERRED_AXIOMS_PATTERN);
            Future<AxiomData> reasonerFuture = TinkExecutor.threadPool().submit(runReasonerTask);
            AxiomData axiomData = null;
            int statedCount = 0;
            try {
                axiomData = reasonerFuture.get();
                statedCount = axiomData.processedSemantics.get();
            } catch (InterruptedException | ExecutionException e) {
                AlertStreams.dispatchToRoot(e);
            }

            LOG.info("Stated axiom count: " + statedCount + " " + runReasonerTask.durationString());
        });
    }

    @Override
    public String getDefaultTitle() {
        return TITLE;
    }

    @Override
    public void handleActivity(ImmutableList<EntityFacade> entities) {
        // Nothing to do...
    }

    @Override
    public void revertAdditionalPreferences() {

    }

    @Override
    public String getStyleId() {
        return STYLE_ID;
    }

    @Override
    protected void saveAdditionalPreferences() {

    }

    @Override
    public Node getNode() {
        return contentPane;
    }

    @Override
    public void close() {

    }

    @Override
    public boolean canClose() {
        return false;
    }

    @Override
    public Class factoryClass() {
        return ReasonerResultsNodeFactory.class;
    }
}