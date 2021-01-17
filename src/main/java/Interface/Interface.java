package Interface;

import Connexion.*;
import Mapping.*;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.DateTimePicker;
import com.github.lgooddatepicker.components.TimePicker;
import com.github.lgooddatepicker.components.TimePickerSettings;
import com.github.lgooddatepicker.optionalusertools.PickerUtilities;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Interface {

    private Database database;
    private Map<Capteur, Boolean> capteurs;
    private int portDatabase;


    /* Composants Java Swing */

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

    private DateTimePicker datePickerFin;
    private DateTimePicker datePickerDebut;
    private JTextField textPort;
    private JTextField textSeuilMin;
    private JTextField textSeuilMax;

    private ChartPanel panelCourbe1;
    private ChartPanel panelCourbe2;
    private ChartPanel panelCourbe3;
    private JPanel panelCourbes;

    private JTree treeCapteurs;
    private JScrollPane scrollPanelTree;


    public Interface(int portDatabase) {
        frame = new JFrame("Gestion des capteurs");
        capteurs = new HashMap<>();
        this.portDatabase = portDatabase;
        initialisation();
    }

    private void etablirConnexion() {

        Serveur serveur = new Serveur(this, Integer.parseInt(textPort.getText()));
        Thread t = new Thread(serveur);
        t.start();


        database = new Database("jdbc:mysql://localhost:" + portDatabase + "/capteurs", "root", "");
        dialogPort.setVisible(false);

        if (!database.connect()) {
            System.err.println("Could not connect to database");
            System.exit(0);
        }

        refreshBatiments();
        int[] selected = new int[listeBatiments.getVisibleRowCount()];
        for (int i = 0; i < selected.length; i++)
            selected[i] = i;
        listeBatiments.setSelectedIndices(selected);

        refreshTableauCapteurs();
        refreshListeCapteurs();

        generateTree();

        frame.setVisible(true);
    }

    public void newMessage(String msg) {

        String[] tokens = msg.split(" ");
        switch (tokens[0]) {
            case "Connexion":
                String[] description = tokens[2].split(":");
                connecterCapteur(tokens[1], description[0], description[1], description[2], description[3]);
                break;
            case "Deconnexion":
                deconnecterCapteur(tokens[1]);
                break;
            case "Donnee":
                updateValeur(tokens[1], Float.valueOf(tokens[2]));
                break;
        }
    }

    public void connecterCapteur(String nomC, String typeF, String batiment, String etage, String lieu) {

        database.newCapteur(nomC, typeF, batiment, etage, lieu);

        boolean exists = false;
        for (Map.Entry entry : capteurs.entrySet()) {
            if (((Capteur) entry.getKey()).getNomC().equals(nomC))
                exists = true;
        }
        if (!exists)
            capteurs.put(database.getCapteurById(nomC), true);
        else
            for (Map.Entry entry : capteurs.entrySet())
                if (((Capteur) entry.getKey()).getNomC().equals(nomC))
                    entry.setValue(true);

        refreshTableauCapteurs();
        refreshBatiments();
        refreshListeCapteurs();
    }

    public void deconnecterCapteur(String nomC) {
        for (Map.Entry entry : capteurs.entrySet())
            if (((Capteur) entry.getKey()).getNomC().equals(nomC))
                entry.setValue(false);

        refreshTableauCapteurs();
        refreshBatiments();
        refreshListeCapteurs();
    }

    public void updateValeur(String nomC, Float valeur) {
        for (Map.Entry entry : capteurs.entrySet()) {
            Capteur cpt = (Capteur) (entry.getKey());
            if (cpt.getNomC().equals(nomC)) {
                cpt.setValeur(valeur);
            }
        }
        database.addDonnee(nomC, valeur);
        refreshTableauCapteurs();
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
        JLabel labelConnexion = new JLabel("Connexion a l'interface de visualisation NeoCampus");
        JLabel labelPort = new JLabel("Veuillez renseigner le port sur lequel vous souhaitez vous connecter :");
        textPort = new JTextField("8952");
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

        checkTemperature1.setSelected(true);
        checkElectricite1.setSelected(true);
        checkEau1.setSelected(true);
        checkAirComprime1.setSelected(true);

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
        JPanel panelDates2 = new JPanel(new GridLayout(2, 1));
        JPanel panelDatesDe = new JPanel(new FlowLayout());
        JPanel panelDatesA = new JPanel(new FlowLayout());
        panelCourbes = new JPanel(new GridLayout(3, 1));
        JScrollPane scrollPanelListeCapteurs2 = new JScrollPane();
        JLabel labelDe = new JLabel("de : ");
        JLabel labelA = new JLabel(" a : ");
        listeCapteurs2 = new JList<>();
        checkEau2 = new JCheckBox("Eau");
        checkElectricite2 = new JCheckBox("Electricite");
        checkAirComprime2 = new JCheckBox("Air comprime");
        checkTemperature2 = new JCheckBox("Temperature");
        btnValiderCourbes = new JButton("Valider");

        TimePickerSettings timeSettings = new TimePickerSettings();
        timeSettings.setFormatForDisplayTime(PickerUtilities.createFormatterFromPatternString("HH:mm:ss.SSS", timeSettings.getLocale()));
        timeSettings.setFormatForMenuTimes(PickerUtilities.createFormatterFromPatternString("HH:mm:ss.SSS", timeSettings.getLocale()));

        datePickerDebut = new DateTimePicker(new DatePickerSettings(), timeSettings);
        datePickerFin = new DateTimePicker(new DatePickerSettings(), timeSettings);

        checkTemperature2.setSelected(true);
        checkElectricite2.setSelected(true);
        checkEau2.setSelected(true);
        checkAirComprime2.setSelected(true);

        panelFiltres2.setBorder(BorderFactory.createTitledBorder("Filtres"));
        listeCapteurs2.setBorder(BorderFactory.createTitledBorder("Capteurs"));

        panelFiltres2.add(scrollPanelListeCapteurs2, BorderLayout.CENTER);

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
        panelDatesDe.add(labelDe);
        panelDatesDe.add(datePickerDebut);
        panelDatesA.add(labelA);
        panelDatesA.add(datePickerFin);
        panelDates2.add(panelDatesDe);
        panelDates2.add(panelDatesA);
        panelDates.add(panelDates2, BorderLayout.CENTER);
        panelDates.add(btnValiderCourbes, BorderLayout.PAGE_END);
        panelFiltres2.add(panelFluides2, BorderLayout.PAGE_START);
        panelFiltres2.add(panelDates, BorderLayout.PAGE_END);
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
        scrollPanelTree = new JScrollPane();

        tableauInfosCapteur = new JTable();
        tableauSeuilsDefaut = new JTable();
        btnModifierSeuils = new JButton("Modifier les seuils");

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
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
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
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        });

        tableauSeuilsDefaut.getColumnModel().getColumn(0).setPreferredWidth(150);

        scrollPanelInfosCapteur.setViewportView(tableauInfosCapteur);
        scrollPanelSeuilsDefaut.setViewportView(tableauSeuilsDefaut);
        scrollPanelTree.setViewportView(treeCapteurs);

        btnModifierSeuils.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeCapteurs.getLastSelectedPathComponent();
                if (treeCapteurs.getSelectionCount() > 0 && node != null && node.isLeaf()) {
                    dialogSeuils.setVisible(true);
                    textSeuilMin.setText("");
                    textSeuilMax.setText("");
                }
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


    //////////////////////////////
    /* Méthodes ActionPerformed */
    //////////////////////////////

    /* ONGLET 1 */

    private void refreshBatiments() {
        if (checkAirComprime1.isSelected() || checkEau1.isSelected() || checkElectricite1.isSelected() || checkTemperature1.isSelected()) {
            String[] batiments = database.getBatimentsFluides(checkAirComprime1.isSelected(), checkEau1.isSelected(), checkElectricite1.isSelected(), checkTemperature1.isSelected());
            listeBatiments.setModel(new AbstractListModel<>() {
                String[] strings = batiments;

                public int getSize() {
                    return strings.length;
                }

                public String getElementAt(int i) {
                    return strings[i];
                }
            });
        } else { // liste vide
            listeBatiments.setModel(new AbstractListModel<>() {
                String[] strings = {};

                public int getSize() {
                    return 0;
                }

                public String getElementAt(int i) {
                    return null;
                }
            });
        }
    }

    private void refreshTableauCapteurs() {
        String[] batiments = listeBatiments.getSelectedValuesList().toArray(new String[0]);
        if ((checkAirComprime1.isSelected() || checkEau1.isSelected() || checkElectricite1.isSelected() || checkTemperature1.isSelected()) && batiments.length > 0) {
            // Tous les capteurs connectés
            List<Capteur> connected = new ArrayList<>();
            for (Map.Entry entry : capteurs.entrySet())
                if ((boolean) entry.getValue())
                    connected.add((Capteur) entry.getKey());
            // Tous les noms de capteus correspondants aux filtres (fluides + batiments)
            List<String> nomsCapteurs = database.getCapteursFiltresOnglet1(checkAirComprime1.isSelected(), checkEau1.isSelected(), checkElectricite1.isSelected(), checkTemperature1.isSelected(), batiments);
            // Les capteurs connectés correspondants aux filtres
            List<Capteur> toDisplay = new ArrayList<>();
            for (Capteur cpt : connected) {
                if (nomsCapteurs.contains(cpt.getNomC()))
                    toDisplay.add(cpt);
            }

            String[][] modele = new String[toDisplay.size()][6];
            for (int i = 0; i < toDisplay.size(); i++) {
                modele[i][0] = toDisplay.get(i).getNomC();
                modele[i][1] = toDisplay.get(i).getFluide().getType_fluide();
                modele[i][2] = toDisplay.get(i).getBatiment();
                modele[i][3] = String.valueOf(toDisplay.get(i).getEtage());
                modele[i][4] = toDisplay.get(i).getLieu();
                modele[i][5] = String.valueOf(toDisplay.get(i).getValeur());
            }

            tableauCapteurs.setModel(new javax.swing.table.DefaultTableModel(
                    modele, new String[]{"Nom", "Type Fluide", "Batiment", "Etage", "Lieu", "Valeur"}
            ) {
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return false;
                }
            });
        } else {  // tableau vide
            tableauCapteurs.setModel(new javax.swing.table.DefaultTableModel(
                    new Object[][]{}, new String[]{"Nom", "Type Fluide", "Batiment", "Etage", "Lieu", "Valeur"}
            ));
        }
    }

    /* ONGLET 2 */

    private void refreshListeCapteurs() {
        if (checkAirComprime2.isSelected() || checkEau2.isSelected() || checkElectricite2.isSelected() || checkTemperature2.isSelected()) {
            String[] capteurs = database.getAllCapteursFiltres(checkAirComprime2.isSelected(), checkEau2.isSelected(), checkElectricite2.isSelected(), checkTemperature2.isSelected());
            listeCapteurs2.setModel(new AbstractListModel<>() {
                String[] strings = capteurs;

                public int getSize() {
                    return strings.length;
                }

                public String getElementAt(int i) {
                    return strings[i];
                }
            });
            listeCapteurs2.setSelectionModel(new Interface.SelectionModelMax(listeCapteurs2, 3));
        } else { // liste vide
            listeCapteurs2.setModel(new AbstractListModel<>() {
                String[] strings = {};

                public int getSize() {
                    return 0;
                }

                public String getElementAt(int i) {
                    return null;
                }
            });
        }
    }

    private void displayCourbesCapteurs() {

        List<String> selected = listeCapteurs2.getSelectedValuesList();

        if (selected.size() > 0 && selected.size() < 4
                && datePickerDebut.getDatePicker().getDate() != null
                && datePickerDebut.getTimePicker().getTime() != null
                && datePickerFin.getDatePicker().getDate() != null
                && datePickerFin.getTimePicker().getTime() != null ) {

            String dateDebut = datePickerDebut.getDatePicker().getDate().toString() + " " + datePickerDebut.getTimePicker().getTime().toString();
            String dateFin = datePickerFin.getDatePicker().getDate().toString() + " " + datePickerFin.getTimePicker().getTime().toString();

            panelCourbes.removeAll();
            panelCourbe1 = new Graphique(selected.get(0), database, dateDebut, dateFin).create();
            panelCourbes.add(panelCourbe1);
            if (selected.size() > 1) {
                panelCourbe2 = new Graphique(selected.get(1), database, dateDebut, dateFin).create();
                panelCourbes.add(panelCourbe2);
            }
            if (selected.size() > 2) {
                panelCourbe3 = new Graphique(selected.get(2), database, dateDebut, dateFin).create();
                panelCourbes.add(panelCourbe3);
            }
            panelCourbes.repaint();
            frame.repaint();
        }
    }

    /* ONGLET 3 */

    private void displayInfosCapteur(Capteur capteur) {
        tableauInfosCapteur.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {"Batiment", capteur.getBatiment()},
                        {"Etage", capteur.getEtage()},
                        {"Type de fluide", capteur.getFluide().getType_fluide()},
                        {"Seuil minimum", capteur.getSeuilMin()},
                        {"Seuil maximum", capteur.getSeuilMax()}
                },
                new String[]{"Informations", "Valeurs"}
        ) {
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        });

    }

    private void editSeuils() {
        if (treeCapteurs.getSelectionCount() == 1 && !textSeuilMax.getText().equals("") && !textSeuilMin.getText().equals("")) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                    treeCapteurs.getLastSelectedPathComponent();

            if (node == null)
                return;

            Object selected = node.getUserObject();
            if (node.isLeaf()) {
                String nomCapteur = String.valueOf(selected);
                database.updateSeuils(nomCapteur, textSeuilMin.getText(), textSeuilMax.getText());
                displayInfosCapteur(database.getCapteurById(nomCapteur));
            }
            dialogSeuils.setVisible(false);
        }
    }

    private void generateTree() {

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Batiments");

        DefaultMutableTreeNode batiment = null;
        DefaultMutableTreeNode etage = null;
        DefaultMutableTreeNode capteur = null;

        String[] batiments = database.getAllBatiments();
        Integer[] etages;
        Capteur[] capteurs;

        for (String bat : batiments) {
            batiment = new DefaultMutableTreeNode(bat);
            root.add(batiment);
            etages = database.getEtagesBatiment(bat);
            for (int floor : etages) {
                etage = new DefaultMutableTreeNode("Etage " + floor);
                batiment.add(etage);
                capteurs = database.getCapteurEtageBatiment(floor, bat);
                for (Capteur cpt : capteurs) {
                    capteur = new DefaultMutableTreeNode(cpt.getNomC());
                    etage.add(capteur);
                }
            }
        }
        treeCapteurs = new JTree(root);
        scrollPanelTree.setViewportView(treeCapteurs);
        treeCapteurs.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        treeCapteurs.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                        treeCapteurs.getLastSelectedPathComponent();

                if (node == null)
                    return;

                Object selected = node.getUserObject();
                if (node.isLeaf()) {
                    String nomCapteur = String.valueOf(selected);
                    displayInfosCapteur(database.getCapteurById(nomCapteur));
                }
            }
        });
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
