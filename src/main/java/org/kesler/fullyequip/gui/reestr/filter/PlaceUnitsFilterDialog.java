package org.kesler.fullyequip.gui.reestr.filter;

import java.util.List;
import java.util.ArrayList;
import javax.swing.*;

import java.awt.GridLayout;

import com.alee.extended.list.WebCheckBoxList;
import net.miginfocom.swing.MigLayout;

import org.kesler.fullyequip.logic.Place;
import org.kesler.fullyequip.logic.model.PlacesModel;
import org.kesler.fullyequip.logic.unitsfilter.PlaceUnitsFilter;

public class PlaceUnitsFilterDialog extends UnitsFilterDialog {

	private List<Place> filterPlaces;

	private List<Place> allPlaces;
    private WebCheckBoxList placesCheckBoxList;
	private List<JCheckBox> checkBoxes;


    public PlaceUnitsFilterDialog(JFrame frame) {
		super(frame, "Фильтр по размещению");
	}

	public PlaceUnitsFilterDialog(JFrame frame, PlaceUnitsFilter filter) {
		super(frame, "Фильтр по размещению", filter);
	}

	@Override
	protected void createUnitsFilter() {
		filterPlaces = new ArrayList<Place>();
		unitsFilter = new PlaceUnitsFilter(filterPlaces);
	}

	@Override
	protected JPanel createDataPanel() {
		JPanel dataPanel = new JPanel(new MigLayout("fill"));

		JPanel statusesPanel = new JPanel(new GridLayout(0,1));
		checkBoxes = new ArrayList<JCheckBox>();

		allPlaces = PlacesModel.getInstance().getAllItems();

		for (Place place: allPlaces) {
			JCheckBox checkBox = new JCheckBox(place.getName());
			checkBoxes.add(checkBox);
			statusesPanel.add(checkBox);
		}

		JScrollPane statusesPanelScrollPane = new JScrollPane(statusesPanel);



        dataPanel.add(statusesPanelScrollPane, "span, grow");

        return dataPanel;
	}

	@Override 
	protected void loadGUIDataFromUnitsFilter() {
		PlaceUnitsFilter placeUnitsFilter = (PlaceUnitsFilter) unitsFilter;
		this.filterPlaces = placeUnitsFilter.getPlaces();

        for (int i = 0; i < allPlaces.size(); i++) {
			Place place = allPlaces.get(i);
			JCheckBox checkBox = checkBoxes.get(i);
			if(filterPlaces.contains(place)) checkBox.setSelected(true);
			else checkBox.setSelected(false);
		}

    }


	@Override
	protected boolean readUnitsFilterFromGUIData() {

		filterPlaces.clear();

		for (int i = 0; i < checkBoxes.size(); i++) {
			JCheckBox checkBox = checkBoxes.get(i);
			Place place = allPlaces.get(i);
			if (checkBox.isSelected()) {
				filterPlaces.add(place);
			}
		}

		if (filterPlaces.size() == 0) {
			JOptionPane.showMessageDialog(this, "Не выбрано ни одного статуса", "Ошибка", JOptionPane.ERROR_MESSAGE);
			return false;
		}


        return true;

	}

}