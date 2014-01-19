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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Reine {

    private boolean en_marche=false;
    private int[] damier_local;
    private int position;//Remarque : il s'agit de la colonne
    private int numero; //numero est aussi le numero de la ligne
    private int port;
    private String group;
    private MulticastSocket s;//*/
    private String delims = "[;]";
    //private int conflits;

    public Reine(int i,int taille ) throws IOException {
        this.numero=i;
        System.out.println("I am The Queen n°"+i);

        damier_local=new int[taille];

        // Which port should we listen to
        port = 5000;
        // Which address
        group = "225.4.5.6";
        // Create the socket and bind it to port 'port'.
        s = new MulticastSocket(port);
        // join the multicast group
        s.joinGroup(InetAddress.getByName(group));//*/
    }
    public void plateau(){
        System.out.println("\n\tPlateau de jeu\n");
        for (int i=0; i<getTaille();i++){
            System.out.print(i + "\t");
            for (int j=0; j<getTaille();j++){
                if(j==damier_local[i]){
                    System.out.print("R ");
                }else{
                    System.out.print("_ ");
                }

            }
            System.out.println();

        }
        System.out.println();
    }

    private void MiseAJourPosReine(int nReine,int p){
        damier_local[nReine]=p;

    }
    private void traitement(String mess) throws IOException {
        String[] data;
        data=mess.split(delims);
        //System.out.println("Traitement:"+mess);
        if(data[1].equals("OK")){
            en_marche=true;

        }else{

            if(data[1].equals("STOP")){
                en_marche=false;

            }
            else{//*/


                if((data[0].equals("Reine"))&&(Integer.parseInt(data[1])!=numero)){
                    //System.out.println("Reine "+numero+" Message recu de la reine "+data[1]);
                    int num_reine=Integer.parseInt(data[1]);
                    int pos=Integer.parseInt(data[3]);
                    MiseAJourPosReine(num_reine,pos);
                }
            }
        }

    }
    private void broadcast_UDP_Multicast() throws IOException {
        // Which port should we send to

        // Which ttl
        int ttl = 1;
        // Create the socket but we don't bind it as we are only going to send data
        MulticastSocket s = new MulticastSocket();//*/
        // Note that we don't have to join the multicast group if we are only
        // sending data and not receiving
        // Fill the buffer with some data

        byte[] buf = new byte[1024];
        String req=identifiant()+numero+";"+position+";";//
        buf=req.getBytes();

        //for (int i=0; i<buf.length; i++) buf[i] = (byte)i;
        // Create a DatagramPacket
        DatagramPacket pack = new DatagramPacket(buf, buf.length, InetAddress.getByName(group), port);

        // Do a send. Note that send takes a byte for the ttl and not an int.
        s.send(pack,(byte)ttl);
        //System.out.println("req:"+req);

        //ttl = s.getTimeToLive(); s.setTimeToLive(ttl); s.send(pack); s.setTimeToLive(ttl);

        // And when we have finished sending data close the socket
        //s.close();
    }


    private boolean demanderAutorisation() throws Exception{
        boolean auto=false;
        Socket socket = new Socket("localhost", 6000);   //Contacte le Damier sur le port local 6000
        PrintWriter sortie = new PrintWriter(socket.getOutputStream(),true);
        BufferedReader entree = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        sortie.println(identifiant()+"auto");
        String rep=entree.readLine();//Attente de la réponse

        if(rep.equals("Autorisé")){
            //System.out.println(rep);
            auto=true;
        }
        return auto;
    }

    private void envoiNouvellePosition() throws Exception{
        boolean auto=false;
        Socket socket = new Socket("localhost", 6000);   //Contacte le Damier sur le port local 6000
        PrintWriter sortie = new PrintWriter(socket.getOutputStream(),true);
        BufferedReader entree = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        sortie.println(identifiant()+"autorise;"+position);

        String rep=entree.readLine();//Attente de la réponse



    }






    public class EcouteUDP extends Thread {
        private boolean stopThread = false;


        private void ecouteMultiCast() throws IOException {
            // Import some needed classes
            // Now the socket is set up and we are ready to receive packets
            // Create a DatagramPacket and do a receive

            byte[] buf = new byte[1024];


            DatagramPacket pack = new DatagramPacket(buf, buf.length);

            s.receive(pack);
            // Finally, let us do something useful with the data we just received,
            // like print it on stdout :-)
            // System.out.println("Received data from: " + pack.getAddress().toString() +  ":" + pack.getPort() + " with length: " + pack.getLength());
            // System.out.write(pack.getData(),0,pack.getLength());
            String req;

            req=new String(pack.getData());

            //System.out.println(identifiant()+" a reçu "+req);
            traitement(req);
            // And when we have finished receiving data leave the multicast group and
            // close the socket
            // s.leaveGroup(InetAddress.getByName(group));
            //s.close();
        }
        @Override
        public void run() {
            boolean fin = false;

            while( !fin ) {

                    try{
                        while(true)
                           // ecoute();
                            ecouteMultiCast();
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
    /*public static void main(String[] args) {
        Reine reine=new Reine(args[0]);
    }//*/
    public int conflitsHautD(){
        int res=0;

        for (int i=1;i<getTaille();i++){
            int ligne_test=position+i;

            int col_test=numero-i;
            if((ligne_test<getTaille())&&(col_test>=0)){

                if(damier_local[col_test]==ligne_test){
                    res++;
                }
            }else{
                return res;
            }
        }
        return res;
    }
    public int conflitsBasG(){
        int res=0;

        for (int i=1;i<getTaille();i++){
            int ligne_test=position-i;

            int col_test=numero+i;
            if((ligne_test>=0)&&(col_test<getTaille())){

                if(damier_local[col_test]==ligne_test){
                    res++;
                }
            }else{
                return res;
            }
        }
        return res;
    }
    public int conflitsBasD(){
        int res=0;

        for (int i=1;i<getTaille();i++){
            int ligne_test=position+i;

            int col_test=numero+i;
            if((ligne_test<getTaille())&&(col_test<getTaille())){

                if(damier_local[col_test]==ligne_test){
                    res++;
                }
            }else{
                return res;
            }
        }
        return res;
    }
    public int conflitsHautG(){
        int res=0;

        for (int i=1;i<getTaille();i++){
            int ligne_test=position-i;

            int col_test=numero-i;
            if((ligne_test>=0)&&(col_test>=0)){

                if(damier_local[col_test]==ligne_test){
                    res++;
                }
            }else{
                return res;
            }
        }
        return res;
    }

    public int getConflits(){
        int res=0;

        for(int i=0;i<getTaille();i++){
            if(i!=numero){
                if(damier_local[i]==position){
                    res++;
                }
            }
        }
        res=res+conflitsBasD()+conflitsHautG()+conflitsHautD()+conflitsBasG();//+conflitsBasG()+conflitsBasD()+conflitsHautG()+conflitsHautD();



        return res;
    }


    private void trouverMeilleurePos(){
        int conflits=getConflits();

        int meilleur_pos=position;
        //int min_conflits=conflits;


        for(int i=0;i<getTaille();i++){
            position=(position+1)%getTaille();

            int tmp=getConflits();

            if(tmp<conflits){
                conflits=tmp;
                meilleur_pos = position;
            }
        }
        position=meilleur_pos;
        damier_local[numero]=position;
    }

    public void lancement() throws IOException {
        // 1) La reine demande a bouger
        // 2) Si autorisée, bouge puis broadcast ses infos
        // 3) Ecoute en permanence en UDP
        int k=0;
       while(k<30){
            // broadcast_UDP();
            broadcast_UDP_Multicast();
            try {
                synchronized ((Object)position){
                    if(demanderAutorisation()){
                        //System.out.println(identifiant() + position);

                        trouverMeilleurePos();

                        //System.out.println(identifiant()+position);
                        envoiNouvellePosition();

                        broadcast_UDP_Multicast();


                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
           k++;
       }

        //System.out.println("Reine : "+numero+" conflits" + getConflits());

    }



    //GETTERS
    private int getTaille(){
        return this.damier_local.length;
    }

    private String identifiant(){
        return "Reine;"+this.numero+";";
    }
}
