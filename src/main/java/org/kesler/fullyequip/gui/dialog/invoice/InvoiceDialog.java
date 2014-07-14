package org.kesler.fullyequip.gui.dialog.invoice;

import com.alee.extended.date.WebDateField;
import net.miginfocom.swing.MigLayout;
import org.kesler.fullyequip.gui.dialog.AbstractDialog;
import org.kesler.fullyequip.logic.Contract;
import org.kesler.fullyequip.logic.Invoice;
import org.kesler.fullyequip.logic.InvoicePosition;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

/**
 * Диалог для управления накладными
 */
public class InvoiceDialog extends AbstractDialog {

    private Invoice invoice;

    private JLabel contractLabel;
    private JTextField numberTextField;
    private WebDateField dateWebDateField;

    private InvoicePositionsTableModel invoicePositionsTableModel;


    public InvoiceDialog(JDialog parentDialog, Contract contract) {
        super(parentDialog,"Накладная", true);
        createNewItem();
        invoice.setContract(contract);
        createGUI();
        loadGUIFromInvoice();
        pack();
        setLocationRelativeTo(parentDialog);
    }

    public InvoiceDialog(JDialog parentDialog, Invoice invoice) {
        super(parentDialog,"Накладная", true);
        this.invoice = invoice;
        createGUI();
        loadGUIFromInvoice();
        pack();
        setLocationRelativeTo(parentDialog);
    }


    public Invoice getInvoice() {
        return invoice;
    }

    private void createNewItem() {
        invoice = new Invoice();
        invoice.setDate(new Date());
    }



    private void createGUI() {

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Панель данных
        JPanel dataPanel = new JPanel(new MigLayout("fill"));

        contractLabel = new JLabel("Не определен");
        contractLabel.setBorder(BorderFactory.createEtchedBorder());

        numberTextField = new JTextField(10);

        dateWebDateField = new WebDateField(new Date());

        invoicePositionsTableModel = new InvoicePositionsTableModel();
        JTable invoicePositionsTable = new JTable(invoicePositionsTableModel);
        JScrollPane invoicePositionsTableScrollPane = new JScrollPane(invoicePositionsTable);

        // Собираем панель данных
        dataPanel.add(new JLabel("Договор: "));
        dataPanel.add(contractLabel, "push, w 300, wrap");
        dataPanel.add(new JLabel("Номер: "));
        dataPanel.add(numberTextField, "span, split 3");
        dataPanel.add(new JLabel(" от "));
        dataPanel.add(dateWebDateField, "wrap");
        dataPanel.add(invoicePositionsTableScrollPane, "span, grow");

        //  Панель кнопок
        JPanel buttonPanel = new JPanel();

        JButton okButton = new JButton("Ок");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (readInvoiceFromGUI()) {
                    result = OK;
                }
                setVisible(false);
            }
        });

        JButton cancelButton = new JButton("Отмена");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

        buttonPanel.add(okButton);

        mainPanel.add(dataPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);


    }


    protected void loadGUIFromInvoice() {
        String contractName = invoice.getContract()==null?"Не определен":invoice.getContract().toString();
        contractLabel.setText("<html>"+contractName+"</html>");
        numberTextField.setText(invoice.getNumber());
        dateWebDateField.setDate(invoice.getDate());
        invoicePositionsTableModel.setInvoicePositions(new ArrayList<InvoicePosition>(invoice.getPositions()));
    }


    protected boolean readInvoiceFromGUI() {
        invoice.setNumber(numberTextField.getText());
        invoice.setDate(dateWebDateField.getDate());

        return true;
    }

    class InvoicePositionsTableModel extends AbstractTableModel {
        private java.util.List<InvoicePosition> invoicePositions;

        InvoicePositionsTableModel() {
            invoicePositions = new ArrayList<InvoicePosition>();
        }

        public void setInvoicePositions(List<InvoicePosition> invoicePositions) {
            this.invoicePositions = invoicePositions;
            fireTableDataChanged();
        }

        @Override
        public int getRowCount() {
            return invoicePositions.size();
        }

        @Override
        public int getColumnCount() {
            return 5;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            InvoicePosition invoicePosition = invoicePositions.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return rowIndex + 1;
                case 1:
                    return invoicePosition.getName();
                case 2:
                    return invoicePosition.getUnitType().getName();
                case 3:
                    return invoicePosition.getPrice();
                case 4:
                    return invoicePosition.getQuantity();

            }
            return null;
        }
    }

}
