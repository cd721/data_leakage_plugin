package com.github.cd721.data_leakage_plugin;

import com.google.common.collect.Sets;
import com.intellij.codeInspection.InspectionEP;
import com.intellij.codeInspection.InspectionProfileEntry;
import com.intellij.codeInspection.LocalInspectionEP;
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixture4TestCase;
import org.intellij.lang.annotations.Language;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class FlakeInspectionSuppressorTest extends LightPlatformCodeInsightFixture4TestCase {
    private Set<String> excluded = Sets.newHashSet("PyInterpreterInspection", "PyMandatoryEncodingInspection", "PyMissingOrEmptyDocstringInspection");

    @Test
    //Inspections disabled by default in test
    public void testSuppressErrors() {
        //Inspections are available at runtime
        //Global inspections can be used in batch mode, local inspections cannot
        Stream<InspectionProfileEntry> inspections = Arrays.stream(LocalInspectionEP.LOCAL_INSPECTION.getExtensionList().stream()
                .map(InspectionEP::instantiateTool)
                .filter(e -> e.getShortName().startsWith("Py")) // Find all Python inspections
                .filter(e -> !excluded.contains(e.getShortName()))
                .toArray(InspectionProfileEntry[]::new));

        //Tell testing environment to enable inspections
        InspectionProfileEntry[] var = inspections.toArray(InspectionProfileEntry[]::new);
        for (InspectionProfileEntry i : var) {
            myFixture.enableInspections(i);
        }

        //language=Python
        assertNoErrors("def foo():\n    x = l # noqa");
        //language=Python
        assertNoErrors("# flake8: noqa\ndef foo():\n   x = 1 # n");
    }

    //Use a fixture to talk to the headless IDE
    //Create a dummy file and make sure inspections are suppressed
    private void assertNoErrors(@Language("Python") String code) {
        myFixture.configureByText("file.py", code);//should not show an error
        myFixture.checkHighlighting(); // check for error markers
    }
}