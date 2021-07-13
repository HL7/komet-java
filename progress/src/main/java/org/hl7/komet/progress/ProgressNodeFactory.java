package org.hl7.komet.progress;

import com.google.auto.service.AutoService;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.hl7.komet.framework.KometNode;
import org.hl7.komet.framework.KometNodeFactory;
import org.hl7.komet.framework.activity.ActivityStream;
import org.hl7.komet.framework.activity.ActivityStreamOption;
import org.hl7.komet.framework.activity.ActivityStreams;
import org.hl7.komet.framework.graphics.Icon;
import org.hl7.komet.preferences.KometPreferences;
import org.hl7.komet.framework.view.ObservableViewNoOverride;
import org.hl7.tinkar.common.id.PublicIdStringKey;
import org.kordamp.ikonli.javafx.FontIcon;

@AutoService(KometNodeFactory.class)
public class ProgressNodeFactory implements KometNodeFactory {

    @Override
    public void addDefaultNodePreferences(KometPreferences nodePreferences) {

    }

    @Override
    public ImmutableList<PublicIdStringKey<ActivityStream>> defaultActivityStreamChoices() {
        return Lists.immutable.empty();
    }

    @Override
    public ImmutableList<PublicIdStringKey<ActivityStreamOption>> defaultOptionsForActivityStream(PublicIdStringKey<ActivityStream> streamKey) {
        return Lists.immutable.empty();
    }

    @Override
    public KometNode create(ObservableViewNoOverride windowView, KometPreferences nodePreferences) {
        return new ProgressNode(windowView.makeOverridableViewProperties(), nodePreferences);
    }

    public KometNode create() {
        return new ProgressNode();
    }


    @Override
    public String getStyleId() {
        return null;
    }

    @Override
    public Class<? extends KometNode> kometNodeClass() {
        return ProgressNode.class;
    }

    @Override
    public String getMenuText() {
        return "Activity";
    }

    @Override
    public Label getMenuGraphic() {
        return Icon.makeIcon("activity-node");
    }


}
