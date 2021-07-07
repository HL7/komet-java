package org.hl7.komet.framework.alerts;

import java.util.*;
import java.util.concurrent.Callable;

public class AlertObject implements Comparable<AlertObject> {
    final UUID alertId = UUID.randomUUID();
    final int[] affectedComponents;
    final String alertTitle;
    final String alertDescription;
    final AlertType alertType;
    final Throwable throwable;
    final AlertCategory alertCategory;
    final Callable<Boolean> resolutionTester;
    private final List<AlertResolver> alertResolvers = new ArrayList<>();

    public AlertObject(String alertTitle,
                       String alertDescription,
                       AlertType alertType,
                       Throwable throwable,
                       AlertCategory alertCategory,
                       Callable<Boolean> resolutionTester,
                       int... affectedComponents) {
        this.affectedComponents = affectedComponents;
        this.alertTitle = alertTitle;
        this.alertDescription = alertDescription;
        this.alertType = alertType;
        this.throwable = throwable;
        this.alertCategory = alertCategory;
        this.resolutionTester = resolutionTester;
    }

    public static AlertObject makeWarning(String alertTitle, String alertDescription) {
        return new AlertObject(alertTitle, alertDescription, AlertType.WARNING, AlertCategory.UNSPECIFIED);
    }


    public AlertObject(String alertTitle, String alertDescription, AlertType alertType, AlertCategory alertCategory, int... affectedComponents) {
        this(alertTitle, alertDescription, alertType, null, alertCategory, null, affectedComponents);
    }

    public Optional<Throwable> getThrowable() {
        return Optional.ofNullable(throwable);
    }

    public int[] getAffectedComponents() {
        return affectedComponents;
    }

    public String getAlertTitle() {
        return alertTitle;
    }

    public String getAlertDescription() {
        return alertDescription;
    }

    public AlertType getAlertType() {
        return alertType;
    }

    public AlertCategory getAlertCategory() {
        return alertCategory;
    }

    public Optional<Callable<Boolean>> getResolutionTester() {
        return Optional.ofNullable(resolutionTester);
    }

    public List<AlertResolver> getResolvers() {
        return alertResolvers;
    }

    public Boolean failCommit() {
        return getAlertType().preventsCheckerPass();
    }

    @Override
    public int compareTo(AlertObject o) {
        return this.alertId.compareTo(o.alertId);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() +  ", alertTitle=" + alertTitle + ", alertType=" + alertType +
                ", alertDescription=" + alertDescription + ", resolvers=" + alertResolvers + ", resolutionTester="
                + resolutionTester + " " + Arrays.toString(affectedComponents);
    }
}