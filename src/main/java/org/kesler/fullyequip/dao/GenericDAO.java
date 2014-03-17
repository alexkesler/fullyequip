package org.kesler.fullyequip.dao;

import java.util.List;

public interface GenericDAO <T extends AbstractEntity> extends DAOObservable{
	
	public Long saveItem(T item);

	public void updateItem(T item);

    public void saveOrUpdateItem(T item);

	public T getItemById(long id);

	public List<T> getAllItems();

	public void removeItem(T item);
}