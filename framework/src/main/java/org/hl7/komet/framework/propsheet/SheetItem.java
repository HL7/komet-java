package org.hl7.komet.framework.propsheet;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.PropertyEditor;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.hl7.komet.framework.controls.EntityLabelWithDragAndDrop;
import org.hl7.komet.framework.propsheet.editor.ListEditor;
import org.hl7.komet.framework.propsheet.editor.PasswordEditor;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.tinkar.common.id.IntIdCollection;
import org.hl7.tinkar.common.util.text.NaturalOrder;
import org.hl7.tinkar.entity.Field;
import org.hl7.tinkar.terms.EntityFacade;

import java.util.List;
import java.util.Optional;

public class SheetItem<T> implements PropertySheet.Item {

    private final Class<?> classType;
    private final String category;
    private final String name;
    private final String description;
    private final Property<T> property;
    private final Class propertyEditorClass;
    private final ValidationSupport validationSupport;
    private final Validator<T> validator;

    private SheetItem(Class<?> classType, String category, String name,
                      String description, Property<T> property, Class propertyEditorClass,
                      ValidationSupport validationSupport, Validator<T> validator) {
        this.classType = classType;
        this.category = category;
        this.name = name;
        this.description = description;
        this.property = property;
        this.propertyEditorClass = propertyEditorClass;
        this.validationSupport = validationSupport;
        this.validator = validator;
    }

    public static <T> SheetItem<T> make(StringProperty property, ValidationSupport validationSupport, Validator<T> validator) {
        if (validationSupport == null || validator == null) {
            throw new IllegalStateException("Validation and validation support cannot be null");
        }
        return new SheetItem(String.class, null, property.getName(),
                null, property, null, validationSupport, validator);
    }

    public static <T> SheetItem<T> make(Field field, ViewProperties viewProperties) {
        return make(field, null, viewProperties);
    }

    public static <T> SheetItem<T> make(Field field, String category, ViewProperties viewProperties) {
        Class<?> classType;
        // meaning
        String name = viewProperties.calculator().getDescriptionTextOrNid(field.meaningNid());
        // Purpose
        String description = viewProperties.calculator().getDescriptionTextOrNid(field.purposeNid());
        SimpleObjectProperty property = new SimpleObjectProperty(field.value());

        Class propertyEditorClass = null;
        switch (field.fieldDataType()) {
            case STRING:
                classType = String.class;
                propertyEditorClass = null;
                break;
            case CONCEPT:
            case CONCEPT_CHRONOLOGY:
            case CONCEPT_VERSION:
            case SEMANTIC:
            case SEMANTIC_CHRONOLOGY:
            case SEMANTIC_VERSION:
            case PATTERN:
            case PATTERN_CHRONOLOGY:
            case PATTERN_VERSION:
            case IDENTIFIED_THING:
                classType = EntityFacade.class;
                propertyEditorClass = EntityLabelWithDragAndDrop.class;
                break;
            case COMPONENT_ID_LIST:
                // leave list in same order...
                classType = ObservableList.class;
                propertyEditorClass = ListEditor.class;
                if (property.getValue() instanceof IntIdCollection intIdCollection) {
                    property.setValue(FXCollections.observableArrayList(intIdCollection.mapToList(nid -> EntityFacade.make(nid))));
                }
                break;
            case COMPONENT_ID_SET:
                // sort set for presentation, order does not matter in set.
                classType = ObservableList.class;
                propertyEditorClass = ListEditor.class;
                if (property.getValue() instanceof IntIdCollection intIdCollection) {
                    List<EntityFacade> facades = intIdCollection.mapToList(nid -> EntityFacade.make(nid));
                    facades.sort((o1, o2) -> NaturalOrder.compareStrings(viewProperties.calculator().getDescriptionTextOrNid(o1),
                            viewProperties.calculator().getDescriptionTextOrNid(o2)));

                    property.setValue(FXCollections.observableArrayList(facades));
                }
                break;
            default:
                classType = Object.class;
                propertyEditorClass = null;
        }
        ValidationSupport validationSupport = null;
        Validator<T> validator = null;

        return new SheetItem<>(classType, category, name,
                description, property, propertyEditorClass,
                validationSupport, validator);
    }

    public static <T> SheetItem<T> make(StringProperty property) {

        return new SheetItem(String.class, null, property.getName(),
                null, property, null, null, null);
    }

    public static <T> SheetItem<T> makeForPassword(StringProperty property) {

        return new SheetItem(String.class, null, property.getName(),
                null, property, PasswordEditor.class, null, null);
    }

    public static <T> SheetItem<T> makeForPassword(StringProperty property, ValidationSupport validationSupport, Validator<T> validator) {

        return new SheetItem(String.class, null, property.getName(),
                null, property, PasswordEditor.class, validationSupport, validator);
    }

    @Override
    public Class<?> getType() {
        return classType;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public T getValue() {
        return property.getValue();
    }

    @Override
    public void setValue(Object o) {
        property.setValue((T) o);
    }

    @Override
    public Optional<ObservableValue<? extends Object>> getObservableValue() {
        return Optional.ofNullable(property);
    }

    public Optional<Class<? extends PropertyEditor<?>>> getPropertyEditorClass() {
        return Optional.ofNullable(this.propertyEditorClass);
    }

    public void set(T o) {
        property.setValue(o);
    }

    public void addValidation(Control control) {
        if (this.validationSupport != null) {
            this.validationSupport.registerValidator(control, validator);
        }
    }

}
