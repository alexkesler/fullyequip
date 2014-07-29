package org.kesler.fullyequip.gui.reestr;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import net.miginfocom.swing.MigLayout;
import org.kesler.fullyequip.gui.reestr.column.ReestrColumn;
import org.kesler.fullyequip.gui.reestr.column.ReestrColumns;
import org.kesler.fullyequip.logic.Unit;
import org.kesler.fullyequip.logic.unitsfilter.UnitsFilter;
import org.kesler.fullyequip.logic.unitsfilter.UnitsFiltersEnum;
import org.kesler.fullyequip.util.ResourcesUtil;


public class ReestrView extends JFrame {

	private final boolean DEBUG = true;
	
	private ReestrViewController controller;

	private JTable reestrTable;
	private ReestrTableModel reestrTableModel;

	private Action openReceptionAction;

	private FilterListModel filterListModel;
	private int selectedFilterIndex = -1;
	private UnitsFilter selectedFilter = null;

	public ReestrView(ReestrViewController controller, JFrame parentFrame) {
		super("Реестр оборудования");
		this.controller = controller;
		createGUI();

		this.setLocationRelativeTo(parentFrame);
	}
	
	public void setUnits(List<Unit> units) {
		reestrTableModel.setUnits(units);
	}

	private void createGUI() {
		JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel(new MigLayout("fill"));

		JPanel filterPanel = new JPanel(new MigLayout("fill"));
        filterPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Фильтр"));

		// делаем лист для отображения и измененения набора фильтров
		filterListModel = new FilterListModel();
		final JList filterList = new JList(filterListModel);
		filterList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		filterList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent lse) {
				if(lse.getValueIsAdjusting() == false) {
					selectedFilterIndex = filterList.getSelectedIndex();
					if (DEBUG) System.out.println("selected filter item: " + selectedFilterIndex);
					if (selectedFilterIndex != -1) {
						selectedFilter = controller.getFilters().get(selectedFilterIndex);
					} else {
						selectedFilter = null;
					}

				}				
			}
		});
        filterList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount()==2) {
                    controller.editFilter(selectedFilterIndex);
                }
            }
        });
		JScrollPane filterListScrollPane = new JScrollPane(filterList);

		////// кнопки управления набором фильтров

		final JPopupMenu filtersPopupMenu = new JPopupMenu();


		// кнопка добавления
		final JButton addFilterButton = new JButton();
		addFilterButton.setIcon(ResourcesUtil.getIcon("add.png"));
        addFilterButton.setToolTipText("Добавить");
		addFilterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				filtersPopupMenu.show(addFilterButton, addFilterButton.getWidth(), 0);
			}
		});
		// Пункт меню - добавление фильтра по размещению
		JMenuItem placeFilterMenuItem = new JMenuItem("По размещению");
		placeFilterMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                controller.addFilter(UnitsFiltersEnum.PLACE);
            }
        });
        // Пункт меню - добавление фильтра по типу аукциона
        JMenuItem auctionTypeFilterMenuItem = new JMenuItem("По типу аукциона");
        auctionTypeFilterMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                controller.addFilter(UnitsFiltersEnum.AUCTION_TYPE);
            }
        });
        // Пункт меню - добавление фильтра по состоянию
        JMenuItem stateFilterMenuItem = new JMenuItem("По состоянию");
        stateFilterMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                controller.addFilter(UnitsFiltersEnum.STATE);
            }
        });


        // собираем всплывающее меню добавления фильтра
		filtersPopupMenu.add(placeFilterMenuItem);
        filtersPopupMenu.add(auctionTypeFilterMenuItem);
        filtersPopupMenu.add(stateFilterMenuItem);

		// кнопка реадктирования
		JButton editFilterButton = new JButton();
		editFilterButton.setIcon(ResourcesUtil.getIcon("pencil.png"));
        editFilterButton.setToolTipText("Изменить");
		editFilterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.editFilter(selectedFilterIndex);
			}
		});


		// кнопка удаления фильтра
		JButton removeFilterButton = new JButton();
		removeFilterButton.setIcon(ResourcesUtil.getIcon("delete.png"));
        removeFilterButton.setToolTipText("Удалить");
		removeFilterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.removeFilter(selectedFilterIndex);
			}
		});

		// кнопка очистки фильтра
		JButton resetFiltersButton = new JButton("Очистить");
		resetFiltersButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.resetFilters();
			}
		});

		/// Применить фильтры
		JButton applyFiltersButton = new JButton("Применить");
		applyFiltersButton.setIcon(ResourcesUtil.getIcon("tick.png"));
		applyFiltersButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.applyFilters();
				reestrTableModel.fireTableDataChanged();
				//setReestrTableColumnsWidth();
			}
		});

        /// Перечитать из БД и применить фильтры
        JButton readFromDBButton = new JButton("Перечитать");
        readFromDBButton.setIcon(ResourcesUtil.getIcon("database_refresh.png"));
        readFromDBButton.setToolTipText("Перечитать из базы данных");
        readFromDBButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.readFromDBAndApplyFilters();
            }
        });



		// Собираем панель фильтра
		filterPanel.add(new JLabel("Фильтры: "), "wrap");
		filterPanel.add(filterListScrollPane, "push, spany 2, w 500, h 80");
        filterPanel.add(readFromDBButton, "wrap");
		filterPanel.add(applyFiltersButton,"pushy, grow, wrap");
		filterPanel.add(addFilterButton, "split");
		filterPanel.add(editFilterButton);
		filterPanel.add(removeFilterButton);
		filterPanel.add(resetFiltersButton, "wrap");

        JPanel exportPanel = new JPanel();
        exportPanel.setLayout(new BoxLayout(exportPanel,BoxLayout.Y_AXIS));
        exportPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Ведомости"));

        JButton exportSelectedColumnsButton = new JButton("Список");
        exportSelectedColumnsButton.setIcon(ResourcesUtil.getIcon("table.png"));
        exportSelectedColumnsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                controller.exportSelectedColumns();
            }
        });


        exportPanel.add(exportSelectedColumnsButton);

        topPanel.add(filterPanel);
        topPanel.add(exportPanel);


		// Панель данных
		JPanel dataPanel = new JPanel(new MigLayout("fill, nogrid"));

		final JButton columnsButton = new JButton();
		columnsButton.setIcon(ResourcesUtil.getIcon("wrench.png"));
		columnsButton.setToolTipText("Изменить набор отображаемых колонок");
		columnsButton.setPreferredSize(new Dimension(16,16));
		columnsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.openColumnsDialog();
				reestrTableModel.fireTableStructureChanged();
			}
		});

		//////// Основная таблица для приемов
		reestrTableModel = new ReestrTableModel();
		reestrTable = new JTable(reestrTableModel);
		//добавляем кнопку изменения столбцов
		reestrTable.getTableHeader().setLayout(new BorderLayout());
		reestrTable.getTableHeader().add(columnsButton, BorderLayout.EAST);

		reestrTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent lse) {
				if (reestrTable.getSelectedRows().length==0) {
					openReceptionAction.setEnabled(false);
				} else if (reestrTable.getSelectedRows().length==1) {
					openReceptionAction.setEnabled(true);
				} else {
					openReceptionAction.setEnabled(false);
				}
			}
		});

		/// добавление реакции на двойной клик - открытие приема на просмотр
		reestrTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent ev) {
				if (ev.getClickCount() == 2) {
					int selectedReceptionIndex = reestrTable.getSelectedRow();
					controller.openUnitDialog(selectedReceptionIndex);
					reestrTableModel.fireTableDataChanged();
				}
			}
		});



		JScrollPane reestrTableScrollPane = new JScrollPane(reestrTable);

		///////////// всплывающее меню для таблицы
		JPopupMenu reestrPopupMenu = new JPopupMenu();


		openReceptionAction = new OpenUnitAction();
		openReceptionAction.setEnabled(false);
		JMenuItem openReceptionMenuItem = new JMenuItem(openReceptionAction);



		reestrPopupMenu.add(openReceptionMenuItem);

		reestrTable.setComponentPopupMenu(reestrPopupMenu);

		dataPanel.add(reestrTableScrollPane, "push, grow");

		// Панель кнопок
		JPanel buttonPanel = new JPanel();

		JButton okButton = new JButton("Ok");
		this.getRootPane().setDefaultButton(okButton);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				setVisible(false);
			}
		});


		buttonPanel.add(okButton);


		mainPanel.add(topPanel, BorderLayout.NORTH);
		mainPanel.add(dataPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		this.setContentPane(mainPanel);
		this.pack();
		
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

	class FilterListModel extends AbstractListModel {

		@Override
		public int getSize() {
			return controller.getFilters().size();
		}

		@Override
		public String getElementAt(int index) {
			return controller.getFilters().get(index).toString();
		}

		public void filterAdded(int index) {
			fireIntervalAdded(this,index,index);
		}

		public void filterUpdated(int index) {
			fireContentsChanged(this,index,index);
		}

		public void filterRemoved(int index) {
			fireIntervalRemoved(this,index,index);
		}

		public void filtersCleared(int count) {
			fireIntervalRemoved(this,0,count-1);
		}

	}

	public FilterListModel getFilterListModel() {
		return filterListModel;
	}

	// устанавливает ширину полей таблички
	private void setReestrTableColumnsWidth() {
		List<ReestrColumn> reestrColumns = ReestrColumns.getInstance().getActiveColumns();
		reestrTable.getColumnModel().getColumn(0).setPreferredWidth(20);
		for (int i = 1; i < reestrColumns.size(); i++) {
			reestrTable.getColumnModel().getColumn(i).setPreferredWidth(reestrColumns.get(i).getWidth());
		}
	}

	public void tableDataChanged() {
		reestrTableModel.fireTableDataChanged();
	}

	public void tableStructureChanged() {
		reestrTableModel.fireTableStructureChanged();
	}


	class ReestrTableModel extends AbstractTableModel {

		private List<Unit> units;

		ReestrTableModel() {
			units = new ArrayList<Unit>();
		}

		void setUnits(List<Unit> units) {
			this.units = units;
			fireTableDataChanged();
		}

		public int getRowCount() {
			return units.size();
		}

		public int getColumnCount() {
			return ReestrColumns.getInstance().getActiveColumns().size() + 1;
		}

		public String getColumnName(int column) {
			List<ReestrColumn> reestrColumns = ReestrColumns.getInstance().getActiveColumns();			

			String name = "Не опр";

			if (column == 0) {
				name = "№";
			} else {
				name = reestrColumns.get(column - 1).getName();
			}

		return name;
		}

		public Object getValueAt(int row, int column) {
			Unit unit = units.get(row);
			
			List<ReestrColumn> reestrColumns = ReestrColumns.getInstance().getActiveColumns();			


			Object value = null;

			if (column == 0) {
				value = row + 1;
			} else {
				value = reestrColumns.get(column - 1).getValue(unit);
			}

			return value;
		}

		public String getToolTipText(MouseEvent e) {
			String tip = null;
			java.awt.Point p = e.getPoint();
			int rowIndex = reestrTable.rowAtPoint(p);
			int colIndex = reestrTable.columnAtPoint(p);
			int realColumnIndex = reestrTable.convertColumnIndexToModel(colIndex);
			tip = getValueAt(rowIndex, realColumnIndex).toString();

			return tip;
		}


	}

	class OpenUnitAction extends AbstractAction {
		OpenUnitAction() {
			super("Редактировать");
		}

		public void actionPerformed(ActionEvent ev) {
			int selectedUnitIndex = reestrTable.getSelectedRow();
			controller.openUnitDialog(selectedUnitIndex);
		}
	}


}	