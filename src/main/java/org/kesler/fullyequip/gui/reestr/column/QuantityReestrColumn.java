package org.kesler.fullyequip.gui.reestr.column;


import org.kesler.fullyequip.logic.Unit;

public class QuantityReestrColumn extends ReestrColumn {

    public QuantityReestrColumn() {
        name = "Количество";
        alias = "quantity";
        width = 30;
    }

    public String getValue(Unit unit) {

        Long quantity = unit.getQuantity();

        return quantity==null?"Не опр":quantity.toString();

    }
}
