package com.moe.myfamilybudget.domain.notifications.calculation;

import java.math.BigDecimal;
import java.util.List;

/**
 * Entrée minimale de la règle {@code PeriodicExpenseDueRule} (PROV-09).
 *
 * @param lines       lignes de charges périodiques actives
 * @param currentMonth mois courant (1 à 12)
 * @param monthsAhead nombre de mois d'anticipation pour l'alerte (généralement 1 ou 2)
 */
public record PeriodicExpenseDueInput(
        List<PeriodicExpenseLine> lines,
        int currentMonth,
        int monthsAhead
) {
    public PeriodicExpenseDueInput {
        lines = lines != null ? List.copyOf(lines) : List.of();
        if (currentMonth < 1 || currentMonth > 12) currentMonth = 1;
        if (monthsAhead < 1) monthsAhead = 1;
    }

    public record PeriodicExpenseLine(
            String id,
            String label,
            BigDecimal amountPerOccurrence,
            List<Integer> dueMonths
    ) {
        public PeriodicExpenseLine {
            if (id == null) id = "";
            if (label == null) label = "";
            if (amountPerOccurrence == null) amountPerOccurrence = BigDecimal.ZERO;
            dueMonths = dueMonths != null ? List.copyOf(dueMonths) : List.of();
        }
    }
}
