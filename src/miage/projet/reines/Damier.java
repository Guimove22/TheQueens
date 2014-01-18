/*

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
import java.net.ServerSocket;
import java.net.Socket;
public class Damier {
    private int taille_damier;

    public Damier(int taille_damier) throws IOException {
        this.taille_damier=taille_damier;




            ServerSocket s = new ServerSocket(6000);// Assignation du socket d'écoute

            //Boucle d'écoute des requêtes.
            while(true){

                Socket soc=s.accept();
                ThreadDamier th= new ThreadDamier(soc);
                th.start();
            }
        }




    //SETTERS

    //GETTERS
    public int get_taille_damier(){
        return this.taille_damier;
    }
}
