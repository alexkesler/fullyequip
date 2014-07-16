package org.kesler.fullyequip.dao.impl;

import java.util.List;
import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.HibernateException;
import org.apache.log4j.Logger;

import org.kesler.fullyequip.dao.GenericDAO;
import org.kesler.fullyequip.dao.AbstractEntity;
import org.kesler.fullyequip.dao.DAOState;
import org.kesler.fullyequip.dao.DAOListener;

import org.kesler.fullyequip.util.HibernateUtil;

public class GenericDAOHibernateImpl<T extends AbstractEntity> implements GenericDAO <T> {
	protected final Logger log;

	private List<DAOListener> listeners = new ArrayList<DAOListener>();

	private Class<T> type;

	public GenericDAOHibernateImpl(Class<T> type) {
		this.type = type;
		log = Logger.getLogger("GenericDAO<"+ type.getSimpleName() + ">");
	}

	@Override
	public void addDAOListener(DAOListener listener) {
		listeners.add(listener);
	}

	public Long saveItem(T item) {
		Long id = null;

		notifyListeners(DAOState.CONNECTING);
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			log.info("Begin to write item");
			notifyListeners(DAOState.WRITING);
			tx = session.beginTransaction();
			session.save(item);
			session.flush();
			session.clear();
			tx.commit();
			log.info("Adding item complete");
			notifyListeners(DAOState.READY);
		} catch (HibernateException he) {
			if (tx != null) tx.rollback();
			log.error("Error writing item", he);
			he.printStackTrace();
			notifyListeners(DAOState.ERROR);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}

		id = item.getId();

		return id;
	}

	public void updateItem(T item) {

		notifyListeners(DAOState.CONNECTING);
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			log.info("Updating item");
			notifyListeners(DAOState.WRITING);
			tx = session.beginTransaction();
			session.update(item);
			session.flush();
			session.clear();
			tx.commit();
			log.info("Updating item complete");
			notifyListeners(DAOState.READY);
		} catch (HibernateException he) {
			if (tx != null) tx.rollback();
			log.error("Error updating item", he);
			notifyListeners(DAOState.ERROR);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();		
			}
		}		

	}

    public void saveOrUpdateItem(T item) {

        notifyListeners(DAOState.CONNECTING);
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            log.info("Updating item");
            notifyListeners(DAOState.WRITING);
            tx = session.beginTransaction();
            session.saveOrUpdate(item);
            session.flush();
            session.clear();
            tx.commit();
            log.info("Updating item complete");
            notifyListeners(DAOState.READY);
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            log.error("Error updating item", he);
            notifyListeners(DAOState.ERROR);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }

    }


    public T getItemById(long id) {
		T item = null;

		notifyListeners(DAOState.CONNECTING);
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			log.info("Reading item");
			notifyListeners(DAOState.READING);
			item = (T) session.load(type, id);
			log.info("Reading item complete");
			notifyListeners(DAOState.READY);
		} catch (HibernateException he) {
			log.error("Reading item error", he);
			notifyListeners(DAOState.ERROR);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();				
			}

		}
		return item;
	}

	public List<T> getAllItems() {
		List<T> list = new ArrayList<T>();
		notifyListeners(DAOState.CONNECTING);
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			log.info("Reading items");
			notifyListeners(DAOState.READING);
			list = session.createCriteria(type).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
			log.info("Read " + list.size() + " items");
			notifyListeners(DAOState.READY);
		} catch (HibernateException he) {
			log.error("Error reading items", he);
			notifyListeners(DAOState.ERROR);
		} finally {
			if (session!=null && session.isOpen()) {
				session.close();				
			}
		}

		return list;
	}

	public void removeItem(T item) {

		notifyListeners(DAOState.CONNECTING);
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			log.info("Removing item");
			notifyListeners(DAOState.WRITING);
			tx = session.beginTransaction();
			session.delete(item);
			session.flush();
			session.clear();
			tx.commit();
			log.info("Item removed");
			notifyListeners(DAOState.READY);
		} catch (HibernateException he) {
			if (tx != null) tx.rollback();
			log.error("Error removing item", he);
			notifyListeners(DAOState.ERROR);
		} finally {
			if (session!=null && session.isOpen()) {
				session.close();				
			}			
		}

	}

	protected void notifyListeners(DAOState state) {
		for (DAOListener listener: listeners) {
			listener.daoStateChanged(state);
		}
	}

}