package org.hl7.komet.list;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.Label;
import org.hl7.komet.collection.CollectionNode;
import org.hl7.komet.collection.CollectionType;
import org.hl7.komet.framework.propsheet.editor.IntIdCollectionEditor;
import org.hl7.komet.framework.propsheet.editor.IntIdListEditor;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.komet.preferences.KometPreferences;
import org.hl7.tinkar.common.id.IntIdList;
import org.hl7.tinkar.common.id.IntIds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ListNode extends CollectionNode<IntIdList> {
    private static final Logger LOG = LoggerFactory.getLogger(ListNode.class);
    protected static final String TITLE = "List Manager";

    public ListNode(ViewProperties viewProperties, KometPreferences nodePreferences) {
        super(viewProperties, nodePreferences);
        this.collectionItemsProperty.set(IntIds.list.empty());
    }

    @Override
    protected IntIdCollectionEditor<IntIdList> getCollectionEditor(ViewProperties viewProperties, SimpleObjectProperty<IntIdList> listItems) {
        return new IntIdListEditor(viewProperties, listItems);
    }

    @Override
    protected CollectionType getCollectionType() {
        return CollectionType.LIST;
    }

    public Node getMenuIconGraphic() {
        Label menuIcon = new Label("(…)");
        return menuIcon;
    }

    @Override
    public Class factoryClass() {
        return ListNodeFactory.class;
    }

    @Override
    public String getDefaultTitle() {
        return TITLE;
    }
}