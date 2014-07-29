package org.kesler.fullyequip.gui.reestr;

import org.kesler.fullyequip.gui.dialog.unit.UnitDialogController;
import org.kesler.fullyequip.gui.reestr.column.ReestrColumnsDialog;
import org.kesler.fullyequip.gui.reestr.export.ReestrExportEnum;
import org.kesler.fullyequip.gui.reestr.export.ReestrExporterFactory;
import org.kesler.fullyequip.gui.reestr.filter.UnitsFilterDialog;
import org.kesler.fullyequip.gui.reestr.filter.UnitsFilterDialogFactory;
import org.kesler.fullyequip.gui.util.InfoDialog;
import org.kesler.fullyequip.gui.util.ProcessDialog;
import org.kesler.fullyequip.logic.Unit;
import org.kesler.fullyequip.logic.model.ModelState;
import org.kesler.fullyequip.logic.model.ModelStateListener;
import org.kesler.fullyequip.logic.model.UnitsModel;
import org.kesler.fullyequip.logic.unitsfilter.UnitsFilter;
import org.kesler.fullyequip.logic.unitsfilter.UnitsFiltersEnum;
import org.kesler.fullyequip.logic.unitsfilter.UnitsFiltersModel;

import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JFrame;


public class ReestrViewController implements ModelStateListener{

	private static ReestrViewController instance = null;	
	private ReestrView view;

	private UnitsModel model;
    private UnitsFiltersModel filtersModel;
//	private List<ReceptionsFilter> filters;

	private ProcessDialog processDialog = null;

	private boolean listenReceptionsModel = true;

	public static synchronized ReestrViewController getInstance() {
		if (instance == null) {
			instance = new ReestrViewController();
		}
		return instance;
	}

	private ReestrViewController() {
		model = UnitsModel.getInstance();
		model.addModelStateListener(this);

        filtersModel = model.getFiltersModel();
//		filters = new ArrayList<ReceptionsFilter>();
		// создаем вид с привязкой к этому контроллеру
		
	}

	// Открывает основной вид
	public void openView(JFrame parentFrame) {
		view = new ReestrView(this, parentFrame);
		view.setVisible(true);
	}

	public List<UnitsFilter> getFilters() {
		return filtersModel.getFilters();
	}

	// добавление фильра - вызывается из вида
	public void addFilter(UnitsFiltersEnum filterEnum) {
		
		// Создаем подходящий диалог
		UnitsFilterDialog unitsFilterDialog = UnitsFilterDialogFactory.createDialog(view, filterEnum);
		if (unitsFilterDialog == null) return;

		/// Открываем диалог
		unitsFilterDialog.setVisible(true);

		if (unitsFilterDialog.getResult() == UnitsFilterDialog.OK) {
			int index = filtersModel.addFilter(unitsFilterDialog.getUnitsFilter());
			view.getFilterListModel().filterAdded(index);
		}
	}

	// Редактирование фильтра - вызывается из вида
	public void editFilter(int filterIndex) {
		if (filterIndex == -1) {
			JOptionPane.showMessageDialog(view, "Фильтр не выбран", " Ошибка", JOptionPane.ERROR_MESSAGE);
			return ;
		}

		// Получаем выбранный фильтр
		UnitsFilter receptionsFilter = filtersModel.getFilters().get(filterIndex);

		// Создаем диалог редактирования фильтра
		UnitsFilterDialog receptionsFilterDialog = UnitsFilterDialogFactory.createDialog(view, receptionsFilter);

		if(receptionsFilterDialog!=null) {
            receptionsFilterDialog.setVisible(true);
            if (receptionsFilterDialog.getResult() == UnitsFilterDialog.OK) {
                view.getFilterListModel().filterUpdated(filterIndex);
            }

        }

	}

	public void removeFilter(int index) {
		if (index == -1) {
			JOptionPane.showMessageDialog(view, "Фильтр не выбран", " Ошибка", JOptionPane.ERROR_MESSAGE);
			return ;
		}

		filtersModel.removeFilter(index);
		view.getFilterListModel().filterRemoved(index);
	}

	// открывает диалог редактирования видимых колонок - вызывается из вида
	public void openColumnsDialog() {
		ReestrColumnsDialog reestrColumnsDialog  = new ReestrColumnsDialog(view);
		reestrColumnsDialog.setVisible(true);
	}

	// применяет созданный набор фильтров - вызывается из вида
	public void applyFilters() {

		listenReceptionsModel = true;
		processDialog = new ProcessDialog(view);
		model.applyFiltersInSeparateThread();

	}

    public void readFromDBAndApplyFilters() {
        processDialog = new ProcessDialog(view);
        model.readReceptionsAndApplyFiltersInSeparateThread();
    }

	// Очищает все фильтры
	public void resetFilters() {
		int count = filtersModel.getFilters().size();
		filtersModel.resetFilters();
		view.getFilterListModel().filtersCleared(count);
	}


	public void openUnitDialog(int index) {
		if (index == -1) {
			new InfoDialog(view, "Ничего не выбрано", 1000, InfoDialog.RED).showInfo();
			return;
		}
		List<Unit> units = model.getFilteredUnits();
		Unit unit = units.get(index);
        if (UnitDialogController.getInstance().showDialog(view, unit)) {
            view.tableDataChanged();
        }
	}


	public void exportSelectedColumns() {
        ReestrExporterFactory.createReestrExporter(ReestrExportEnum.SELECTED_COLUMNS).export(view, model.getFilteredUnits());
	}


	// реализует интерфейс для слушателя модели приемов 
	@Override
	public void modelStateChanged(ModelState state) {
		switch (state) {
			case CONNECTING:
				if (processDialog != null) processDialog.showProcess("Соединяюсь");
				break;

			case READING:
				if (processDialog != null) processDialog.showProcess("Читаю список приемов");
				break;

			case UPDATED:
				// Ничего не делаем - ждем выполнения фильтрации
				break;

			case FILTERING:
				if (processDialog != null) processDialog.showProcess("Фильтрую список приемов");
				break;

			case FILTERED:
				if (processDialog != null) {processDialog.hideProcess(); processDialog = null;}
				List<Unit> filteredUnits = model.getFilteredUnits();
				view.setUnits(filteredUnits);
				break;

			case READY:
				// ProcessDialog.hideProcess();
				break;	

			case ERROR:
				if (processDialog != null) {processDialog.hideProcess(); processDialog = null;}
				new InfoDialog(view, "Ошибка базы данных", 1000, InfoDialog.RED).showInfo();
				break;	
			
		}					
	} 
}