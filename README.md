# METROID

<!-- PROJECT LOGO -->
<br />
<p align="center">
  <a href="https://github.com/rmonier/metroid">
    <img src="metroid/src/main/resources/img/logo.png" alt="Logo" width="130">
  </a>

<h3 align="center"><a href="https://github.com/rmonier/metroid"><img src="metroid/src/main/resources/img/backgrounds/titre.png" alt="metroid" width="380"></a></h3>

  <p align="center">
    Jeu inspiré de l'univers de Metroid
    <br />
    <a href="https://github.com/rmonier/metroid/releases"><strong>Voir les Releases »</strong></a>
    <br />
    <br />
    <a href="https://github.com/rmonier/metroid/wiki">Lire le wiki</a>
    ·
    <a href="https://github.com/rmonier/metroid/issues">Issues</a>
    ·
    <a href="https://github.com/rmonier/metroid/projects">Voir le Projet</a>
  </p>

<!-- TABLE OF CONTENTS -->
<details open="open">
  <summary>Table des Matières</summary>
  <ol>
    <li>
      <a href="#à-propos-du-projet">À Propos du Projet</a>
      <ul>
        <li><a href="#créé-avec">Créé avec</a></li>
      </ul>
    </li>
    <li>
      <a href="#commencer">Commencer</a>
      <ul>
        <li><a href="#prérequis">Prérequis</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#utilisation">Utilisation</a></li>
      <ul>
        <li><a href="#exécution">Exécution</a></li>
        <li><a href="#génération">Génération</a></li>
        <ul>
           <li><a href="#documentation">Documentation</a></li>
           <li><a href="#archive-jar">Archive JAR</a></li>
           <li><a href="#exécutable-optionnel">Exécutable (optionnel)</a></li>
        </ul>
      </ul>
    <li><a href="#arborescence">Arborescence</a></li>
    <li><a href="#crédits">Crédits</a></li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>

***

<!-- ABOUT THE PROJECT -->
## À Propos du Projet
Jeu réalisé en 2016/2017 avec Quentin Kaiffer et Marin Navarro pour le projet final de Java en 1ère année INSA Lyon. Ajout en 2021 sur GitHub avec un README et une transformation en projet Gradle avec gestion automatique des dépendances.

**Compatible Windows et Linux.** Les binaires macOS ne sont pas distribués par sdljava ainsi le projet n'installera pas les dépendances pour cet OS. Pour construire et exécuter METROID sur une machine de ce type, il faudra reconstruire les librairies depuis les sources de sdljava et télécharger les dépendances SDL pour macOS. Les instructions sont trouvables sur le site de sdljava.

### Créé avec
* [Java OpenJDK](https://openjdk.java.net/)
* [sdljava](http://sdljava.sourceforge.net/)

<!-- GETTING STARTED -->
## Commencer

### Prérequis

* Installer la version 17 32 bits de l'OpenJDK d'Azul (Zulu) disponible à cette adresse : https://www.azul.com/downloads/?version=java-17-lts&architecture=x86-32-bit&package=jdk

> :warning: **Utilisation sous Linux** : Afin d'extraire automatiquement les dépendances il faut installer `rpm2cpio` et `cpio` :
> ```sh
> sudo apt-get install -y rpm2cpio
> sudo apt-get install -y cpio
> ```

> :warning: **Pour pouvoir créer un exécutable localement (optionnel)** : D'autres programmes devront être installés manuellement selon l'OS cible (voir <a href="#exécutable-optionnel">Exécutable (optionnel)</a>)



### Installation

1. Cloner le dépôt sur votre machine
   ```sh
   git clone https://github.com/rmonier/metroid.git
   ```
2. METROID est maintenant prêt à être utilisé.

<!-- USAGE EXAMPLES -->
## Utilisation

### Exécution

Utiliser le script suivant à la racine du projet pour lancer METROID :
  ```sh
  gradlew run
  ```

### Génération

Pour générer la Javadoc et l'archive JAR, lancer le script suivant :
  ```sh
  gradlew build
  ```

#### Documentation

Lors du build METROID utilise un script Gradle pour générer la Javadoc qui peut se retrouver à l'emplacement suivant : `metroid/build/docs`.
Le script peut être lancé séparemment du build :
  ```sh
  gradlew javadoc
  ```

#### Archive JAR

Est aussi utilisé un script Gradle lors du build pour générer l'archive JAR qui peut se retrouver à l'emplacement suivant : `metroid/build/libs`.
Le script peut être lancé séparemment du build :
  ```sh
  gradlew jar
  ```
Cette archive est indépendante et peut être distribuée sur tout système qui utilise un Java OpenJDK 17 Zulu 32 bits sur l'OS correspondant en utilisant la commande suivante :
  ```sh
  java -jar metroid-1.0.1-windows.jar
  java -jar metroid-1.0.1-linux.jar
  ```

#### Exécutable (optionnel)

Enfin un dernier script Gradle peut être utilisé après le build pour créer un exécutable compatible avec l'OS utilisé (à l'aide de l'outil `jpackage`) que l'on retrouvera à cet emplacement : `metroid/build/dist`.
Le script à lancer est le suivant :
  ```sh
  gradlew jpackage
  ```

> :warning: **Pour mener à bien cette étape il faut posséder les programmes suivants selon l'OS utilisé** :
> * Linux: deb, rpm:
>    * For Red Hat Linux, the rpm-build package is required.
>    * For Ubuntu Linux, the fakeroot package is required.
> * macOS: pkg, app in a dmg
>    * Xcode command line tools are required when the --mac-sign option is used to request that the package be signed, and when the --icon option is used to customize the DMG image.
> * Windows: exe, msi
>    * WiX 3.0 or later is required.

***

<!-- TREE STRUCTURE -->
## Arborescence
<details>

_TODO_

</details>

<!-- CREDITS -->
## Crédits

Romain Monier [ [GitHub](https://github.com/rmonier) ] – Co-développeur
<br>
Quentin Kaiffer – Co-développeur
<br>
Marin Navarro – Co-développeur

<!-- CONTACT -->
## Contact

Lien du Projet : [https://github.com/rmonier/metroid](https://github.com/rmonier/metroid)