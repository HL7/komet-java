package org.hl7.komet.progress;

import com.google.auto.service.AutoService;
import org.hl7.komet.framework.KometNode;
import org.hl7.komet.framework.KometNodeFactory;
import org.hl7.komet.preferences.KometPreferences;
import org.hl7.komet.framework.view.ObservableViewNoOverride;

@AutoService(KometNodeFactory.class)
public class CompletionNodeFactory implements KometNodeFactory {

    @Override
    public void addDefaultNodePreferences(KometPreferences nodePreferences) {

    }

    @Override
    public KometNode create(ObservableViewNoOverride windowView, KometPreferences nodePreferences) {
        return new CompletionNode(windowView.makeOverridableViewProperties(), nodePreferences);
    }

    @Override
    public Class<? extends KometNode> kometNodeClass() {
        return CompletionNode.class;
    }

    @Override
    public String getMenuText() {
        return CompletionNode.TITLE;
    }

    @Override
    public String getStyleId() {
        return CompletionNode.STYLE_ID;
    }
}
