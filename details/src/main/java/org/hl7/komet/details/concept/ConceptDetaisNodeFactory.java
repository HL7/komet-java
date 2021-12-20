package org.hl7.komet.details.concept;

import com.google.auto.service.AutoService;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.hl7.komet.framework.KometNode;
import org.hl7.komet.framework.KometNodeFactory;
import org.hl7.komet.framework.activity.ActivityStream;
import org.hl7.komet.framework.activity.ActivityStreamOption;
import org.hl7.komet.framework.activity.ActivityStreams;
import org.hl7.komet.framework.preferences.Reconstructor;
import org.hl7.komet.framework.view.ObservableViewNoOverride;
import org.hl7.komet.preferences.KometPreferences;
import org.hl7.tinkar.common.id.PublicIdStringKey;

@AutoService(KometNodeFactory.class)
public class ConceptDetaisNodeFactory implements KometNodeFactory {
    protected static final String STYLE_ID = ConceptDetailsNode.STYLE_ID;
    protected static final String TITLE = ConceptDetailsNode.TITLE;

    @Override
    public void addDefaultNodePreferences(KometPreferences nodePreferences) {
        ConceptDetailsNode.addDefaultNodePreferences(nodePreferences);
    }

    @Override
    public ConceptDetailsNode create(ObservableViewNoOverride windowView, KometPreferences nodePreferences) {
        return reconstructor(windowView, nodePreferences);
    }

    @Reconstructor
    public static ConceptDetailsNode reconstructor(ObservableViewNoOverride windowView, KometPreferences nodePreferences) {
        return new ConceptDetailsNode(windowView.makeOverridableViewProperties(), nodePreferences);
    }

    @Override
    public Class<? extends KometNode> kometNodeClass() {
        return ConceptDetailsNode.class;
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
    public String getMenuText() {
        return TITLE;
    }

    @Override
    public String getStyleId() {
        return STYLE_ID;
    }
}