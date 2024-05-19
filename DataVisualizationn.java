import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataVisualizationn extends JPanel {
    private final List<Point> scatterData;
    private final int pointSize = 5;  // Size of the points to be drawn
    private final int padding = 50;   // Padding around the plot

    // Constructor to initialize scatter data
    public DataVisualizationn(List<Point> scatterData) {
        this.scatterData = scatterData;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth();
        int height = getHeight();

        // Find min and max values to scale the plot
        int minX = scatterData.stream().mapToInt(p -> p.x).min().orElse(0);
        int maxX = scatterData.stream().mapToInt(p -> p.x).max().orElse(1);
        int minY = scatterData.stream().mapToInt(p -> p.y).min().orElse(0);
        int maxY = scatterData.stream().mapToInt(p -> p.y).max().orElse(1);

        // Draw axis lines
        g.setColor(Color.BLACK);
        g.drawLine(padding, height - padding, padding, padding); // Y axis
        g.drawLine(padding, height - padding, width - padding, height - padding); // X axis

        // Draw axis labels
        g.drawString("Approved Date", width / 2, height - padding / 2); // X axis label
        g.drawString("Delivery Date", padding / 4, height / 2); // Y axis label

        // Draw grid lines and labels
        g.setColor(Color.LIGHT_GRAY);
        int gridCount = 10;
        for (int i = 0; i <= gridCount; i++) {
            int x = padding + i * (width - 2 * padding) / gridCount;
            int y = height - padding - i * (height - 2 * padding) / gridCount;

            g.drawLine(x, height - padding, x, padding); // Vertical grid lines
            g.drawLine(padding, y, width - padding, y); // Horizontal grid lines

            // X axis labels
            String xLabel = String.valueOf(minX + i * (maxX - minX) / gridCount);
            g.drawString(xLabel, x - g.getFontMetrics().stringWidth(xLabel) / 2, height - padding + 20);

            // Y axis labels
            String yLabel = String.valueOf(minY + i * (maxY - minY) / gridCount);
            g.drawString(yLabel, padding - g.getFontMetrics().stringWidth(yLabel) - 10, y + 5);
        }

        // Draw scatter points
        g.setColor(Color.BLUE);
        for (Point point : scatterData) {
            int x = padding + (int) ((point.x - minX) / (double) (maxX - minX) * (width - 2 * padding));
            int y = padding + (int) ((point.y - minY) / (double) (maxY - minY) * (height - 2 * padding));
            g.fillOval(x - pointSize / 2, height - y - pointSize / 2, pointSize, pointSize);
        }
    }

    public static void main(String[] args) {
        try {
            // Load the CSV data
            List<String[]> data = CSVDataLoader.loadCSV("C:/PC/classes/TRIMESTER 3/JAVA Programming/MLinJAVA/olist_orders_dataset.csv");

            // Limit the dataset to 1000 rows for performance reasons
            int limit = 1000;
            List<Point> scatterData = new ArrayList<>();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

            for (int i = 1; i < data.size() && scatterData.size() < limit; i++) { // Skip header and limit rows
                String[] row = data.get(i);
                String approvedDateStr = row[4]; // order_approved_at
                String deliveryDateStr = row[6]; // order_delivered_customer_date

                if (!approvedDateStr.isEmpty() && !deliveryDateStr.isEmpty()) {
                    try {
                        Date approvedDate = dateFormat.parse(approvedDateStr);
                        Date deliveryDate = dateFormat.parse(deliveryDateStr);

                        int approvedDateInt = (int) ((approvedDate.getTime() - dateFormat.parse("01-01-1970 00:00").getTime()) / (1000 * 60 * 60 * 24));
                        int deliveryDateInt = (int) ((deliveryDate.getTime() - dateFormat.parse("01-01-1970 00:00").getTime()) / (1000 * 60 * 60 * 24));

                        scatterData.add(new Point(approvedDateInt, deliveryDateInt));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

            // Create and display the scatter plot visualization
            JFrame frame = new JFrame("Approval vs Delivery Time Scatter Plot");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.add(new DataVisualizationn(scatterData));
            frame.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
