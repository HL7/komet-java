package sh.komet.app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.hl7.komet.framework.activity.ActivityStream;
import org.hl7.komet.framework.activity.ActivityStreams;
import org.hl7.komet.framework.alerts.AlertStream;
import org.hl7.komet.framework.alerts.AlertStreams;
import org.hl7.komet.framework.graphics.LoadFonts;
import org.hl7.komet.framework.view.ObservableViewNoOverride;
import org.hl7.komet.preferences.KometPreferences;
import org.hl7.komet.preferences.KometPreferencesImpl;
import org.hl7.komet.preferences.Preferences;
import org.hl7.tinkar.common.id.PublicIdStringKey;
import org.hl7.tinkar.common.service.Executor;
import org.hl7.tinkar.common.service.PrimitiveData;
import org.hl7.tinkar.coordinate.Coordinates;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static sh.komet.app.AppState.*;

/**
 * JavaFX App
 */
public class App extends Application {
    public static final String CSS_LOCATION = "org/hl7/komet/framework/graphics/komet.css";
    public static Logger kometLog;
    private static Stage primaryStage;
    private static Module graphicsModule;

    public static final SimpleObjectProperty<AppState> state = new SimpleObjectProperty<>(STARTING);

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
        // TODO replace logger configuration
        String loggerConfiguraton =
                "handlers= java.util.logging.FileHandler, java.util.logging.ConsoleHandler\n" +
                        ".level= ALL\n" +
                        "java.util.logging.FileHandler.pattern = %h/Solor/komet/logs/komet%g.log\n" +
                        "java.util.logging.FileHandler.limit = 50000\n" +
                        "java.util.logging.FileHandler.count = 1\n" +
                        "java.util.logging.FileHandler.formatter = java.util.logging.SimpleFormatter\n" +
                        "# For example, set the com.xyz.foo logger to only log SEVERE\n" +
                        "# messages:\n" +
                        "com.xyz.foo.level = SEVERE";


        LogManager.getLogManager().readConfiguration(new ByteArrayInputStream(loggerConfiguraton.getBytes("UTF-8")));
        kometLog = Logger.getLogger("komet");
        kometLog.info("Starting Komet");
        LoadFonts.load();
        graphicsModule = ModuleLayer.boot()
                .findModule("org.hl7.komet.graphics")
                // Optional<Module> at this point
                .orElseThrow();
    }

    @Override
    public void start(Stage stage) {

        try {
            App.primaryStage = stage;
            // Add menu toolkit for Mac?
            // https://github.com/0x4a616e/NSMenuFX

            FXMLLoader sourceLoader = new FXMLLoader(getClass().getResource("SelectDataSource.fxml"));
            BorderPane sourceRoot = sourceLoader.load();
            SelectDataSourceController selectDataSourceController = sourceLoader.getController();
            Scene sourceScene = new Scene(sourceRoot, 600, 400);



            sourceScene.getStylesheets()

                       .add(graphicsModule.getClassLoader().getResource(CSS_LOCATION).toString());
            stage.setScene(sourceScene);
            stage.setTitle("KOMET Startup");

            stage.show();
            state.set(AppState.SELECT_DATA_SOURCE);
            state.addListener(this::appStateChangeListener);

        } catch (IOException e) {
            kometLog.log(Level.SEVERE, e.getLocalizedMessage(), e);
            Platform.exit();
        }
    }

    private void appStateChangeListener(ObservableValue<? extends AppState> observable, AppState oldValue, AppState newValue) {
        try {
            switch (newValue) {
                case SELECTED_DATA_SOURCE -> {

                    Platform.runLater(() -> state.set(LOADING_DATA_SOURCE));
                    Executor.threadPool().submit(new LoadDataSourceTask(state));
                }

                case RUNNING -> {
                    FXMLLoader kometStageLoader = new FXMLLoader(getClass().getResource("KometStageScene.fxml"));
                    BorderPane kometRoot = kometStageLoader.load();
                    KometStageController controller = kometStageLoader.getController();

                    Scene kometScene = new Scene(kometRoot, 1800, 1024);
                    kometScene.getStylesheets()
                            .add(graphicsModule.getClassLoader().getResource(CSS_LOCATION).toString());

                    primaryStage.setScene(kometScene);
                    ObservableViewNoOverride windowView = new ObservableViewNoOverride(Coordinates.View.DefaultView());
                    KometPreferences nodePreferences = KometPreferencesImpl.getConfigurationRootPreferences();
                    KometPreferences windowPreferences = nodePreferences.node("main-komet-window");
                    PublicIdStringKey<ActivityStream> activityStreamKey = ActivityStreams.UNLINKED;
                    AlertStream alertStream = AlertStreams.get(AlertStreams.ROOT_ALERT_STREAM_KEY);

                    controller.setup(windowView, windowPreferences, activityStreamKey, alertStream);
                    primaryStage.setTitle("Komet");
                    primaryStage.centerOnScreen();


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

    @Override
    public void stop() {
        kometLog.info("Stopping application\n\n###############\n\n");

        // There is a known bug on shutdown:
        // https://bugs.openjdk.java.net/browse/JDK-8231558
        // Java has been detached already, but someone is still trying to use it at -[GlassViewDelegate dealloc]
    }

    public static void main(String[] args) {
        // https://stackoverflow.com/questions/42598097/using-javafx-application-stop-method-over-shutdownhook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println("Shutdown hook")));

        launch();
    }

}