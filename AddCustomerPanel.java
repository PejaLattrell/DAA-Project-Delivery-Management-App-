package package1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AddCustomerPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private DeliveryMap map;

    public AddCustomerPanel(CardLayout cardLayout, JPanel mainPanel, DeliveryMap map) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        this.map = map;
        setBackground(new Color(236, 240, 241));
        setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            new EmptyBorder(10, 10, 10, 10)));
        GridBagConstraints gbcInput = new GridBagConstraints();
        gbcInput.insets = new Insets(10, 10, 10, 10);
        gbcInput.fill = GridBagConstraints.HORIZONTAL;

        JTextField nameField = new JTextField(20);
        JTextField addressField = new JTextField(20);
        JTextField xField = new JTextField(5);
        JTextField yField = new JTextField(5);

        gbcInput.gridx = 0; gbcInput.gridy = 0;
        inputPanel.add(new JLabel("Customer Name:"), gbcInput);
        gbcInput.gridx = 1;
        inputPanel.add(nameField, gbcInput);

        gbcInput.gridx = 0; gbcInput.gridy = 1;
        inputPanel.add(new JLabel("Address:"), gbcInput);
        gbcInput.gridx = 1;
        inputPanel.add(addressField, gbcInput);

        gbcInput.gridx = 0; gbcInput.gridy = 2;
        inputPanel.add(new JLabel("X Coord (0-600):"), gbcInput);
        gbcInput.gridx = 1;
        inputPanel.add(xField, gbcInput);

        gbcInput.gridx = 0; gbcInput.gridy = 3;
        inputPanel.add(new JLabel("Y Coord (0-600):"), gbcInput);
        gbcInput.gridx = 1;
        inputPanel.add(yField, gbcInput);

        JButton addBtn = createStyledButton("Add Customer", new Color(33, 150, 243));
        addBtn.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                String address = addressField.getText().trim();
                int x = Integer.parseInt(xField.getText().trim());
                int y = Integer.parseInt(yField.getText().trim());

                if (name.isEmpty() || address.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Name and address cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (x < 0 || x > 600 || y < 0 || y > 600) {
                    JOptionPane.showMessageDialog(this, "Coordinates must be between 0 and 600.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                map.addCustomer(name, address, x, y);
                nameField.setText("");
                addressField.setText("");
                xField.setText("");
                yField.setText("");
                JOptionPane.showMessageDialog(this, "Customer added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid coordinates. Enter numbers only.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        gbcInput.gridx = 0; gbcInput.gridy = 4; gbcInput.gridwidth = 2;
        inputPanel.add(addBtn, gbcInput);

        JButton backBtn = createStyledButton("Back", new Color(149, 165, 166));
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "home"));

        add(inputPanel, BorderLayout.CENTER);
        add(backBtn, BorderLayout.SOUTH);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Roboto", Font.BOLD, 16)); // Modern, bold font
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24)); // Generous padding
        button.setOpaque(true);
        button.setBorderPainted(false);

        // Custom UI for rounded corners and smooth rendering
        button.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = c.getWidth();
                int h = c.getHeight();
                g2d.setColor(button.getBackground());
                g2d.fillRoundRect(0, 0, w, h, 20, 20); // Rounded corners
                super.paint(g2d, c);
                g2d.dispose();
            }
        });

        // Hover and press effects
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
    
}