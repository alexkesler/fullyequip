package org.kesler.fullyequip.gui.dialog;

import org.kesler.fullyequip.gui.dialog.item.AbstractItemDialog;
import org.kesler.fullyequip.gui.dialog.item.ItemDialogFactory;
import org.kesler.fullyequip.gui.dict.DictEntity;
import org.kesler.fullyequip.gui.util.InfoDialog;
import org.kesler.fullyequip.gui.util.ProcessDialog;
import org.kesler.fullyequip.logic.model.DefaultModel;
import org.kesler.fullyequip.logic.model.ModelState;
import org.kesler.fullyequip.logic.model.ModelStateListener;

import javax.swing.*;

/**
 * Контроллер для управления диалогом справочника
 * @param <T>
 */
public class ListDialogController<T extends DictEntity> implements ModelStateListener {

    private Class<T> type;
    private String name;
	private DefaultModel<T> model;
	private ListDialog<T> dialog;

	private ProcessDialog processDialog = null;


	public ListDialogController(Class<T> type, String name) {
        this.type = type;
        this.name = name;

		model = new DefaultModel<T>(type);
		model.addModelStateListener(this);

	}

    public static <T extends DictEntity> ListDialogController<T> create(Class<T> type, String name) {
         return new ListDialogController<T>(type, name);
    }

	public void showDialog(JFrame parentFrame) {

		dialog = new ListDialog<T>(parentFrame, name, this);
		processDialog = new ProcessDialog(dialog);
		model.readItemsInSeparateThread();

		dialog.setVisible(true);


		// Освобождаем ресурсы
		dialog.dispose();
		dialog = null;

	}


    public void showDialog(JDialog parentDialog) {

        dialog = new ListDialog<T>(parentDialog, name, this);
        processDialog = new ProcessDialog(dialog);
        model.readItemsInSeparateThread();

        dialog.setVisible(true);


        // Освобождаем ресурсы
        dialog.dispose();
        dialog = null;

    }

    public T showSelectDialog(JDialog parentDialog) {
        T selectedItem = null;

        dialog = new ListDialog<T>(parentDialog, name, this, ListDialog.SELECT_MODE);
        processDialog = new ProcessDialog(dialog);
        model.readItemsInSeparateThread();

        dialog.setVisible(true);

        if (dialog.getResult() == AbstractDialog.OK) {
            int selectedIndex = dialog.getSelectedIndex();
            selectedItem = model.getAllItems().get(selectedIndex);
        }

        return selectedItem;
    }

    public T showSelectDialog(JFrame parentFrame) {
        T selectedItem = null;

        dialog = new ListDialog<T>(parentFrame, name, this, ListDialog.SELECT_MODE);
        processDialog = new ProcessDialog(dialog);
        model.readItemsInSeparateThread();

        dialog.setVisible(true);

        if (dialog.getResult() == AbstractDialog.OK) {
            int selectedIndex = dialog.getSelectedIndex();
            selectedItem = model.getAllItems().get(selectedIndex);
        }

        return selectedItem;
    }


	public boolean openAddItemDialog() {
		boolean result = false;
		AbstractItemDialog itemDialog = ItemDialogFactory.createItemDialog(dialog, type);
		itemDialog.setVisible(true);

		if (itemDialog.getResult() == AbstractItemDialog.OK) {
			T item = (T)itemDialog.getItem();
			int index = model.addItem(item);
			dialog.addedItem(index);
			result = true;
		}

		// Освобождаем ресурсы
		itemDialog.dispose();
		itemDialog = null;

		return result;
	}


	public boolean openEditItemDialog(int index) {

		boolean result = false;
		T item = model.getAllItems().get(index);
		AbstractItemDialog itemDialog = ItemDialogFactory.createItemDialog(dialog, item);
		itemDialog.setVisible(true);

		if (itemDialog.getResult() == AbstractItemDialog.OK) {
            if(itemDialog.isChanged()) {
                model.updateItem(item);
                dialog.updatedItem(index);
                result = true;
            }

		}

		// Освобождаем ресурсы
		itemDialog.dispose();
		itemDialog = null;

		return result;

	}


	public boolean removeItem(int index) {

		boolean result = false;

		T item = model.getAllItems().get(index);
		
		int confirmResult = JOptionPane.showConfirmDialog(dialog, "Удалить " + item + "?", "Удалить?", JOptionPane.YES_NO_OPTION);
		if (confirmResult == JOptionPane.OK_OPTION) { 			
			model.removeItem(item);
			dialog.removedItem(index);
			result = true;
		}

		return result;

	}


	public void readItems() {
		processDialog = new ProcessDialog(dialog);
		model.readItemsInSeparateThread();
	}


	public void filterItems(String filter) {
		
	}

	@Override
	public void modelStateChanged(ModelState state) {
		switch (state) {
			
			case CONNECTING:
				if (processDialog != null) processDialog.showProcess("Соединяюсь...");			
				break;

			case READING:
				if (processDialog != null) processDialog.showProcess("Читаю список операторов");
				break;	
			
			case WRITING:
				if (processDialog != null) processDialog.showProcess("Сохраняю изменения");
				break;	
			
			case UPDATED:
				if (dialog != null) dialog.setItems(model.getAllItems());
				if (processDialog != null) {processDialog.hideProcess(); processDialog = null;}
				new InfoDialog(dialog, "Обновлено", 500, InfoDialog.GREEN).showInfo();
				break;
			
			case READY:
				if (processDialog != null) {processDialog.hideProcess(); processDialog=null;}	
				break;
			
			case ERROR:				
				if (processDialog != null) {processDialog.hideProcess(); processDialog=null;}
				new InfoDialog(dialog, "Ошибка базы данных", 1000, InfoDialog.RED).showInfo();
				break;

		}
		
	}

}