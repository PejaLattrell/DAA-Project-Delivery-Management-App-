package package1;

import javax.swing.*;
import java.awt.*;

public class FooterPanel extends JPanel {
    public FooterPanel() {
        setBackground(new Color(52, 73, 94));
        setPreferredSize(new Dimension(900, 30));
        JLabel footerLabel = new JLabel("© 2025 iShop - All Rights Reserved", SwingConstants.CENTER);
        footerLabel.setForeground(Color.WHITE);
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        add(footerLabel);
    }
}