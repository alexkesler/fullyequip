package org.kesler.fullyequip.gui.dialog.item;

import com.alee.extended.date.WebDateField;
import net.miginfocom.swing.MigLayout;
import org.kesler.fullyequip.gui.dialog.AbstractItemDialog;
import org.kesler.fullyequip.gui.dialog.ListDialogController;
import org.kesler.fullyequip.logic.Auction;
import org.kesler.fullyequip.logic.Contract;
import org.kesler.fullyequip.logic.Supplier;
import org.kesler.fullyequip.util.ResourcesUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

/**
 * Диалог для редактирования Договора
 */
public class ContractDialog extends AbstractItemDialog<Contract>{

    private JTextField numberTextField;
    private WebDateField dateWebDateField;
    private Supplier supplier;
    private JLabel supplierLabel;
    private JRadioButton byAuctionRadioButton;
//    private WebCollapsiblePane auctionWebCollapsiblePane;
    private JPanel auctionPanel;
    private Auction auction;
    private JLabel auctionLabel;

    public ContractDialog(JDialog parentDialog) {
        super(parentDialog);
    }

    public ContractDialog(JDialog parentDialog, Contract contract) {
        super(parentDialog, contract);
    }

    @Override
    protected void createNewItem() {
        item = new Contract();
        item.setDate(new Date());
        item.setByAuction(true);
    }

    @Override
    protected JPanel createItemPanel() {

        JPanel dataPanel = new JPanel(new MigLayout("fillx"));

        numberTextField = new JTextField(7);
        dateWebDateField = new WebDateField();
        supplierLabel = new JLabel("Не определен");
        supplierLabel.setBorder(BorderFactory.createEtchedBorder());
        JButton selectSupplierButton = new JButton();
        selectSupplierButton.setIcon(ResourcesUtil.getIcon("book_previous.png"));
        selectSupplierButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setSupplier(ListDialogController.create(Supplier.class, "Поставщики").showSelectDialog(currentDialog));
            }
        });

        ButtonGroup byAuctionButtonGroup = new ButtonGroup();

        byAuctionRadioButton = new JRadioButton("Да");
        byAuctionRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (byAuctionRadioButton.isSelected()) {
                    auctionPanel.setVisible(true);
//                    auctionWebCollapsiblePane.setExpanded(true);
//                    System.out.println("----By auction----");
                    pack();
                }
            }
        });
        JRadioButton notByAuctionRadioButton = new JRadioButton("Нет");
        notByAuctionRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                auctionPanel.setVisible(false);
//                auctionWebCollapsiblePane.setExpanded(false);
                setAuction(null);
//                System.out.println("----Not by auction----");
                pack();
            }
        });
        byAuctionButtonGroup.add(byAuctionRadioButton);
        byAuctionButtonGroup.add(notByAuctionRadioButton);

        auctionPanel = new JPanel(new MigLayout("fillx, nogrid, insets 0"));
        auctionLabel = new JLabel("Не определен");
        auctionLabel.setBorder(BorderFactory.createEtchedBorder());
        JButton selectAuctionButton = new JButton();
        selectAuctionButton.setIcon(ResourcesUtil.getIcon("book_previous.png"));
        selectAuctionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setAuction(ListDialogController.create(Auction.class, "Аукционы").showSelectDialog(currentDialog));
            }
        });

        auctionPanel.add(new JLabel("Аукцион: "));
        auctionPanel.add(auctionLabel, "pushx, grow");
        auctionPanel.add(selectAuctionButton);
//        auctionWebCollapsiblePane = new WebCollapsiblePane("Аукцион", auctionPanel);
//        auctionWebCollapsiblePane.setExpanded(false);



        dataPanel.add(new JLabel("Номер: "));
        dataPanel.add(numberTextField,"span, split 3");
        dataPanel.add(new JLabel(" от "));
        dataPanel.add(dateWebDateField, "wrap");
        dataPanel.add(new JLabel("Поставщик: "));
        dataPanel.add(supplierLabel,"pushx, grow");
        dataPanel.add(selectSupplierButton, "wrap");
        dataPanel.add(new JLabel("По аукциону: "));
        dataPanel.add(byAuctionRadioButton, "span, split 2");
        dataPanel.add(notByAuctionRadioButton, "wrap");
        dataPanel.add(auctionPanel, "span, growx");


        dataPanel.setPreferredSize(new Dimension(500, 170));

        return dataPanel;

    }

    @Override
    protected void loadGUIFromItem() {

        String number = item.getNumber();
        Date date = item.getDate();
        Supplier sup = item.getSupplier();
        Boolean byAuction = item.isByAuction();
        Auction auc = item.getAuction();


        numberTextField.setText(number);
        dateWebDateField.setDate(date);
        setSupplier(sup);
        byAuctionRadioButton.setSelected(byAuction == null ? false : byAuction);
        auctionPanel.setVisible(byAuctionRadioButton.isSelected());
        setAuction(auc);


    }

    @Override
    protected boolean readItemFromGUI() {

        String number = numberTextField.getText();
        Date date = dateWebDateField.getDate();
        Boolean byAuction = byAuctionRadioButton.isSelected();

        if(supplier==null) {
            JOptionPane.showMessageDialog(currentDialog, "Необходимо выбрать поставщика","Ошибка",JOptionPane.ERROR_MESSAGE);
            return false;
        }

        item.setNumber(number);
        item.setDate(date);
        item.setSupplier(supplier);
        item.setByAuction(byAuction);
        item.setAuction(auction);


        return true;

    }

    @Override
    protected boolean checkChanged() {

        if(!numberTextField.getText().equals(item.getNumber())) return true;
        if(!dateWebDateField.getDate().equals(item.getDate())) return true;
        if(byAuctionRadioButton.isSelected()!=item.isByAuction()) return true;
        if(supplier!=null && !supplier.equals(item.getSupplier())) return true;
        if(auction!=null && !auction.equals(item.getAuction())) return true;


        return true;
    }

    private void setSupplier(Supplier supplier) {
        this.supplier = supplier;
        String supString = supplier==null?"Не определен":supplier.getName();
        supplierLabel.setText("<html>" + supString + "</html>");
    }

    private void setAuction(Auction auction) {
        this.auction = auction;
        auctionLabel.setText(auction==null?"Не определен":auction.toString());

    }


}
