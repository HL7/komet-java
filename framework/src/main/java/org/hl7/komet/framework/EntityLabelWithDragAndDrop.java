package org.hl7.komet.framework;


import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.Dragboard;
import javafx.stage.WindowEvent;
import org.controlsfx.property.editor.PropertyEditor;
import org.hl7.komet.framework.activity.ActivityStream;
import org.hl7.komet.framework.activity.ActivityStreams;
import org.hl7.komet.framework.dnd.DragAndDropHelper;
import org.hl7.komet.framework.dnd.KometClipboard;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.tinkar.common.id.PublicIdStringKey;
import org.hl7.tinkar.terms.EntityFacade;

import java.util.Optional;
import java.util.function.Consumer;

import static org.hl7.komet.framework.StyleClasses.CONCEPT_LABEL;

//~--- non-JDK imports --------------------------------------------------------

//~--- classes ----------------------------------------------------------------
/**
 *
 * @author kec
 */
public class EntityLabelWithDragAndDrop
        extends Label implements PropertyEditor<EntityFacade> {

    public static final String EMPTY_TEXT = "empty";
    final ViewProperties viewProperties;
    final SimpleObjectProperty<EntityFacade> entityFocusProperty;
    final Consumer<EntityLabelWithDragAndDrop> descriptionTextUpdater;
    final DragAndDropHelper dragAndDropHelper;
    final SimpleIntegerProperty selectionIndexProperty;
    final Runnable unlink;
    final AddToContextMenu[] contextMenuProviders;


    //~--- constructors --------------------------------------------------------
    public EntityLabelWithDragAndDrop(ViewProperties viewProperties,
                                      SimpleObjectProperty<EntityFacade> entityFocusProperty,
                                      Consumer<EntityLabelWithDragAndDrop> descriptionTextUpdater,
                                      SimpleIntegerProperty selectionIndexProperty,
                                      Runnable unlink,
                                      AddToContextMenu[] contextMenuProviders) {
        super(EMPTY_TEXT);
        this.viewProperties = viewProperties;
        this.entityFocusProperty = entityFocusProperty;
        this.descriptionTextUpdater = descriptionTextUpdater;
        this.selectionIndexProperty = selectionIndexProperty;
        this.unlink = unlink;
        this.contextMenuProviders = contextMenuProviders;
        this.descriptionTextUpdater.accept(this);
        this.entityFocusProperty.addListener((observable, oldValue, newValue) -> {
            this.descriptionTextUpdater.accept(this);
        });
        this.getStyleClass().add(CONCEPT_LABEL.toString());
        this.dragAndDropHelper = new DragAndDropHelper(this, () -> {
            Optional<EntityFacade> optionalConcept = Optional.ofNullable(entityFocusProperty.get());

            if (optionalConcept.isPresent()) {
                return optionalConcept.get();
            }
            return null;

        } , this::droppedValue, mouseEvent -> true,
                dragEvent -> true);


        this.setMinWidth(100);

        ContextMenu contextMenu = new ContextMenu();

        for (PublicIdStringKey<ActivityStream> activityFeedKey : ActivityStreams.KEYS) {
            MenuItem item = new MenuItemWithText(activityFeedKey.getString() + " history");
            contextMenu.getItems().add(item);
        }

        this.setContextMenu(contextMenu);
        contextMenu.setOnShowing(this::handle);
    }

    //~--- methods -------------------------------------------------------------

    void droppedValue(Dragboard dragboard) {
        this.unlink.run();
        if (dragboard.hasContent(KometClipboard.KOMET_CONCEPT_PROXY)) {
            setValue((EntityFacade) dragboard.getContent(KometClipboard.KOMET_CONCEPT_PROXY));
        } else if (dragboard.hasContent(KometClipboard.KOMET_SEMANTIC_PROXY)) {
            setValue((EntityFacade) dragboard.getContent(KometClipboard.KOMET_SEMANTIC_PROXY));
        } else if (dragboard.hasContent(KometClipboard.KOMET_PATTERN_PROXY)) {
            setValue((EntityFacade) dragboard.getContent(KometClipboard.KOMET_PATTERN_PROXY));
        } else {
            setValue(null);
        }

    }


    private void handle(WindowEvent event) {
        ContextMenu contextMenu = (ContextMenu) event.getSource();
        contextMenu.getItems().clear();
        for (AddToContextMenu contextMenuProvider: contextMenuProviders) {
            contextMenuProvider.addToContextMenu(contextMenu, this.viewProperties,
                    this.entityFocusProperty, this.selectionIndexProperty, this.unlink);
        }

    }

    public void setEntity(EntityFacade entityFacade) {
        this.entityFocusProperty.set(entityFacade);
    }

    //~--- set methods ---------------------------------------------------------
    private void setDescriptionText(String latestDescriptionText) {
        this.setText(latestDescriptionText);
    }

    private void setEmptyText() {
        setLabelToEmptyText(this);
    }

    private static void setLabelToEmptyText(Label label) {
        label.setText(EMPTY_TEXT);
    }

    public static void setFullyQualifiedText(EntityLabelWithDragAndDrop label) {

        Optional<EntityFacade> optionalEntity = Optional.ofNullable(label.entityFocusProperty.getValue());
        if (optionalEntity.isPresent()) {
            label.setText(
                label.viewProperties.nodeView().getFullyQualifiedNameTextOrNid(optionalEntity.get().nid())
            );
        } else {
            setLabelToEmptyText(label);
        }
    }

    public static void setPreferredText(EntityLabelWithDragAndDrop label) {
        Optional<EntityFacade> optionalConcept = Optional.ofNullable(label.entityFocusProperty.getValue());
        if (optionalConcept.isPresent()) {
            label.setText(
                label.viewProperties.nodeView().getPreferredDescriptionStringOrNid(optionalConcept.get().nid())
            );
        } else {
            setLabelToEmptyText(label);
        }
    }

    @Override
    public Node getEditor() {
        return this;
    }

    @Override
    public EntityFacade getValue() {
        return this.entityFocusProperty.getValue();
    }

    @Override
    public void setValue(EntityFacade value) {
        this.setEntity(value);
    }
}
