package org.kesler.fullyequip.gui.report;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.swing.JRViewer;
import org.kesler.fullyequip.logic.Contract;
import org.kesler.fullyequip.report.ReportCollectionFactory;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;

/**
 * Контроллер для управления диалогом формирования отчетов
 */
public class ReportDialogController {

    private static ReportDialogController instance = null;
    private ReportDialog dialog;

    public static synchronized ReportDialogController getInstance() {
        if(instance==null) instance = new ReportDialogController();
        return instance;
    }

    private ReportDialogController() {

    }

    public void showDialog(JFrame parentFrame) {
        dialog = new ReportDialog(parentFrame, this);
        dialog.setVisible(true);
    }

    void printReport() {
        List<Contract> contracts = ReportCollectionFactory.createContractCollection();

        String reportName = "/Users/alex/Developer/Java/FullyEquip/src/main/resources/reports/IncomeByContractsReport.jasper";

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(contracts);

        try {
            JasperPrint jp = JasperFillManager.fillReport(reportName,new HashMap(),dataSource);
            JRViewer jv = new JRViewer(jp);
            dialog.setReportPanel(jv);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dialog,"Ошибка: "+ex.getMessage(),"Ошибка",JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }


    }

}
