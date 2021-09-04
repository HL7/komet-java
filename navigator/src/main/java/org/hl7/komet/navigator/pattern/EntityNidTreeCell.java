package org.hl7.komet.navigator.pattern;

import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.TreeCell;
import javafx.scene.image.Image;
import javafx.scene.layout.TilePane;
import javafx.scene.transform.NonInvertibleTransformException;
import org.hl7.komet.framework.dnd.DragDetectedCellEventHandler;
import org.hl7.komet.framework.dnd.DragDoneEventHandler;
import org.hl7.komet.framework.dnd.DraggableWithImage;
import org.hl7.komet.framework.view.ViewProperties;

public class EntityNidTreeCell extends TreeCell<Object>
        implements DraggableWithImage {

    final ViewProperties viewProperties;
    private double dragOffset = 0;
    private TilePane graphicTilePane;
    private String entityDescriptionText; // Cached to speed up updates

    public EntityNidTreeCell(ViewProperties viewProperties) {
        this.viewProperties = viewProperties;
        // Allow drags

        this.setOnDragDetected(new DragDetectedCellEventHandler());
        this.setOnDragDone(new DragDoneEventHandler());
    }

    @Override
    protected void updateItem(Object item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null && !empty) {
            if (item instanceof String stringItem) {
                entityDescriptionText = stringItem;
            } else if (item instanceof Integer nid) {
                if (entityDescriptionText != null) {
                    entityDescriptionText = viewProperties.calculator().getPreferredDescriptionTextWithFallbackOrNid(nid);
                }
            }
        } else {
            entityDescriptionText = "";
        }
        setText(entityDescriptionText);
    }

    @Override
    public Image getDragImage() {
        //TODO see if we can replace this method with DragImageMaker...
        SnapshotParameters snapshotParameters = new SnapshotParameters();

        dragOffset = 0;

        double width = this.getWidth();
        double height = this.getHeight();

        if (graphicTilePane != null) {
            // The height difference and width difference are to account for possible
            // changes in size of an object secondary to a hover (which might cause a
            // -fx-effect:  dropshadow... or similar, whicn will create a difference in the
            // tile pane height, but not cause a change in getLayoutBounds()...
            // I don't know if this is a workaround for a bug, or if this is expected
            // behaviour for some reason...

            double layoutWidth = graphicTilePane.getLayoutBounds()
                    .getWidth();
            double widthDifference = graphicTilePane.getBoundsInParent()
                    .getWidth() - layoutWidth;
            double widthAdjustment = 0;
            if (widthDifference > 0) {
                widthDifference = Math.rint(widthDifference);
                widthAdjustment = widthDifference / 2;
            }

            dragOffset = graphicTilePane.getBoundsInParent()
                    .getMinX() + widthAdjustment;
            width = this.getWidth() - dragOffset;
            height = this.getLayoutBounds().getHeight();
        }

        try {
            snapshotParameters.setTransform(this.getLocalToParentTransform().createInverse());
        } catch (NonInvertibleTransformException ex) {
            throw new RuntimeException(ex);
        }
        snapshotParameters.setViewport(new Rectangle2D(dragOffset - 2, 0, width, height));
        return snapshot(snapshotParameters, null);
    }

    @Override
    public double getDragViewOffsetX() {
        return dragOffset;
    }
}
