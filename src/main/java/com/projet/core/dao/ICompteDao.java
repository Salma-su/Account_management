package com.projet.core.dao;

import com.projet.core.bo.Compte;
import com.projet.genericdao.GenericDao;

public interface ICompteDao extends GenericDao<Compte, Long> {
	public Compte findByUsername(String username);
	 

}
