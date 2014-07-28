package org.kesler.fullyequip.gui.dialog.item;

import com.alee.extended.date.WebDateField;
import net.miginfocom.swing.MigLayout;
import org.kesler.fullyequip.gui.dialog.ListDialogFactory;
import org.kesler.fullyequip.logic.Auction;
import org.kesler.fullyequip.logic.AuctionType;
import org.kesler.fullyequip.logic.model.DefaultModel;
import org.kesler.fullyequip.logic.model.ModelState;
import org.kesler.fullyequip.logic.model.ModelStateListener;
import org.kesler.fullyequip.util.ResourcesUtil;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

/**
 * Диалог редктирования аукциона
 */
public class AuctionDialog extends AbstractItemDialog<Auction>{

    private AuctionTypeComboBoxModel auctionTypeComboBoxModel;
    private JComboBox<AuctionType> auctionTypeComboBox;
    private AuctionType selectedAuctionType;
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

        auctionTypeComboBoxModel = new AuctionTypeComboBoxModel();
        auctionTypeComboBox = new JComboBox<AuctionType>(auctionTypeComboBoxModel);
        JButton editAuctionTypesButton = new JButton(ResourcesUtil.getIcon("table_edit.png"));
        editAuctionTypesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editAuctionTypes();
            }
        });

        numberTextField = new JTextField(20);
        nameTextArea = new JTextArea();
        JScrollPane nameTextAreaScrollPane = new JScrollPane(nameTextArea);
        priceTextField = new JFormattedTextField();
        NumberFormatter numberFormatter = new NumberFormatter(NumberFormat.getNumberInstance());
        DefaultFormatterFactory numberFormatterFactory = new DefaultFormatterFactory(numberFormatter,
                numberFormatter, numberFormatter);
        priceTextField.setFormatterFactory(numberFormatterFactory);
        holdingDateWebDateField = new WebDateField();

        dataPanel.add(new JLabel("Тип аукциона"));
        dataPanel.add(auctionTypeComboBox, "pushx, growx");
        dataPanel.add(editAuctionTypesButton, "wrap");
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

    private void editAuctionTypes() {
        AuctionType selectedAuctionType = ListDialogFactory.showSelectAuctionTypeListDialog(currentDialog);
        if (selectedAuctionType!=null) {
            auctionTypeComboBoxModel.update();
            auctionTypeComboBox.setSelectedItem(selectedAuctionType);
        }
    }

    class AuctionTypeComboBoxModel extends DefaultComboBoxModel<AuctionType> implements ModelStateListener {
        private DefaultModel<AuctionType> model;

        AuctionTypeComboBoxModel() {
            this.model = new DefaultModel<AuctionType>(AuctionType.class);
            model.addModelStateListener(this);
            model.readItemsInSeparateThread();
        }

        void update() {
            model.readItemsFromDB();
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
