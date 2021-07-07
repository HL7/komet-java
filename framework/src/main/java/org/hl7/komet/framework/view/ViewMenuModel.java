package org.hl7.komet.framework.view;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.MapChangeListener;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Shape;
import org.hl7.komet.executor.TaskWrapper;
import org.hl7.komet.framework.graphics.Icon;
import org.hl7.komet.framework.temp.FxGet;
import org.hl7.tinkar.common.id.PublicIdStringKey;
import org.hl7.tinkar.common.service.Executor;
import org.hl7.tinkar.coordinate.stamp.StampPathImmutable;
import org.hl7.tinkar.coordinate.view.ViewCoordinateRecord;
import org.hl7.tinkar.coordinate.view.calculator.ViewCalculatorWithCache;

import java.util.List;

public class ViewMenuModel {
    private final ChangeListener<ViewCoordinateRecord> viewChangedListener = this::viewCoordinateChanged;
    private final ViewProperties viewProperties;
    private final Control baseControlToShowOverride;
    private final Shape baseControlGraphic;
    private String oldFill = null;

    public Menu getCoordinateMenu() {
        return coordinateMenu;
    }

    private final Menu coordinateMenu;


    public ViewMenuModel(ViewProperties viewProperties, Control baseControlToShowOverride) {
        this(viewProperties, baseControlToShowOverride, new Menu("Coordinates", Icon.COORDINATES.makeIcon()));
    }

    public ViewMenuModel(ViewProperties viewProperties, Control baseControlToShowOverride, Menu coordinateMenu) {
        this.viewProperties = viewProperties;
        this.coordinateMenu = coordinateMenu;
        this.viewProperties.nodeView().addListener(this.viewChangedListener);
        ViewCalculatorWithCache viewCalculator = ViewCalculatorWithCache.getCalculator(this.viewProperties.nodeView().getValue());
        FxGet.pathCoordinates(viewCalculator).addListener((MapChangeListener<PublicIdStringKey, StampPathImmutable>) change -> updateManifoldMenu());

        this.baseControlToShowOverride = baseControlToShowOverride;
        if (baseControlToShowOverride instanceof Labeled) {
            Node graphic = ((Labeled)this.baseControlToShowOverride).getGraphic();
            if (graphic instanceof AnchorPane) {
                Node childZero = ((AnchorPane) graphic).getChildren().get(0);
                this.baseControlGraphic = (Shape) childZero;
            } else {
                baseControlGraphic = null;
            }
        } else {
            this.baseControlGraphic = null;
        }
        updateManifoldMenu();

    }


    private void viewCoordinateChanged(ObservableValue<? extends ViewCoordinateRecord> observable,
                                       ViewCoordinateRecord oldValue,
                                       ViewCoordinateRecord newValue) {
        updateManifoldMenu();

    }

    public void updateManifoldMenu() {
        if (this.viewProperties.nodeView().hasOverrides()) {
            if (this.baseControlGraphic != null) {
                if (this.oldFill == null) {
                    this.oldFill = this.baseControlGraphic.getFill().toString().replace("0x", "#");
                }
                this.baseControlGraphic.setStyle("-fx-font-family: 'Material Design Icons'; -fx-font-size: 18.0; -icons-color: #ff9100;");
            } else {
                this.baseControlToShowOverride.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, #ff9100;");
            }
        } else {
            if (this.baseControlGraphic != null) {
                this.baseControlGraphic.setStyle("-fx-font-family: 'Material Design Icons'; -fx-font-size: 18.0; -icons-color: " +
                        this.oldFill + ";");
            } else {
                this.baseControlToShowOverride.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
            }
        }

        //this.manifoldMenu.setTooltip();

        Platform.runLater(() -> {
            ViewCalculatorWithCache viewCalculator = ViewCalculatorWithCache.getCalculator(this.viewProperties.nodeView().getValue());
            this.coordinateMenu.getItems().clear();
            Executor.threadPool().execute(TaskWrapper.make(new ViewMenuTask(viewCalculator, this.viewProperties.nodeView()),
                    (List<MenuItem> result) ->
                            this.coordinateMenu.getItems().addAll(result)));
        });
    }


}
