package com.backbase.goldensample.product.archunit;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

import com.backbase.goldensample.product.Application;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

@AnalyzeClasses(packagesOf = Application.class)
public class ArchitectureRulesTest {

    @ArchTest
    ArchRule layeredArchitecture = layeredArchitecture()
        .layer("Controller").definedBy("..api..")
        .layer("Service").definedBy("..service..")
        .layer("Mapping").definedBy("..mapper..")
        .layer("Configuration").definedBy("..config..")
        .layer("Persistence").definedBy("..persistence..")
        .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
        .whereLayer("Mapping").mayOnlyBeAccessedByLayers("Controller", "Configuration", "Persistence")
        .whereLayer("Service").mayOnlyBeAccessedByLayers("Controller");

    @ArchTest
    ArchRule serviceClassesShouldBeInServicePackage = classes().that()
        .areAnnotatedWith(Service.class)
        .or().haveNameMatching(".*Service*")
        .should().resideInAPackage("..service..");

    @ArchTest
    ArchRule securedServiceLayer = methods().that()
        .areDeclaredInClassesThat().areAnnotatedWith(Service.class)
        .and().arePublic()
        .should().notBeAnnotatedWith(NotNull.class)
        .orShould().beAnnotatedWith(PostConstruct.class);

}
