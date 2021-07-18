package org.hl7.komet.framework;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import org.hl7.komet.executor.TaskWrapper;
import org.hl7.komet.framework.activity.ActivityStream;
import org.hl7.komet.framework.activity.ActivityStreamOption;
import org.hl7.komet.framework.activity.ActivityStreams;
import org.hl7.komet.framework.context.AddToContextMenu;
import org.hl7.komet.framework.context.AddToContextMenuSimple;
import org.hl7.komet.framework.graphics.Icon;
import org.hl7.komet.framework.view.ViewMenuTask;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.tinkar.common.id.PublicIdStringKey;
import org.hl7.tinkar.common.util.time.DateTimeUtil;
import org.hl7.tinkar.coordinate.view.calculator.ViewCalculatorWithCache;
import org.hl7.tinkar.terms.EntityFacade;

import java.util.List;
import java.util.function.Consumer;

public class TopPanelFactory {
    public static Node make(ViewProperties viewProperties,
                            SimpleObjectProperty<EntityFacade> entityFocusProperty,
                            SimpleObjectProperty<PublicIdStringKey<ActivityStream>> activityStreamKeyProperty,
                            SimpleObjectProperty<PublicIdStringKey<ActivityStreamOption>> optionForActivityStreamKeyProperty) {

        ViewCalculatorWithCache viewCalculator =
                ViewCalculatorWithCache.getCalculator(viewProperties.nodeView().getValue());

        GridPane gridPane = new GridPane();
        ColumnConstraints column0 = new ColumnConstraints();
        column0.setHgrow(Priority.NEVER);
        column0.setHalignment(HPos.LEFT);
        gridPane.getColumnConstraints().add(column0);

        RowConstraints row0 = new RowConstraints();
        row0.setVgrow(Priority.NEVER);
        row0.setValignment(VPos.CENTER);
        gridPane.getRowConstraints().add(row0);

        MenuButton viewPropertiesButton = new MenuButton();
        Menu coordinatesMenu = new Menu("Coordinates", Icon.COORDINATES.makeIcon());

        viewPropertiesButton.getItems().add(coordinatesMenu);
        viewPropertiesButton.setGraphic(Icon.VIEW.makeIcon());

        SimpleIntegerProperty selectionIndexProperty = new SimpleIntegerProperty();
        Runnable unlink = () -> { };
        AddToContextMenu[] contextMenuProviders = new AddToContextMenu[] { new AddToContextMenuSimple() };

        EntityLabelWithDragAndDrop entityLabel = EntityLabelWithDragAndDrop.make(viewProperties,
                entityFocusProperty, null, selectionIndexProperty, unlink,
                contextMenuProviders);

        updateGridPane(activityStreamKeyProperty, optionForActivityStreamKeyProperty, gridPane, viewPropertiesButton, entityLabel);
        activityStreamKeyProperty.addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> updateGridPane(activityStreamKeyProperty, optionForActivityStreamKeyProperty, gridPane,
                    viewPropertiesButton, entityLabel));
        });
        optionForActivityStreamKeyProperty.addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> updateGridPane(activityStreamKeyProperty, optionForActivityStreamKeyProperty, gridPane,
                    viewPropertiesButton, entityLabel));
        });

        Platform.runLater(TaskWrapper.make(new ViewMenuTask(viewCalculator, viewProperties.nodeView()),
                (List<MenuItem> result) -> coordinatesMenu.getItems().addAll(result)));

        Menu activityStreamMenu = new Menu("Activity stream", Icon.ACTIVITY.makeIcon());
        viewPropertiesButton.getItems().add(activityStreamMenu);
        for (PublicIdStringKey<ActivityStream> key: ActivityStreams.KEYS) {
            Menu optionsForStreamMenu = new Menu(key.getString(), ActivityStreams.getActivityIcon(key));
            activityStreamMenu.getItems().add(optionsForStreamMenu);
            for (ActivityStreamOption activityStreamOption: ActivityStreamOption.optionsForStream(key)) {
                MenuItem activityStreamOptionItem = new MenuItem(activityStreamOption.keyForOption().getString(),
                        activityStreamOption.iconForOption());
                optionsForStreamMenu.getItems().add(activityStreamOptionItem);
                activityStreamOptionItem.setOnAction(event -> {
                    activityStreamKeyProperty.set(key);
                    optionForActivityStreamKeyProperty.set(activityStreamOption.keyForOption());
                });
            }
        }
        return gridPane;
    }

    private static void updateGridPane(SimpleObjectProperty<PublicIdStringKey<ActivityStream>> activityStreamKeyProperty,
                                       SimpleObjectProperty<PublicIdStringKey<ActivityStreamOption>> optionForActivityStreamKeyProperty,
                                       GridPane gridPane,
                                       MenuButton viewPropertiesButton,
                                       EntityLabelWithDragAndDrop entityLabel) {
        gridPane.getChildren().clear();
        gridPane.add(viewPropertiesButton, 0, 0, 2, 1);

        GridPane.setHgrow(entityLabel, Priority.ALWAYS);
        gridPane.add(entityLabel, 2, 1, 2, 2);



        int activityStreamColumn = 0;
        int optionForActivityStreamColumn = 1;

        if (optionForActivityStreamKeyProperty.get().equals(ActivityStreamOption.PUBLISH.keyForOption())) {
            optionForActivityStreamColumn = 0;
            activityStreamColumn = 1;
        }

        if (activityStreamKeyProperty.get() != null) {
            gridPane.add(ActivityStreams.getActivityIcon(activityStreamKeyProperty.get()), activityStreamColumn, 1, 1, 1);
            gridPane.add(ActivityStreamOption.get(optionForActivityStreamKeyProperty.get()).iconForOption(), optionForActivityStreamColumn, 1, 1, 1);
        }
    }
}
