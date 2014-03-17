package org.kesler.fullyequip.gui.equipment;

import org.kesler.fullyequip.gui.dialog.DefaultListDialogController;
import org.kesler.fullyequip.logic.Place;
import org.kesler.fullyequip.logic.Unit;

import javax.swing.JFrame;
import javax.swing.JDialog;
import java.util.ArrayList;
import java.util.List;

/**
 * Контроллер для управления диалогом размещения оборудования
 */
public class PlaceEquipDialogController {

    private static PlaceEquipDialogController instance = null;

    private PlaceEquipDialog dialog;

    private PlaceEquipDialogController() {

    }

    public static synchronized PlaceEquipDialogController getInstance() {
        if (instance == null) instance = new PlaceEquipDialogController();
        return instance;
    }

    /**
     * Открывает диалог просмотра оборудования для окна
     * @param parentFrame родительское окно
     */
    public void showDialog(JFrame parentFrame) {
        dialog = new PlaceEquipDialog(parentFrame, this);
        dialog.setVisible(true);
    }

    /**
     * Открывает диалог выбора оборудования для окна
     * @param parentFrame родительское окно
     * @return список выбранного оборудования
     */
    public List<Unit> showSelectDialog(JFrame parentFrame) {
        List<Unit> units = new ArrayList<Unit>();
        dialog = new PlaceEquipDialog(parentFrame, this);
        dialog.setVisible(true);
        if(dialog.getResult()==PlaceEquipDialog.OK) {
            units = dialog.getSelectedUnits();
        }

        return units;
    }

    /**
     * Открывает диалог выбора оборудования для диалога
     * @param parentDialog родительский диалог
     * @return список выбранного оборудования
     */
    public List<Unit> showSelectDialog(JDialog parentDialog) {
        List<Unit> units = new ArrayList<Unit>();
        dialog = new PlaceEquipDialog(parentDialog, this);
        dialog.setVisible(true);
        if(dialog.getResult()==PlaceEquipDialog.OK) {
            units = dialog.getSelectedUnits();
        }

        return units;
    }


    Place selectPlace() {
        Place place = null;

        place = DefaultListDialogController.create(Place.class,"Выберите размещение").showSelectDialog(dialog);

        return place;

    }

}
