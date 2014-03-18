package org.kesler.fullyequip.gui.dialog;

import org.kesler.fullyequip.logic.AuctionType;

import javax.swing.*;

/**
 * Фабрика для создания диалогов справочников
 */
public class ListDialogFactory {

    private static ListDialogController<AuctionType> auctionTypeListDialogController;

    public static void showAuctionTypeListDialog(JFrame parentFrame) {
        ListDialogController.create(AuctionType.class, "Типы аукционов").showDialog(parentFrame);
    }


}
