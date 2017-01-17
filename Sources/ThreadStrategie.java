import javax.swing.JTextArea;
import java.util.*;
/*
 * ThreadStrategie.java
 *
 * Created on 24 novembre 2006, 23:01
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author Fildz
 */
public class ThreadStrategie extends Thread {
    
    private iMastermind imm ;
    
    private int n;
    private int k;
    private boolean repetition;
    private int strategie ;
    
    private ArrayList combis; /* Liste de combinaison à trouver */
    private int[] combi; /* combinaison courante */
    
    private JTextArea status; // text area ou on affiche le status de la strategie
    private double nbTentativesMoyen ;
    private long nbTentativeMax;
    private long sommeTemps;
    
    private boolean affichage ;
    
    private JTextArea status2;
    
    public ThreadStrategie(iMastermind imm, int n, int k, boolean repetition, int strategie, ArrayList combis, boolean affichage) {
        this.imm = imm;
        this.n = n;
        this.k = k;
        this.repetition = repetition;
        this.strategie = strategie;
        
        this.combis = combis;
        
        this.affichage = affichage ;
        
        this.status = imm.getTextArea(strategie);
        this.status2 = imm.getTextArea2(strategie);
        
        
        
    }
    
    public void run() {
        
        Chrono chrono = new Chrono();
        long sommeTentative = 0;
        nbTentativeMax = 0;
        
        status.setText("          *** Stratégie "+strategie+" ***\n\n");
        status2.setText("");
        
        for(int i = 0; i<combis.size(); i++) {
            combi =(int[]) combis.get(i);
            
            if(affichage) {
                status.setText(status.getText() + "combinaison à trouver : ");
                afficheTab(combi);
                status.setText(status.getText() + "\n");
            }
            
            int nbTentatives=0 ;
            long temps=0;
            
            switch(strategie) {
                case 1 :
                    chrono.start();
                    nbTentatives = strategie1(combi,n,k);
                    chrono.stop();
                    temps = chrono.getMilliSec();
                    
                    break;
                case 2 :
                    chrono.start();
                    nbTentatives = strategie2(combi,n,k,repetition);
                    chrono.stop();
                    temps = chrono.getMilliSec();
                    break;
                case 3 :
                    chrono.start();
                    nbTentatives = strategie3(combi,n,k);
                    chrono.stop();
                    temps = chrono.getMilliSec();
                    break;
                case 4 :
                    //strategie4(combi,n,k);
                    break;
            }
            if (nbTentatives > nbTentativeMax)
                nbTentativeMax = nbTentatives;
            
            
            sommeTentative += nbTentatives;
            sommeTemps += temps;
            
            if (affichage) {
            status.setText(status.getText() + "[ "+nbTentatives + " tentatives ]\n");
            status.setText(status.getText() + "[ "+temps + " ms ]\n\n");
            }
        }
        
        nbTentativesMoyen = (double) sommeTentative / (double) combis.size() ;
        
        status2.setText( "Nb tentatives moyen : "+ nbTentativesMoyen+"\n");
        status2.setText( status2.getText() +"Nb tentatives maximum : "+ nbTentativeMax+"\n");
        status2.setText(status2.getText() + "Temps d'execution : "+ sommeTemps+"\n");
        
    }
    
