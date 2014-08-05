package org.kesler.fullyequip.gui.main;

import net.miginfocom.swing.MigLayout;
import org.kesler.fullyequip.logic.Place;
import org.kesler.fullyequip.logic.Unit;
import org.kesler.fullyequip.logic.UnitState;
import org.kesler.fullyequip.logic.UnitType;
import org.kesler.fullyequip.util.ResourcesUtil;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * Главное окно программы
 */
class MainView extends JFrame {

    private MainViewController controller;

    private JTree summaryTree;
    private DefaultTreeModel summaryTreeModel;

    MainView(MainViewController controller) {
        super("Учет материальных средств");
        this.controller = controller;
        createGUI();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLocale(new Locale("ru", "RU"));
        getInputContext().selectInputMethod(new Locale("ru","RU"));
    }

    private void createGUI() {

        JMenuBar menuBar = new JMenuBar();

        JMenu taskMenu = new JMenu("Задачи");

        JMenuItem newUnitsMenuItem = new JMenuItem("Поступление");
        newUnitsMenuItem.setIcon(ResourcesUtil.getIcon("server_add.png"));
        newUnitsMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.showIncomeDialog();
            }
        });
        JMenuItem newMoveMenuItem = new JMenuItem("Перемещение");
        newMoveMenuItem.setIcon(ResourcesUtil.getIcon("server_go.png"));
        newMoveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.showMovingDialog();
            }
        });


        JMenuItem closeMenuItem = new JMenuItem("Закрыть");
        closeMenuItem.setIcon(ResourcesUtil.getIcon("door_out.png"));
        closeMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.exit();
            }
        });

        taskMenu.add(newUnitsMenuItem);
        taskMenu.add(newMoveMenuItem);
        taskMenu.add(closeMenuItem);


        JMenu dictMenu = new JMenu("Справочники");

        JMenuItem unitTypesMenuItem = new JMenuItem("Типы оборудования");
        unitTypesMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.showUnitTypeListDialog();
            }
        });

        JMenuItem unitStatesMenuItem = new JMenuItem("Состояния оборудования");
        unitStatesMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.showUnitStateListDialog();
            }
        });

        JMenuItem auctionTypesMenuItem = new JMenuItem("Типы аукционов");
        auctionTypesMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.showAuctionTypeDialog();
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


        dictMenu.add(unitTypesMenuItem);
        dictMenu.add(unitStatesMenuItem);
        dictMenu.add(auctionTypesMenuItem);
        dictMenu.add(placesMenuItem);
        dictMenu.add(suppliersMenuItem);
        dictMenu.add(auctionsMenuItem);
        dictMenu.add(contractsMenuItem);


        JMenu reportMenu = new JMenu("Отчеты");

        JMenuItem reestrMenuItem = new JMenuItem("Реестр");
        reestrMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.showReestrView();
            }
        });

        reportMenu.add(reestrMenuItem);

        JMenu optionsMenu = new JMenu("Настройки");

        JMenuItem optionsMenuItem = new JMenuItem("Подключение");
        optionsMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.showOptions();
            }
        });

        optionsMenu.add(optionsMenuItem);


        JMenu helpMenu = new JMenu("Справка");

        JMenuItem aboutMenuItem = new JMenuItem("О программе");
        aboutMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.showAbout();
            }
        });

        helpMenu.add(aboutMenuItem);


        menuBar.add(taskMenu);
        menuBar.add(dictMenu);
        menuBar.add(reportMenu);
        menuBar.add(optionsMenu);
        menuBar.add(helpMenu);

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

        JButton reloadTreeButton = new JButton("Обновить");
        reloadTreeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.reloadTree();
            }
        });

        JButton placeEquipButton = new JButton("Оборудование");
        placeEquipButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.showPlaceEquipDialog();
            }
        });

        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Филиалы");
        summaryTree = new JTree(rootNode);
        summaryTreeModel = (DefaultTreeModel) summaryTree.getModel();
        JScrollPane summaryTreeScrollPane = new JScrollPane(summaryTree);

        dataPanel.add(newUnitsButton, "span, split 5");
        dataPanel.add(newMoveButton);
        dataPanel.add(reportButton);
        dataPanel.add(placeEquipButton);
        dataPanel.add(reloadTreeButton);
        dataPanel.add(summaryTreeScrollPane,"push,grow");


        mainPanel.add(dataPanel, BorderLayout.CENTER);




        setContentPane(mainPanel);
        setSize(700,500);

    }

    void reloadTree(Set<Place> places) {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) summaryTreeModel.getRoot();
        root.removeAllChildren();

        for(Place place: places) {
            DefaultMutableTreeNode placeNode = new DefaultMutableTreeNode(place.getName());
            Map<UnitType,DefaultMutableTreeNode> unitTypesMap = new HashMap<UnitType,DefaultMutableTreeNode>();

            // запоминаем все имеющиеся типы
            for (Unit unit:place.getUnits()) {
                if(!unitTypesMap.containsKey(unit.getType())) {
                    DefaultMutableTreeNode typeNode = new DefaultMutableTreeNode(unit.getTypeName());
                    unitTypesMap.put(unit.getType(), typeNode);
                    placeNode.add(typeNode);
                }
            }

//            Map<SameUnit, DefaultMutableTreeNode> sameUnits = new HashMap<SameUnit, DefaultMutableTreeNode>();
            Set<SameUnit> sameUnits = new HashSet<SameUnit>();
            // выделяем аналогичное оборудовние
            boolean fit;
            for (Unit unit:place.getUnits()) {
                fit = false;
                for (SameUnit sameUnit:sameUnits) {
                    if (sameUnit.mergeUnit(unit)) {fit = true; break;}
                }
                if(!fit) {
                    SameUnit sameUnit = new SameUnit(unit);
                    sameUnits.add(sameUnit);
                }
            }


            // заполняем оборудование по типам
            for (SameUnit sameUnit:sameUnits) {
                DefaultMutableTreeNode typeNode = unitTypesMap.get(sameUnit.getType());

                String unitString = sameUnit.getName() + " - " + sameUnit.getQuantity() + "шт.(" + sameUnit.getState()+ ")";
                typeNode.add(new DefaultMutableTreeNode(unitString));
            }
//            // заполняем оборудование по типам
//            for (Unit unit:place.getUnits()) {
//                DefaultMutableTreeNode typeNode = unitTypesMap.get(unit.getType());
//
//                String unitString = unit.getName() + " - " + unit.getQuantity() + "шт.";
//                typeNode.add(new DefaultMutableTreeNode(unitString));
//            }


            root.add(placeNode);
        }

        summaryTreeModel.reload();
    }

    class SameUnit {
        private UnitType type;
        private String name;
        private UnitState state;
        private Long quantity;

        SameUnit(Unit unit) {
            type = unit.getType();
            name = unit.getName();
            state = unit.getState();
            quantity = unit.getQuantity();
        }

        boolean mergeUnit(Unit unit) {
            if (!type.equals(unit.getType())) return false;
            if (!name.equals(unit.getName())) return false;
            if (!state.equals(unit.getState())) return false;
            quantity += unit.getQuantity();
            return true;
        }

        public UnitType getType() {
            return type;
        }

        public String getName() {
            return name;
        }

        public UnitState getState() {
            return state;
        }

        public Long getQuantity() {
            return quantity;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            SameUnit sameUnit = (SameUnit) o;

            if (!name.equals(sameUnit.name)) return false;
            if (quantity != null ? !quantity.equals(sameUnit.quantity) : sameUnit.quantity != null) return false;
            if (!state.equals(sameUnit.state)) return false;
            if (!type.equals(sameUnit.type)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = type.hashCode();
            result = 31 * result + name.hashCode();
            result = 31 * result + state.hashCode();
            result = 31 * result + (quantity != null ? quantity.hashCode() : 0);
            return result;
        }
    }

}
