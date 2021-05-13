package com.backbase.goldensample.product.archunit;

import com.backbase.buildingblocks.archunit.ControllerRules;
import com.backbase.goldensample.product.Application;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packagesOf = Application.class)
public class ControllerRulesTest {

    @ArchTest
    ArchRule dateAndTimeControllerParametersShouldBeAnnotated = ControllerRules.DATE_AND_TIME_CONTROLLER_PARAMETERS_SHOULD_BE_ANNOTATED;
}
