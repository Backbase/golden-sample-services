package com.backbase.goldensample.product.archunit;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import com.backbase.goldensample.product.Application;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.mapstruct.Mapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

@AnalyzeClasses(packagesOf = Application.class)
public class NamingConventionRulesTest {

    @ArchTest
    ArchRule controllersShouldBeSuffixed = classes().that()
        .areAnnotatedWith(RestController.class)
        .should().haveSimpleNameEndingWith("Controller");

    @ArchTest
    ArchRule servicesShouldHaveServiceInName = classes().that()
        .areAnnotatedWith(Service.class)
        .should().haveSimpleNameContaining("Service");

    @ArchTest
    ArchRule mappersShouldBeSuffixed = classes().that()
        .areAnnotatedWith(Mapper.class)
        .should().haveSimpleNameEndingWith("Mapper");

    @ArchTest
    ArchRule configurationClassesShouldBeSuffixed = classes().that()
        .areAnnotatedWith(Configuration.class)
        .should().haveSimpleNameEndingWith("Configuration");
}
