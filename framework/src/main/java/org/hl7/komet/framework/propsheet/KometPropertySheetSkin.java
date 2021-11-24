package org.hl7.komet.framework.propsheet;


import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.control.SegmentedButton;
import org.controlsfx.control.action.Action;
import org.controlsfx.control.action.ActionUtils;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.property.editor.AbstractPropertyEditor;
import org.controlsfx.property.editor.PropertyEditor;
import org.hl7.komet.framework.graphics.Icon;
import org.hl7.komet.framework.panel.axiom.AxiomView;
import org.hl7.komet.framework.propsheet.editor.IntIdListEditor;
import org.hl7.komet.framework.propsheet.editor.IntIdSetEditor;
import org.hl7.komet.framework.propsheet.editor.ListEditor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.hl7.komet.framework.StyleClasses.PROP_SHEET_PROPERTY_NAME;

public class KometPropertySheetSkin extends SkinBase<PropertySheet> {

    /**************************************************************************
     *
     * Static fieldValues
     *
     **************************************************************************/

    private static final int MIN_COLUMN_WIDTH = 100;

    /**************************************************************************
     *
     * fieldValues
     *
     **************************************************************************/

    private final BorderPane content;
    private final ToolBar toolbar;
    private final SegmentedButton modeButton = ActionUtils.createSegmentedButton(
            new KometPropertySheetSkin.ActionChangeMode(PropertySheet.Mode.NAME),
            new KometPropertySheetSkin.ActionChangeMode(PropertySheet.Mode.CATEGORY)
    );
    private final TextField searchField = TextFields.createClearableTextField();


    /**************************************************************************
     *
     * Constructors
     *
     **************************************************************************/

    public KometPropertySheetSkin(final PropertySheet control) {
        super(control);

        toolbar = new ToolBar();
        toolbar.managedProperty().bind(toolbar.visibleProperty());
        toolbar.setFocusTraversable(true);

        // property sheet mode
        modeButton.managedProperty().bind(modeButton.visibleProperty());
        modeButton.getButtons().get(getSkinnable().modeProperty().get().ordinal()).setSelected(true);
        toolbar.getItems().add(modeButton);

        // property sheet search
        searchField.setPromptText("Search"); //$NON-NLS-1$
        searchField.setMinWidth(0);
        HBox.setHgrow(searchField, Priority.SOMETIMES);
        searchField.managedProperty().bind(searchField.visibleProperty());
        toolbar.getItems().add(searchField);

        // layout controls
        content = new BorderPane();
        content.setTop(toolbar);
        getChildren().add(content);

        // setup listeners
        registerChangeListener(control.modeProperty(), e -> refreshProperties());
        registerChangeListener(control.propertyEditorFactory(), e -> refreshProperties());
        registerChangeListener(control.titleFilter(), e -> refreshProperties());
        registerChangeListener(searchField.textProperty(), e -> getSkinnable().setTitleFilter(searchField.getText()));
        registerChangeListener(control.modeSwitcherVisibleProperty(), e -> updateToolbar());
        registerChangeListener(control.searchBoxVisibleProperty(), e -> updateToolbar());
        registerChangeListener(control.categoryComparatorProperty(), e -> refreshProperties());

        control.getItems().addListener((ListChangeListener<PropertySheet.Item>) change -> refreshProperties());

        // initialize properly
        refreshProperties();
        updateToolbar();
    }

    private void refreshProperties() {
        content.setCenter(buildPropertySheetContainer());
    }

    /**************************************************************************
     *
     * Implementation
     *
     **************************************************************************/

    private void updateToolbar() {
        modeButton.setVisible(getSkinnable().isModeSwitcherVisible());
        searchField.setVisible(getSkinnable().isSearchBoxVisible());

        toolbar.setVisible(modeButton.isVisible() || searchField.isVisible());
    }

    private Node buildPropertySheetContainer() {
        switch (getSkinnable().modeProperty().get()) {
            case CATEGORY: {
                // group by category
                Map<String, List<PropertySheet.Item>> categoryMap = new TreeMap(getSkinnable().getCategoryComparator());
                for (PropertySheet.Item p : getSkinnable().getItems()) {
                    String category = p.getCategory();
                    List<PropertySheet.Item> list = categoryMap.get(category);
                    if (list == null) {
                        list = new ArrayList<>();
                        categoryMap.put(category, list);
                    }
                    list.add(p);
                }

                // WAS: create category-based accordion; KEC want to support more than one pane open.
                VBox categoryPanes = new VBox(2);
                for (String category : categoryMap.keySet()) {
                    KometPropertySheetSkin.PropertyPane props = new KometPropertySheetSkin.PropertyPane(categoryMap.get(category));
                    // Only show non-empty categories
                    if (props.getChildrenUnmodifiable().size() > 0) {
                        TitledPane pane = new TitledPane(category, props);
                        pane.setExpanded(true);
                        categoryPanes.getChildren().add(pane);
                    }
                }
                return categoryPanes;
            }

            default:
                return new KometPropertySheetSkin.PropertyPane(getSkinnable().getItems());
        }

    }

