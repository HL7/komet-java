package org.hl7.komet.search;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.primitive.MutableIntObjectMap;
import org.eclipse.collections.impl.factory.primitive.IntObjectMaps;
import org.hl7.komet.framework.graphics.Icon;
import org.hl7.komet.framework.view.ViewMenuModel;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.komet.preferences.KometPreferences;
import org.hl7.tinkar.common.service.Executor;
import org.hl7.tinkar.common.util.text.NaturalOrder;
import org.hl7.tinkar.coordinate.stamp.calculator.LatestVersionSearchResult;
import org.hl7.tinkar.terms.EntityFacade;
import org.hl7.tinkar.terms.EntityProxy;

import java.net.URL;
import java.util.ResourceBundle;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class SearchPanelController implements ListChangeListener<TreeItem<Object>> {

    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private TextField queryString;
    @FXML
    private MenuButton navigationMenuButton;
    @FXML
    private Menu navigationCoordinateMenu;

    @FXML
    private BorderPane treeBorderPane;

    private SearchTreeView searchTreeView = new SearchTreeView();

    @FXML
    private ComboBox<RESULT_LAYOUT_OPTIONS> resultsLayoutCombo;
    private SearchNode searchNode;
    private ViewProperties viewProperties;
    private KometPreferences nodePreferences;
    private ViewMenuModel viewMenuModel;
    private TreeItem<Object> resultsRoot = new TreeItem<>("root");

    @FXML
    void initialize() {
        assert queryString != null : "fx:id=\"queryString\" was not injected: check your FXML file 'SearchPanel.fxml'.";
        assert treeBorderPane != null : "fx:id=\"treeBorderPane\" was not injected: check your FXML file 'SearchPanel.fxml'.";

        treeBorderPane.setCenter(searchTreeView);

        searchTreeView.getSelectionModel().getSelectedItems().addListener(this);
        searchTreeView.setRoot(resultsRoot);
        resultsRoot.setExpanded(true);
        searchTreeView.setShowRoot(false);
        searchTreeView.setFixedCellSize(USE_COMPUTED_SIZE);
        searchTreeView.setMinWidth(350);
        searchTreeView.setPrefWidth(350);
        searchTreeView.setMaxWidth(350);

        searchTreeView.setCellFactory(param -> new SearchResultCell());

        resultsLayoutCombo.getItems().addAll(RESULT_LAYOUT_OPTIONS.values());
        resultsLayoutCombo.getSelectionModel().select(RESULT_LAYOUT_OPTIONS.MATCHED_SEMANTIC_SCORE);
        resultsLayoutCombo.setOnAction(event -> doSearch(event));
    }

    @FXML
    void doSearch(ActionEvent event) {
        System.out.println("start search...");
        resultsRoot.getChildren().clear();
        if (queryString.getText() == null || queryString.getText().isEmpty()) {
            return;
        }
        Executor.threadPool().execute(() -> {
            try {
                TreeItem<Object> tempRoot = new TreeItem<>("Temp root");
                ImmutableList<LatestVersionSearchResult> results = viewProperties.calculator().search(queryString.getText(), 1000);
                System.out.println("Finished search. Hits: " + results.size());
                switch (resultsLayoutCombo.getSelectionModel().getSelectedItem()) {
                    case MATCHED_SEMANTIC_SCORE -> {
                        results = results.toSortedList((o1, o2) -> Float.compare(o1.score(), o2.score())).toImmutable();
                        for (LatestVersionSearchResult result : results) {
                            tempRoot.getChildren().add(new TreeItem<>(result));
                        }
                    }
                    case MATCHED_SEMANTIC_NATURAL_ORDER -> {
                        results = results.toSortedList((o1, o2) -> {
                            String string1 = (String) o1.latestVersion().get().fields().get(o1.fieldIndex());
                            String string2 = (String) o2.latestVersion().get().fields().get(o2.fieldIndex());
                            return NaturalOrder.compareStrings(string1, string2);
                        }).toImmutable();
                        for (LatestVersionSearchResult result : results) {
                            tempRoot.getChildren().add(new TreeItem<>(result));
                        }
                    }
                    case TOP_COMPONENT_NATURAL_ORDER -> {
                        populateTempRoot(tempRoot, results);
                        tempRoot.getChildren().sort((o1, o2) ->
                                NaturalOrder.compareStrings(o1.getValue().toString(),
                                        o2.getValue().toString()));
                        for (TreeItem child : tempRoot.getChildren()) {
                            child.getChildren().sort((o1, o2) -> NaturalOrder.compareStrings(o1.toString(), o2.toString()));
                        }
                    }

                    case TOP_COMPONENT_SEMANTIC_SCORE -> {
                        populateTempRoot(tempRoot, results);
                        for (TreeItem<Object> topItem : tempRoot.getChildren()) {
                            topItem.getChildren().sort((o1, o2) ->
                                    Float.compare(((LatestVersionSearchResult) o1.getValue()).score(),
                                            ((LatestVersionSearchResult) o2.getValue()).score()));
                        }
                        tempRoot.getChildren().sort((o1, o2) -> Float.compare(((LatestVersionSearchResult) o1.getChildren().get(0).getValue()).score(),
                                ((LatestVersionSearchResult) o2.getChildren().get(0).getValue()).score()));
                    }
                }
                Platform.runLater(() -> {
                    resultsRoot.getChildren().setAll(tempRoot.getChildren());
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void populateTempRoot(TreeItem<Object> tempRoot, ImmutableList<LatestVersionSearchResult> results) {
        MutableIntObjectMap<MutableList<LatestVersionSearchResult>> topNidMatchMap = IntObjectMaps.mutable.empty();
        for (LatestVersionSearchResult result : results) {
            topNidMatchMap.getIfAbsentPut(result.latestVersion().get().chronology().topEnclosingComponentNid(),
                    () -> Lists.mutable.empty()).add(result);
        }
        for (int topNid : topNidMatchMap.keySet().toArray()) {
            String topText = viewProperties.nodeView().calculator().getFullyQualifiedDescriptionTextWithFallbackOrNid(topNid);
            TreeItem<Object> topItem = new TreeItem<>(new NidTextRecord(topNid, topText));
            tempRoot.getChildren().add(topItem);
            topNidMatchMap.get(topNid).forEach(latestVersionSearchResult -> topItem.getChildren().add(new TreeItem<>(latestVersionSearchResult)));
            topItem.setExpanded(true);
        }
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
        this.searchNode.widthProperty().addListener((observable, oldValue, newValue) -> {
            setWidth(newValue.doubleValue());
            System.out.println("Width: " + newValue);
        });
        this.setWidth(searchNode.widthProperty().get());
    }

    private void menuUpdate() {
        if (!resultsRoot.getChildren().isEmpty()) {
            resultsRoot.getChildren().clear();
            doSearch(null);
        }
    }

    private void setWidth(Double width) {
        this.searchTreeView.setMinWidth(width);
        this.searchTreeView.setPrefWidth(width);
        this.searchTreeView.setMaxWidth(width);
        Platform.runLater(() -> {
            doSearch(null);
//            VirtualFlow<IndexedCell> vf = (VirtualFlow<IndexedCell>) this.searchTreeView.getChildrenUnmodifiable().get(0);
//            int cellCount = vf.getCellCount();
//            for (int i = 0; i < cellCount; i++) {
//                SearchResultCell cell = (SearchResultCell) vf.getCell(i);
//                Platform.runLater(() -> {
//                    cell.updateItem(cell.getItem(), cell.isEmpty(), width);
//                    cell.layout();
//                });
//            }
//            Platform.runLater(() -> this.searchTreeView.layout());
        });
    }

    @Override
    public void onChanged(Change<? extends TreeItem<Object>> c) {
        if (c.getList().isEmpty()) {
            // nothing to do...
        } else {
            MutableList<EntityFacade> selectedItems = Lists.mutable.withInitialCapacity(c.getList().size());
            for (TreeItem<Object> selectedItem : c.getList()) {
                if (selectedItem.getValue() instanceof LatestVersionSearchResult latestVersionSearchResult) {
                    selectedItems.add(latestVersionSearchResult.latestVersion().get().chronology());
                } else if (selectedItem.getValue() instanceof NidTextRecord nidTextRecord) {
                    selectedItems.add(EntityProxy.make(nidTextRecord.nid));
                }
            }
            this.searchNode.getActivityStream().dispatch(selectedItems.toImmutable());
        }
    }

    enum RESULT_LAYOUT_OPTIONS {
        MATCHED_SEMANTIC_NATURAL_ORDER("Matched semantic with lexicographic order"),
        MATCHED_SEMANTIC_SCORE("Matched semantic with score order"),
        TOP_COMPONENT_NATURAL_ORDER("Top component with lexicographic order"),
        TOP_COMPONENT_SEMANTIC_SCORE("Top component with score order");

        String menuText;

        RESULT_LAYOUT_OPTIONS(String menuText) {
            this.menuText = menuText;
        }

        @Override
        public String toString() {
            return menuText;
        }
    }

    protected record NidTextRecord(int nid, String text) {
    }
}