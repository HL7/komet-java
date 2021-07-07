package org.hl7.komet.framework.dnd;

//~--- JDK imports ------------------------------------------------------------
import java.nio.ByteBuffer;
import java.util.*;

import java.util.function.Function;

//~--- non-JDK imports --------------------------------------------------------
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import org.hl7.tinkar.component.*;
import org.hl7.tinkar.dto.*;
import org.hl7.tinkar.entity.*;
import org.hl7.tinkar.terms.EntityProxy;

//~--- classes ----------------------------------------------------------------
/**
 *
 * @author kec
 */
public class KometClipboard
        extends ClipboardContent {

    public static final DataFormat KOMET_PROXY_LIST = new DataFormat("application/komet-proxy-list");
    public static final DataFormat KOMET_CONCEPT_PROXY = new DataFormat("application/komet-concept-proxy");
    public static final DataFormat KOMET_PATTERN_PROXY = new DataFormat("application/komet-pattern-proxy");
    public static final DataFormat KOMET_SEMANTIC_PROXY = new DataFormat("application/komet-semantic-proxy");
    public static final DataFormat KOMET_CONCEPT_DTO = new DataFormat("application/komet-concept-dto");
    public static final DataFormat KOMET_PATTERN_DTO = new DataFormat("application/komet-pattern-dto");
    public static final DataFormat KOMET_SEMANTIC_DTO = new DataFormat("application/komet-semantic-dto");
    public static final DataFormat KOMET_CONCEPT_VERSION_DTO = new DataFormat("application/komet-concept-version-dto");
    public static final DataFormat KOMET_PATTERN_VERSION_DTO = new DataFormat("application/komet-pattern-version-dto");
    public static final DataFormat KOMET_SEMANTIC_VERSION_DTO = new DataFormat("application/komet-semantic-version-dto");

    public static final Set<DataFormat> CONCEPT_TYPES = new HashSet<>(Arrays.asList(KOMET_CONCEPT_DTO, KOMET_CONCEPT_VERSION_DTO, KOMET_CONCEPT_PROXY));
    public static final Set<DataFormat> PATTERN_TYPES = new HashSet<>(Arrays.asList(KOMET_PATTERN_DTO, KOMET_PATTERN_VERSION_DTO, KOMET_PATTERN_PROXY));
    public static final Set<DataFormat> SEMANTIC_TYPES = new HashSet<>(Arrays.asList(KOMET_SEMANTIC_DTO, KOMET_SEMANTIC_VERSION_DTO, KOMET_SEMANTIC_PROXY));

    public static boolean containsAny(Collection<?> c1,
                                      Collection<?> c2) {
        return !Collections.disjoint(c1, c2);
    }


    private static final HashMap<DataFormat, Function<? super Component, ? extends Object>> GENERATOR_MAP
            = new HashMap<>();

    //~--- static initializers -------------------------------------------------
    static {
        GENERATOR_MAP.put(
                DataFormat.HTML,
                (t) -> {
                    throw new UnsupportedOperationException();
                });
        GENERATOR_MAP.put(
                DataFormat.PLAIN_TEXT,
                (t) -> t.publicId());
    }

    //~--- constructors --------------------------------------------------------
    public KometClipboard(Component component) {
        if (component instanceof EntityProxy entityProxy) {
            component = EntityService.get().getEntityFast(entityProxy.nid());
        }

        if (component instanceof ConceptEntity conceptEntity) {
            this.put(KOMET_CONCEPT_PROXY, conceptEntity.toXmlFragment());
            this.put(KOMET_CONCEPT_DTO, ConceptChronologyDTO.make(conceptEntity));
            addExtra(DataFormat.PLAIN_TEXT, conceptEntity);
        } else if (component instanceof ConceptEntityVersion conceptEntityVersion) {
            this.put(KOMET_CONCEPT_PROXY, conceptEntityVersion.chronology().toXmlFragment());
            this.put(KOMET_CONCEPT_VERSION_DTO, ConceptVersionDTO.make(conceptEntityVersion));
            addExtra(DataFormat.PLAIN_TEXT, conceptEntityVersion);
        } else if (component instanceof PatternEntity patternEntity) {
            this.put(KOMET_PATTERN_PROXY, patternEntity.toXmlFragment());
            this.put(KOMET_PATTERN_DTO, PatternChronologyDTO.make(patternEntity));
            addExtra(DataFormat.PLAIN_TEXT, patternEntity);
        } else if (component instanceof PatternEntityVersion patternEntityVersion) {
            this.put(KOMET_PATTERN_PROXY, patternEntityVersion.chronology().toXmlFragment());
            this.put(KOMET_PATTERN_VERSION_DTO, PatternVersionDTO.make(patternEntityVersion));
            addExtra(DataFormat.PLAIN_TEXT, patternEntityVersion);
        } else if (component instanceof SemanticEntity semanticEntity) {
            this.put(KOMET_SEMANTIC_PROXY, semanticEntity.toXmlFragment());
            this.put(KOMET_SEMANTIC_DTO, SemanticChronologyDTO.make(semanticEntity));
            addExtra(DataFormat.PLAIN_TEXT, semanticEntity);
        } else if (component instanceof SemanticEntityVersion semanticEntityVersion) {
            this.put(KOMET_SEMANTIC_PROXY, semanticEntityVersion.chronology().toXmlFragment());
            this.put(KOMET_SEMANTIC_VERSION_DTO, SemanticVersionDTO.make(semanticEntityVersion));
            addExtra(DataFormat.PLAIN_TEXT, semanticEntityVersion);
        }
    }

    public KometClipboard(List<EntityProxy> entityProxyList) {
        this.put(KOMET_PROXY_LIST, entityProxyList);
        this.put(DataFormat.PLAIN_TEXT, entityProxyList.toString());
    }

    //~--- methods -------------------------------------------------------------
    private void addExtra(DataFormat format, Component component) {
        put(format, GENERATOR_MAP.get(format)
                .apply(component));
    }


}
