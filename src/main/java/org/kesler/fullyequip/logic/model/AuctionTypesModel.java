package org.kesler.fullyequip.logic.model;

import org.kesler.fullyequip.logic.AuctionType;


public class AuctionTypesModel extends DefaultModel<AuctionType> {

    private static final AuctionTypesModel instance = new AuctionTypesModel();

    AuctionTypesModel() {
        super(AuctionType.class);
    }

    public static synchronized AuctionTypesModel getInstance() {return instance;}
}
