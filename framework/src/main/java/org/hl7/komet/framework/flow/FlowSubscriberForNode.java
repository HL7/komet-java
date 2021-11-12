package org.hl7.komet.framework.flow;

import javafx.scene.Node;
import org.hl7.tinkar.common.flow.FlowSubscriber;

import java.util.function.Consumer;

public class FlowSubscriberForNode<T> extends FlowSubscriber<T> {
    public FlowSubscriberForNode(Consumer<T> action, Node node) {
        super(action);
        node.sceneProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                cancel();
            }
        });
    }
}
