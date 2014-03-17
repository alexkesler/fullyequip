package org.kesler.fullyequip.gui.dialog;

import org.kesler.fullyequip.gui.dict.DictEntity;

import javax.swing.*;
import java.util.List;

public interface ListDialogController<T extends DictEntity> {


//    public void showDialog(JDialog parentDialog);
//    public void showDialog(JFrame parentFrame);
//
//    public T showSelectDialog(JDialog parentDialog);
//    public T showSelectDialog(JFrame parentFrame);
//
//    public T showFilterSelectDialog(JDialog parentDialog);
//    public T showFilterSelectDialog(JFrame parentFrame);
	
	boolean openAddItemDialog();
	boolean openEditItemDialog(int index);
	boolean removeItem(int index);

	void filterItems(String filter);
	
	void readItems();
}	

