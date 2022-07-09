package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class RainfallVisualiser extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Rainfall Visualiser");
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc =
                new BarChart<String,Number>(xAxis,yAxis);
        xAxis.setLabel("Rainfall Data");
        yAxis.setLabel("Value");

        String[] record = extractRecord();
        for (int i = 0; i < 218; i++) {
            record = extractRecord();

            int year = Integer.parseInt(record[0]);
            int month = Integer.parseInt(record[1]);
            double total =Double.parseDouble(record[2]);
            double min =Double.parseDouble(record[2]);
            double max =Double.parseDouble(record[2]);

            bc.setTitle("1999-2017");
            XYChart.Series series1 = new XYChart.Series();
            series1.setName(year+"-Month"+month);
            series1.getData().add(new XYChart.Data("Total Rainfall", total));
            series1.getData().add(new XYChart.Data("Minimum Rainfall", min));
            series1.getData().add(new XYChart.Data("Maximum Rainfall", max));
            bc.getData().addAll(series1);
        }

        Scene scene  = new Scene(bc,1540,500);
        stage.setScene(scene);
        stage.show();
    }
    private static String[] extractRecord() {
        if (textio.TextIO.eof()) return null; // convert EOF to null

        var text = textio.TextIO.getln();
        return text.trim().split(",");
    }

    public static void main(String[] args) {
        if (textio.TextIO.readUserSelectedFile()) {
            launch();
        } else {
            System.exit(0);
        }
    }
}