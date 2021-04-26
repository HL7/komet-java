import org.hl7.komet.artifact.ArtifactExportNodeFactory;
import org.hl7.komet.artifact.ArtifactImportNodeFactory;
import org.hl7.komet.framework.NodeFactory;

module org.hl7.komet.artifact {

    requires static org.hl7.tinkar.autoservice;
    requires transitive org.hl7.komet.framework;

    opens org.hl7.komet.artifact;
    exports org.hl7.komet.artifact;
    
    provides NodeFactory
            with ArtifactExportNodeFactory, ArtifactImportNodeFactory;

}