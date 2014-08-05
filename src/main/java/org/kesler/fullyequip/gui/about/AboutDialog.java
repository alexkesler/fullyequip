package org.kesler.fullyequip.gui.about;

import net.miginfocom.swing.MigLayout;
import org.kesler.fullyequip.Version;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AboutDialog extends JDialog{

    public AboutDialog(JFrame parentFrame) {
        super(parentFrame,"О программе", true);
        createGUI();
        setLocationRelativeTo(parentFrame);
    }

    private void createGUI() {
        JPanel mainPanel = new JPanel(new MigLayout());

        JLabel versionLabel = new JLabel(Version.getVersion());
        JLabel releaseLabel = new JLabel(Version.getReleaseDate());

        JButton okButton = new JButton("Ок");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

        mainPanel.add(new JLabel("Версия: "));
        mainPanel.add(versionLabel, "wrap");
        mainPanel.add(new JLabel("Дата сборки: "));
        mainPanel.add(releaseLabel,"wrap");
        mainPanel.add(okButton,"span, center");

        setContentPane(mainPanel);
        pack();
    }

    public void showDialog() {
        setVisible(true);
    }
}
