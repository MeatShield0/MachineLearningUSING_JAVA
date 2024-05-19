import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataVisualizationMenu {
    private static final String FILE_PATH = "C:\\PC\\classes\\TRIMESTER 3\\JAVA Programming\\MLinJAVA\\olist_order_items_dataset.csv";
    private static final int MAX_ROWS = 1000;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Data Visualization");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLayout(new BorderLayout());

            JPanel menuPanel = new JPanel();
            JButton scatterButton = new JButton("Scatterplot");
            JButton histogramButton = new JButton("Histogram");
            JButton piechartButton = new JButton("Piechart");

            menuPanel.add(scatterButton);
            menuPanel.add(histogramButton);
            menuPanel.add(piechartButton);

            frame.add(menuPanel, BorderLayout.NORTH);

            scatterButton.addActionListener(e -> showScatterPlot(frame));
            histogramButton.addActionListener(e -> showHistogram(frame));
            piechartButton.addActionListener(e -> showPieChart(frame));

            frame.setVisible(true);
        });
    }

    private static List<String[]> readCSV(String filePath, int maxRows) {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int count = 0;
            while ((line = br.readLine()) != null && count <= maxRows) {
                String[] values = line.split(",");
                data.add(values);
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    private static void showScatterPlot(JFrame frame) {
        List<String[]> data = readCSV(FILE_PATH, MAX_ROWS);
        if (data == null) return;

        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series = new XYSeries("Price vs Freight");

        for (int i = 1; i < data.size(); i++) { // Start from 1 to skip header
            double price = Double.parseDouble(data.get(i)[5]);
            double freight = Double.parseDouble(data.get(i)[6]);
            series.add(price, freight);
        }

        dataset.addSeries(series);

        JFreeChart scatterPlot = ChartFactory.createScatterPlot(
                "Price vs Freight",
                "Price",
                "Freight",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        updateChartPanel(frame, scatterPlot);
    }

    private static void showHistogram(JFrame frame) {
        List<String[]> data = readCSV(FILE_PATH, MAX_ROWS);
        if (data == null) return;

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (int i = 1; i < data.size(); i++) {
            String productId = data.get(i)[2];
            double price = Double.parseDouble(data.get(i)[5]);
            dataset.addValue(price, "Price", productId);
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "Product Prices",
                "Product ID",
                "Price",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        updateChartPanel(frame, barChart);
    }

    private static void showPieChart(JFrame frame) {
        List<String[]> data = readCSV(FILE_PATH, MAX_ROWS);
        if (data == null || data.isEmpty()) {
            System.out.println("No data found.");
            return; // Ensure there's data beyond just the header
        }
    
        DefaultPieDataset dataset = new DefaultPieDataset();
    
        System.out.println("Processing data...");
        for (int i = 1; i < data.size(); i++) {
            String[] row = data.get(i);
            if (row.length > 6) { // Ensure there's enough data in the row
                String orderItemId = row[1]; // Assuming the second column is order_item_id
                double price;
                try {
                    price = Double.parseDouble(row[5]);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid price format at row " + i + ": " + row[5]);
                    continue; // Skip this row on parse failure
                }
    
                dataset.setValue(orderItemId, price);
            }
        }
    
        if (dataset.getItemCount() == 0) {
            System.out.println("No valid data to display.");
            return;
        }
    
        JFreeChart pieChart = ChartFactory.createPieChart(
            "Order Item Prices Distribution",
            dataset,
            true, true, false);
    
        PiePlot plot = (PiePlot) pieChart.getPlot();
        plot.setCircular(true);
    
        updateChartPanel(frame, pieChart);
        System.out.println("Pie chart updated.");
    }
    

    private static void updateChartPanel(JFrame frame, JFreeChart chart) {
        frame.getContentPane().removeAll();
        frame.add(new ChartPanel(chart), BorderLayout.CENTER);
        frame.validate();
    }
}
