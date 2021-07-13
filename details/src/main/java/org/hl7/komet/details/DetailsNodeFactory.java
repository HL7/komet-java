package org.hl7.komet.details;

import com.google.auto.service.AutoService;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.hl7.komet.framework.KometNode;
import org.hl7.komet.framework.KometNodeFactory;
import org.hl7.komet.framework.activity.ActivityStream;
import org.hl7.komet.framework.activity.ActivityStreamOption;
import org.hl7.komet.framework.activity.ActivityStreams;
import org.hl7.komet.preferences.KometPreferences;
import org.hl7.komet.framework.view.ObservableViewNoOverride;
import org.hl7.tinkar.common.id.PublicIdStringKey;

@AutoService(KometNodeFactory.class)
public class DetailsNodeFactory  implements KometNodeFactory {
    protected static final String STYLE_ID = DetailsNode.STYLE_ID;
    protected static final String TITLE = DetailsNode.TITLE;

    @Override
    public void addDefaultNodePreferences(KometPreferences nodePreferences) {
        DetailsNode.addDefaultNodePreferences(nodePreferences);
    }


    @Override
    public ImmutableList<PublicIdStringKey<ActivityStream>> defaultActivityStreamChoices() {
        return Lists.immutable.of(ActivityStreams.SEARCH, ActivityStreams.NAVIGATION, ActivityStreams.CLASSIFICATION,
                ActivityStreams.UNLINKED, ActivityStreams.BUILDER, ActivityStreams.CORRELATION, ActivityStreams.LIST);
    }

    @Override
    public ImmutableList<PublicIdStringKey<ActivityStreamOption>> defaultOptionsForActivityStream(PublicIdStringKey<ActivityStream> streamKey) {
        if (defaultActivityStreamChoices().contains(streamKey)) {
            return Lists.immutable.of(ActivityStreamOption.SUBSCRIBE.keyForOption());
        }
        return Lists.immutable.empty();
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