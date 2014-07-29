package org.kesler.fullyequip.gui.reestr.filter;

import javax.swing.JFrame;

import org.kesler.fullyequip.logic.unitsfilter.UnitsFilter;
import org.kesler.fullyequip.logic.unitsfilter.UnitsFiltersEnum;


public abstract class UnitsFilterDialogFactory {

    /**
     * Создает диалог создания нового фильтра
     * @param view родительский вид
     * @param filterEnum тип диалога
     * @return  диалог создания фильтра
     */
	public static UnitsFilterDialog createDialog(JFrame view, UnitsFiltersEnum filterEnum) {
		UnitsFilterDialog unitsFilterDialog = null;
		/// Создаем диалог на основнии сведений о необходимом типе диалога
		switch (filterEnum) {
			case PLACE:
				unitsFilterDialog = new PlaceUnitsFilterDialog(view);
			break;
            case AUCTION_TYPE:
                unitsFilterDialog = new AuctionTypeUnitsFilterDialog(view);
                break;
 			default:
				break;
		}

		return unitsFilterDialog;
	}

    /**
     * Создает диалог редактирования фильтра
     * @param view
     * @param unitsFilter
     * @return
     */
	public static UnitsFilterDialog createDialog(JFrame view, UnitsFilter unitsFilter) {

		UnitsFilterDialog unitsFilterDialog = createDialog(view, unitsFilter.getFiltersEnum());
        if(unitsFilterDialog!=null)
            unitsFilterDialog.setUnitsFilter(unitsFilter);

		return unitsFilterDialog;

	}



}