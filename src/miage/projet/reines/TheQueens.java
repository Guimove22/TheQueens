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

    private static void init_damier(int i) throws IOException {
        Damier damier;
        damier = new Damier(i);
        damier.start();
    }

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
        /*for(int i=0;i<taille;i++){
            LesReines.get(i).lancement();
        }//*/
    }
    public static void main(String[] args) throws IOException, InterruptedException {

        setTaille(2);
        init_damier(taille);

        init_reines(taille);

        lancer_reines();
    }

    //SETTERS
    private static void setTaille(int i){
        taille=i;
    }
}
