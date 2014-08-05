package org.kesler.fullyequip.gui.moving;

import com.alee.extended.date.WebDateField;
import net.miginfocom.swing.MigLayout;
import org.kesler.fullyequip.gui.dialog.AbstractDialog;
import org.kesler.fullyequip.logic.Place;
import org.kesler.fullyequip.logic.Unit;
import org.kesler.fullyequip.util.ResourcesUtil;


import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Диалог перемещения
 */
public class MovingDialog extends AbstractDialog{

    private MovingDialogController controller;

    private JLabel newPlaceLabel;
    private WebDateField moveDateField;
    private UnitsTableModel unitsTableModel;
    private JTable unitsTable;

    MovingDialog(JFrame parentFrame, MovingDialogController controller) {
        super(parentFrame,"Перемещение", true);
        this.controller = controller;
        createGUI();
        setSize(800,600);
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

        moveDateField = new WebDateField(new Date());

        unitsTableModel = new UnitsTableModel();
        unitsTable = new JTable(unitsTableModel);
        // Устанавливаем ширину колонок
        for (int i=0; i< unitsTableModel.getColumnCount(); i++) {
            unitsTable.getColumnModel().getColumn(i).setPreferredWidth(unitsTableModel.getColumnWidth(i));
        }
        unitsTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        unitsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==2) {
                    editUnit();
                }
            }
        });
        JScrollPane unitsTableScrollPane = new JScrollPane(unitsTable);

        JButton addUnitsButton = new JButton(ResourcesUtil.getIcon("add.png"));
        addUnitsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addUnits();
            }
        });

        JButton removeUnitButton = new JButton(ResourcesUtil.getIcon("delete.png"));
        removeUnitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeUnit();
            }
        });

        JButton printMoveReportButton = new JButton("Печать ведомости");


        dataPanel.add(new JLabel("Куда перемещаем: "), "span, split 3");
        dataPanel.add(newPlaceLabel, "pushx, growx");
        dataPanel.add(selectNewPlaceButton, "wrap");
        dataPanel.add(new JLabel("Дата перемещения: "), "span, split 2");
        dataPanel.add(moveDateField);
        dataPanel.add(new JLabel("Оборудование: "), "wrap");
        dataPanel.add(unitsTableScrollPane, "span, pushy, grow");
        dataPanel.add(addUnitsButton, " split 2");
        dataPanel.add(removeUnitButton);
        dataPanel.add(printMoveReportButton, "right");



        JPanel buttonPanel = new JPanel();

        JButton moveButton = new JButton("Переместить");
        moveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.move();
            }
        });
        JButton cancelButton = new JButton("Закрыть");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.closeDialog();
            }
        });

        buttonPanel.add(moveButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(dataPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }


    private void selectNewPlace() {
        controller.selectNewPlace();
    }

    void update() {
        Place place = controller.getNewPlace();
        newPlaceLabel.setText(place == null ? "Не опр" : place.toString());
        unitsTableModel.setMovingUnits(controller.getMovingUnits());
    }

    private void addUnits() {
        controller.addUnits();
    }

    private void editUnit() {
        int index = unitsTable.getSelectedRow();
        if (index < 0) {
            JOptionPane.showMessageDialog(currentDialog, "Ничего не выбрано", "Внимание", JOptionPane.WARNING_MESSAGE);
            return;
        }

        controller.editUnit(unitsTableModel.getUnitAt(index));
    }

    private void removeUnit() {
        int index = unitsTable.getSelectedRow();
        if (index < 0) {
            JOptionPane.showMessageDialog(currentDialog, "Ничего не выбрано", "Внимание", JOptionPane.WARNING_MESSAGE);
            return;
        }
        controller.removeUnit(unitsTableModel.getUnitAt(index));
    }



    Date getMovingDate() {
        return moveDateField.getDate();
    }

    class UnitsTableModel extends AbstractTableModel {

        private List<MovingUnit> movingUnits;

        UnitsTableModel() {
            movingUnits = new ArrayList<MovingUnit>();
        }

        public void setMovingUnits(Collection<MovingUnit> movingUnits) {
            this.movingUnits = new ArrayList<MovingUnit>(movingUnits);
            fireTableDataChanged();
        }

        Unit getUnitAt(int index) {
            return movingUnits.get(index).getUnit();
        }

        @Override
        public int getRowCount() {
            return movingUnits.size();
        }

        @Override
        public int getColumnCount() {
            return 6;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Object value = null;

            MovingUnit movingUnit = movingUnits.get(rowIndex);
            Unit unit = movingUnit.getUnit();

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
                    value = movingUnit.getQuantity();
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
                    width = 50;
                    break;
                case 5:
                    width = 150;
                    break;

            }

            return width;
        }
    }


}