    public void setCombi(int[] combi) {
        this.combi = combi ;
    }
    /**
     * Cette fonction implémente la stratégie 1. Il s'agit d'un algorithme
     * déterministe, très simpliste.
     *
     * @param combi
     *            Tableau representant la combinaison a trouvé
     * @param n
     *            Nombre de couleurs
     * @param k
     *            Nombre de cases de la combi
     *
     * @return Nombre de tentatives effectuée
     */
    public int strategie1(int[] combi, int n, int k) {
                /*
                 * Initialisations
                 */
        
        
        int coul = 1; // Couleur testé, on commence avec la 1ier couleur : 1
        
        int[] combiTente = new int[k]; // Tableau contenant la combinaison
        // tenté par l'ia
        int[] combiTrouve = new int[k]; // Tableau contenant les couleurs
        // trouvées,
        int nbBienPlace = 0;
        int nbMalPlace = 0;
        int nbTrouve = 0; // Nombre de boules de couleurs dont ont a trouvé la
        // position
        int nbTentative = 0;
        int pos = 0;
        
                /*
                 * Boucle générant les tentatives jusqu'à la résolution du MM
                 */
        
        // tant que le nb de bien placé est inférieur aux nb de cases (ie: tant
        // que le MM n'est pas résolu) faire
        while (nbBienPlace(combi, combiTente, k) < k /* && coul <= n */) {
            
            // On crée la nouvelle combinaison à tenté, qui determine la
            // presence ou non d'une couleur. Tout en tenant compte des
            // positions des
            // boules de couleurs deja trouvées
            for (int i = 0; i < k; i++) {
                if (combiTrouve[i] == 0)
                    combiTente[i] = coul;
                else
                    combiTente[i] = combiTrouve[i];
            }
            
            nbTentative++;
            
            // On détermine le nombre de bien placés et mal placés
            nbBienPlace = nbBienPlace(combi, combiTente, k);
            nbMalPlace = nbCommuns(combi, combiTente, k)
            - nbBienPlace(combi, combiTente, k);
            
            //afficheTab(combiTente);
            //System.out.println(" --> "+nbBienPlace+" biens placées, "+nbMalPlace+" mal placées.");
            if(affichage)
                afficheTentative(combiTente,nbBienPlace,nbMalPlace);
            
            int nbBoules = nbBienPlace - nbTrouve;
            
            // Si la couleur tester est présente (ie. nbBienPlace-nbTrouve >= 1)
            // faire
            if (nbBoules >= 1 && coul <= (n + 1)) {
                
                for (int x = 1; x <= nbBoules; x++) {
                    // On met nbMalPlace à un nombre différent de 0
                    nbMalPlace = 1;
                    
                    // indice de la position testé pour trouver l'emplacement de
                    // la couleur coul. On ne test pas une position dont on
                    // connais deja la
                    // position. Donc on crée une boucle qui cherche une
                    // position possible.
                    pos = 0;
                    
                    // tant que nbMalPlace != 0 faire
                    while (nbMalPlace > 0) {
                        while ((pos < k) && combiTrouve[pos] != 0)
                            pos++;
                        // On crée la nouvelle combinaison à tenté, qui cherche
                        // la position exacte de la boule de couleur
                        // en cour. Tout en tenant compte des positions des
                        // boules de couleurs deja trouvées
                        for (int i = 0; i < k; i++) {
                            if (combiTrouve[i] == 0) // Si la case courante,
                                // n'est pas une case dont on connais la couleur
                                // (ie. combiTrouve==0)
                            {
                                if (i != pos) // Si la case n'est pas la case
                                    // testé, on met la boule de
                                    // couleur supérieur
                                    combiTente[i] = coul + 1;
                                else
                                    combiTente[i] = coul;
                                
                            } else
                                combiTente[i] = combiTrouve[i];
                        }
                        
                        // Test de la nouvelle tentative
                        // Calcul du nombre de boule mal placé (en souhaitant
                        // qu'il y en ai 0)
                        nbTentative++;
                        nbBienPlace =nbBienPlace(combi, combiTente, k);
                        nbMalPlace = nbCommuns(combi, combiTente, k)
                        - nbBienPlace;
                        
                        // Affichage de la tentative ***
                        //afficheTab(combiTente);
                        //System.out.println(" --> "+nbBienPlace+" biens placées, "+nbMalPlace+" mal placées.");
                        if(affichage)
                            afficheTentative(combiTente,nbBienPlace,nbMalPlace);
                        // on se prépare à tester la position suivante
                        pos++;
                        
                    }
                    
                    // A la sortie de la boucle, on a la position de la boule de
                    // couleur -> pos - 1
                    // On ajoute donc cette boule à la combinaison contenant les
                    // boules Trouvées
                    combiTrouve[pos - 1] = coul;
                    
                    // on incrémente le nombre de boule trouvées
                    nbTrouve++;
                }
            }
            coul++;
        }
        return nbTentative;
    }
    
    
    
    
    /**
     * Cette fonction implémente les critère 1, 2, 3 et 4. Il s'agit d'un algorithme
     * déterministe, optimisé notamment en nombre de tentatives effectuée.
     *
     * @param combi
     *            Tableau representant la combinaison a trouvé
     * @param n
     *            Nombre de couleurs
     * @param k
     *            Nombre de cases de la combi
     *
     * @return Nombre de tentatives effectuée
     */
    public int strategie3(int[] combi, int n, int k) {
                /*
                 * Initialisations
                 */
        
        int coul = 1; // Couleur testé, on commence avec la 1ier couleur : 1
        
        int[] combiTente = new int[k]; // Tableau contenant la combinaison
        // tenté par l'ia
        int[] combiTrouve = new int[k]; // Tableau contenant les couleurs
        // trouvées,
        int nbBienPlace = 0;
        int nbMalPlace = 0;
        int nbTrouve = 0; // Nombre de boules de couleurs dont ont a trouvé la
        // position
        int nbTentative = 0;
        int pos = 0;
        
        int coulSuivante = 1 ; // Contient la couleur suivante servant à remplir
        // la combinaison lorsqu'on cherche la position de la couleur courante
        
        int nbBoulesSuivante = 0;
        
                /*
                 * Boucle générant les tentatives jusqu'à la résolution du MM
                 */
        
        // tant que le nb de bien placé est inférieur aux nb de cases (ie: tant
        // que le MM n'est pas résolu) faire *** utiliser la variable nbBienPlace ***
        while (nbBienPlace(combi, combiTente, k) < k  && coul <= n ) {
            
            coul = coulSuivante;
            // On crée la nouvelle combinaison à tenté, qui determine la
            // presence ou non d'une couleur. Tout en tenant compte des
            // positions des
            // boules de couleurs deja trouvées
            for (int i = 0; i < k; i++) {
                if (combiTrouve[i] == 0)
                    combiTente[i] = coul;
                else
                    combiTente[i] = combiTrouve[i];
            }
            
            // On vérifie que la combinaison a tenté n'est pas celle deja tenté dernierement)
            
            
            // On détermine le nombre de bien placés et mal placés
            nbBienPlace = nbBienPlace(combi, combiTente, k);
            nbMalPlace = nbCommuns(combi, combiTente, k)
            - nbBienPlace(combi, combiTente, k);
            
            // incrémentation du nombre de combi tenté
            nbTentative++;
            
            // affichage de la combi tenté et des indications données
            //System.out.print("cherche couleur : ");
            //afficheTab(combiTente);
            //System.out.println(" --> "+nbBienPlace+" biens placées, "+nbMalPlace+" mal placées.");
            
            if(affichage)
                afficheTentative(combiTente,nbBienPlace,nbMalPlace);
            
                                /*
                                 * Analyse des indications données par le nbBienPlace et nbMalPlace
                                 */
            
            // 1 - Si le nombre de bien placé = k, pas la peine de continué on a fini la résolution
            if (nbBienPlace == k)
                break;
            
            
            // Détermination du nombre de boules de la couleur courante
            int nbBoules = nbBienPlace - nbTrouve;
            
            
            // Si la couleur tester est présente (ie. nbBoules >= 1)
            // faire
            if (nbBoules >= 1 && coul <= (n + 1)) {
                
                do {
                    
                    if (nbBoulesSuivante>0) // si on a cour circuite la recherche de couleur
                    {
                        //System.out.println("Court circuit");
                        coul = coulSuivante;
                        nbBoules = nbBoulesSuivante;
                    }
                    nbBoulesSuivante = 0; // init du nb de boules de la couleur suivante
                    
                    coulSuivante = coul + 1;
                    
                    for (int x = 1; x <= nbBoules; x++) { // cherche la position de chaque couleurs
                        // On met nbMalPlace à un nombre différent de 0
                        nbMalPlace = 1;
                        
                        // indice de la position testé pour trouver l'emplacement de
                        // la couleur coul. On ne test pas une position dont on
                        // connais deja la
                        // couleur. Donc on crée une boucle qui cherche une
                        // position possible.
                        pos = 0;
                        
                        // tant que nbMalPlace != 0 faire
                        while (nbMalPlace > 0) {
                            
                            // recherche d'une position a tester qui n'a pas deja été trouvé
                            while ((pos < k) && combiTrouve[pos] != 0)
                                pos++;
                            
                            // On crée la nouvelle combinaison à tenté, qui cherche
                            // la position exacte de la boule de couleur
                            // en cour. Tout en tenant compte des positions des
                            // boules de couleurs deja trouvées
                            for (int i = 0; i < k; i++) {
                                
                                if (combiTrouve[i] == 0) // Si la case courante,
                                    // n'est pas une case dont on connais la couleur
                                    // (ie. combiTrouve==0)
                                {
                                    if (i != pos) // Si la case n'est pas la case
                                        // testé, on met la boule de
                                        // couleur supérieur
                                        combiTente[i] = coulSuivante;
                                    else
                                        combiTente[i] = coul;
                                    
                                } else
                                    combiTente[i] = combiTrouve[i];
                            }
                            
                            // Test de la nouvelle tentative
                            // Calcul du nombre de boules mal placé (en souhaitant
                            // qu'il y en ai 0)
                            nbBienPlace = nbBienPlace(combi, combiTente, k);
                            int nbCommuns = nbCommuns(combi, combiTente, k);
                            nbMalPlace = nbCommuns - nbBienPlace;
                            
                            // Affichage de la tentative ***
                            //afficheTab(combiTente);
                            //System.out.println(" --> "+nbBienPlace+" biens placées, "+nbMalPlace+" mal placées.");
                            
                            if(affichage)
                                afficheTentative(combiTente,nbBienPlace,nbMalPlace);
                            
                            nbTentative++;
                            
                                                        /*
                                                         * Analyse des indications données
                                                         */
                            int nbCoulSuivante = (nbCommuns - nbTrouve - 1);
                            //System.out.println("coulSuivante : "+ coulSuivante+ " nbCoulSuivante : "+nbCoulSuivante);
                            if ( nbCoulSuivante <= 0) {
                                coulSuivante++;
                                //	System.out.println(coulSuivante);
                            } else
                                nbBoulesSuivante = nbCoulSuivante;
                            
                            pos++;
                        }
                        
                        // A la sortie de la boucle, on a la position de la boule de
                        // couleur -> pos - 1
                        // On ajoute donc cette boule à la combinaison contenant les
                        // boules Trouvées
                        combiTrouve[pos - 1] = coul;
                        // on incrémente le nombre de boule trouvées
                        nbTrouve++;
                    }
                    
                }
                while(nbBoulesSuivante >0 && nbBienPlace != k); // Tant que la couleur suivante est prensente
            } else
                coulSuivante++;
            
        }
        // Renvoi du nombre de tentatives effectuées
        return nbTentative;
    }
    
    
    public int strategie2(int[] combi, int n, int k, boolean repetition) {
		/*
		 * Initialisations
		 */

		int bienplace=0;        //Nombre de pions bien placés
		int malplace=0;         //Nombre de pions mal placés
		int nbtentative=0;	//Nombre de combinaisons tentées
		int i,j,l;
		int temp=-1;
                int drap=-1;
                int nbi=-1;
		int nombredejatrouve=0;
		int[] combitemp = new int[k];
		int[] combitrouve = new int[k]; //Combinaison finale
		int[] combitente = new int[k];  //Combinaison proposée au programme
		//Initialisation des tableaux
		for (i=0;i<k;i++){
			combitemp[i]=-1;
			combitrouve[i]=-1;
		}
		// Pour chaque couleur
		for (i=1;i<=n;i++){
                        //Si la solution n'est pas encore trouvee
			if(bienplace!=k){
                                //On affecte à tous les pions pas encore trouves a la couleur courante i
				for(j=0;j<k;j++){
					if (combitrouve[j]==-1) combitente[j]=i;
				}
                                //On cherche le nombre de pions biens et mals places
				bienplace = nbBienPlace(combi, combitente, k);
				malplace = nbCommuns(combi, combitente, k)- nbBienPlace(combi, combitente, k);
                                //Cela fait une tentative de plus
				nbtentative++;
                                if(affichage)
                                  afficheTentative(combitente,bienplace,malplace);
                                //afficheTab(combitente);
                                //Nombre d'occurence de la couleur i en cours
                                if(bienplace==k)
                                    nbi=0;
                                else
                                    nbi=bienplace-nombredejatrouve;
                                //Tant que tous les pions de la couleur en cours i ne sont pas bien placés
				while(nbi>0){
					temp=-1;
					drap=-1;
					for(l=0;l<k;l++) combitemp[l]=combitrouve[l];
                                        //Tant que le pion courant de couleur courante i est mal placé
					do{
                                                //Si on a déjà teste une position non valable
						if(temp!=-1){
                                                        //On passe à la couleur suivante pour le pion temp
							combitente[temp]=i+1;
                                                        combitemp[temp]=-1;
                                                        //On baisse le drapeau
							drap=-1;
						}
						for(j=(temp+1);j<k;j++){
                                                        //Si le pion courant a deja ete trouve
							if (combitrouve[j]!=-1){
                                                                //On le copie dans la combinaison a la bonne position
								combitente[j]=combitrouve[j];
							}
                                                        //Sinon si le drapeau n'est pas mis et donc que la couleur n'a pas deja ete affectee
     							else if (drap==-1){
                                                                //On tente la couleur courante
								combitente[j]=i;
								combitemp[j]=i;
								temp=j;
                                                                //On lève le drapeau
								drap=0;
							}
                                                        //Sinon on passe à la couleur suivante pour le pion courant
							else{
								combitente[j]=i+1;
							}
						}
                                                //On cherche le nombre de pions biens et mals places
						bienplace = nbBienPlace(combi, combitemp, k);
						malplace = nbCommuns(combi, combitemp, k)- nbBienPlace(combi, combitemp, k);
                                                //Cela fait une tentative de plus
						nbtentative++;
                                                if(affichage)
                                                afficheTentative(combitente,bienplace,malplace);
                                                //afficheTab(combitemp);
                                        }while ((malplace!=0));
					for (j=0;j<k;j++){
						combitrouve[j]=combitemp[j];
					}
                                        //Un pion de plus trouve
					nombredejatrouve++;
                                        //Un pion de moins de couleur i a placer
                                        nbi--;
				}
			}
		}
                //afficheTab(combitrouve);
		return nbtentative;//on retourne le nombre de tentatives
	}
    
    
    
    
    
