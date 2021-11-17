package org.hl7.komet.framework.observable;

import org.hl7.tinkar.coordinate.view.calculator.ViewCalculator;
import org.hl7.tinkar.entity.PatternEntity;
import org.hl7.tinkar.entity.PatternVersionRecord;

public class ObservablePattern
        extends ObservableEntity<ObservablePatternVersion, PatternVersionRecord>
        implements PatternEntity<ObservablePatternVersion> {
    ObservablePattern(PatternEntity<PatternVersionRecord> patternEntity) {
        super(patternEntity);
    }

    @Override
    protected ObservablePatternVersion wrap(PatternVersionRecord version) {
        return new ObservablePatternVersion(version);
    }

    @Override
    public ObservablePatternSnapshot getSnapshot(ViewCalculator calculator) {
        return new ObservablePatternSnapshot(calculator, this);
    }

}
