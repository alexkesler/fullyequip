package org.kesler.fullyequip.logic.unitsfilter;

import org.apache.log4j.Logger;
import org.kesler.fullyequip.logic.Unit;


import java.util.*;


public class UnitsFiltersModel {
    protected final Logger log;

    private List<UnitsFilter> filters;

    public UnitsFiltersModel() {
        log = Logger.getLogger(this.getClass().getSimpleName());
        filters = new ArrayList<UnitsFilter>();
    }

    public List<UnitsFilter> getFilters() {
        return Collections.unmodifiableList(filters);
    }


    public int addFilter(UnitsFilter filter) {
        log.info("Adding filter: " + filter);
        filters.add(filter);
        return filters.size()-1;
    }

    public void removeFilter(int index) {
        log.info("Remove filter:" + filters.get(index));
        filters.remove(index);
    }

    public void resetFilters() {
        log.info("Reset filters");
        filters = new ArrayList<UnitsFilter>();
    }

    public boolean checkUnit(Unit unit) {
        boolean fit = true;
        for (UnitsFilter unitsFilter: filters) {
            if (!unitsFilter.checkUnit(unit)) fit = false;
        }
        return fit;
    }

}