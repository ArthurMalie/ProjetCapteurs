import Interface.Interface;
import Connexion.Connexion;

import javax.swing.*;

public class Application {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Interface();
            }
        });

    }
}
