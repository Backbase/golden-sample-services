package com.backbase.goldensample.product.archunit;

import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_FIELD_INJECTION;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_JODATIME;

import com.backbase.buildingblocks.archunit.BackbaseGeneralCodingRules;
import com.backbase.buildingblocks.archunit.option.DoNotIncludeMapperImplementations;
import com.backbase.goldensample.product.Application;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packagesOf = Application.class, importOptions = {
    ImportOption.DoNotIncludeTests.class,
    DoNotIncludeMapperImplementations.class
})
class GeneralCodingRulesTest {

    @ArchTest
    ArchRule noClassesShouldAccessStandardStreams = NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;

    @ArchTest
    ArchRule noClassesShouldThrowGenericExceptions = NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS;

    @ArchTest
    ArchRule noClassesShouldUseJavaUtilLogging = NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;

    @ArchTest
    ArchRule noClassesShouldUseJodatime = NO_CLASSES_SHOULD_USE_JODATIME;

    @ArchTest
    ArchRule noClassesShouldUseFieldInjection = NO_CLASSES_SHOULD_USE_FIELD_INJECTION;

    @ArchTest
    ArchRule noMutableCollections = BackbaseGeneralCodingRules.NO_MUTABLE_COLLECTIONS;

    @ArchTest
    ArchRule noJavaUtilDate = BackbaseGeneralCodingRules.NO_JAVA_UTIL_DATE;

    @ArchTest
    ArchRule noJavaUtilCalendar = BackbaseGeneralCodingRules.NO_JAVA_UTIL_CALENDAR;

    @ArchTest
    ArchRule noSimpleDateFormat = BackbaseGeneralCodingRules.NO_SIMPLE_DATE_FORMAT;
}
