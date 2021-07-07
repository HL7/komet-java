package org.hl7.komet.framework.temp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.hl7.tinkar.common.id.PublicIdStringKey;
import org.hl7.tinkar.coordinate.PathService;
import org.hl7.tinkar.coordinate.stamp.StampPathImmutable;
import org.hl7.tinkar.coordinate.view.calculator.ViewCalculator;
import org.hl7.tinkar.terms.ConceptFacade;
import org.hl7.tinkar.terms.PatternFacade;
import org.hl7.tinkar.terms.TinkarTerm;

import java.util.Collection;
import java.util.TreeMap;
import java.util.logging.Logger;

public class FxGet {
    private static Logger LOG = Logger.getLogger(FxGet.class.getName());

    private static ObservableList<ImmutableList<PatternFacade>> NAVIGATION_OPTIONS = FXCollections.observableArrayList();
    static {
        NAVIGATION_OPTIONS.addAll(
                Lists.immutable.of(TinkarTerm.EL_PLUS_PLUS_INFERRED_DIGRAPH),
                Lists.immutable.of(TinkarTerm.EL_PLUS_PLUS_STATED_DIGRAPH),
                Lists.immutable.of(TinkarTerm.EL_PLUS_PLUS_STATED_DIGRAPH, TinkarTerm.EL_PLUS_PLUS_STATED_DIGRAPH),
                Lists.immutable.of(TinkarTerm.PATH_ORIGINS_PATTERN),
                Lists.immutable.of(TinkarTerm.DEPENDENCY_MANAGEMENT_ASSEMBLAGE));
    }

    private static ObservableMap<PublicIdStringKey, StampPathImmutable> PATHS = FXCollections.observableMap(new TreeMap<>());

    private static void addPaths(ViewCalculator viewCalculator) {

        PathService.get().getPaths().forEach(stampPathImmutable -> {
            String pathDescription = viewCalculator.getPreferredDescriptionStringOrNid(stampPathImmutable.pathConceptNid());
            PublicIdStringKey pathKey = new PublicIdStringKey(stampPathImmutable.pathConcept().publicId(), pathDescription);
            PATHS.put(pathKey, stampPathImmutable);
        });
    }

    public static Collection<? extends ConceptFacade> allowedLanguages() {
        return Lists.immutable.of(TinkarTerm.ENGLISH_LANGUAGE, TinkarTerm.SPANISH_LANGUAGE).castToList();
    }
    public static ImmutableList<ImmutableList<? extends ConceptFacade>> allowedDescriptionTypeOrder() {

        return Lists.immutable.of(
                Lists.immutable.of(TinkarTerm.REGULAR_NAME_DESCRIPTION_TYPE, TinkarTerm.FULLY_QUALIFIED_NAME_DESCRIPTION_TYPE),
                Lists.immutable.of(TinkarTerm.FULLY_QUALIFIED_NAME_DESCRIPTION_TYPE, TinkarTerm.REGULAR_NAME_DESCRIPTION_TYPE));
    }

    public static ImmutableList<ImmutableList<? extends PatternFacade>> allowedDialectTypeOrder() {
        return Lists.immutable.of(
                Lists.immutable.of(TinkarTerm.US_DIALECT_PATTERN, TinkarTerm.GB_DIALECT_PATTERN),
                Lists.immutable.of(TinkarTerm.GB_DIALECT_PATTERN, TinkarTerm.US_DIALECT_PATTERN));
    }

    public static ObservableList<ImmutableList<PatternFacade>> navigationOptions() {
        return NAVIGATION_OPTIONS;
    }
    public static ObservableMap<PublicIdStringKey, StampPathImmutable> pathCoordinates(ViewCalculator viewCalculator) {
        if (PATHS.isEmpty()) {
            //TODO add commit listener, and update when new semantic or a commit.
            addPaths(viewCalculator);
        }
        return PATHS;
    }

}
