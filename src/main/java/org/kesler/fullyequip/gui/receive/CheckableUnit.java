package org.kesler.fullyequip.gui.receive;

import org.kesler.fullyequip.logic.Unit;


public class CheckableUnit {

    private Unit unit;
    private Boolean checked;
    private Long quantity;

    public CheckableUnit(Unit unit, Boolean checked) {
        this(unit,checked,1L);
    }

    public CheckableUnit(Unit unit, Boolean checked, Long quantity) {
        this.unit = unit;
        this.checked = checked;
        this.quantity = quantity;
    }

    public Unit getUnit() {return unit;}

    public Boolean getChecked() {return checked;}
    public void setChecked(Boolean checked) {this.checked = checked;}

    public Long getQuantity() { return quantity; }
    public void setQuantity(Long quantity) { this.quantity = quantity; }
}
