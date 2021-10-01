package org.hl7.komet.framework.activity;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.operators.multi.processors.BroadcastProcessor;
import javafx.scene.Node;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.hl7.komet.framework.graphics.Icon;
import org.hl7.tinkar.common.id.PublicIdStringKey;
import org.hl7.tinkar.terms.EntityFacade;
import org.reactivestreams.FlowAdapters;

import java.util.Optional;
import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicReference;

public class ActivityStream implements Flow.Publisher<ImmutableList<EntityFacade>> {

    final String streamIconCssId;
    final Multi<ImmutableList<EntityFacade>> entityListStream;
    final BroadcastProcessor<ImmutableList<EntityFacade>> processor;
    final PublicIdStringKey<ActivityStream> activityStreamKey;
    final AtomicReference<ImmutableList<EntityFacade>> lastDispatch = new AtomicReference<>(Lists.immutable.empty());

    public ActivityStream(String streamIconCssId, PublicIdStringKey<ActivityStream> activityStreamKey) {
        this.streamIconCssId = streamIconCssId;
        this.activityStreamKey = activityStreamKey;
        this.processor = BroadcastProcessor.create();
        this.entityListStream = processor.toHotStream();
    }

    @Override
    public void subscribe(Flow.Subscriber<? super ImmutableList<EntityFacade>> subscriber) {
        entityListStream.subscribe().withSubscriber(FlowAdapters.toSubscriber(subscriber));
    }

    public void dispatch(EntityFacade... entities) {
        dispatch(Lists.immutable.of(entities));
    }

    public void dispatch(ImmutableList<EntityFacade> entities) {
        lastDispatch.set(entities);
        processor.onNext(entities);
    }

    public ImmutableList<EntityFacade> lastDispatch() {
        return lastDispatch.get();
    }

    public Optional<EntityFacade> lastDispatchOfIndex(int index) {
        ImmutableList<EntityFacade> lastDispatchList = lastDispatch.get();
        if (index < lastDispatchList.size()) {
            return Optional.of(lastDispatchList.get(index));
        }
        return Optional.empty();
    }

    public Node getStreamIcon() {
        return Icon.makeIcon(streamIconCssId);
    }


}
