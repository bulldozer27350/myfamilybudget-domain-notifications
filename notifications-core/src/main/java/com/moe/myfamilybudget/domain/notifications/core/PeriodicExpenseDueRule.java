package com.moe.myfamilybudget.domain.notifications.core;

import java.util.ArrayList;
import java.util.List;

import com.moe.myfamilybudget.domain.notifications.calculation.PeriodicExpenseDueInput;
import com.moe.myfamilybudget.domain.notifications.model.NotificationMessage;
import com.moe.myfamilybudget.domain.notifications.model.NotificationRule;
import com.moe.myfamilybudget.domain.notifications.model.NotificationRuleKeys;

/**
 * Signale une dépense périodique qui arrive à échéance dans les mois à venir (M+1 ou M+2) (PROV-09).
 */
public class PeriodicExpenseDueRule implements NotificationRule<PeriodicExpenseDueInput> {

    public static final String KEY = NotificationRuleKeys.PERIODIC_EXPENSE_DUE;

    private static final String[] MONTH_NAMES = {
        "", "janvier", "février", "mars", "avril", "mai", "juin",
        "juillet", "août", "septembre", "octobre", "novembre", "décembre"
    };

    @Override
    public String key() {
        return KEY;
    }

    @Override
    public List<NotificationMessage> check(PeriodicExpenseDueInput input) {
        if (input == null || input.lines() == null || input.lines().isEmpty()) {
            return List.of();
        }
        int currentMonth = input.currentMonth();
        int monthsAhead = input.monthsAhead();
        List<NotificationMessage> messages = new ArrayList<>();

        for (PeriodicExpenseDueInput.PeriodicExpenseLine line : input.lines()) {
            if (line.dueMonths() == null || line.dueMonths().isEmpty()) {
                continue;
            }
            for (int delta = 1; delta <= monthsAhead; delta++) {
                int targetMonth = ((currentMonth - 1 + delta) % 12) + 1;
                if (line.dueMonths().contains(targetMonth)) {
                    String monthName = (targetMonth >= 1 && targetMonth <= 12) ? MONTH_NAMES[targetMonth] : String.valueOf(targetMonth);
                    String title = "Échéance périodique à venir : " + line.label();
                    String body = String.format("La charge « %s » arrive à échéance en %s (%s € attendus).",
                            line.label(), monthName, line.amountPerOccurrence().toPlainString());
                    messages.add(new NotificationMessage(KEY, line.id() + ":" + targetMonth, title, body));
                    break;
                }
            }
        }
        return messages;
    }
}
