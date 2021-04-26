package org.hl7.komet.tabs;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import org.hl7.tinkar.common.util.text.NaturalOrder;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import org.hl7.komet.framework.ExplorationNode;
import org.hl7.komet.framework.NodeFactory;
import org.hl7.komet.framework.TaskLists;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.ServiceLoader;

public class TabStack extends StackPane {
    private static ServiceLoader<NodeFactory> nodeFactoryLoader = ServiceLoader.load(NodeFactory.class);
    final DetachableTabPane tabPane;
    final MenuButton newTabMenu;
    final boolean allowRemoval;

    private TabStack(DetachableTabPane tabPane, MenuButton newTabMenu, boolean allowRemoval) {
        super(tabPane, newTabMenu);
        tabPane.setDetachableStack(this);
        setAlignment(tabPane, Pos.TOP_LEFT);
        setAlignment(newTabMenu, Pos.TOP_LEFT);
        this.tabPane = tabPane;
        this.newTabMenu = newTabMenu;
        this.allowRemoval = allowRemoval;
    }

    public ObservableList<Tab> getTabs() {
        return tabPane.getTabs();
    }

    public DetachableTabPane getTabPane() {
        return tabPane;
    }

    public SingleSelectionModel<Tab> getSelectionModel() {
        return tabPane.getSelectionModel();
    }

    public MenuButton getNewTabMenu() {
        return newTabMenu;
    }

    public static TabStack make(boolean allowRemoval) {
        return make(new DetachableTabPane(), allowRemoval);
    }
    public static TabStack make() {
        return make(new DetachableTabPane());
    }
    public static TabStack make(DetachableTabPane tabPane) {
        return make(tabPane, true);
    }
    public static TabStack make(DetachableTabPane tabPane, boolean allowRemoval) {
        MenuButton menuButton = new MenuButton("+");
        menuButton.getStyleClass().add("add-tab-menu");
        menuButton.setGraphic(new FontIcon());
        menuButton.setId("add-tab");
        nodeFactoryLoader.stream().forEach(nodeFactoryProvider -> {
            NodeFactory factory = nodeFactoryProvider.get();
            MenuItem newTabMenuItem = new MenuItem(factory.getMenuText(), factory.getMenuGraphic());
            newTabMenuItem.getStyleClass().add("add-tab-menu-item");
            newTabMenuItem.setOnAction(event -> {
                ExplorationNode explorationNode = factory.create();
                DetachableTab newTab = new DetachableTab(explorationNode.getTitle().getValue(), explorationNode.getNode());
                newTab.setGraphic(explorationNode.getTitleNode());
                tabPane.getTabs().add(newTab);
                tabPane.getSelectionModel().select(newTab);
            });
            menuButton.getItems().add(newTabMenuItem);
        });
        menuButton.getItems().sort(new NaturalOrder());

        FontIcon icon = new FontIcon();
        Label iconLabel = new Label("", icon);
        iconLabel.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        iconLabel.setId("remove-tab-area");
        if (allowRemoval) {
            menuButton.getItems().add(new SeparatorMenuItem());
            MenuItem removeTabArea = new MenuItem("Remove tab area", iconLabel);
            removeTabArea.getStyleClass().add("add-tab-menu-item");
            removeTabArea.setOnAction(event -> {
                Platform.runLater(() -> tabPane.removeFromParent());
            });
            menuButton.getItems().add(removeTabArea);
        }
        return new TabStack(tabPane, menuButton, allowRemoval);
    }
}
