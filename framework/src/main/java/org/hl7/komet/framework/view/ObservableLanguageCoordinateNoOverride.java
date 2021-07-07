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
package org.hl7.komet.framework.view;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import org.eclipse.collections.api.list.ImmutableList;
import org.hl7.komet.terms.KometTerm;
import org.hl7.tinkar.coordinate.language.LanguageCoordinate;
import org.hl7.tinkar.coordinate.language.LanguageCoordinateRecord;
import org.hl7.tinkar.terms.ConceptFacade;
import org.hl7.tinkar.terms.ConceptProxy;
import org.hl7.tinkar.terms.PatternFacade;
import org.hl7.tinkar.terms.PatternProxy;

/**
 * The Class ObservableLanguageCoordinateImpl.
 *
 * @author kec
 */
public final class ObservableLanguageCoordinateNoOverride
        extends ObservableLanguageCoordinateBase {

     /**
     * Instantiates a new observable language coordinate impl.
     *
     * @param languageCoordinate the language coordinate
     */
     public ObservableLanguageCoordinateNoOverride(LanguageCoordinate languageCoordinate, String coordinateName) {
         super(languageCoordinate, coordinateName);
     }

    public ObservableLanguageCoordinateNoOverride(LanguageCoordinate languageCoordinate) {
        super(languageCoordinate, "Language coordinate");
    }

    @Override
    public void setExceptOverrides(LanguageCoordinateRecord updatedCoordinate) {
        setValue(updatedCoordinate);
    }

    @Override
    protected SimpleEqualityBasedObjectProperty<ConceptFacade> makeLanguageProperty(LanguageCoordinate languageCoordinate) {
        return new SimpleEqualityBasedObjectProperty<>(this,
                KometTerm.LANGUAGE_SPECIFICATION_FOR_LANGUAGE_COORDINATE.toXmlFragment(),
                languageCoordinate.languageConcept());
    }

    @Override
    protected SimpleEqualityBasedListProperty<PatternFacade> makeDialectPatternPreferenceListProperty(LanguageCoordinate languageCoordinate) {
        return new SimpleEqualityBasedListProperty<>(this,
                KometTerm.DIALECT_ASSEMBLAGE_PREFERENCE_LIST_FOR_LANGUAGE_COORDINATE.toXmlFragment(),
                FXCollections.observableArrayList(languageCoordinate.dialectPatternPreferenceNidList().mapToList(PatternProxy::make)));
    }

    @Override
    protected SimpleEqualityBasedListProperty<ConceptFacade> makeDescriptionTypePreferenceListProperty(LanguageCoordinate languageCoordinate) {
        return new SimpleEqualityBasedListProperty<>(this,
                KometTerm.DESCRIPTION_TYPE_PREFERENCE_LIST_FOR_LANGUAGE_COORDINATE.toXmlFragment(),
                FXCollections.observableArrayList(languageCoordinate.descriptionTypePreferenceNidList().mapToList(ConceptProxy::make)));
    }

    @Override
    protected SimpleEqualityBasedListProperty<ConceptFacade> makeModulePreferenceListProperty(LanguageCoordinate languageCoordinate) {
        ImmutableList<ConceptFacade> modulePreferenceList = languageCoordinate.modulePreferenceListForLanguage();
        return new SimpleEqualityBasedListProperty<>(this,
                KometTerm.MODULE_PREFERENCE_LIST_FOR_LANGUAGE_COORDINATE.toXmlFragment(),
                FXCollections.observableArrayList(modulePreferenceNidListForLanguage().mapToList(ConceptProxy::make)));
    }

    @Override
    protected SimpleEqualityBasedListProperty<PatternFacade> makeDescriptionPatternPreferenceListProperty(LanguageCoordinate languageCoordinate) {
        return new SimpleEqualityBasedListProperty<>(this,
                "Description pattern list (fix)",
                FXCollections.observableArrayList(descriptionPatternPreferenceNidList().mapToList(PatternProxy::make)));
    }

    @Override
    public LanguageCoordinateRecord getOriginalValue() {
        return getValue();
    }


    @Override
    protected LanguageCoordinateRecord baseCoordinateChangedListenersRemoved(
            ObservableValue<? extends LanguageCoordinateRecord> observable,
            LanguageCoordinateRecord oldValue, LanguageCoordinateRecord newValue) {
        this.languageConceptProperty().setValue(newValue.languageConcept());
        this.descriptionPatternPreferenceListProperty().setAll(newValue.dialectPatternPreferenceNidList().mapToList(PatternFacade::make));
        this.dialectPatternPreferenceListProperty().setAll(newValue.dialectPatternPreferenceNidList().mapToList(PatternFacade::make));
        this.descriptionTypePreferenceListProperty().setAll(newValue.descriptionTypePreferenceNidList().mapToList(ConceptFacade::make));
        this.modulePreferenceListForLanguageProperty().setAll(newValue.modulePreferenceNidListForLanguage().mapToList(ConceptFacade::make));
        return newValue;
    }

}
