package org.hl7.komet.search;

import com.google.auto.service.AutoService;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import org.hl7.komet.framework.KometNode;
import org.hl7.komet.framework.KometNodeFactory;
import org.hl7.komet.framework.graphics.Icon;
import org.hl7.komet.preferences.KometPreferences;
import org.hl7.komet.framework.view.ObservableViewNoOverride;

@AutoService(KometNodeFactory.class)
public class SearchNodeFactory implements KometNodeFactory {
    protected static final String STYLE_ID = SearchNode.STYLE_ID;
    protected static final String TITLE = SearchNode.TITLE;

    @Override
    public void addDefaultNodePreferences(KometPreferences nodePreferences) {

    }

    @Override
    public KometNode create(ObservableViewNoOverride windowView, KometPreferences nodePreferences) {
        return new SearchNode(windowView.makeOverridableViewProperties(), nodePreferences);
    }

    @Override
    public Class<? extends KometNode> kometNodeClass() {
        return SearchNode.class;
    }

    @Override
    public String getMenuText() {
        return TITLE;
    }

    @Override
    public String getStyleId() {
        return STYLE_ID;
    }
}