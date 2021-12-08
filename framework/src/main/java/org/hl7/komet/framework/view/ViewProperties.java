package org.hl7.komet.framework.view;

import org.hl7.komet.preferences.KometPreferences;
import org.hl7.tinkar.common.binary.DecoderInput;
import org.hl7.tinkar.coordinate.view.ViewCoordinateRecord;
import org.hl7.tinkar.coordinate.view.calculator.ViewCalculator;

import java.util.Optional;

public class ViewProperties {

    private final ObservableViewNoOverride parentView;
    private final ObservableViewWithOverride overridableView;

    public ViewProperties(ObservableViewWithOverride overridableView, ObservableViewNoOverride parentView) {
        this.overridableView = overridableView;
        this.parentView = parentView;
    }

    public static ViewProperties make(ObservableViewNoOverride parentView,
                                      KometPreferences preferencesNode) {
        return makeOverridableView(preferencesNode, parentView);
    }

    private static ViewProperties makeOverridableView(KometPreferences preferencesNode, ObservableViewNoOverride parentView) {
        Optional<byte[]> optionalCoordinateData = preferencesNode.getByteArray(Keys.VIEW_COORDINATE_BYTES);
        if (optionalCoordinateData.isEmpty()) {
            throw new IllegalStateException(Keys.VIEW_COORDINATE_BYTES + " not initialized: " + preferencesNode);
        }
        ViewCoordinateRecord viewCoordinateRecord = ViewCoordinateRecord.decode(new DecoderInput(optionalCoordinateData.get()));
        ViewProperties viewProperties = parentView.makeOverridableViewProperties();
        viewProperties.overridableView.setOverrides(viewCoordinateRecord);
        return viewProperties;
    }

    public ViewCalculator calculator() {
        return this.overridableView.calculator();
    }

    public ObservableViewNoOverride parentView() {
        return parentView;
    }

    public ObservableViewWithOverride nodeView() {
        return overridableView;
    }

    public enum Keys {
        VIEW_COORDINATE_BYTES,
    }
}
