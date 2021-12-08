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
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.hl7.komet.details.DetailsNodeFactory;
import org.hl7.komet.framework.KometNode;
import org.hl7.komet.framework.ScreenInfo;
import org.hl7.komet.framework.activity.ActivityStream;
import org.hl7.komet.framework.activity.ActivityStreamOption;
import org.hl7.komet.framework.activity.ActivityStreams;
import org.hl7.komet.framework.graphics.Icon;
import org.hl7.komet.framework.graphics.LoadFonts;
import org.hl7.komet.framework.preferences.KometPreferencesStage;
import org.hl7.komet.framework.tabs.DetachableTab;
import org.hl7.komet.framework.view.ObservableViewNoOverride;
import org.hl7.komet.framework.window.KometStageController;
import org.hl7.komet.framework.window.MainWindowRecord;
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

    private static ImmutableList<DetachableTab> makeDefaultLeftTabs(ObservableViewNoOverride windowView,
                                                                    KometPreferences nodePreferences) {

        GraphNavigatorNodeFactory navigatorNodeFactory = new GraphNavigatorNodeFactory();
        KometNode navigatorNode1 = navigatorNodeFactory.create(windowView, nodePreferences,
                ActivityStreams.NAVIGATION, ActivityStreamOption.PUBLISH.keyForOption(), AlertStreams.ROOT_ALERT_STREAM_KEY);
        DetachableTab navigatorNode1Tab = new DetachableTab(navigatorNode1.getTitle().getValue(), navigatorNode1.getNode());
        navigatorNode1Tab.setGraphic(navigatorNode1.getTitleNode());


        PatternNavigatorFactory patternNavigatorNodeFactory = new PatternNavigatorFactory();

        KometNode patternNavigatorNode2 = patternNavigatorNodeFactory.create(windowView, nodePreferences,
                ActivityStreams.NAVIGATION, ActivityStreamOption.PUBLISH.keyForOption(), AlertStreams.ROOT_ALERT_STREAM_KEY);

        DetachableTab patternNavigatorNode1Tab = new DetachableTab(patternNavigatorNode2.getTitle().getValue(), patternNavigatorNode2.getNode());
        patternNavigatorNode1Tab.setGraphic(patternNavigatorNode2.getTitleNode());

        return Lists.immutable.of(navigatorNode1Tab, patternNavigatorNode1Tab);
    }

    private static ImmutableList<DetachableTab> makeDefaultCenterTabs(ObservableViewNoOverride windowView,
                                                                      KometPreferences nodePreferences) {

        DetailsNodeFactory detailsNodeFactory = new DetailsNodeFactory();
        KometNode detailsNode1 = detailsNodeFactory.create(windowView, nodePreferences,
                ActivityStreams.NAVIGATION, ActivityStreamOption.SUBSCRIBE.keyForOption(), AlertStreams.ROOT_ALERT_STREAM_KEY);

        DetachableTab detailsNode1Tab = new DetachableTab(detailsNode1.getTitle().getValue(), detailsNode1.getNode());
        // TODO: setting up tab graphic, title, and tooltip needs to be standardized by the factory...
        detailsNode1Tab.setGraphic(detailsNode1.getTitleNode());
        detailsNode1Tab.textProperty().bind(detailsNode1.getTitle());
        detailsNode1Tab.tooltipProperty().setValue(detailsNode1.makeToolTip());

        KometNode detailsNode2 = detailsNodeFactory.create(windowView, nodePreferences,
                ActivityStreams.SEARCH, ActivityStreamOption.SUBSCRIBE.keyForOption(), AlertStreams.ROOT_ALERT_STREAM_KEY);
        DetachableTab detailsNode2Tab = new DetachableTab(detailsNode2.getTitle().getValue(), detailsNode2.getNode());
        // TODO: setting up tab graphic, title, and tooltip needs to be standardized by the factory...
        detailsNode2Tab.setGraphic(detailsNode2.getTitleNode());
        detailsNode2Tab.textProperty().bind(detailsNode2.getTitle());
        detailsNode2Tab.tooltipProperty().setValue(detailsNode2.makeToolTip());

        KometNode detailsNode3 = detailsNodeFactory.create(windowView, nodePreferences,
                ActivityStreams.UNLINKED, ActivityStreamOption.PUBLISH.keyForOption(), AlertStreams.ROOT_ALERT_STREAM_KEY);
        DetachableTab detailsNode3Tab = new DetachableTab(detailsNode3.getTitle().getValue(), detailsNode3.getNode());
        // TODO: setting up tab graphic, title, and tooltip needs to be standardized by the factory...
        detailsNode3Tab.setGraphic(detailsNode3.getTitleNode());
        detailsNode3Tab.textProperty().bind(detailsNode3.getTitle());
        detailsNode3Tab.tooltipProperty().setValue(detailsNode3.makeToolTip());

        ListNodeFactory listNodeFactory = new ListNodeFactory();
        KometNode listNode = listNodeFactory.create(windowView, nodePreferences,
                ActivityStreams.LIST, ActivityStreamOption.PUBLISH.keyForOption(), AlertStreams.ROOT_ALERT_STREAM_KEY);
        DetachableTab listNodeNodeTab = new DetachableTab(listNode.getTitle().getValue(), listNode.getNode());
        // TODO: setting up tab graphic, title, and tooltip needs to be standardized by the factory...
        listNodeNodeTab.setGraphic(listNode.getTitleNode());
        listNodeNodeTab.textProperty().bind(listNode.getTitle());
        listNodeNodeTab.tooltipProperty().setValue(listNode.makeToolTip());

        TableNodeFactory tableNodeFactory = new TableNodeFactory();
        KometNode tableNode = tableNodeFactory.create(windowView, nodePreferences,
                ActivityStreams.UNLINKED, ActivityStreamOption.PUBLISH.keyForOption(), AlertStreams.ROOT_ALERT_STREAM_KEY);
        DetachableTab tableNodeTab = new DetachableTab(tableNode.getTitle().getValue(), tableNode.getNode());
        // TODO: setting up tab graphic, title, and tooltip needs to be standardized by the factory...
        tableNodeTab.setGraphic(tableNode.getTitleNode());
        tableNodeTab.textProperty().bind(tableNode.getTitle());
        tableNodeTab.tooltipProperty().setValue(tableNode.makeToolTip());

        return Lists.immutable.of(detailsNode1Tab, detailsNode2Tab, detailsNode3Tab, listNodeNodeTab, tableNodeTab);
    }

    private static ImmutableList<DetachableTab> makeDefaultRightTabs(ObservableViewNoOverride windowView,
                                                                     KometPreferences nodePreferences) {

        SearchNodeFactory searchNodeFactory = new SearchNodeFactory();
        KometNode searchNode = searchNodeFactory.create(windowView, nodePreferences,
                ActivityStreams.SEARCH, ActivityStreamOption.PUBLISH.keyForOption(), AlertStreams.ROOT_ALERT_STREAM_KEY);
        DetachableTab newSearchTab = new DetachableTab(searchNode.getTitle().getValue(), searchNode.getNode());
        newSearchTab.setGraphic(searchNode.getTitleNode());

        ProgressNodeFactory progressNodeFactory = new ProgressNodeFactory();
        KometNode kometNode = progressNodeFactory.create(windowView, nodePreferences,
                null, null, AlertStreams.ROOT_ALERT_STREAM_KEY);
        DetachableTab progressTab = new DetachableTab(kometNode.getTitle().getValue(), kometNode.getNode());
        progressTab.setGraphic(kometNode.getTitleNode());

        CompletionNodeFactory completionNodeFactory = new CompletionNodeFactory();
        KometNode completionNode = completionNodeFactory.create(windowView, nodePreferences,
                null, null, AlertStreams.ROOT_ALERT_STREAM_KEY);
        DetachableTab completionTab = new DetachableTab(completionNode.getTitle().getValue(), completionNode.getNode());
        completionTab.setGraphic(completionNode.getTitleNode());

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
                    ObservableViewNoOverride windowView = new ObservableViewNoOverride(Coordinates.View.DefaultView());

                    MainWindowRecord mainWindowRecord = MainWindowRecord.make();

                    BorderPane kometRoot = mainWindowRecord.root();
                    KometStageController controller = mainWindowRecord.controller();

                    Scene kometScene = new Scene(kometRoot, 1800, 1024);
                    kometScene.getStylesheets()
                            .add(graphicsModule.getClassLoader().getResource(CSS_LOCATION).toString());

                    primaryStage.setScene(kometScene);

                    KometPreferences windowPreferences = nodePreferences.node("main-komet-window");
                    PublicIdStringKey<ActivityStream> activityStreamKey = ActivityStreams.UNLINKED;
                    AlertStream alertStream = AlertStreams.get(AlertStreams.ROOT_ALERT_STREAM_KEY);

                    controller.setup(windowView, windowPreferences, activityStreamKey, alertStream);
                    primaryStage.setTitle("Komet");
                    primaryStage.centerOnScreen();

                    controller.setLeftTabs(makeDefaultLeftTabs(windowView, nodePreferences), 0);
                    controller.setCenterTabs(makeDefaultCenterTabs(windowView, nodePreferences), 0);
                    controller.setRightTabs(makeDefaultRightTabs(windowView, nodePreferences), 1);

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