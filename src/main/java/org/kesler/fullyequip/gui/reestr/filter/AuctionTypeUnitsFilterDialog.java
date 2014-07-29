package org.kesler.fullyequip.gui.reestr.filter;


import com.alee.extended.list.CheckBoxListModel;
import com.alee.extended.list.WebCheckBoxList;
import net.miginfocom.swing.MigLayout;
import org.kesler.fullyequip.logic.AuctionType;
import org.kesler.fullyequip.logic.model.AuctionTypesModel;
import org.kesler.fullyequip.logic.model.DefaultModel;
import org.kesler.fullyequip.logic.unitsfilter.AuctionTypeUnitsFilter;

import javax.swing.*;
import java.util.HashSet;
import java.util.Set;

public class AuctionTypeUnitsFilterDialog extends UnitsFilterDialog{
    private Set<AuctionType> filterAuctionTypes;
    private Set<AuctionType> allAuctionTypes;
    private AuctionTypesCheckBoxListModel auctionTypesCheckBoxListModel;


    public AuctionTypeUnitsFilterDialog(JFrame frame) {
        super(frame, "Фильтр по типу аукциона");
    }

    public AuctionTypeUnitsFilterDialog(JFrame frame, AuctionTypeUnitsFilter filter) {
        super(frame, "Фильтр по типу аукциона", filter);
    }

    @Override
    protected void createUnitsFilter() {
        filterAuctionTypes = new HashSet<AuctionType>();
        unitsFilter = new AuctionTypeUnitsFilter(filterAuctionTypes);
    }

    @Override
    protected JPanel createDataPanel() {
        JPanel dataPanel = new JPanel(new MigLayout("fill"));

        auctionTypesCheckBoxListModel = new AuctionTypesCheckBoxListModel();
        WebCheckBoxList auctionTypesCheckBoxList = new WebCheckBoxList(auctionTypesCheckBoxListModel);

        JScrollPane auctionTypesCheckBoxListScrollPane = new JScrollPane(auctionTypesCheckBoxList);


        dataPanel.add(auctionTypesCheckBoxListScrollPane, "span, grow");

        return dataPanel;
    }

    @Override
    protected void loadGUIDataFromUnitsFilter() {
        AuctionTypeUnitsFilter auctionTypeUnitsFilter = (AuctionTypeUnitsFilter) unitsFilter;
        this.filterAuctionTypes = auctionTypeUnitsFilter.getAuctionTypes();
        AuctionTypesModel.getInstance().readItemsFromDB();
        this.allAuctionTypes = new HashSet<AuctionType>(AuctionTypesModel.getInstance().getAllItems());

        auctionTypesCheckBoxListModel.updatePlaces();

    }


    @Override
    protected boolean readUnitsFilterFromGUIData() {

        filterAuctionTypes.clear();
        filterAuctionTypes.addAll(auctionTypesCheckBoxListModel.getCheckedPlaces());

        if (filterAuctionTypes.size() == 0) {
            JOptionPane.showMessageDialog(this, "Не выбрано ни одного размещения", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return false;
        }


        return true;

    }


    class AuctionTypesCheckBoxListModel extends CheckBoxListModel {

        void updatePlaces() {
            removeAllElements();
            for (AuctionType auctionType : allAuctionTypes) {
                addCheckBoxElement(auctionType, filterAuctionTypes.contains(auctionType));
            }
        }

        Set<AuctionType> getCheckedPlaces() {
            Set<AuctionType> checkedAuctionTypes = new HashSet<AuctionType>();
            for (int i=0; i<size();i++) {
                if (isCheckBoxSelected(i)) checkedAuctionTypes.add((AuctionType)(get(i).getUserObject()));
            }
            return checkedAuctionTypes;
        }

    }

}
