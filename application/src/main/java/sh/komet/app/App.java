package sh.komet.app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.hl7.komet.framework.NodeFactory;
import org.hl7.komet.graphics.LoadFonts;
import org.hl7.tinkar.common.service.Executor;
import org.hl7.tinkar.common.service.PrimitiveData;
import org.hl7.tinkar.common.util.time.Stopwatch;
import org.hl7.tinkar.entity.Entity;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ServiceLoader;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static sh.komet.app.AppState.*;

/**
 * JavaFX App
 */
public class App extends Application {
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
                       .add(graphicsModule.getClassLoader().getResource("org/hl7/komet/graphics/komet.css").toString());
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
                    FXMLLoader kometStageLoader = new FXMLLoader(getClass().getResource("KometStageScene.fxml"));
                    BorderPane kometRoot = kometStageLoader.load();

                    Scene kometScene = new Scene(kometRoot, 1800, 1024);
                    kometScene.getStylesheets()
                            .add(graphicsModule.getClassLoader().getResource("org/hl7/komet/graphics/komet.css").toString());

                    primaryStage.setScene(kometScene);
                    primaryStage.setTitle("Komet");
                    primaryStage.centerOnScreen();

                    Platform.runLater(() -> state.set(LOADING_DATA_SOURCE));
                    Executor.threadPool().execute(() -> {
                        Stopwatch reloadStopwatch = new Stopwatch();
                        PrimitiveData.start();
                        kometLog.info("Reloading in: " + reloadStopwatch.durationString() + "\n");
                        reloadStopwatch.reset();
                        Entity.provider().forEachSemanticForComponent(PrimitiveData.get().nidForUuids(UUID.fromString("ac2b08ea-457c-3847-b099-569c74d97ccd")),semanticEntity -> {
                            kometLog.info(semanticEntity.toString() + "\n");
                        });
                        kometLog.info("Query 1: " + reloadStopwatch.durationString() + "\n");
                        reloadStopwatch.reset();

                        Entity.provider().forEachSemanticForComponent(PrimitiveData.get().nidForUuids(UUID.fromString("7a9c1860-c70d-36c4-b45d-184c05cd9421")),semanticEntity -> {
                            kometLog.info(semanticEntity.toString() + "\n");
                        });
                        kometLog.info("Query 2: " + reloadStopwatch.durationString() + "\n");


                        Platform.runLater(() -> state.set(RUNNING));
                    });
                }
                case SHUTDOWN -> {
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
        launch();
    }

}