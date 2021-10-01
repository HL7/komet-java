package org.hl7.komet.framework.observable;

import org.hl7.tinkar.entity.EntityVersion;
import org.hl7.tinkar.entity.StampEntity;
import org.hl7.tinkar.entity.transaction.Transaction;

public abstract class ObservableNonStampVersion<V extends EntityVersion> extends ObservableVersion<V> {
    ObservableNonStampVersion(V entityVersion) {
        super(entityVersion);
    }

    @Override
    protected final void addListeners() {
        stateProperty.addListener((observable, oldValue, newValue) -> {
            if (version().uncommitted()) {
                Transaction.forVersion(version()).ifPresentOrElse(transaction -> {
                    StampEntity newStamp = transaction.getStamp(newValue, version().time(), version().authorNid(), version().moduleNid(), version().pathNid());
                    versionProperty.set(withStampNid(newStamp.nid()));
                }, () -> {
                    throw new IllegalStateException("No transaction for uncommitted version: " + version());
                });
            } else {
                throw new IllegalStateException("Version is already committed, cannot change value.");
            }
        });

        timeProperty.addListener((observable, oldValue, newValue) -> {
            // TODO when to update the chronology with new record? At commit time? Automatically with reactive stream for commits?
            if (version().uncommitted()) {
                Transaction.forVersion(version()).ifPresentOrElse(transaction -> {
                    StampEntity newStamp = transaction.getStamp(version().state(), newValue.longValue(), version().authorNid(), version().moduleNid(), version().pathNid());
                    versionProperty.set(withStampNid(newStamp.nid()));
                }, () -> {
                    throw new IllegalStateException("No transaction for uncommitted version: " + version());
                });
            } else {
                throw new IllegalStateException("Version is already committed, cannot change value.");
            }
        });

        authorProperty.addListener((observable, oldValue, newValue) -> {
            if (version().uncommitted()) {
                Transaction.forVersion(version()).ifPresentOrElse(transaction -> {
                    StampEntity newStamp = transaction.getStamp(version().state(), version().time(), newValue.nid(), version().moduleNid(), version().pathNid());
                    versionProperty.set(withStampNid(newStamp.nid()));
                }, () -> {
                    throw new IllegalStateException("No transaction for uncommitted version: " + version());
                });
            } else {
                throw new IllegalStateException("Version is already committed, cannot change value.");
            }
        });

        moduleProperty.addListener((observable, oldValue, newValue) -> {
            if (version().uncommitted()) {
                Transaction.forVersion(version()).ifPresentOrElse(transaction -> {
                    StampEntity newStamp = transaction.getStamp(version().state(), version().time(), version().authorNid(), newValue.nid(), version().pathNid());
                    versionProperty.set(withStampNid(newStamp.nid()));
                }, () -> {
                    throw new IllegalStateException("No transaction for uncommitted version: " + version());
                });
            } else {
                throw new IllegalStateException("Version is already committed, cannot change value.");
            }
        });

        pathProperty.addListener((observable, oldValue, newValue) -> {
            if (version().uncommitted()) {
                Transaction.forVersion(version()).ifPresentOrElse(transaction -> {
                    StampEntity newStamp = transaction.getStamp(version().state(), version().time(), version().authorNid(), version().moduleNid(), newValue.nid());
                    versionProperty.set(withStampNid(newStamp.nid()));
                }, () -> {
                    throw new IllegalStateException("No transaction for uncommitted version: " + version());
                });
            } else {
                throw new IllegalStateException("Version is already committed, cannot change value.");
            }
        });
    }

    protected abstract V withStampNid(int stampNid);
}
