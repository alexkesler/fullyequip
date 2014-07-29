package org.kesler.fullyequip.gui.reestr.column;

import org.kesler.fullyequip.logic.Unit;


public class NameReestrColumn extends ReestrColumn {

    public NameReestrColumn() {
        name = "Наименование";
        alias = "name";
        width = 100;
    }

    public String getValue(Unit unit) {

        String unitName = unit.getName();
        if (unitName == null) unitName = "Не опр.";
        return unitName;

    }
}
