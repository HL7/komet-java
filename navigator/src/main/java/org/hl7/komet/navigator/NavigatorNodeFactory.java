package org.hl7.komet.navigator;

import com.google.auto.service.AutoService;
import org.hl7.komet.framework.KometNode;
import org.hl7.komet.framework.KometNodeFactory;
import org.hl7.komet.preferences.KometPreferences;
import org.hl7.komet.framework.view.ObservableViewNoOverride;

@AutoService(NavigatorNode.class)
public class NavigatorNodeFactory implements KometNodeFactory {
    protected static final String STYLE_ID = NavigatorNode.STYLE_ID;
    protected static final String TITLE = NavigatorNode.TITLE;

    @Override
    public void addDefaultNodePreferences(KometPreferences nodePreferences) {

    }

    @Override
    public KometNode create(ObservableViewNoOverride windowView, KometPreferences nodePreferences) {
        return new NavigatorNode(windowView.makeOverridableViewProperties(), nodePreferences);
    }

    @Override
    public Class<? extends KometNode> kometNodeClass() {
        return NavigatorNode.class;
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