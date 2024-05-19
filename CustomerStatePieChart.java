import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerStatePieChart extends JPanel {
    private Map<String, Integer> stateCounts;
    private Map<String, Integer> customerCounts; // New map for customer counts
    private Color[] sliceColors;

    public CustomerStatePieChart(Map<String, Integer> stateCounts, Map<String, Integer> customerCounts) {
        this.stateCounts = stateCounts;
        this.customerCounts = customerCounts;
        this.sliceColors = generateSliceColors(stateCounts.size());
    }

    private Color[] generateSliceColors(int count) {
        Color[] colors = new Color[count];
        for (int i = 0; i < count; i++) {
            float hue = (float) i / count;
            colors[i] = Color.getHSBColor(hue, 0.6f, 0.9f); // Adjust saturation and brightness for better visibility
        }
        return colors;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Set background color
        setBackground(Color.WHITE);

        // Calculate total count for percentage calculation
        int totalCount = customerCounts.size(); // Total count is the size of the customerCounts map

        // Define pie chart dimensions
        int pieX = 50;
        int pieY = 50;
        int pieWidth = 300;
        int pieHeight = 300;

        // Draw pie chart slices
        double startAngle = 0;
        int labelX = pieX + pieWidth + 10;
        int labelY = pieY;

        int colorIndex = 0;
        for (Map.Entry<String, Integer> entry : stateCounts.entrySet()) {
            int count = entry.getValue();
            double angle = (count / (double) totalCount) * 360;

            // Set color for the slice
            g2d.setColor(sliceColors[colorIndex]);

            // Fill pie chart slice
            g2d.fillArc(pieX, pieY, pieWidth, pieHeight, (int) startAngle, (int) angle);

            // Draw label
            drawLabel(g2d, entry.getKey(), labelX, labelY);

            // Update start angle for the next slice
            startAngle += angle;
            colorIndex++;
        }

        // Draw legend
        drawLegend(g2d, pieX + pieWidth + 50, pieY + 20);

        // Draw title
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.drawString("Customer State Distribution", pieX + 20, pieY - 20);
    }

    private void drawLabel(Graphics2D g2d, String label, int x, int y) {
        g2d.setColor(Color.BLACK);
        g2d.drawString(label, x, y);
    }

    private void drawLegend(Graphics2D g2d, int x, int y) {
        int legendX = x;
        int legendY = y;

        int colorIndex = 0;
        for (Map.Entry<String, Integer> entry : stateCounts.entrySet()) {
            g2d.setColor(sliceColors[colorIndex]);
            g2d.fillRect(legendX, legendY, 10, 10);
            g2d.setColor(Color.BLACK);
            g2d.drawString(entry.getKey(), legendX + 20, legendY + 10);
            legendY += 20;
            colorIndex++;
        }
    }

    public static void main(String[] args) {
        // Load the CSV data and extract a sample of 1000 rows
        Map<String, Integer> stateCounts = new HashMap<>();
        Map<String, Integer> customerCounts = new HashMap<>(); // New map for customer counts
        try {
            List<String[]> data = CSVDataLoader.loadCSV("C:/PC/classes/TRIMESTER 3/JAVA Programming/MLinJAVA/olist_customers_dataset.csv");

            int limit = Math.min(data.size() - 1, 1000); // Limit to 1000 rows or less
            for (int i = 1; i <= limit; i++) { // Skip header
                String[] row = data.get(i);
                String state = row[4]; //  customer_state is in the 5th column (index 4)
                String customerId = row[1]; //  customer_unique_id is in the 2nd column (index 1)

                stateCounts.put(state, stateCounts.getOrDefault(state, 0) + 1);
                customerCounts.put(customerId, 1); // Count unique customers
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create and display the pie chart
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Customer State Pie Chart");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 400); // Adjusted size
            frame.add(new CustomerStatePieChart(stateCounts, customerCounts));
            frame.setVisible(true);
        });
    }
}
