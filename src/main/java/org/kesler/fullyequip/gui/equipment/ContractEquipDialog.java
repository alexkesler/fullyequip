package org.kesler.fullyequip.gui.equipment;

import com.alee.extended.list.CheckBoxListModel;
import com.alee.extended.list.WebCheckBoxList;
import net.miginfocom.swing.MigLayout;
import org.kesler.fullyequip.gui.dialog.AbstractDialog;
import org.kesler.fullyequip.gui.dialog.ListDialogController;
import org.kesler.fullyequip.logic.*;
import org.kesler.fullyequip.util.ResourcesUtil;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.table.AbstractTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Форма для ввода, редактирования оборудования по договору
 */
public class ContractEquipDialog extends AbstractDialog {

    private Contract contract;
    private Place defaultPlace;

    private ContractEquipDialogController controller;

    private JLabel contractLabel;
    private InvoicesCheckBoxListModel invoicesCheckBoxListModel;
    private WebCheckBoxList invoiceWebCheckBoxList;
    private JLabel defaultPlaceLabel;
    private InvoicePositionsTableModel invoicePositionsTableModel;
    private JTable invoicePositionsTable;

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
//        contractLabel.setForeground(Color.GREEN);
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


        defaultPlaceLabel = new JLabel("Не определено");
        defaultPlaceLabel.setBorder(BorderFactory.createEtchedBorder());

        JButton selectDefaultPlaceButton = new JButton(ResourcesUtil.getIcon("book_previous.png"));
        selectDefaultPlaceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectDefaultPlace();
            }
        });

        // Формируем таблицу с оборудованием
        invoicePositionsTableModel = new InvoicePositionsTableModel();
        invoicePositionsTable = new JTable(invoicePositionsTableModel);
        // Устанавливаем ширину колонок
        for (int i=0; i< invoicePositionsTableModel.getColumnCount(); i++) {
            invoicePositionsTable.getColumnModel().getColumn(i).setPreferredWidth(invoicePositionsTableModel.getColumnWidth(i));
        }
        invoicePositionsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() == 2) {
//                    editUnit();
                }
            }
        });
        JScrollPane unitsTableScrollPane = new JScrollPane(invoicePositionsTable);

//        // Кнопки управления списком оборудования
//        JButton addUnitButton = new JButton(ResourcesUtil.getIcon("add.png"));
//        addUnitButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                addUnit();
//            }
//        });
//
//        JButton editUnitButton = new JButton(ResourcesUtil.getIcon("pencil.png"));
//        editUnitButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                editUnit();
//            }
//        });
//
//        JButton removeUnitButton = new JButton(ResourcesUtil.getIcon("delete.png"));
//        removeUnitButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                removeUnit();
//            }
//        });


        dataPanel.add(new JLabel("Договор: "), "span, split 2");
        dataPanel.add(contractLabel, "growx,wrap");
        dataPanel.add(new JLabel("Накладные: "));
        dataPanel.add(invoiceWebCheckBoxListScrollPane, "pushx, growx, wrap");
        dataPanel.add(addInvoiceButton, "skip, span, split 3");
        dataPanel.add(editInvoiceButton);
        dataPanel.add(removeInvoiceButton, "wrap");
        dataPanel.add(new JLabel("Размещение: "));
        dataPanel.add(defaultPlaceLabel, "growx");
        dataPanel.add(selectDefaultPlaceButton, "wrap");
        dataPanel.add(new JLabel("Оборудование: "), "wrap");
        dataPanel.add(unitsTableScrollPane,"span, grow");
//        dataPanel.add(addUnitButton, "span, split 3");
//        dataPanel.add(editUnitButton);
//        dataPanel.add(removeUnitButton);


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
        contractLabel.setText("<html>" + contract.toString() + "</html>");
        invoicesCheckBoxListModel.updateInvoices();
        updateInvoicePositions();
    }

    private void selectDefaultPlace() {
        defaultPlace = ListDialogController.create(Place.class, "Размещения").showSelectDialog(currentDialog);
        defaultPlaceLabel.setText(defaultPlace==null?"Не определено":defaultPlace.toString());
    }


    // Добавление накладной
    private void addInvoice() {
        controller.addInvoice();
        invoicesCheckBoxListModel.updateInvoices();
    }

    // Редактирование накладной
    private void editInvoice() {
        int selectedIndex = invoiceWebCheckBoxList.getSelectedIndex();
        Invoice selectedInvoice = (Invoice)invoicesCheckBoxListModel.getElementAt(selectedIndex).getUserObject();

        if (controller.editInvoice(selectedInvoice)) {
            invoicesCheckBoxListModel.updatedInvoice(selectedIndex);
        }
    }

    // Удаление накладной
    private void removeInvoice() {
        int selectedIndex = invoiceWebCheckBoxList.getSelectedIndex();
        Invoice selectedInvoice = (Invoice)invoicesCheckBoxListModel.getElementAt(selectedIndex).getUserObject();
        if (controller.removeInvoice(selectedInvoice)) {
            invoicesCheckBoxListModel.removedInvoice(selectedIndex);
        }
    }

