package org.kesler.fullyequip.gui.equipment;

import com.alee.extended.list.CheckBoxListModel;
import com.alee.extended.list.WebCheckBoxList;
import net.miginfocom.swing.MigLayout;
import org.kesler.fullyequip.gui.dialog.AbstractDialog;
import org.kesler.fullyequip.gui.dialog.invoice.InvoicePositionDialog;
import org.kesler.fullyequip.logic.*;
import org.kesler.fullyequip.util.ResourcesUtil;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Форма для ввода, редактирования оборудования по договору
 */
public class ContractEquipDialog extends AbstractDialog {

    private Contract contract;

    private ContractEquipDialogController controller;

    private JLabel contractLabel;
    private InvoicesCheckBoxListModel invoicesCheckBoxListModel;
    private WebCheckBoxList invoiceWebCheckBoxList;
    private InvoicePositionsTableModel invoicePositionsTableModel;
    private JTable invoicePositionsTable;
    private InvoicePosition selectedInvoicePosition;

    private JLabel totalLabel;

    ContractEquipDialog(JDialog parentDialog, Contract contract, ContractEquipDialogController controller) {
        super(parentDialog, "Оборудование по договору", true);
        this.contract = contract;
        this.controller = controller;
        createGUI();
        loadGUIFromContract();
        setLocationRelativeTo(parentDialog);
    }

    ContractEquipDialog(JFrame parentFrame, Contract contract, ContractEquipDialogController controller) {
        super(parentFrame, "Оборудование по договору", true);
        this.contract = contract;
        this.controller = controller;
        createGUI();
        loadGUIFromContract();
        setLocationRelativeTo(parentFrame);
    }


