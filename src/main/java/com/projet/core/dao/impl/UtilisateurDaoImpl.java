package com.projet.core.dao.impl;

import org.springframework.stereotype.Repository;

import com.projet.core.bo.Etudiant;
import com.projet.core.bo.Utilisateur;
import com.projet.core.dao.IUtilisateurDao;
import com.projet.genericdao.HibernateSpringGenericDaoImpl;

@Repository
public class UtilisateurDaoImpl extends HibernateSpringGenericDaoImpl<Utilisateur, Long> implements IUtilisateurDao {

	public UtilisateurDaoImpl() {
		super(Utilisateur.class);
	}

}
