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



package org.hl7.komet.view;

//~--- non-JDK imports --------------------------------------------------------

//~--- interfaces -------------------------------------------------------------

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import org.hl7.tinkar.coordinate.logic.LogicCoordinateRecord;
import org.hl7.tinkar.coordinate.logic.LogicCoordinateDelegate;
import org.hl7.tinkar.terms.ConceptFacade;
import org.hl7.tinkar.terms.PatternFacade;

/**
 * The Interface ObservableLogicCoordinate.
 *
 * @author kec
 */
public interface ObservableLogicCoordinate
        extends LogicCoordinateDelegate, ObservableCoordinate<LogicCoordinateRecord> {

   @Override
   default ObservableCoordinate<?>[] getCompositeCoordinates() {
      return new ObservableCoordinate[0];
   }

   default Property<?>[] getBaseProperties() {
      return new Property<?>[] {
              classifierProperty(),
              conceptMemberPatternProperty(),
              descriptionLogicProfileProperty(),
              inferredAxiomsPatternProperty(),
              statedAxiomsPatternProperty(),
              statedNavigationPatternProperty(),
              inferredNavigationPatternProperty(),
              rootConceptProperty()
      };
   }

   /**
    * Classifier property.
    *
    * @return the classifier concept property. 
    */
   ObjectProperty<ConceptFacade> classifierProperty();

   /**
    * Concept assemblage property.
    *
    * @return the assemblage concept property. 
    */
   ObjectProperty<PatternFacade> conceptMemberPatternProperty();

   /**
    * Description logic profile property.
    *
    * @return the description logic profile concept property. 
    */
   ObjectProperty<ConceptFacade> descriptionLogicProfileProperty();


   /**
    * Stated assemblage property.
    *
    * @return the stated assemblage concept property.
    */
   ObjectProperty<PatternFacade> statedAxiomsPatternProperty();
   /**
    * Inferred assemblage property.
    *
    * @return the inferred assemblage concept property. 
    */
   ObjectProperty<PatternFacade> inferredAxiomsPatternProperty();

   ObjectProperty<PatternFacade> statedNavigationPatternProperty();

   ObjectProperty<PatternFacade> inferredNavigationPatternProperty();

   ObjectProperty<ConceptFacade> rootConceptProperty();
}

