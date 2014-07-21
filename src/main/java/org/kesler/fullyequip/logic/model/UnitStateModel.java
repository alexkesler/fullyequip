package org.kesler.fullyequip.logic.model;

import org.apache.log4j.Logger;
import org.kesler.fullyequip.logic.UnitState;

import java.util.List;

/**
 * Created by alex on 12.04.14.
 */
public class UnitStateModel extends DefaultModel<UnitState> {

    public UnitStateModel() {
        super(UnitState.class);
        log = Logger.getLogger("UnitStateModel");
    }

    public UnitState getInitialState() {
        UnitState initialState = null;

        readItemsFromDB();
        List<UnitState> states = getAllItems();
        for(UnitState state: states) {
            if(state.isInitial()) initialState = state;
        }

        if(initialState == null) {
            initialState = createInitialState();
            addItem(initialState);
        }

        return initialState;
    }

    private UnitState createInitialState() {
        log.info("Creating new initial UnitState");
        UnitState initialState = new UnitState();

        initialState.setName("У поставщика");
        initialState.setInitial(true);


        return initialState;
    }

}
