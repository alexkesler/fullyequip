package org.kesler.fullyequip.logic.unitsfilter;

import org.kesler.fullyequip.logic.Place;
import org.kesler.fullyequip.logic.Unit;

import java.util.List;


public class PlaceUnitsFilter implements UnitsFilter {

    private UnitsFiltersEnum filtersEnum = UnitsFiltersEnum.PLACE;

	private List<Place> filterPlaces;


	public PlaceUnitsFilter(List<Place> filterPlaces) {
		this.filterPlaces = filterPlaces;
	}

	public List<Place> getPlaces() {
		return filterPlaces;
	}

    @Override
    public UnitsFiltersEnum getFiltersEnum() {
        return filtersEnum;
    }

	@Override
	public boolean checkUnit(Unit unit) {
		if (unit == null) {
			throw new IllegalArgumentException();
		}

		boolean fit = false;
		
		for (Place place: filterPlaces) {
			if (place.equals(unit.getPlace())) {
				fit = true;
			}
		}

		return fit;
	}

	@Override
	public String toString() {
		String filterString  = "По размещению: (";
		for (Place place: filterPlaces) {
			filterString += place.getName() + ";";
		}
		filterString += ")";

		return filterString;
	}

}