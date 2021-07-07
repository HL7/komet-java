package org.hl7.komet.framework;

import java.util.UUID;

/**
 *
 * @author kec
 */
public interface RefreshListener {
    UUID getListenerUuid();
    void refresh();
}
