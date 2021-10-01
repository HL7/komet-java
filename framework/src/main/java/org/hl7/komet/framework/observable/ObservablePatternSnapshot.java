package org.hl7.komet.framework.observable;

import org.eclipse.collections.api.list.ImmutableList;
import org.hl7.tinkar.coordinate.stamp.calculator.Latest;
import org.hl7.tinkar.coordinate.view.calculator.ViewCalculator;
import org.hl7.tinkar.entity.PatternVersionRecord;

import java.util.Comparator;
import java.util.function.Predicate;

public class ObservablePatternSnapshot extends ObservableEntitySnapshot<ObservablePattern, ObservablePatternVersion, PatternVersionRecord> {

    public ObservablePatternSnapshot(ViewCalculator viewCalculator, ObservablePattern entity) {
        super(viewCalculator, entity);
    }

    @Override
    public ImmutableList<ObservablePatternVersion> getProcessedVersions() {
        return super.getProcessedVersions();
    }

    @Override
    public void filterProcessedVersions(Predicate<ObservablePatternVersion> filter) {
        super.filterProcessedVersions(filter);
    }

    @Override
    public void sortProcessedVersions(Comparator<ObservablePatternVersion> comparator) {
        super.sortProcessedVersions(comparator);
    }

    @Override
    public ObservablePattern observableEntity() {
        return super.observableEntity();
    }

    @Override
    public ImmutableList<ObservablePatternVersion> getUncommittedVersions() {
        return super.getUncommittedVersions();
    }

    @Override
    public ImmutableList<ObservablePatternVersion> getHistoricVersions() {
        return super.getHistoricVersions();
    }

    @Override
    public Latest<ObservablePatternVersion> getLatestVersion() {
        return super.getLatestVersion();
    }

}