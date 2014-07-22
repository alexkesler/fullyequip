package org.kesler.fullyequip.gui.dialog.invoice;

import net.miginfocom.swing.MigLayout;
import org.kesler.fullyequip.gui.dialog.AbstractDialog;
import org.kesler.fullyequip.gui.dialog.unit.UnitDialog;
import org.kesler.fullyequip.logic.InvoicePosition;
import org.kesler.fullyequip.logic.Place;
import org.kesler.fullyequip.logic.Unit;
import org.kesler.fullyequip.logic.UnitState;
import org.kesler.fullyequip.logic.model.PlaceModel;
import org.kesler.fullyequip.logic.model.UnitStateModel;


import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import java.awt.BorderLayout;
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

    private void createUnits() {
        PlaceModel placeModel = PlaceModel.getInstance();
        Place initialPlace = placeModel.getInitialPlace();
        UnitStateModel unitStateModel = UnitStateModel.getInstance();
        UnitState unitState = unitStateModel.getInitialState();
        if(invoicePosition.isInvReg() && invoicePosition.getUnits().size()==0) {
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
        }
    }

    private void createGUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel dataPanel = new JPanel(new MigLayout("fill"));

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

    void editUnit() {
        if(selectedUnit==null) {
            JOptionPane.showMessageDialog(currentDialog,"Ничего не выбрано", "Внимание", JOptionPane.WARNING_MESSAGE);
            return;
        }
        UnitDialog unitDialog = new UnitDialog(currentDialog, selectedUnit);
        unitDialog.setVisible(true);
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
            return 4;
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
