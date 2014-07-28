package org.kesler.fullyequip.logic.unitsfilter;

import org.kesler.fullyequip.logic.Unit;

public interface UnitsFilter {
    public UnitsFiltersEnum getFiltersEnum();
	public boolean checkUnit(Unit unit);
	@Override
	public String toString();
}