package org.hl7.komet.framework.observable;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.set.primitive.IntSet;
import org.eclipse.collections.impl.factory.primitive.IntSets;
import org.hl7.tinkar.collection.ConcurrentReferenceHashMap;
import org.hl7.tinkar.common.id.PublicId;
import org.hl7.tinkar.component.FieldDataType;
import org.hl7.tinkar.coordinate.view.calculator.ViewCalculator;
import org.hl7.tinkar.entity.*;

public abstract class ObservableEntity<O extends ObservableVersion<V>, V extends EntityVersion> implements Entity<O> {

    protected static final ConcurrentReferenceHashMap<PublicId, ObservableEntity> SINGLETONS =
            new ConcurrentReferenceHashMap<>(ConcurrentReferenceHashMap.ReferenceType.WEAK,
                    ConcurrentReferenceHashMap.ReferenceType.WEAK);
    final Entity<V> entity;
    final ObservableList<O> versionProperty = FXCollections.observableArrayList();

    ObservableEntity(Entity<V> entity) {
        this.entity = entity;
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
        ObservableEntity observableEntity = SINGLETONS.computeIfAbsent(entity.publicId(), publicId ->
                switch (entity) {
                    case ConceptEntity conceptEntity -> new ObservableConcept(conceptEntity);
                    case PatternEntity patternEntity -> new ObservablePattern(patternEntity);
                    case SemanticEntity semanticEntity -> new ObservableSemantic(semanticEntity);
                    case StampEntity stampEntity -> new ObservableStamp(stampEntity);
                    default -> throw new UnsupportedOperationException("Can't handle: " + entity);
                });
        if (!observableEntity.stampNids().equals(entity.stampNids())) {
            IntSet stampsInObservable = IntSets.immutable.of(observableEntity.stampNids().toArray());
            IntSet stampsInEntity = IntSets.immutable.of(entity.stampNids().toArray());
            IntSet stampsToAdd = stampsInEntity.reject(stampsInObservable::contains);
            for (EntityVersion ev : entity.versions()) {
                if (stampsToAdd.contains(ev.stampNid())) {
                    observableEntity.versionProperty.add(observableEntity.wrap(ev));
                }
            }
        }

        return (OE) observableEntity;
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
        return entity.getBytes();
    }

    @Override
    public FieldDataType entityDataType() {
        return entity.entityDataType();
    }

    @Override
    public FieldDataType versionDataType() {
        return entity.versionDataType();
    }

    @Override
    public int nid() {
        return entity.nid();
    }

    @Override
    public long mostSignificantBits() {
        return entity.mostSignificantBits();
    }

    @Override
    public long leastSignificantBits() {
        return entity.leastSignificantBits();
    }

    @Override
    public long[] additionalUuidLongs() {
        return entity.additionalUuidLongs();
    }

    public Iterable<ObservableSemantic> getObservableSemanticList() {
        throw new UnsupportedOperationException();
    }
}
