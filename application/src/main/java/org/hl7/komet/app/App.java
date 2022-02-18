package org.hl7.komet.app;

import de.jangassen.MenuToolkit;
import de.jangassen.model.AppearanceMode;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.hl7.komet.details.DetailsNodeFactory;
import org.hl7.komet.framework.KometNode;
import org.hl7.komet.framework.ScreenInfo;
import org.hl7.komet.framework.activity.ActivityStreamOption;
import org.hl7.komet.framework.activity.ActivityStreams;
import org.hl7.komet.framework.graphics.Icon;
import org.hl7.komet.framework.graphics.LoadFonts;
import org.hl7.komet.framework.preferences.KometPreferencesStage;
import org.hl7.komet.framework.preferences.Reconstructor;
import org.hl7.komet.framework.tabs.DetachableTab;
import org.hl7.komet.framework.view.ObservableViewNoOverride;
import org.hl7.komet.framework.window.KometStageController;
import org.hl7.komet.framework.window.MainWindowRecord;
import org.hl7.komet.framework.window.WindowComponent;
import org.hl7.komet.list.ListNodeFactory;
import org.hl7.komet.navigator.graph.GraphNavigatorNodeFactory;
import org.hl7.komet.navigator.pattern.PatternNavigatorFactory;
import org.hl7.komet.preferences.KometPreferences;
import org.hl7.komet.preferences.KometPreferencesImpl;
import org.hl7.komet.preferences.Preferences;
import org.hl7.komet.progress.CompletionNodeFactory;
import org.hl7.komet.progress.ProgressNodeFactory;
import org.hl7.komet.search.SearchNodeFactory;
import org.hl7.komet.table.TableNodeFactory;
import org.hl7.tinkar.common.alert.AlertObject;
import org.hl7.tinkar.common.alert.AlertStreams;
import org.hl7.tinkar.common.binary.Encodable;
import org.hl7.tinkar.common.service.PrimitiveData;
import org.hl7.tinkar.common.service.TinkExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.function.Consumer;

