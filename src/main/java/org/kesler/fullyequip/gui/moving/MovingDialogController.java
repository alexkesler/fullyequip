package org.kesler.fullyequip.gui.moving;

import org.kesler.fullyequip.gui.dialog.ListDialogController;
import org.kesler.fullyequip.gui.equipment.PlaceEquipDialogController;
import org.kesler.fullyequip.logic.Moving;
import org.kesler.fullyequip.logic.Place;
import org.kesler.fullyequip.logic.Unit;

import javax.swing.JFrame;
import java.util.List;

/**
 * Контроллер управления диалогом перемещения
 */
public class MovingDialogController {

    private static MovingDialogController instance = null;
    private Moving moving;
    MovingDialog movingDialog;

    private MovingDialogController() {

    }

    public static synchronized MovingDialogController getInstance() {
        if (instance == null) {
            instance = new MovingDialogController();
        }
        return instance;
    }

    public void showDialog(JFrame parentFrame) {
        moving = new Moving();
        movingDialog = new MovingDialog(parentFrame, this);
        movingDialog.setVisible(true);
    }

    Moving getMoving() {return moving;}

    boolean selectNewPlace() {
        Place place = ListDialogController.create(Place.class, "Выберите новое размещение").showSelectDialog(movingDialog);
        moving.setNewPlace(place);
        return true;
    }

    List<Unit> getAddingUnits() {

        List<Unit> units = PlaceEquipDialogController.getInstance().showSelectDialog(movingDialog);

        return units;
    }

}
