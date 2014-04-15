package org.kesler.fullyequip.gui.dialog.invoice;

import org.kesler.fullyequip.gui.dialog.AbstractDialog;
import org.kesler.fullyequip.logic.InvoicePosition;

import javax.swing.*;

/**
 * Диалог редактирования позиции в накладной
 */
public class InvoicePositionDialog extends AbstractDialog {

    private InvoicePosition invoicePosition;

    public InvoicePositionDialog(JDialog parentDialog) {
        super(parentDialog, true);
        createGUI();
        pack();
        setLocationRelativeTo(parentDialog);
    }

    public InvoicePosition getInvoicePosition() {return invoicePosition;}

    private void createGUI() {

        JPanel mainPanel = new JPanel();




        setContentPane(mainPanel);

    }
}
