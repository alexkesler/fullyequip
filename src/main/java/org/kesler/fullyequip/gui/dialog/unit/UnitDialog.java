package org.kesler.fullyequip.gui.dialog.unit;

import com.alee.extended.panel.CollapsiblePaneAdapter;
import com.alee.extended.panel.WebCollapsiblePane;
import net.miginfocom.swing.MigLayout;
import org.kesler.fullyequip.gui.dialog.item.AbstractItemDialog;
import org.kesler.fullyequip.logic.*;
import org.kesler.fullyequip.logic.model.*;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.text.NumberFormatter;
import java.awt.Font;
import java.awt.Dimension;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Диалог редактирования единицы оборудования
 */
public class UnitDialog extends AbstractItemDialog<Unit> {

    private InvoicePosition invoicePosition;
    private Place place;
    private UnitState state;

    private JLabel contractLabel;
    private JLabel invoiceLabel;
    private JLabel placeLabel;

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
        super(parentDialog, "Создать оборудование");
        this.invoicePosition = invoicePosition;
        loadGUIFromItem();
    }

    // Редактируем оборудование
    public UnitDialog(JDialog parentDialog, Unit unit) {
        super(parentDialog, "Оборудование", unit);
        this.invoicePosition = unit.getInvoicePosition();
        this.place = unit.getPlace();
        loadGUIFromItem();

    }

    @Override
    protected JPanel createItemPanel() {
        JPanel dataPanel = new JPanel(new MigLayout("fillx"));

        contractLabel = new JLabel("Не определен");
        contractLabel.setFont(new Font("Arial", Font.BOLD, 14));

        invoiceLabel = new JLabel("Не опеределена");

        placeLabel = new JLabel("Не определено");

        JPanel equipPanel = new JPanel(new MigLayout("fill"));
        equipPanel.setBorder(BorderFactory.createEtchedBorder());

        unitTypeComboBox = new JComboBox<UnitType>(new UnitTypeComboBoxModel());

        unitNameTextField = new JTextField(20);
        serialNumTextField = new JTextField(15);
        invNumTextField = new JTextField(15);
        priceTextField = new JFormattedTextField(new NumberFormatter(NumberFormat.getNumberInstance()));
        quantityTextField = new JFormattedTextField(new NumberFormatter(NumberFormat.getIntegerInstance()));

        equipPanel.add(new JLabel("Тип: "));
        equipPanel.add(unitTypeComboBox, "wrap");
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
        statePanel.setBorder(BorderFactory.createEtchedBorder());

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
        dataPanel.add(placeLabel, "wrap");
        dataPanel.add(equipPanel, "span, grow");
        dataPanel.add(statePanel, "span");
        dataPanel.add(unitMoveWebCollapsiblePane,"span");

        return dataPanel;
    }

    @Override
    protected void createNewItem() {
        item = new Unit();
        item.setInvoicePosition(invoicePosition);
        item.setPlace(PlaceModel.getInstance().getInitialPlace());
        item.setState(UnitStateModel.getInstance().getInitialState());
    }

    @Override
    protected void loadGUIFromItem() {

        Invoice invoice = item.getInvoicePosition().getInvoice();
        String contractName = invoice==null?"Не определен":invoice.getContract().toString();

        contractLabel.setText("<html>"
                + contractName
                + "</html>");
        invoiceLabel.setText(invoice==null?"Не определена":invoice.toString());
        placeLabel.setText(place==null?"Не определено":place.toString());

        unitTypeComboBox.setSelectedItem(item.getType());
        unitNameTextField.setText(item.getName());
        serialNumTextField.setText(item.getSerialNumber());
        invNumTextField.setText(item.getInvNumber());
        priceTextField.setValue(item.getPrice());
        quantityTextField.setValue(item.getQuantity());

        unitStateComboBox.setSelectedItem(item.getState());

    }

    @Override
    protected boolean readItemFromGUI () {

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

        item.setType((UnitType)unitTypeComboBox.getSelectedItem());
        item.setName(unitName);
        item.setSerialNumber(serialNum);
        item.setInvNumber(invNum);
        item.setPrice(price);
        item.setQuantity(quantity);

        item.setState((UnitState)unitStateComboBox.getSelectedItem());

        item.setInvoicePosition(invoicePosition);
        item.setPlace(place);

        return true;
    }

    @Override
    protected boolean checkChanged() {
        if(!unitTypeComboBox.getSelectedItem().equals(item.getType())) return true;
        if(!unitNameTextField.getText().equals(item.getName())) return true;
        if (!serialNumTextField.getText().equals(item.getSerialNumber())) return true;
        if(!invNumTextField.getText().equals(item.getInvNumber())) return true;
        Float price = ((Number)priceTextField.getValue()).floatValue();
        if(!price.equals(item.getPrice())) return true;
        Integer quantity = ((Number)quantityTextField.getValue()).intValue();
        if (!quantity.equals(item.getQuantity())) return true;


        return false;
    }

    // Модель для типов оборудования
    class UnitTypeComboBoxModel extends DefaultComboBoxModel<UnitType> implements ModelStateListener {

        private DefaultModel<UnitType> model;

        UnitTypeComboBoxModel() {
            model = new DefaultModel<UnitType>(UnitType.class);
            model.addModelStateListener(this);
            model.readItemsInSeparateThread();
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

        private UnitStateModel model;

        UnitStateComboBoxModel() {
            model = UnitStateModel.getInstance();
            model.addModelStateListener(this);
            model.readItemsInSeparateThread();
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

    // Модель для таблицы перемещений
    class UnitMoveTableModel extends AbstractTableModel {

        private SimpleDateFormat simpleDateFormat;
        private List<UnitMove> moves;

        UnitMoveTableModel() {
            moves = new ArrayList<UnitMove>(item.getMoves());
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
