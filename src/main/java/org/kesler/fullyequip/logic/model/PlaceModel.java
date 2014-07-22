package org.kesler.fullyequip.logic.model;

import org.apache.log4j.Logger;
import org.kesler.fullyequip.logic.Place;

import java.util.List;

/**
 * Модель для Размещения, добавляет формирование начального места
 */
public class PlaceModel extends DefaultModel<Place> {
    private static PlaceModel instance = new PlaceModel();
    private Place initialPlace;

    private PlaceModel() {
        super(Place.class);
        log = Logger.getLogger("PlaceModel");
    }

    public static synchronized PlaceModel getInstance() {return instance;}

    public Place getInitialPlace() {

        if (initialPlace == null) {
            readItemsFromDB();
            List<Place> places = getAllItems();
            for(Place place: places) {
                if(place.isInitial()) initialPlace = place;
            }
        }

        if(initialPlace == null) {
            initialPlace = createInitialPlace();
            addItem(initialPlace);
        }

        return initialPlace;

    }

    private Place createInitialPlace() {
        Place initialPlace = new Place();
        initialPlace.setName("Поставщик");
        initialPlace.setStorage(true);
        initialPlace.setInitial(true);

        return initialPlace;
    }
}
