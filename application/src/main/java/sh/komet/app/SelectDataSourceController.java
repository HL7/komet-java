/**
 * Sample Skeleton for 'SelectDataSource.fxml' Controller Class
 */

package sh.komet.app;


import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Control;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.validation.*;
import org.hl7.tinkar.common.service.DataServiceController;
import org.hl7.tinkar.common.service.DataServiceProperty;
import org.hl7.tinkar.common.service.DataUriOption;
import org.hl7.tinkar.common.service.PrimitiveData;
import org.hl7.tinkar.common.util.text.NaturalOrder;
import org.hl7.tinkar.common.validation.ValidationRecord;
import sh.komet.app.propsheet.PropertyEditorFactoryWithValidation;
import sh.komet.app.propsheet.SheetItem;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class SelectDataSourceController {

    private File rootFolder = new File(System.getProperty("user.home"), "Solor");

    private File workingFolder = new File(System.getProperty("user.dir"), "target");

    private Map<DataServiceProperty, SimpleStringProperty> dataServicePropertyStringMap = new HashMap<>();

    private ValidationSupport validationSupport = new ValidationSupport();

    @FXML
    private GridPane inputGrid;

    @FXML
    private PropertySheet propertySheet;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="dataSourceChoiceBox"
    private ChoiceBox<DataServiceController<?>> dataSourceChoiceBox; // Value injected by FXMLLoader

    @FXML // fx:id="cancelButton"
    private Button cancelButton; // Value injected by FXMLLoader

    @FXML // fx:id="rootBorderPane"
    private BorderPane rootBorderPane; // Value injected by FXMLLoader

    @FXML // fx:id="fileListView"
    private ListView<DataUriOption> fileListView; // Value injected by FXMLLoader

    @FXML
    void cancelButtonPressed(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void okButtonPressed(ActionEvent event) {
        saveDataServiceProperties(dataSourceChoiceBox.getValue());
        dataSourceChoiceBox.getValue().setDataUriOption(fileListView.getSelectionModel().getSelectedItem());
        PrimitiveData.setController(dataSourceChoiceBox.getValue());
        Platform.runLater(() -> App.state.set(AppState.SELECTED_DATA_SOURCE));
    }

    void dataSourceChanged(ObservableValue<? extends DataServiceController<?>> observable,
                           DataServiceController<?> oldValue,
                           DataServiceController<?> newValue) {
        saveDataServiceProperties(oldValue);

        dataServicePropertyStringMap.clear();
        fileListView.getItems().clear();
        fileListView.getItems().addAll(dataSourceChoiceBox.getValue().providerOptions());
        fileListView.getItems().sort(new NaturalOrder());
        fileListView.getSelectionModel().selectFirst();
        fileListView.getSelectionModel().selectFirst();
        fileListView.requestFocus();

        propertySheet.getItems().clear();
        validationSupport = new ValidationSupport();

        DataServiceController<?> dataSourceController = dataSourceChoiceBox.getValue();
        dataSourceController.providerProperties().forEachKeyValue(
                (DataServiceProperty key, String value) -> {
                    SimpleStringProperty dataServiceProperty = new SimpleStringProperty(key, key.propertyName(), value);
                    dataServicePropertyStringMap.put(key, dataServiceProperty);
                    if (key.validate()) {
                        Validator validator = new Validator<String>() {

                            @Override
                            public ValidationResult apply(Control control, String s) {
                                ValidationResult validationResult = new ValidationResult();
                                for  (ValidationRecord validationRecord: dataSourceController.validate(key, s, control)) {
                                    switch (validationRecord.severity()) {
                                        case ERROR ->
                                                validationResult.add(ValidationMessage.error(
                                                        (Control) validationRecord.target(), validationRecord.message()));
                                        case  INFO ->
                                                validationResult.add(ValidationMessage.info(
                                                        (Control) validationRecord.target(), validationRecord.message()));
                                        case OK ->
                                                validationResult.add(ValidationMessage.ok(
                                                        (Control) validationRecord.target(), validationRecord.message()));
                                        case WARNING ->
                                                validationResult.add(ValidationMessage.warning(
                                                        (Control) validationRecord.target(), validationRecord.message()));
                                    }
                                }
                                return validationResult;
                            }
                        };
                        if (key.hiddenText()) {
                            propertySheet.getItems().add(SheetItem.makeForPassword(dataServiceProperty, validationSupport,validator));

                        } else {
                            propertySheet.getItems().add(SheetItem.make(dataServiceProperty, validationSupport, validator));
                        }
                    } else {
                        if (key.hiddenText()) {
                            propertySheet.getItems().add(SheetItem.makeForPassword(dataServiceProperty));
                        } else {
                            propertySheet.getItems().add(SheetItem.make(dataServiceProperty));
                        }
                    }
                });
    }

    private void saveDataServiceProperties(DataServiceController<?> dataServiceController) {
        dataServicePropertyStringMap.forEach((dataServiceProperty, simpleStringProperty) -> {
            dataServiceController.setDataServiceProperty(dataServiceProperty, simpleStringProperty.getValue());
        });
    }

    // This method is called by the FXMLLoader when initialization is complete
    @FXML
    void initialize() {
        assert dataSourceChoiceBox != null : "fx:id=\"dataSourceChoiceBox\" was not injected: check your FXML file 'SelectDataSource.fxml'.";
        assert cancelButton != null : "fx:id=\"cancelButton\" was not injected: check your FXML file 'SelectDataSource.fxml'.";
        ObservableList<DataServiceController> controllerOptions = FXCollections.observableList(PrimitiveData.getControllerOptions());
        controllerOptions.forEach(dataServiceController -> dataSourceChoiceBox.getItems().add(dataServiceController));

        dataSourceChoiceBox.getSelectionModel().selectedItemProperty().addListener(this::dataSourceChanged);

        fileListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                okButtonPressed(null);
            }
        });

        propertySheet.setPropertyEditorFactory(new PropertyEditorFactoryWithValidation());

        Platform.runLater(() -> dataSourceChoiceBox.getSelectionModel().select(controllerOptions.get(0)));
    }
}
