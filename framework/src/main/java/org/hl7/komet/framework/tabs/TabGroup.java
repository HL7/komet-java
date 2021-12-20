package org.hl7.komet.framework.tabs;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.hl7.komet.framework.KometNode;
import org.hl7.komet.framework.KometNodeFactory;
import org.hl7.komet.framework.activity.ActivityStream;
import org.hl7.komet.framework.activity.ActivityStreamOption;
import org.hl7.komet.framework.activity.ActivityStreams;
import org.hl7.komet.framework.graphics.Icon;
import org.hl7.komet.framework.preferences.Reconstructor;
import org.hl7.komet.framework.view.ObservableViewNoOverride;
import org.hl7.komet.framework.window.WindowComponent;
import org.hl7.komet.preferences.KometPreferences;
import org.hl7.komet.preferences.KometPreferencesImpl;
import org.hl7.tinkar.common.alert.AlertObject;
import org.hl7.tinkar.common.alert.AlertStreams;
import org.hl7.tinkar.common.binary.Encodable;
import org.hl7.tinkar.common.id.PublicIdStringKey;
import org.hl7.tinkar.common.util.text.NaturalOrder;
import org.kordamp.ikonli.javafx.FontIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.ServiceLoader;
import java.util.UUID;

import static org.hl7.komet.framework.KometNodeFactory.KOMET_NODES;

public class TabGroup extends StackPane implements WindowComponent {
    private static final Logger LOG = LoggerFactory.getLogger(TabGroup.class);
    private static ServiceLoader<KometNodeFactory> nodeFactoryLoader = ServiceLoader.load(KometNodeFactory.class);
    final DetachableTabPane tabPane;
    final MenuButton newTabMenu;
    final REMOVAL allowRemoval;
    final ObservableViewNoOverride windowView;
    final KometPreferences nodePreferences;

    private TabGroup(DetachableTabPane tabPane, MenuButton newTabMenu, REMOVAL allowRemoval,
                     ObservableViewNoOverride windowView, KometPreferences nodePreferences) {
        super(tabPane, newTabMenu);
        tabPane.setDetachableStack(this);
        setAlignment(tabPane, Pos.TOP_LEFT);
        setAlignment(newTabMenu, Pos.TOP_LEFT);
        this.tabPane = tabPane;
        this.newTabMenu = newTabMenu;
        this.allowRemoval = allowRemoval;
        this.windowView = windowView;
        this.nodePreferences = nodePreferences;
    }

    @Reconstructor
    public static TabGroup create(ObservableViewNoOverride windowView, KometPreferences nodePreferences) {
        if (nodePreferences.hasKey(WindowComponentKeys.INITIALIZED)) {
            // Restore from preferences...
            REMOVAL allowRemoval = REMOVAL.valueOf(nodePreferences.get(TabGroupKeys.ALLOW_REMOVAL, "ALLOW"));
            DetachableTabPane detachableTabPane = new DetachableTabPane();
            for (String preferencesNode : nodePreferences.getList(WindowComponentKeys.CHILDREN)) {
                KometPreferences childPreferences = nodePreferences.node(KOMET_NODES + preferencesNode);
                childPreferences.get(WindowComponentKeys.FACTORY_CLASS).ifPresent(factoryClassName -> {
                    try {
                        Class<?> objectClass = Class.forName(factoryClassName);
                        Class<? extends Annotation> annotationClass = Reconstructor.class;
                        Object[] parameters = new Object[]{windowView, childPreferences};
                        KometNode kometNode = (KometNode) Encodable.decode(objectClass, annotationClass, parameters);
                        DetachableTab componentTab = new DetachableTab(kometNode);
                        detachableTabPane.getTabs().add(componentTab);
                    } catch (Exception e) {
                        AlertStreams.getRoot().dispatch(AlertObject.makeError(e));
                    }
                });
            }
            return make(detachableTabPane, allowRemoval, windowView, nodePreferences);
        }
        throw new UnsupportedOperationException("Should only be called on restore. ");
    }

