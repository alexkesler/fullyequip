package org.kesler.fullyequip.logic.model;

import org.apache.log4j.Logger;
import org.kesler.fullyequip.logic.Unit;
import org.kesler.fullyequip.logic.unitsfilter.UnitsFiltersModel;

import java.util.ArrayList;
import java.util.List;


public class UnitsModel extends DefaultModel<Unit> {
    private static UnitsModel instance = new UnitsModel();
    private UnitsFiltersModel unitsFiltersModel;


    List<Unit> filteredUnits;

    public static synchronized UnitsModel getInstance() {return instance;}

    private UnitsModel() {
        super(Unit.class);
        log = Logger.getLogger(getClass().getSimpleName());
        unitsFiltersModel = new UnitsFiltersModel();
        filteredUnits = new ArrayList<Unit>();
    }

    public UnitsFiltersModel getFiltersModel() {
        return unitsFiltersModel;
    }

    public List<Unit> getFilteredUnits() {return filteredUnits;}


    public void applyFilters() {
        log.info("Filtering units");
        notifyListeners(ModelState.FILTERING);
        filteredUnits = new ArrayList<Unit>();
        for (Unit unit: super.getAllItems()) {
            if (unitsFiltersModel.checkUnit(unit)) filteredUnits.add(unit);
        }
        notifyListeners(ModelState.FILTERED);
    }

    public void applyFiltersInSeparateThread() {
        Thread filterThread = new Thread(new Runnable() {
            public void run() {
                applyFilters();
            }
        });
        filterThread.start();
    }

    public void readReceptionsAndApplyFiltersInSeparateThread() {
        Thread readerFiltererThread = new Thread(new Runnable() {
            public void run() {
                readItemsFromDB();
                applyFilters();
            }
        });
        readerFiltererThread.start();
    }


}
