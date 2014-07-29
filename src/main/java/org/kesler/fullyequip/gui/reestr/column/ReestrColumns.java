package org.kesler.fullyequip.gui.reestr.column;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;


public class ReestrColumns {

	private List<ReestrColumn> allColumns;

	private List<ReestrColumn> activeColumns;
	private List<ReestrColumn> inactiveColumns;

	private static ReestrColumns instance = null;

	public static synchronized ReestrColumns getInstance() {
		if (instance == null) {
			instance = new ReestrColumns();
		}

		return instance;
	}

	private ReestrColumns() {
		allColumns = new ArrayList<ReestrColumn>();
        ReestrColumn nameReestrColumn = new NameReestrColumn();
        ReestrColumn typeReestrColumn = new TypeReestrColumn();
        ReestrColumn invNumReestrColumn = new InvNumReestrColumn();
        ReestrColumn placeReestrColumn = new PlaceReestrColumn();
        ReestrColumn quantityReestrColumn = new QuantityReestrColumn();
        ReestrColumn priceReestrColumn = new PriceReestrColumn();
        ReestrColumn auctionTypeReestrColumn = new AuctionTypeReestrColumn();


        allColumns.add(nameReestrColumn);
        allColumns.add(typeReestrColumn);
        allColumns.add(invNumReestrColumn);
		allColumns.add(placeReestrColumn);
        allColumns.add(quantityReestrColumn);
        allColumns.add(priceReestrColumn);


		allColumns = Collections.unmodifiableList(allColumns); /// делаем полный список колонок неизменным во избежание

		// добавляем в активные поля 
		activeColumns = new ArrayList<ReestrColumn>();

        activeColumns.add(nameReestrColumn);
        activeColumns.add(typeReestrColumn);
        activeColumns.add(invNumReestrColumn);
		activeColumns.add(placeReestrColumn);
        activeColumns.add(quantityReestrColumn);


		// список неактивных полей
		inactiveColumns = new ArrayList<ReestrColumn>();
        inactiveColumns.add(priceReestrColumn);
        inactiveColumns.add(auctionTypeReestrColumn);
	}

	public List<ReestrColumn> getAllColumns() {
		return allColumns;
	}

	public List<ReestrColumn> getActiveColumns() {
		return activeColumns;
	}

	public List<ReestrColumn> getInactiveColumns() {
		return inactiveColumns;
	}


	public boolean activateColumn(ReestrColumn column) {
		if (inactiveColumns.contains(column)) {
			inactiveColumns.remove(column);
			activeColumns.add(column);
			return true;
		} else {
			return false;
		}
	}

	public boolean deactivateColumn(ReestrColumn column) {
		if (activeColumns.contains(column)) {
			activeColumns.remove(column);
			inactiveColumns.add(column);
			return true;
		} else {
			return false;
		}
	}

}