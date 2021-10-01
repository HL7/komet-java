package org.hl7.komet.framework.observable;

import org.hl7.tinkar.coordinate.view.calculator.ViewCalculator;
import org.hl7.tinkar.entity.SemanticEntity;
import org.hl7.tinkar.entity.SemanticVersionRecord;

public class ObservableSemantic
        extends ObservableEntity<ObservableSemanticVersion, SemanticVersionRecord>
        implements SemanticEntity<ObservableSemanticVersion> {
    ObservableSemantic(SemanticEntity<SemanticVersionRecord> semanticEntity) {
        super(semanticEntity);
    }

    @Override
    protected ObservableSemanticVersion wrap(SemanticVersionRecord version) {
        return new ObservableSemanticVersion(version);
    }

    @Override
    public ObservableSemanticSnapshot getSnapshot(ViewCalculator calculator) {
        return new ObservableSemanticSnapshot(calculator, this);
    }

    @Override
    public int referencedComponentNid() {
        return ((SemanticEntity) entity).referencedComponentNid();
    }

    @Override
    public int patternNid() {
        return ((SemanticEntity) entity).patternNid();
    }

}
