package org.hl7.komet.framework.preferences;

import javafx.beans.property.BooleanProperty;

/**
 * @author kec
 */
public interface PreferenceChanged {

    BooleanProperty changedProperty();

}
