package org.hl7.komet.tabs;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import org.hl7.komet.view.ObservableViewNoOverride;
import org.hl7.komet.view.ViewProperties;
import org.hl7.tinkar.common.util.text.NaturalOrder;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import org.hl7.komet.framework.ExplorationNode;
import org.hl7.komet.framework.NodeFactory;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.ServiceLoader;
import java.util.concurrent.atomic.AtomicReference;

public class TabStack extends StackPane {
    public enum REMOVAL {ALLOW, DISALLOW }
    private static ServiceLoader<NodeFactory> nodeFactoryLoader = ServiceLoader.load(NodeFactory.class);
    final DetachableTabPane tabPane;
    final MenuButton newTabMenu;
    final REMOVAL allowRemoval;

    private TabStack(DetachableTabPane tabPane, MenuButton newTabMenu, REMOVAL allowRemoval) {
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

    public static TabStack make(REMOVAL allowRemoval, AtomicReference<ObservableViewNoOverride> windowViewReference) {
        return make(new DetachableTabPane(windowViewReference), allowRemoval, windowViewReference);
    }
    public static TabStack make(DetachableTabPane tabPane, AtomicReference<ObservableViewNoOverride> windowViewReference) {
        return make(tabPane, REMOVAL.ALLOW, windowViewReference);
    }
    public static TabStack make(DetachableTabPane tabPane, REMOVAL allowRemoval, AtomicReference<ObservableViewNoOverride> windowViewReference) {
        MenuButton menuButton = new MenuButton("+");
        menuButton.getStyleClass().add("add-tab-menu");
        menuButton.setGraphic(new FontIcon());
        menuButton.setId("add-tab");
        nodeFactoryLoader.stream().forEach(nodeFactoryProvider -> {
            NodeFactory factory = nodeFactoryProvider.get();
            MenuItem newTabMenuItem = new MenuItem(factory.getMenuText(), factory.getMenuGraphic());
            newTabMenuItem.getStyleClass().add("add-tab-menu-item");
            newTabMenuItem.setOnAction(event -> {
                ExplorationNode explorationNode = factory.create(windowViewReference);
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
        if (allowRemoval == REMOVAL.ALLOW) {
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
