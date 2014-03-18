package org.kesler.fullyequip.gui.main;

import org.kesler.fullyequip.gui.dialog.ListDialogController;
import org.kesler.fullyequip.gui.equipment.ContractEquipDialogController;
import org.kesler.fullyequip.gui.equipment.PlaceEquipDialogController;
import org.kesler.fullyequip.gui.moving.MovingDialogController;
import org.kesler.fullyequip.gui.report.ReportDialogController;
import org.kesler.fullyequip.logic.*;

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
        ListDialogController.create(UnitType.class, "Типы оборудования").showDialog(view);
    }

    public void showPlacesDialog() {
        ListDialogController.create(Place.class, "Места размещения").showDialog(view);
    }

    public void showSuppliersDialog() {
        ListDialogController.create(Supplier.class, "Поставщики").showDialog(view);
    }


    public void showAuctionTypeDialog() {
        ListDialogController.create(AuctionType.class, "Типы аукцонов").showDialog(view);
    }

    public void showAuctionsDialog() {
        ListDialogController.create(Auction.class, "Аукционы").showDialog(view);
    }


    public void showContractsDialog() {
        ListDialogController.create(Contract.class, "Договора").showDialog(view);
    }


    public void showIncomeDialog() {

        Contract contract = ListDialogController.create(Contract.class, "Выберите договор поставки").showSelectDialog(view);
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
