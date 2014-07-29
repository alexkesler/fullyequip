package org.kesler.fullyequip.gui.reestr.column;

import org.kesler.fullyequip.logic.Unit;



public class InvNumReestrColumn extends ReestrColumn {

    public InvNumReestrColumn() {
        name = "Инвентарный";
        alias = "invNum";
        width = 70;
    }

    public String getValue(Unit unit) {

        String invNumber = unit.getInvNumber();
        if (invNumber == null) invNumber = "Не опр.";
        return invNumber;

    }
}
