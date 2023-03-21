package com.backbase.goldensample.review.archunit;

import com.backbase.buildingblocks.archunit.test.AllArchitectureRules;
import com.backbase.buildingblocks.archunit.test.AllConfigurationRules;
import com.backbase.buildingblocks.archunit.test.AllControllerRules;
import com.backbase.buildingblocks.archunit.test.AllDataMappingRules;
import com.backbase.buildingblocks.archunit.test.AllGeneralCodingRules;
import com.backbase.buildingblocks.archunit.test.AllLoggingRules;
import com.backbase.buildingblocks.archunit.test.AllNamingConventionRules;
import com.backbase.buildingblocks.archunit.test.AllRelationalPersistenceRules;
import com.backbase.goldensample.review.Application;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.junit.ArchTests;

@AnalyzeClasses(packagesOf = Application.class, importOptions = ImportOption.DoNotIncludeTests.class)
public class ArchUnitRulesTest {

    @ArchTest
    ArchTests architectureRules = ArchTests.in(AllArchitectureRules.class);

    @ArchTest
    ArchTests configurationRules = ArchTests.in(AllConfigurationRules.class);

    @ArchTest
    ArchTests controllerRules = ArchTests.in(AllControllerRules.class);

    @ArchTest
    ArchTests dataMappingRules = ArchTests.in(AllDataMappingRules.class);

    @ArchTest
    ArchTests generalCodingRules = ArchTests.in(AllGeneralCodingRules.class);

    @ArchTest
    ArchTests loggingRules = ArchTests.in(AllLoggingRules.class);

    @ArchTest
    ArchTests namingConventionRules = ArchTests.in(AllNamingConventionRules.class);

    @ArchTest
    ArchTests relationalPersistenceRules = ArchTests.in(AllRelationalPersistenceRules.class);
}
