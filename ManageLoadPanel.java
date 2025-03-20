package package1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

public class ManageLoadPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private DeliveryMap map;
    private JTextField capacityField;
    private JPanel categoryPanel;
    private JTextArea productList;
    private JPanel centerPanel;
    private CardLayout centerCardLayout;
    private ArrayList<Product> currentProducts;

    public ManageLoadPanel(CardLayout cardLayout, JPanel mainPanel, DeliveryMap map) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        this.map = map;
        this.currentProducts = new ArrayList<>();
        setBackground(new Color(245, 248, 252));
        setLayout(new BorderLayout(10, 10));

        // Capacity input panel (right side)
        JPanel loadInputPanel = new JPanel(new GridBagLayout());
        loadInputPanel.setBackground(Color.WHITE);
        loadInputPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                new EmptyBorder(10, 10, 10, 10)));
        GridBagConstraints gbcLoad = new GridBagConstraints();
        gbcLoad.insets = new Insets(10, 10, 10, 10);
        gbcLoad.fill = GridBagConstraints.HORIZONTAL;

        capacityField = new JTextField(5);
        capacityField.setFont(new Font("Roboto", Font.PLAIN, 14));
        gbcLoad.gridx = 0;
        gbcLoad.gridy = 0;
        loadInputPanel.add(new JLabel("Max Capacity (kg):"), gbcLoad);
        gbcLoad.gridx = 1;
        loadInputPanel.add(capacityField, gbcLoad);

        JButton setCapacityBtn = createStyledButton("Set Capacity", new Color(76, 175, 80));
        setCapacityBtn.addActionListener(e -> {
            try {
                double capacity = Double.parseDouble(capacityField.getText().trim());
                if (capacity <= 0) {
                    JOptionPane.showMessageDialog(this, "Capacity must be positive.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                map.setMaxCapacity(capacity);
                JOptionPane.showMessageDialog(this, "Capacity set to " + capacity + " kg", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid capacity. Enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        gbcLoad.gridx = 0;
        gbcLoad.gridy = 1;
        gbcLoad.gridwidth = 2;
        loadInputPanel.add(setCapacityBtn, gbcLoad);

        // Center panel with CardLayout for categories and products
        centerCardLayout = new CardLayout();
        centerPanel = new JPanel(centerCardLayout);
        categoryPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        categoryPanel.setBackground(new Color(245, 248, 252));

        // Product list with sorting buttons
        JPanel productPanel = new JPanel(new BorderLayout(5, 5));
        productList = new JTextArea(15, 30);
        productList.setEditable(false);
        productList.setFont(new Font("Roboto", Font.PLAIN, 12));
        productList.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                new EmptyBorder(5, 5, 5, 5)));

        // Sorting buttons panel
        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        sortPanel.setBackground(new Color(245, 248, 252));
        JButton sortByNameBtn = createStyledButton("Sort by Name", new Color(52, 152, 219));
        JButton sortByWeightBtn = createStyledButton("Sort by Weight", new Color(52, 152, 219));
        JButton sortByValueBtn = createStyledButton("Sort by Value", new Color(52, 152, 219));

        // Use lambdas to access public fields directly
        sortByNameBtn.addActionListener(e -> sortAndDisplayProducts((p1, p2) -> p1.name.compareTo(p2.name)));
        sortByWeightBtn.addActionListener(e -> sortAndDisplayProducts((p1, p2) -> Double.compare(p1.weight, p2.weight)));
        sortByValueBtn.addActionListener(e -> sortAndDisplayProducts((p1, p2) -> Integer.compare(p2.value, p1.value))); // Reversed for descending order

        sortPanel.add(sortByNameBtn);
        sortPanel.add(sortByWeightBtn);
        sortPanel.add(sortByValueBtn);

        productPanel.add(sortPanel, BorderLayout.NORTH);
        productPanel.add(new JScrollPane(productList), BorderLayout.CENTER);

        centerPanel.add(categoryPanel, "Categories");
        centerPanel.add(productPanel, "Products");

        add(centerPanel, BorderLayout.CENTER);
        add(loadInputPanel, BorderLayout.EAST);

        // Back button with conditional navigation
        JButton backBtn = createStyledButton("Back", new Color(149, 165, 166));
        backBtn.addActionListener(e -> {
            Component visibleComponent = centerPanel.getComponent(0).isVisible() ? categoryPanel : productPanel;
            if (visibleComponent == productPanel) {
                centerCardLayout.show(centerPanel, "Categories");
            } else {
                cardLayout.show(mainPanel, "home");
            }
        });
        add(backBtn, BorderLayout.SOUTH);

        // Load categories initially
        loadCategories();
    }

    private void loadCategories() {
        HashSet<String> categories = new HashSet<>();
        try (Connection conn = DataBase.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT DISTINCT category FROM products")) {
            while (rs.next()) {
                categories.add(rs.getString("category"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading categories: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        categoryPanel.removeAll();
        for (String category : categories) {
            JButton categoryBtn = createStyledButton(category, new Color(52, 152, 219));
            categoryBtn.addActionListener(e -> showProductsForCategory(category));
            categoryPanel.add(categoryBtn);
        }
        categoryPanel.revalidate();
        categoryPanel.repaint();
        centerCardLayout.show(centerPanel, "Categories");
    }

    private void showProductsForCategory(String category) {
        currentProducts.clear();
        StringBuilder productText = new StringBuilder("Products in " + category + ":\n\n");
        try (Connection conn = DataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT name, weight, value FROM products WHERE category = ?")) {
            pstmt.setString(1, category);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String name = rs.getString("name");
                double weight = rs.getDouble("weight");
                int value = rs.getInt("value");
                currentProducts.add(new Product(name, weight, value));
            }
            // Initially display unsorted
            for (Product p : currentProducts) {
                productText.append(String.format("%s (Weight: %.2f kg, Value: %d Pesos)\n", p.name, p.weight, p.value));
            }
            productList.setText(productText.toString());
            centerCardLayout.show(centerPanel, "Products");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading products: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void sortAndDisplayProducts(Comparator<Product> comparator) {
        if (currentProducts.isEmpty()) {
            productList.setText("No products loaded yet.");
            return;
        }
        Collections.sort(currentProducts, comparator);
        StringBuilder productText = new StringBuilder("Products (Sorted):\n\n");
        for (Product p : currentProducts) {
            productText.append(String.format("%s (Weight: %.2f kg, Value: %d Pesos)\n", p.name, p.weight, p.value));
        }
        productList.setText(productText.toString());
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Roboto", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
        button.setOpaque(true);
        button.setBorderPainted(false);

        button.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = c.getWidth();
                int h = c.getHeight();
                g2d.setColor(button.getBackground());
                g2d.fillRoundRect(0, 0, w, h, 30, 30);
                g2d.setColor(new Color(0, 0, 0, 50));
                g2d.fillRoundRect(2, 2, w, h + 2, 30, 30);
                g2d.setColor(button.getBackground());
                g2d.fillRoundRect(0, 0, w, h, 30, 30);
                super.paint(g2d, c);
                g2d.dispose();
            }
        });

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

    public JTextField getCapacityField() {
        return capacityField;
    }
}