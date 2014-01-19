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
import java.net.*;


public class Damier extends Thread{
    private int taille_damier;
    private ServerSocket s;
    private String delims = "[;]";
    private boolean[] jeton;
    private int[] plateau;

   public void afficher(){
       System.out.println("\n\tPlateau de jeu\n");
       for (int i=0; i<taille_damier;i++){
           System.out.print(i+"\t");
           for (int j=0; j<taille_damier;j++){
               if(j==plateau[i]){
                   System.out.print("R");
               }else{
                   System.out.print("_");
               }

           }
           System.out.println();

       }
       System.out.println();
   }

    private void envoyerAuxReines(String message) throws IOException {
        // Which port should we send to
        int port = 5000;
        // Which address
        String group = "225.4.5.6";
        // Which ttl
        int ttl = 1;
        // Create the socket but we don't bind it as we are only going to send data
        MulticastSocket s = new MulticastSocket();
        // Note that we don't have to join the multicast group if we are only
        // sending data and not receiving
        // Fill the buffer with some data
        byte[] buf = new byte[1024];
        String req="Server;"+message+";";
        buf=req.getBytes();
        //for (int i=0; i<buf.length; i++) buf[i] = (byte)i;
        // Create a DatagramPacket
        DatagramPacket pack = new DatagramPacket(buf, buf.length, InetAddress.getByName(group), port);
        // Do a send. Note that send takes a byte for the ttl and not an int.
        s.send(pack,(byte)ttl);
        // And when we have finished sending data close the socket
        s.close();
    }

    private void init_jeton(int t){
        jeton[0]=true;
        for(int i=1;i<t;i++){
            jeton[i]=false;
        }
    }
    public Damier(int taille_damier) throws IOException {
        this.taille_damier=taille_damier;
        jeton=new boolean[taille_damier];
        init_jeton(taille_damier);
        plateau=new int[taille_damier];
        s = new ServerSocket(6000);// Assignation du socket d'écoute
        //Boucle d'écoute des requêtes.



    }

    public class Messager extends Thread{



        public void run() {
            try {
                envoyerAuxReines("OK");
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        public Messager(){


        }

    }

    private String traitement(String mess){
        String[] data=mess.split(delims);
        String reponse="";
        if(data[2].equals("auto")){


            int a = Integer.parseInt(data[1]);

            if (jeton[a]){
                reponse="Autorisé";
            }else{
                reponse="Non Autorisé";
            }
            System.out.println("La " + data[0] + " N°" + data[1] + " demande une autorisation de bouger:" + reponse);
        }


        if(data[2].equals("autorise")){
            System.out.println("Mi");
            int num_reine=Integer.parseInt(data[1]);
            int p=Integer.parseInt(data[3]);
            System.out.println("Mise à jour de la pos de la "+data[0]+" N°"+data[1]+" en cours.");
            System.out.println("La "+data[0]+" N°"+data[1]+" passe de "+plateau[num_reine]+" à "+p);


            MiseAJourPosReine(num_reine,p);
        }
        return reponse;
    }
    private void MiseAJourPosReine(int nR,int p){
        plateau[nR]=p;
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
                //System.out.println("Serveur a reçu : " +str);
                String reponseServeur=traitement(str);
                sortie.println(reponseServeur);


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

        Messager facteur=new Messager();
        facteur.start();

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
