package org.hl7.komet.framework.observable;

import org.hl7.tinkar.entity.StampEntity;
import org.hl7.tinkar.entity.transaction.Transaction;
import org.hl7.tinkar.terms.ConceptFacade;
import org.hl7.tinkar.terms.PatternFacade;
import org.hl7.tinkar.terms.State;

public class ObservableCompoundVersion {
    ObservableStamp stamp;

    public void setField(PatternFacade pattern, ConceptFacade fieldMeaning, Object fieldValue) {
        setField(pattern.nid(), fieldMeaning.nid(), fieldValue);
    }

    public void setField(int patternNid, int fieldMeaningNid, Object fieldValue) {
        throw new UnsupportedOperationException();
    }

    public void setStamp(ObservableStamp stamp) {
        this.stamp = stamp;
    }

    public void setStatus(State state) {
        // Need to handle stamp & transactions properly.
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setAuthorNid(int authorSequence) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setModuleNid(int moduleSequence) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setPathNid(int pathSequence) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setTime(long time) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean committed() {
        return !uncommitted();
    }

    public boolean uncommitted() {
        StampEntity stampEntity = (StampEntity) stamp.entity;
        if (stampEntity.time() == Long.MAX_VALUE) {
            return true;
        }
        if (Transaction.forStamp(stamp).isPresent()) {
            // Participating in an active transaction...
            return true;
        }
        return false;
    }
}
