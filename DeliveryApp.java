package package1;

import javax.swing.*;
import java.awt.*;

public class DeliveryApp extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private DeliveryMap map;
    private ManageLoadPanel manageLoadPanel;

    public DeliveryApp() {
        setTitle("iShop");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);

        // Custom content pane with elegant gradient background
        JPanel contentPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setPaint(new GradientPaint(0, 0, new Color(245, 248, 252), 0, getHeight(), new Color(230, 236, 240)));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        contentPane.setLayout(new BorderLayout());
        setContentPane(contentPane);

        // Initialize components
        map = new DeliveryMap();
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Add panels
        HeaderPanel header = new HeaderPanel(map);
        contentPane.add(header, BorderLayout.NORTH);

        mainPanel.add(new HomePanel(cardLayout, mainPanel), "home");
        mainPanel.add(new AddCustomerPanel(cardLayout, mainPanel, map), "addCustomer");
        manageLoadPanel = new ManageLoadPanel(cardLayout, mainPanel, map);
        mainPanel.add(manageLoadPanel, "manageLoad");
        mainPanel.add(new ViewRoutePanel(cardLayout, mainPanel, map, manageLoadPanel.getCapacityField()), "viewRoute");

        contentPane.add(mainPanel, BorderLayout.CENTER);
        contentPane.add(new FooterPanel(), BorderLayout.SOUTH);

        cardLayout.show(mainPanel, "home");
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DeliveryApp::new);
    }
}