package org.hl7.komet.classification;

import com.google.auto.service.AutoService;
import org.hl7.komet.framework.KometNode;
import org.hl7.komet.framework.KometNodeFactory;
import org.hl7.komet.preferences.KometPreferences;
import org.hl7.komet.framework.view.ObservableViewNoOverride;

@AutoService(KometNodeFactory.class)
public class ClassificationResultsNodeFactory implements KometNodeFactory {
    protected static final String STYLE_ID = ClassificationResultsNode.STYLE_ID;
    protected static final String TITLE = ClassificationResultsNode.TITLE;

    @Override
    public void addDefaultNodePreferences(KometPreferences nodePreferences) {

    }

    @Override
    public KometNode create(ObservableViewNoOverride windowView, KometPreferences nodePreferences) {
        return new ClassificationResultsNode(windowView.makeOverridableViewProperties(), nodePreferences);
    }

    @Override
    public String getMenuText() {
        return TITLE;
    }

    @Override
    public String getStyleId() {
        return STYLE_ID;
    }

    @Override
    public Class<? extends KometNode> kometNodeClass() {
        return ClassificationResultsNode.class;
    }
}