    /**
     * Nombre de boules de couleurs communs entre deux tableaux (utilise la
     * valeur 0 pour les boules deja comptÈes.)
     *
     * @param tab1
     *            Tableau contenant une combinaison
     * @param tab2
     *            Tableau contenant une seconde combinaison
     * @param k
     *            Nomre de cases dans les combinaisons (=tab1.length, mais c'est
     *            plus claire de passÈ ce parametre)
     *
     * @return Renvoie le nombre de boule en communs entre deux combinaisons
     */
    public int nbCommuns(int[] tab1, int[] tab2, int k) {
        // CrÈation de tableaux avec une taille equivalent aux nombres de
        // couleurs
        int[] t1 = new int[k];
        int[] t2 = new int[k];
        for (int i = 0; i < k; i++) {
            t1[i] = tab1[i];
            t2[i] = tab2[i];
        }
        
        int cpt = 0;
        boolean trouve = false;
        
        for (int i = 0; i < k; i++) {
            int j = 0;
            trouve = false;
            do {
                if (t1[i] == t2[j]) {
                    cpt++;
                    t2[j] = 0;
                    trouve = true;
                }
                j++;
            } while (j < k && !trouve);
        }
        
        return cpt;
    }
    
    /**
     * @param tab1
     *            Tableau contenant une combinaison
     * @param tab2
     *            Tableau contenant une seconde combinaison
     * @param k
     *            Nomre de cases dans les combinaisons (=tab1.length, mais c'est
     *            plus claire de passÈ ce parametre)
     *
     * @return Renvoie le nombre de boule bien placÈ entre deux combinaisons
     */
    public int nbBienPlace(int[] tab1, int[] tab2, int k) {
        int nb_bien_place = 0;
        for (int i = 0; i < k; i++) {
            if (tab1[i] == tab2[i]) {
                nb_bien_place++;
            }
        }
        return nb_bien_place;
    }
    
