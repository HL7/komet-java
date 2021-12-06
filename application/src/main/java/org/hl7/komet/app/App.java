package org.hl7.komet.app;

import de.jangassen.MenuToolkit;
import de.jangassen.model.AppearanceMode;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.hl7.komet.framework.ScreenInfo;
import org.hl7.komet.framework.activity.ActivityStream;
import org.hl7.komet.framework.activity.ActivityStreams;
import org.hl7.komet.framework.graphics.Icon;
import org.hl7.komet.framework.graphics.LoadFonts;
import org.hl7.komet.framework.preferences.KometPreferencesStage;
import org.hl7.komet.framework.view.ObservableViewNoOverride;
import org.hl7.komet.preferences.KometPreferences;
import org.hl7.komet.preferences.KometPreferencesImpl;
import org.hl7.komet.preferences.Preferences;
import org.hl7.tinkar.common.alert.AlertObject;
import org.hl7.tinkar.common.alert.AlertStream;
import org.hl7.tinkar.common.alert.AlertStreams;
import org.hl7.tinkar.common.id.PublicIdStringKey;
import org.hl7.tinkar.common.service.Executor;
import org.hl7.tinkar.common.service.PrimitiveData;
import org.hl7.tinkar.coordinate.Coordinates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

import static org.hl7.komet.app.AppState.LOADING_DATA_SOURCE;
import static org.hl7.komet.app.AppState.STARTING;

/**
 * JavaFX App
 */
public class App extends Application {
    private static final Logger LOG = LoggerFactory.getLogger(App.class);
    public static final String CSS_LOCATION = "org/hl7/komet/framework/graphics/komet.css";
    public static final SimpleObjectProperty<AppState> state = new SimpleObjectProperty<>(STARTING);
    private static Stage primaryStage;
    private static Module graphicsModule;
    private static long windowCount = 1;
    private static KometPreferencesStage kometPreferencesStage;

