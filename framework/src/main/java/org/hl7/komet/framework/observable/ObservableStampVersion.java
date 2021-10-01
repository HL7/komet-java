package org.hl7.komet.framework.observable;

import org.hl7.tinkar.entity.StampVersionRecord;

public class ObservableStampVersion
        extends ObservableVersion<StampVersionRecord> {

    ObservableStampVersion(StampVersionRecord stampVersion) {
        super(stampVersion);
    }

    protected void addListeners() {
        stateProperty.addListener((observable, oldValue, newValue) -> {
            versionProperty.set(version().withStateNid(newValue.nid()));
        });

        timeProperty.addListener((observable, oldValue, newValue) -> {
            // TODO when to update the chronology with new record? At commit time? Automatically with reactive stream for commits?
            versionProperty.set(version().withTime(newValue.longValue()));
        });

        authorProperty.addListener((observable, oldValue, newValue) -> {
            versionProperty.set(version().withAuthorNid(newValue.nid()));
        });

        moduleProperty.addListener((observable, oldValue, newValue) -> {
            versionProperty.set(version().withModuleNid(newValue.nid()));
        });

        pathProperty.addListener((observable, oldValue, newValue) -> {
            versionProperty.set(version().withPathNid(newValue.nid()));
        });
    }
}
