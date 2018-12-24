### Introduction

Projet constitué :

* d'un crawler en Java qui permet de récupérer tous les spells des montres des 5 bestiaires présents sur le site http://legacy.aonprd.com/
* d'un cluster Spark pour effectuer des traitements sur cette grande quantité de données

### Fonctionnement

* la classe `src/Main.java` run le crawler, et génère un fichier json de tous les spells
* la classe `src/Main.scala` effectue tous les traitements Spark sur ce jeux de données