import java.util.ArrayList;
/*
 * Mastermind.java
 *
 * Created on 24 novembre 2006, 00:07
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author Fildz
 */
public class Mastermind {
    
   // Variable de classe
	
	// Definition des regle du mastermind
	private int n; // Nombre de couleurs
	private int k; // Nombre de cases
	private boolean repetition; // Repetition autoris ou non
        
        private boolean strategie1;
        private boolean strategie2;
        private boolean strategie3;
        private boolean strategie4;
        
        private boolean affichage ; // permet de ne pas afficher le detail
        
        private int nbCombi;
        private int[] combi;
	
        private iMastermind imaster ; // reference vers l'interface graphique
	
	
	public Mastermind(int n,int k, boolean repetition,iMastermind imaster)
	{

		this.n = n;
		this.k = k;
		this.repetition = repetition;
                this.imaster = imaster;
	
                
	}
        
        
        public void setN(int n){
            this.n = n;
        }
        public void setK(int k){
            this.k = k;
        }
        public void setRepetition(boolean repetition){
            this.repetition = repetition;
        }
        public void setStrategie1(boolean s){
            this.strategie1 = s;
        }
        public void setStrategie2(boolean s){
            this.strategie2 = s;
        }
        public void setStrategie3(boolean s){
            this.strategie3 = s;
        }
        public void setStrategie4(boolean s){
            this.strategie4 = s;
        }
        public void setCombi(int[] combi)
        {
            this.combi = combi;
            
        }
        
        public void setNbCombi(int n)
        {
            this.nbCombi = n;
        }
        
        
        public void setAffichage(boolean a)
        {
            affichage = a;
        }
        
        /**
         * Renvoi une chaine contenant les calculs selon les rgles dfinie du
         * MM
         */
        public String calcul() {
            
            String text ;
            text = "Nombre de combinaisons : ";
            
            text += String.valueOf(calculerNbCombinaisons(n,k,repetition));
            
            text +="\nNombre de tentative thorique dans le pire des cas : ";
            
            text += String.valueOf(calculerNbTentativesTheorique(n,k,repetition));
            
            return text;
            
        }
        
        /**
         *
         */
        public void resoudre() throws Exception
        { 
            ArrayList list = new ArrayList();
            
            if (nbCombi > 0) {
                for(int i =0;i<nbCombi;i++)
                {
                    
                    list.add(genererCombinaison(n,k,repetition));
                }
                    
            }
                
            else
                list.add(combi);
             
              
                ThreadStrategie tst1 = new ThreadStrategie(imaster,n,k,repetition,1,list,affichage);
                ThreadStrategie tst2 = new ThreadStrategie(imaster,n,k,repetition,2,list,affichage);
                ThreadStrategie tst3 = new ThreadStrategie(imaster,n,k,repetition,3,list,affichage);
                ThreadStrategie tst4 = new ThreadStrategie(imaster,n,k,repetition,4,list,affichage);
                       
                    if (strategie1)
                        tst1.start();
                    
                    if (strategie2)
                        tst2.start();
                    
                    if (strategie3)
                        tst3.start();
                    
                    if (strategie4)
                        tst4.start();
                
               
                    
        }
        
        
        
	
	
	/**
	 * Génère une combinaison aléatoire sous forme de tableau
	 * 
	 * @param n
	 *            Nombre de couleurs (nombre compris entre 1 et n)
	 * @param k
	 *            Nombre de cases de la combinaison (taille du tableau)
	 * @param repetition
	 *            Booléen indiquant si les répétitions sont autorisées ou non
	 * @return Tableau d'entier, représentant une combinaison
	 */
	public int[] genererCombinaison(int n, int k, boolean repetition) {
		int[] tab = new int[k];

		// Si les répetition sont autorisées
		if (repetition) {
			for (int i = 0; i < k; i++)
				tab[i] = (int) Math.floor(Math.random() * n + 1);
		} else { // Si les répétitions ne sont pas autorisées
			int coul;
			boolean rep = false;
			for (int i = 0; i < k; i++) {
				do { // Boucle générant une couleur qui n'a pas deja ete
						// tiré.
					rep = false;
					coul = (int) Math.floor(Math.random() * n + 1);
					for (int j = 0; j < i; j++)
						if (tab[j] == coul)
							rep = true;
				} while (rep);
				tab[i] = coul;
			}
		}
		return tab;
	}

        
	/**
	 * Permet de calculer le nombre de combinaison possible, selon les regles
	 * choisies du mastermind.
	 * 
	 * @param n
	 *            Nombre de boules de couleur
	 * @param k
	 *            Nombre de cases
	 * @param repetition
	 *            true s'il y a des répétition dans la combinaison, false sinon
	 * @return Un double contenant le nombre de combinaison possible
	 */
	public long calculerNbCombinaisons(int n, int k, boolean repetition) {
		if (repetition) { // Arrangement avec repetition formule : n^k
			return (long) Math.ceil(Math.pow(n, k));
		} else { // Arrangement sans repetition formule : A(n,k) = n! / (n -
					// k)!
			return (long) Math.ceil(((double) facto(n) / (double) facto(n - k)));
		}
	}


	
	/**
	 * Calcul le nombre d'indication possible (1 bien placé ,2 mal placé etc...) 
	 * @param k
	 * @return 
	 */
	public long calculerNbIndications(int k)
	{

		return (long) Math.floor(((double) facto (3+k-1) / (double) (facto(k) * 2)) - 1) ;
	}
	
	

	/**
	 * Permet de calculer le nombre de combinaison possible, selon les regles
	 * choisies du mastermind.
	 * 
	 * @param n
	 *            Nombre de boules de couleur
	 * @param k
	 *            Nombre de cases
	 * @param repetition
	 *            true s'il y a des répétition dans la combinaison, false sinon
	 * @return Un double contenant le nombre de combinaison possible
	 */
	public int calculerNbTentativesTheorique(int n, int k, boolean repetition) {
		// calcul du nombre de combinaisons possible
		double nbCombinaisons = calculerNbCombinaisons(n, k, repetition);
		double nbIndications = calculerNbIndications(k);
		
		// calcul du nombre d'indications a chaque tentatives
		// Attention ce nombre est le nombre d'indication maximal, en pratique
		// ce nombre varie. En effet par exemple lorsqu'on tente une combinaison
		// avec seulement la meme couleur partt, ce nombre d'indications passe à 'k'
		// car il n'y peut pas y avoir de boules 'mal placé' d'indiqué.
		// Par consequent, le nombre de tentatives 'dans le pire des cas' donné ici,
		// est un nombre idéaliste, que tous les meilleurs stratège
		// du mastermind tente d'approcher.
		
		// R(3,k) = C(3+k-1)(k) - 1 
		double logCombi =  Math.log10( nbCombinaisons) / Math.log10(2);
		double logInd =  Math.log10(nbIndications) / Math.log10(2);		
		
		return (int) Math.ceil( logCombi / logInd);
		
	}
	
	


	/**
	 * Donne la factorielle d'un nombre Remarque : Au dela de n >= 14 je crois
	 * que ça déconne... Il faudrai programmer la fonction factorielle vu en Td
	 * pour palier ce probleme...
	 * 
	 * @param n
	 *            Nombre dont on veut la factorielle
	 * 
	 * @return 
	 * 			  Renvoi la factoriel du nombre donné en paramètre
	 */
	public long facto(int n) {
		if (n>0)
                    {	
                    return n * facto(n - 1);
               
                        
                    }
                else 
                    {
                     return 1;
                    }
	}
        
        
}

