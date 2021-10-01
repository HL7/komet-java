package org.hl7.komet.framework.observable;

import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.hl7.tinkar.entity.Entity;
import org.hl7.tinkar.entity.EntityVersion;
import org.hl7.tinkar.terms.ConceptFacade;
import org.hl7.tinkar.terms.State;

public abstract class ObservableVersion<V extends EntityVersion> implements EntityVersion {
    protected final SimpleObjectProperty<V> versionProperty = new SimpleObjectProperty<>();

    final SimpleObjectProperty<State> stateProperty = new SimpleObjectProperty<>();
    final SimpleLongProperty timeProperty = new SimpleLongProperty();
    final SimpleObjectProperty<ConceptFacade> authorProperty = new SimpleObjectProperty<>();
    final SimpleObjectProperty<ConceptFacade> moduleProperty = new SimpleObjectProperty<>();
    final SimpleObjectProperty<ConceptFacade> pathProperty = new SimpleObjectProperty<>();


    ObservableVersion(V entityVersion) {
        versionProperty.set(entityVersion);
        stateProperty.set(entityVersion.state());
        timeProperty.set(entityVersion.time());
        authorProperty.set(Entity.provider().getEntityFast(entityVersion.authorNid()));
        moduleProperty.set(Entity.provider().getEntityFast(entityVersion.moduleNid()));
        pathProperty.set(Entity.provider().getEntityFast(entityVersion.pathNid()));
        addListeners();
    }

    protected abstract void addListeners();

    public ObjectProperty<V> versionProperty() {
        return versionProperty;
    }

    @Override
    public Entity entity() {
        return version().entity();
    }

    public V version() {
        return versionProperty.getValue();
    }

    @Override
    public int stampNid() {
        return version().stampNid();
    }

    @Override
    public Entity chronology() {
        return version().chronology();
    }

    public ObjectProperty<State> stateProperty() {
        return stateProperty;
    }

    public LongProperty timeProperty() {
        return timeProperty;
    }

    public ObjectProperty<ConceptFacade> authorProperty() {
        return authorProperty;
    }

    public ObjectProperty<ConceptFacade> moduleProperty() {
        return moduleProperty;
    }

    public ObjectProperty<ConceptFacade> pathProperty() {
        return pathProperty;
    }
}
