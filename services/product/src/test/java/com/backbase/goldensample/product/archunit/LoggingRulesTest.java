package com.backbase.goldensample.product.archunit;

import com.backbase.buildingblocks.archunit.LoggingRules;
import com.backbase.goldensample.product.Application;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packagesOf = Application.class)
class LoggingRulesTest {

    @ArchTest
    ArchRule loggersShouldBePrivateStaticFinal = LoggingRules.LOGGERS_SHOULD_BE_PRIVATE_STATIC_FINAL;

    @ArchTest
    ArchRule servicesShouldHaveLogging = LoggingRules.SERVICES_SHOULD_HAVE_LOGGING;

    @ArchTest
    ArchRule loggingMessagesShouldBeParameterized = LoggingRules.LOGGING_MESSAGES_SHOULD_BE_PARAMETERIZED;
}
