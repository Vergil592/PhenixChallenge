# Phenix Challenge

Les détails du Phenix Challenge ssont dans le Github (Readme.md)

https://github.com/Carrefour-Group/phenix-challenge


## Méthodologie

L'ensemble des fichiers crées sont stockées dans le dossier results.

La première étape consiste à récuperer l'ensemble des magasins
à partir des fichiers `reference_prod-<MAGASIN>_<YYYYMMDD>.data`.

Ensuite, à partir du fichiers `transactions_<YYYYMMDD>.data`,
on créer un fichier `transaction-<MAGASIN>_<YYYYMMDD>.data`
qui comporte les transactions effectuée par magasin.
Ces fichiers ne contiennent que le numéro du produit et 
la quantité du produit vendue.
Ils sont stockés dans le dossier produits_par_magasin.
A cette étape, existe des doublons des produits au sein d'un même fichier.


Pour ce faire, on calcule le nombre total de vente du jour pour un magasin
pour chaque produits. Ces valeurs (numero_produit|nb_ventes) sont
stockées dans des ficheirs au format `<MAGASIN>-prod_<YYYYMMDD>.data` dans 
le dossier produits_par_magasin. (dossier paramétrable via le fichier application.conf situé dans /src/resources)

Une fois les fichiers `<MAGASIN>-prod_<YYYYMMDD>.data` produits,
le programme calcul alors le top 100 des ventes pour chaque magasins 
présent dans le fichier de transaction initial et le top 100 des ventes global.
Ces resulstats sont sockés dans un dossier nommé top100_ventes.

### Outils utilisés

* [Apache Maven 3.3.9](https://maven.apache.org/index.html) - Permet le build et le packaging le l'application
* [Java 1.8.0_201](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) - Permet de lancer le .jar
* [Scala 2.11.8](https://www.scala-lang.org/download/2.11.8.html) - Code produit en Scala

* [IntelliJ 18 3.4](https://www.jetbrains.com/idea/download/#section=windows) - IDE qui à permit le développement de l'application


### Installation des dépendances

Pour lancer le .jar, il est necessaire d'instaler Java :

* [Java 1.8.0_201](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) - Permet l'execution du .jar


### Lancer le projet

Les données présentent sur le Github doivent étre placées dans le dossier 'data' qui est à la racine du projet

Se placer à la racine du projet (projet-phenix) et executer la commande suivante
 afin d'obtenir les top 100 des ventes par magasins et global :

```
java -jar \target\Project-phenix-1.0-SNAPSHOT-jar-with-dependencies.jar <YYYYMMDD>
```

Où \<YYYYMMDD\> est la date à laquelle on souhaite obtenir les top 100 des ventes par magasin
et global.
Par défaut, la date correspond à la date des ficheirs présents dans le GitHub (20170514)

Une fois le programme terminé, les résultats sont stockés dans le dossier
results qui est à la racien du projet.
Le nom du dossier est un paramètres du fichier application.conf (situé dans src/resources/)

### Build le projet

Le projet se build à partir de Maven (ou via intelliJ en utilisant maven également)

Pour se faire, se placer à la racine du projet
 et exécuter les lignes suivantes
 
Pour clean le projet :
```
mvn clean
```
Puis pour build le projet :
```
mvn package
```
## Documentation

La documentation Scala est disponible dans le fichier documentation
à la racine du projet.

### TODO

Choses qu'il reste à effectuer dans le code :

```
Terminer les tests de la classe VentesMagasins
```
```
Gérer les erreurs lors de la saisie 
d'une date incorrect lors de l'execution du .jar
```

### Remarques

L'aplication permet d'obtenir les fichiers 
`top_100_ventes_<ID_MAGASIN>_YYYYMMDD.data`  et
`top_100_ventes_GLOBAL_YYYYMMDD.data`
 avec 512M de mémoire
dédié à la JVM pour un fichier de transaction initial allant
jusqu'à 12 millions de lignes.
Dépassement des 512M de mémoire autour de 13-15 millions de lignes
de transactions.



## Auteur

* **Aymeric Van Mael**
