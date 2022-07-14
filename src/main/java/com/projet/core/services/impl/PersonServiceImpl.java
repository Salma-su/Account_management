package com.projet.core.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projet.core.bo.Etudiant;
import com.projet.core.bo.Utilisateur;
import com.projet.core.dao.IUtilisateurDao;
import com.projet.core.services.IPersonService;

 
@Service
@Transactional
public class PersonServiceImpl implements IPersonService {

	@Autowired
	private IUtilisateurDao personDao;
	
	@Autowired
	private IUtilisateurDao etudiant;
	

	public List<Utilisateur> getAllPersons() {

		return personDao.getAll();
	}

	public void deletePerson(Long id) {
		personDao.delete(id);

	}

	public Utilisateur getPersonById(Long id) {
		return personDao.findById(id);

	}

	public void addPerson(Utilisateur pPerson) {
		personDao.create(pPerson);

	}

	public void updatePerson(Utilisateur pPerson) {
		personDao.update(pPerson);

	}

 

	public Utilisateur getPersonByCin(String cin) {
		List<Utilisateur> u = personDao.getEntityByColValue("Utilisateur", "cin", cin);
		if (u != null && u.size() != 0) {
			return personDao.getEntityByColValue("Utilisateur", "cin", cin).get(0);
		}

		return null;

	}
	public Etudiant getPersonByCne(String cne) {
		List<Utilisateur> e = etudiant.getEntityByColValue("Etudiant", "cne", cne);
		if (e != null && e.size() != 0) {
			return (Etudiant) etudiant.getEntityByColValue("Etudiant", "cne", cne).get(0);
		}

		return null;

	}

}
