package org.hl7.komet.search;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import org.eclipse.collections.api.list.ImmutableList;
import org.hl7.komet.framework.ExplorationNodeAbstract;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.komet.preferences.KometPreferences;
import org.hl7.tinkar.terms.EntityFacade;

import java.io.IOException;

public class SearchNode extends ExplorationNodeAbstract {
    protected static final String STYLE_ID = "search-node";
    protected static final String TITLE = "Search";
    // TODO link entityFocus with list selection
    final SimpleObjectProperty<EntityFacade> entityFocusProperty = new SimpleObjectProperty<>();
    final SearchPanelController controller;
    private final BorderPane searchPane = new BorderPane();

    // TODO add option to send semantic or enclosing top component to activity stream...
    public SearchNode(ViewProperties viewProperties, KometPreferences nodePreferences) {
        super(viewProperties, nodePreferences);
        // TODO makeOverridableViewProperties should accept node preferences, and accept saved overrides
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/hl7/komet/search/SearchPanel.fxml"));
            this.searchPane.setCenter(loader.load());
            this.controller = loader.getController();

            Platform.runLater(() -> {
                this.controller.setProperties(this, viewProperties, nodePreferences);
                this.searchPane.setTop(null);
            });
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    protected boolean showActivityStreamIcon() {
        return false;
    }

    @Override
    public String getDefaultTitle() {
        return TITLE;
    }

    @Override
    public void handleActivity(ImmutableList<EntityFacade> entities) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getStyleId() {
        return STYLE_ID;
    }

    @Override
    public Node getNode() {
        return searchPane;
    }

    @Override
    public void close() {

    }

    @Override
    public boolean canClose() {
        return false;
    }

    @Override
    public void savePreferences() {

    }

    @Override
    public void revertPreferences() {

    }
}