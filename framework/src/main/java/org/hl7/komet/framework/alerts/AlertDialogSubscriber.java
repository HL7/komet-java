package org.hl7.komet.framework.alerts;

import org.hl7.komet.framework.Dialogs;
import org.hl7.tinkar.common.alert.AlertCategory;
import org.hl7.tinkar.common.alert.AlertObject;
import org.hl7.tinkar.common.alert.AlertType;

import java.util.concurrent.Callable;
import java.util.concurrent.Flow;

/**
 * Presents dialogs for alerts
 */
public class AlertDialogSubscriber implements Flow.Subscriber<AlertObject> {
    Flow.Subscription subscription;

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        this.subscription.request(1);

    }

    @Override
    public void onNext(AlertObject item) {
        Dialogs.showDialogForAlert(item);
        this.subscription.request(1);
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
