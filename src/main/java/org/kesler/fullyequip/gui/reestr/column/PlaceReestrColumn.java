package org.kesler.fullyequip.gui.reestr.column;

import org.kesler.fullyequip.logic.Unit;

public class PlaceReestrColumn extends ReestrColumn {

	public PlaceReestrColumn() {
		name = "Размещение";
		alias = "place";
		width = 100;
	}

	public String getValue(Unit unit) {
		
		String placeName = unit.getPlaceName();

		if (placeName == null) placeName = "Не опр.";

		return placeName;

	}
}