# Release Notes — Silo Notifications (domain-notifications)

Toutes les versions publiées de ce dépôt respectent la spécification [SemVer 2.0.0](https://semver.org/lang/fr/).

---

## [0.0.1] - 2026-11-07

### Type de release
**Version initiale** (mise en place et validation de la structure autonome multi-dépôts)

### Résumé
Première version stable du silo Notifications (`domain-notifications`), regroupant `notifications-api` et `notifications-core` avec import du BOM centralisé, cycle de build autonome et configuration de déploiement GitHub Packages.

### Changements par composant
- **`notifications-api`** : contrats, paramètres, entrées de règles (aucun changement métier).
- **`notifications-core`** : règles pures, dispatch, paramètres, canal Web Push, et persistance JPA des paramètres, du journal d'envoi et des abonnements.

### Rétro-compatibilité & Compatibilité binaire
Non applicable : version initiale, aucune version antérieure publiée.
