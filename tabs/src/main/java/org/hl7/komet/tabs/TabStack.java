package org.hl7.komet.tabs;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import org.hl7.komet.framework.activity.ActivityStreams;
import org.hl7.komet.framework.alerts.AlertStreams;
import org.hl7.komet.preferences.KometPreferences;
import org.hl7.komet.framework.view.ObservableViewNoOverride;
import org.hl7.tinkar.common.util.text.NaturalOrder;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import org.hl7.komet.framework.KometNode;
import org.hl7.komet.framework.KometNodeFactory;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.ServiceLoader;

public class TabStack extends StackPane {
    public enum REMOVAL {ALLOW, DISALLOW }
    private static ServiceLoader<KometNodeFactory> nodeFactoryLoader = ServiceLoader.load(KometNodeFactory.class);
    final DetachableTabPane tabPane;
    final MenuButton newTabMenu;
    final REMOVAL allowRemoval;
    final KometPreferences parentNodePreferences;

    private TabStack(DetachableTabPane tabPane, MenuButton newTabMenu, REMOVAL allowRemoval,
                     KometPreferences parentNodePreferences) {
        super(tabPane, newTabMenu);
        tabPane.setDetachableStack(this);
        setAlignment(tabPane, Pos.TOP_LEFT);
        setAlignment(newTabMenu, Pos.TOP_LEFT);
        this.tabPane = tabPane;
        this.newTabMenu = newTabMenu;
        this.allowRemoval = allowRemoval;
        this.parentNodePreferences = parentNodePreferences;
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

    public static TabStack make(REMOVAL allowRemoval, ObservableViewNoOverride windowView,
                                KometPreferences parentNodePreferences) {
        return make(new DetachableTabPane(windowView, parentNodePreferences), allowRemoval, windowView, parentNodePreferences);
    }
    public static TabStack make(DetachableTabPane tabPane, ObservableViewNoOverride windowView,
                                KometPreferences parentNodePreferences) {
        return make(tabPane, REMOVAL.ALLOW, windowView, parentNodePreferences);
    }
    public static TabStack make(DetachableTabPane tabPane, REMOVAL allowRemoval, ObservableViewNoOverride windowView,
                                KometPreferences parentNodePreferences) {
        MenuButton menuButton = new MenuButton("+");
        menuButton.getStyleClass().add("add-tab-menu");
        menuButton.setGraphic(new FontIcon());
        menuButton.setId("add-tab");
        nodeFactoryLoader.stream().forEach(nodeFactoryProvider -> {
            KometNodeFactory factory = nodeFactoryProvider.get();
            MenuItem newTabMenuItem = new MenuItem(factory.getMenuText(), factory.getMenuGraphic());
            newTabMenuItem.getStyleClass().add("add-tab-menu-item");
            newTabMenuItem.setOnAction(event -> {
                KometNode kometNode = factory.create(windowView, parentNodePreferences,
                        ActivityStreams.UNLINKED, AlertStreams.ROOT_ALERT_STREAM_KEY);
                DetachableTab newTab = new DetachableTab(kometNode.getTitle().getValue(), kometNode.getNode());
                newTab.setGraphic(kometNode.getTitleNode());
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
        return new TabStack(tabPane, menuButton, allowRemoval, parentNodePreferences);
    }
}
