package package1;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

class DeliveryMap extends JPanel {
    private ArrayList<DeliveryPoint> route;
    private boolean showRoute;
    private ArrayList<Product> selectedProducts;
    private double maxCapacity;
    private DeliveryPoint warehouse;

    public DeliveryMap() {
        route = new ArrayList<>();
        showRoute = false;
        maxCapacity = 0;
        selectedProducts = new ArrayList<>();
        setBackground(new Color(240, 245, 250));
        setPreferredSize(new Dimension(600, 600));
        setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150), 1));

        // Load warehouse from DB
        loadWarehouse();
    }

    private void loadWarehouse() {
        try (Connection conn = DataBase.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM delivery_points WHERE name = 'Warehouse'")) {
            if (rs.next()) {
                warehouse = new DeliveryPoint(rs.getString("name"), rs.getString("address"),
                        rs.getInt("x"), rs.getInt("y"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            warehouse = new DeliveryPoint("Warehouse", "500 Delivery Hub", 300, 300); // Fallback
        }
    }

    private void selectionSort(ArrayList<Product> list) {
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            int maxIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (list.get(j).value > list.get(maxIdx).value) {
                    maxIdx = j;
                }
            }
            Product temp = list.get(i);
            list.set(i, list.get(maxIdx));
            list.set(maxIdx, temp);
        }
    }

    public void addCustomer(String name, String address, int x, int y) {
        try (Connection conn = DataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO delivery_points (name, address, x, y) VALUES (?, ?, ?, ?)")) {
            pstmt.setString(1, name);
            pstmt.setString(2, address);
            pstmt.setInt(3, x);
            pstmt.setInt(4, y);
            pstmt.executeUpdate();
            repaint();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding customer: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setMaxCapacity(double capacity) {
        this.maxCapacity = capacity;
    }

    public void computeRouteAndKnapsack() {
        ArrayList<DeliveryPoint> points = getPoints();
        route.clear();
        route.add(warehouse);
        ArrayList<DeliveryPoint> unvisited = new ArrayList<>(points);
        unvisited.remove(warehouse);

        DeliveryPoint current = warehouse;
        while (!unvisited.isEmpty()) {
            DeliveryPoint nearest = null;
            double minDist = Double.MAX_VALUE;

            for (DeliveryPoint p : unvisited) {
                double dist = Math.sqrt(Math.pow(current.x - p.x, 2) + Math.pow(current.y - p.y, 2));
                if (dist < minDist) {
                    minDist = dist;
                    nearest = p;
                }
            }

            if (nearest != null) {
                route.add(nearest);
                unvisited.remove(nearest);
                current = nearest;
            }
        }
        route.add(warehouse);
        showRoute = true;

        selectedProducts.clear();
        if (maxCapacity > 0) {
            ArrayList<Product> products = getProducts();
            int n = products.size();
            double[] weights = new double[n];
            int[] values = new int[n];
            for (int i = 0; i < n; i++) {
                weights[i] = products.get(i).weight;
                values[i] = products.get(i).value;
            }

            int[][] dp = new int[n + 1][(int)maxCapacity + 1];
            for (int i = 1; i <= n; i++) {
                for (int w = 0; w <= (int)maxCapacity; w++) {
                    if (weights[i - 1] <= w) {
                        dp[i][w] = Math.max(values[i - 1] + dp[i - 1][(int)(w - weights[i - 1])], dp[i - 1][w]);
                    } else {
                        dp[i][w] = dp[i - 1][w];
                    }
                }
            }

            int w = (int)maxCapacity;
            for (int i = n; i > 0 && w > 0; i--) {
                if (dp[i][w] != dp[i - 1][w]) {
                    selectedProducts.add(products.get(i - 1));
                    w -= weights[i - 1];
                }
            }
        }
        repaint();
    }

    public void reset() {
        try (Connection conn = DataBase.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM delivery_points WHERE name != 'Warehouse'");
            route.clear();
            selectedProducts.clear();
            maxCapacity = 0;
            showRoute = false;
            repaint();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error resetting: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public String getRouteDetails() {
        StringBuilder sb = new StringBuilder("Delivery Route:\n");
        if (!showRoute) return "No route computed yet.";
        double totalDistance = 0;
        for (int i = 0; i < route.size() - 1; i++) {
            DeliveryPoint from = route.get(i);
            DeliveryPoint to = route.get(i + 1);
            double dist = Math.sqrt(Math.pow(from.x - to.x, 2) + Math.pow(from.y - to.y, 2));
            totalDistance += dist;
            sb.append(from).append(" -> ").append(to).append(" (Distance: ").append(String.format("%.1f", dist)).append(" km)\n");
        }
        sb.append("Total Distance Travelled: ").append(String.format("%.1f", totalDistance)).append(" km");
        return sb.toString();
    }

    public String getKnapsackDetails() {
        StringBuilder sb = new StringBuilder("Selected Products (Max Capacity: " + maxCapacity + "):\n");
        if (selectedProducts.isEmpty()) {
            sb.append("No products selected or capacity not set.\n");
        } else {
            int totalValue = 0;
            double totalWeight = 0;
            for (Product p : selectedProducts) {
                sb.append("- ").append(p).append("\n");
                totalValue += p.value;
                totalWeight += p.weight;
            }
            sb.append("Total Weight: ").append(totalWeight).append(" Kg\n");
            sb.append("Total Value: ").append(totalValue).append(" Pesos\n");
        }
        return sb.toString();
    }

    public ArrayList<Product> getProducts() {
        ArrayList<Product> products = new ArrayList<>();
        try (Connection conn = DataBase.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name, weight, value FROM products")) {
            while (rs.next()) {
                products.add(new Product(rs.getString("name"), rs.getDouble("weight"), rs.getInt("value")));
            }
            selectionSort(products); // Keep your sorting logic
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public ArrayList<DeliveryPoint> getPoints() {
        ArrayList<DeliveryPoint> points = new ArrayList<>();
        try (Connection conn = DataBase.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name, address, x, y FROM delivery_points")) {
            while (rs.next()) {
                points.add(new DeliveryPoint(rs.getString("name"), rs.getString("address"),
                        rs.getInt("x"), rs.getInt("y")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return points;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        ArrayList<DeliveryPoint> points = getPoints();
        for (DeliveryPoint p : points) {
            if (p.name.equals("Warehouse")) {
                g2d.setColor(new Color(255, 87, 34));
                g2d.fillOval(p.x - 8, p.y - 8, 16, 16);
                g2d.setColor(Color.BLACK);
                g2d.drawString(p.name, p.x + 10, p.y - 5);
            } else {
                g2d.setColor(new Color(33, 150, 243));
                g2d.fillOval(p.x - 5, p.y - 5, 10, 10);
                g2d.setColor(Color.BLACK);
                g2d.drawString(p.name, p.x + 10, p.y - 5);
            }
        }

        if (showRoute && route.size() > 1) {
            g2d.setColor(new Color(76, 175, 80));
            g2d.setStroke(new BasicStroke(2));
            for (int i = 0; i < route.size() - 1; i++) {
                DeliveryPoint from = route.get(i);
                DeliveryPoint to = route.get(i + 1);
                g2d.drawLine(from.x, from.y, to.x, to.y);
            }
        }
    }
}