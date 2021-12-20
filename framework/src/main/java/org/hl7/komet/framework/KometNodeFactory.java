package org.hl7.komet.framework;

import javafx.scene.control.Label;
import org.eclipse.collections.api.list.ImmutableList;
import org.hl7.komet.framework.activity.ActivityStream;
import org.hl7.komet.framework.activity.ActivityStreamOption;
import org.hl7.komet.framework.graphics.Icon;
import org.hl7.komet.framework.view.ObservableViewNoOverride;
import org.hl7.komet.preferences.KometPreferences;
import org.hl7.komet.preferences.KometPreferencesImpl;
import org.hl7.tinkar.common.alert.AlertStream;
import org.hl7.tinkar.common.id.PublicIdStringKey;

import java.util.UUID;

public interface KometNodeFactory {

    String KOMET_NODES = "/komet-nodes/";

    default KometNode create(ObservableViewNoOverride windowView,
                             PublicIdStringKey<ActivityStream> activityStreamKey,
                             PublicIdStringKey<ActivityStreamOption> activityStreamOption,
                             PublicIdStringKey<AlertStream> parentAlertStreamKey) {
        KometPreferences nodePreferences = KometPreferencesImpl.getConfigurationRootPreferences().node(newPreferenceNodeName());
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

    default String newPreferenceNodeName() {
        return KOMET_NODES + kometNodeClass().getSimpleName() + "_" + UUID.randomUUID();
    }

    void addDefaultNodePreferences(KometPreferences nodePreferences);

    KometNode create(ObservableViewNoOverride windowView, KometPreferences nodePreferences);

    Class<? extends KometNode> kometNodeClass();

    ImmutableList<PublicIdStringKey<ActivityStream>> defaultActivityStreamChoices();

    ImmutableList<PublicIdStringKey<ActivityStreamOption>> defaultOptionsForActivityStream(PublicIdStringKey<ActivityStream> streamKey);

    String getMenuText();

    default Label getMenuGraphic() {
        return Icon.makeIcon(getStyleId());
    }

    String getStyleId();

}
