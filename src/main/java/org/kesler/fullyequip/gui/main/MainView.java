package org.kesler.fullyequip.gui.main;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Главное окно программы
 */
class MainView extends JFrame {

    private MainViewController controller;

    MainView(MainViewController controller) {
        super("Учет материальных средств");
        this.controller = controller;
        createGUI();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void createGUI() {

        JMenuBar menuBar = new JMenuBar();

        JMenu taskMenu = new JMenu("Задачи");

        JMenuItem newUnitsMenuItem = new JMenuItem("Поступление");
        newUnitsMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.showIncomeDialog();
            }
        });
        JMenuItem newMoveMenuItem = new JMenuItem("Перемещение");
        newMoveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.showMovingDialog();
            }
        });

        JMenuItem placesEquipMenuItem = new JMenuItem("Наличие");
        placesEquipMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.showPlaceEquipDialog();
            }
        });
        JMenuItem auctionsMenuItem = new JMenuItem("Аукционы");
        auctionsMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.showAuctionsDialog();
            }
        });
        JMenuItem contractsMenuItem = new JMenuItem("Договора");
        contractsMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.showContractsDialog();
            }
        });

        taskMenu.add(newUnitsMenuItem);
        taskMenu.add(newMoveMenuItem);
        taskMenu.add(placesEquipMenuItem);
        taskMenu.add(auctionsMenuItem);
        taskMenu.add(contractsMenuItem);


        JMenu dictMenu = new JMenu("Справочники");

        JMenuItem unitTypesMenuItem = new JMenuItem("Типы оборудования");
        unitTypesMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.showUnitTypeListDialog();
            }
        });

        JMenuItem placesMenuItem = new JMenuItem("Размещения");
        placesMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.showPlacesDialog();
            }
        });

        JMenuItem suppliersMenuItem = new JMenuItem("Поставщики");
        suppliersMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.showSuppliersDialog();
            }
        });

        JMenuItem auctionTypesMenuItem = new JMenuItem("Типы аукционов");
        auctionTypesMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.showAuctionTypeDialog();
            }
        });


        dictMenu.add(unitTypesMenuItem);
        dictMenu.add(placesMenuItem);
        dictMenu.add(suppliersMenuItem);
        dictMenu.add(auctionTypesMenuItem);

        menuBar.add(taskMenu);
        menuBar.add(dictMenu);

        setJMenuBar(menuBar);

        JPanel mainPanel = new JPanel(new BorderLayout());


        JPanel dataPanel = new JPanel(new MigLayout("fill"));

        JButton newUnitsButton = new JButton("Поступление");

        JButton newMoveButton = new JButton("Перемещение");

        JButton reportButton = new JButton("Отчеты");
        reportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.showReportDialog();
            }
        });

        JButton placeEquipButton = new JButton("Оборудование");
        placeEquipButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.showPlaceEquipDialog();
            }
        });

        dataPanel.add(newUnitsButton, "span, split 4");
        dataPanel.add(newMoveButton);
        dataPanel.add(reportButton);
        dataPanel.add(placeEquipButton);


        mainPanel.add(dataPanel, BorderLayout.CENTER);




        setContentPane(mainPanel);
        setSize(700,500);

    }


}