    /**************************************************************************
     *
     * Overriding public API
     *
     **************************************************************************/

    @Override
    protected void layoutChildren(double x, double y, double w, double h) {
        content.resizeRelocate(x, y, w, h);
    }

    /**************************************************************************
     *
     * Support classes / enums
     *
     **************************************************************************/

    private class ActionChangeMode extends Action {

        private final Label CATEGORY_IMAGE = Icon.INDENT_INCREASE.makeIcon();
        private final Label NAME_IMAGE = Icon.BY_NAME.makeIcon();

        public ActionChangeMode(PropertySheet.Mode mode) {
            super(""); //$NON-NLS-1$
            setEventHandler(ae -> getSkinnable().modeProperty().set(mode));

            if (mode == PropertySheet.Mode.CATEGORY) {
                setGraphic(CATEGORY_IMAGE);
                setLongText("By Category"); //$NON-NLS-1$
            } else if (mode == PropertySheet.Mode.NAME) {
                setGraphic(NAME_IMAGE);
                setLongText("By Name"); //$NON-NLS-1$
            } else {
                setText("???"); //$NON-NLS-1$
            }
        }

    }


    private class PropertyPane extends GridPane {

        public PropertyPane(List<PropertySheet.Item> properties) {
            this(properties, 0);
        }

        public PropertyPane(List<PropertySheet.Item> properties, int nestingLevel) {
            setVgap(5);
            setHgap(5);
            setPadding(new Insets(5, 15, 5, 15 + nestingLevel * 10));
            getStyleClass().add("property-pane"); //$NON-NLS-1$
            setItems(properties);
//            setGridLinesVisible(true);
        }

        public void setItems(List<PropertySheet.Item> properties) {
            getChildren().clear();

            String filter = getSkinnable().titleFilter().get();
            filter = filter == null ? "" : filter.trim().toLowerCase(); //$NON-NLS-1$

            int row = 0;

            for (PropertySheet.Item item : properties) {
                Node editor = getEditor(item);

                // filter properties
                String title = item.getName();

                if (!filter.isEmpty() && title.toLowerCase().indexOf(filter) < 0) continue;

                // setup property label
                Label label = new Label(title + ": ");
                label.setAlignment(Pos.CENTER_RIGHT);
                label.setWrapText(true);
                label.setMinWidth(MIN_COLUMN_WIDTH);
                label.getStyleClass().add(PROP_SHEET_PROPERTY_NAME.toString());

                // show description as a tooltip
                String description = item.getDescription();
                if (description != null && !description.trim().isEmpty()) {
                    label.setTooltip(new Tooltip(description));
                }

                GridPane.setHalignment(label, HPos.RIGHT);
                item.getPropertyEditorClass().ifPresent((Class aClass) -> {
                    if (aClass == AxiomView.class ||
                            aClass == ListEditor.class ||
                            aClass == IntIdListEditor.class ||
                            aClass == IntIdSetEditor.class) {
                        label.setMaxWidth(100);
                        GridPane.setValignment(label, VPos.TOP);
                    }
                });
                add(label, 0, row);

                // setup property editor

                if (editor instanceof Region) {
                    ((Region) editor).setMinWidth(MIN_COLUMN_WIDTH);
                    ((Region) editor).setMaxWidth(Double.MAX_VALUE);
                }
                label.setLabelFor(editor);
                add(editor, 1, row);
                GridPane.setHgrow(editor, Priority.ALWAYS);

                //TODO add support for recursive properties

                row++;
            }

        }

        @SuppressWarnings("unchecked")
        private Node getEditor(PropertySheet.Item item) {
            @SuppressWarnings("rawtypes")
            PropertyEditor editor = getSkinnable().getPropertyEditorFactory().call(item);
            if (editor == null) {
                editor = new AbstractPropertyEditor<Object, TextField>(item, new TextField(), true) {
                    {
                        getEditor().setEditable(false);
                        getEditor().setDisable(true);
                    }

                    /**
                     * {@inheritDoc}
                     */
                    @Override
                    protected ObservableValue<Object> getObservableValue() {
                        return (ObservableValue<Object>) (Object) getEditor().textProperty();
                    }

                    /**
                     * {@inheritDoc}
                     */
                    @Override
                    public void setValue(Object value) {
                        getEditor().setText(value == null ? "" : value.toString()); //$NON-NLS-1$
                    }
                };
            } else if (!item.isEditable()) {
                editor.getEditor().setDisable(true);
            }
            editor.setValue(item.getValue());
            return editor.getEditor();
        }
    }
}
