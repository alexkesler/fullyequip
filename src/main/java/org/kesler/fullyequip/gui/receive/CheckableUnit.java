package org.kesler.fullyequip.gui.receive;

import org.kesler.fullyequip.logic.Unit;

/**
 * Created by alex on 07.03.14.
 */
public class CheckableUnit {

    private Unit unit;
    private Boolean checked;

    public CheckableUnit(Unit unit, Boolean checked) {
        this.unit = unit;
        this.checked = checked;
    }

    public Unit getUnit() {return unit;}

    public Boolean getChecked() {return checked;}
    public void setChecked(Boolean checked) {this.checked = checked;}

}
