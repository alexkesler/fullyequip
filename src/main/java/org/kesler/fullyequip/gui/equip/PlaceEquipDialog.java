package org.kesler.fullyequip.gui.equip;

import net.miginfocom.swing.MigLayout;
import org.kesler.fullyequip.gui.dialog.AbstractDialog;
import org.kesler.fullyequip.gui.moving.MovingUnit;
import org.kesler.fullyequip.logic.Place;
import org.kesler.fullyequip.logic.Unit;
import org.kesler.fullyequip.util.ResourcesUtil;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Окно для отображения оснащения Филиала (содержимое склада)
 */
public class PlaceEquipDialog extends AbstractDialog{

    private PlaceEquipDialogController controller;

    private JList<Place> placesList;
    private PlacesListModel placesListModel;
    private JTable unitsTable;
    private UnitsTableModel unitsTableModel;

    PlaceEquipDialog(JDialog parentDialog, PlaceEquipDialogController controller) {
        super(parentDialog, "Наличие оборудования", true);
        this.controller = controller;
        createGUI();
        pack();
        setLocationRelativeTo(parentDialog);

    }

    PlaceEquipDialog(JFrame parentFrame, PlaceEquipDialogController controller) {
        super(parentFrame, "Наличие оборудования", true);
        this.controller = controller;
        createGUI();
        pack();
        setLocationRelativeTo(parentFrame);
    }

    /**
     * Возвращает выбранное оборудование
     * @return выбранные единицы оборудования
     */
    List<MovingUnit> getSelectedMovingUnits() {
        return unitsTableModel.getCheckedMovingUnits();
    }

