package com.backbase.goldensample.product.archunit;

import com.backbase.buildingblocks.archunit.NamingConventionRules;
import com.backbase.goldensample.product.Application;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packagesOf = Application.class)
public class NamingConventionRulesTest {

    @ArchTest
    ArchRule controllersShouldBeSuffixed = NamingConventionRules.CONTROLLERS_SHOULD_BE_SUFFIXED;

    @ArchTest
    ArchRule servicesShouldHaveServiceInName = NamingConventionRules.SERVICES_SHOULD_HAVE_SERVICE_IN_NAME;

    @ArchTest
    ArchRule mappersShouldBeSuffixed = NamingConventionRules.MAPPERS_SHOULD_BE_SUFFIXED;

    @ArchTest
    ArchRule configurationClassesShouldBeSuffixed = NamingConventionRules.CONFIGURATION_CLASSES_SHOULD_BE_SUFFIXED;

    @ArchTest
    ArchRule repositoryClassesShouldBeSuffixed = NamingConventionRules.REPOSITORY_CLASSES_SHOULD_BE_SUFFIXED;

    @ArchTest
    ArchRule actuatorEndpointClassesShouldBeSuffixed = NamingConventionRules.ACTUATOR_ENDPOINT_CLASSES_SHOULD_BE_SUFFIXED;

    @ArchTest
    ArchRule eventHandlerClassesShouldBeSuffixed = NamingConventionRules.EVENT_HANDLER_CLASSES_SHOULD_BE_SUFFIXED;
}
