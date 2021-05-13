package com.backbase.goldensample.product.archunit;

import com.backbase.buildingblocks.archunit.ArchitectureRules;
import com.backbase.goldensample.product.Application;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packagesOf = Application.class)
public class ArchitectureRulesTest {

    @ArchTest
    ArchRule layeredArchitectureShouldBeAdopted = ArchitectureRules.LAYERED_ARCHITECTURE_SHOULD_BE_ADOPTED;

    @ArchTest
    ArchRule controllerClassesShouldBeInAppropriatePackage = ArchitectureRules.CONTROLLER_CLASSES_SHOULD_BE_IN_APPROPRIATE_PACKAGE;

    @ArchTest
    ArchRule serviceClassesShouldBeInAppropriatePackage = ArchitectureRules.SERVICE_CLASSES_SHOULD_BE_IN_APPROPRIATE_PACKAGE;

    @ArchTest
    ArchRule mapperClassesShouldBeInAppropriatePackage = ArchitectureRules.MAPPER_CLASSES_SHOULD_BE_IN_APPROPRIATE_PACKAGE;

    @ArchTest
    ArchRule configurationClassesShouldBeInAppropriatePackage = ArchitectureRules.CONFIGURATION_CLASSES_SHOULD_BE_IN_APPROPRIATE_PACKAGE;

    @ArchTest
    ArchRule repositoryClassesShouldBeInAppropriatePackage = ArchitectureRules.REPOSITORY_CLASSES_SHOULD_BE_IN_APPROPRIATE_PACKAGE;

    @ArchTest
    ArchRule clientClassesShouldBeInAppropriatePackage = ArchitectureRules.CLIENT_CLASSES_SHOULD_BE_IN_APPROPRIATE_PACKAGE;

    @ArchTest
    ArchRule actuatorEndpointClassesShouldBeInAppropriatePackage = ArchitectureRules.ACTUATOR_ENDPOINT_CLASSES_SHOULD_BE_IN_APPROPRIATE_PACKAGE;

    @ArchTest
    ArchRule eventHandlerClassesShouldBeInAppropriatePackage = ArchitectureRules.EVENT_HANDLER_CLASSES_SHOULD_BE_IN_APPROPRIATE_PACKAGE;

// Security is not yet implemented: https://github.com/Backbase/golden-sample-services/issues/4
//    @ArchTest
//    ArchRule servicesShouldBeSecured = ArchitectureRules.SERVICES_SHOULD_BE_SECURED;
}
