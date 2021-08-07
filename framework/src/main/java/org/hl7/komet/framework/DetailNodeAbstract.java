package org.hl7.komet.framework;

import org.hl7.komet.framework.controls.EntityLabelWithDragAndDrop;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.komet.preferences.KometPreferences;

public abstract class DetailNodeAbstract extends ExplorationNodeAbstract {

    protected final EntityLabelWithDragAndDrop titleLabel;

    public DetailNodeAbstract(ViewProperties viewProperties, KometPreferences nodePreferences) {
        super(viewProperties, nodePreferences);
        throw new UnsupportedOperationException("Have to initialize titleLabel");
    }
}
