package com.backbase.goldensample.product.archunit;

import static com.tngtech.archunit.lang.SimpleConditionEvent.violated;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import com.backbase.goldensample.product.Application;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@AnalyzeClasses(packagesOf = Application.class)
public class ConfigurationRulesTest {

    private static final String BACKBASE_CONFIG_KEY = "backbase.";

    ArchCondition<JavaClass> backbaseKeyAnnotation = new ArchCondition<>("be under backbase key") {
        @Override
        public void check(JavaClass input, ConditionEvents events) {
            ConfigurationProperties configurationProperties = input.getAnnotationOfType(ConfigurationProperties.class);
            String value = configurationProperties.value();
            if (value.isBlank()) {
                value = configurationProperties.prefix();
            }
            if (!value.startsWith(BACKBASE_CONFIG_KEY)) {
                String msg = String.format("@%s annotation on %s should have value starting with '%s'",
                    ConfigurationProperties.class.getSimpleName(), input.getDescription(), BACKBASE_CONFIG_KEY);
                events.add(violated(input, msg));
            }
        }
    };

    @ArchTest
    ArchRule configurationClassesShouldBeUnderBackbaseKey = classes().that()
        .areAnnotatedWith(Configuration.class)
        .and().areAnnotatedWith(ConfigurationProperties.class)
        .should(backbaseKeyAnnotation);

    @ArchTest
    ArchRule configurationClassesShouldBeValidated = classes().that()
        .areAnnotatedWith(Configuration.class)
        .should().beAnnotatedWith(Validated.class);

//    @ArchTest
//    ArchRule configurationClassesShouldBeContextScoped = classes().that()
//        .areAnnotatedWith(Configuration.class)
//        .should().beAnnotatedWith(ContextScoped.class)
//        .because("multi-tenancy support requires context scoped beans");
}
