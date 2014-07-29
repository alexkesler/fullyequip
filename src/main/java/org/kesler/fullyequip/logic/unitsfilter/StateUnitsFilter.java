package org.kesler.fullyequip.logic.unitsfilter;

import org.kesler.fullyequip.logic.Unit;
import org.kesler.fullyequip.logic.UnitState;

import java.util.Set;


public class StateUnitsFilter implements UnitsFilter {
    private UnitsFiltersEnum filtersEnum = UnitsFiltersEnum.STATE;

    private Set<UnitState> filterStates;


    public StateUnitsFilter(Set<UnitState> filterStates) {
        this.filterStates = filterStates;
    }

    public Set<UnitState> getStates() {
        return filterStates;
    }

    @Override
    public UnitsFiltersEnum getFiltersEnum() {
        return filtersEnum;
    }

    @Override
    public boolean checkUnit(Unit unit) {
        if (unit == null) {
            throw new IllegalArgumentException();
        }

        boolean fit = false;

        for (UnitState unitState: filterStates) {
            if (unitState.equals(unit.getState())) {
                fit = true;
            }
        }

        return fit;
    }

    @Override
    public String toString() {
        String filterString  = "По состоянию: (";
        for (UnitState unitState: filterStates) {
            filterString += unitState.getName() + ";";
        }
        filterString += ")";

        return filterString;
    }

}
