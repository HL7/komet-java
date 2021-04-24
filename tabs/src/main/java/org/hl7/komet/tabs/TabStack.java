package org.hl7.komet.tabs;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.layout.StackPane;
import org.hl7.komet.framework.NodeFactory;
import org.hl7.komet.framework.TaskLists;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.ServiceLoader;

public class TabStack extends StackPane {
    private static ServiceLoader<NodeFactory> nodeFactoryLoader = ServiceLoader.load(NodeFactory.class);
    final DetachableTabPane tabPane;
    final MenuButton newTabMenu;

    private TabStack(DetachableTabPane tabPane, MenuButton newTabMenu) {
        super(tabPane, newTabMenu);
        setAlignment(tabPane, Pos.TOP_LEFT);
        setAlignment(newTabMenu, Pos.TOP_LEFT);
        this.tabPane = tabPane;
        this.newTabMenu = newTabMenu;
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
    public static TabStack make() {

        return make(new DetachableTabPane());
    }

    public static TabStack make(DetachableTabPane tabPane) {
        MenuButton menuButton = new MenuButton("+");
        menuButton.getStyleClass().add("add-tab-menu");
        menuButton.setGraphic(new FontIcon());
        menuButton.setId("add-tab");
        nodeFactoryLoader.stream().forEach(nodeFactoryProvider -> {
            NodeFactory factory = nodeFactoryProvider.get();
            menuButton.getItems().add(new MenuItem(factory.getMenuText(), factory.getMenuGraphic()));
        });
        
        menuButton.getItems().add(new MenuItem("Remove tab area"));
        return new TabStack(tabPane, menuButton);
    }
}
