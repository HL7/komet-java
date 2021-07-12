package org.hl7.komet.framework.activity;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.operators.multi.processors.BroadcastProcessor;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.hl7.komet.framework.graphics.Icon;
import org.hl7.tinkar.common.id.PublicIdStringKey;
import org.hl7.tinkar.terms.EntityFacade;
import org.kordamp.ikonli.javafx.FontIcon;
import org.reactivestreams.FlowAdapters;

import java.util.concurrent.Flow;

public class ActivityStream implements Flow.Publisher<ImmutableList<EntityFacade>> {

    final String streamIconCssId;
    final Multi<ImmutableList<EntityFacade>> entityListStream;
    final BroadcastProcessor<ImmutableList<EntityFacade>> processor;
    final PublicIdStringKey<ActivityStream> activityStreamKey;

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
        processor.onNext(Lists.immutable.of(entities));
    }

    public Node getStreamIcon() {
        return Icon.makeIcon(streamIconCssId);
    }
}
