package org.hl7.komet.framework;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import org.hl7.komet.framework.activity.ActivityStream;
import org.hl7.komet.framework.activity.ActivityStreamOption;
import org.hl7.komet.framework.activity.ActivityStreams;
import org.hl7.komet.framework.context.AddToContextMenu;
import org.hl7.komet.framework.context.AddToContextMenuSimple;
import org.hl7.komet.framework.controls.EntityLabelWithDragAndDrop;
import org.hl7.komet.framework.graphics.Icon;
import org.hl7.komet.framework.view.ViewMenuModel;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.tinkar.common.id.PublicIdStringKey;
import org.hl7.tinkar.coordinate.view.calculator.ViewCalculatorWithCache;
import org.hl7.tinkar.terms.EntityFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;

public class TopPanelFactory {
    private static final Logger LOG = LoggerFactory.getLogger(TopPanelFactory.class);

    public static Node make(ViewProperties viewProperties,
                            SimpleObjectProperty<EntityFacade> entityFocusProperty,
                            SimpleObjectProperty<PublicIdStringKey<ActivityStream>> activityStreamKeyProperty,
                            SimpleObjectProperty<PublicIdStringKey<ActivityStreamOption>> optionForActivityStreamKeyProperty,
                            boolean focusOnActivity) {

        ViewCalculatorWithCache viewCalculator =
                ViewCalculatorWithCache.getCalculator(viewProperties.nodeView().getValue());

        SimpleBooleanProperty focusOnActivityProperty = new SimpleBooleanProperty(focusOnActivity);
        GridPane gridPane = new GridPane();
        gridPane.getStyleClass().add("top-panel");

        MenuButton viewPropertiesMenuButton = new MenuButton();
        Menu coordinatesMenu = new Menu("Coordinates", Icon.COORDINATES.makeIcon());

        ViewMenuModel viewMenuModel = new ViewMenuModel(viewProperties, viewPropertiesMenuButton, coordinatesMenu);
        viewPropertiesMenuButton.getProperties().put("viewMenuModel", viewMenuModel);
        viewPropertiesMenuButton.getItems().add(coordinatesMenu);
        viewPropertiesMenuButton.setGraphic(Icon.VIEW.makeIcon());

        SimpleIntegerProperty selectionIndexProperty = new SimpleIntegerProperty();
        Runnable unlink = () -> {
        };
        AddToContextMenu[] contextMenuProviders = new AddToContextMenu[]{new AddToContextMenuSimple()};

        EntityLabelWithDragAndDrop entityLabel = EntityLabelWithDragAndDrop.make(viewProperties,
                entityFocusProperty, null, selectionIndexProperty, unlink,
                contextMenuProviders);
        Menu activityStreamMenu = new Menu("Activity stream", Icon.ACTIVITY.makeIcon());

        updateGridPane(activityStreamKeyProperty, optionForActivityStreamKeyProperty, gridPane, viewPropertiesMenuButton, activityStreamMenu, focusOnActivityProperty, entityLabel);
        activityStreamKeyProperty.addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> updateGridPane(activityStreamKeyProperty, optionForActivityStreamKeyProperty, gridPane,
                    viewPropertiesMenuButton, activityStreamMenu, focusOnActivityProperty, entityLabel));
        });
        optionForActivityStreamKeyProperty.addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> updateGridPane(activityStreamKeyProperty, optionForActivityStreamKeyProperty, gridPane,
                    viewPropertiesMenuButton, activityStreamMenu, focusOnActivityProperty, entityLabel));
        });

        viewPropertiesMenuButton.getItems().add(activityStreamMenu);
        entityFocusProperty.addListener((observable, oldValue, newValue) -> {
            if (focusOnActivityProperty.get()) {
                if (!ScreenInfo.isMousePressed() & !ScreenInfo.mouseWasDragged()) {
                    selectTab(gridPane);
                } else {
                    Timer timer = new Timer();

                    timer.schedule(new TimerTask() {
                        boolean mouseWasDragged = false;

                        @Override
                        public void run() {
                            if (ScreenInfo.isMousePressed()) {
                                // come back later and check. ;
                                //LOG.info("Mouse is pressed. Come back later and check again. ");
                                mouseWasDragged = ScreenInfo.mouseWasDragged();
                                if (mouseWasDragged) {
                                    //LOG.info("Mouse was dragged.");
                                }
                            } else {
                                if (mouseWasDragged || ScreenInfo.mouseWasDragged()) {
                                    // Don't focus if from a drag event.
                                    //LOG.info("No focus change because mouse was dragged.");
                                } else {
                                    Platform.runLater(() -> selectTab(gridPane));
                                }
                                timer.cancel();
                                timer.purge();
                            }
                        }
                    }, 500, 500);
                }
            }
        });
        // show the current activity stream at the top
        return gridPane;
    }

    private static void selectTab(GridPane gridPane) {
        Parent parentNode = gridPane.getParent();
        Node tabContentRegion = null;

        while (parentNode != null && !(parentNode instanceof TabPane)) {
            parentNode = parentNode.getParent();
            if (parentNode.getStyleClass().contains("tab-content-area")) {
                tabContentRegion = parentNode;
            }
        }
        if (parentNode != null && parentNode instanceof TabPane tabPane) {
            ObservableList<Tab> tabList = tabPane.getTabs();

            for (Tab t : tabList) {
                if (t.getContent().getParent().equals(tabContentRegion)) {
                    tabPane.getSelectionModel().select(t);
                    break;
                }
            }
        }
    }

    private static void updateGridPane(SimpleObjectProperty<PublicIdStringKey<ActivityStream>> activityStreamKeyProperty,
                                       SimpleObjectProperty<PublicIdStringKey<ActivityStreamOption>> optionForActivityStreamKeyProperty,
                                       GridPane gridPane,
                                       MenuButton viewPropertiesButton,
                                       Menu activityStreamMenu,
                                       SimpleBooleanProperty focusOnActivity,
                                       EntityLabelWithDragAndDrop entityLabel) {
        gridPane.getChildren().clear();
        gridPane.add(viewPropertiesButton, 0, 0, 2, 1);

        GridPane.setHgrow(entityLabel, Priority.ALWAYS);
        GridPane.setVgrow(entityLabel, Priority.ALWAYS);
        GridPane.setFillHeight(entityLabel, true);
        GridPane.setFillWidth(entityLabel, true);
        entityLabel.setMaxWidth(Double.MAX_VALUE);
        entityLabel.setPrefWidth(Double.MAX_VALUE);
        entityLabel.setMaxHeight(Double.MAX_VALUE);
        entityLabel.setAlignment(Pos.TOP_LEFT);
        GridPane.setValignment(entityLabel, VPos.TOP);

        gridPane.add(entityLabel, 2, 0, 4, 2);

        activityStreamMenu.getItems().clear();
        MenuItem currentActivityMenuItem = new MenuItem();
        activityStreamMenu.getItems().add(currentActivityMenuItem);

        if (activityStreamKeyProperty.get().equals(ActivityStreams.UNLINKED)) {
            currentActivityMenuItem.setGraphic(Icon.makeIconGroup(
                    ActivityStreamOption.get(optionForActivityStreamKeyProperty.get()).iconForOption(),
                    ActivityStreams.getActivityIcon(activityStreamKeyProperty.get())));
            currentActivityMenuItem.setText("Unlinked");
        } else if (optionForActivityStreamKeyProperty.get().equals(ActivityStreamOption.PUBLISH.keyForOption())) {
            currentActivityMenuItem.setGraphic(Icon.makeIconGroup(ActivityStreams.getActivityIcon(activityStreamKeyProperty.get()),
                    ActivityStreamOption.get(optionForActivityStreamKeyProperty.get()).iconForOption()));
            currentActivityMenuItem.setText("Publishing to " + activityStreamKeyProperty.get().getString());
        } else if (optionForActivityStreamKeyProperty.get().equals(ActivityStreamOption.SYNCHRONIZE.keyForOption())) {
            currentActivityMenuItem.setGraphic(Icon.makeIconGroup(ActivityStreams.getActivityIcon(activityStreamKeyProperty.get()),
                    ActivityStreamOption.get(optionForActivityStreamKeyProperty.get()).iconForOption()));
            currentActivityMenuItem.setText("Synchronizing with " + activityStreamKeyProperty.get().getString());
        } else if (optionForActivityStreamKeyProperty.get().equals(ActivityStreamOption.SUBSCRIBE.keyForOption())) {
            currentActivityMenuItem.setGraphic(Icon.makeIconGroup(ActivityStreams.getActivityIcon(activityStreamKeyProperty.get()),
                    ActivityStreamOption.get(optionForActivityStreamKeyProperty.get()).iconForOption()));
            currentActivityMenuItem.setText("Subscribed to " + activityStreamKeyProperty.get().getString());
        } else {
            throw new IllegalStateException(optionForActivityStreamKeyProperty.get().toString());
        }
        activityStreamMenu.getItems().add(new SeparatorMenuItem());
        MenuItem focusOnActivityMenuItem = new MenuItem("Focus on activity");
        focusOnActivityMenuItem.setOnAction(event -> {
            focusOnActivity.set(!focusOnActivity.get());
            Platform.runLater(() -> updateGridPane(activityStreamKeyProperty, optionForActivityStreamKeyProperty, gridPane,
                    viewPropertiesButton, activityStreamMenu, focusOnActivity, entityLabel));
        });
        if (focusOnActivity.get()) {
            focusOnActivityMenuItem.setText("Focus tab on activity");
            focusOnActivityMenuItem.setGraphic(Icon.EYE.makeIcon());
        } else {
            focusOnActivityMenuItem.setText("Don't focus tab on activity");
            focusOnActivityMenuItem.setGraphic(Icon.EYE_SLASH.makeIcon());
        }

        activityStreamMenu.getItems().add(focusOnActivityMenuItem);
        activityStreamMenu.getItems().add(new SeparatorMenuItem());

        for (PublicIdStringKey<ActivityStream> key : ActivityStreams.KEYS) {
            Menu optionsForStreamMenu = new Menu(key.getString(), ActivityStreams.getActivityIcon(key));
            activityStreamMenu.getItems().add(optionsForStreamMenu);
            for (ActivityStreamOption activityStreamOption : ActivityStreamOption.optionsForStream(key)) {
                MenuItem activityStreamOptionItem = new MenuItem(activityStreamOption.keyForOption().getString(),
                        activityStreamOption.iconForOption());
                optionsForStreamMenu.getItems().add(activityStreamOptionItem);
                activityStreamOptionItem.setOnAction(event -> {
                    activityStreamKeyProperty.set(key);
                    optionForActivityStreamKeyProperty.set(activityStreamOption.keyForOption());
                });
            }
        }

    }
}
