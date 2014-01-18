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

import java.net.*;
import java.io.*;
/**
 * Created by Axel on 18/01/2014.
 */
public class Reine {

    private int[][] damier_local;
    private int numero;
    private String test;


    public Reine(int i,int taille ){
        this.numero=i;
        System.out.println("I am The Queen n°"+i);
        test = "Youhou";
        damier_local=new int[taille][taille];
    }

    private void broadcast_UDP() throws IOException {
        byte[] envoitampon = new byte[1024];
        byte[] rectampon = new byte[1024];
        String req,rep;
        req="Je suis"+identifiant();

        envoitampon = req.getBytes();
        for(int i =0;i<getTaille();i++){

            if(i!=numero){
                DatagramPacket envoipacket= new DatagramPacket(envoitampon,envoitampon.length, InetAddress.getLocalHost(),6100+i);

                // crée un socket UDP
                DatagramSocket socket=new DatagramSocket();

                // envoi le datagramme UDP
                socket.send(envoipacket);

            }
           /* //crée l’objet qui stockera les données du datagramme réponse
            DatagramPacket recpacket = new DatagramPacket(rectampon,rectampon.length);

            // reception d'un datagramme UDP
            socket.receive(recpacket);

            rep= new String(recpacket.getData());//*/
        }



    }

    private void demanderAutorisation() throws Exception{

        Socket socket = new Socket("localhost", 6000);   //Contacte le Damier sur le port local 6000
        PrintWriter sortie = new PrintWriter(socket.getOutputStream(),true);
        BufferedReader entree = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        sortie.println("Ici "+identifiant()+" demande autorisation");
        String rep=entree.readLine();//Attente de la réponse


    }






    public class EcouteUDP extends Thread {
        private boolean stopThread = false;
        private void ecoute() throws IOException {
            byte[] envoitampon = new byte[1024];
            byte[]rectampon=new byte[1024];
            String req,rep;

            //socket UDP qui attend des datagrammes sur port
            int port=6100+numero;

            DatagramSocket socket =new DatagramSocket(port);
            while(true){
                // crée un objet pour stocker les données du datagramme attendu
                DatagramPacket receptionpacket = new DatagramPacket(rectampon,rectampon.length);
                socket.receive(receptionpacket);
                req=new String(receptionpacket.getData());


                // traiter req pour fournir rep
                rep =identifiant()+" a reçu "+req;

                System.out.println(rep);

                /*envoitampon=rep.getBytes();
                 DatagramPacket reponsepacket =new DatagramPacket
                        (envoitampon,envoitampon.length, InetAddress.getLocalHost(),port);
                socket.send(reponsepacket);//*/

            }
        }
        @Override
        public void run() {
            boolean fin = false;

            while( !fin ) {

                    try{
                        while(true)
                            ecoute();
                    }catch (IOException e){}

                    synchronized(this) {
                        Thread.yield();

                        // lecture du boolean
                        fin = this.stopThread;
                    }

            }
        }
        public synchronized void quit() {
            this.stopThread = true;
        }
    }

    public void turnUDPOn() throws InterruptedException {
        EcouteUDP listener = new EcouteUDP();
        listener.start();


    }

    public void lancement() throws IOException {
        // 1) La reine demande a bouger
        // 2) Si autorisée, bouge puis broadcast ses infos
        // 3) Ecoute en permanence en UDP



        broadcast_UDP();
        try {

            demanderAutorisation();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }




    //GETTERS
    private int getTaille(){
        return this.damier_local.length;
    }

    private String identifiant(){
        return "Reine "+this.numero;
    }
}
