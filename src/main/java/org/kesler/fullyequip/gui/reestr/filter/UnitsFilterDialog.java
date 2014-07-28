package org.kesler.fullyequip.gui.reestr.filter;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


import org.kesler.fullyequip.gui.dialog.AbstractDialog;
import org.kesler.fullyequip.logic.unitsfilter.UnitsFilter;


public abstract class UnitsFilterDialog extends AbstractDialog {

	protected UnitsFilter unitsFilter = null;

	public UnitsFilterDialog(JFrame parentFrame, String name) {
		super(parentFrame, name, true);

		createUnitsFilter();

		createGUI();
		this.setLocationRelativeTo(parentFrame);

		loadGUIDataFromUnitsFilter();
		result = NONE;
	}

	public UnitsFilterDialog(JFrame parentFrame, String name, UnitsFilter filter) {
		super(parentFrame, name, true);
		this.unitsFilter = filter;

		createGUI();
		this.setLocationRelativeTo(parentFrame);

		loadGUIDataFromUnitsFilter();
		result = NONE;
	}

	public UnitsFilter getUnitsFilter() {
		return unitsFilter;
	}
    public void setUnitsFilter(UnitsFilter unitsFilter) {
        this.unitsFilter = unitsFilter;
        loadGUIDataFromUnitsFilter();
    }

	private void createGUI() {

		JPanel mainPanel = new JPanel(new BorderLayout());

		JPanel dataPanel = createDataPanel();


		JPanel buttonPanel = new JPanel();

		JButton okButton = new JButton("Ok");
		this.getRootPane().setDefaultButton(okButton);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				
				if (readUnitsFilterFromGUIData()) {
					result = OK;
					setVisible(false);
				}
				
			}
		});

		JButton cancelButton = new JButton("Отмена");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				result = CANCEL;
				setVisible(false);
			}
		});

		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);


		//собираем основную панель
		mainPanel.add(dataPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		this.setContentPane(mainPanel);
		this.pack();
        this.setSize(this.getWidth()+20,this.getHeight()+20);
		
	}

	// это надо будет реализовать в специальных диалогах

	abstract protected void createUnitsFilter();

	abstract protected JPanel createDataPanel(); 

	abstract protected void loadGUIDataFromUnitsFilter();

	abstract protected boolean readUnitsFilterFromGUIData();


}