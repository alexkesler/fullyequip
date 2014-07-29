package org.kesler.fullyequip.gui.reestr.column;

import org.kesler.fullyequip.logic.Auction;
import org.kesler.fullyequip.logic.Unit;

public class AuctionTypeReestrColumn extends ReestrColumn{

    public AuctionTypeReestrColumn() {
        name = "Тип аукцона";
        alias = "auctionType";
        width = 70;
    }

    public String getValue(Unit unit) {

        Auction auction = unit.getInvoicePosition().getInvoice().getContract().getAuction();

        return  auction==null?"Не опр.":auction.getType().toString();

    }

}
