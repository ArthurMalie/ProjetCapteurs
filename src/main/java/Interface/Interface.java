package Interface;

import Connexion.Connexion;
import Mapping.Capteur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Interface {

    private Connexion connexion;

    private JFrame frame;

    private JDialog dialogPort;
    private JDialog dialogSeuils;

    private JButton btnValiderPort;
    private JButton btnAnnulerPort;
    private JButton btnFiltrer;
    private JButton btnValiderCourbes;
    private JButton btnModifierSeuils;
    private JButton btnValiderSeuils;
    private JButton btnAnnulerSeuils;

    private JCheckBox checkAirComprime1;
    private JCheckBox checkAirComprime2;
    private JCheckBox checkEau1;
    private JCheckBox checkEau2;
    private JCheckBox checkElectricite1;
    private JCheckBox checkElectricite2;
    private JCheckBox checkTemperature1;
    private JCheckBox checkTemperature2;

    private JList<String> listeBatiments;
    private JList<String> listeCapteurs2;

    private JTable tableauCapteurs;
    private JTable tableauInfosCapteur;
    private JTable tableauSeuilsDefaut;

    private JTextField textDateFin;
    private JTextField textDateDebut;
    private JTextField textPort;
    private JTextField textSeuilMin;
    private JTextField textSeuilMax;

    private JTree treeCapteurs;

    public Interface() {
        frame = new JFrame("Gestion des capteurs");
        initialisation();
    }

    private void initialisation() {



        /* Fenêtre principale - Gestion des capteurs */

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(600, 400));
        frame.setPreferredSize(new Dimension(800, 500));

        JTabbedPane onglets = new JTabbedPane();

        onglets.setBorder(BorderFactory.createTitledBorder(null, "NeoCampus", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new Font("Tahoma", 1, 14)));
        onglets.setPreferredSize(new Dimension(500, 450));



        /* Boite de dialogue pour le port */

        dialogPort = new JDialog();
        JPanel panelPort = new JPanel(new GridLayout(5, 1));
        JLabel labelConnexion = new JLabel("Connexion à l'interface de visualisation NeoCampus");
        JLabel labelPort = new JLabel("Veuillez renseigner le port sur lequel vous souhaitez vous connecter :");
        textPort = new JTextField("3306");
        btnAnnulerPort = new JButton("Quitter");
        btnValiderPort = new JButton("Valider");

        dialogPort.setResizable(false);
        dialogPort.setMinimumSize(new Dimension(450, 200));
        dialogPort.setLocationRelativeTo(null);
        dialogPort.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        labelConnexion.setFont(new Font("Tahoma", 1, 11));
        labelConnexion.setHorizontalAlignment(SwingConstants.CENTER);
        labelPort.setHorizontalAlignment(SwingConstants.CENTER);
        textPort.setHorizontalAlignment(JTextField.CENTER);

        btnAnnulerPort.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        btnValiderPort.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                etablirConnexion();
            }
        });

        panelPort.add(labelConnexion);
        panelPort.add(labelPort);
        panelPort.add(textPort);
        panelPort.add(btnAnnulerPort);
        panelPort.add(btnValiderPort);

        dialogPort.add(panelPort);

        dialogPort.setVisible(true);



        /* Boite de dialogue pour la modification des seuils */

        dialogSeuils = new JDialog();
        JPanel panelDialogSeuils = new JPanel(new GridLayout(3, 2));
        JLabel labelSeuilMin = new JLabel("Seuil minimum : ");
        JLabel labelSeuilMax = new JLabel("Seuil maximum : ");
        textSeuilMin = new JTextField();
        textSeuilMax = new JTextField();
        btnAnnulerSeuils = new JButton("Annuler");
        btnValiderSeuils = new JButton("Valider");

        dialogSeuils.setMinimumSize(new Dimension(250, 130));
        dialogSeuils.setLocationRelativeTo(null);
        dialogSeuils.setTitle("Modification des seuils");

        labelSeuilMin.setHorizontalAlignment(SwingConstants.TRAILING);
        labelSeuilMax.setHorizontalAlignment(SwingConstants.TRAILING);
        textSeuilMin.setHorizontalAlignment(SwingConstants.CENTER);
        textSeuilMax.setHorizontalAlignment(SwingConstants.CENTER);

        panelDialogSeuils.add(labelSeuilMin);
        panelDialogSeuils.add(textSeuilMin);
        panelDialogSeuils.add(labelSeuilMax);
        panelDialogSeuils.add(textSeuilMax);
        panelDialogSeuils.add(btnAnnulerSeuils);
        panelDialogSeuils.add(btnValiderSeuils);
        dialogSeuils.add(panelDialogSeuils);



        /* Onglet 1 - Visualisation en temps réel */

        JPanel onglet1 = new JPanel(new BorderLayout());
        JPanel panelFiltres1 = new JPanel(new BorderLayout());
        JPanel panelFluides1 = new JPanel(new GridLayout(4, 0));
        JScrollPane scrollPanelListeCapteurs1 = new JScrollPane();
        JScrollPane scrollPanelBatiments = new JScrollPane();
        tableauCapteurs = new JTable();
        listeBatiments = new JList<>();
        checkEau1 = new JCheckBox("Eau");
        checkElectricite1 = new JCheckBox("Electricite");
        checkAirComprime1 = new JCheckBox("Air comprime");
        checkTemperature1 = new JCheckBox("Temperature");
        btnFiltrer = new JButton("Filtrer");

        panelFiltres1.setBorder(BorderFactory.createTitledBorder("Filtres"));
        listeBatiments.setBorder(BorderFactory.createTitledBorder("Bâtiments"));

        scrollPanelListeCapteurs1.setViewportView(tableauCapteurs);
        scrollPanelBatiments.setViewportView(listeBatiments);

        btnFiltrer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshTableauCapteurs();
            }
        });
        ActionListener checkListener1 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshBatiments();
            }
        };
        checkEau1.addActionListener(checkListener1);
        checkTemperature1.addActionListener(checkListener1);
        checkElectricite1.addActionListener(checkListener1);
        checkAirComprime1.addActionListener(checkListener1);


        panelFluides1.add(checkEau1);
        panelFluides1.add(checkElectricite1);
        panelFluides1.add(checkAirComprime1);
        panelFluides1.add(checkTemperature1);
        panelFiltres1.add(panelFluides1, BorderLayout.PAGE_START);
        panelFiltres1.add(scrollPanelBatiments, BorderLayout.CENTER);
        panelFiltres1.add(btnFiltrer, BorderLayout.PAGE_END);
        onglet1.add(panelFiltres1, BorderLayout.LINE_END);
        onglet1.add(scrollPanelListeCapteurs1, BorderLayout.CENTER);

        onglets.addTab("Visualisation en temps reel", onglet1);



        /* Onglet 2 - Visualisation en temps différé */

        JPanel onglet2 = new JPanel(new BorderLayout());
        JPanel panelFiltres2 = new JPanel(new BorderLayout());
        JPanel panelFluides2 = new JPanel(new GridLayout(4, 0));
        JPanel panelDates = new JPanel(new BorderLayout());
        JPanel panelDates2 = new JPanel(new FlowLayout());
        JPanel panelCourbes = new JPanel(new GridLayout(3, 0));
        JPanel panelCourbe1 = new JPanel();
        JPanel panelCourbe2 = new JPanel();
        JPanel panelCourbe3 = new JPanel();
        JScrollPane scrollPanelListeCapteurs2 = new JScrollPane();
        JLabel labelDe = new JLabel("de : ");
        JLabel labelA = new JLabel(" a : ");
        listeCapteurs2 = new JList<>();
        checkEau2 = new JCheckBox("Eau");
        checkElectricite2 = new JCheckBox("Electricite");
        checkAirComprime2 = new JCheckBox("Air comprime");
        checkTemperature2 = new JCheckBox("Temperature");
        textDateDebut = new JTextField("01/01/2020");
        textDateFin = new JTextField("01/01/2022");
        btnValiderCourbes = new JButton("Valider");

        panelFiltres2.setBorder(BorderFactory.createTitledBorder("Filtres"));
        listeCapteurs2.setBorder(BorderFactory.createTitledBorder("Capteurs"));

        // REQUETE POUR GET TOUS LES NOMS DE CAPTEURS

        listeCapteurs2.setModel(new AbstractListModel<>() {
            String[] strings = {"Item 1", "Item 2", "Item 3", "Item 4", "Item 5"};

            public int getSize() {
                return strings.length;
            }

            public String getElementAt(int i) {
                return strings[i];
            }
        });

        panelFiltres2.add(scrollPanelListeCapteurs2, BorderLayout.CENTER);

        panelCourbe1.setBackground(new Color(204, 255, 204));
        panelCourbe2.setBackground(new Color(255, 255, 204));
        panelCourbe3.setBackground(new Color(255, 204, 255));

        scrollPanelListeCapteurs2.setViewportView(listeCapteurs2);

        btnValiderCourbes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayCourbesCapteurs();
            }
        });
        ActionListener checkListener2 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshListeCapteurs();
            }
        };
        checkEau2.addActionListener(checkListener2);
        checkTemperature2.addActionListener(checkListener2);
        checkElectricite2.addActionListener(checkListener2);
        checkAirComprime2.addActionListener(checkListener2);

        panelFluides2.add(checkEau2);
        panelFluides2.add(checkElectricite2);
        panelFluides2.add(checkAirComprime2);
        panelFluides2.add(checkTemperature2);
        panelDates2.add(labelDe);
        panelDates2.add(textDateDebut);
        panelDates2.add(labelA);
        panelDates2.add(textDateFin);
        panelDates.add(panelDates2, BorderLayout.CENTER);
        panelDates.add(btnValiderCourbes, BorderLayout.PAGE_END);
        panelFiltres2.add(panelFluides2, BorderLayout.PAGE_START);
        panelFiltres2.add(panelDates, BorderLayout.PAGE_END);
        panelCourbes.add(panelCourbe1);
        panelCourbes.add(panelCourbe2);
        panelCourbes.add(panelCourbe3);
        onglet2.add(panelFiltres2, BorderLayout.LINE_END);
        onglet2.add(panelCourbes, BorderLayout.CENTER);

        onglets.addTab("Visualisation en temps differe", onglet2);



        /* Onglet 3 - Gestion des capteurs */

        JPanel onglet3 = new JPanel(new BorderLayout());
        JPanel panelTableaux = new JPanel(new BorderLayout());
        JPanel panelInfosCapteur = new JPanel(new BorderLayout());
        JPanel panelSeuilsDefaut = new JPanel(new BorderLayout());
        JScrollPane scrollPanelInfosCapteur = new JScrollPane();
        JScrollPane scrollPanelSeuilsDefaut = new JScrollPane();
        JScrollPane scrollPanelTree = new JScrollPane();

        tableauInfosCapteur = new JTable();
        tableauSeuilsDefaut = new JTable();
        btnModifierSeuils = new JButton("Modifier les seuils");
        treeCapteurs = new JTree();

        panelSeuilsDefaut.setBorder(BorderFactory.createTitledBorder("Seuils par defaut"));
        panelInfosCapteur.setBorder(BorderFactory.createTitledBorder("Informations du capteur"));
        panelTableaux.setPreferredSize(new Dimension(300, 300));
        panelSeuilsDefaut.setPreferredSize(new Dimension(300, 120));

        tableauInfosCapteur.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {"Batiment", null},
                        {"Etage", null},
                        {"Type de fluide", null},
                        {"Seuil minimum", null},
                        {"Seuil maximum", null}
                },
                new String[]{"Informations", "Valeurs"}
        ) {
            boolean[] canEdit = new boolean[]{false, false};

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });

        tableauSeuilsDefaut.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {"EAU", 0, 10, "m3"},
                        {"ELECTRICITE", 10, 500, "kWh"},
                        {"TEMPERATURE", 17, 22, "°C"},
                        {"AIR COMPRIME", 0, 6, "m3/h"}
                },
                new String[]{"Fluide", "Seuil min", "Seuil max", "Unite"}
        ) {
            Class[] types = new Class[]{
                    Object.class, Integer.class, Integer.class, String.class
            };
            boolean[] canEdit = new boolean[]{false, false, false, false};

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });

        tableauSeuilsDefaut.getColumnModel().getColumn(0).setPreferredWidth(150);

        scrollPanelInfosCapteur.setViewportView(tableauInfosCapteur);
        scrollPanelSeuilsDefaut.setViewportView(tableauSeuilsDefaut);
        scrollPanelTree.setViewportView(treeCapteurs);

        btnModifierSeuils.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialogSeuils.setVisible(true);
            }
        });
        btnValiderSeuils.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editSeuils();
            }
        });
        btnAnnulerSeuils.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialogSeuils.setVisible(false);
            }
        });


        panelInfosCapteur.add(scrollPanelInfosCapteur, BorderLayout.CENTER);
        panelInfosCapteur.add(btnModifierSeuils, BorderLayout.PAGE_END);
        panelSeuilsDefaut.add(scrollPanelSeuilsDefaut, BorderLayout.CENTER);
        panelTableaux.add(panelInfosCapteur, BorderLayout.CENTER);
        panelTableaux.add(panelSeuilsDefaut, BorderLayout.SOUTH);
        onglet3.add(panelTableaux, BorderLayout.EAST);
        onglet3.add(scrollPanelTree, BorderLayout.CENTER);

        onglets.addTab("Gestion des capteurs", onglet3);

        frame.add(onglets);
        frame.setLocationRelativeTo(null);

        frame.pack();
    }

    private void refreshBatiments() {
        String[] batiments = connexion.getBatimentsFluides(checkAirComprime1.isSelected(), checkEau1.isSelected(), checkElectricite1.isSelected(), checkTemperature1.isSelected());
        listeBatiments.setModel(new AbstractListModel<>() {
            String[] strings = batiments;

            public int getSize() {
                return strings.length;
            }

            public String getElementAt(int i) {
                return strings[i];
            }
        });
    }

    private void refreshTableauCapteurs() {
        String[] batiments = listeBatiments.getSelectedValuesList().toArray(new String[0]);
        if( ( checkAirComprime1.isSelected() || checkEau1.isSelected() || checkElectricite1.isSelected() || checkTemperature1.isSelected() ) && batiments.length > 0) {
            Capteur[] capteurs = connexion.getAllCapteursFiltresOnglet1(checkAirComprime1.isSelected(), checkEau1.isSelected(), checkElectricite1.isSelected(), checkTemperature1.isSelected(), batiments);
            String[][] modele = new String[capteurs.length][6];
            for(int i = 0 ; i< capteurs.length ; i++){
                modele[i][0] = "Capteur " + capteurs[i].getId();
                modele[i][1] = capteurs[i].getFluide().getType_fluide();
                modele[i][2] = capteurs[i].getLieu().getBatiment();
                modele[i][3] = String.valueOf(capteurs[i].getLieu().getEtage());
                modele[i][4] = capteurs[i].getLieu().getNom();
                modele[i][5] = "0";
            }

            tableauCapteurs.setModel(new javax.swing.table.DefaultTableModel(
                    modele, new String[]{"Nom", "Type Fluide", "Batiment", "Etage", "Lieu", "Valeur"}
            ));
        }
        else {  // tableau vide
            tableauCapteurs.setModel(new javax.swing.table.DefaultTableModel(
                    new Object[][] {}, new String[]{"Nom", "Type Fluide", "Batiment", "Etage", "Lieu", "Valeur"}
            ));
        }
    }

    private void refreshListeCapteurs() {

        String[] capteurs = connexion.getAllCapteursFiltres(checkAirComprime2.isSelected(), checkEau2.isSelected(), checkElectricite2.isSelected(), checkTemperature2.isSelected());
        listeCapteurs2.setModel(new AbstractListModel<>() {
            String[] strings = capteurs;

            public int getSize() {
                return strings.length;
            }

            public String getElementAt(int i) {
                return strings[i];
            }
        });
    }

    private void displayCourbesCapteurs() {
        //List<String> capteurs = connexion.getCapteurs(checkAirComprime2.isSelected(), checkEau2.isSelected(), checkElectricite2.isSelected(), checkTemperature2.isSelected(), textDateDebut.getText(), textDateFin.getText());
        listeCapteurs2.setSelectionModel(new Interface.SelectionModelMax(listeCapteurs2, 3));
    }

    private void editSeuils() {
    }

    private void etablirConnexion() {
        connexion = new Connexion("jdbc:mysql://localhost:" + textPort.getText() + "/capteur", "root", "");
        dialogPort.setVisible(false);

        if (!connexion.connect()) {
            System.err.println("Could not connect to database");
            System.exit(0);
        }
        //refreshBatiments();
        //refreshTableauCapteurs();
        frame.setVisible(true);
    }

    private static class SelectionModelMax extends DefaultListSelectionModel {
        private JList list;
        private int maxCount;

        private SelectionModelMax(JList list, int maxCount) {
            this.list = list;

            this.maxCount = maxCount;
        }

        @Override
        public void setSelectionInterval(int index0, int index1) {
            if (index1 - index0 >= maxCount) {
                index1 = index0 + maxCount - 1;
            }
            super.setSelectionInterval(index0, index1);
        }

        @Override
        public void addSelectionInterval(int index0, int index1) {
            int selectionLength = list.getSelectedIndices().length;
            if (selectionLength >= maxCount)
                return;

            if (index1 - index0 >= maxCount - selectionLength) {
                index1 = index0 + maxCount - 1 - selectionLength;
            }
            if (index1 < index0)
                return;
            super.addSelectionInterval(index0, index1);
        }
    }
}
