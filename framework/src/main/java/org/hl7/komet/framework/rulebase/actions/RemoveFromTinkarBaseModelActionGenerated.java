package org.hl7.komet.framework.rulebase.actions;

import javafx.event.ActionEvent;
import org.eclipse.collections.api.factory.Lists;
import org.hl7.tinkar.common.service.TinkExecutor;
import org.hl7.tinkar.common.service.PrimitiveData;
import org.hl7.tinkar.coordinate.edit.EditCoordinate;
import org.hl7.tinkar.coordinate.edit.EditCoordinateImmutable;
import org.hl7.tinkar.coordinate.stamp.calculator.Latest;
import org.hl7.tinkar.coordinate.view.ViewCoordinateRecord;
import org.hl7.tinkar.coordinate.view.calculator.ViewCalculator;
import org.hl7.tinkar.entity.*;
import org.hl7.tinkar.entity.transaction.CommitTransactionTask;
import org.hl7.tinkar.entity.transaction.Transaction;
import org.hl7.tinkar.terms.State;

import static org.hl7.tinkar.terms.TinkarTerm.TINKAR_BASE_MODEL_COMPONENT_PATTERN;

public class RemoveFromTinkarBaseModelActionGenerated extends AbstractActionSuggested {
    final ConceptEntityVersion conceptVersion;

    public RemoveFromTinkarBaseModelActionGenerated(ConceptEntityVersion conceptVersion, ViewCalculator viewCalculator, EditCoordinate editCoordinate) {
        super("Remove from Tinkar base model", viewCalculator, editCoordinate);
        this.conceptVersion = conceptVersion;
    }

    @Override
    public void doAction(ActionEvent actionEvent, EditCoordinateImmutable editCoordinate) {
        int[] semanticNidsForComponent = PrimitiveData.get().semanticNidsForComponentOfPattern(conceptVersion.nid(), TINKAR_BASE_MODEL_COMPONENT_PATTERN.nid());
        if (semanticNidsForComponent.length == 0) {
            // case 1: never a member
            throw new IllegalStateException("Asking to retire element that was never a member...");
        } else {
            updateSemantic(semanticNidsForComponent[0], editCoordinate.toEditCoordinateImmutable());
        }
    }


    private void updateSemantic(int semanticNid, EditCoordinateImmutable editCoordinateImmutable) {
        SemanticRecord semanticEntity = Entity.getFast(semanticNid);
        Transaction transaction = Transaction.make();
        ViewCoordinateRecord viewRecord = viewCalculator.viewCoordinateRecord();

        Latest<PatternEntityVersion> latestPatternVersion = viewCalculator.latestPatternEntityVersion(TINKAR_BASE_MODEL_COMPONENT_PATTERN);
        latestPatternVersion.ifPresentOrElse(patternEntityVersion -> {
            StampEntity stampEntity = transaction.getStamp(State.INACTIVE, Long.MAX_VALUE, editCoordinateImmutable.getAuthorNidForChanges(),
                    patternEntityVersion.moduleNid(), viewRecord.stampCoordinate().pathNidForFilter());
            SemanticVersionRecord newSemanticVersion = new SemanticVersionRecord(semanticEntity, stampEntity.nid(), Lists.immutable.empty());
            SemanticRecord analogue = semanticEntity.with(newSemanticVersion).build();
            transaction.addComponent(analogue);
            Entity.provider().putEntity(analogue);
        }, () -> {
            throw new IllegalStateException("No latest pattern version for: " + Entity.getFast(TINKAR_BASE_MODEL_COMPONENT_PATTERN));
        });
        CommitTransactionTask commitTransactionTask = new CommitTransactionTask(transaction);
        TinkExecutor.threadPool().submit(commitTransactionTask);
    }

}
