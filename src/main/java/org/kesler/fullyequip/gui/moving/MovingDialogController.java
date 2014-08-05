package org.kesler.fullyequip.gui.moving;

import org.kesler.fullyequip.gui.dialog.ListDialogController;
import org.kesler.fullyequip.gui.dialog.unit.UnitDialogController;
import org.kesler.fullyequip.gui.equip.PlaceEquipDialogController;
import org.kesler.fullyequip.logic.Place;
import org.kesler.fullyequip.logic.Unit;
import org.kesler.fullyequip.logic.model.PlacesModel;
import org.kesler.fullyequip.logic.model.UnitsModel;

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
    private Set<MovingUnit> movingUnits;
    private Place newPlace;

    private MovingDialogController() {
        movingUnits = new HashSet<MovingUnit>();
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
        movingUnits.clear();
        newPlace = null;
    }


    void selectNewPlace() {
        newPlace = ListDialogController.create(Place.class, "Выберите новое размещение").showSelectDialog(movingDialog);
        movingDialog.update();
    }

    Place getNewPlace() {
        return newPlace;
    }

    Set<MovingUnit> getMovingUnits() {
        return movingUnits;
    }

    void addUnits() {
        List<MovingUnit> movingUnits = PlaceEquipDialogController.getInstance().showSelectDialog(movingDialog);
        this.movingUnits.addAll(movingUnits);
        movingDialog.update();
    }

    void editUnit (Unit unit) {
        UnitDialogController.getInstance().showDialog(movingDialog,unit);
    }


    void removeUnit(Unit unit) {
        movingUnits.remove(unit);
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

        // осуществляем перемещение
        if (confirmResult==JOptionPane.YES_OPTION) {
            for (MovingUnit movingUnit : movingUnits) {
                if(movingUnit.getQuantity() == movingUnit.getAllQuantity()) {
                    Unit unit = movingUnit.getUnit();
                    unit.moveTo(newPlace,moveDate);
                    UnitsModel.getInstance().saveOrUpdateItem(unit);
                } else if (movingUnit.getQuantity() < movingUnit.getAllQuantity()) {
                    Unit sourceUnit = movingUnit.getUnit();
                    Unit resultUnit = sourceUnit.splitUnit(movingUnit.getQuantity());
                    resultUnit.moveTo(newPlace,moveDate);
                    movingUnit.setUnit(resultUnit);
                    UnitsModel.getInstance().saveOrUpdateItem(sourceUnit);
                    UnitsModel.getInstance().saveOrUpdateItem(resultUnit);
                }

            }
            movingDialog.update();
            JOptionPane.showMessageDialog(movingDialog, "Готово", "Сообщение", JOptionPane.INFORMATION_MESSAGE);
        }
    }

}
