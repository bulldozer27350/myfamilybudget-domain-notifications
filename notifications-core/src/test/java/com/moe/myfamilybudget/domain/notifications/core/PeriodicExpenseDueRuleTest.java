package com.moe.myfamilybudget.domain.notifications.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.moe.myfamilybudget.domain.notifications.calculation.PeriodicExpenseDueInput;
import com.moe.myfamilybudget.domain.notifications.model.NotificationMessage;

class PeriodicExpenseDueRuleTest {

    private final PeriodicExpenseDueRule rule = new PeriodicExpenseDueRule();

    @Test
    void notifiesWhenDueMonthIsUpcoming() {
        // Mois courant : 11 (novembre), anticipation 1 mois -> cible mois 12 (décembre)
        PeriodicExpenseDueInput.PeriodicExpenseLine line = new PeriodicExpenseDueInput.PeriodicExpenseLine(
                "c1", "Assurance auto", new BigDecimal("350.00"), List.of(6, 12));

        PeriodicExpenseDueInput input = new PeriodicExpenseDueInput(List.of(line), 11, 1);
        List<NotificationMessage> messages = rule.check(input);

        assertThat(messages).hasSize(1);
        NotificationMessage msg = messages.get(0);
        assertThat(msg.ruleKey()).isEqualTo("periodic-expense-due");
        assertThat(msg.entityId()).isEqualTo("c1:12");
        assertThat(msg.title()).contains("Assurance auto");
        assertThat(msg.body()).contains("décembre").contains("350.00 €");
    }

    @Test
    void doesNotNotifyWhenNoDueMonthUpcoming() {
        // Mois courant : 11 (novembre), anticipation 1 mois -> cible mois 12 (décembre)
        // Échéances : 3 et 9 -> pas d'échéance en décembre
        PeriodicExpenseDueInput.PeriodicExpenseLine line = new PeriodicExpenseDueInput.PeriodicExpenseLine(
                "c2", "Taxe foncière", new BigDecimal("1200.00"), List.of(3, 9));

        PeriodicExpenseDueInput input = new PeriodicExpenseDueInput(List.of(line), 11, 1);
        List<NotificationMessage> messages = rule.check(input);

        assertThat(messages).isEmpty();
    }

    @Test
    void supportsYearWrapFromDecemberToJanuary() {
        // Mois courant : 12 (décembre), anticipation 1 mois -> cible mois 1 (janvier)
        PeriodicExpenseDueInput.PeriodicExpenseLine line = new PeriodicExpenseDueInput.PeriodicExpenseLine(
                "c3", "Cotisation club", new BigDecimal("150.00"), List.of(1));

        PeriodicExpenseDueInput input = new PeriodicExpenseDueInput(List.of(line), 12, 1);
        List<NotificationMessage> messages = rule.check(input);

        assertThat(messages).hasSize(1);
        assertThat(messages.get(0).body()).contains("janvier");
    }
}
