package org.hl7.komet.framework.alerts;

import javafx.concurrent.Task;

/**
 *
 * @author kec
 */

public interface Resolver {
    String getTitle();

    String getDescription();

    Task<Void> resolve();

    ResolutionPersistence getPersistence();
}
