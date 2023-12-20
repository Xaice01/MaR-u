# Projet MaRéu

Ce dépot contient l'application MaRéu qui permet afficher une liste de réunions, d'ajouter des réunion, supprimer des réunion et de filtrer la liste de réunion en fonction de la date ou/et du lieu

## Capture

![Capture d'écran List Acceuil](https://github.com/Xaice01/MaR-u/assets/103635912/0c9d945c-ee54-4e46-82f9-b750fb41d925) ![Capture d'écranAcceuilMenu](https://github.com/Xaice01/MaR-u/assets/103635912/ce663a01-4cd4-4cb3-8d71-64e5ae93fe81)

![Capture d'écran CreateViewEmpty](https://github.com/Xaice01/MaR-u/assets/103635912/3e971756-57e4-46f6-80bc-f04976145fe6) ![Capture d'écran DatepickerCreateView](https://github.com/Xaice01/MaR-u/assets/103635912/8ddb82f5-a02a-4093-9974-11b95acc9fc6) ![Capture d'écran CreateView complet](https://github.com/Xaice01/MaR-u/assets/103635912/e60b6e64-b4c5-4eb9-904f-b63079862e9c)


## Prérequis
Pour installer l'application il faut avoir un téléphone sous Android 
API minimun 21

## Design Pattern
Disign pattern utilisé dans cette application :
- injection de dépendances (dans le ReunionViewModel)
- Repository
- Architecture Model View ViewModel
- Adapter (ReunionListAdapter)
- Observer (LiveData)

## Fonctionnalité
les fonctionnalité de cette application sont :
- lister les réunions
- filtrer les réunions en fonction de la date
- filtrer les réunions en fonction du lieu
- ajouter une réunions
- vérification de la disponibilité des salles en fonction de la durée de la réunion à ajouter et des réunions déja existante
- supprimer une réunions


## Licence
Les icons dans la page de création de réunion sont sous licence Apache 2.0

## Contribution 
l'auteur de l'application est [Xavier](https://github.com/Xaice01)
pour l'entreprise Lamzone
