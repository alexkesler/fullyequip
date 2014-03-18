package org.kesler.fullyequip.gui.dialog.item;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;

import net.miginfocom.swing.MigLayout;

import org.kesler.fullyequip.logic.UnitType;

public class UnitTypeDialog extends AbstractItemDialog<UnitType> {

	private JTextField nameTextField;


	public UnitTypeDialog(JDialog parentDialog) {
		super(parentDialog);
	}

	public UnitTypeDialog(JDialog parentDialog, UnitType unitType) {
		super(parentDialog, unitType);
	}

    @Override
    protected void createNewItem() {
        item = new UnitType();
    }

    @Override
	protected JPanel createItemPanel() {

		// Панель данных
		JPanel dataPanel = new JPanel(new MigLayout("fill"));

		nameTextField = new JTextField(20);


		// Собираем панель данных
		dataPanel.add(new JLabel("Наименование:"));
		dataPanel.add(nameTextField, "wrap");

        return dataPanel;

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
        return true;
    }

}