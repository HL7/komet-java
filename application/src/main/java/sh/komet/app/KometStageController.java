package sh.komet.app;

import javafx.beans.binding.Bindings;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.controlsfx.control.TaskProgressView;
import org.hl7.komet.framework.TaskLists;
import org.hl7.komet.tabs.DetachableTab;
import org.hl7.komet.tabs.TabStack;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;


/**
 * Root node of scene is given a UUID for unique identification.
 *
 * @author kec
 */
public class KometStageController {

    private static final Logger LOG = Logger.getLogger(KometStageController.class.getName());

    public enum Keys {
        FACTORY_CLASS,
        TAB_PANE_INDEX,
        INDEX_IN_TAB_PANE,
    }

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

    private final ImageView vanityImage = new ImageView();

    private TabStack leftDetachableTabPane = TabStack.make();
    private TabStack centerDetachableTabPane = TabStack.make();
    private TabStack rightDetachableTabPane = TabStack.make();

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
            File cssSourceFile = new File("../graphics/src/main/resources/org/hl7/komet/graphics/komet.css");
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

        //windowCoordinates.setGraphic(Iconography.COORDINATES.getStyledIconographic());

        viewPropertiesButton.setGraphic(new FontIcon());
        viewPropertiesButton.setId("view-coordinates");

        leftBorderPane.setCenter(this.leftDetachableTabPane);
        centerBorderPane.setCenter(this.centerDetachableTabPane);
        rightBorderPane.setCenter(this.rightDetachableTabPane);
        //classifierMenuButton.setGraphic(Iconography.ICON_CLASSIFIER1.getIconographic());
        classifierMenuButton.getItems().clear();
        classifierMenuButton.getItems().addAll(getTaskMenuItems());

        Image image = new Image(KometStageController.class.getResourceAsStream("/images/viewer-logo-b@2.png"));
        vanityImage.setImage(image);
        vanityImage.setFitHeight(36);
        vanityImage.setPreserveRatio(true);
        vanityImage.setSmooth(true);
        vanityImage.setCache(true);
        vanityBox.setGraphic(vanityImage);

        TaskProgressView<Task<?>> progressView = new TaskProgressView<>();
        progressView.setRetainTasks(false);

        Optional<TaskLists> optionalTaskLists = ServiceLoader.load(TaskLists.class).findFirst();
        optionalTaskLists.ifPresent(taskLists -> {
            Bindings.bindContent(progressView.getTasks(), taskLists.executingTasks());
        });
        this.leftDetachableTabPane.getTabPane().getTabs().add(new DetachableTab("Label 3", new Label("3")));
        this.leftDetachableTabPane.getTabPane().getTabs().add(new DetachableTab("Label 4", new Label("4")));

        this.centerDetachableTabPane.getTabs().add(new DetachableTab("Label 1", new Label("1")));
        this.centerDetachableTabPane.getTabs().add(new DetachableTab("Label 2", new Label("2")));

        this.rightDetachableTabPane.getTabs().add(new DetachableTab("Progress", progressView));
    }
    private List<MenuItem> getTaskMenuItems() {
        ArrayList<MenuItem> items = new ArrayList<>();

        return items;
    }


    void handleCloseRequest(WindowEvent event) {
        //stage.focusedProperty().removeListener(this.focusChangeListener);
        //MenuProvider.handleCloseRequest(event);
    }
}
