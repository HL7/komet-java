package org.hl7.komet.framework.activity;

import javafx.scene.Node;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.factory.Maps;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.api.map.MutableMap;
import org.hl7.tinkar.common.id.PublicIdStringKey;
import org.hl7.tinkar.common.id.PublicIds;

public class ActivityStreams {
    public static final int marshalVersion = 1;

    public static final PublicIdStringKey<ActivityStream> ANY = new PublicIdStringKey(PublicIds.of("721339e8-f0f9-4187-bfcc-e1e9467a9286"),  "any");
    public static final PublicIdStringKey<ActivityStream> UNLINKED = new PublicIdStringKey(PublicIds.of("2b6a31af-4023-4129-a3f5-7f99ef958c4a"),"unlinked");
    public static final PublicIdStringKey<ActivityStream> SEARCH = new PublicIdStringKey(PublicIds.of("b4289694-c8be-45c8-9a59-9834f3a3c647"),"search");
    public static final PublicIdStringKey<ActivityStream> NAVIGATION = new PublicIdStringKey(PublicIds.of("9575ae84-c36a-4c7d-9b10-1c7e581ca9e1"),"navigation");
    public static final PublicIdStringKey<ActivityStream> CLASSIFICATION = new PublicIdStringKey(PublicIds.of("9575ae84-c36a-4c7d-9b10-1c7e581ca9e1"),"classification");
    public static final PublicIdStringKey<ActivityStream> CORRELATION = new PublicIdStringKey(PublicIds.of("aef2bd4e-e270-4824-8d39-401b714d1a33"),"correlation");
    public static final PublicIdStringKey<ActivityStream> LIST = new PublicIdStringKey(PublicIds.of("26b42c6d-b9ea-4a20-9446-c283a2d5383c"),"list");
    public static final PublicIdStringKey<ActivityStream> CONCEPT_BUILDER = new PublicIdStringKey(PublicIds.of("f1291277-abe8-465a-87f4-3434c51b6540"),"concept builder");
    public static final PublicIdStringKey<ActivityStream> FLWOR = new PublicIdStringKey(PublicIds.of("736ae53f-fbac-4fe8-97ea-44d77bda59d7"),"flwor");
    public static final PublicIdStringKey<ActivityStream> PREFERENCES = new PublicIdStringKey(PublicIds.of("6c4753f5-3abd-49f1-ada5-69aa021306da"),"preferences");

    public static final ImmutableList<PublicIdStringKey<ActivityStream>> KEYS =
            Lists.immutable.of(ANY, UNLINKED, SEARCH, NAVIGATION, CLASSIFICATION, CORRELATION, LIST, FLWOR, CONCEPT_BUILDER, PREFERENCES);

    private static ImmutableMap<PublicIdStringKey<ActivityStream>, ActivityStream> activityStreamMap;
    static {
        MutableMap<PublicIdStringKey<ActivityStream>, ActivityStream> tempMap = Maps.mutable.ofInitialCapacity(KEYS.size());
        tempMap.put(ANY, new ActivityStream("any-activityStream"));
        tempMap.put(UNLINKED, new ActivityStream("unlinked-activityStream"));
        tempMap.put(SEARCH, new ActivityStream("search-activityStream"));
        tempMap.put(NAVIGATION, new ActivityStream("navigation-activityStream"));
        tempMap.put(CLASSIFICATION, new ActivityStream("classification-activityStream"));
        tempMap.put(CORRELATION, new ActivityStream("correlation-activityStream"));
        tempMap.put(LIST, new ActivityStream("list-activityStream"));
        tempMap.put(CONCEPT_BUILDER, new ActivityStream("concept-builder-activityStream"));
        tempMap.put(FLWOR, new ActivityStream("flwor-activityStream"));
        tempMap.put(PREFERENCES, new ActivityStream("preferences-activityStream"));

        ActivityStreams.activityStreamMap = tempMap.toImmutable();
    }

    public static ActivityStream get(PublicIdStringKey<ActivityStream> activityStreamKey) {
        return ActivityStreams.activityStreamMap.get(activityStreamKey);
    }

    public static Node getActivityIcon(PublicIdStringKey<ActivityStream> key) {
        return get(key).getStreamIcon();
    }
}
