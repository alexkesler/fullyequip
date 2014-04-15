package org.kesler.fullyequip.dao;

import org.kesler.fullyequip.dao.impl.GenericDAOHibernateImpl;

import org.kesler.fullyequip.logic.Unit;

public class DAOFactory {
	private static GenericDAO<Unit> unitDAO = null;

	
	private static DAOFactory instance = null;

	public static synchronized DAOFactory getInstance() {
		if (instance == null) {	
			instance = new DAOFactory();
		}
		return instance;
	}


	public GenericDAO<Unit> getUnitDAO() {
		if (unitDAO == null) {
			unitDAO = new GenericDAOHibernateImpl<Unit>(Unit.class);
		}
		
		return unitDAO;
	}

    public <T extends AbstractEntity> GenericDAO<T> getGenericDAO(Class<T> type) {
        GenericDAO<T> genericDAO = new GenericDAOHibernateImpl<T>(type);

        return genericDAO;
    }

}