package org.hl7.komet.framework;

import javafx.scene.Node;

public interface NodeFactory {
    ExplorationNode create();
    String getMenuText();
    Node getMenuGraphic();
}
