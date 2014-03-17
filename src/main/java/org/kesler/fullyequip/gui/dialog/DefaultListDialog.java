package org.kesler.fullyequip.gui.dialog;

import java.util.List;
import java.util.ArrayList;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JScrollPane;
import javax.swing.AbstractListModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;

import net.miginfocom.swing.MigLayout;
import org.kesler.fullyequip.gui.dict.DictEntity;
import org.kesler.fullyequip.gui.util.InfoDialog;
import org.kesler.fullyequip.util.ResourcesUtil;

/**
 * Диалог по умолчанию для справочников сущностей
 * @param <T> тип сущности
 */
public class DefaultListDialog<T extends DictEntity> extends AbstractDialog {

	public static final int VIEW_MODE=0;
	public static final int VIEW_FILTER_MODE = 1;
	public static final int SELECT_MODE = 2;
	public static final int SELECT_FILTER_MODE = 3;

	private boolean isSelect = false;
	private boolean isFilter = false;

	private ListDialogController controller;

	private JTextField filterTextField;
	private JList itemsList;
	private ItemsListModel itemsListModel;
	private int selectedIndex;

	public DefaultListDialog(JFrame parentFrame, String name, ListDialogController controller, Dimension size, int mode) {
		super(parentFrame, name, true);
		this.controller = controller;

		setMode(mode);

		selectedIndex = -1;

		createGUI();
		setSize(size);
		setLocationRelativeTo(parentFrame);
	}


	public DefaultListDialog(JDialog parentDialog, String name, ListDialogController controller, Dimension size, int mode) {
		super(parentDialog, name, true);
		this.controller = controller;

		setMode(mode);

		selectedIndex = -1;

		createGUI();
		setSize(size);
		setLocationRelativeTo(parentDialog);
	}

	public DefaultListDialog(JFrame parentFrame, String name, ListDialogController controller, int mode) {
		this(parentFrame, name, controller, new Dimension(400,500), mode);
	}

	public DefaultListDialog(JDialog parentDialog, String name, ListDialogController controller, int mode) {
		this(parentDialog, name, controller, new Dimension(400,500), mode);
	}


	public DefaultListDialog(JFrame parentFrame, String name, ListDialogController controller) {
		this(parentFrame, name, controller, VIEW_MODE);
	}

	public DefaultListDialog(JDialog parentDialog, String name, ListDialogController controller) {
		this(parentDialog, name, controller, VIEW_MODE);		
	}

	public DefaultListDialog(JFrame parentFrame, ListDialogController controller) {
		this(parentFrame, "", controller);
	}

	public DefaultListDialog(JDialog parentDialog, ListDialogController controller) {
		this(parentDialog, "", controller);		
	}

	private void setMode(int mode) {
		switch (mode) {
			case VIEW_MODE:
				isSelect = false;
				isFilter = false;
				break;
			case VIEW_FILTER_MODE:
				isSelect = false;
				isFilter = true;
				break;
			case SELECT_MODE:
				isSelect = true;
				isFilter = false;
				break;
			case SELECT_FILTER_MODE:
				isSelect = true;
				isFilter = true;
				break;		
		}		
	}


	public int getSelectedIndex() {
		return selectedIndex;
	}


	public void setItems(List<T> items) {
		itemsListModel.setItems(items);
		if (items.size() > 0) {
			selectedIndex = 0;
			itemsList.setSelectedIndex(selectedIndex);			
		}
	}

	public void cleanFilter() {
		filterTextField.setText("");
	}


    public void addedItem(int index) {
        itemsListModel.addedItem(index);
        itemsList.setSelectedIndex(index);
    }

    public void updatedItem(int index) {
        itemsListModel.updatedItem(index);
        itemsList.setSelectedIndex(index);
    }

    public void removedItem(int index) {
        itemsListModel.removedItem(index);
    }



    private void createGUI() {

		// Основная панель
		JPanel mainPanel = new JPanel(new BorderLayout());

		// Панель данных
		JPanel dataPanel = new JPanel(new MigLayout("fill,nogrid"));


		filterTextField = new JTextField(20);

		filterTextField.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent event) {
				filterChanged();
			}

			public void removeUpdate(DocumentEvent event) {
				filterChanged();
			}

