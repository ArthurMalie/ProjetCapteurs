package Mapping;

import Connexion.Database;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Graphique {
    private final int idC;
    private final Database connection;
    private final String dateDebut;
    private final String dateFin;

    public Graphique (int idC, Database connection, String dateDebut, String dateFin ) {
        this.idC = idC;
        this.connection = connection;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    public ChartPanel create () throws SQLException {

        String chartTitle = " Capteur " + idC;
        String xAxisLabel = "Date/Time";
        String yAxisLabel = "Valeur";
        XYDataset dataset = createDataset();
        boolean showLegend = true;
        boolean createURL = false;
        boolean createTooltip = false;


        JFreeChart chart = ChartFactory.createTimeSeriesChart(chartTitle,
                xAxisLabel, yAxisLabel, dataset,
                showLegend, createTooltip, createURL);


        XYPlot plot = chart.getXYPlot();
        int width = 300;
        int height = 200;


        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("dd-MMM-yyyy HH:mm"));

        return new ChartPanel(chart);
    }

    public XYDataset createDataset () throws SQLException {
        TimeSeriesCollection mydata = new TimeSeriesCollection();
        TimeSeries series11 = new TimeSeries("Data 1");
        try {
            //This query helps to fetch the data from the database.

            ResultSet y = connection.executeQuery("SELECT `valeur` FROM `donnee`");
            ResultSet x = connection.executeQuery("SELECT `dateTime` FROM `donnee`");
            ResultSet table = connection.executeQuery("SELECT * FROM `donnee` WHERE idC=" + idC + " AND dateTime BETWEEN" + dateDebut + " AND " + dateFin);

            while (table.next()) {
                Timestamp time = table.getTimestamp("dateTime");
                Double value = table.getDouble("valeur");
                series11.addOrUpdate(new Millisecond(time), value);
                System.out.println("The date" + time);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(series11);

        return dataset;
    }


    /*
    ____________________________
              GETTERS
    ____________________________
    */

    public int getIdC () {
        return idC;
    }

}
