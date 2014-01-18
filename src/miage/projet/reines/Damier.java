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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
public class Damier extends Thread{
    private int taille_damier;
    ServerSocket s;

    public Damier(int taille_damier) throws IOException {
        this.taille_damier=taille_damier;
        s = new ServerSocket(6000);// Assignation du socket d'écoute
        //Boucle d'écoute des requêtes.


    }
    public class ThreadDamier extends Thread{
        private String delims = "[;]";
        private Socket port;


        public void run() {

            try{


                BufferedReader entree = new BufferedReader(new InputStreamReader( this.port.getInputStream()));
                PrintWriter sortie = new PrintWriter(this.port.getOutputStream(),true);
                //System.out.println("Bienvenue sur le Serveur " +this.getName());

                String str=entree.readLine(); //Attente d'un message
                System.out.println("Serveur a reçu " +str);
                String[] data=str.split(delims);

                entree.close();
                sortie.close();

            }catch(IOException e){}
            finally{
                try{
                    port.close();
                }catch(IOException e){}

            }

        }

        public ThreadDamier(Socket port){
            this.port=port;

        }

    }
    public void run()  {

        while (true){
            Socket soc = null;
            try {
                soc = s.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // crée un nouveau thread pour le nouveau client
            ThreadDamier th = new ThreadDamier(soc);
            //lance l'execution du thread
            th.start();
    }


}
    //SETTERS

    //GETTERS
    public int get_taille_damier(){
        return this.taille_damier;
    }
}
