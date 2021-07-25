/* 
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * You may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributions from 2013-2017 where performed either by US government 
 * employees, or under US Veterans Health Administration contracts. 
 *
 * US Veterans Health Administration contributions by government employees
 * are work of the U.S. Government and are not subject to copyright
 * protection in the United States. Portions contributed by government 
 * employees are USGovWork (17USC §105). Not subject to copyright. 
 * 
 * Contribution by contractors to the US Veterans Health Administration
 * during this period are contractually contributed under the
 * Apache License, Version 2.0.
 *
 * See: https://www.usa.gov/government-works
 * 
 * Contributions prior to 2013:
 *
 * Copyright (C) International Health Terminology Standards Development Organisation.
 * Licensed under the Apache License, Version 2.0.
 *
 */
package org.hl7.komet.navigator;

//~--- JDK imports ------------------------------------------------------------
import java.util.*;
import java.util.concurrent.CountDownLatch;

//~--- non-JDK imports --------------------------------------------------------
import javafx.application.Platform;

import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import org.eclipse.collections.api.collection.ImmutableCollection;
import org.hl7.komet.framework.view.ObservableView;
import org.hl7.tinkar.common.id.IntIdSet;
import org.hl7.tinkar.common.id.IntIds;
import org.hl7.tinkar.common.id.PublicId;
import org.hl7.tinkar.common.service.Executor;
import org.hl7.tinkar.common.util.text.NaturalOrder;
import org.hl7.tinkar.coordinate.navigation.calculator.Edge;
import org.hl7.tinkar.coordinate.view.calculator.ViewCalculator;
import org.hl7.tinkar.entity.ConceptEntity;
import org.hl7.tinkar.entity.Entity;
import org.hl7.tinkar.terms.ConceptFacade;
import org.hl7.tinkar.terms.TinkarTerm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- classes ----------------------------------------------------------------
/**
 * A {@link TreeItem} for modeling nodes in ISAAC taxonomies.
 *
 * The {@code MultiParentGraphItemImpl} is not a visual component. The
 * {@code MultiParentGraphCell} provides the rendering for this tree item.
 *
 * @author kec
 * @author ocarlsen
 * @author <a href="mailto:daniel.armbrust.list@gmail.com">Dan Armbrust</a>
 * @see MultiParentGraphCell
 */
