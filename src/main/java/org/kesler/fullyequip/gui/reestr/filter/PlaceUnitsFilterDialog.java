package org.kesler.fullyequip.gui.reestr.filter;

import com.alee.extended.list.CheckBoxListModel;
import com.alee.extended.list.WebCheckBoxList;
import net.miginfocom.swing.MigLayout;
import org.kesler.fullyequip.logic.Place;
import org.kesler.fullyequip.logic.model.PlacesModel;
import org.kesler.fullyequip.logic.unitsfilter.PlaceUnitsFilter;

import javax.swing.*;
import java.util.HashSet;
import java.util.Set;

public class PlaceUnitsFilterDialog extends UnitsFilterDialog {

	private Set<Place> filterPlaces;

	private Set<Place> allPlaces;
    private PlacesCheckBoxListModel placesCheckBoxListModel;


    public PlaceUnitsFilterDialog(JFrame frame) {
		super(frame, "Фильтр по размещению");
	}

	public PlaceUnitsFilterDialog(JFrame frame, PlaceUnitsFilter filter) {
		super(frame, "Фильтр по размещению", filter);
	}

	@Override
	protected void createUnitsFilter() {
		filterPlaces = new HashSet<Place>();
		unitsFilter = new PlaceUnitsFilter(filterPlaces);
	}

	@Override
	protected JPanel createDataPanel() {
		JPanel dataPanel = new JPanel(new MigLayout("fill"));

	    placesCheckBoxListModel = new PlacesCheckBoxListModel();
        WebCheckBoxList placesCheckBoxList = new WebCheckBoxList(placesCheckBoxListModel);

        JScrollPane placesCheckBoxListScrollPane = new JScrollPane(placesCheckBoxList);


        dataPanel.add(placesCheckBoxListScrollPane, "span, grow");

        return dataPanel;
	}

	@Override 
	protected void loadGUIDataFromUnitsFilter() {
		PlaceUnitsFilter placeUnitsFilter = (PlaceUnitsFilter) unitsFilter;
		this.filterPlaces = placeUnitsFilter.getPlaces();
        PlacesModel.getInstance().readItemsFromDB();
        this.allPlaces = new HashSet<Place>(PlacesModel.getInstance().getAllItems());

        placesCheckBoxListModel.updatePlaces();

    }


	@Override
	protected boolean readUnitsFilterFromGUIData() {

        filterPlaces.clear();
        filterPlaces.addAll(placesCheckBoxListModel.getCheckedPlaces());

		if (filterPlaces.size() == 0) {
			JOptionPane.showMessageDialog(this, "Не выбрано ни одного размещения", "Ошибка", JOptionPane.ERROR_MESSAGE);
			return false;
		}


        return true;

	}


    class PlacesCheckBoxListModel extends CheckBoxListModel {

        void updatePlaces() {
            removeAllElements();
            for (Place place :allPlaces) {
                addCheckBoxElement(place,filterPlaces.contains(place));
            }
        }

        Set<Place> getCheckedPlaces() {
            Set<Place> checkedPlaces = new HashSet<Place>();
            for (int i=0; i<size();i++) {
                if (isCheckBoxSelected(i)) checkedPlaces.add((Place)(get(i).getUserObject()));
            }
            return checkedPlaces;
        }

    }

}