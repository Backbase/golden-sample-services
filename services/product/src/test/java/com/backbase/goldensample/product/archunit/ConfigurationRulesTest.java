package com.backbase.goldensample.product.archunit;

import com.backbase.buildingblocks.archunit.ConfigurationRules;
import com.backbase.goldensample.product.Application;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packagesOf = Application.class)
public class ConfigurationRulesTest {

    @ArchTest
    ArchRule configurationClassesShouldBeUnderBackbaseKey = ConfigurationRules.CONFIGURATION_CLASSES_SHOULD_BE_UNDER_BACKBASE_KEY;

    @ArchTest
    ArchRule configurationClassesShouldBeValidated = ConfigurationRules.CONFIGURATION_CLASSES_SHOULD_BE_VALIDATED;

// Multitenancy is not yet implemented: https://github.com/Backbase/golden-sample-services/issues/6
//    @ArchTest
//    ArchRule configurationClassesShouldBeContextScoped = ConfigurationRules.CONFIGURATION_CLASSES_SHOULD_BE_CONTEXT_SCOPED;
//
//    @ArchTest
//    ArchRule apiClientBeansShouldBeContextScoped = ConfigurationRules.API_CLIENT_BEANS_SHOULD_BE_CONTEXT_SCOPED;
}
