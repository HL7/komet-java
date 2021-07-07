package org.hl7.komet.framework;

import javafx.application.Platform;
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
import org.hl7.komet.framework.view.ObservableCoordinate;
import org.hl7.komet.framework.view.ViewMenuTask;
import org.hl7.tinkar.coordinate.view.calculator.ViewCalculator;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.List;

public class TopPanelFactory {
    public static Node make(ViewCalculator viewCalculator, ObservableCoordinate observableCoordinate) {
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
        Menu coordinatesMenu = new Menu("Coordinates");
        viewPropertiesButton.getItems().add(coordinatesMenu);
        viewPropertiesButton.setGraphic(new FontIcon());
        viewPropertiesButton.setId("detail-coordinates");
        gridPane.add(viewPropertiesButton, 0, 0, 1, 1);

        Platform.runLater(TaskWrapper.make(new ViewMenuTask(viewCalculator, observableCoordinate),
                (List<MenuItem> result) -> coordinatesMenu.getItems().addAll(result)));


        return gridPane;
    }
}
