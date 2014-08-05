package org.kesler.fullyequip.gui.moving;

import org.kesler.fullyequip.logic.Unit;


public class MovingUnit {
    private Unit unit;
    private Long quantity;

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getAllQuantity() {
        return unit==null?null:unit.getQuantity();
    }
}
