package org.hl7.komet.framework.graphics;

import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import org.kordamp.ikonli.javafx.FontIcon;

import static org.hl7.komet.framework.graphics.IconCheetSheet.*;

public enum Icon {


    COORDINATES("detail-coordinates", "ri-viewpoint", RunestroIcons),
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


    ;

    String styleId;
    String fxIconCode;
    IconCheetSheet cheatSheet;


    Icon(String styleId, String fxIconCode, IconCheetSheet cheatSheet) {
        this.styleId = styleId;
        this.fxIconCode = fxIconCode;
        this.cheatSheet = cheatSheet;
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

    public Label makeIconWithStyles(String... styleClasses) {
        Label label = makeIcon();
        label.getStyleClass().addAll(styleClasses);
        return label;
    }

}
