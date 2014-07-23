package org.kesler.fullyequip.gui.dialog;

import org.kesler.fullyequip.logic.AuctionType;
import org.kesler.fullyequip.logic.Place;
import org.kesler.fullyequip.logic.UnitType;

import javax.swing.*;

/**
 * Фабрика для создания диалогов справочников
 */
public class ListDialogFactory {

    private static ListDialogController<AuctionType> auctionTypeListDialogController;

    public static void showAuctionTypeListDialog(JFrame parentFrame) {
        ListDialogController.create(AuctionType.class, "Типы аукционов").showDialog(parentFrame);
    }

    public static void showAuctionTypeListDialog(JDialog parentDialog) {
        ListDialogController.create(AuctionType.class, "Типы аукционов").showDialog(parentDialog);
    }

    public static AuctionType showSelectAuctionTypeListDialog(JDialog parentDialog) {
        return ListDialogController.create(AuctionType.class,"Типы аукционов").showSelectDialog(parentDialog);
    }

    public static void showUnitTypeListDialog(JDialog parentDialog) {
        ListDialogController.create(UnitType.class, "Типы оборудовния").showDialog(parentDialog);
    }

    public static UnitType showselectUnitTypeListDialog(JDialog parentDialog) {
        return ListDialogController.create(UnitType.class,"Типы оборудовния").showSelectDialog(parentDialog);
    }


    public static Place showselectPlaceListDialog(JDialog parentDialog) {
        return ListDialogController.create(Place.class,"Размещения").showSelectDialog(parentDialog);
    }


}