    private static TabGroup make(DetachableTabPane tabPane, REMOVAL allowRemoval, ObservableViewNoOverride windowView,
                                 KometPreferences nodePreferences) {
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
                    KometNode kometNode = factory.create(windowView,
                            null, null, AlertStreams.ROOT_ALERT_STREAM_KEY);
                    DetachableTab newTab = new DetachableTab(kometNode);
                    newTab.setGraphic(kometNode.getTitleNode());
                    tabPane.getTabs().add(newTab);
                    tabPane.getSelectionModel().select(newTab);
                });
                menuButton.getItems().add(newTabMenuItem);
            } else {
                Menu newTabGroupMenu = new Menu(factory.getMenuText(), factory.getMenuGraphic());
                newTabGroupMenu.getStyleClass().add("add-tab-menu-item");
                menuButton.getItems().add(newTabGroupMenu);
                for (PublicIdStringKey<ActivityStream> activityStreamKey : factory.defaultActivityStreamChoices()) {
                    for (PublicIdStringKey<ActivityStreamOption> activityStreamOptionKey : factory.defaultOptionsForActivityStream(activityStreamKey)) {
                        MenuItem newTabMenuItem = new MenuItem(activityStreamOptionKey.getString());
                        newTabMenuItem.getStyleClass().add("add-tab-menu-item");
                        newTabMenuItem.setOnAction(event -> {
                            KometNode kometNode = factory.create(windowView,
                                    activityStreamKey, activityStreamOptionKey, AlertStreams.ROOT_ALERT_STREAM_KEY);
                            DetachableTab newTab = new DetachableTab(kometNode);
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
                for (MenuItem menuItem : newTabGroupMenu.getItems()) {
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
        return new TabGroup(tabPane, menuButton, allowRemoval, windowView, nodePreferences);
    }

    public static TabGroup create(ObservableViewNoOverride windowView, REMOVAL allowRemoval) {
        return TabGroup.make(new DetachableTabPane(), allowRemoval, windowView,
                KometPreferencesImpl.getConfigurationRootPreferences().node(KOMET_NODES + "TabGroup_" + UUID.randomUUID()));
    }

    public DetachableTabPane tabPane() {
        return tabPane;
    }

    @Override
    public ObservableViewNoOverride windowView() {
        return this.windowView;
    }

    @Override
    public KometPreferences nodePreferences() {
        return this.nodePreferences;
    }

    @Override
    public ImmutableList<WindowComponent> children() {
        MutableList<WindowComponent> children = Lists.mutable.empty();
        for (Tab tab : tabPane.getTabs()) {
            if (tab instanceof DetachableTab detachableTab) {
                children.add(detachableTab.getKometNode());
            }
        }
        return children.toImmutable();
    }

    @Override
    public void saveConfiguration() {
        try {
            nodePreferences.put(WindowComponentKeys.INITIALIZED, "true");
            nodePreferences.put(WindowComponentKeys.FACTORY_CLASS, factoryClass().getName());
            List<String> childrenPreferenceNodes = children().stream().map(windowComponent -> windowComponent.nodePreferences().name()).toList();
            this.nodePreferences.putList(WindowComponentKeys.CHILDREN, childrenPreferenceNodes);
            this.nodePreferences.sync();
            try {
                for (WindowComponent child : children()) {
                    try {
                        child.saveConfiguration();
                    } catch (UnsupportedOperationException e) {
                        LOG.warn("Save " + child.getClass().getName() + " is not supported. ");
                    } catch (Throwable e) {
                        AlertStreams.getRoot().dispatch(AlertObject.makeError(e));
                    }
                }
            } catch (UnsupportedOperationException e) {
                LOG.warn("children() is not supported for: " + this.getClass().getName());
            } catch (Throwable e) {
                AlertStreams.getRoot().dispatch(AlertObject.makeError(e));
            }
        } catch (Throwable e) {
            AlertStreams.getRoot().dispatch(AlertObject.makeError(e));
        }
    }

    @Override
    public Node getNode() {
        return this;
    }

    @Override
    public Class factoryClass() {
        return TabGroup.class;
    }

    public ObservableList<Tab> getTabs() {
        return tabPane.getTabs();
    }

    public SingleSelectionModel<Tab> getSelectionModel() {
        return tabPane.getSelectionModel();
    }

    public MenuButton getNewTabMenu() {
        return newTabMenu;
    }

    public String getScope() {
        return tabPane.getScope();
    }

    public void setScope(String scope) {
        tabPane.setScope(scope);
    }

    public Callback<TabGroup, Scene> getSceneFactory() {
        return tabPane.getSceneFactory();
    }

    public void setSceneFactory(Callback<TabGroup, Scene> sceneFactory) {
        tabPane.setSceneFactory(sceneFactory);
    }

    public Callback<Stage, Window> getStageOwnerFactory() {
        return tabPane.getStageOwnerFactory();
    }

    public void setStageOwnerFactory(Callback<Stage, Window> stageOwnerFactory) {
        tabPane.setStageOwnerFactory(stageOwnerFactory);
    }

    public void setCloseIfEmpty(boolean closeIfEmpty) {
        tabPane.setCloseIfEmpty(closeIfEmpty);
    }

    public TabGroupFactory getTabGroupFactory() {
        return tabPane.getTabGroupFactory();
    }

    public void setTabGroupFactory(TabGroupFactory detachableTabPaneFactory) {
        tabPane.setTabGroupFactory(detachableTabPaneFactory);
    }

    public TabPane.TabClosingPolicy getTabClosingPolicy() {
        return tabPane.getTabClosingPolicy();
    }

    public void setTabClosingPolicy(TabPane.TabClosingPolicy value) {
        tabPane.setTabClosingPolicy(value);
    }

    public KometPreferences getParentNodePreferences() {
        return this.nodePreferences.parent();
    }

    public enum REMOVAL {ALLOW, DISALLOW}

    public enum TabGroupKeys {
        ALLOW_REMOVAL
    }
}
