package org.hl7.komet.framework.observable;

import org.hl7.tinkar.coordinate.view.calculator.ViewCalculator;
import org.hl7.tinkar.entity.StampEntity;
import org.hl7.tinkar.entity.StampVersionRecord;

public class ObservableStamp
        extends ObservableEntity<ObservableStampVersion, StampVersionRecord> {
    ObservableStamp(StampEntity<StampVersionRecord> stampEntity) {
        super(stampEntity);
    }

    @Override
    protected ObservableStampVersion wrap(StampVersionRecord version) {
        return new ObservableStampVersion(version);
    }

    @Override
    public ObservableEntitySnapshot getSnapshot(ViewCalculator calculator) {
        throw new UnsupportedOperationException();
    }
}
