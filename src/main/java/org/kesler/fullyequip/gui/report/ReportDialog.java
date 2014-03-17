package org.kesler.fullyequip.gui.report;

import net.miginfocom.swing.MigLayout;
import org.kesler.fullyequip.gui.dialog.AbstractDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Диалог для отображения формы печати
 */
public class ReportDialog extends AbstractDialog{

    private ReportDialogController controller;
    private JPanel mainPanel;

    public ReportDialog(JFrame parentFrame, ReportDialogController controller) {
        super(parentFrame,"Отчеты", true);
        this.controller = controller;
        createGUI();
        pack();
        setLocationRelativeTo(parentFrame);
    }

    private void createGUI() {

        mainPanel = new JPanel(new BorderLayout());

        JPanel buttonPanel = new JPanel(new MigLayout("fill"));

        JButton printReportButton = new JButton("Распечатать первый отчет");
        printReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.printReport();
            }
        });

        buttonPanel.add(printReportButton);



        mainPanel.add(buttonPanel, BorderLayout.NORTH);


        setContentPane(mainPanel);

    }

    void setReportPanel(JPanel reportPanel) {
        mainPanel.add(reportPanel,BorderLayout.CENTER);
        pack();
    }

}
