package org.hl7.komet.details;

import com.google.auto.service.AutoService;
import org.hl7.komet.framework.KometNode;
import org.hl7.komet.framework.KometNodeFactory;
import org.hl7.komet.preferences.KometPreferences;
import org.hl7.komet.framework.view.ObservableViewNoOverride;

@AutoService(KometNodeFactory.class)
public class DetailsNodeFactory  implements KometNodeFactory {
    protected static final String STYLE_ID = DetailsNode.STYLE_ID;
    protected static final String TITLE = DetailsNode.TITLE;

    @Override
    public void addDefaultNodePreferences(KometPreferences nodePreferences) {
        DetailsNode.addDefaultNodePreferences(nodePreferences);
    }

    @Override
    public KometNode create(ObservableViewNoOverride windowView, KometPreferences nodePreferences) {
        return new DetailsNode(windowView.makeOverridableViewProperties(), nodePreferences);
    }

    @Override
    public Class<? extends KometNode> kometNodeClass() {
        return DetailsNode.class;
    }

    @Override
    public String getStyleId() {
        return STYLE_ID;
    }

    @Override
    public String getMenuText() {
        return TITLE;
    }
}