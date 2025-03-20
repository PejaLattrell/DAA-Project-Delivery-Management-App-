package package1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class HeaderPanel extends JPanel {
    private JTextField searchField;
    private DeliveryMap map;

    public HeaderPanel(DeliveryMap map) {
        this.map = map;
        setBackground(new Color(44, 62, 80)); // Elegant dark tone
        setPreferredSize(new Dimension(900, 60));
        setLayout(new BorderLayout());

        // Left: App Title and Buttons with more space
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10)); // Increased horizontal gap to 20
        leftPanel.setBackground(new Color(44, 62, 80));
        JLabel appTitle = new JLabel("iShop");
        appTitle.setFont(new Font("Roboto", Font.BOLD, 28));
        appTitle.setForeground(Color.WHITE);
        appTitle.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        leftPanel.add(appTitle);

        // Subtle separator for visual appeal
        JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
        separator.setPreferredSize(new Dimension(2, 30));
        separator.setForeground(new Color(200, 200, 200));
        leftPanel.add(separator);

        JButton saveBtn = createStyledButton("Save Points", new Color(46, 204, 113));
        saveBtn.addActionListener(e -> savePointsToFile());
        leftPanel.add(saveBtn);

        JButton loadBtn = createStyledButton("Load Points", new Color(52, 152, 219));
        loadBtn.addActionListener(e -> loadPointsFromFile());
        leftPanel.add(loadBtn);

        JButton helpBtn = createStyledButton("Help", new Color(241, 196, 15));
        helpBtn.addActionListener(e -> showHelpDialog());
        leftPanel.add(helpBtn);

        JButton aboutBtn = createStyledButton("About", new Color(155, 89, 182));
        aboutBtn.addActionListener(e -> showAboutDialog());
        leftPanel.add(aboutBtn);

        add(leftPanel, BorderLayout.WEST);

        // Right: Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        searchPanel.setBackground(new Color(44, 62, 80));
        searchField = new JTextField(15);
        searchField.setFont(new Font("Roboto", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1, true),
                new EmptyBorder(8, 8, 8, 8)));
        searchField.setToolTipText("Search by customer name, address, or product name");

        JButton searchBtn = createStyledButton("Search", new Color(41, 128, 185));
        searchBtn.addActionListener(e -> performSearch());

        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        add(searchPanel, BorderLayout.EAST);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Roboto", Font.BOLD, 12)); // Smaller font for header
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16)); // Smaller padding
        button.setOpaque(true);
        button.setBorderPainted(false);

        // Custom UI for gently rounded corners and classy shadow
        button.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = c.getWidth();
                int h = c.getHeight();
                g2d.setColor(button.getBackground());
                g2d.fillRoundRect(0, 0, w, h, 30, 30); // Classy, softer corners
                // Subtle shadow
                g2d.setColor(new Color(0, 0, 0, 50));
                g2d.fillRoundRect(2, 2, w, h + 2, 30, 30);
                g2d.setColor(button.getBackground());
                g2d.fillRoundRect(0, 0, w, h, 30, 30);
                super.paint(g2d, c);
                g2d.dispose();
            }
        });

        // Smooth hover and press effects
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        return button;
    }

    private void performSearch() {
        String query = searchField.getText().trim().toLowerCase();
        if (query.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search term.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Search for customers
        ArrayList<DeliveryPoint> customerMatches = new ArrayList<>();
        for (DeliveryPoint p : map.getPoints()) {
            if (p.name.toLowerCase().contains(query) || p.address.toLowerCase().contains(query)) {
                customerMatches.add(p);
            }
        }

        // Search for products
        ArrayList<Product> productMatches = new ArrayList<>();
        try (Connection conn = DataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT name, weight, value FROM products WHERE name LIKE ?")) {
            pstmt.setString(1, "%" + query + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String name = rs.getString("name");
                double weight = rs.getDouble("weight");
                int value = rs.getInt("value");
                productMatches.add(new Product(name, weight, value));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error searching products: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Combine results
        if (customerMatches.isEmpty() && productMatches.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No customers or products found matching '" + query + "'.", "Search Results", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JTextArea resultsArea = new JTextArea(10, 40);
            resultsArea.setEditable(false);
            resultsArea.setFont(new Font("Roboto", Font.PLAIN, 14));
            StringBuilder sb = new StringBuilder("Search Results for '" + query + "':\n\n");

            // Customer results
            if (!customerMatches.isEmpty()) {
                sb.append("Customers:\n");
                for (DeliveryPoint p : customerMatches) {
                    sb.append(p.toString()).append("\n");
                }
                sb.append("\n");
            }

            // Product results
            if (!productMatches.isEmpty()) {
                sb.append("Products:\n");
                for (Product p : productMatches) {
                    sb.append(String.format("%s (Weight: %.2f kg, Value: %d Pesos)\n", p.name, p.weight, p.value));
                }
            }

            resultsArea.setText(sb.toString());
            JOptionPane.showMessageDialog(this, new JScrollPane(resultsArea), "Search Results", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void savePointsToFile() {
        try (PrintWriter writer = new PrintWriter("delivery_points.txt")) {
            for (DeliveryPoint p : map.getPoints()) {
                if (!p.name.equals("Warehouse")) {
                    writer.println(p.name + "," + p.address + "," + p.x + "," + p.y);
                }
            }
            JOptionPane.showMessageDialog(this, "Points saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving points: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadPointsFromFile() {
        try (Scanner scanner = new Scanner(new File("delivery_points.txt"))) {
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(",");
                if (parts.length == 4) {
                    String name = parts[0];
                    String address = parts[1];
                    int x = Integer.parseInt(parts[2]);
                    int y = Integer.parseInt(parts[3]);
                    map.addCustomer(name, address, x, y);
                }
            }
            JOptionPane.showMessageDialog(this, "Points loaded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "No saved points file found.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading points: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showHelpDialog() {
        JTextArea helpText = new JTextArea(10, 40);
        helpText.setEditable(false);
        helpText.setFont(new Font("Roboto", Font.PLAIN, 14));
        helpText.setText(
                "iShop - Delivery Management App\n\n" +
                "Features:\n" +
                "- Add Customer: Add new delivery points with coordinates.\n" +
                "- Manage Load: Set vehicle capacity and view products.\n" +
                "- View Route: Compute and visualize the delivery route and load.\n" +
                "- Save Points: Save current delivery points to a file.\n" +
                "- Load Points: Load delivery points from a file.\n" +
                "- Search: Find customers by name or address, or products by name.\n\n" +
                "Coordinates must be between 0 and 600."
        );
        JOptionPane.showMessageDialog(this, new JScrollPane(helpText), "Help", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showAboutDialog() {
        JTextArea aboutText = new JTextArea(6, 30);
        aboutText.setEditable(false);
        aboutText.setFont(new Font("Roboto", Font.PLAIN, 14));
        aboutText.setText(
                "iShop - Delivery Management App\n\n" +
                "Version: 1.0.0\n" +
                "Developed by: Debailoper\n" +
                "© 2025 iShop - All Rights Reserved\n" +
                "A tool for optimizing delivery routes and loads."
        );
        JOptionPane.showMessageDialog(this, new JScrollPane(aboutText), "About iShop", JOptionPane.INFORMATION_MESSAGE);
    }

    public JTextField getSearchField() {
        return searchField;
    }
}