package org.hl7.komet.framework.propsheet;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.util.Callback;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.AbstractPropertyEditor;
import org.controlsfx.property.editor.DefaultPropertyEditorFactory;
import org.controlsfx.property.editor.PropertyEditor;
import org.hl7.komet.framework.controls.EntityLabelWithDragAndDrop;
import org.hl7.komet.framework.panel.axiom.AxiomView;
import org.hl7.komet.framework.propsheet.editor.IntIdListEditor;
import org.hl7.komet.framework.propsheet.editor.IntIdSetEditor;
import org.hl7.komet.framework.propsheet.editor.ListEditor;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.tinkar.common.alert.AlertObject;
import org.hl7.tinkar.common.alert.AlertStreams;
import org.hl7.tinkar.common.id.IntIdList;
import org.hl7.tinkar.common.id.IntIdSet;
import org.hl7.tinkar.component.graph.DiTree;
import org.hl7.tinkar.coordinate.logic.PremiseType;
import org.hl7.tinkar.entity.SemanticEntityVersion;
import org.hl7.tinkar.entity.graph.EntityVertex;
import org.hl7.tinkar.terms.EntityFacade;
import org.hl7.tinkar.terms.EntityProxy;
import org.hl7.tinkar.terms.TinkarTerm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

public class KometPropertyEditorFactory implements Callback<PropertySheet.Item, PropertyEditor<?>> {
    private static final Logger LOG = LoggerFactory.getLogger(KometPropertyEditorFactory.class);
    private final ViewProperties viewProperties;
    DefaultPropertyEditorFactory defaultFactory = new DefaultPropertyEditorFactory();

    public KometPropertyEditorFactory(ViewProperties viewProperties) {
        this.viewProperties = viewProperties;
    }

    @Override
    public PropertyEditor<?> call(PropertySheet.Item item) {
        PropertyEditor<?> propertyEditor;
        Optional<Class<? extends PropertyEditor<?>>> optionalPropertyEditorClass = item.getPropertyEditorClass();
        if (optionalPropertyEditorClass.isPresent() && optionalPropertyEditorClass.get() == KometPropertyEditorFactory.TextFieldEditor.class) {
            propertyEditor = new TextFieldEditor(item);
        } else if (item.getType() == String.class) {
            propertyEditor = createTextAreaEditor(item);
        } else if (item.getPropertyEditorClass().isPresent()) {
            Optional<PropertyEditor<?>> ed = createCustomEditor(item, viewProperties);
            if (ed.isPresent()) {
                propertyEditor = ed.get();
            } else {
                propertyEditor = null;
                AlertStreams.getRoot().dispatch(AlertObject.makeWarning("No editor for item " + item.getName(), item.toString()));
            }
        } else {
            return null;
        }

        if (item instanceof SheetItem sheetItem) {
            if (propertyEditor != null && propertyEditor.getEditor() instanceof Control control) {
                sheetItem.addValidation(control);
            }
        }
        return propertyEditor;
    }

    public static final PropertyEditor<?> createTextAreaEditor(PropertySheet.Item property) {

        return new AbstractPropertyEditor<String, TextArea>(property, new TextArea()) {

            {
                getEditor().setWrapText(true);
                getEditor().setPrefRowCount(2);
                enableAutoSelectAll(getEditor());
            }

            @Override
            protected StringProperty getObservableValue() {
                return getEditor().textProperty();
            }

            @Override
            public void setValue(String value) {
                if (value.length() < 60) {
                    getEditor().setPrefRowCount(1);
                } else {
                    getEditor().setPrefRowCount(2 + value.length() / 80);
                }
                getEditor().setText(value);
            }
        };
    }

    public static final Optional<PropertyEditor<?>> createCustomEditor(final PropertySheet.Item property, final ViewProperties viewProperties) {
        try {
            if (property.getPropertyEditorClass().isPresent()) {
                Class editorClass = property.getPropertyEditorClass().get();
                if (editorClass == ListEditor.class) {
                    return Optional.of(new ListEditor(viewProperties, (SimpleObjectProperty<ObservableList<EntityFacade>>) property.getObservableValue().get()));
                }
                if (editorClass == IntIdSetEditor.class) {
                    return Optional.of(new IntIdSetEditor(viewProperties, (SimpleObjectProperty<IntIdSet>) property.getObservableValue().get()));
                }
                if (editorClass == IntIdListEditor.class) {
                    return Optional.of(new IntIdListEditor(viewProperties, (SimpleObjectProperty<IntIdList>) property.getObservableValue().get()));
                }
                if (editorClass == EntityLabelWithDragAndDrop.class) {
                    return Optional.of(EntityLabelWithDragAndDrop.make(viewProperties, (ObjectProperty<EntityFacade>) property.getObservableValue().get()));
                }
                if (editorClass == AxiomView.class) {
                    //TODO add stated/inferred to root property?
                    DiTree<EntityVertex> axiomTree = (DiTree<EntityVertex>) property.getValue();
                    Optional<EntityProxy.Concept> optionalPremiseType = axiomTree.root().uncommittedProperty(TinkarTerm.PREMISE_TYPE_FOR_MANIFOLD.nid());
                    Optional<SemanticEntityVersion> optionalSemanticVersion = axiomTree.root().uncommittedProperty(TinkarTerm.LOGICAL_EXPRESSION_SEMANTIC.nid());
                    PremiseType premiseType = PremiseType.STATED;
                    if (optionalPremiseType.get().nid() == TinkarTerm.INFERRED_PREMISE_TYPE.nid()) {
                        premiseType = PremiseType.INFERRED;
                    }
                    AxiomView axiomView = AxiomView.create(optionalSemanticVersion.get(), premiseType, viewProperties);
                    return Optional.of(axiomView);
                }
            }
            return property.getPropertyEditorClass().map(cls -> {
                try {
                    Constructor<?> cn = cls.getConstructor(PropertySheet.Item.class, ViewProperties.class);
                    return (PropertyEditor<?>) cn.newInstance(property, viewProperties);
                } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    LOG.debug("No constructor(PropertySheet.Item.class, ViewProperties.class). Will try next pattern.");
                }
                try {
                    Constructor<?> cn = cls.getConstructor(PropertySheet.Item.class);
                    return (PropertyEditor<?>) cn.newInstance(property);
                } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    LOG.debug("No (PropertySheet.Item.class) constructor. Will return null.");
                }
                return null;
            });
        } catch (Exception e) {
            AlertStreams.getRoot().dispatch(AlertObject.makeError(e));
            return Optional.empty();
        }
    }

    private static void enableAutoSelectAll(final TextInputControl control) {
        control.focusedProperty().addListener((ObservableValue<? extends Boolean> o, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                Platform.runLater(() -> {
                    control.selectAll();
                });
            }
        });
    }

    public static class TextFieldEditor extends AbstractPropertyEditor<String, TextField> {
        {
            enableAutoSelectAll(getEditor());
        }

        public TextFieldEditor(PropertySheet.Item property) {
            super(property, new TextField());
        }

        public TextFieldEditor(PropertySheet.Item property, boolean readonly) {
            super(property, new TextField(), readonly);
        }

        @Override
        protected StringProperty getObservableValue() {
            return getEditor().textProperty();
        }

        @Override
        public void setValue(String value) {
            getEditor().setText(value);
        }
    }
}
