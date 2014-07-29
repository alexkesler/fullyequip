package org.kesler.fullyequip.gui.reestr.column;

import org.kesler.fullyequip.logic.Unit;

public class TypeReestrColumn extends ReestrColumn {

    public TypeReestrColumn() {
        name = "Тип";
        alias = "type";
        width = 70;
    }

    public String getValue(Unit unit) {

        String typeName = unit.getTypeName();
        if (typeName == null) typeName = "Не опр.";
        return typeName;

    }
}