    private void createGUI() {

        JPanel mainPanel = new JPanel(new BorderLayout());


        JPanel dataPanel = new JPanel(new MigLayout("fill"));

        Font bigFont = new Font("Arial", Font.BOLD, 14);

        contractLabel = new JLabel("Не определен");
        contractLabel.setFont(bigFont);
        contractLabel.setBorder(BorderFactory.createEtchedBorder());

        // Формируем список накладных
        invoicesCheckBoxListModel = new InvoicesCheckBoxListModel();
        invoiceWebCheckBoxList = new WebCheckBoxList(invoicesCheckBoxListModel);
        JScrollPane invoiceWebCheckBoxListScrollPane = new JScrollPane(invoiceWebCheckBoxList);
        invoiceWebCheckBoxList.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {}

            @Override
            public void intervalRemoved(ListDataEvent e) {}

            @Override
            public void contentsChanged(ListDataEvent e) {
                 updateInvoicePositions();
            }
        });
        invoiceWebCheckBoxList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(e.getClickCount()==2) {
                    editInvoice();
                 }
            }
        });

        // Кнопки управления списком накладных
        // Кнопка добавления накладной
        JButton addInvoiceButton = new JButton(ResourcesUtil.getIcon("add.png"));
        addInvoiceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addInvoice();
            }
        });

        // Кнопка редактирования накладной
        JButton editInvoiceButton = new JButton(ResourcesUtil.getIcon("pencil.png"));
        editInvoiceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editInvoice();
            }
        });

        // Кнопка удаления накладной
        JButton removeInvoiceButton = new JButton(ResourcesUtil.getIcon("delete.png"));
        removeInvoiceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeInvoice();
            }
        });

        // Формируем таблицу с оборудованием
        invoicePositionsTableModel = new InvoicePositionsTableModel();
        invoicePositionsTable = new JTable(invoicePositionsTableModel);
        // Устанавливаем ширину колонок
        for (int i=0; i< invoicePositionsTableModel.getColumnCount(); i++) {
            invoicePositionsTable.getColumnModel().getColumn(i).setPreferredWidth(invoicePositionsTableModel.getColumnWidth(i));
        }
        invoicePositionsTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        invoicePositionsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                selectedInvoicePosition = invoicePositionsTableModel.getInvoicePositionAt(e.getFirstIndex());
            }
        });
        invoicePositionsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() == 2) {
                    editInvoicePosition();
                }
            }
        });
        JScrollPane invoicePositionsScrollPane = new JScrollPane(invoicePositionsTable);

        totalLabel = new JLabel();

        dataPanel.add(new JLabel("Договор: "), "span, split 2");
        dataPanel.add(contractLabel, "growx,wrap");
        dataPanel.add(new JLabel("Накладные: "));
        dataPanel.add(invoiceWebCheckBoxListScrollPane, "pushx, growx, wrap");
        dataPanel.add(addInvoiceButton, "skip, span, split 3");
        dataPanel.add(editInvoiceButton);
        dataPanel.add(removeInvoiceButton, "wrap");
        dataPanel.add(new JLabel("Оборудование: "), "wrap");
        dataPanel.add(invoicePositionsScrollPane,"span, grow");

        dataPanel.add(new JLabel(),"span, split 3, growx");
        dataPanel.add(new JLabel("Итого по договору: "));
        dataPanel.add(totalLabel);

        JPanel buttonPanel = new JPanel();

        JButton okButton = new JButton("Сохранить");
        okButton.setIcon(ResourcesUtil.getIcon("accept.png"));
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            controller.saveContract();
            result = OK;
            setVisible(false);
            }
        });

        JButton cancelButton = new JButton("Отмена");
        cancelButton.setIcon(ResourcesUtil.getIcon("cancel.png"));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                result = CANCEL;
                setVisible(false);
            }
        });

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);


        mainPanel.add(dataPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);

        setPreferredSize(new Dimension(1000, 700));

        pack();

    }

    private void loadGUIFromContract() {
        contractLabel.setText("<html>" + contract.getDictName() + "</html>");
        invoicesCheckBoxListModel.updateInvoices();
        updateInvoicePositions();
    }

    // Добавление накладной
    private void addInvoice() {
        controller.addInvoice();
        invoicesCheckBoxListModel.updateInvoices();
        updateInvoicePositions();
    }

    // Редактирование накладной
    private void editInvoice() {
        int selectedIndex = invoiceWebCheckBoxList.getSelectedIndex();
        Invoice selectedInvoice = (Invoice)invoicesCheckBoxListModel.getElementAt(selectedIndex).getUserObject();

        if (controller.editInvoice(selectedInvoice)) {
            invoicesCheckBoxListModel.updatedInvoice(selectedIndex);
            updateInvoicePositions();
        }
    }

    // Удаление накладной
    private void removeInvoice() {
        int selectedIndex = invoiceWebCheckBoxList.getSelectedIndex();
        Invoice selectedInvoice = (Invoice)invoicesCheckBoxListModel.getElementAt(selectedIndex).getUserObject();
        if (controller.removeInvoice(selectedInvoice)) {
            invoicesCheckBoxListModel.removedInvoice(selectedIndex);
            updateInvoicePositions();
        }
    }


    private void editInvoicePosition() {
        if(selectedInvoicePosition==null) {
            JOptionPane.showMessageDialog(currentDialog, "Ничего не выбрано", "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        }
        InvoicePositionDialog invoicePositionDialog = new InvoicePositionDialog(currentDialog, selectedInvoicePosition);
        invoicePositionDialog.setVisible(true);
        if(invoicePositionDialog.getResult() == InvoicePositionDialog.OK) {
            updateInvoicePositions();
        }

    }

    private void updateInvoicePositions() {
        List<Invoice> invoices = invoicesCheckBoxListModel.getSelectedInvoices();
        List<InvoicePosition> invoicePositions = new ArrayList<InvoicePosition>();
        for(Invoice invoice: invoices) {
            invoicePositions.addAll(invoice.getPositions());
        }
        invoicePositionsTableModel.setInvoicePositions(invoicePositions);
        updateTotal();
    }

    private void updateTotal() {
        totalLabel.setText("<html><strong color=blue>" + NumberFormat.getInstance().format(contract.computeTotal()) + " р.</strong></html>");
    }


    // Модель данных для списка накладных
    class InvoicesCheckBoxListModel extends CheckBoxListModel  {

        InvoicesCheckBoxListModel() {
//            updateInvoices();
        }

        void updateInvoices() {
            removeAllElements();
            Set<Invoice> invoices = contract.getInvoices();
            for(Invoice invoice: invoices) {
                addCheckBoxElement(invoice,true);
            }

        }

        void updatedInvoice(int index) {
            fireContentsChanged(this, index, index);
        }

        void removedInvoice(int index) {
            remove(index);
        }

        List<Invoice> getSelectedInvoices() {
            List<Invoice> selectedInvoices = new ArrayList<Invoice>();
            for(int i=0; i < size(); i++) {
                if(isCheckBoxSelected(i)) {
                    Invoice invoice = (Invoice) get(i).getUserObject();
                    selectedInvoices.add(invoice);
                }
            }
            return selectedInvoices;
        }


    }

    // модель данных для таблицы Оборудование
    class InvoicePositionsTableModel extends AbstractTableModel {

        private List<InvoicePosition> invoicePositions;

        InvoicePositionsTableModel() {
           invoicePositions = new ArrayList<InvoicePosition>();
        }

        void setInvoicePositions(List<InvoicePosition> invoicePositions) {
            this.invoicePositions = invoicePositions;
            fireTableDataChanged();
        }

        InvoicePosition getInvoicePositionAt(int index) {
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

        public int getColumnWidth(int col) {
            int width = 50;
            switch (col) {
                case 0:
                    width = 5;
                    break;
                case 1:
                    width = 300;
                    break;
                case 2:
                    width = 100;
                    break;
                case 3:
                    width = 50;
                    break;
                case 4:
                    width = 20;
                    break;
            }

            return width;
        }

        @Override
        public String getColumnName(int col) {
            String name = "Не опр";

            switch (col) {
                case 0:
                    name = "№";
                    break;
                case 1:
                    name = "Наименование";
                    break;
                case 2:
                    name = "Тип";
                    break;
                case 3:
                    name = "Цена";
                    break;
                case 4:
                    name = "Кол-во";

            }

            return name;
        }

        @Override
        public String getValueAt(int row, int col) {

            InvoicePosition invoicePosition = invoicePositions.get(row);

            String value = "Не опр";
            switch (col) {
                case 0:
                    value = String.valueOf(row + 1);
                    break;
                case 1:
                    value = invoicePosition.getName();
                    break;
                case 2:
                    value = invoicePosition.getUnitType()==null?"Не опр":invoicePosition.getUnitType().getDictName();
                    break;
                case 3:
                    value = invoicePosition.getPrice()==null?"Не опр":NumberFormat.getInstance().format(invoicePosition.getPrice());
                    break;
                case 4:
                    value = invoicePosition.getQuantity()==null?"Не опр":invoicePosition.getQuantity().toString();
                    break;

            }

            return value;
        }

    }




}
