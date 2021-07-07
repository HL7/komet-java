/*
 * Copyright 2017 Organizations participating in ISAAC, ISAAC's KOMET, and SOLOR development include the
         US Veterans Health Administration, OSHERA, and the Health Services Platform Consortium..
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hl7.komet.navigator;

import org.hl7.komet.framework.KometNode;
import org.hl7.komet.framework.KometNodeFactory;
import org.hl7.komet.preferences.KometPreferences;
import org.hl7.komet.framework.view.ObservableViewNoOverride;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author kec
 */

public class GraphNavigatorNodeFactory
        implements KometNodeFactory {
   private static final Logger LOG = LoggerFactory.getLogger(GraphNavigatorNodeFactory.class);
   protected static final String STYLE_ID = GraphNavigatorNode.STYLE_ID;
   protected static final String TITLE = GraphNavigatorNode.TITLE;

   @Override
   public KometNode create(ObservableViewNoOverride windowViewReference, KometPreferences nodePreferences) {
      return new GraphNavigatorNode(windowViewReference.makeOverridableViewProperties(), nodePreferences);
   }

   @Override
   public String getMenuText() {
      return TITLE;
   }

   @Override
   public String getStyleId() {
      return STYLE_ID;
   }

   @Override
   public void addDefaultNodePreferences(KometPreferences nodePreferences) {

   }

   @Override
   public Class<? extends KometNode> kometNodeClass() {
      return GraphNavigatorNode.class;
   }
}
