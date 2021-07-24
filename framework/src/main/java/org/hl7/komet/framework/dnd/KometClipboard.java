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
import org.hl7.tinkar.terms.EntityFacade;
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
    public static final DataFormat KOMET_CONCEPT_VERSION_PROXY = new DataFormat("application/komet-concept-version-proxy");
    public static final DataFormat KOMET_PATTERN_VERSION_PROXY = new DataFormat("application/komet-pattern-version-proxy");
    public static final DataFormat KOMET_SEMANTIC_VERSION_PROXY = new DataFormat("application/komet-semantic-version-proxy");

    public static final Set<DataFormat> CONCEPT_TYPES = new HashSet<>(Arrays.asList(KOMET_CONCEPT_VERSION_PROXY, KOMET_CONCEPT_PROXY));
    public static final Set<DataFormat> PATTERN_TYPES = new HashSet<>(Arrays.asList(KOMET_PATTERN_VERSION_PROXY, KOMET_PATTERN_PROXY));
    public static final Set<DataFormat> SEMANTIC_TYPES = new HashSet<>(Arrays.asList(KOMET_SEMANTIC_VERSION_PROXY, KOMET_SEMANTIC_PROXY));

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
                (t) -> t.publicId().toString());
    }

    //~--- constructors --------------------------------------------------------
    public KometClipboard(Component component) {
        if (component instanceof Version version) {
            if (version instanceof ConceptEntityVersion conceptVersion) {
                this.put(KOMET_CONCEPT_VERSION_PROXY, conceptVersion.toXmlFragment());
            } else if (version instanceof SemanticEntityVersion semanticVersion) {
                this.put(KOMET_SEMANTIC_VERSION_PROXY, semanticVersion.toXmlFragment());
            } else if (version instanceof PatternEntityVersion patternVersion) {
                this.put(KOMET_PATTERN_VERSION_PROXY, patternVersion.toXmlFragment());
            }
        } else if (component instanceof EntityFacade entityFacade) {
            addEntity(entityFacade);
        }
        addExtra(DataFormat.PLAIN_TEXT, component);

    }

    private void addEntity(EntityFacade entityFacade) {
        if (entityFacade instanceof ConceptEntity conceptEntity) {
            this.put(KOMET_CONCEPT_PROXY, conceptEntity.toXmlFragment());
        } else if (entityFacade instanceof PatternEntity patternEntity) {
            this.put(KOMET_PATTERN_PROXY, patternEntity.toXmlFragment());
        } else if (entityFacade instanceof SemanticEntity semanticEntity) {
            this.put(KOMET_SEMANTIC_PROXY, semanticEntity.toXmlFragment());
        } else {
            Entity<?> entity = Entity.getFast(entityFacade);
            addEntity(entity);
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

    @Override
    public Object get(Object key) {
        return super.get(key);
    }
}