import static org.hl7.komet.app.AppState.LOADING_DATA_SOURCE;
import static org.hl7.komet.app.AppState.STARTING;
import static org.hl7.komet.framework.KometNodeFactory.KOMET_NODES;
import static org.hl7.komet.framework.window.WindowSettings.Keys.*;

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

    private static ImmutableList<DetachableTab> makeDefaultLeftTabs(ObservableViewNoOverride windowView) {

        GraphNavigatorNodeFactory navigatorNodeFactory = new GraphNavigatorNodeFactory();
        KometNode navigatorNode1 = navigatorNodeFactory.create(windowView,
                ActivityStreams.NAVIGATION, ActivityStreamOption.PUBLISH.keyForOption(), AlertStreams.ROOT_ALERT_STREAM_KEY);
        DetachableTab navigatorNode1Tab = new DetachableTab(navigatorNode1);


        PatternNavigatorFactory patternNavigatorNodeFactory = new PatternNavigatorFactory();

        KometNode patternNavigatorNode2 = patternNavigatorNodeFactory.create(windowView,
                ActivityStreams.NAVIGATION, ActivityStreamOption.PUBLISH.keyForOption(), AlertStreams.ROOT_ALERT_STREAM_KEY);

        DetachableTab patternNavigatorNode1Tab = new DetachableTab(patternNavigatorNode2);

        return Lists.immutable.of(navigatorNode1Tab, patternNavigatorNode1Tab);
    }

    private static ImmutableList<DetachableTab> makeDefaultCenterTabs(ObservableViewNoOverride windowView) {

        DetailsNodeFactory detailsNodeFactory = new DetailsNodeFactory();
        KometNode detailsNode1 = detailsNodeFactory.create(windowView,
                ActivityStreams.NAVIGATION, ActivityStreamOption.SUBSCRIBE.keyForOption(), AlertStreams.ROOT_ALERT_STREAM_KEY);

        DetachableTab detailsNode1Tab = new DetachableTab(detailsNode1);
        // TODO: setting up tab graphic, title, and tooltip needs to be standardized by the factory...
        detailsNode1Tab.textProperty().bind(detailsNode1.getTitle());
        detailsNode1Tab.tooltipProperty().setValue(detailsNode1.makeToolTip());

        KometNode detailsNode2 = detailsNodeFactory.create(windowView,
                ActivityStreams.SEARCH, ActivityStreamOption.SUBSCRIBE.keyForOption(), AlertStreams.ROOT_ALERT_STREAM_KEY);
        DetachableTab detailsNode2Tab = new DetachableTab(detailsNode2);

        KometNode detailsNode3 = detailsNodeFactory.create(windowView,
                ActivityStreams.UNLINKED, ActivityStreamOption.PUBLISH.keyForOption(), AlertStreams.ROOT_ALERT_STREAM_KEY);
        DetachableTab detailsNode3Tab = new DetachableTab(detailsNode3);

        ListNodeFactory listNodeFactory = new ListNodeFactory();
        KometNode listNode = listNodeFactory.create(windowView,
                ActivityStreams.LIST, ActivityStreamOption.PUBLISH.keyForOption(), AlertStreams.ROOT_ALERT_STREAM_KEY);
        DetachableTab listNodeNodeTab = new DetachableTab(listNode);

        TableNodeFactory tableNodeFactory = new TableNodeFactory();
        KometNode tableNode = tableNodeFactory.create(windowView,
                ActivityStreams.UNLINKED, ActivityStreamOption.PUBLISH.keyForOption(), AlertStreams.ROOT_ALERT_STREAM_KEY);
        DetachableTab tableNodeTab = new DetachableTab(tableNode);

        return Lists.immutable.of(detailsNode1Tab, detailsNode2Tab, detailsNode3Tab, listNodeNodeTab, tableNodeTab);
    }

    private static ImmutableList<DetachableTab> makeDefaultRightTabs(ObservableViewNoOverride windowView) {

        SearchNodeFactory searchNodeFactory = new SearchNodeFactory();
        KometNode searchNode = searchNodeFactory.create(windowView,
                ActivityStreams.SEARCH, ActivityStreamOption.PUBLISH.keyForOption(), AlertStreams.ROOT_ALERT_STREAM_KEY);
        DetachableTab newSearchTab = new DetachableTab(searchNode);

        ProgressNodeFactory progressNodeFactory = new ProgressNodeFactory();
        KometNode kometNode = progressNodeFactory.create(windowView,
                null, null, AlertStreams.ROOT_ALERT_STREAM_KEY);
        DetachableTab progressTab = new DetachableTab(kometNode);

        CompletionNodeFactory completionNodeFactory = new CompletionNodeFactory();
        KometNode completionNode = completionNodeFactory.create(windowView,
                null, null, AlertStreams.ROOT_ALERT_STREAM_KEY);
        DetachableTab completionTab = new DetachableTab(completionNode);

        return Lists.immutable.of(newSearchTab, progressTab, completionTab);
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
                    TinkExecutor.threadPool().submit(new LoadDataSourceTask(state));
                }

                case RUNNING -> {
                    primaryStage.hide();
                    Preferences.start();
                    KometPreferences appPreferences = KometPreferencesImpl.getConfigurationRootPreferences();
                    boolean appInitialized = appPreferences.getBoolean(AppKeys.APP_INITIALIZED, false);
                    if (appInitialized) {
                        LOG.info("Restoring configuration preferences. ");
                    } else {
                        LOG.info("Creating new configuration preferences. ");
                    }

                    MainWindowRecord mainWindowRecord = MainWindowRecord.make();

                    BorderPane kometRoot = mainWindowRecord.root();
                    KometStageController controller = mainWindowRecord.controller();

                    Scene kometScene = new Scene(kometRoot, 1800, 1024);
                    kometScene.getStylesheets()
                            .add(graphicsModule.getClassLoader().getResource(CSS_LOCATION).toString());

                    primaryStage.setScene(kometScene);

                    KometPreferences windowPreferences = appPreferences.node("main-komet-window");
                    boolean mainWindowInitialized = windowPreferences.getBoolean(KometStageController.WindowKeys.WINDOW_INITIALIZED, false);
                    controller.setup(windowPreferences);
                    primaryStage.setTitle("Komet");
                    //primaryStage.centerOnScreen();

                    if (!mainWindowInitialized) {
                        controller.setLeftTabs(makeDefaultLeftTabs(controller.windowView()), 0);
                        controller.setCenterTabs(makeDefaultCenterTabs(controller.windowView()), 0);
                        controller.setRightTabs(makeDefaultRightTabs(controller.windowView()), 1);
                        windowPreferences.putBoolean(KometStageController.WindowKeys.WINDOW_INITIALIZED, true);
                        appPreferences.putBoolean(AppKeys.APP_INITIALIZED, true);
                    } else {
                        // Restore nodes from preferences.
                        windowPreferences.get(LEFT_TAB_PREFERENCES).ifPresent(leftTabPreferencesName -> {
                            restoreTab(windowPreferences, leftTabPreferencesName, controller.windowView(), node -> controller.leftBorderPaneSetCenter(node));
                        });
                        windowPreferences.get(CENTER_TAB_PREFERENCES).ifPresent(centerTabPreferencesName -> {
                            restoreTab(windowPreferences, centerTabPreferencesName, controller.windowView(), node -> controller.centerBorderPaneSetCenter(node));
                        });
                        windowPreferences.get(RIGHT_TAB_PREFERENCES).ifPresent(rightTabPreferencesName -> {
                            restoreTab(windowPreferences, rightTabPreferencesName, controller.windowView(), node -> controller.rightBorderPaneSetCenter(node));
                        });
                    }
                    primaryStage.setX(controller.windowSettings().xLocationProperty().get());
                    primaryStage.setY(controller.windowSettings().yLocationProperty().get());
                    primaryStage.setHeight(controller.windowSettings().heightProperty().get());
                    primaryStage.setWidth(controller.windowSettings().widthProperty().get());
                    primaryStage.show();
                    //ScenicView.show(kometRoot);

                    App.kometPreferencesStage = new KometPreferencesStage(controller.windowView().makeOverridableViewProperties());

                    windowPreferences.sync();
                    appPreferences.sync();

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

    private void restoreTab(KometPreferences windowPreferences, String tabPreferenceNodeName, ObservableViewNoOverride windowView, Consumer<Node> nodeConsumer) {
        LOG.info("Restoring from: " + tabPreferenceNodeName);
        KometPreferences itemPreferences = windowPreferences.node(KOMET_NODES + tabPreferenceNodeName);
        itemPreferences.get(WindowComponent.WindowComponentKeys.FACTORY_CLASS).ifPresent(factoryClassName -> {
            try {
                Class<?> objectClass = Class.forName(factoryClassName);
                Class<? extends Annotation> annotationClass = Reconstructor.class;
                Object[] parameters = new Object[]{windowView, itemPreferences};
                WindowComponent windowComponent = (WindowComponent) Encodable.decode(objectClass, annotationClass, parameters);
                nodeConsumer.accept(windowComponent.getNode());

            } catch (Exception e) {
                AlertStreams.getRoot().dispatch(AlertObject.makeError(e));
            }
        });
    }

    public enum AppKeys {
        APP_INITIALIZED
    }
}