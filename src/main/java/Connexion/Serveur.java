package Connexion;

public class Serveur implements Runnable {
    private static int PORT = 8952;

    public Serveur() {
        //TODO création du serveur
    }

    public void run() {
        //TODO création des sockets à partir du serveur, et lancement
    }

    private class ServeurSocket implements Runnable {
        /**
         Chaque thread possède un numéro de capteur, pour qu'il puisse toujours s'occuper du même capteur quelque soit le message qu'il reçoit, mais c'est un peu bugué, on a des soucis quand on deoc un capteur puis qu'on le reco.
         */
        private int numCapteur;

        public ServeurSocket(int numCapteur) {
            this.numCapteur = numCapteur;
        }

        public void run() {
            //TODO Récupération des messages et traitement...
        }
    }
}
