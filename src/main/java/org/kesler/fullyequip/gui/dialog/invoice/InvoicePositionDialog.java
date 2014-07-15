package org.kesler.fullyequip.gui.dialog.invoice;

import net.miginfocom.swing.MigLayout;
import org.kesler.fullyequip.gui.dialog.AbstractDialog;
import org.kesler.fullyequip.logic.Invoice;
import org.kesler.fullyequip.logic.InvoicePosition;
import org.kesler.fullyequip.logic.UnitType;
import org.kesler.fullyequip.logic.model.DefaultModel;
import org.kesler.fullyequip.logic.model.ModelState;
import org.kesler.fullyequip.logic.model.ModelStateListener;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

/**
 * Диалог редактирования позиции в накладной
 */
public class InvoicePositionDialog extends AbstractDialog {

    private InvoicePosition invoicePosition;

    private JComboBox<UnitType> unitTypeComboBox;
    private JTextField unitNameTextField;
    private JFormattedTextField priceTextField;
    private JFormattedTextField quantityTextField;
    private JCheckBox invRegCheckBox;

    public InvoicePositionDialog(JDialog parentDialog, Invoice invoice) {
        super(parentDialog, true);
        createGUI();
        invoicePosition = new InvoicePosition();
        invoicePosition.setInvoice(invoice);
        loadGUIFromInvoicePosition();
        pack();
        setLocationRelativeTo(parentDialog);
    }


    public InvoicePositionDialog(JDialog parentDialog, InvoicePosition invoicePosition) {
        super(parentDialog, true);
        createGUI();
        this.invoicePosition = invoicePosition;
        loadGUIFromInvoicePosition();
        pack();
        setLocationRelativeTo(parentDialog);
    }


    public InvoicePosition getInvoicePosition() {return invoicePosition;}

    private void createGUI() {

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel dataPanel = new JPanel(new MigLayout("fill"));

        unitTypeComboBox = new JComboBox<UnitType>(new UnitTypeComboBoxModel());
        unitNameTextField = new JTextField(15);
        priceTextField = new JFormattedTextField(new NumberFormatter(NumberFormat.getNumberInstance()));
        quantityTextField = new JFormattedTextField(new NumberFormatter(NumberFormat.getIntegerInstance()));
        invRegCheckBox = new JCheckBox();

        dataPanel.add(new JLabel("Тип оборудования"));
        dataPanel.add(unitTypeComboBox,"wrap");
        dataPanel.add(new JLabel("Наименование"));
        dataPanel.add(unitNameTextField, "wrap");
        dataPanel.add(new JLabel("Цена"));
        dataPanel.add(priceTextField, "w 100, wrap");
        dataPanel.add(new JLabel("Количество"));
        dataPanel.add(quantityTextField, "w 50, wrap");
        dataPanel.add(new JLabel("Инвентарный учет"));
        dataPanel.add(invRegCheckBox, "wrap");

        JPanel buttonPanel = new JPanel();

        JButton okButton = new JButton("Ок");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (readInvoicePositionFromGUI()) {
                    result = OK;
                }
                setVisible(false);
            }
        });

        JButton cancelButton = new JButton("Отмена");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(dataPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);

    }

    private boolean readInvoicePositionFromGUI() {

        String name = unitNameTextField.getText();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(currentDialog,"Поле Наименование не может быть пустым", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        Double price = ((Number)priceTextField.getValue()).doubleValue();

        Long quantity = ((Number)quantityTextField.getValue()).longValue();

        invoicePosition.setUnitType((UnitType)unitTypeComboBox.getSelectedItem());
        invoicePosition.setName(name);
        invoicePosition.setPrice(price);
        invoicePosition.setQuantity(quantity);
        invoicePosition.setInvReg(invRegCheckBox.isSelected());
//        if (invoicePosition.getUnits().size()==0) invoicePosition.createUnits();

        return true;
    }

    private void loadGUIFromInvoicePosition() {
        unitNameTextField.setText(invoicePosition.getName());
        unitTypeComboBox.setSelectedItem(invoicePosition.getUnitType());
        priceTextField.setValue(invoicePosition.getPrice());
        quantityTextField.setValue(invoicePosition.getQuantity());
        invRegCheckBox.setSelected(invoicePosition.isInvReg()==null?false:invoicePosition.isInvReg());
    }

    // Модель для типов оборудования
    class UnitTypeComboBoxModel extends DefaultComboBoxModel<UnitType> implements ModelStateListener {

        private DefaultModel<UnitType> model;

        UnitTypeComboBoxModel() {
            model = new DefaultModel<UnitType>(UnitType.class);
            model.addModelStateListener(this);
            model.readItemsInSeparateThread();
        }

        @Override
        public void modelStateChanged(ModelState state) {
            if(state == ModelState.UPDATED) {
                removeAllElements();
                java.util.List<UnitType> types = model.getAllItems();
                for(UnitType type:types) addElement(type);
            }
        }

    }


}
