package org.kesler.fullyequip.gui.dialog.item;

import net.miginfocom.swing.MigLayout;
import org.kesler.fullyequip.logic.UnitState;

import javax.swing.*;

/**
 * Created by alex on 13.04.14.
 */
public class UnitStateDialog  extends AbstractItemDialog<UnitState>{

    private JTextField nameTextField;
    private JCheckBox initialCheckBox;


    public UnitStateDialog(JDialog parentDialog) {
        super(parentDialog);
    }

    public UnitStateDialog(JDialog parentDialog, UnitState unitState) {
        super(parentDialog, unitState);
    }

    @Override
    protected void createNewItem() {
        item = new UnitState();
    }

    @Override
    protected JPanel createItemPanel() {

        // Панель данных
        JPanel dataPanel = new JPanel(new MigLayout("fill"));

        nameTextField = new JTextField(20);
        initialCheckBox = new JCheckBox();


        // Собираем панель данных
        dataPanel.add(new JLabel("Наименование:"));
        dataPanel.add(nameTextField, "wrap");
        dataPanel.add(new JLabel("По умолчанию: "));
        dataPanel.add(initialCheckBox, "wrap");

        return dataPanel;

    }

    @Override
    protected void loadGUIFromItem() {

        String name = item.getName();
        Boolean initial = item.getInitial();

        // текстовые поля

        nameTextField.setText(name==null?"":name);
        initialCheckBox.setSelected(initial==null?false:initial);


    }

    @Override
    protected boolean readItemFromGUI() {

        String name = nameTextField.getText();
        Boolean initial = initialCheckBox.isSelected();



        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Поле Наименование не может быть пустым", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        item.setName(name);
        item.setInitial(initial);

        return true;
    }

    @Override
    protected boolean checkChanged() {
        return true;
    }

}