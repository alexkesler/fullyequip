package org.kesler.fullyequip.gui.receive;

import org.kesler.fullyequip.gui.dialog.invoice.InvoiceDialog;
import org.kesler.fullyequip.logic.Contract;
import org.kesler.fullyequip.logic.Invoice;
import org.kesler.fullyequip.logic.model.DefaultModel;

import javax.swing.*;

/**
 * Контроллер для управления диалогом оснащения
 */
public class ContractEquipDialogController {

    private static ContractEquipDialogController instance = null;
    private ContractEquipDialog dialog;

    private Contract contract;

    private DefaultModel<Invoice> invoiceModel;
    private DefaultModel<Contract> contractModel;

    private ContractEquipDialogController() {
        contractModel = new DefaultModel<Contract>(Contract.class);
        invoiceModel = new DefaultModel<Invoice>(Invoice.class);
    }

    public static synchronized ContractEquipDialogController getInstance() {
        if (instance==null) {
            instance = new ContractEquipDialogController();
        }
        return instance;
    }

    public int showDialog(JDialog parentDialog, Contract contract) {
        this.contract = contract;
        dialog = new ContractEquipDialog(parentDialog, contract, this);
        dialog.setVisible(true);
        return dialog.getResult();
    }

    public int showDialog(JFrame parentFrame, Contract contract) {
        this.contract = contract;
        dialog = new ContractEquipDialog(parentFrame, contract, this);
        dialog.setVisible(true);
        return dialog.getResult();
    }

    Contract getContract() {return contract;}

    /**
     * Добавляем накладную в договор
     */
    void addInvoice() {
        InvoiceDialog invoiceDialog = new InvoiceDialog(dialog,contract);
        invoiceDialog.setVisible(true);
        if (invoiceDialog.getResult()==InvoiceDialog.OK) {
            Invoice invoice = invoiceDialog.getInvoice();
            contract.getInvoices().add(invoice);
            invoiceModel.addItem(invoice);
        }

    }

    /**
     * Вызываем диалог редактирования накладной
     * @param invoice накладная
     * @return результат редактирования (ОК-Отмена)
     */
    boolean editInvoice(Invoice invoice) {
        InvoiceDialog invoiceDialog = new InvoiceDialog(dialog,invoice);
        invoiceDialog.setVisible(true);
        if (invoiceDialog.getResult()==InvoiceDialog.OK) {
            invoiceModel.updateItem(invoice);
            return true;
        }  else {
            return false;
        }
    }

    /**
     * Удаляем накладную, предварительно спросив у пользователя
     * @param invoice
     * @return
     */
    boolean removeInvoice(Invoice invoice) {
        int result = JOptionPane.showConfirmDialog(dialog,
                "Удалить накладную: " + invoice +"?",
                "Внимание",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            contract.getInvoices().remove(invoice);
            invoiceModel.removeItem(invoice);
            return true;
        } else {
            return false;
        }

    }

    void saveContract() {
        contractModel.saveOrUpdateItem(contract);
    }

}
