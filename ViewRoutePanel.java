package package1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ViewRoutePanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private DeliveryMap map;
    private JTextField capacityField;

    public ViewRoutePanel(CardLayout cardLayout, JPanel mainPanel, DeliveryMap map, JTextField capacityField) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        this.map = map;
        this.capacityField = capacityField;
        setBackground(new Color(236, 240, 241));
        setLayout(new BorderLayout(10, 10));

        JButton computeBtn = createStyledButton("Compute Route & Load", new Color(255, 152, 0));
        computeBtn.addActionListener(e -> {
            if (capacityField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Set capacity in Manage Load first.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            map.computeRouteAndKnapsack();
            JTextArea detailsArea = new JTextArea(20, 40);
            detailsArea.setEditable(false);
            detailsArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            detailsArea.setText(map.getRouteDetails() + "\n\n" + map.getKnapsackDetails());
            JOptionPane.showMessageDialog(this, new JScrollPane(detailsArea), "Delivery Details", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton resetBtn = createStyledButton("Reset", new Color(244, 67, 54));
        resetBtn.addActionListener(e -> {
            map.reset();
            capacityField.setText("");
            JOptionPane.showMessageDialog(this, "App reset successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton backBtn = createStyledButton("Back", new Color(149, 165, 166));
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "home"));

        JPanel routeControl = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        routeControl.setBackground(new Color(236, 240, 241));
        routeControl.add(computeBtn);
        routeControl.add(resetBtn);
        routeControl.add(backBtn);

        add(map, BorderLayout.CENTER);
        add(routeControl, BorderLayout.SOUTH);
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