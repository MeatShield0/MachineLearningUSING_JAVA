import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PieChartVisualization extends JPanel {
    private final Map<String, Integer> salesData;

    // Constructor to initialize sales data
    public PieChartVisualization(Map<String, Integer> salesData) {
        this.salesData = salesData;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth();
        int height = getHeight();

        // Calculate total sales
        int totalSales = salesData.values().stream().mapToInt(Integer::intValue).sum();

        // Draw pie chart
        int startAngle = 0;
        int i = 0;
        Color[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.ORANGE, Color.CYAN, Color.MAGENTA, Color.PINK, Color.LIGHT_GRAY};

        for (Map.Entry<String, Integer> entry : salesData.entrySet()) {
            int arcAngle = (int) Math.round(360.0 * entry.getValue() / totalSales);
            g.setColor(colors[i % colors.length]);
            g.fillArc(50, 50, width - 100, height - 100, startAngle, arcAngle);
            startAngle += arcAngle;
            i++;
        }

        // Draw labels
        int legendX = width - 200;
        int legendY = 50;
        i = 0;
        for (Map.Entry<String, Integer> entry : salesData.entrySet()) {
            g.setColor(colors[i % colors.length]);
            g.fillRect(legendX, legendY + (i * 20), 10, 10);
            g.setColor(Color.BLACK);
            g.drawString(entry.getKey() + " (" + entry.getValue() + ")", legendX + 15, legendY + 10 + (i * 20));
            i++;
        }

        // Draw title
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Sales Data Pie Chart", width / 2 - 50, 30);
    }

    public static void main(String[] args) {
        try {
            // Load the CSV data
            List<String[]> data = CSVDataLoader.loadCSV("C:/PC/classes/TRIMESTER 3/JAVA Programming/MLinJAVA/olist_orders_dataset.csv");

            // Data preparation for pie chart
            Map<String, Integer> salesData = new HashMap<>();
            int limit = 1000;  // Limiting to 1000 rows for performance
            for (int i = 1; i < data.size() && i <= limit; i++) { // Skip header and limit rows
                String[] row = data.get(i);
                String status = row[2]; // order_status

                if (!status.isEmpty()) {
                    salesData.put(status, salesData.getOrDefault(status, 0) + 1);
                }
            }

            // Create and display the pie chart visualization
            JFrame frame = new JFrame("Sales Data Pie Chart");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.add(new PieChartVisualization(salesData));
            frame.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
