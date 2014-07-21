package org.kesler.fullyequip.gui.moving;

import net.miginfocom.swing.MigLayout;
import org.kesler.fullyequip.gui.dialog.AbstractDialog;
import org.kesler.fullyequip.gui.receive.CheckableUnit;
import org.kesler.fullyequip.logic.Place;
import org.kesler.fullyequip.logic.Unit;
import org.kesler.fullyequip.util.ResourcesUtil;


import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Диалог перемещения
 */
public class MovingDialog extends AbstractDialog{

    private MovingDialogController controller;

    private JLabel newPlaceLabel;
    private UnitsTableModel unitsTableModel;
    private JTable unitsTable;

    MovingDialog(JFrame parentFrame, MovingDialogController controller) {
        super(parentFrame,"Перемещение", true);
        this.controller = controller;
        createGUI();
        pack();
        setLocationRelativeTo(parentFrame);
    }

    private void createGUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel dataPanel = new JPanel(new MigLayout("fill"));

        newPlaceLabel = new JLabel("Не опр");
        newPlaceLabel.setBorder(BorderFactory.createEtchedBorder());

        JButton selectNewPlaceButton = new JButton(ResourcesUtil.getIcon("book_previous.png"));
        selectNewPlaceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectNewPlace();
            }
        });

        unitsTableModel = new UnitsTableModel();
        unitsTable = new JTable(unitsTableModel);
        // Устанавливаем ширину колонок
        for (int i=0; i< unitsTableModel.getColumnCount(); i++) {
            unitsTable.getColumnModel().getColumn(i).setPreferredWidth(unitsTableModel.getColumnWidth(i));
        }
        JScrollPane unitsTableScrollPane = new JScrollPane(unitsTable);

        JButton addUnitsButton = new JButton(ResourcesUtil.getIcon("add.png"));
        addUnitsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addUnits();
            }
        });
        JButton removeUnitsButton = new JButton(ResourcesUtil.getIcon("delete.png"));
        JButton printMoveReportButton = new JButton("Печать ведомости");


        dataPanel.add(new JLabel("Куда перемещаем: "), "span, split 3");
        dataPanel.add(newPlaceLabel, "pushx, growx");
        dataPanel.add(selectNewPlaceButton, "wrap");
        dataPanel.add(new JLabel("Оборудование: "), "wrap");
        dataPanel.add(unitsTableScrollPane, "span, pushy, grow");
        dataPanel.add(addUnitsButton, " split 2");
        dataPanel.add(removeUnitsButton);
        dataPanel.add(printMoveReportButton, "right");



        JPanel buttonPanel = new JPanel();

        JButton moveButton = new JButton("Переместить");
        JButton cancelButton = new JButton("Отмена");

        buttonPanel.add(moveButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(dataPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }


    private void selectNewPlace() {
        controller.selectNewPlace();
        Place place = controller.getMoving().getNewPlace();
        newPlaceLabel.setText(place == null ? "Не опр" : place.toString());

    }

    private void addUnits() {
        List<Unit> units = controller.getAddingUnits();
        unitsTableModel.addUnits(units);
    }

    class UnitsTableModel extends AbstractTableModel {

        private List<CheckableUnit> checkableUnits;

        UnitsTableModel() {
            checkableUnits = new ArrayList<CheckableUnit>();
        }

        public void addUnits(List<Unit> units) {
            ArrayList<CheckableUnit> newCheckableUnits = new ArrayList<CheckableUnit>();
            for(Unit unit:units) {
                CheckableUnit newCheckableUnit = null;
                for(CheckableUnit checkableUnit : checkableUnits) {
                    if (unit.equals(checkableUnit.getUnit())) newCheckableUnit = checkableUnit;
                    break;
                }
                if (newCheckableUnit ==null) newCheckableUnit = new CheckableUnit(unit,false);
                newCheckableUnits.add(newCheckableUnit);
            }
            checkableUnits = newCheckableUnits;
            fireTableDataChanged();
        }

        @Override
        public int getRowCount() {
            return checkableUnits.size();
        }

        @Override
        public int getColumnCount() {
            return 6;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Object value = null;

            CheckableUnit checkableUnit = checkableUnits.get(rowIndex);
            Unit unit = checkableUnit.getUnit();

            switch (columnIndex) {
                case 0:
                    value = rowIndex+1;
                    break;
                case 1:
                    value = unit.getName();
                    break;
                case 2:
                    value = unit.getType().getName();
                    break;
                case 3:
                    value = unit.getInvNumber();
                    break;
                case 4:
                    value = unit.getQuantity();
                    break;
                case 5:
                    value = unit.getPlace().getName();
                    break;
            }


            return value;
        }

        @Override
        public String getColumnName(int column) {
            String name = "Не опр";

            switch (column) {
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
                    name = "Инв номер";
                    break;
                case 4:
                    name = "Кол-во";
                    break;
                case 5:
                    name = "Размещение";
                    break;
            }
            return name;
        }

        int getColumnWidth(int column) {
            int width = 50;

            switch (column) {
                case 0:
                    width = 20;
                    break;
                case 1:
                    width = 200;
                    break;
                case 2:
                    width = 100;
                    break;
                case 3:
                    width = 100;
                    break;
                case 4:
                    width = 20;
                    break;
                case 5:
                    width = 150;
                    break;

            }

            return width;
        }
    }


}
