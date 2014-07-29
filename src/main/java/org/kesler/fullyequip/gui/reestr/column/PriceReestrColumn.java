package org.kesler.fullyequip.gui.reestr.column;

import org.kesler.fullyequip.logic.Unit;

public class PriceReestrColumn extends ReestrColumn {

    public PriceReestrColumn() {
        name = "Цена";
        alias = "price";
        width = 30;
    }

    public String getValue(Unit unit) {

        Double price = unit.getPrice();

        return  price == null?"Не опр.":price.toString();

    }
}
