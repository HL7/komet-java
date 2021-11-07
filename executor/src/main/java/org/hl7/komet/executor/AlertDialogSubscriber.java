package org.hl7.komet.executor;

import com.google.auto.service.AutoService;
import javafx.application.Platform;
import org.hl7.komet.framework.Dialogs;
import org.hl7.tinkar.common.alert.*;
import org.hl7.tinkar.common.id.PublicIdStringKey;
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
    Flow.Subscription subscription;

    public AlertDialogSubscriber() {
        this(AlertStreams.ROOT_ALERT_STREAM_KEY);
    }

    public AlertDialogSubscriber(PublicIdStringKey<AlertStream> alertStreamKey) {
        LOG.info("Constructing AlertDialogSubscriber");
        AlertStreams.get(alertStreamKey).subscribe(this);
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        this.subscription.request(1);
    }

    @Override
    public void onNext(AlertObject item) {
        this.subscription.request(1);
        Platform.runLater(() -> Dialogs.showDialogForAlert(item));
    }

    @Override
    public void onError(Throwable throwable) {
        // Create a new alert object, and show Alert dialog.
        String alertTitle = "Error in Alert reactive stream.";
        String alertDescription = throwable.getLocalizedMessage();
        AlertType alertType = AlertType.ERROR;

        AlertCategory alertCategory = AlertCategory.ENVIRONMENT;
        Callable<Boolean> resolutionTester = null;
        int[] affectedComponents = new int[0];

        AlertObject alert = new AlertObject(alertTitle,
                alertDescription, alertType, throwable,
                alertCategory, resolutionTester, affectedComponents);
        Dialogs.showDialogForAlert(alert);
    }

    @Override
    public void onComplete() {

    }
}