public class MultiParentVertexImpl
        extends TreeItem<ConceptFacade>
        implements MultiParentVertex, Comparable<MultiParentVertexImpl> {

    enum LeafStatus {
        UNKNOWN, IS_LEAF, NOT_LEAF
    }

    /**
     * The Constant LOG.
     */
    private static final Logger LOG = LoggerFactory.getLogger(MultiParentVertexImpl.class);

    //~--- fields --------------------------------------------------------------
    private final List<MultiParentVertexImpl> extraParents = new ArrayList<>();
    private CountDownLatch childrenLoadedLatch = new CountDownLatch(1);

    private volatile boolean cancelLookup = false;
    private boolean defined = false;
    private boolean multiParent = false;
    private int multiParentDepth = 0;
    private boolean secondaryParentOpened = false;
    private MultiParentGraphViewController graphController;
    private String conceptDescriptionText;  // Cached to speed up comparisons with toString method.
    private final int nid;
    private final IntIdSet typeNids;
    private ImmutableCollection<Edge> childLinks;
    private LeafStatus leafStatus = LeafStatus.UNKNOWN;

    //~--- constructors --------------------------------------------------------
    MultiParentVertexImpl(MultiParentGraphViewController graphController) {
        super();
        this.graphController = graphController;
        this.nid = Integer.MAX_VALUE;
        this.typeNids = IntIds.set.of(TinkarTerm.UNINITIALIZED_COMPONENT.nid());
    }

    MultiParentVertexImpl(int conceptNid, MultiParentGraphViewController graphController, IntIdSet typeNids) {
        this(Entity.getFast(conceptNid), graphController, typeNids, null);
    }

    @Override
    public OptionalInt getOptionalParentNid() {
        if (getParent() != null && getParent().getValue() != null) {
            return OptionalInt.of(getParent().getValue().nid());
        }
        return OptionalInt.empty();
    }

    MultiParentVertexImpl(ConceptEntity conceptEntity
            , MultiParentGraphViewController graphController, IntIdSet typeNids, Node graphic) {
        super(conceptEntity, graphic);
        this.graphController = graphController;
        this.nid = conceptEntity.nid();
        this.typeNids = typeNids;
    }

    //~--- methods -------------------------------------------------------------
    public void blockUntilChildrenReady()
            throws InterruptedException {
        childrenLoadedLatch.await();
    }

    public IntIdSet getTypeNids() {
        return typeNids;
    }

    /**
     * clears the display nodes, and the nid lists, and resets the calculators
     */
    public void clearChildren() {
        cancelLookup = true;
        childrenLoadedLatch.countDown();
        getChildren().forEach(
                (child) -> {
                    ((MultiParentVertexImpl) child).clearChildren();
                });
        getChildren().clear();
        childLinks = null;
        resetChildrenCalculators();

    }

    @Override
    public int compareTo(MultiParentVertexImpl o) {
        int compare = NaturalOrder.compareStrings(this.toString(), o.toString());
        if (compare != 0) {
           return compare; 
        }
        return Integer.compare(nid, o.nid);
    }

    public Node computeGraphic() {
        return graphController.getDisplayPolicies()
                .computeGraphic(this, graphController.getViewCalculator());
    }

    public void invalidate() {
        updateDescription();

        for (TreeItem<ConceptFacade> child : getChildren()) {
            MultiParentVertexImpl multiParentTreeItem = (MultiParentVertexImpl) child;

            multiParentTreeItem.invalidate();
        }
    }

    /**
     * Removed the graphical display nodes from the tree, but does not clear the
     * cached nid set of children
     */
    public void removeChildren() {
        this.getChildren()
                .clear();
    }

    public boolean shouldDisplay() {
        if (graphController == null || graphController.getDisplayPolicies() == null) {
            return false;
        }
        return graphController.getDisplayPolicies()
                .shouldDisplay(this, graphController.getViewCalculator());
    }

    /**
     * @see javafx.scene.control.TreeItem#toString() WARNING: toString is
     * currently used in compareTo()
     */
    @Override
    public String toString() {
        try {
            if (this.getValue() != null) {
                if ((conceptDescriptionText == null) || conceptDescriptionText.startsWith("no description for ")) {
                    updateDescription();
                }
                return this.conceptDescriptionText;
            }

            return "root";
        } catch (RuntimeException | Error re) {
            LOG.error(re.getLocalizedMessage(), re);
            throw re;
        }
    }

    private void updateDescription() {
        if (this.nid != Integer.MAX_VALUE) {
            this.conceptDescriptionText = graphController.getObservableView().getDescriptionTextOrNid(nid);
        } else {
            this.conceptDescriptionText = "hidden root";
        }
    }

    void addChildrenNow() {
        if (getChildren().isEmpty()) {
            try {
                final ConceptFacade conceptFacade = getValue();

                if (!shouldDisplay()) {
                    // Don't add children to something that shouldn't be displayed
                    LOG.atTrace().log("this.shouldDisplay() == false: not adding children to " + this.getConceptPublicId());
                } else if (conceptFacade == null) {
                    LOG.atTrace().log("addChildren(): conceptEntity=" + conceptFacade);
                } else {  // if (conceptEntity != null)
                    // Gather the children
                    LOG.info("addChildrenNOW(): conceptEntity=" + conceptFacade);
                    ArrayList<MultiParentVertexImpl> childrenToAdd = new ArrayList<>();
                    Navigator navigator = graphController.getNavigator();

                    if (childLinks == null) {
                        childLinks = navigator.getChildEdges(conceptFacade.nid());
                    }

                    for (Edge childLink : childLinks) {
                        ConceptEntity childChronology = Entity.getFast(childLink.destinationNid());
                        MultiParentVertexImpl childItem = new MultiParentVertexImpl(childChronology, graphController, childLink.typeNids(), null);
                        ObservableView observableView = graphController.getObservableView();

                        childItem.setDefined(observableView.calculator().hasSufficientSet(childChronology.nid()));
                        childItem.toString();
                        childItem.setMultiParent(navigator.getParentNids(childLink.destinationNid()).length > 1);

                        if (childItem.shouldDisplay()) {
                            childrenToAdd.add(childItem);
                        } else {
                            LOG.atTrace().log(
                                    "item.shouldDisplay() == false: not adding " + childItem.getConceptPublicId() + " as child of "
                                    + this.getConceptPublicId());
                        }
                    }

                    Collections.sort(childrenToAdd);

                    if (cancelLookup) {
                        return;
                    }
                    getChildren().addAll(childrenToAdd);
                }
            } catch (Exception e) {
                LOG.error("Unexpected error computing children and/or grandchildren for " + this.conceptDescriptionText, e);
            } finally {
                childrenLoadedLatch.countDown();
            }
        }
    }

    void addChildren() {
        LOG.atTrace().log("addChildren: ConceptEntity=" + this.getValue());
        if (getChildren().isEmpty()) {
            if (shouldDisplay()) {
                FetchChildren fetchTask = new FetchChildren(childrenLoadedLatch, this);
                Executor.threadPool().submit(fetchTask);
            }
        }
    }

    private void resetChildrenCalculators() {
        CountDownLatch cdl = new CountDownLatch(1);
        Runnable r = () -> {
            cancelLookup = false;
            childrenLoadedLatch.countDown();
            childrenLoadedLatch = new CountDownLatch(1);
            cdl.countDown();
        };

        if (Platform.isFxApplicationThread()) {
            r.run();
        } else {
            Platform.runLater(r);
        }

        try {
            cdl.await();
        } catch (InterruptedException e) {
            LOG.error("unexpected interrupt", e);
        }
    }

    //~--- get methods ---------------------------------------------------------
    protected boolean isCancelRequested() {
        return cancelLookup;
    }

    @Override
    public int getConceptNid() {
        return (getValue() != null) ? getValue().nid()
                : Integer.MIN_VALUE;
    }

    private static int getConceptNid(TreeItem<ConceptEntity> item) {
        return ((item != null) && (item.getValue() != null)) ? item.getValue()
                .nid()
                : null;
    }

    public PublicId getConceptPublicId() {
        return (getValue() != null) ? getValue().publicId()
                : null;
    }

    @Override
    public boolean isDefined() {
        return defined;
    }

    //~--- set methods ---------------------------------------------------------
    public void setDefined(boolean defined) {
        this.defined = defined;
    }

    //~--- get methods ---------------------------------------------------------
    NavigatorDisplayPolicies getDisplayPolicies() {
        return this.graphController.getDisplayPolicies();
    }

    public List<MultiParentVertexImpl> getExtraParents() {
        return extraParents;
    }

    @Override
    public boolean isLeaf() {
        if (this.nid == Integer.MAX_VALUE) {
            return false;
        }
        if (leafStatus != LeafStatus.UNKNOWN) {
            return leafStatus == LeafStatus.IS_LEAF;
        }
        if (multiParentDepth > 0) {
            leafStatus = LeafStatus.IS_LEAF;
            return true;
        }
        if (this.childLinks == null) {
            if (this.graphController.getNavigator().isLeaf(nid)) {
                leafStatus = LeafStatus.IS_LEAF;
            } else {
                leafStatus = LeafStatus.NOT_LEAF;
            }
            return leafStatus == LeafStatus.IS_LEAF;
        }
        if (this.childLinks.isEmpty()) {
            leafStatus = LeafStatus.IS_LEAF;
        } else {
            leafStatus = LeafStatus.NOT_LEAF;
        }
        return leafStatus == LeafStatus.IS_LEAF;
    }

    @Override
    public boolean isMultiParent() {
        return multiParent;
    }

    //~--- set methods ---------------------------------------------------------
    public void setMultiParent(boolean multiParent) {
        this.multiParent = multiParent;
    }

    //~--- get methods ---------------------------------------------------------
    @Override
    public int getMultiParentDepth() {
        return multiParentDepth;
    }

    //~--- set methods ---------------------------------------------------------
    public void setMultiParentDepth(int multiParentDepth) {
        this.multiParentDepth = multiParentDepth;
    }

    //~--- get methods ---------------------------------------------------------
    @Override
    public boolean isRoot() {
        if (this.nid == Integer.MAX_VALUE) {
            return true;
        }
        if (TinkarTerm.ROOT_VERTEX.nid() == this.nid) {
            return true;
        } else if (this.getParent() == null) {
            return true;
        } else if (this.getParent() == graphController.getRoot()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isSecondaryParentOpened() {
        return secondaryParentOpened;
    }

    //~--- set methods ---------------------------------------------------------
    public void setSecondaryParentOpened(boolean secondaryParentOpened) {
        this.secondaryParentOpened = secondaryParentOpened;
    }

    //~--- get methods ---------------------------------------------------------
    private static TreeItem<ConceptEntity> getTreeRoot(TreeItem<ConceptEntity> item) {
        TreeItem<ConceptEntity> parent = item.getParent();

        if (parent == null) {
            return item;
        } else {
            return getTreeRoot(parent);
        }
    }

    public MultiParentGraphViewController getGraphController() {
        return graphController;
    }

    public ViewCalculator getViewCalculator() {
        return graphController.getObservableView().calculator();
    }
}
