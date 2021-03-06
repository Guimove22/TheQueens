/*
Created by Axel on 18/01/2014. :)
╔══════════════╦════════════════════════════════════════════════════════════╗
║  ( (         ║						2013-2014							║
║    ) )	   ║				Université Dauphine Paris 9					║
║  ........	   ║					Master 1 - MIAGE						║
║  |      |]   ║			       Projet Java Avancé			            ║
║  \      /    ╟────────────────────────────────────────────────────────────╢
║   `----'     ║	Axel Richier - Thibault Schleret - Guillaume Fronczak   ║
╚══════════════╩════════════════════════════════════════════════════════════╝

*/
package miage.projet.reines;

import java.io.IOException;
import java.util.ArrayList;


public class TheQueens {

    private static int taille;
    private static ArrayList<Reine> LesReines  = new ArrayList<Reine>();



    private static void init_reines(int taille) throws IOException {
        for(int i=0;i<taille;i++){
            LesReines.add(i,new Reine(i,taille));
        }
    }

    private static void lancer_reines() throws IOException, InterruptedException {
        for(int i=0;i<taille;i++){
            LesReines.get(i).turnUDPOn();
        }
        Thread.sleep(1000);
        for(int i=0;i<taille;i++){
            LesReines.get(i).lancement();
        }//*/
    }
    public static void main(String[] args) throws IOException, InterruptedException {

        setTaille(6);
        Damier damier;
        damier = new Damier(taille);
        damier.start();

        init_reines(taille);
        damier.afficher();
        // afficherPlateauReine(0);

        lancer_reines();
        Thread.sleep(4000);
        damier.afficher();

        afficherEnsembleConflits();
       /* afficherPlateauReine(0);
        afficherPlateauReine(1);
        afficherPlateauReine(2);
        afficherPlateauReine(3);
        afficherPlateauReine(4);
        afficherPlateauReine(5);
        afficherPlateauReine(6);
        afficherPlateauReine(7);//*/


    }

    private static void afficherEnsembleConflits(){
        for(int i=0;i<taille;i++)
            System.out.println(i + ":" + LesReines.get(i).getConflits());
    }

    private static void afficherPlateauReine(int k){
            LesReines.get(k).plateau();
    }
    //SETTERS
    private static void setTaille(int i){
        taille=i;
    }
}
