package org.hl7.komet.set;


import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.Label;
import org.hl7.komet.collection.CollectionNode;
import org.hl7.komet.collection.CollectionType;
import org.hl7.komet.framework.propsheet.editor.IntIdCollectionEditor;
import org.hl7.komet.framework.propsheet.editor.IntIdSetEditor;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.komet.preferences.KometPreferences;
import org.hl7.tinkar.common.id.IntIdSet;
import org.hl7.tinkar.common.id.IntIds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetNode extends CollectionNode<IntIdSet> {
    private static final Logger LOG = LoggerFactory.getLogger(SetNode.class);
    protected static final String TITLE = "Set Manager";

    public SetNode(ViewProperties viewProperties, KometPreferences nodePreferences) {
        super(viewProperties, nodePreferences);
        this.collectionItemsProperty.set(IntIds.set.empty());
    }

    @Override
    protected IntIdCollectionEditor<IntIdSet> getCollectionEditor(ViewProperties viewProperties,
                                                                  SimpleObjectProperty<IntIdSet> collectionItems) {
        return new IntIdSetEditor(viewProperties, collectionItems);
    }

    @Override
    protected CollectionType getCollectionType() {
        return CollectionType.SET;
    }

    public Node getMenuIconGraphic() {
        Label menuIcon = new Label("{…}");
        return menuIcon;
    }

    @Override
    public Class factoryClass() {
        return SetNodeFactory.class;
    }

    @Override
    public String getDefaultTitle() {
        return TITLE;
    }
}