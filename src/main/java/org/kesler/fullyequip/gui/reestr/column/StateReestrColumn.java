package org.kesler.fullyequip.gui.reestr.column;



import org.kesler.fullyequip.logic.Unit;
import org.kesler.fullyequip.logic.UnitState;

public class StateReestrColumn extends ReestrColumn {

    public StateReestrColumn() {
        name = "Состояние";
        alias = "state";
        width = 70;
    }

    public String getValue(Unit unit) {

        UnitState state = unit.getState();

        return  state==null?"Не опр.":state.toString();

    }

}
