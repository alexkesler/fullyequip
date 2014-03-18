package org.kesler.fullyequip.gui.dialog.item;

import com.alee.extended.date.WebDateField;
import net.miginfocom.swing.MigLayout;
import org.kesler.fullyequip.logic.Auction;
import org.kesler.fullyequip.logic.AuctionType;
import org.kesler.fullyequip.logic.model.DefaultModel;
import org.kesler.fullyequip.logic.model.ModelState;
import org.kesler.fullyequip.logic.model.ModelStateListener;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

/**
 * Диалог редктирования аукциона
 */
public class AuctionDialog extends AbstractItemDialog<Auction>{

    private JComboBox<AuctionType> auctionTypeComboBox;
    private JTextField numberTextField;
    private JTextArea nameTextArea;
    private JFormattedTextField priceTextField;
    private WebDateField holdingDateWebDateField;


    public AuctionDialog(JDialog parentDialog) {
        super(parentDialog);

    }

    public AuctionDialog(JDialog parentDialog, Auction auction) {
        super(parentDialog, auction);
    }

    @Override
    protected JPanel createItemPanel() {

        JPanel dataPanel = new JPanel(new MigLayout("fill"));


        auctionTypeComboBox = new JComboBox<AuctionType>(new AuctionTypeComboBoxModel());


        numberTextField = new JTextField(20);
        nameTextArea = new JTextArea();
        JScrollPane nameTextAreaScrollPane = new JScrollPane(nameTextArea);
        priceTextField = new JFormattedTextField();
        NumberFormatter currencyFormatter = new NumberFormatter(NumberFormat.getCurrencyInstance());
        DefaultFormatterFactory currencyFormatterFactory = new DefaultFormatterFactory(currencyFormatter,
                                                                currencyFormatter, currencyFormatter);
        priceTextField.setFormatterFactory(currencyFormatterFactory);
        holdingDateWebDateField = new WebDateField();

        dataPanel.add(new JLabel("Тип аукциона"));
        dataPanel.add(auctionTypeComboBox, "pushx, growx, wrap");
        dataPanel.add(new JLabel("Номер"));
        dataPanel.add(numberTextField, "wrap");
        dataPanel.add(new JLabel("Наименование"), "wrap");
        dataPanel.add(nameTextAreaScrollPane, "span, pushy, grow");
        dataPanel.add(new JLabel("Цена аукциона"));
        dataPanel.add(priceTextField, "growx, wrap");
        dataPanel.add(new JLabel("Дата аукциона"));
        dataPanel.add(holdingDateWebDateField);

        dataPanel.setPreferredSize(new Dimension(500,200));

        return dataPanel;
    }

    @Override
    protected void createNewItem() {
        item = new Auction();
        item.setHoldingDate(new Date());
    }

    @Override
    protected void loadGUIFromItem() {

        AuctionType auctionType = item.getType();
        String number = item.getNumber();
        String name = item.getName();
        Float price = item.getPrice();
        Date holdingDate = item.getHoldingDate();

        auctionTypeComboBox.setSelectedItem(auctionType);
        numberTextField.setText(number == null ? "" : number);
        nameTextArea.setText(name == null ? "" : name);
        priceTextField.setValue(price == null ? 0 : price);
        holdingDateWebDateField.setDate(holdingDate);


    }

    @Override
    protected boolean readItemFromGUI() {

        AuctionType type = (AuctionType) auctionTypeComboBox.getSelectedItem();
        String number = numberTextField.getText();
        String name = nameTextArea.getText();
//        String priceString = priceTextField.getText();
        Float price = ((Number) priceTextField.getValue()).floatValue();
//        try {
//            price = Float.parseFloat(priceString);
//        } catch (NumberFormatException nfe) {
//            JOptionPane.showMessageDialog(currentDialog,"Стоимость должна быть цифровой");
//            return false;
//        }

        Date holdingDate = holdingDateWebDateField.getDate();

        item.setType(type);
        item.setNumber(number);
        item.setName(name);
        item.setPrice(price);
        item.setHoldingDate(holdingDate);


        return true;
    }


    @Override
    protected boolean checkChanged() {

        AuctionType type = (AuctionType) auctionTypeComboBox.getSelectedItem();
        if (type != null && !type.equals(item.getType())) return true;

        if (!numberTextField.getText().equals(item.getNumber())) return true;
        if (!nameTextArea.getText().equals(item.getName())) return true;

        Float price = ((Number) priceTextField.getValue()).floatValue();
        if(!price.equals(item.getPrice())) return true;
        if(!holdingDateWebDateField.getDate().equals(item.getHoldingDate())) return true;

        return false;
    }

    class AuctionTypeComboBoxModel extends DefaultComboBoxModel<AuctionType> implements ModelStateListener {
        private DefaultModel<AuctionType> model;

        AuctionTypeComboBoxModel() {
            this.model = new DefaultModel<AuctionType>(AuctionType.class);
            model.addModelStateListener(this);
            model.readItemsInSeparateThread();
        }

        @Override
        public void modelStateChanged(ModelState state) {
            if (state == ModelState.UPDATED) {
                removeAllElements();
                List<AuctionType> types = model.getAllItems();
                for(AuctionType type:types) addElement(type);
            }
        }

    }

}