    /**
     * Permet l'affichage d'un tableau
     *
     * @param tab
     *            tableau à afficher
     */
    public void afficheTab(int[] tab) {
        for (int i = 0; i < tab.length; i++) {
            //System.out.print(tab[i] + " ");
            status.setText(status.getText() + tab[i] + " " );
        }
        //System.out.println();
        
    }
    
    public void afficheTentative(int tab[],int nbBienPlace,int nbMalPlace) {
        for (int i = 0; i < tab.length; i++) {
            
            status.setText(status.getText() + tab[i] + " ");
        }
        status.setText(status.getText() + " --> "+nbBienPlace+" noirs, "+nbMalPlace+" blancs.\n");
        
        
    }
    
}


/**
 * Permet de mesure le temps d'un algorithme. Usage :
 *
 * Chrono chrono = new Chrono();
 * chrono.start();
 * chrono.stop();
 *
 *  // renvoi le temps en ms
 *  long temps = chrono.getMilliSec();
 *
 *
 * @author Benoit Maréchal
 */
class Chrono {
    long m_start;
    
    long m_stop;
    
    Chrono() {
    }
    
    // Lance le chronomètre
    public void start() {
        m_start = System.currentTimeMillis();
    }
    
    // Arrète le chronomètre
    public void stop() {
        m_stop = System.currentTimeMillis();
    }
    
    // Retourne le nombre de millisecondes séparant l'appel des méthode start()
    // et stop()
    public long getMilliSec() {
        return (m_stop - m_start);
    }
    
}

