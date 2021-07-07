package org.hl7.komet.framework;

import org.hl7.komet.preferences.KometPreferences;
import org.hl7.komet.framework.view.ViewProperties;

public abstract class DetailNodeAbstract extends ExplorationNodeAbstract {

    protected final EntityLabelWithDragAndDrop titleLabel;

    public DetailNodeAbstract(ViewProperties viewProperties, KometPreferences nodePreferences) {
        super(viewProperties, nodePreferences);
        throw new UnsupportedOperationException("Have to initialize titleLabel");
    }
}
