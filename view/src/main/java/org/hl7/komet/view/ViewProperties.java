package org.hl7.komet.view;

import org.hl7.tinkar.coordinate.view.ViewCoordinateRecord;

import java.util.UUID;

public class ViewProperties {
    private final UUID viewUuid = UUID.randomUUID();
    private final ObservableViewNoOverride parentView;
    private final ObservableViewWithOverride overridableView;

    public ViewProperties(ObservableViewWithOverride overridableView, ObservableViewNoOverride parentView) {
        this.overridableView = overridableView;
        this.parentView = parentView;
    }

    public UUID viewUuid() {
        return viewUuid;
    }


    public ObservableViewNoOverride parentView() {
        return parentView;
    }

    public ObservableViewWithOverride overridableView() {
        return overridableView;
    }
}
