package org.hl7.komet.navigator.pattern;


import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.AnchorPane;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.hl7.komet.framework.ExplorationNodeAbstract;
import org.hl7.komet.framework.view.ViewProperties;
import org.hl7.komet.navigator.graph.GraphNavigatorNode;
import org.hl7.komet.preferences.KometPreferences;
import org.hl7.tinkar.entity.Entity;
import org.hl7.tinkar.entity.EntityVersion;
import org.hl7.tinkar.terms.EntityFacade;
import org.hl7.tinkar.terms.EntityProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class PatternNavigatorNode extends ExplorationNodeAbstract {
    protected static final String STYLE_ID = "pattern-navigator-node";
    protected static final String TITLE = "Pattern Navigator";
    private static final Logger LOG = LoggerFactory.getLogger(GraphNavigatorNode.class);
    final AnchorPane root;
    final PatternViewController controller;

    public PatternNavigatorNode(ViewProperties viewProperties, KometPreferences nodePreferences) {
        super(viewProperties, nodePreferences);
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/hl7/komet/navigator/PatternView.fxml"));
            this.root = loader.load();
            this.controller = loader.getController();
            this.controller.setProperties(this, viewProperties, nodePreferences);
            ObservableList<TreeItem<Object>> selectedItems = this.controller.getTreeView().getSelectionModel().getSelectedItems();
            selectedItems.addListener((ListChangeListener.Change<? extends TreeItem<Object>> c) -> {
                MutableList<EntityFacade> selectedItemList = Lists.mutable.empty();
                for (TreeItem<Object> item : c.getList()) {
                    if (item.getValue() instanceof Integer nid) {
                        selectedItemList.add(EntityProxy.make(nid));
                    } else if (item.getValue() instanceof Entity entity) {
                        selectedItemList.add(EntityProxy.make(entity.nid()));
                    } else if (item.getValue() instanceof EntityVersion entityVersion) {
                        selectedItemList.add(EntityProxy.make(entityVersion.nid()));
                    }
                }
                if (!selectedItemList.isEmpty()) {
                    dispatchActivity(selectedItemList.toImmutable());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    protected boolean showActivityStreamIcon() {
        return false;
    }

    @Override
    public String getDefaultTitle() {
        return TITLE;
    }

    @Override
    public void handleActivity(ImmutableList<EntityFacade> entities) {
        // Nothing to do...
    }

    @Override
    public String getStyleId() {
        return STYLE_ID;
    }

    @Override
    public Node getNode() {
        return root;
    }

    @Override
    public void close() {
        // nothing to do...
    }

    @Override
    public boolean canClose() {
        return true;
    }

    @Override
    public void savePreferences() {

    }

    @Override
    public void revertPreferences() {

    }
}
