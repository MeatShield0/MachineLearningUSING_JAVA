import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BarGraphVisualization extends JPanel {
    private final Map<String, Integer> salesData;

    // Constructor to initialize sales data
    public BarGraphVisualization(Map<String, Integer> salesData) {
        this.salesData = salesData;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth();
        int height = getHeight();

        // Calculate max sales for scaling
        int maxSales = salesData.values().stream().mapToInt(v -> v).max().orElse(1);
        int barWidth = width / salesData.size();

        int i = 0;
        for (Map.Entry<String, Integer> entry : salesData.entrySet()) {
            int barHeight = (int) ((double) entry.getValue() / maxSales * (height - 50));  // 50 pixels reserved for labels
            g.setColor(Color.BLUE);
            g.fillRect(i * barWidth + 10, height - barHeight - 30, barWidth - 20, barHeight);  // Adjusting bar width and height

            g.setColor(Color.BLACK);
            g.drawString(entry.getKey(), i * barWidth + 15, height - 10);  // Label below bar
            g.drawString(String.valueOf(entry.getValue()), i * barWidth + 15, height - barHeight - 35);  // Value above bar
            i++;
        }

        // Draw axes
        g.drawLine(10, height - 30, width, height - 30);  // X-axis
        g.drawLine(10, height - 30, 10, 10);  // Y-axis

        // Draw title
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Sales Data Bar Graph", width / 2 - 60, 20);
    }

    public static void main(String[] args) {
        try {
            // Load the CSV data
            List<String[]> data = CSVDataLoader.loadCSV("C:/PC/classes/TRIMESTER 3/JAVA Programming/MLinJAVA/olist_orders_dataset.csv");

            // Data preparation for bar graph
            Map<String, Integer> salesData = new HashMap<>();
            int limit = 1000;  // Limiting to 1000 rows for performance
            for (int i = 1; i < data.size() && i <= limit; i++) { // Skip header and limit rows
                String[] row = data.get(i);
                String status = row[2]; // order_status

                if (!status.isEmpty()) {
                    salesData.put(status, salesData.getOrDefault(status, 0) + 1);
                }
            }

            // Create and display the bar graph visualization
            JFrame frame = new JFrame("Sales Data Bar Graph");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.add(new BarGraphVisualization(salesData));
            frame.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
