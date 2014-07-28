package org.kesler.fullyequip.gui.dialog.unit;

import com.alee.extended.panel.CollapsiblePaneAdapter;
import com.alee.extended.panel.WebCollapsiblePane;
import net.miginfocom.swing.MigLayout;
import org.kesler.fullyequip.gui.dialog.AbstractDialog;
import org.kesler.fullyequip.gui.dialog.ListDialogController;
import org.kesler.fullyequip.gui.dialog.ListDialogFactory;
import org.kesler.fullyequip.logic.*;
import org.kesler.fullyequip.logic.model.*;
import org.kesler.fullyequip.util.ResourcesUtil;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Диалог редактирования единицы оборудования
 */
class UnitDialog extends AbstractDialog {

    private Unit unit;
    private InvoicePosition invoicePosition;
    private Place place;


    private JLabel contractLabel;
    private JLabel invoiceLabel;
    private JLabel placeLabel;

    private UnitTypeComboBoxModel unitTypeComboBoxModel;
    private JComboBox<UnitType> unitTypeComboBox;
    private JComboBox<UnitState> unitStateComboBox;
    private JTextField unitNameTextField;
    private JTextField serialNumTextField;
    private JTextField invNumTextField;
    private JFormattedTextField priceTextField;
    private JFormattedTextField quantityTextField;

    private UnitMoveTableModel unitMoveTableModel;
    private JTable unitMoveTable;


    // Создаем новое оборудование по указанной накладной и помещаем его в указанное место
    public UnitDialog(JDialog parentDialog, InvoicePosition invoicePosition) {
        super(parentDialog, "Создать оборудование",true);
        this.invoicePosition = invoicePosition;
        createNewUnit();
        createGUI();
        loadGUIFromUnit();
    }

    // Редактируем оборудование
    UnitDialog(JDialog parentDialog, Unit unit) {
        super(parentDialog, "Оборудование", true);
        this.unit = unit;
        this.invoicePosition = unit.getInvoicePosition();
        this.place = unit.getPlace();
        createGUI();
        loadGUIFromUnit();

    }

    // Редактируем оборудование
    UnitDialog(JFrame parentFrame, Unit unit) {
        super(parentFrame, "Оборудование", true);
        this.unit = unit;
        this.invoicePosition = unit.getInvoicePosition();
        this.place = unit.getPlace();
        createGUI();
        loadGUIFromUnit();

    }


    private void createGUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel dataPanel = new JPanel(new MigLayout("fillx"));

        contractLabel = new JLabel("Не определен");
        contractLabel.setFont(new Font("Arial", Font.BOLD, 14));

        invoiceLabel = new JLabel("Не опеределена");

