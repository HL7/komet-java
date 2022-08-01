package org.hl7.komet.framework.rulebase.actions;

import javafx.event.ActionEvent;
import org.eclipse.collections.api.list.ImmutableList;
import org.hl7.komet.framework.activity.ActivityStreams;
import org.hl7.komet.framework.builder.AxiomBuilderRecord;
import org.hl7.komet.framework.builder.ConceptEntityBuilder;
import org.hl7.komet.framework.performance.impl.RequestRecord;
import org.hl7.komet.framework.rulebase.GeneratedActionImmediate;
import org.hl7.tinkar.common.service.TinkExecutor;
import org.hl7.tinkar.coordinate.edit.EditCoordinate;
import org.hl7.tinkar.coordinate.edit.EditCoordinateRecord;
import org.hl7.tinkar.coordinate.view.calculator.ViewCalculator;
import org.hl7.tinkar.entity.Entity;
import org.hl7.tinkar.entity.StampEntity;
import org.hl7.tinkar.entity.transaction.Transaction;
import org.hl7.tinkar.terms.EntityFacade;
import org.hl7.tinkar.terms.State;
import org.hl7.tinkar.terms.TinkarTerm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hl7.komet.framework.activity.ActivityStreams.BUILDER;

public class NewConceptFromTextActionGenerated extends AbstractActionImmediate implements GeneratedActionImmediate {
    private static final Logger LOG = LoggerFactory.getLogger(NewConceptFromTextActionGenerated.class);
    final RequestRecord newConceptRequest;
    final String newConceptText;

    public NewConceptFromTextActionGenerated(RequestRecord newConceptRequest, ViewCalculator viewCalculator, EditCoordinate editCoordinate) {
        super("New concept from text", viewCalculator, editCoordinate);
        this.newConceptRequest = newConceptRequest;
        if (newConceptRequest.subject() instanceof String newConceptText) {
            this.newConceptText = newConceptText;
        } else {
            throw new IllegalStateException("newConceptRequest.subject() is not instanceof String");
        }
    }

    public final void doAction(ActionEvent actionEvent, EditCoordinateRecord editCoordinate) {
        TinkExecutor.threadPool().execute(() -> {
            Transaction transaction = Transaction.make("New concept for: " + newConceptText);
            StampEntity stampEntity = transaction.getStamp(State.ACTIVE, editCoordinate.getAuthorNidForChanges(), editCoordinate.getDefaultModuleNid(), editCoordinate.getDefaultPathNid());
            Entity.provider().putStamp(stampEntity);

            ConceptEntityBuilder newConceptBuilder = ConceptEntityBuilder.builder(stampEntity);
            // TODO automate UK dialect at some point.
            newConceptBuilder.makeRegularName(newConceptText);

            AxiomBuilderRecord ab = newConceptBuilder.axiomBuilder();
            ab.withNecessarySet(
                    ab.makeConceptReference(TinkarTerm.LANGUAGE),
                    ab.makeConceptReference(TinkarTerm.DESCRIPTION_ASSEMBLAGE),
                    ab.makeRoleGroup(
                            ab.makeSome(TinkarTerm.PART_OF, TinkarTerm.UNMODELED_ROLE_CONCEPT),
                            ab.makeSome(TinkarTerm.PART_OF, TinkarTerm.LANGUAGE)));


            ab.withSufficientSet(
                    ab.makeConceptReference(TinkarTerm.LANGUAGE),
                    ab.makeConceptReference(TinkarTerm.DESCRIPTION_ASSEMBLAGE),
                    ab.makeSome(TinkarTerm.ROLE_TYPE, TinkarTerm.GB_ENGLISH_DIALECT),
                    ab.makeRoleGroup(
                            ab.makeSome(TinkarTerm.PART_OF, TinkarTerm.DYNAMIC_REFERENCED_COMPONENT_RESTRICTION),
                            ab.makeSome(TinkarTerm.PART_OF, TinkarTerm.INTRINSIC_ROLE)));

            // Build turns into EntityRecords, which get written, added to transaction, but not committed.
            // Have build return a list of entity records, with the top component returned first.

            ImmutableList<EntityFacade> builtEntities = newConceptBuilder.build();
            LOG.info("Built: " + builtEntities);
            // focus the panel on the new concept somehow... ?
            // Associate the transaction with the action event somehow... ?
            /*
            CommitTask commitTask = new CommitTask(transaction);
            Future<Void> future = Executor.threadPool().submit(commitTask);
            try {
                future.get();
            } catch (Exception e) {
                AlertStreams.getRoot().dispatch(AlertObject.makeError(e));
            }
            */
            ActivityStreams.get(BUILDER).dispatch(builtEntities);
            //AlertStreams.getRoot().dispatch(AlertObject.makeError(new UnsupportedOperationException("NewConceptFromTextActionGenerated is not completely validated yet. ")));
        });

    }

}
