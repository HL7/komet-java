import org.hl7.komet.framework.KometNodeFactory;
import org.hl7.komet.list.ListNodeFactory;
import org.hl7.komet.set.SetNodeFactory;
import org.hl7.komet.table.TableNodeFactory;

module org.hl7.komet.list {

    requires transitive org.hl7.komet.framework;
    requires static org.hl7.tinkar.autoservice;

    opens org.hl7.komet.list;
    exports org.hl7.komet.list;

    opens org.hl7.komet.set;
    exports org.hl7.komet.set;

    opens org.hl7.komet.table;
    exports org.hl7.komet.table;

    provides KometNodeFactory
            with ListNodeFactory, SetNodeFactory, TableNodeFactory;

}