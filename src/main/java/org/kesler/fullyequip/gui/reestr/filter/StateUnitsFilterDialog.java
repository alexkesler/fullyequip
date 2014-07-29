package org.kesler.fullyequip.gui.reestr.filter;


import com.alee.extended.list.CheckBoxListModel;
import com.alee.extended.list.WebCheckBoxList;
import net.miginfocom.swing.MigLayout;
import org.kesler.fullyequip.logic.UnitState;
import org.kesler.fullyequip.logic.model.PlacesModel;
import org.kesler.fullyequip.logic.model.UnitStatesModel;
import org.kesler.fullyequip.logic.unitsfilter.StateUnitsFilter;

import javax.swing.*;
import java.util.HashSet;
import java.util.Set;

public class StateUnitsFilterDialog extends UnitsFilterDialog{
    private Set<UnitState> filterStates;

    private Set<UnitState> allStates;
    private StatesCheckBoxListModel statesCheckBoxListModel;


    public StateUnitsFilterDialog(JFrame frame) {
        super(frame, "Фильтр по состоянию");
    }

    public StateUnitsFilterDialog(JFrame frame, StateUnitsFilter filter) {
        super(frame, "Фильтр по состоянию", filter);
    }

    @Override
    protected void createUnitsFilter() {
        filterStates = new HashSet<UnitState>();
        unitsFilter = new StateUnitsFilter(filterStates);
    }

    @Override
    protected JPanel createDataPanel() {
        JPanel dataPanel = new JPanel(new MigLayout("fill"));

        statesCheckBoxListModel = new StatesCheckBoxListModel();
        WebCheckBoxList statesCheckBoxList = new WebCheckBoxList(statesCheckBoxListModel);

        JScrollPane statesCheckBoxListScrollPane = new JScrollPane(statesCheckBoxList);


        dataPanel.add(statesCheckBoxListScrollPane, "span, grow");

        return dataPanel;
    }

    @Override
    protected void loadGUIDataFromUnitsFilter() {
        StateUnitsFilter placeUnitsFilter = (StateUnitsFilter) unitsFilter;
        this.filterStates = placeUnitsFilter.getStates();
        PlacesModel.getInstance().readItemsFromDB();
        this.allStates = new HashSet<UnitState>(UnitStatesModel.getInstance().getAllItems());

        statesCheckBoxListModel.updatePlaces();

    }


    @Override
    protected boolean readUnitsFilterFromGUIData() {

        filterStates.clear();
        filterStates.addAll(statesCheckBoxListModel.getCheckedStates());

        if (filterStates.size() == 0) {
            JOptionPane.showMessageDialog(this, "Не выбрано ни одного размещения", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return false;
        }


        return true;

    }


    class StatesCheckBoxListModel extends CheckBoxListModel {

        void updatePlaces() {
            removeAllElements();
            for (UnitState state : allStates) {
                addCheckBoxElement(state, filterStates.contains(state));
            }
        }

        Set<UnitState> getCheckedStates() {
            Set<UnitState> checkedStates = new HashSet<UnitState>();
            for (int i=0; i<size();i++) {
                if (isCheckBoxSelected(i)) checkedStates.add((UnitState)(get(i).getUserObject()));
            }
            return checkedStates;
        }

    }

}
