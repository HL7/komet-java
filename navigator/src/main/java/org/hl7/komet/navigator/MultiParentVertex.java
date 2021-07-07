package org.hl7.komet.navigator;

import java.util.OptionalInt;

public interface MultiParentVertex {
    boolean isRoot();
    boolean isDefined();
    boolean isMultiParent();
    boolean isSecondaryParentOpened();
    int getConceptNid();
    int getTypeNid();
    int getMultiParentDepth();
    OptionalInt getOptionalParentNid();

}
