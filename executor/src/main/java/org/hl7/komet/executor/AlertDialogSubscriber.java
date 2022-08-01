package org.hl7.komet.executor;

import com.google.auto.service.AutoService;
import javafx.application.Platform;
import org.hl7.komet.framework.Dialogs;
import org.hl7.tinkar.common.alert.*;
import org.hl7.tinkar.common.id.PublicIdStringKey;
import org.hl7.tinkar.common.service.TinkExecutor;
import org.hl7.tinkar.common.util.broadcast.Broadcaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.Flow;

/**
 * Presents dialogs for alerts
 */
@AutoService(AlertReportingService.class)
public class AlertDialogSubscriber implements AlertReportingService {
    private static final Logger LOG = LoggerFactory.getLogger(AlertDialogSubscriber.class);

    public AlertDialogSubscriber() {
        this(AlertStreams.ROOT_ALERT_STREAM_KEY);
    }

    public AlertDialogSubscriber(PublicIdStringKey<Broadcaster<AlertObject>> alertStreamKey) {
        LOG.info("Constructing AlertDialogSubscriber");
        AlertStreams.get(alertStreamKey).addSubscriberWithWeakReference(this);
    }

    @Override
    public void onNext(AlertObject item) {
        Platform.runLater(() -> Dialogs.showDialogForAlert(item));
    }
}
