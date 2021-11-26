package org.hl7.komet.framework.observable;

import javafx.application.Platform;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.hl7.tinkar.collection.ConcurrentReferenceHashMap;
import org.hl7.tinkar.common.alert.AlertObject;
import org.hl7.tinkar.common.alert.AlertStreams;
import org.hl7.tinkar.component.FieldDataType;
import org.hl7.tinkar.coordinate.view.calculator.ViewCalculator;
import org.hl7.tinkar.entity.*;

import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicReference;

/**
 * TODO: should be a way of listening for changes to the versions of the entity?
 *
 * @param <O>
 * @param <V>
 */
public abstract class ObservableEntity<O extends ObservableVersion<V>, V extends EntityVersion> implements Entity<O> {

    protected static final ConcurrentReferenceHashMap<Integer, ObservableEntity> SINGLETONS =
            new ConcurrentReferenceHashMap<>(ConcurrentReferenceHashMap.ReferenceType.WEAK,
                    ConcurrentReferenceHashMap.ReferenceType.WEAK);
    private static final EntityChangeSubscriber ENTITY_CHANGE_SUBSCRIBER = new EntityChangeSubscriber();

    static {
        Entity.provider().subscribe(ENTITY_CHANGE_SUBSCRIBER);
    }

    final AtomicReference<Entity<V>> entityReference;
    final SimpleListProperty<O> versionProperty = new SimpleListProperty<>(FXCollections.observableArrayList());


    ObservableEntity(Entity<V> entity) {
        this.entityReference = new AtomicReference<>(entity);
        for (V version : entity.versions()) {
            versionProperty.add(wrap(version));
        }
    }

    protected abstract O wrap(V version);

    public static <OE extends ObservableEntity> ObservableEntitySnapshot getSnapshot(int nid, ViewCalculator calculator) {
        return get(Entity.getFast(nid)).getSnapshot(calculator);
    }

    public abstract ObservableEntitySnapshot getSnapshot(ViewCalculator calculator);

    public static <OE extends ObservableEntity> OE get(Entity<? extends EntityVersion> entity) {
        if (entity instanceof ObservableEntity) {
            return (OE) entity;
        }
        ObservableEntity observableEntity = SINGLETONS.computeIfAbsent(entity.nid(), publicId ->
                switch (entity) {
                    case ConceptEntity conceptEntity -> new ObservableConcept(conceptEntity);
                    case PatternEntity patternEntity -> new ObservablePattern(patternEntity);
                    case SemanticEntity semanticEntity -> new ObservableSemantic(semanticEntity);
                    case StampEntity stampEntity -> new ObservableStamp(stampEntity);
                    default -> throw new UnsupportedOperationException("Can't handle: " + entity);
                });
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> updateVersions(entity, observableEntity));
        } else {
            updateVersions(entity, observableEntity);
        }

        return (OE) observableEntity;
    }

    private static void updateVersions(Entity<? extends EntityVersion> entity, ObservableEntity observableEntity) {
        if (!observableEntity.entityReference.get().equals(entity)) {
            observableEntity.entityReference.set(entity);
            observableEntity.versionProperty.clear();
            for (EntityVersion version : entity.versions()) {
                observableEntity.versionProperty.add(observableEntity.wrap(version));
            }
        }
    }

    public static <OE extends ObservableEntity> OE get(int nid) {
        return get(Entity.getFast(nid));
    }

    ObservableList<O> versionProperty() {
        return versionProperty;
    }

    @Override
    public ImmutableList<O> versions() {
        return Lists.immutable.ofAll(versionProperty);
    }

    @Override
    public byte[] getBytes() {
        return entityReference.get().getBytes();
    }

    @Override
    public FieldDataType entityDataType() {
        return entityReference.get().entityDataType();
    }

    @Override
    public FieldDataType versionDataType() {
        return entityReference.get().versionDataType();
    }

    @Override
    public int nid() {
        return entityReference.get().nid();
    }

    @Override
    public long mostSignificantBits() {
        return entityReference.get().mostSignificantBits();
    }

    @Override
    public long leastSignificantBits() {
        return entityReference.get().leastSignificantBits();
    }

    @Override
    public long[] additionalUuidLongs() {
        return entityReference.get().additionalUuidLongs();
    }

    public Iterable<ObservableSemantic> getObservableSemanticList() {
        throw new UnsupportedOperationException();
    }

    public static class EntityChangeSubscriber implements Flow.Subscriber<Integer> {
        Flow.Subscription subscription;

        public Flow.Subscription subscription() {
            return subscription;
        }


        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            this.subscription = subscription;
            this.subscription.request(1);
        }

        @Override
        public void onNext(Integer nid) {
            // Do nothing with item, but request another...
            this.subscription.request(1);

            if (SINGLETONS.contains(nid)) {
                Platform.runLater(() -> {
                    get(Entity.getFast(nid));
                });

            }
        }

        @Override
        public void onError(Throwable throwable) {
            AlertStreams.getRoot().dispatch(AlertObject.makeError(throwable));
        }

        @Override
        public void onComplete() {
            // Do nothing
        }
    }
}