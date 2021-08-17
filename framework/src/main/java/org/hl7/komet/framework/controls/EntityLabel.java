package org.hl7.komet.framework.controls;

import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import org.controlsfx.control.PopOver;
import org.hl7.komet.framework.StyleClasses;
import org.hl7.komet.framework.dnd.DragImageMaker;
import org.hl7.komet.framework.dnd.KometClipboard;
import org.hl7.komet.framework.graphics.Icon;
import org.hl7.komet.framework.menu.MenuSupplierForFocusedEntity;
import org.hl7.komet.framework.panel.axiom.AxiomView;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.tinkar.coordinate.logic.PremiseType;
import org.hl7.tinkar.coordinate.stamp.calculator.Latest;
import org.hl7.tinkar.entity.Entity;
import org.hl7.tinkar.entity.EntityVersion;
import org.hl7.tinkar.entity.SemanticEntityVersion;
import org.hl7.tinkar.terms.EntityFacade;
import org.hl7.tinkar.terms.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class EntityLabel extends Label {
    private static final Logger LOG = LoggerFactory.getLogger(EntityLabel.class);

    private final int entityNid;
    private final Button openConceptButton = new Button("", Icon.LINK_EXTERNAL.makeIcon());
    private final ViewProperties viewProperties;
    private PremiseType premiseType = PremiseType.INFERRED;

    public EntityLabel(EntityFacade entity, ViewProperties viewProperties) {
        this(entity.nid(), viewProperties);
    }

    public EntityLabel(int entityNid, ViewProperties viewProperties) {
        this.entityNid = entityNid;
        this.viewProperties = viewProperties;
        this.setText(viewProperties.calculator().getPreferredDescriptionTextWithFallbackOrNid(entityNid));

        HBox controlBox;
        Latest<EntityVersion> latest = viewProperties.calculator().latest(entityNid);
        if (latest.isPresent()) {
            controlBox = new HBox(openConceptButton, AxiomView.computeGraphic(entityNid, false,
                    latest.get().stamp().state(), viewProperties, premiseType));
        } else {
            controlBox = new HBox(openConceptButton, AxiomView.computeGraphic(entityNid, false,
                    State.PRIMORDIAL, viewProperties, premiseType));
        }

        this.setGraphic(controlBox);
        setOnDragDetected(this::handleDragDetected);
        setOnDragDone(this::handleDragDone);
        openConceptButton.getStyleClass().setAll(StyleClasses.OPEN_CONCEPT_BUTTON.toString());
        openConceptButton.setOnMouseClicked(this::handleShowConceptNodeClick);
        ContextMenu contextMenu = new ContextMenu();
        this.setContextMenu(contextMenu);
        Menu copyMenu = MenuSupplierForFocusedEntity.makeCopyMenuItem(Optional.of(Entity.getFast(this.entityNid)), this.viewProperties);
        contextMenu.getItems().addAll(copyMenu.getItems());
    }

    private void handleDragDetected(MouseEvent event) {
        LOG.debug("Drag detected: " + event);

        DragImageMaker dragImageMaker = new DragImageMaker(this);
        Dragboard db = this.startDragAndDrop(TransferMode.COPY);

        db.setDragView(dragImageMaker.getDragImage());

        KometClipboard content = new KometClipboard((Entity) Entity.getFast(entityNid));
        db.setContent(content);
        event.consume();
    }

    private void handleDragDone(DragEvent event) {
        LOG.debug("Dragging done: " + event);
    }

    private void handleShowConceptNodeClick(MouseEvent mouseEvent) {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            showPopup(entityNid, mouseEvent);
        }
    }

    private void showPopup(int conceptNid, MouseEvent mouseEvent) {
        Latest<SemanticEntityVersion> axiomSemanticForEntity = viewProperties.calculator().getAxiomSemanticForEntity(conceptNid, premiseType);
        if (!axiomSemanticForEntity.isPresent()) {
            premiseType = PremiseType.STATED;
            axiomSemanticForEntity = viewProperties.calculator().getAxiomSemanticForEntity(conceptNid, premiseType);
        }
        if (axiomSemanticForEntity.isPresent()) {
            PopOver popover = new PopOver();
            AxiomView axiomView = AxiomView.createWithCommitPanel(axiomSemanticForEntity.get(),
                    premiseType,
                    viewProperties);
            popover.setContentNode(axiomView.getEditor());
            popover.setCloseButtonEnabled(true);
            popover.setHeaderAlwaysVisible(false);
            popover.setTitle("");
            popover.show(openConceptButton, mouseEvent.getScreenX(), mouseEvent.getScreenY());
            mouseEvent.consume();
        }
    }
}

