package org.kesler.fullyequip.gui.dialog.item;

import net.miginfocom.swing.MigLayout;
import org.kesler.fullyequip.gui.dialog.AbstractItemDialog;
import org.kesler.fullyequip.logic.Supplier;

import javax.swing.*;

/**
 * Диалог редактирования поставщика
 */
public class SupplierDialog extends AbstractItemDialog<Supplier> {

    private JTextField nameTextField;

    public SupplierDialog(JDialog parentDialog) {
        super(parentDialog);
    }

    public SupplierDialog(JDialog parentDialog, Supplier supplier) {
        super(parentDialog, supplier);
    }

    @Override
    public void createNewItem() {
        item = new Supplier();
    }

    @Override
    protected JPanel createItemPanel() {
        JPanel dataPanel  = new JPanel(new MigLayout());

        nameTextField = new JTextField(20);

        dataPanel.add(new JLabel("Наименование"));
        dataPanel.add(nameTextField);


        return dataPanel;
    }

    @Override
    protected void loadGUIFromItem() {

        String name = item.getName();

        nameTextField.setText(name==null?"":name);

    }

    @Override
    protected boolean readItemFromGUI() {

        String name = nameTextField.getText();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(currentDialog,"Поле Наименование не может быть пустым");
            return false;
        }

        item.setName(name);

        return true;
    }


    @Override
    protected boolean checkChanged() {

        return true;
    }
}
