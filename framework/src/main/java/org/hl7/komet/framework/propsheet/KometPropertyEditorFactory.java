package org.hl7.komet.framework.propsheet;

import javafx.beans.property.ObjectProperty;
import javafx.scene.control.Control;
import javafx.util.Callback;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.DefaultPropertyEditorFactory;
import org.controlsfx.property.editor.Editors;
import org.controlsfx.property.editor.PropertyEditor;
import org.hl7.komet.framework.controls.EntityLabelWithDragAndDrop;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.tinkar.terms.EntityFacade;
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
        return property.getPropertyEditorClass().map(cls -> {
            if (cls == EntityLabelWithDragAndDrop.class) {
                return EntityLabelWithDragAndDrop.make(viewProperties, (ObjectProperty<EntityFacade>) property.getObservableValue().get());
            }
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