//    // Добавление оборудования
//    private void addUnit() {
//        List<Invoice> selectedInvoices = invoicesCheckBoxListModel.getSelectedInvoices();
//        if(selectedInvoices.size()!=1) {
//            JOptionPane.showMessageDialog(currentDialog,
//                    "Выберите одну накладную, по которой добавляется оборудование",
//                    "Внимание",
//                    JOptionPane.WARNING_MESSAGE);
//            return;
//        }
//
//        Invoice invoice = selectedInvoices.get(0);
//
//        if(defaultPlace == null) {
//            JOptionPane.showMessageDialog(currentDialog,
//                    "Выберите место для размещения оборудования",
//                    "Внимание",
//                    JOptionPane.INFORMATION_MESSAGE);
//            selectDefaultPlace();
//        }
//
//        UnitDialog unitDialog = new UnitDialog(currentDialog,invoice,defaultPlace);
//        unitDialog.setVisible(true);
//        if(unitDialog.getResult()==UnitDialog.OK) {
//            Unit unit = unitDialog.getItem();
//            invoice.getUnits().add(unit);
//            updateInvoicePositions();
//        }
//    }
//
//    // Редактирвание оборудования
//    private void editUnit() {
//        int selectedIndex = invoicePositionsTable.getSelectedRow();
//        if(selectedIndex== -1) {
//            JOptionPane.showMessageDialog(currentDialog, "Ничего не выбрано");
//            return;
//        }
//        Unit unit = unitsTableModel.getInvoicePositionAt(selectedIndex);
//        UnitDialog unitDialog = new UnitDialog(currentDialog, unit);
//        unitDialog.setVisible(true);
//        if(unitDialog.getResult()==UnitDialog.OK) {
//            unitsTableModel.unitUpdated(selectedIndex);
//        }
//    }
//
//    // Удаление оборудования
//    private void removeUnit() {
//        int selectedIndex = invoicePositionsTable.getSelectedRow();
//        if(selectedIndex== -1) {
//            JOptionPane.showMessageDialog(currentDialog, "Ничего не выбрано");
//            return;
//        }
//
//        Unit unit = unitsTableModel.getInvoicePositionAt(selectedIndex);
//
//        int result = JOptionPane.showConfirmDialog(currentDialog,
//                "Удалить оборудование " + unit.toString(),
//                "Внимание",
//                JOptionPane.YES_NO_OPTION,
//                JOptionPane.WARNING_MESSAGE);
//        if (result == JOptionPane.OK_OPTION) {
//            Invoice invoice = unit.getInvoice();
//            invoice.getUnits().remove(unit);
//            unitsTableModel.unitRemoved(selectedIndex);
//        }
//
//
//
//    }

    //
//    private void moveUnitToInvoice() {
//        int selectedIndex = invoicePositionsTable.getSelectedRow();
//        if(selectedIndex== -1) {
//            JOptionPane.showMessageDialog(currentDialog, "Ничего не выбрано");
//            return;
//        }
//
//        InvoicePosition unit = invoicePositionsTableModel.getInvoicePositionAt(selectedIndex);
//
//    }


    private void updateInvoicePositions() {
        List<Invoice> invoices = invoicesCheckBoxListModel.getSelectedInvoices();
        List<InvoicePosition> invoicePositions = new ArrayList<InvoicePosition>();
        for(Invoice invoice: invoices) {
            invoicePositions.addAll(invoice.getPositions());
        }
        invoicePositionsTableModel.setInvoicePositions(invoicePositions);
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

//        void unitUpdated(int index) {
//            fireTableRowsUpdated(index,index);
//        }
//
//        void unitRemoved(int index) {
//            fireTableRowsDeleted(index,index);
//        }

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
                    value = invoicePosition.getUnitType()==null?"Не опр":invoicePosition.getUnitType().toString();
                    break;
                case 3:
                    value = invoicePosition.getPrice()==null?"Не опр":invoicePosition.getPrice().toString();
                    break;
                case 4:
                    value = invoicePosition.getQuantity()==null?"Не опр":invoicePosition.getQuantity().toString();
                    break;

            }

            return value;
        }

    }




}
