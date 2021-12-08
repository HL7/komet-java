package org.hl7.komet.framework.window;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.eclipse.collections.api.list.ImmutableList;
import org.hl7.komet.framework.SetupNode;
import org.hl7.komet.framework.activity.ActivityStream;
import org.hl7.komet.framework.concurrent.TaskWrapper;
import org.hl7.komet.framework.tabs.DetachableTab;
import org.hl7.komet.framework.tabs.TabStack;
import org.hl7.komet.framework.view.ObservableViewNoOverride;
import org.hl7.komet.framework.view.ViewMenuTask;
import org.hl7.komet.preferences.KometPreferences;
import org.hl7.tinkar.common.alert.AlertStream;
import org.hl7.tinkar.common.id.PublicIdStringKey;
import org.hl7.tinkar.common.service.Executor;
import org.hl7.tinkar.coordinate.view.calculator.ViewCalculatorWithCache;
import org.kordamp.ikonli.javafx.FontIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


/**
 * Root node of scene is given a UUID for unique identification.
 *
 * @author kec
 */
public class KometStageController implements SetupNode {

    private static final Logger LOG = LoggerFactory.getLogger(KometStageController.class);
    private final ImageView vanityImage = new ImageView();
    ArrayList<TabPane> tabPanes = new ArrayList<>();
    @FXML  // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;
    @FXML  // URL location of the FXML file that was given to the FXMLLoader
    private URL location;
    @FXML                                                                          // fx:id="bottomBorderBox"
    private HBox bottomBorderBox;                  // Value injected by FXMLLoader
    @FXML                                                                          // fx:id="editorButtonBar"
    private ButtonBar editorButtonBar;                  // Value injected by FXMLLoader
    @FXML                                                                          // fx:id="windowSplitPane"
    private SplitPane windowSplitPane;       // Value injected by FXMLLoader
    @FXML
    private BorderPane leftBorderPane;
    @FXML
    private BorderPane centerBorderPane;
    @FXML
    private BorderPane rightBorderPane;
    @FXML                                                                          // fx:id="editToolBar"
    private ToolBar editToolBar;                      // Value injected by FXMLLoader
    @FXML                                                                          // fx:id="statusMessage"
    private Label statusMessage;                    // Value injected by FXMLLoader
    @FXML                                                                          // fx:id="vanityBox"
    private Button vanityBox;                        // Value injected by FXMLLoader
    @FXML                                                                          // fx:id="topGridPane"
    private GridPane topGridPane;                      // Value injected by FXMLLoader
    @FXML                                                                          // fx:id="classifierMenuButton"
    private MenuButton classifierMenuButton;             // Value injected by FXMLLoader

    @FXML
    private Label pathLabel;

    @FXML
    private Menu windowCoordinates;

    @FXML
    private MenuButton viewPropertiesButton;

    //private ChangeListener<Boolean> focusChangeListener = this::handleFocusEvents;
    private Stage stage;
    private List<MenuButton> newTabMenuButtons = new ArrayList<>(5);

    private ObservableViewNoOverride windowView;
    private KometPreferences nodePreferences;
    private TabStack leftDetachableTabPane;
    private TabStack centerDetachableTabPane;
    private TabStack rightDetachableTabPane;

    private Node getTabPaneFromIndex(int index) {
        switch (index) {
            case 0:
                return leftDetachableTabPane;
            case 1:
                return centerDetachableTabPane;
            case 2:
                return rightDetachableTabPane;
            default:
                throw new ArrayIndexOutOfBoundsException("Tab pane index is: " + index);
        }
    }

    /**
     * When the button action event is triggered, refresh the user CSS file.
     *
     * @param event the action event.
     */
    @FXML
    public void handleRefreshUserCss(ActionEvent event) {
        try {
            // "Feature" to make css editing/testing easy in the dev environment.
            File cssSourceFile = new File("../framework/src/main/resources/org/hl7/komet/framework/graphics/komet.css");
            if (cssSourceFile.exists()) {
                Scene scene = vanityBox.getScene();
                scene.getStylesheets().clear();
                scene.getStylesheets().add(cssSourceFile.toURI().toURL().toString());
                LOG.info("Updated komet.css: " + cssSourceFile.getAbsolutePath());
            } else {
                LOG.info("File not found for komet.css: " + cssSourceFile.getAbsolutePath());
            }
        } catch (IOException e) {
            // TODO: Raise an alert
            e.printStackTrace();
        }

    }

    @FXML
        // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert bottomBorderBox != null :
                "fx:id=\"bottomBorderBox\" was not injected: check your FXML file 'KometStageScene.fxml'.";
        assert editorButtonBar != null :
                "fx:id=\"editorButtonBar\" was not injected: check your FXML file 'KometStageScene.fxml'.";
        assert centerBorderPane != null :
                "fx:id=\"centerBorderPane\" was not injected: check your FXML file 'KometStageScene.fxml'.";
        assert editToolBar != null :
                "fx:id=\"editToolBar\" was not injected: check your FXML file 'KometStageScene.fxml'.";
        assert leftBorderPane != null :
                "fx:id=\"leftBorderPane\" was not injected: check your FXML file 'KometStageScene.fxml'.";
        assert rightBorderPane != null :
                "fx:id=\"rightBorderPane\" was not injected: check your FXML file 'KometStageScene.fxml'.";
        assert statusMessage != null :
                "fx:id=\"statusMessage\" was not injected: check your FXML file 'KometStageScene.fxml'.";
        assert vanityBox != null : "fx:id=\"vanityBox\" was not injected: check your FXML file 'KometStageScene.fxml'.";
        assert topGridPane != null :
                "fx:id=\"topGridPane\" was not injected: check your FXML file 'KometStageScene.fxml'.";
        assert classifierMenuButton != null :
                "fx:id=\"classifierMenuButton\" was not injected: check your FXML file 'KometStageScene.fxml'.";

