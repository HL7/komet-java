package org.hl7.komet.search;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import org.eclipse.collections.api.list.ImmutableList;
import org.hl7.komet.framework.ExplorationNodeAbstract;
import org.hl7.komet.framework.TopPanelFactory;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.komet.preferences.KometPreferences;
import org.hl7.tinkar.coordinate.view.calculator.ViewCalculatorWithCache;
import org.hl7.tinkar.terms.EntityFacade;

public class SearchNode extends ExplorationNodeAbstract {
    protected static final String STYLE_ID = "search-node";
    protected static final String TITLE = "Search";

    private final BorderPane searchPane = new BorderPane();
    // TODO link entityFocus with list selection
    final SimpleObjectProperty<EntityFacade> entityFocusProperty  = new SimpleObjectProperty<>();;

    public SearchNode(ViewProperties viewProperties, KometPreferences nodePreferences) {
        super(viewProperties, nodePreferences);
             // TODO makeOverridableViewProperties should accept node preferences, and accept saved overrides

            Platform.runLater(() -> {
                this.searchPane.setCenter(new Label(titleProperty.getValue()));
                Node topPanel = TopPanelFactory.make(viewProperties, entityFocusProperty,
                        activityStreamKeyProperty(), optionForActivityStreamKeyProperty());
                this.searchPane.setTop(topPanel);
            });
    }
    @Override
    protected boolean showActivityStreamIcon() {
        return false;
    }

    @Override
    public void handleActivity(ImmutableList<EntityFacade> entities) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Node getNode() {
        return searchPane;
    }

    @Override
    public String getStyleId() {
        return STYLE_ID;
    }

    @Override
    public String getDefaultTitle() {
        return TITLE;
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