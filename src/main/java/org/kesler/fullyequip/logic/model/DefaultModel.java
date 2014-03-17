package org.kesler.fullyequip.logic.model;

import org.apache.log4j.Logger;
import org.kesler.fullyequip.dao.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Модель по умолчанию для получения данных
 */
public class DefaultModel<T extends AbstractEntity> implements DAOListener{

    protected static Logger log;

    private List<ModelStateListener> listeners;

    private GenericDAO<T> genericDAO;
    private List<T> items;

    public DefaultModel(Class<T> type) {
        log = Logger.getLogger("DefaultModel<" + type.getSimpleName() + ">");
        genericDAO = DAOFactory.getInstance().getGenericDAO(type);
        genericDAO.addDAOListener(this);

        listeners = new ArrayList<ModelStateListener>();

        items = new ArrayList<T>();
    }

    public void addModelStateListener(ModelStateListener listener) {
        listeners.add(listener);
    }

    // добавление сущности

    public int addItem(T item) {
        log.info("Adding Item");
        items.add(item);
        genericDAO.saveItem(item);
        return items.size()-1;
    }

    public void addItemInSeparateProcess(T item) {

    }

    // Изменение сущности

    public void updateItem(T item) {
        log.info("Update Item");
        genericDAO.updateItem(item);
    }


    public void saveOrUpdateItem(T item) {
        log.info("SaveOrUpdate Item");
        genericDAO.saveOrUpdateItem(item);
    }

    // чтение списка сущностей из БД

    public void readItemsFromDB() {
        log.info("Reading all Items from DB");
        items = genericDAO.getAllItems();
        notifyListeners(ModelState.UPDATED);
    }

    public void readItemsInSeparateThread() {
        Thread reader = new Thread(new Runnable() {
            @Override
            public void run() {
                readItemsFromDB();
            }
        });
        reader.start();
    }

    public List<T> getAllItems() {
        return items;
    }


    public void removeItem(T item) {
        log.info("Removing item");
        genericDAO.removeItem(item);
        items.remove(item);
    }

    @Override
    public void daoStateChanged(DAOState state) {
        switch (state) {
            case CONNECTING:
                notifyListeners(ModelState.CONNECTING);
                break;

            case READING:
                notifyListeners(ModelState.READING);
                break;

            case WRITING:
                notifyListeners(ModelState.WRITING);
                break;

            case READY:
                notifyListeners(ModelState.READY);
                break;

            case ERROR:
                notifyListeners(ModelState.ERROR);
                break;
        }

    }

    private void notifyListeners(ModelState state) {
        for (ModelStateListener listener: listeners) {
            listener.modelStateChanged(state);
        }
    }

}
