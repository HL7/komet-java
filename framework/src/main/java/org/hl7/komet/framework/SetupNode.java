package org.hl7.komet.framework;

import org.hl7.komet.framework.activity.ActivityStream;
import org.hl7.komet.framework.view.ObservableViewNoOverride;
import org.hl7.komet.preferences.KometPreferences;
import org.hl7.tinkar.common.alert.AlertObject;
import org.hl7.tinkar.common.id.PublicIdStringKey;
import org.hl7.tinkar.common.util.broadcast.Broadcaster;

public interface SetupNode {
    /**
     * Call to setup Komet specific framework items. Will be executed after the
     * FXML initialize() method.
     * TODO: refactor setup to require reading the activity stream key from node preferences.
     *  @param windowView Either used directly, or an overridable ObservableView is created by the KometNode
     *
     * @param nodePreferences   persistent preferences to save and restore KometNode state.
     * @param activityStreamKey Key for the activity stream
     * @param alertBroadcaster       from the parent node.
     */
    void setup(ObservableViewNoOverride windowView,
               KometPreferences nodePreferences,
               PublicIdStringKey<ActivityStream> activityStreamKey,
               Broadcaster<AlertObject> alertBroadcaster);
}
