package com.backbase.goldensample.product.archunit;

import static com.tngtech.archunit.lang.SimpleConditionEvent.violated;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;

import com.backbase.goldensample.product.Application;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@AnalyzeClasses(packagesOf = Application.class)
class LoggingRulesTest {

    @ArchTest
    ArchRule loggersShouldBePrivateStaticFinal = fields().that()
        .haveRawType(Logger.class)
        .should().bePrivate()
        .andShould().beStatic()
        .andShould().beFinal()
        .because("it's a best practice agreed upon by the Backend Guild");

    ArchCondition<JavaMethod> haveLoggingCalls = new ArchCondition<>("should have logging calls") {
        @Override
        public void check(JavaMethod item, ConditionEvents events) {
            boolean callsLogger = item.getCallsFromSelf().stream()
                .anyMatch(call -> call.getTarget().getOwner().isAssignableFrom(Logger.class));
            if (!callsLogger) {
                events.add(violated(item, item.getDescription() + " doesn't call logging methods"));
            }
        }
    };

    @ArchTest
    ArchRule servicesShouldHaveLogging = methods().that()
        .areDeclaredInClassesThat().areAnnotatedWith(Service.class)
        .and().arePublic()
        .should(haveLoggingCalls);

    ArchCondition<JavaMethod> logParameterizedMessages = new ArchCondition<>("should log parameterized messages") {
        @Override
        public void check(JavaMethod item, ConditionEvents events) {
            boolean hasMessageOnlyCalls = item.getCallsFromSelf().stream()
                .filter(call -> call.getTarget().getOwner().isAssignableFrom(Logger.class))
                .map(call -> call.getTarget().getRawParameterTypes())
                .filter(parameterTypes -> parameterTypes.size() == 1)
                .anyMatch(parameterTypes -> parameterTypes.get(0).isAssignableFrom(String.class));
            if (hasMessageOnlyCalls) {
                events.add(violated(item, item.getDescription() + " logs only a message without context"));
            }
        }
    };

    @ArchTest
    ArchRule loggingMessagesShouldBeParameterized = methods().should(logParameterizedMessages);
}
