package org.kesler.fullyequip.gui.dialog.unit;

import org.kesler.fullyequip.logic.Unit;
import org.kesler.fullyequip.logic.model.UnitModel;

import javax.swing.*;

/**
 * Контроллер для управления диалогом оборудования
 */
public class UnitDialogController {
    private final static UnitDialogController instance = new UnitDialogController();
    private UnitDialog unitDialog;

    private UnitDialogController() {}

    public static synchronized UnitDialogController getInstance() {return instance;}

    public void showDialog(JDialog parentDialog, Unit unit) {
        unitDialog = new UnitDialog(parentDialog,unit);
        unitDialog.setVisible(true);
        if (unitDialog.getResult()==UnitDialog.OK) {
            UnitModel.getInstance().saveOrUpdateItem(unit);
        }
    }


    void closeDialog() {
        unitDialog.setVisible(false);
        unitDialog.dispose();
    }



}
