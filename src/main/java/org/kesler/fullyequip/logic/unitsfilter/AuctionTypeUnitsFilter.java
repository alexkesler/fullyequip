package org.kesler.fullyequip.logic.unitsfilter;


import org.kesler.fullyequip.logic.AuctionType;
import org.kesler.fullyequip.logic.Unit;

import java.util.Set;

public class AuctionTypeUnitsFilter implements UnitsFilter{

    private UnitsFiltersEnum filtersEnum = UnitsFiltersEnum.AUCTION_TYPE;

    private Set<AuctionType> filterAuctionTypes;


    public AuctionTypeUnitsFilter(Set<AuctionType> filterAuctionTypes) {
        this.filterAuctionTypes = filterAuctionTypes;
    }

    public Set<AuctionType> getAuctionTypes() {
        return filterAuctionTypes;
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

        for (AuctionType auctionType: filterAuctionTypes) {
            if (auctionType.equals(unit.getAuctionType())) {
                fit = true;
            }
        }

        return fit;
    }

    @Override
    public String toString() {
        String filterString  = "По типу аукциона: (";
        for (AuctionType auctionType: filterAuctionTypes) {
            filterString += auctionType.getName() + ";";
        }
        filterString += ")";

        return filterString;
    }

}
