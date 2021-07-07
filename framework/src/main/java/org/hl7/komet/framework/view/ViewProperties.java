package org.hl7.komet.framework.view;

import org.hl7.tinkar.coordinate.view.calculator.ViewCalculator;

import java.util.UUID;

public class ViewProperties {
    private final UUID viewUuid = UUID.randomUUID();
    private final ObservableViewNoOverride parentView;
    private final ObservableViewWithOverride overridableView;

    public ViewProperties(ObservableViewWithOverride overridableView, ObservableViewNoOverride parentView) {
        this.overridableView = overridableView;
        this.parentView = parentView;
    }

    public ViewCalculator calculator() {
        return this.overridableView.calculator();
    }

    public UUID viewUuid() {
        return viewUuid;
    }

    public ObservableViewNoOverride parentView() {
        return parentView;
    }

    public ObservableViewWithOverride nodeView() {
        return overridableView;
    }
}
