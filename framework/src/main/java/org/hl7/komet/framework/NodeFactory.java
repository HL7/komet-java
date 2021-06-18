package org.hl7.komet.framework;

import javafx.scene.Node;
import org.hl7.komet.view.ObservableViewNoOverride;
import org.hl7.komet.view.ViewProperties;

import java.util.concurrent.atomic.AtomicReference;

public interface NodeFactory {
    ExplorationNode create(AtomicReference<ObservableViewNoOverride> windowViewReference);
    String getMenuText();
    Node getMenuGraphic();
}
