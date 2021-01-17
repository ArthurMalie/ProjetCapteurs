package Connexion;

import Interface.Interface;

import java.io.*;
import java.net.*;

public class Serveur implements Runnable {

    private Interface ui;

    private int port;
    Socket socket;
    ServerSocket server;

    public Serveur(Interface ui, int port) {
        this.ui = ui;

        try {
            server = new ServerSocket(port);
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