    private void createGUI() {

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel dataPanel = new JPanel(new MigLayout("fill"));

        placesListModel = new PlacesListModel();
        placesList = new JList<Place>(placesListModel);
        JScrollPane placesListScrollPane = new JScrollPane(placesList);

        JButton addPlaceButton = new JButton(ResourcesUtil.getIcon("add.png"));
        addPlaceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPlace();
            }
        });
        JButton removePlaceButton = new JButton(ResourcesUtil.getIcon("delete.png"));
        removePlaceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removePlace();
            }
        });

        unitsTableModel = new UnitsTableModel();
        unitsTable = new JTable(unitsTableModel);
        // Устанавливаем ширину колонок
        for (int i=0; i< unitsTableModel.getColumnCount(); i++) {
            unitsTable.getColumnModel().getColumn(i).setPreferredWidth(unitsTableModel.getColumnWidth(i));
        }
        unitsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int col = unitsTable.columnAtPoint(e.getPoint());
                if (col==0) {
                    int row = unitsTable.rowAtPoint(e.getPoint());
                    CheckableUnit checkableUnit = unitsTableModel.getCheckableUnitAt(row);
                    if (checkableUnit.getChecked()&& checkableUnit.getQuantity() > 1) {
                        String newQuantityString = JOptionPane.showInputDialog(currentDialog,
                                "Ведите количество единиц оборудования",
                                checkableUnit.getQuantity());
                        if (newQuantityString==null) return;
                        Long newQuantity = null;
                        try {
                            newQuantity = Long.decode(newQuantityString);
                        } catch (NumberFormatException e1) {
                            JOptionPane.showMessageDialog(currentDialog,
                                    "Необходимо ввести число",
                                    "Ошибка",
                                    JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        if (newQuantity > checkableUnit.getQuantity()) {
                            JOptionPane.showMessageDialog(currentDialog,
                                    "Число больше имеющихся в наличии",
                                    "Ошибка",
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        checkableUnit.setQuantity(newQuantity);
                    }
                }
            }
        });

        JScrollPane unitsTableScrollPane = new JScrollPane(unitsTable);


        /// Собираем панель данных
        dataPanel.add(new JLabel("Размещения"));
        dataPanel.add(placesListScrollPane, "pushx, growx, wrap");
        dataPanel.add(addPlaceButton,"skip,span,split 2");
        dataPanel.add(removePlaceButton);
        dataPanel.add(new JLabel("Оборудование"), "wrap");
        dataPanel.add(unitsTableScrollPane, "span, pushy, grow");

        // Панель кнопок
        JPanel buttonPanel = new JPanel();

        JButton okButton = new JButton("Выбрать");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                result = OK;
                setVisible(false);
            }
        });

        JButton cancelButton = new JButton("Отмена");
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

    }

    private void addPlace() {
        Place place = controller.selectPlace();
        if (place != null) {
            placesListModel.addPlace(place);
            updateUnits();
        }

    }

    private void removePlace() {
        int selectedIndex = placesList.getSelectedIndex();
        if(selectedIndex== -1) {
            JOptionPane.showMessageDialog(currentDialog,
                    "Размещение не выбрано",
                    "Внимание",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        placesListModel.removePlace(selectedIndex);
        updateUnits();

    }

    /**
     * Обновляет список оборудования для выбранных размещений
     */
    private void updateUnits() {
        List<Unit> units = new ArrayList<Unit>();
        for(Place place:placesListModel.getPlaces()) {
            units.addAll(place.getUnits());
        }
        unitsTableModel.setUnits(units);
    }

    /**
     * Модель управления списком размещений
     */
    class PlacesListModel extends AbstractListModel<Place> {

        private List<Place> places;

        PlacesListModel() {
            places = new ArrayList<Place>();
        }

        List<Place> getPlaces() {
            return places;
        }

        void addPlace(Place place) {
            if(!places.contains(place)) {
                places.add(place);
                fireIntervalAdded(this,places.size()-1,places.size()-1);
            }

        }

        void removePlace(int index) {
            if (index!= -1) {
                places.remove(index);
                fireIntervalRemoved(this,index,index);
            }

        }


        @Override
        public int getSize() {
            return places.size();
        }

        @Override
        public Place getElementAt(int index) {
            return places.get(index);
        }
    }


    /**
     * Модель уравления таблицей с оборудованием
     */
    class UnitsTableModel extends AbstractTableModel {

        private List<CheckableUnit> checkableUnits;


        UnitsTableModel() {
            checkableUnits = new ArrayList<CheckableUnit>();
        }

        /**
         * Устанавливает набор блоков. Новые блоки не выделяются, старые остаются без изменений.
         * @param units набор блоков,который необходимо установить
         */
        void setUnits(List<Unit> units) {
            ArrayList<CheckableUnit> newCheckableUnits = new ArrayList<CheckableUnit>();
            for(Unit unit:units) {
                CheckableUnit newCheckableUnit = null;
                for(CheckableUnit checkableUnit : checkableUnits) {
                    if (unit.equals(checkableUnit.getUnit())) newCheckableUnit = checkableUnit;
                    break;
                }
                if (newCheckableUnit ==null) newCheckableUnit = new CheckableUnit(unit,false, unit.getQuantity());
                newCheckableUnits.add(newCheckableUnit);
            }
            checkableUnits = newCheckableUnits;
            fireTableDataChanged();
        }

        /**
         * Вычисляет отмеченные юниты
         * @return отмеченные юниты
         */
        List<MovingUnit> getCheckedMovingUnits() {
            List<MovingUnit> checkedUnits = new ArrayList<MovingUnit>();
            for(CheckableUnit checkableUnit: checkableUnits) {
                if(checkableUnit.getChecked()) {
                    MovingUnit movingUnit = new MovingUnit();
                    movingUnit.setUnit(checkableUnit.getUnit());
                    movingUnit.setQuantity(checkableUnit.getQuantity());
                    checkedUnits.add(movingUnit);
                }


            }

            return checkedUnits;
        }

        CheckableUnit getCheckableUnitAt(int index) {return checkableUnits.get(index);}

        @Override
        public int getColumnCount() {
            return 6;
        }

        @Override
        public String getColumnName(int col) {
            String name = "Не опр";

            switch (col) {
                case 0:
                    name = "Отм";
                    break;
                case 1:
                    name = "Наименование";
                    break;
                case 2:
                    name = "Тип";
                    break;
                case 3:
                    name = "Ивентарный";
                    break;
                case 4:
                    name = "Кол-во";
                    break;
                case 5:
                    name = "Из";
                    break;
            }

            return name;
        }

        public int getColumnWidth(int col) {
            int width = 50;
            switch (col) {
                case 0:
                    width = 30;
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
                    width = 50;
                    break;
            }

            return width;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex==0) return Boolean.class;
            return super.getColumnClass(columnIndex);
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex==0||columnIndex==4;
        }

        @Override
        public int getRowCount() {
            return checkableUnits.size();
        }

        @Override
        public Object getValueAt(int row, int col) {
            String value = "Не опр";

            CheckableUnit checkableUnit = checkableUnits.get(row);
            Unit unit = checkableUnit.getUnit();

            switch (col) {
                case 0:
                    return checkableUnit.getChecked();

                case 1:
                    return unit.getName();

                case 2:
                    return unit.getType()==null?"Не опр":unit.getType().getName();

                case 3:
                    return unit.getInvNumber();

                case 4:
                    return checkableUnit.getQuantity();

                case 5:
                    return unit.getQuantity();

            }

            return value;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            super.setValueAt(aValue, rowIndex, columnIndex);
            if(columnIndex==0 && aValue instanceof Boolean) {
                checkableUnits.get(rowIndex).setChecked((Boolean)aValue);
            }
            if(columnIndex==4 && aValue instanceof String) {
                checkableUnits.get(rowIndex).setQuantity(Long.decode(aValue.toString()));
            }
        }
    }

}