			public void changedUpdate(DocumentEvent event) {}

			private void filterChanged() {
				String filterString = filterTextField.getText();
				controller.filterItems(filterString);
			}

		});

		itemsListModel = new ItemsListModel();
		itemsList = new JList(itemsListModel);
		// Можно выбрать только один элемент
		itemsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// Запоминаем выбраный элемент
		itemsList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent lse) {
				if(lse.getValueIsAdjusting() == false) {
					selectedIndex = itemsList.getSelectedIndex();
				}				
			}
		});


		/// добавление реакции на двойной клик - открытие приема на просмотр либо выбор
		itemsList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent ev) {
				if (ev.getClickCount() == 2) {
					if (isSelect) {
						result = OK;
						setVisible(false);										
					} else {
						controller.openEditItemDialog(selectedIndex);
					}
				}
			}
		});


		JScrollPane itemsListScrollPane = new JScrollPane(itemsList);



		JButton addItemButton = new JButton();
		addItemButton.setIcon(ResourcesUtil.getIcon("add.png"));
		addItemButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.openAddItemDialog();
			}
		});

		JButton editItemButton = new JButton();
		editItemButton.setIcon(ResourcesUtil.getIcon("pencil.png"));
		editItemButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if (selectedIndex != -1) {
					controller.openEditItemDialog(selectedIndex);
				} else {
					new InfoDialog(currentDialog, "Ничего не выбрано", 1000, InfoDialog.RED).showInfo();
				}
				
			}
		});

		JButton removeItemButton = new JButton();
		removeItemButton.setIcon(ResourcesUtil.getIcon("delete.png"));
		removeItemButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if (selectedIndex != -1) {
					controller.removeItem(selectedIndex);
				} else {
					new InfoDialog(currentDialog, "Ничего не выбрано", 1000, InfoDialog.RED).showInfo();
				}

			}
		});

		JButton updateButton = new JButton();
		updateButton.setIcon(ResourcesUtil.getIcon("arrow_refresh.png"));
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.readItems();
				// itemsListModel.updateItems();
				// new InfoDialog(currentDialog, "Обновлено", 500, InfoDialog.GREEN).showInfo();
			}
		});


		// собираем панель данных
		if (isFilter) dataPanel.add(filterTextField, "wrap");
		dataPanel.add(itemsListScrollPane, "push, grow, wrap, w 200");
		dataPanel.add(addItemButton);
		dataPanel.add(editItemButton);
		dataPanel.add(removeItemButton);
		dataPanel.add(updateButton);

		// Панель кнопок
		JPanel buttonPanel = new JPanel();

		String okString = "Ok";
		if (isSelect) okString = "Выбрать";
        JButton okButton = new JButton(okString);         
		okButton.setIcon(ResourcesUtil.getIcon("accept.png"));
		this.getRootPane().setDefaultButton(okButton);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if (isSelect && selectedIndex == -1) {
					new InfoDialog(currentDialog, "Ничего не выбрано", 1000, InfoDialog.RED).showInfo();
				} else {
					result = OK;
					setVisible(false);										
				}
			}
		});


		JButton cancelButton = new JButton("Отмена");
		cancelButton.setIcon(ResourcesUtil.getIcon("cancel.png"));
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				result = CANCEL;
				setVisible(false);
			}
		});

		// Собираем панель кнопок
		buttonPanel.add(okButton);
		if (isSelect) buttonPanel.add(cancelButton);

		// Собираем основную панель
		mainPanel.add(dataPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		this.setContentPane(mainPanel);

	}


	class ItemsListModel extends AbstractListModel {

		private List<T> items;

		ItemsListModel() {
			items = new ArrayList<T>();
		}

		void setItems(List<T> items) {
			this.items = items;
			updateItems();
		}

		@Override
		public int getSize() {
			return items.size();
		}

		@Override
		public String getElementAt(int index) {
			String value = items.get(index).toString();
			return value;
		}

		void addedItem(int index) {
			fireIntervalAdded(this, index, index);
		}

		void updatedItem(int index) {
			fireContentsChanged(this, index, index);
		}

		void removedItem(int index) {
			fireIntervalRemoved(this, index, index);
		}

		void updateItems() {
			fireContentsChanged(this, 0, items.size()-1);
		}
	}


}
