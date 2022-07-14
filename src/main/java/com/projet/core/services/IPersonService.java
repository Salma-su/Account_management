package com.projet.core.services;

import java.util.List;

import com.projet.core.bo.Etudiant;
import com.projet.core.bo.Utilisateur;


public interface IPersonService {

	public void addPerson(Utilisateur pPerson);

	public void updatePerson(Utilisateur pPerson);

	public List<Utilisateur> getAllPersons();

	public void deletePerson(Long id);

	public Utilisateur getPersonById(Long id);
	
	public Utilisateur getPersonByCin(String cin);
	
	public Etudiant getPersonByCne(String cne);
	
	
	
	

}
