package org.hl7.komet.framework.propsheet;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.util.Callback;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.DefaultPropertyEditorFactory;
import org.controlsfx.property.editor.Editors;
import org.controlsfx.property.editor.PropertyEditor;
import org.hl7.komet.framework.controls.EntityLabelWithDragAndDrop;
import org.hl7.komet.framework.panel.axiom.AxiomView;
import org.hl7.komet.framework.propsheet.editor.ListEditor;
import org.hl7.komet.framework.view.ViewProperties;
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

        if (item.getType() == String.class) {
            propertyEditor = Editors.createTextEditor(item);
        } else if (item.getPropertyEditorClass().isPresent()) {
            Optional<PropertyEditor<?>> ed = createCustomEditor(item, viewProperties);
            propertyEditor = ed.get();
        } else {
            return null;
        }

        if (item instanceof SheetItem sheetItem) {
            if (propertyEditor.getEditor() instanceof Control control) {
                sheetItem.addValidation(control);
            }
        }
        return propertyEditor;
    }

    public static final Optional<PropertyEditor<?>> createCustomEditor(final PropertySheet.Item property, final ViewProperties viewProperties) {
        if (property.getPropertyEditorClass().isPresent()) {
            Class editorClass = property.getPropertyEditorClass().get();
            if (editorClass == ListEditor.class) {
                return Optional.of(new ListEditor(viewProperties, (SimpleObjectProperty<ObservableList<EntityFacade>>) property.getObservableValue().get()));
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
    }
}
