package org.hl7.komet.search;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import org.eclipse.collections.api.list.ImmutableList;
import org.hl7.komet.framework.graphics.Icon;
import org.hl7.komet.framework.view.ViewMenuModel;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.komet.preferences.KometPreferences;
import org.hl7.tinkar.common.service.Executor;
import org.hl7.tinkar.coordinate.stamp.calculator.LatestVersionSearchResult;
import org.hl7.tinkar.terms.EntityFacade;

import java.net.URL;
import java.util.ResourceBundle;

public class SearchPanelController implements ListChangeListener<LatestVersionSearchResult> {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField queryString;

    @FXML
    private MenuButton navigationMenuButton;

    @FXML
    private ListView<LatestVersionSearchResult> resultsListView;

    @FXML
    private Menu navigationCoordinateMenu;


    private SearchNode searchNode;
    private ViewProperties viewProperties;
    private KometPreferences nodePreferences;
    private ViewMenuModel viewMenuModel;

    @FXML
    void initialize() {
        assert queryString != null : "fx:id=\"queryString\" was not injected: check your FXML file 'SearchPanel.fxml'.";
        assert resultsListView != null : "fx:id=\"resultsListView\" was not injected: check your FXML file 'SearchPanel.fxml'.";
        resultsListView.getSelectionModel().getSelectedItems().addListener(this);
        resultsListView.setCellFactory(new Callback<ListView<LatestVersionSearchResult>,
                                               ListCell<LatestVersionSearchResult>>() {
                                           @Override
                                           public ListCell<LatestVersionSearchResult> call(ListView<LatestVersionSearchResult> list) {
                                               return new SearchResultCell();
                                           }
                                       }
        );
    }

    public void setProperties(SearchNode searchNode, ViewProperties viewProperties, KometPreferences nodePreferences) {
        this.searchNode = searchNode;
        this.viewProperties = viewProperties;
        this.nodePreferences = nodePreferences;
        this.navigationMenuButton.setGraphic(Icon.VIEW.makeIcon());
        this.navigationCoordinateMenu.setGraphic(Icon.COORDINATES.makeIcon());
        this.viewMenuModel = new ViewMenuModel(viewProperties, navigationMenuButton, navigationCoordinateMenu);
        this.viewProperties.nodeView().addListener((observable, oldValue, newValue) -> {
            menuUpdate();
        });
    }

    private void menuUpdate() {
        if (resultsListView.getItems().isEmpty()) {
            resultsListView.getItems().clear();
            doSearch(null);
        }
    }


    @FXML
    void doSearch(ActionEvent event) {
        System.out.println("start search...");
        resultsListView.getItems().clear();
        Executor.threadPool().execute(() -> {
            try {
                ImmutableList<LatestVersionSearchResult> results = viewProperties.calculator().search(queryString.getText(), 1000);
                System.out.println("Finished search. Hits: " + results.size());
                Platform.runLater(() -> {
                    resultsListView.getItems().setAll(results.castToList());
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onChanged(Change<? extends LatestVersionSearchResult> c) {
        if (c.getList().isEmpty()) {
            // nothing to do...
        } else {
            EntityFacade[] selectedItems = new EntityFacade[c.getList().size()];
            for (int i = 0; i < selectedItems.length; i++) {
                selectedItems[i] = c.getList().get(i).latestVersion().get().chronology();
            }
            this.searchNode.getActivityStream().dispatch(selectedItems);
        }
    }
}