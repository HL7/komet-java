package sh.komet.app.propsheet;

import javafx.scene.control.Control;
import javafx.util.Callback;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.DefaultPropertyEditorFactory;
import org.controlsfx.property.editor.PropertyEditor;

public class PropertyEditorFactoryWithValidation implements Callback<PropertySheet.Item, PropertyEditor<?>> {

    DefaultPropertyEditorFactory defaultFactory = new DefaultPropertyEditorFactory();

    @Override public PropertyEditor<?> call(PropertySheet.Item item) {
        PropertyEditor<?> propertyEditor = defaultFactory.call(item);

        if (item instanceof SheetItem sheetItem) {
            if (propertyEditor.getEditor() instanceof Control control) {
                sheetItem.addValidation(control);
            }
        }

        return propertyEditor;
    }

}
