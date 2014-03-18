package org.kesler.fullyequip.gui.dialog.item;


import net.miginfocom.swing.MigLayout;
import org.kesler.fullyequip.logic.Place;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * Диалог для редактирования размещения
 */
public class PlaceDialog extends AbstractItemDialog<Place> {


    private JTextField nameTextField;
    private JRadioButton storageRadioButton;
    private JRadioButton filialRadioButton;
    private JTextField windowsCountTextField;
    private JTextField supervisorTextField;
    private JTextField addressTextField;
    private JTextArea contactsTextArea;
    private JPanel filialInfoPanel;


    public PlaceDialog(JDialog parentDialog) {
        super(parentDialog);
    }

    public PlaceDialog(JDialog parentDialog, Place place) {
        super(parentDialog, place);
    }

    @Override
    protected void createNewItem() {
        item = new Place();
        item.setStorage(false);
    }

    @Override
    protected JPanel createItemPanel() {


        // Панель данных
        final JPanel dataPanel = new JPanel(new MigLayout("fill"));

        nameTextField = new JTextField(30);
        ButtonGroup storageButtonGroup = new ButtonGroup();
        storageRadioButton = new JRadioButton("Склад");
        storageRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filialInfoPanel.setVisible(false);

            }
        });
        filialRadioButton = new JRadioButton("Филиал");
        filialRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filialInfoPanel.setVisible(true);

            }
        });
        storageButtonGroup.add(storageRadioButton);
        storageButtonGroup.add(filialRadioButton);


        windowsCountTextField = new JTextField(10);
        supervisorTextField = new JTextField(20);
        addressTextField = new JTextField(30);

        filialInfoPanel = new JPanel(new MigLayout("fill, insets 0"));
        filialInfoPanel.add(new JLabel("Кол-во окон: "));
        filialInfoPanel.add(windowsCountTextField, "w 50, wrap");
        filialInfoPanel.add(new JLabel("Начальник: "));
        filialInfoPanel.add(supervisorTextField, "wrap");
        filialInfoPanel.add(new JLabel("Адрес: "));
        filialInfoPanel.add(addressTextField,"wrap");



        contactsTextArea = new JTextArea(10,5);
        JScrollPane contactsTextAreaScrollPane = new JScrollPane(contactsTextArea);



        // Собираем панель данных
        dataPanel.add(new JLabel("Наименование: "));
        dataPanel.add(nameTextField, "wrap");
        dataPanel.add(filialRadioButton,"skip, span, split 2");
        dataPanel.add(storageRadioButton, "wrap");
        dataPanel.add(filialInfoPanel,"span");
        dataPanel.add(new JLabel("Контакты: "), "wrap");
        dataPanel.add(contactsTextAreaScrollPane, "span, push, grow");


        return dataPanel;

    }

    @Override
    protected void loadGUIFromItem() {

        String name = item.getName();
        Boolean storage = item.isStorage();
        Integer windowsCount = item.getWindowsCount();
        String supervisor = item.getSupervisor();
        String address = item.getAddress();
        String contacts = item.getContacts();


        nameTextField.setText(name == null ? "" : name);

        storage = storage==null?false:storage;
        if(storage) storageRadioButton.setSelected(true);
        else filialRadioButton.setSelected(true);

        windowsCountTextField.setText(windowsCount == null ? "" : windowsCount.toString());

        supervisorTextField.setText(supervisor == null ? "" : supervisor);

        addressTextField.setText(address==null ? "" : address);

        contactsTextArea.setText(contacts == null ? "" : contacts);


    }

    @Override
    protected boolean readItemFromGUI() {

        String name = nameTextField.getText();
        Boolean storage = storageRadioButton.isSelected();
        String windowsCountString = windowsCountTextField.getText();

        Integer windowsCount = null;

        String supervisor = supervisorTextField.getText();
        String address = addressTextField.getText();
        String contacts = contactsTextArea.getText();



        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Поле Наименование не может быть пустым", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return false;
        }


        if (!windowsCountString.isEmpty()) {
            try {
                windowsCount =  Integer.parseInt(windowsCountString);
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(currentDialog,"Кол-во окон должно быть целым числом", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }

        item.setName(name);
        item.setStorage(storage);
        item.setWindowsCount(windowsCount);
        item.setSupervisor(supervisor);
        item.setAddress(address);
        item.setContacts(contacts);

        return true;
    }

    @Override
    protected boolean checkChanged() {



        return true;
    }

}
