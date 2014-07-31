package org.kesler.fullyequip.gui.dialog.invoice;

import net.miginfocom.swing.MigLayout;
import org.kesler.fullyequip.gui.dialog.AbstractDialog;
import org.kesler.fullyequip.gui.dialog.ListDialogFactory;
import org.kesler.fullyequip.gui.dialog.unit.UnitDialogController;
import org.kesler.fullyequip.logic.InvoicePosition;
import org.kesler.fullyequip.logic.Place;
import org.kesler.fullyequip.logic.Unit;
import org.kesler.fullyequip.logic.UnitState;
import org.kesler.fullyequip.logic.model.PlacesModel;
import org.kesler.fullyequip.logic.model.UnitStatesModel;
import org.kesler.fullyequip.util.ResourcesUtil;


import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Диалог редактирования единиц оборудования по определенной позиции
 */
public class InvoicePositionUnitsDialog extends AbstractDialog {

    private InvoicePosition invoicePosition;
    private UnitsTableModel unitsTableModel;

    private Unit selectedUnit;

    public InvoicePositionUnitsDialog(JDialog parentDialog, InvoicePosition invoicePosition) {
        super(parentDialog,"Оборудование по позиции накладной", true);
        this.invoicePosition = invoicePosition;
        createUnits();
        createGUI();
        setLocationRelativeTo(parentDialog);
    }

    private void createGUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel dataPanel = new JPanel(new MigLayout("fill"));

        JButton selectPlaceButton = new JButton(ResourcesUtil.getIcon("building.png"));
        selectPlaceButton.setToolTipText("Выбрать размещение");
        selectPlaceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectPlace();
            }
        });

        JButton processInvNumbersButton = new JButton(ResourcesUtil.getIcon("table_lightning.png"));
        processInvNumbersButton.setToolTipText("Назначить инвентарные");
        processInvNumbersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processInvNumbers();
            }
        });


        unitsTableModel = new UnitsTableModel();
        JTable unitsTable = new JTable(unitsTableModel);
        unitsTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        unitsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                selectedUnit = unitsTableModel.getUnitAt(e.getFirstIndex());
            }
        });
        unitsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==2) {
                    editUnit();
                }
            }
        });
        JScrollPane unitsTableScrollPane = new JScrollPane(unitsTable);


        dataPanel.add(new JLabel(), "span, split 3, growx");
        dataPanel.add(selectPlaceButton);
        dataPanel.add(processInvNumbersButton);
        dataPanel.add(unitsTableScrollPane, "grow, span");

        JPanel buttonPanel = new JPanel();

        JButton okButton = new JButton("Ок");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

        buttonPanel.add(okButton);

        mainPanel.add(dataPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        pack();
    }

    private void createUnits() {
        PlacesModel placesModel = PlacesModel.getInstance();
        Place initialPlace = placesModel.getInitialPlace();
        UnitStatesModel unitStatesModel = UnitStatesModel.getInstance();
        UnitState unitState = unitStatesModel.getInitialState();
        if (invoicePosition.getUnits().size()==0) {
            if(invoicePosition.isInvReg()) {
                for(int i=0;i<invoicePosition.getQuantity();i++) {
                    Unit unit = new Unit();
                    unit.setInvoicePosition(invoicePosition);
                    unit.setName(invoicePosition.getName());
                    unit.setPrice(invoicePosition.getPrice());
                    unit.setType(invoicePosition.getUnitType());
                    unit.setPlace(initialPlace);
                    unit.setState(unitState);
                    unit.setQuantity(1L);
                    invoicePosition.getUnits().add(unit);
                }
            } else {
                Unit unit = new Unit();
                unit.setInvoicePosition(invoicePosition);
                unit.setName(invoicePosition.getName());
                unit.setPrice(invoicePosition.getPrice());
                unit.setType(invoicePosition.getUnitType());
                unit.setPlace(initialPlace);
                unit.setState(unitState);
                unit.setQuantity(invoicePosition.getQuantity());
                invoicePosition.getUnits().add(unit);
            }

        }
    }

    private void processInvNumbers()  {


        String invString = JOptionPane.showInputDialog(currentDialog,
                "Введите начальный инвентарный номер",
                "Назначение инвентарных",
                JOptionPane.QUESTION_MESSAGE);
//        JOptionPane.showMessageDialog(currentDialog,"Введенное значение: " + invString);

        Long invNumber = null;
        try {
            invNumber = Long.decode(invString);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(currentDialog,"Инвентарный номер должен быть числом","Ошибка",JOptionPane.ERROR_MESSAGE);
            return;
        }

//        if (invNumber == null) {
//            JOptionPane.showMessageDialog(currentDialog,"Инвентарный номер должен быть числом","Ошибка",JOptionPane.ERROR_MESSAGE);
//            return;
//        }


        for (Unit unit: invoicePosition.getUnits()) {
            unit.setInvNumber(String.valueOf(invNumber++));
        }

        unitsTableModel.updateUnits();
    }

    private void selectPlace() {
        Place selectedPlace = ListDialogFactory.showSelectPlaceListDialog(currentDialog);
        if (selectedPlace!=null) {
            for (Unit unit: invoicePosition.getUnits()) {
                unit.setPlace(selectedPlace);
            }
            unitsTableModel.updateUnits();
        }
    }

    void editUnit() {
        if(selectedUnit==null) {
            JOptionPane.showMessageDialog(currentDialog,"Ничего не выбрано", "Внимание", JOptionPane.WARNING_MESSAGE);
            return;
        }
        UnitDialogController.getInstance().showDialog(currentDialog, selectedUnit);
        unitsTableModel.updateUnits();
    }

    class UnitsTableModel extends AbstractTableModel {
        private List<Unit> units;

        UnitsTableModel() {
            units = new ArrayList<Unit>(invoicePosition.getUnits());
        }

        Unit getUnitAt(int index) {
            return units.get(index);
        }

        void updateUnits() {
            units = new ArrayList<Unit>(invoicePosition.getUnits());
            fireTableDataChanged();
        }


        @Override
        public int getRowCount() {
            return units.size();
        }

        @Override
        public int getColumnCount() {
            return 5;
        }

        @Override
        public String getColumnName(int column) {
            switch (column) {
                case 0:
                    return "Модель";
                case 1:
                    return "Тип";
                case 2:
                    return "Инв номер";
                case 3:
                    return "Размещение";
                case 4:
                    return "Кол-во";
            }
            return "";
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Unit unit = units.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return unit.getName();
                case 1:
                    return unit.getTypeName();
                case 2:
                    return unit.getInvNumber();
                case 3:
                    return unit.getPlaceName();
                case 4:
                    return unit.getQuantity();

            }
            return null;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex==2;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if(columnIndex==2) {
                units.get(rowIndex).setInvNumber((String)aValue);
            }
        }
    }
}
