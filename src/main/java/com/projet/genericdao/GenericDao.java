package com.projet.genericdao;

import java.util.List;


public interface GenericDao<T, PK> {

	
	PK create(T o);

	
	void update(T o);

	 
	 
	List<T> getAll();

	 
	List<T> getAllDistinct();

	 
	T findById(PK pId) throws EntityNotFoundException ;

	 
	List<T> getEntityByColValue(String ClassName, String pColumnName, String pValue);

	 
	void delete(PK pId) throws EntityNotFoundException;

	 
	boolean exists(PK pId);



}
