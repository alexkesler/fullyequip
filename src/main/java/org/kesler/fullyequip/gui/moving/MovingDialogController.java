package org.kesler.fullyequip.gui.moving;

import org.kesler.fullyequip.gui.dialog.ListDialogController;
import org.kesler.fullyequip.gui.receive.PlaceEquipDialogController;
import org.kesler.fullyequip.logic.Place;
import org.kesler.fullyequip.logic.Unit;
import org.kesler.fullyequip.logic.model.PlaceModel;

import javax.swing.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Контроллер управления диалогом перемещения
 */
public class MovingDialogController {

    private static MovingDialogController instance = null;
    private MovingDialog movingDialog;
    private Set<Unit> units;
    private Place newPlace;

    private MovingDialogController() {
        units = new HashSet<Unit>();
    }

    public static synchronized MovingDialogController getInstance() {
        if (instance == null) {
            instance = new MovingDialogController();
        }
        return instance;
    }

    public void showDialog(JFrame parentFrame) {
        movingDialog = new MovingDialog(parentFrame, this);
        movingDialog.setVisible(true);
    }

    void closeDialog() {
        movingDialog.setVisible(false);
        movingDialog.dispose();
        movingDialog = null;
    }


    void selectNewPlace() {
        newPlace = ListDialogController.create(Place.class, "Выберите новое размещение").showSelectDialog(movingDialog);
        movingDialog.update();
    }

    Place getNewPlace() {
        return newPlace;
    }

    Set<Unit> getUnits() {
        return units;
    }

    void addUnits() {
        List<Unit> newUnits = PlaceEquipDialogController.getInstance().showSelectDialog(movingDialog);
        units.addAll(newUnits);
        movingDialog.update();
    }

    void removeUnit(Unit unit) {
        units.remove(unit);
    }

    void move() {
        if (newPlace==null) {
            JOptionPane.showMessageDialog(movingDialog,
                    "Не определено место перемещения",
                    "Ошибка",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmResult = JOptionPane.showConfirmDialog(movingDialog,
                "Переместить указанное оборудование в новое место?",
                "Подтверждение",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);

        Date moveDate = movingDialog.getMovingDate();

        if (confirmResult==JOptionPane.YES_OPTION) {
            for (Unit unit : units) {
                unit.moveTo(newPlace, moveDate);

            }
            PlaceModel.getInstance().saveOrUpdateItem(newPlace);
            movingDialog.update();
            JOptionPane.showMessageDialog(movingDialog, "Готово", "Сообщение", JOptionPane.INFORMATION_MESSAGE);
        }
    }

}
