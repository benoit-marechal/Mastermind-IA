> Destiné aux débutants et à but pédagogique, j'ai réalisé ce projet il y a plusieurs années à mes débuts en informatique. Il est donc loin de respecter toutes les règles de l'art en développement et en programmation. Je l'ai conçu avec l'aide de Marc Rastoix. N'hésitez pas à corriger, compléter, modifier, ou d'ajouter vos remarques et bonnes pratiques soit par l'intermédiaire de Pull requests ou d'issues, c'est aussi pour ça que je le partage avec la communauté Github 💪 😀.


# Exécutables

Le dossier "Executables" contient le fichier .jar du projet. Pour le lancer,
il suffit de cliquer sur :
- "lancez-moi-windows.bat", si vous êtes sous Windows.
- "lancez-moi-linux.sh", si vous êtes sous Linux.
- Directement sur le fichier "IHMMastermind.jar", si vous êtes sous Mac.

Remarque : La machine virtuelle JAVA (JVM) doit être installée sur votre machine, et le chemin vers
l'executable java, doit faire partie de vos variables d'environnement.

# Sources

Le dossier contient les classes Java du projet.
Le sous dossier "Projet Netbeans" est le projet réaliser sous netbeans.


Les 3 stratégies sont implémentées dans le fichier ThreadStrategie,
par les méthodes :
strategie1(...)
strategie2(...)
strategie3(...)

Nous avons implémenté ces méthodes de la manière la plus statique possible, afin de faciliter
leur portage vers d'autres langages non orienté objet ainsi que leur parallélisation.

Ces méthodes sont donc complètement indépendantes de toutes variables de classe, cependant pour
pouvoir les lancer en parallèles nous avons dû les encapsuler dans un Thread et enlever
leur mot clé "static".

# Documentation

Le dossier "Documentation" contient les fichiers PDF constituant le rapport et les annexes.
Le sous-dossier "Sources LateX" contient les fichiers .tex nécessaire à la compilation en pdf.


# Screenshot
![alt text](screenshot.png?raw=true "screenshot 1")