        placeLabel = new JLabel("Не определено");
        JButton selectPlaceButton = new JButton(ResourcesUtil.getIcon("book_previous.png"));
        selectPlaceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectPlace();
            }
        });

        JPanel equipPanel = new JPanel(new MigLayout("fill"));
        equipPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Оборудование"));

        unitTypeComboBoxModel = new UnitTypeComboBoxModel();
        unitTypeComboBox = new JComboBox<UnitType>(unitTypeComboBoxModel);
        JButton editUnitTypesButton  =new JButton(ResourcesUtil.getIcon("table_edit.png"));
        editUnitTypesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editUnitTypes();
            }
        });

        unitNameTextField = new JTextField(20);
        serialNumTextField = new JTextField(15);
        invNumTextField = new JTextField(15);
        priceTextField = new JFormattedTextField(new NumberFormatter(NumberFormat.getNumberInstance()));
        quantityTextField = new JFormattedTextField(new NumberFormatter(NumberFormat.getIntegerInstance()));

        equipPanel.add(new JLabel("Тип: "));
        equipPanel.add(unitTypeComboBox);
        equipPanel.add(editUnitTypesButton, "wrap");
        equipPanel.add(new JLabel("Наименование: "));
        equipPanel.add(unitNameTextField, "wrap");
        equipPanel.add(new JLabel("Серийный номер: "));
        equipPanel.add(serialNumTextField, "wrap");
        equipPanel.add(new JLabel(("Инвентарный номер: ")));
        equipPanel.add(invNumTextField, "wrap");
        equipPanel.add(new JLabel("Цена: "));
        equipPanel.add(priceTextField, "w 150, span 2, split 2");
        equipPanel.add(new JLabel("р."), "wrap");
        equipPanel.add(new JLabel("Кол-во: "));
        equipPanel.add(quantityTextField, "w 50, wrap");

        JPanel statePanel = new JPanel(new MigLayout("fill"));
        statePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Состояние"));

        unitStateComboBox = new JComboBox<UnitState>(new UnitStateComboBoxModel());

        statePanel.add(unitStateComboBox);


        JPanel unitMovePanel = new JPanel(new MigLayout("fill"));
        unitMovePanel.setBorder(BorderFactory.createEtchedBorder());


        unitMoveTable = new JTable(new UnitMoveTableModel());
        unitMoveTable.getColumnModel().getColumn(0).setPreferredWidth(200);
        unitMoveTable.getColumnModel().getColumn(1).setPreferredWidth(50);
        unitMoveTable.setPreferredScrollableViewportSize(new Dimension(
                unitMoveTable.getPreferredScrollableViewportSize().width,
                100
        ));
        JScrollPane unitMoveTableScrollPane = new JScrollPane(unitMoveTable);

        unitMovePanel.add(new JLabel("Перемещения: "), "wrap");
        unitMovePanel.add(unitMoveTableScrollPane, "growx");

        WebCollapsiblePane unitMoveWebCollapsiblePane = new WebCollapsiblePane("Перемещения", unitMoveTableScrollPane);
        unitMoveWebCollapsiblePane.setExpanded(false);
        unitMoveWebCollapsiblePane.addCollapsiblePaneListener(new CollapsiblePaneAdapter() {
            @Override
            public void expanded(WebCollapsiblePane pane) {
                super.expanded(pane);
                pack();
            }

            @Override
            public void expanding(WebCollapsiblePane pane) {
                super.expanding(pane);
                pack();
            }

            @Override
            public void collapsed(WebCollapsiblePane pane) {
                super.collapsed(pane);
                pack();
            }

            @Override
            public void collapsing(WebCollapsiblePane pane) {
                super.collapsing(pane);
                pack();
            }
        });



        dataPanel.add(new JLabel("Договор: "));
        dataPanel.add(contractLabel, "w 370, wrap");
        dataPanel.add(new JLabel("Накладная: "));
        dataPanel.add(invoiceLabel, "wrap");
        dataPanel.add(new JLabel("Размещение: "));
        dataPanel.add(placeLabel);
        dataPanel.add(selectPlaceButton, "wrap");
        dataPanel.add(equipPanel, "span, grow");
        dataPanel.add(statePanel, "span, growx");
        dataPanel.add(unitMoveWebCollapsiblePane,"span");


        JPanel buttonPanel = new JPanel();

        JButton okButton = new JButton("Ок");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (readUnitFromGUI()) {
                    result = OK;
                    setVisible(false);
                }
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


        mainPanel.add(dataPanel,BorderLayout.CENTER);
        mainPanel.add(buttonPanel,BorderLayout.SOUTH);

        setContentPane(mainPanel);
        pack();
    }


    protected void createNewUnit() {
        unit = new Unit();
        unit.setInvoicePosition(invoicePosition);
        unit.setPlace(PlacesModel.getInstance().getInitialPlace());
        unit.setState(UnitStatesModel.getInstance().getInitialState());
    }


    public void loadGUIFromUnit() {

        Invoice invoice = unit.getInvoicePosition().getInvoice();
        place = unit.getPlace();
        String contractName = invoice==null?"Не определен":invoice.getContract().toString();

        contractLabel.setText("<html>"
                + contractName
                + "</html>");
        invoiceLabel.setText(invoice==null?"Не определена":invoice.toString());
        placeLabel.setText(place==null?"Не определено": place.toString());

        unitTypeComboBox.setSelectedItem(unit.getType());
        unitNameTextField.setText(unit.getName());
        serialNumTextField.setText(unit.getSerialNumber());
        invNumTextField.setText(unit.getInvNumber());
        priceTextField.setValue(unit.getPrice());
        quantityTextField.setValue(unit.getQuantity());

        unitStateComboBox.setSelectedItem(unit.getState());

    }

    protected boolean readUnitFromGUI () {

        String unitName = unitNameTextField.getText();
        if(unitName.isEmpty()) {
            JOptionPane.showMessageDialog(currentDialog,
                    "Поле Наименование не может быть пустым",
                    "Ошибка",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        String serialNum = serialNumTextField.getText();
//        if(serialNum.isEmpty()) {
//            JOptionPane.showMessageDialog(currentDialog,
//                    "Поле Серийный номер не может быть пустым",
//                    "Ошибка",
//                    JOptionPane.WARNING_MESSAGE);
//            return false;
//        }

        String invNum = invNumTextField.getText();
        Double price = ((Number)priceTextField.getValue()).doubleValue();

        Long quantity = ((Number)quantityTextField.getValue()).longValue();

        unit.setType((UnitType) unitTypeComboBox.getSelectedItem());
        unit.setName(unitName);
        unit.setSerialNumber(serialNum);
        unit.setInvNumber(invNum);
        unit.setPrice(price);
        unit.setQuantity(quantity);

        unit.setState((UnitState) unitStateComboBox.getSelectedItem());

        unit.setInvoicePosition(invoicePosition);
        unit.setPlace(place);

        return true;
    }


    protected boolean checkChanged() {
        if(!unitTypeComboBox.getSelectedItem().equals(unit.getType())) return true;
        if(!unitNameTextField.getText().equals(unit.getName())) return true;
        if (!serialNumTextField.getText().equals(unit.getSerialNumber())) return true;
        if(!invNumTextField.getText().equals(unit.getInvNumber())) return true;
        Float price = ((Number)priceTextField.getValue()).floatValue();
        if(!price.equals(unit.getPrice())) return true;
        Integer quantity = ((Number)quantityTextField.getValue()).intValue();
        if (!quantity.equals(unit.getQuantity())) return true;


        return false;
    }

    void editUnitTypes() {
        UnitType selectedUnitType = ListDialogFactory.showselectUnitTypeListDialog(currentDialog);
        if (selectedUnitType!=null) {
            unitTypeComboBoxModel.update();
            unitTypeComboBox.setSelectedItem(selectedUnitType);
        }
    }

    // Модель для типов оборудования
    class UnitTypeComboBoxModel extends DefaultComboBoxModel<UnitType> implements ModelStateListener {

        private DefaultModel<UnitType> model;

        UnitTypeComboBoxModel() {
            model = new DefaultModel<UnitType>(UnitType.class);
            model.addModelStateListener(this);
            model.readItemsInSeparateThread();
        }

        void update() {
            model.readItemsFromDB();
        }

        @Override
        public void modelStateChanged(ModelState state) {
            if(state == ModelState.UPDATED) {
                removeAllElements();
                java.util.List<UnitType> types = model.getAllItems();
                for(UnitType type:types) addElement(type);
            }
        }

    }

    // Модель для состояний оборудования
    class UnitStateComboBoxModel extends DefaultComboBoxModel<UnitState> implements ModelStateListener {

        private UnitStatesModel model;

        UnitStateComboBoxModel() {
            model = UnitStatesModel.getInstance();
            model.addModelStateListener(this);
            model.readItemsInSeparateThread();
        }

        void update() {

        }

        @Override
        public void modelStateChanged(ModelState state) {
            if(state == ModelState.UPDATED) {
                removeAllElements();
                java.util.List<UnitState> unitStates = model.getAllItems();
                for(UnitState unitState: unitStates) addElement(unitState);
            }

        }
    }

    void selectPlace() {
        Place selectedPlace = ListDialogController.create(Place.class,"Размещения").showSelectDialog(currentDialog);
        if(selectedPlace!=null) {
            place = selectedPlace;
            placeLabel.setText(place.getName());
        }
    }

    // Модель для таблицы перемещений
    class UnitMoveTableModel extends AbstractTableModel {

        private SimpleDateFormat simpleDateFormat;
        private List<UnitMove> moves;

        UnitMoveTableModel() {
            moves = new ArrayList<UnitMove>(unit.getMoves());
            simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        }

        @Override
        public int getRowCount() {
            return moves.size();
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public String getColumnName(int col) {
            String name = "Не опр";

            switch (col) {
                case 0:
                    name = "Куда перемещено";
                    break;
                case 1:
                    name = "Дата";
                    break;
            }

            return name;
        }

        @Override
        public String getValueAt(int row, int col) {

            String value = "Не опр";

            UnitMove move = moves.get(row);

            switch (col) {
                case 0:
                    value = move.getPlace().toString();
                    break;
                case 1:
                    value = simpleDateFormat.format(move.getMoveDate());
            }

            return value;

        }

    }


}
