package org.kesler.fullyequip.logic.model;

import org.apache.log4j.Logger;
import org.kesler.fullyequip.logic.UnitState;

import java.util.ArrayList;
import java.util.List;

/**
 * Модель управления состояниями оборудования
 */
public class UnitStateModel extends DefaultModel<UnitState> {
    private final static UnitStateModel instance = new UnitStateModel();
    private UnitState initialState;


    private UnitStateModel() {
        super(UnitState.class);
        log = Logger.getLogger("UnitStateModel");
     }

    public static synchronized UnitStateModel getInstance() {return instance;}

    public UnitState getInitialState() {

        if(initialState == null) {
            readItemsFromDB();
            List<UnitState> states = getAllItems();
            for(UnitState state: states) {
                if(state.isInitial()!=null && state.isInitial()) initialState = state;
            }

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
