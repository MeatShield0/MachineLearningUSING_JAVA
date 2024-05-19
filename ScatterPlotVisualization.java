import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//notworking
public class ScatterPlotVisualization extends JPanel {
    private final List<Point> scatterData;
    private final int pointSize = 5;  // Size of the points to be drawn

    // Constructor to initialize scatter data
    public ScatterPlotVisualization(List<Point> scatterData) {
        this.scatterData = scatterData;
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
        int minX = scatterData.stream().mapToInt(p -> p.x).min().orElse(0);
        int maxX = scatterData.stream().mapToInt(p -> p.x).max().orElse(1);
        int minY = scatterData.stream().mapToInt(p -> p.y).min().orElse(0);
        int maxY = scatterData.stream().mapToInt(p -> p.y).max().orElse(1);

        // Draw title
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.drawString("Customer Zip Code vs Customer State Scatter Plot", width / 2 - 200, 20);

        // Draw points
        g2d.setColor(new Color(0, 102, 204));
        for (Point point : scatterData) {
            // Scale the points to fit within the panel
            int x = (int) ((point.x - minX) / (double) (maxX - minX) * (width - 100)) + 50;
            int y = (int) ((point.y - minY) / (double) (maxY - minY) * (height - 100)) + 50;
            g2d.fillOval(x, height - y, pointSize, pointSize); // Adjusting y to match coordinate system
        }

        // Draw axes
        g2d.setColor(Color.BLACK);
        g2d.drawLine(50, height - 50, width - 50, height - 50); // x-axis
        g2d.drawLine(50, 50, 50, height - 50); // y-axis

        // Draw x-axis labels and grid lines
        int numberOfXLabels = 10;
        for (int i = 0; i <= numberOfXLabels; i++) {
            int xLabel = minX + (maxX - minX) * i / numberOfXLabels;
            int x = 50 + (width - 100) * i / numberOfXLabels;

            g2d.drawString(String.valueOf(xLabel), x - 10, height - 30);
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.drawLine(x, height - 50, x, 50);
            g2d.setColor(Color.BLACK);
        }

        // Draw y-axis labels and grid lines
        int numberOfYLabels = 10;
        for (int j = 0; j <= numberOfYLabels; j++) {
            int yLabel = minY + (maxY - minY) * j / numberOfYLabels;
            int y = height - 50 - (height - 100) * j / numberOfYLabels;

            g2d.drawString(String.valueOf(yLabel), 20, y + 5);
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.drawLine(50, y, width - 50, y);
            g2d.setColor(Color.BLACK);
        }

        // Draw labels
        g2d.drawString("Customer Zip Code", width / 2, height - 10);
        g2d.drawString("Customer State", 10, height / 2);
    }

    public static void main(String[] args) {
        try {
            // Load the CSV data
            List<String[]> data = CSVDataLoader.loadCSV("C:/PC/classes/TRIMESTER 3/JAVA Programming/MLinJAVA/olist_customers_dataset.csv");

            // Data preparation for scatter plot
            List<Point> scatterData = new ArrayList<>();
            int limit = Math.min(data.size() - 1, 1000); // Limit to 1000 rows or less
            for (int i = 1; i <= limit; i++) { // Skip header
                String[] row = data.get(i);
                if (row.length >= 5) {
                    try {
                        int zipCode = Integer.parseInt(row[2]); // customer_zip_code_prefix
                        String state = row[4]; // customer_state

                        scatterData.add(new Point(zipCode, state.hashCode()));
                    } catch (NumberFormatException e) {
                        // Handle parsing error for zip code
                        System.out.println("Invalid zip code format: " + row[2]);
                    }
                } else {
                    System.out.println("Invalid row format: " + String.join(",", row));
                }
            }

            // Create and display the scatter plot visualization
            JFrame frame = new JFrame("Customer Zip Code vs Customer State Scatter Plot");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.add(new ScatterPlotVisualization(scatterData));
            frame.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
