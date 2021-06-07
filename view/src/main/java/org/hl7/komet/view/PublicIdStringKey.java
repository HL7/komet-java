package org.hl7.komet.view;


import org.hl7.tinkar.common.id.PublicId;
import org.hl7.tinkar.common.util.text.NaturalOrder;

import java.util.UUID;

public class PublicIdStringKey implements Comparable<PublicIdStringKey> /*, Marshalable */ {
    public static final int marshalVersion = 1;

    final PublicId publicId;
    String string;

    public PublicIdStringKey(PublicId publicId, String string) {
        this.publicId = publicId;
        this.string = string;
    }

    public String[] toStringArray() {
        return new String[] {publicId.toString(), string};
    }
    @Override
    public int compareTo(PublicIdStringKey o) {
        int comparison = NaturalOrder.compareStrings(this.string, o.string);
        if (comparison != 0) {
            return comparison;
        }
        return publicId.compareTo(o.publicId);
    }

    @Override
    public int hashCode() {
        return publicId.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PublicIdStringKey that = (PublicIdStringKey) o;
        return publicId.equals(that.publicId);
    }

    public PublicId getPublicId() {
        return publicId;
    }

    public String getString() {
        return string;
    }

    @Override
    public String toString() {
        return string;
    }

    public String toExternalString() {
        return this.publicId.toString() + "@" + this.string;
    }

    public void updateString(String string) {
        this.string = string;
    }
}
