package package1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HomePanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public HomePanel(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        setBackground(new Color(245, 248, 252)); // Softer background
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);

        JLabel welcomeLabel = new JLabel("Welcome to iShop", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Roboto", Font.PLAIN, 20));
        welcomeLabel.setForeground(new Color(44, 62, 80)); // Elegant dark tone
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(welcomeLabel, gbc);

        JButton addCustomerBtn = createStyledButton("Add Customer", new Color(33, 150, 243));
        addCustomerBtn.addActionListener(e -> cardLayout.show(mainPanel, "addCustomer"));
        addCustomerBtn.setToolTipText("Add a new delivery customer");
        gbc.gridy = 1;
        add(addCustomerBtn, gbc);

        JButton manageLoadBtn = createStyledButton("Manage Load", new Color(76, 175, 80));
        manageLoadBtn.addActionListener(e -> cardLayout.show(mainPanel, "manageLoad"));
        manageLoadBtn.setToolTipText("Set vehicle capacity and view products");
        gbc.gridy = 2;
        add(manageLoadBtn, gbc);

        JButton viewRouteBtn = createStyledButton("View Route", new Color(255, 152, 0));
        viewRouteBtn.addActionListener(e -> cardLayout.show(mainPanel, "viewRoute"));
        viewRouteBtn.setToolTipText("Compute and view delivery route");
        gbc.gridy = 3;
        add(viewRouteBtn, gbc);

        JButton exitBtn = createStyledButton("Exit", new Color(244, 67, 54));
        exitBtn.addActionListener(e -> System.exit(0));
        exitBtn.setToolTipText("Close the application");
        gbc.gridy = 4;
        add(exitBtn, gbc);
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
                g2d.fillRoundRect(0, 0, w, h, 30, 30); // Softer, classier corners
                // Subtle shadow
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
}