package org.kesler.fullyequip.gui.dialog.unit;

import org.kesler.fullyequip.logic.Unit;

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
    }


    void closeDialog() {
        unitDialog.setVisible(false);
        unitDialog.dispose();
    }



}
