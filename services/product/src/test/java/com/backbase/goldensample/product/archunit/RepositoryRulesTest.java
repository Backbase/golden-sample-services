package com.backbase.goldensample.product.archunit;

import com.backbase.buildingblocks.archunit.RepositoryRules;
import com.backbase.goldensample.product.Application;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packagesOf = Application.class)
class RepositoryRulesTest {

    @ArchTest
    ArchRule repositoryCallsShouldBeTransactional = RepositoryRules.REPOSITORY_CALLS_SHOULD_BE_TRANSACTIONAL;

    @ArchTest
    ArchRule modifyingMethodsShouldHaveParameters = RepositoryRules.MODIFYING_METHODS_SHOULD_HAVE_PARAMETERS;
}
