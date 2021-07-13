package org.hl7.komet.framework.graphics;

import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.javafx.FontIcon;

import static org.hl7.komet.framework.graphics.IconCheetSheet.*;

public enum Icon {


    VIEW("view", "ri-viewpoint", RunestroIcons),
    TAXONOMY_CLICK_TO_CLOSE("taxonomy-closed-icon", "fas-caret-down", Fontawesome5),
    TAXONOMY_CLICK_TO_OPEN("taxonomy-open-icon", "fas-caret-right", Fontawesome5),
    STATED("stated-form", "ri-chat", IconCheetSheet.RunestroIcons),
    INFERRED("inferred-form", "fa-gears", Fontawesome),
    SOURCE_BRANCH_1("branch-1", "", null),
    LINK_EXTERNAL("link-external", "oct-link-external-16", OctIcons),
    TAXONOMY_ROOT_ICON("taxonomy-root-icon", "mdi2h-hexagon-outline", MaterialDesign2),
    ALERT_CONFIRM("alert-confirm", "url(\"/org/controlsfx/dialog/dialog-confirm.png\"", ControlsFx),
    TAXONOMY_DEFINED_MULTIPARENT_OPEN("taxonomy-defined-multiparent-open-icon", "mdi2a-arrow-up-bold-circle-outline", MaterialDesign2),
    TAXONOMY_DEFINED_MULTIPARENT_CLOSED("taxonomy-defined-multiparent-closed-icon", "mdi2a-arrow-up-bold-circle-outline", MaterialDesign2),
    TAXONOMY_PRIMITIVE_MULTIPARENT_OPEN("taxonomy-primitive-multiparent-open-icon", "mdi2a-arrow-up-bold-hexagon-outline", MaterialDesign2),
    TAXONOMY_PRIMITIVE_MULTIPARENT_CLOSED("taxonomy-primitive-multiparent-closed-icon", "mdi2a-arrow-up-bold-hexagon-outline", MaterialDesign2),
    TAXONOMY_DEFINED_SINGLE_PARENT("taxonomy-defined-singleparent-icon", "mdi2c-checkbox-blank-circle-outline", MaterialDesign2),
    TAXONOMY_PRIMITIVE_SINGLE_PARENT("taxonomy-primitive-singleparent-icon", "mdi2h-hexagon", MaterialDesign2),
    ALERT_CONFIRM2("alert-confirm-2", "far-question-circle", Fontawesome5),
    ALERT_INFORM2("alert-info-2", "mdi2i-information-outline", MaterialDesign2),
    ALERT_ERROR2("alert-error-2", "mdi2a-alert-octagon", MaterialDesign2),
    ALERT_WARN2("alert-warn-2", "mdi2a-alert-circle-outline", MaterialDesign2),
    CHECK("check", "far-check-circle", Fontawesome5),
    TEMPORARY_FIX("temporary-fix", "mdi2b-bandage", MaterialDesign2),

    NAVIGATION("navigator-node", "mdi2f-file-tree", MaterialDesign2),

    COORDINATES("coordinate-crosshairs", "mdi2c-crosshairs-gps", MaterialDesign2),

    // Activity streams
    ACTIVITY("activityStream", "oct-pulse-16", OctIcons),
    ANY_ACTIVITY_STREAM("any-activityStream","mdi2l-link-variant", MaterialDesign2),
    UNLINKED_ACTIVITY_STREAM("unlinked-activityStream","mdi2l-link-variant-off", MaterialDesign2),
    SEARCH_ACTIVITY_STREAM("search-activityStream","mdi2m-magnify", MaterialDesign2),
    NAVIGATION_ACTIVITY_STREAM("navigation-activityStream", "mdi2f-file-tree", MaterialDesign2),
    CLASSIFICATION_ACTIVITY_STREAM("classification-activityStream","fa-gears", Fontawesome),
    CORRELATION_ACTIVITY_STREAM("correlation-activityStream", "mdi2c-compare-horizontal", MaterialDesign2),
    LIST_ACTIVITY_STREAM("list-activityStream", "mdi2s-script-text-outline", MaterialDesign2),
    BUILDER_ACTIVITY_STREAM("builder-activityStream","mdi2s-shape-circle-plus",MaterialDesign2),
    FLWOR_ACTIVITY_STREAM("flwor-activityStream","mdi2f-flower", MaterialDesign2),
    PREFERENCES_ACTIVITY_STREAM("preferences-activityStream","fas-sliders-h",Fontawesome),

    PUBLISH_TO_STREAM("publish-to-activityStream", "mdi2l-location-exit", MaterialDesign2),
    SUBSCRIBE_TO_STREAM("subscribe-to-activityStream", "mdi2l-location-enter", MaterialDesign2),
    SYNCHRONIZE_WITH_STREAM("synchronize-activityStream", "mdi2c-cached", MaterialDesign2),
    ;

    String styleId;
    String iconCode;
    IconCheetSheet cheatSheet;


    Icon(String styleId, String iconCode, IconCheetSheet cheatSheet) {
        this.styleId = styleId;
        this.iconCode = iconCode;
        this.cheatSheet = cheatSheet;
    }

    public String styleId() {
        return styleId;
    }

    public String iconCode() {
        return iconCode;
    }

    public Label makeIcon() {
        FontIcon icon = new FontIcon();
        //icon.setId(this.styleId);
        Label iconLabel = new Label("", icon);
        iconLabel.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        iconLabel.setId(this.styleId);
        return iconLabel;
    }

    public static Label makeIcon(String styleId) {
        FontIcon icon = new FontIcon();
        //icon.setId(styleId);
        Label iconLabel = new Label("", icon);
        iconLabel.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        iconLabel.setId(styleId);
        return iconLabel;
    }

    public static HBox makeIconGroup(String... styleIds) {
        HBox titleNode = new HBox(2);
        for (String styleId: styleIds) {
            titleNode.getChildren().add(makeIcon(styleId));
        }
        return titleNode;
    }

    public static HBox makeIconGroup(Node... icons) {
        HBox titleNode = new HBox(2);
        for (Node icon: icons) {
            titleNode.getChildren().add(icon);
        }
        return titleNode;
    }

    public Label makeIconWithStyles(String... styleClasses) {
        Label label = makeIcon();
        label.getStyleClass().addAll(styleClasses);
        return label;
    }

}
