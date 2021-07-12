package org.hl7.komet.framework;

import javafx.application.Platform;
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
import org.hl7.komet.framework.graphics.Icon;
import org.hl7.komet.framework.view.ObservableCoordinate;
import org.hl7.komet.framework.view.ViewMenuTask;
import org.hl7.tinkar.common.id.PublicIdStringKey;
import org.hl7.tinkar.coordinate.view.calculator.ViewCalculator;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.List;

public class TopPanelFactory {
    public static Node make(ViewCalculator viewCalculator, ObservableCoordinate observableCoordinate,
                            SimpleObjectProperty<PublicIdStringKey<ActivityStream>> activityStreamKeyProperty,
                            SimpleObjectProperty<PublicIdStringKey<ActivityStreamOption>> optionForActivityStreamKeyProperty) {
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

        updateGridPane(activityStreamKeyProperty, optionForActivityStreamKeyProperty, gridPane, viewPropertiesButton);
        activityStreamKeyProperty.addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> updateGridPane(activityStreamKeyProperty, optionForActivityStreamKeyProperty, gridPane, viewPropertiesButton));
        });
        optionForActivityStreamKeyProperty.addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> updateGridPane(activityStreamKeyProperty, optionForActivityStreamKeyProperty, gridPane, viewPropertiesButton));
        });


        Platform.runLater(TaskWrapper.make(new ViewMenuTask(viewCalculator, observableCoordinate),
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

    private static void updateGridPane(SimpleObjectProperty<PublicIdStringKey<ActivityStream>> activityStreamKeyProperty, SimpleObjectProperty<PublicIdStringKey<ActivityStreamOption>> optionForActivityStreamKeyProperty, GridPane gridPane, MenuButton viewPropertiesButton) {
        gridPane.getChildren().clear();
        gridPane.add(viewPropertiesButton, 0, 0, 2, 1);

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