    public static void main(String[] args) {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Komet");
        // https://stackoverflow.com/questions/42598097/using-javafx-application-stop-method-over-shutdownhook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOG.info("Starting shutdown hook");
            PrimitiveData.save();
            PrimitiveData.stop();
            LOG.info("Finished shutdown hook");
        }));
        launch();
    }

    private static void createNewStage() {
        Stage stage = new Stage();
        stage.setScene(new Scene(new StackPane()));
        stage.setTitle("New stage" + " " + (windowCount++));
        stage.show();
    }

    public void init() throws Exception {
        /*
"/" the local pathname separator
"%t" the system temporary directory
"%h" the value of the "user.home" system property
"%g" the generation number to distinguish rotated logs
"%u" a unique number to resolve conflicts
"%%" translates to a single percent sign "%"
         */
//        String pattern = "%h/Solor/komet/logs/komet%g.log";
//        int fileSizeLimit = 1024 * 1024; //the maximum number of bytes to write to any one file
//        int fileCount = 10;
//        boolean append = true;
//
//        FileHandler fileHandler = new FileHandler(pattern,
//                fileSizeLimit,
//                fileCount,
//                append);

        File logDirectory = new File(System.getProperty("user.home"), "Solor/komet/logs");
        logDirectory.mkdirs();
        LOG.info("Starting Komet");
        LoadFonts.load();
        graphicsModule = ModuleLayer.boot()
                .findModule("org.hl7.komet.framework")
                // Optional<Module> at this point
                .orElseThrow();
    }

    @Override
    public void start(Stage stage) {

        try {
            App.primaryStage = stage;
            Thread.currentThread().setUncaughtExceptionHandler((t, e) -> AlertStreams.getRoot().dispatch(AlertObject.makeError(e)));
            // Get the toolkit
            MenuToolkit tk = MenuToolkit.toolkit();
            Menu kometAppMenu = tk.createDefaultApplicationMenu("Komet");
            MenuItem prefsItem = new MenuItem("Komet preferences...");
            prefsItem.setAccelerator(new KeyCodeCombination(KeyCode.COMMA, KeyCombination.META_DOWN));
            prefsItem.setOnAction(event -> App.kometPreferencesStage.showPreferences());

            kometAppMenu.getItems().add(2, prefsItem);
            kometAppMenu.getItems().add(3, new SeparatorMenuItem());
            tk.setApplicationMenu(kometAppMenu);
            //tk.setGlobalMenuBar();
            // File Menu
            Menu fileMenu = new Menu("File");
            MenuItem newItem = new MenuItem("New...");
            fileMenu.getItems().addAll(newItem, new SeparatorMenuItem(), tk.createCloseWindowMenuItem(),
                    new SeparatorMenuItem(), new MenuItem("TBD"));

            // Edit
            Menu editMenu = new Menu("Edit");
            editMenu.getItems().addAll(createMenuItem("Undo"), createMenuItem("Redo"), new SeparatorMenuItem(),
                    createMenuItem("Cut"), createMenuItem("Copy"), createMenuItem("Paste"), createMenuItem("Select All"));


            // Window Menu
            Menu windowMenu = new Menu("Window");
            windowMenu.getItems().addAll(tk.createMinimizeMenuItem(), tk.createZoomMenuItem(), tk.createCycleWindowsItem(),
                    new SeparatorMenuItem(), tk.createBringAllToFrontItem());

            // Help Menu
            Menu helpMenu = new Menu("Help");
            helpMenu.getItems().addAll(new MenuItem("Getting started"));

            MenuBar bar = new MenuBar();
            bar.getMenus().addAll(kometAppMenu, fileMenu, editMenu, windowMenu, helpMenu);
            tk.setAppearanceMode(AppearanceMode.AUTO);
            tk.setDockIconMenu(createDockMenu());
            tk.autoAddWindowMenuItems(windowMenu);
            tk.setGlobalMenuBar(bar);
            tk.setTrayMenu(createSampleMenu());


            FXMLLoader sourceLoader = new FXMLLoader(getClass().getResource("SelectDataSource.fxml"));
            BorderPane sourceRoot = sourceLoader.load();
            SelectDataSourceController selectDataSourceController = sourceLoader.getController();
            Scene sourceScene = new Scene(sourceRoot, 600, 400);


            sourceScene.getStylesheets()

                    .add(graphicsModule.getClassLoader().getResource(CSS_LOCATION).toString());
            stage.setScene(sourceScene);
            stage.setTitle("KOMET Startup");

            stage.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                ScreenInfo.mouseIsPressed(true);
                ScreenInfo.mouseWasDragged(false);
            });
            stage.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {
                ScreenInfo.mouseIsPressed(false);
                ScreenInfo.mouseIsDragging(false);
            });
            stage.addEventFilter(MouseEvent.DRAG_DETECTED, event -> {
                ScreenInfo.mouseIsDragging(true);
                ScreenInfo.mouseWasDragged(true);

            });
            stage.show();
            state.set(AppState.SELECT_DATA_SOURCE);
            state.addListener(this::appStateChangeListener);

        } catch (IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
            Platform.exit();
        }
    }

    @Override
    public void stop() {
        LOG.info("Stopping application\n\n###############\n\n");
    }

    private MenuItem createMenuItem(String title) {
        MenuItem menuItem = new MenuItem(title);
        menuItem.setOnAction(this::handleEvent);
        return menuItem;
    }

    private Menu createDockMenu() {
        Menu dockMenu = createSampleMenu();
        MenuItem open = new MenuItem("New Window");
        open.setGraphic(Icon.OPEN.makeIcon());
        open.setOnAction(e -> createNewStage());
        dockMenu.getItems().addAll(new SeparatorMenuItem(), open);
        return dockMenu;
    }

    private Menu createSampleMenu() {
        Menu trayMenu = new Menu();
        trayMenu.setGraphic(Icon.TEMPORARY_FIX.makeIcon());
        MenuItem reload = new MenuItem("Reload");
        reload.setGraphic(Icon.SYNCHRONIZE_WITH_STREAM.makeIcon());
        reload.setOnAction(this::handleEvent);
        MenuItem print = new MenuItem("Print");
        print.setOnAction(this::handleEvent);

        Menu share = new Menu("Share");
        MenuItem mail = new MenuItem("Mail");
        mail.setOnAction(this::handleEvent);
        share.getItems().add(mail);

        trayMenu.getItems().addAll(reload, print, new SeparatorMenuItem(), share);
        return trayMenu;
    }

    private void handleEvent(ActionEvent actionEvent) {
        System.out.println("clicked " + actionEvent.getSource());  // NOSONAR
    }

    private void appStateChangeListener(ObservableValue<? extends AppState> observable, AppState oldValue, AppState newValue) {
        try {
            switch (newValue) {
                case SELECTED_DATA_SOURCE -> {

                    Platform.runLater(() -> state.set(LOADING_DATA_SOURCE));
                    Executor.threadPool().submit(new LoadDataSourceTask(state));
                }

                case RUNNING -> {
                    Preferences.start();
                    KometPreferences nodePreferences = KometPreferencesImpl.getConfigurationRootPreferences();
                    if (nodePreferences.hasKey(Keys.INITIALIZED)) {
                        LOG.info("Restoring configuration preferences. ");
                    } else {
                        LOG.info("Creating new configuration preferences. ");
                    }

                    FXMLLoader kometStageLoader = new FXMLLoader(getClass().getResource("KometStageScene.fxml"));
                    BorderPane kometRoot = kometStageLoader.load();
                    KometStageController controller = kometStageLoader.getController();

                    Scene kometScene = new Scene(kometRoot, 1800, 1024);
                    kometScene.getStylesheets()
                            .add(graphicsModule.getClassLoader().getResource(CSS_LOCATION).toString());

                    primaryStage.setScene(kometScene);
                    ObservableViewNoOverride windowView = new ObservableViewNoOverride(Coordinates.View.DefaultView());

                    KometPreferences windowPreferences = nodePreferences.node("main-komet-window");
                    PublicIdStringKey<ActivityStream> activityStreamKey = ActivityStreams.UNLINKED;
                    AlertStream alertStream = AlertStreams.get(AlertStreams.ROOT_ALERT_STREAM_KEY);

                    controller.setup(windowView, windowPreferences, activityStreamKey, alertStream);
                    primaryStage.setTitle("Komet");
                    primaryStage.centerOnScreen();

                    //ScenicView.show(kometRoot);

                    App.kometPreferencesStage = new KometPreferencesStage(windowView.makeOverridableViewProperties());

                }
                case SHUTDOWN -> {
                    // Fork join pool tasks?
                    // Latch of some sort?
                    // Need to put in background thread.
                    PrimitiveData.stop();
                    Preferences.stop();
                    Platform.exit();
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
            Platform.exit();
        }
    }

    public enum Keys {
        INITIALIZED
    }
}