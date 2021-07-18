package org.hl7.komet.framework;


/**
 * CSS Style Classes for JavaFX nodes.
 * @author kec
 */
public enum StyleClasses {
    AND_CLAUSE, OR_CLAUSE, AND_CLAUSE_CHILD, OR_CLAUSE_CHILD,
    COMPONENT_BADGE, COMPONENT_PANEL, CONCEPT_DETAIL_PANE,
    COMPONENT_DETAIL_BACKGROUND, COMPONENT_VERSION_WHAT_CELL,
    COMPONENT_TEXT, VERSION_PANEL, HEADER_PANEL, HEADER_TEXT, ADD_DESCRIPTION_BUTTON,
    ADD_ATTACHMENT, STAMP_INDICATOR, VERSION_GRAPH_TOGGLE, ADD_TAB_MENU_BUTTON,
    MULTI_PARENT_TREE_NODE, MULTI_PARENT_TREE_CELL, TOP_GRID_PANE, EDIT_COMPONENT_BUTTON, HBOX, ASSEMBLAGE_DETAIL,
    // TODO change CONCEPT_LABEL to ENTITY_LABEL
    CONCEPT_LABEL, ASSEMBLAGE_NAME_TEXT, DEFINITION_TEXT, CONCEPT_TEXT, SEMANTIC_TEXT, ERROR_TEXT,
    DATE_TEXT, CONCEPT_COMPONENT_REFERENCE, SEMANTIC_COMPONENT_REFERENCE, CANCEL_BUTTON, COMMIT_BUTTON,
    ALERT_PANE, ALERT_TITLE, ALERT_DESCRIPTION, MORE_ALERT_DETAILS, ALERT_ICON,

    DEF_ROOT, DEF_ROLE, DEF_ROLE_GROUP, DEF_CONCEPT, DEF_EMBEDDED_CONCEPT, DEF_FEATURE,
    DEF_SUFFICIENT_SET, DEF_NECESSARY_SET, DEF_LITERAL,

    OPEN_CONCEPT_BUTTON,

    INTERVAL_BOUND, MEASURE,

    RESET_SEARCH, NEXT_MATCH, PREVIOUS_MATCH, SEARCH_MAGNIFY,

    NAVIGATION_BADGE,

    CONCEPT_LIST_EDITOR_TOOLBAR,

    DESCRIPTION_CELL,
    DESCRIPTION_TEXT;

    @Override
    public String toString() {
        return name().toLowerCase().replace('_', '-');
    }

}