        //windowCoordinates.setGraphic(Icon.COORDINATES.getStyledIconographic());

        viewPropertiesButton.setGraphic(new FontIcon());
        viewPropertiesButton.setId("view-coordinates");
        //classifierMenuButton.setGraphic(Icon.ICON_CLASSIFIER1.getIconographic());
        classifierMenuButton.getItems().clear();
        classifierMenuButton.getItems().addAll(getTaskMenuItems());

        Image image = new Image(KometStageController.class.getResourceAsStream("/org/hl7/komet/framework/images/viewer-logo-b@2.png"));
        vanityImage.setImage(image);
        vanityImage.setFitHeight(36);
        vanityImage.setPreserveRatio(true);
        vanityImage.setSmooth(true);
        vanityImage.setCache(true);
        vanityBox.setGraphic(vanityImage);


        topGridPane.setStyle("-fx-border-color: transparent");
        topGridPane.setOnDragDropped((DragEvent event) -> {
            event.setDropCompleted(true);
        });
        topGridPane.setOnDragOver((DragEvent event) -> {
            event.acceptTransferModes(TransferMode.MOVE);
            topGridPane.setStyle("-fx-border-color: -komet-blue-color");
            event.consume();
        });

        topGridPane.setOnDragEntered((DragEvent event) -> {
            event.acceptTransferModes(TransferMode.MOVE);
            topGridPane.setStyle("-fx-border-color: -komet-blue-color");
            event.consume();
        });

        topGridPane.setOnDragExited((DragEvent event) -> {
            event.acceptTransferModes(TransferMode.MOVE);
            topGridPane.setStyle("-fx-border-color: transparent");
            event.consume();
        });
    }

    private List<MenuItem> getTaskMenuItems() {
        ArrayList<MenuItem> items = new ArrayList<>();
        return items;
    }

    void handleCloseRequest(WindowEvent event) {
        //stage.focusedProperty().removeListener(this.focusChangeListener);
        //MenuProvider.handleCloseRequest(event);
    }

    @Override
    public void setup(ObservableViewNoOverride windowView,
                      KometPreferences nodePreferences,
                      PublicIdStringKey<ActivityStream> activityStreamKey,
                      AlertStream alertStream) {
        this.windowView = windowView;
        this.nodePreferences = nodePreferences;
        setupWindow();
        ViewCalculatorWithCache viewCalculator = ViewCalculatorWithCache.getCalculator(windowView.toViewCoordinateRecord());


        Executor.threadPool().execute(TaskWrapper.make(new ViewMenuTask(viewCalculator, windowView),
                (List<MenuItem> result) -> {
                    windowCoordinates.getItems().addAll(result);
                }));

        windowView.addListener((observable, oldValue, newValue) -> {
            windowCoordinates.getItems().clear();
            Executor.threadPool().execute(TaskWrapper.make(new ViewMenuTask(viewCalculator, windowView),
                    (List<MenuItem> result) -> windowCoordinates.getItems().addAll(result)));
        });
    }

    private void setupWindow() {
        this.leftDetachableTabPane = TabStack.make(TabStack.REMOVAL.DISALLOW, windowView, nodePreferences);
        this.centerDetachableTabPane = TabStack.make(TabStack.REMOVAL.DISALLOW, windowView, nodePreferences);
        this.rightDetachableTabPane = TabStack.make(TabStack.REMOVAL.DISALLOW, windowView, nodePreferences);

        leftBorderPane.setCenter(this.leftDetachableTabPane);
        centerBorderPane.setCenter(this.centerDetachableTabPane);
        rightBorderPane.setCenter(this.rightDetachableTabPane);

    }

    public void setLeftTabs(ImmutableList<DetachableTab> tabs, int selectedIndexInTab) {
        setupTabs(tabs, selectedIndexInTab, this.leftDetachableTabPane);
    }

    private void setupTabs(ImmutableList<DetachableTab> tabs, int selectedIndexInTab, TabStack tabStack) {
        for (DetachableTab tab : tabs) {
            tabStack.getTabs().add(tab);
        }
        if (selectedIndexInTab > -1 || selectedIndexInTab < tabs.size()) {
            tabStack.getSelectionModel().select(tabs.get(selectedIndexInTab));
        }
    }

    public void setCenterTabs(ImmutableList<DetachableTab> tabs, int selectedIndexInTab) {
        setupTabs(tabs, selectedIndexInTab, this.centerDetachableTabPane);
    }

    public void setRightTabs(ImmutableList<DetachableTab> tabs, int selectedIndexInTab) {
        setupTabs(tabs, selectedIndexInTab, this.rightDetachableTabPane);
    }

    public enum Keys {
        FACTORY_CLASS,
        TAB_PANE_INDEX,
        INDEX_IN_TAB_PANE,
    }
}
