package org.kesler.fullyequip.gui.dialog.invoice;

import com.alee.extended.date.WebDateField;
import net.miginfocom.swing.MigLayout;
import org.apache.log4j.lf5.util.ResourceUtils;
import org.kesler.fullyequip.gui.dialog.AbstractDialog;
import org.kesler.fullyequip.logic.Contract;
import org.kesler.fullyequip.logic.Invoice;
import org.kesler.fullyequip.logic.InvoicePosition;
import org.kesler.fullyequip.util.ResourcesUtil;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
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
    private InvoicePosition selectedInvoicePosition;

    private JLabel totalLabel;


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
        invoicePositionsTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        invoicePositionsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                selectedInvoicePosition = invoicePositionsTableModel.getInvoicePosition(e.getFirstIndex());
            }
        });
        invoicePositionsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(e.getClickCount()==2) {
                    editInvoicePosition();
                }
            }
        });
        JScrollPane invoicePositionsTableScrollPane = new JScrollPane(invoicePositionsTable);

        final JButton addInvoicePositionButton = new JButton(ResourcesUtil.getIcon("add.png"));
        addInvoicePositionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addInvoicePosition();
            }
        });

        JButton editInvoicePositionButton = new JButton(ResourcesUtil.getIcon("pencil.png"));
        editInvoicePositionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editInvoicePosition();
            }
        });

        JButton removeInvoicePositionButton = new JButton(ResourcesUtil.getIcon("delete.png"));
        removeInvoicePositionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeInvoicePosition();
            }
        });

        totalLabel = new JLabel();

        // Собираем панель данных
        dataPanel.add(new JLabel("Договор: "));
        dataPanel.add(contractLabel, "push, w 300, wrap");
        dataPanel.add(new JLabel("Номер: "));
        dataPanel.add(numberTextField, "span, split 3");
        dataPanel.add(new JLabel(" от "));
        dataPanel.add(dateWebDateField, "wrap");
        dataPanel.add(invoicePositionsTableScrollPane, "span, grow");
        dataPanel.add(addInvoicePositionButton, "span, split 6");
        dataPanel.add(editInvoicePositionButton);
        dataPanel.add(removeInvoicePositionButton);
        dataPanel.add(new JLabel(), "growx");
        dataPanel.add(new JLabel("Итого по накладной: "));
        dataPanel.add(totalLabel);

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
        updateInvoicePositions();
    }



    protected boolean readInvoiceFromGUI() {
        invoice.setNumber(numberTextField.getText());
        invoice.setDate(dateWebDateField.getDate());

        return true;
    }

    private void updateInvoicePositions() {
        invoicePositionsTableModel.setInvoicePositions(new ArrayList<InvoicePosition>(invoice.getPositions()));
        updateTotal();

    }

    private void updateTotal() {
        totalLabel.setText("<html><strong color=blue>"+ NumberFormat.getInstance().format(invoice.computeTotal()) + " р.</strong><html>");
    }

    private void addInvoicePosition() {
        InvoicePositionDialog invoicePositionDialog = new InvoicePositionDialog(currentDialog, invoice);
        invoicePositionDialog.setVisible(true);
        if(invoicePositionDialog.getResult() == InvoicePositionDialog.OK) {
            invoice.getPositions().add(invoicePositionDialog.getInvoicePosition());
            updateInvoicePositions();
        }
    }

    private void editInvoicePosition() {
        if(selectedInvoicePosition==null) {
            JOptionPane.showMessageDialog(currentDialog, "Ничего не выбрано", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }
        InvoicePositionDialog invoicePositionDialog = new InvoicePositionDialog(currentDialog, selectedInvoicePosition);
        invoicePositionDialog.setVisible(true);
        if(invoicePositionDialog.getResult() == InvoicePositionDialog.OK) {
            updateInvoicePositions();
        }

    }

    private void removeInvoicePosition() {
        if(selectedInvoicePosition==null) {
            JOptionPane.showMessageDialog(currentDialog, "Ничего не выбрано", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int confirmResult = JOptionPane.showConfirmDialog(currentDialog,"Удалить позицию накладной и соответствующие записи об оборудовании?",
                "Внимание",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
        if(confirmResult==JOptionPane.YES_OPTION) {
            invoice.getPositions().remove(selectedInvoicePosition);
            updateInvoicePositions();
        }
    }

    class InvoicePositionsTableModel extends AbstractTableModel {
        private List<InvoicePosition> invoicePositions;

        InvoicePositionsTableModel() {
            invoicePositions = new ArrayList<InvoicePosition>();
        }

        public void setInvoicePositions(List<InvoicePosition> invoicePositions) {
            this.invoicePositions = invoicePositions;
            fireTableDataChanged();
        }

        InvoicePosition getInvoicePosition(int index) {
            return invoicePositions.get(index);
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
        public String getColumnName(int column) {
            switch (column) {
                case 0:
                    return "№";
                case 1:
                    return "Наименование";
                case 2:
                    return "Тип";
                case 3:
                    return "Цена";
                case 4:
                    return "Кол-во";
            }
            return "";
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
                    return invoicePosition.getUnitType();
                case 3:
                    return NumberFormat.getInstance().format(invoicePosition.getPrice());
                case 4:
                    return invoicePosition.getQuantity();

            }
            return null;
        }
    }

}
