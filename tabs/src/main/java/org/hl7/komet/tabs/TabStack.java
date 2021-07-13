package org.hl7.komet.tabs;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import org.hl7.komet.framework.activity.ActivityStream;
import org.hl7.komet.framework.activity.ActivityStreamOption;
import org.hl7.komet.framework.activity.ActivityStreams;
import org.hl7.komet.framework.alerts.AlertStreams;
import org.hl7.komet.framework.graphics.Icon;
import org.hl7.komet.preferences.KometPreferences;
import org.hl7.komet.framework.view.ObservableViewNoOverride;
import org.hl7.tinkar.common.id.PublicIdStringKey;
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
            if (factory.defaultActivityStreamChoices().isEmpty()) {
                MenuItem newTabMenuItem = new MenuItem(factory.getMenuText(), factory.getMenuGraphic());
                newTabMenuItem.getStyleClass().add("add-tab-menu-item");
                newTabMenuItem.setOnAction(event -> {
                    KometNode kometNode = factory.create(windowView, parentNodePreferences,
                            null, null, AlertStreams.ROOT_ALERT_STREAM_KEY);
                    DetachableTab newTab = new DetachableTab(kometNode.getTitle().getValue(), kometNode.getNode());
                    newTab.setGraphic(kometNode.getTitleNode());
                    tabPane.getTabs().add(newTab);
                    tabPane.getSelectionModel().select(newTab);
                });
                menuButton.getItems().add(newTabMenuItem);
            } else {
                Menu newTabGroupMenu = new Menu(factory.getMenuText(), factory.getMenuGraphic());
                newTabGroupMenu.getStyleClass().add("add-tab-menu-item");
                menuButton.getItems().add(newTabGroupMenu);
                for (PublicIdStringKey<ActivityStream> activityStreamKey: factory.defaultActivityStreamChoices()) {
                    for (PublicIdStringKey<ActivityStreamOption> activityStreamOptionKey: factory.defaultOptionsForActivityStream(activityStreamKey)) {
                        MenuItem newTabMenuItem = new MenuItem(activityStreamOptionKey.getString());
                        newTabMenuItem.getStyleClass().add("add-tab-menu-item");
                        newTabMenuItem.setOnAction(event -> {
                            KometNode kometNode = factory.create(windowView, parentNodePreferences,
                                    activityStreamKey, activityStreamOptionKey, AlertStreams.ROOT_ALERT_STREAM_KEY);
                            DetachableTab newTab = new DetachableTab(kometNode.getTitle().getValue(), kometNode.getNode());
                            newTab.setGraphic(kometNode.getTitleNode());
                            tabPane.getTabs().add(newTab);
                            tabPane.getSelectionModel().select(newTab);
                        });
                        newTabGroupMenu.getItems().add(newTabMenuItem);
                        if (activityStreamOptionKey.equals(ActivityStreamOption.PUBLISH.keyForOption())) {
                            newTabMenuItem.setText("with " + activityStreamKey.getString() + " stream publication");
                            newTabMenuItem.setGraphic(Icon.makeIconGroup(ActivityStreamOption.PUBLISH.iconForOption(),
                                    factory.getMenuGraphic()));
                        } else if (activityStreamOptionKey.equals(ActivityStreamOption.SUBSCRIBE.keyForOption())) {
                            if (activityStreamKey.equals(ActivityStreams.UNLINKED)) {

                                // "* " added for sorting, will remove later.
                                newTabMenuItem.setText("* without subscription ");
                                newTabMenuItem.setGraphic(ActivityStreams.getActivityIcon(ActivityStreams.UNLINKED));
                            } else {
                                newTabMenuItem.setText("with " + activityStreamKey.getString() + " stream subscription");
                                newTabMenuItem.setGraphic(Icon.makeIconGroup(ActivityStreams.getActivityIcon(activityStreamKey),
                                        ActivityStreamOption.SUBSCRIBE.iconForOption()));
                            }
                        } else if (activityStreamOptionKey.equals(ActivityStreamOption.SYNCHRONIZE.keyForOption())) {
                            newTabMenuItem.setText("with " + activityStreamKey.getString() + " stream synchronization");
                            newTabMenuItem.setGraphic(Icon.makeIconGroup(factory.getMenuGraphic(),
                                    ActivityStreamOption.SYNCHRONIZE.iconForOption()));
                        }
                    }
                }
                newTabGroupMenu.getItems().sort((o1, o2) -> NaturalOrder.compareStrings(o1.getText(), o2.getText()));
                for (MenuItem menuItem: newTabGroupMenu.getItems()) {
                    if (menuItem.getText().startsWith("* ")) {
                        menuItem.setText(menuItem.getText().substring(2));
                    }
                }
            }
        });
        menuButton.getItems().sort((o1, o2) -> NaturalOrder.compareStrings(o1.getText(), o2.getText()));

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
