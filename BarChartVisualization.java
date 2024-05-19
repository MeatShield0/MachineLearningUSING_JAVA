import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BarChartVisualization extends JPanel {
    private final Map<String, Integer> customerCountByState;

    // Constructor to initialize customer count data
    public BarChartVisualization(Map<String, Integer> customerCountByState) {
        this.customerCountByState = customerCountByState;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        int width = getWidth();
        int height = getHeight();

        // Anti-aliasing for smoother graphics
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Find min and max values to scale the plot
        int maxCount = customerCountByState.values().stream().mapToInt(v -> v).max().orElse(1);
        int barWidth = Math.max(10, width / customerCountByState.size() - 10);

        // Draw title
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.drawString("Customer Distribution by State", width / 2 - 100, 20);

        // Draw bars
        int i = 0;
        int padding = 40;
        for (Map.Entry<String, Integer> entry : customerCountByState.entrySet()) {
            int barHeight = (int) ((double) entry.getValue() / maxCount * (height - 2 * padding));
            int x = i * (barWidth + 10) + padding;
            int y = height - barHeight - padding;

            g2d.setColor(new Color(0, 102, 204));
            g2d.fillRect(x, y, barWidth, barHeight);

            // Draw the count above the bar
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.PLAIN, 12));
            g2d.drawString(String.valueOf(entry.getValue()), x + (barWidth / 2) - 10, y - 5);

            // Draw state label at the bottom
            g2d.drawString(entry.getKey(), x + (barWidth / 2) - 10, height - padding + 15);

            i++;
        }

        // Draw axes
        g2d.setColor(Color.BLACK);
        g2d.drawLine(padding, height - padding, width - padding, height - padding); // x-axis
        g2d.drawLine(padding, padding, padding, height - padding); // y-axis

        // Draw y-axis labels and grid lines
        int numberOfYLabels = 10;
        for (int j = 0; j <= numberOfYLabels; j++) {
            int yLabel = maxCount * j / numberOfYLabels;
            int y = height - padding - (height - 2 * padding) * j / numberOfYLabels;

            g2d.drawString(String.valueOf(yLabel), padding - 30, y + 5);
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.drawLine(padding, y, width - padding, y);
            g2d.setColor(Color.BLACK);
        }

        // Draw labels
        g2d.drawString("States", width / 2, height - padding + 40);
        g2d.drawString("Number of Customers", padding - 20, padding - 20);
    }

    public static void main(String[] args) {
        try {
            // Load the CSV data
            List<String[]> data = CSVDataLoader.loadCSV("C:/PC/classes/TRIMESTER 3/JAVA Programming/MLinJAVA/olist_customers_dataset.csv");

            // Data preparation for bar chart
            Map<String, Integer> customerCountByState = new HashMap<>();
            for (int i = 1; i < data.size(); i++) { // Skip header
                String[] row = data.get(i);
                String state = row[4]; // customer_state
                customerCountByState.put(state, customerCountByState.getOrDefault(state, 0) + 1);
            }

            // Create and display the bar chart visualization
            JFrame frame = new JFrame("Customer Distribution by State");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.add(new BarChartVisualization(customerCountByState));
            frame.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
