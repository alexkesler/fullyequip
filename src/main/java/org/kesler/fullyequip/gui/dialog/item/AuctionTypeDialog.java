package org.kesler.fullyequip.gui.dialog.item;

import net.miginfocom.swing.MigLayout;
import org.kesler.fullyequip.gui.dialog.AbstractItemDialog;
import org.kesler.fullyequip.logic.AuctionType;

import javax.swing.*;


/**
 * Диалог типа аукциона
 */
public class AuctionTypeDialog extends AbstractItemDialog<AuctionType> {


    private JTextField nameTextField;


    public AuctionTypeDialog(JDialog parentDialog) {
        super(parentDialog);

    }

    public AuctionTypeDialog(JDialog parentDialog, AuctionType auctionType) {
        super(parentDialog, auctionType);

     }

    @Override
    protected void createNewItem() {
        item = new AuctionType();
    }


    @Override
    protected JPanel createItemPanel() {


        // Панель данных
        JPanel dataPanel = new JPanel(new MigLayout("fill"));

        nameTextField = new JTextField(10);


        // Собираем панель данных
        dataPanel.add(new JLabel("Наименование:"));
        dataPanel.add(nameTextField, "wrap");


        return  dataPanel;

    }

    @Override
    protected void loadGUIFromItem() {

        String name = item.getName();


        // текстовые поля
        if (name != null) {
            nameTextField.setText(name);
        } else {
            nameTextField.setText("");
        }

    }

    @Override
    protected boolean readItemFromGUI() {

        String name = nameTextField.getText();



        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Поле Наименование не может быть пустым", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        item.setName(name);

        return true;
    }

    @Override
    protected boolean checkChanged() {
        if (!nameTextField.getText().equals(item.getName()))  return true;
        return false;
    }

}
