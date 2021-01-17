package Connexion;

import Interface.Interface;

import java.io.*;
import java.net.*;

public class Serveur implements Runnable {

    private Interface ui;

    private static final int PORT = 8952;
    Socket socket;
    ServerSocket server;

    public Serveur(Interface ui) {
        this.ui = ui;

        try {
            server = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            while (!server.isClosed()) {
                socket = server.accept();
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            BufferedReader plec = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            boolean socketOuvert = true;
                            while (socketOuvert) {
                                try {
                                    String input = plec.readLine();
                                    if (input != null) {
                                        ui.newMessage(input);
                                    }
                                } catch (SocketException se) {
                                    socketOuvert = false;
                                }
                            }
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
