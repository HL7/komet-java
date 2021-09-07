package org.hl7.komet.framework.rulebase;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

public class RuleBase {
    private static final Logger LOG = LoggerFactory.getLogger(RuleBase.class);

    private static RuleBase singleton;

    private final ImmutableList<Rule> rules;

    private RuleBase() {
        try (ScanResult scanResult =                // Assign scanResult in try-with-resources
                     new ClassGraph()                    // Create a new ClassGraph instance
                             .verbose()                      // If you want to enable logging to stderr
                             .enableAllInfo()                // Scan classes, methods, fields, annotations
                             .acceptPackages("org.hl7.komet.framework.rulebase")      // Scan org.hl7.komet.framework.rulebase and subpackages
                             .scan()) {                      // Perform the scan and return a ScanResult
            // Use the ScanResult within the try block, e.g.
            ClassInfoList rulesClassInfoList = scanResult.getClassesWithAnnotation(RuntimeRule.class);
            MutableList<Rule> mutableRules = Lists.mutable.empty();
            rulesClassInfoList.loadClasses().forEach(aClass -> {
                try {
                    mutableRules.add((Rule) aClass.getDeclaredConstructor().newInstance());
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    LOG.error("Error loading rules.", e);
                }
            });
            this.rules = mutableRules.toImmutable();
        }
    }

    public static ImmutableList<Consequence<?>> execute(ObservationStore observations) {
        if (RuleBase.singleton == null) {
            RuleBase.singleton = new RuleBase();
        }
        MutableList<Consequence<?>> consequences = Lists.mutable.empty();
        singleton.rules.forEach(rule -> {
            consequences.addAll(rule.execute(observations).castToList());
        });
        return consequences.toImmutable();
    }
}
