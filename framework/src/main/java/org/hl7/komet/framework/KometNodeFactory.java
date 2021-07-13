package org.hl7.komet.framework;

import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import org.eclipse.collections.api.list.ImmutableList;
import org.hl7.komet.framework.activity.ActivityStream;
import org.hl7.komet.framework.activity.ActivityStreamOption;
import org.hl7.komet.framework.alerts.AlertStream;
import org.hl7.komet.framework.graphics.Icon;
import org.hl7.komet.preferences.KometPreferences;
import org.hl7.komet.framework.view.ObservableViewNoOverride;
import org.hl7.tinkar.common.id.PublicIdStringKey;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.UUID;

public interface KometNodeFactory {
    default KometNode create(ObservableViewNoOverride windowView,
                             KometPreferences parentNodePreferences,
                             PublicIdStringKey<ActivityStream> activityStreamKey,
                             PublicIdStringKey<ActivityStreamOption> activityStreamOption,
                             PublicIdStringKey<AlertStream> parentAlertStreamKey) {
        KometPreferences nodePreferences = parentNodePreferences.node(newPreferenceNodeName());
        // Add activity stream key
        if (activityStreamKey != null) {
            nodePreferences.putObject(KometNode.PreferenceKey.ACTIVITY_STREAM_KEY, activityStreamKey);
        }
        if (activityStreamOption != null) {
            nodePreferences.putObject(KometNode.PreferenceKey.ACTIVITY_STREAM_OPTION_KEY, activityStreamOption);
        }
        // add parent alertStream key
        nodePreferences.putObject(KometNode.PreferenceKey.PARENT_ALERT_STREAM_KEY, parentAlertStreamKey);
        addDefaultNodePreferences(nodePreferences);
        return create(windowView, nodePreferences);
    }

    ImmutableList<PublicIdStringKey<ActivityStream>> defaultActivityStreamChoices();
    ImmutableList<PublicIdStringKey<ActivityStreamOption>> defaultOptionsForActivityStream(PublicIdStringKey<ActivityStream> streamKey);


    void addDefaultNodePreferences(KometPreferences nodePreferences);

    KometNode create(ObservableViewNoOverride windowView, KometPreferences nodePreferences);

    String getMenuText();

    default String newPreferenceNodeName() {
        return kometNodeClass().getSimpleName() + "_" + UUID.randomUUID();
    }

    Class<? extends KometNode> kometNodeClass();

    String getStyleId();

    default Label getMenuGraphic() {
        return Icon.makeIcon(getStyleId());
    }

}
