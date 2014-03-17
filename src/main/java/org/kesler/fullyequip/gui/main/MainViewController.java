package org.kesler.fullyequip.gui.main;

import org.kesler.fullyequip.gui.dialog.DefaultListDialogController;
import org.kesler.fullyequip.gui.dialog.item.InvoiceDialog;
import org.kesler.fullyequip.gui.equipment.ContractEquipDialog;
import org.kesler.fullyequip.gui.equipment.ContractEquipDialogController;
import org.kesler.fullyequip.gui.equipment.PlaceEquipDialogController;
import org.kesler.fullyequip.gui.moving.MovingDialogController;
import org.kesler.fullyequip.gui.report.ReportDialogController;
import org.kesler.fullyequip.gui.util.InfoDialog;
import org.kesler.fullyequip.logic.*;

import javax.swing.*;

/**
 * Контроллер управления основным окном
 */
public class MainViewController {

    private static MainViewController instance = null;

    private MainView view;

    private MainViewController() {
        view = new MainView(this);
    }

    public static synchronized MainViewController getInstance() {
        if (instance == null) {
            instance = new MainViewController();
        }
        return instance;
    }


    public void showView() {
        view.setVisible(true);
    }

    public void showUnitTypeListDialog() {
        DefaultListDialogController.create(UnitType.class, "Типы оборудования").showDialog(view);
    }

    public void showPlacesDialog() {
        DefaultListDialogController.create(Place.class, "Места размещения").showDialog(view);
    }

    public void showSuppliersDialog() {
        DefaultListDialogController.create(Supplier.class, "Поставщики").showDialog(view);
    }


    public void showAuctionTypeDialog() {
        DefaultListDialogController.create(AuctionType.class, "Типы аукцонов").showDialog(view);
    }

    public void showAuctionsDialog() {
        DefaultListDialogController.create(Auction.class, "Аукционы").showDialog(view);
    }


    public void showContractsDialog() {
        DefaultListDialogController.create(Contract.class, "Договора").showDialog(view);
    }


    public void showIncomeDialog() {

        Contract contract = DefaultListDialogController.create(Contract.class, "Выберите договор поставки").showSelectDialog(view);
        if (contract != null) {
            ContractEquipDialogController.getInstance().showDialog(view,contract);
        }
    }

    public void showMovingDialog() {

        MovingDialogController.getInstance().showDialog(view);

    }

    void showReportDialog() {
        ReportDialogController.getInstance().showDialog(view);
    }

    void showPlaceEquipDialog() {
        PlaceEquipDialogController.getInstance().showDialog(view);
    }

}
