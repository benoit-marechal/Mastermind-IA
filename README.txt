-----------------
--- EXECUTABLES
-----------------

Le dossier "Executables" contient le fichier .jar du projet. Pour le lancer,
il suffit de cliquer sur : 
- "lancez-moi-windows.bat", si vous etes sous Windows.
- "lancez-moi-linux.sh", si vous etes sous Linux.
- Directement sur le fichier "IHMMastermind.jar", si vous etes sous Mac.

Remarque : La machine virtuelle JAVA (JVM) doit être installé sur votre machine, et le chemin vers
l'executable java, doit faire partie de vos variables d'environnement.

---------------
--- SOURCES
---------------

Le dossier contient les classes java du projet. 
Le sous dossier "Projet Netbeans" est le projet réaliser sous netbeans.


Les 3 stratégies sont implémentées dans le fichier ThreadStrategie,
par les méthodes : 
strategie1(...) 
strategie2(...) 
strategie3(...) 

Nous avons implémenter ces méthodes de la manière la plus statique possible, afin de facilité
leur portage vers d'autre langage non orienté objet ainsi que leur parallèlisation.

Ses méthodes sont donc complétement indépendante de toute variable de classe, cependant pour 
pouvoir les lancer en parallèles nous avons dû les encapsuler dans un Thread et enlever 
leur mot clé "static".

-------------
--- RAPPORT
-------------

Le dossier "Rapport" contient les fichiers PDF constituant le rapport et les annexes.
Le sous-dossier "Sources LateX" contient les fichiers .tex nécessaire à la compilation en pdf.