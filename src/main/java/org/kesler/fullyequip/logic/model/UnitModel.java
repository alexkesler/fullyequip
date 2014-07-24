package org.kesler.fullyequip.logic.model;

import org.apache.log4j.Logger;
import org.kesler.fullyequip.logic.Unit;

/**
 * Created by alex on 24.07.14.
 */
public class UnitModel extends DefaultModel<Unit> {
    private static UnitModel instance = new UnitModel();

    public static synchronized UnitModel getInstance() {return instance;}

    private UnitModel() {
        super(Unit.class);
        log = Logger.getLogger("UnitModel");
    }
}
