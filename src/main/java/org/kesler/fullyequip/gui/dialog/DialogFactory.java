package org.kesler.fullyequip.gui.dialog;

import org.kesler.fullyequip.gui.dialog.item.*;
import org.kesler.fullyequip.gui.dict.DictEntity;
import org.kesler.fullyequip.logic.*;

import javax.swing.*;

/**
 * Фабрика для создания диалогов сущностей
 */
public class DialogFactory {


    private DialogFactory() {}

    // создает диалог для создания
    public static AbstractItemDialog createItemDialog(JDialog parentDialog, Class<? extends DictEntity> type) {
        AbstractItemDialog itemDialog = null;


            if(type == UnitType.class) {

                ///  диалог для типа оборудования
                itemDialog = new UnitTypeDialog(parentDialog);

            } else if (type == Place.class) {

                // Диалог для размещения
                itemDialog = new PlaceDialog(parentDialog);

            } else if (type == Supplier.class) {

                // Диалог для поставщика
                itemDialog = new SupplierDialog(parentDialog);

            } else if (type == AuctionType.class) {

                // Диалог для типа аукциона
                itemDialog = new AuctionTypeDialog(parentDialog);

            } else if (type == Auction.class) {

                // Диалог для аукциона
                itemDialog = new AuctionDialog(parentDialog);

            } else if (type == Contract.class) {

                // Диалог для договора
                itemDialog = new ContractDialog(parentDialog);

            }




        return itemDialog;

    }



    // создает диалог для редактирования
    public static AbstractItemDialog createItemDialog(JDialog parentDialog, DictEntity item) {
        AbstractItemDialog itemDialog = null;


        if(item instanceof UnitType) {

            // Диалог для типа оборудования
            UnitType unitType = (UnitType) item;
            itemDialog = new UnitTypeDialog(parentDialog, unitType);

        } else if (item instanceof Place) {

            // Диалог для размещения
            Place place = (Place) item;
            itemDialog = new PlaceDialog(parentDialog, place);

        } else if (item instanceof Supplier) {

            // Диалог для размещения
            Supplier supplier = (Supplier) item;
            itemDialog = new SupplierDialog(parentDialog, supplier);

        } else if (item instanceof AuctionType) {

            // Диалог для типа аукцона
            AuctionType auctionType = (AuctionType) item;
            itemDialog = new AuctionTypeDialog(parentDialog, auctionType);

        } else if (item instanceof Auction) {

            // Диалог для аукцона
            Auction auction = (Auction) item;
            itemDialog = new AuctionDialog(parentDialog, auction);

        } else if (item instanceof Contract) {

            // Диалог для аукцона
            Contract contract = (Contract) item;
            itemDialog = new ContractDialog(parentDialog, contract);

        }




        return itemDialog;

    }



}
