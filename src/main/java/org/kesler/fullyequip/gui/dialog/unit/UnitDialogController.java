package org.kesler.fullyequip.gui.dialog.unit;

import org.kesler.fullyequip.gui.dialog.AbstractDialog;
import org.kesler.fullyequip.logic.Unit;
import org.kesler.fullyequip.logic.model.UnitsModel;

import javax.swing.*;

/**
 * Контроллер для управления диалогом оборудования
 */
public class UnitDialogController {
    private final static UnitDialogController instance = new UnitDialogController();
    private UnitDialog unitDialog;

    private UnitDialogController() {}

    public static synchronized UnitDialogController getInstance() {return instance;}

    public boolean showDialog(JDialog parentDialog, Unit unit) {
        unitDialog = new UnitDialog(parentDialog,unit);
        unitDialog.setVisible(true);
        if (unitDialog.getResult()== AbstractDialog.OK) {
            UnitsModel.getInstance().saveOrUpdateItem(unit);
            return true;
        }
        return false;
    }

    public boolean showDialog(JFrame parentFrame, Unit unit) {
        unitDialog = new UnitDialog(parentFrame,unit);
        unitDialog.setVisible(true);
        if (unitDialog.getResult()== AbstractDialog.OK) {
            UnitsModel.getInstance().saveOrUpdateItem(unit);
            return true;
        }
        return false;
    }


    void closeDialog() {
        unitDialog.setVisible(false);
        unitDialog.dispose();
    }



}
