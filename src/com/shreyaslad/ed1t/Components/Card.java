package com.shreyaslad.ed1t.Components;

import javax.swing.*;
import java.awt.*;

public class Card extends Component {
    public JPanel panel;

    private JLabel label;

    public Card(String text) {
        label = new JLabel(text);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);

        panel = new JPanel();
        panel.setVisible(true);
        panel.show();

        panel.add(label);
    }

    public void setBounds(Rectangle r) {
        panel.setBounds(r);
    }

    public void setForeground(Color c) {
        label.setForeground(c);
    }

    public void setBackground(Color c) {
        panel.setBackground(c);
    }
}
