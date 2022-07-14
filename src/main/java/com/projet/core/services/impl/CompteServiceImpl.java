package com.projet.core.services.impl;

import java.util.List;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projet.core.bo.Compte;
import com.projet.core.bo.Role;
import com.projet.core.bo.UserPrincipal;
import com.projet.core.bo.Utilisateur;
import com.projet.core.dao.ICompteDao;
import com.projet.core.dao.IRoleDao;
import com.projet.core.dao.IUtilisateurDao;
import com.projet.core.services.ICompteService;
 

@Service
@Transactional
public class CompteServiceImpl implements ICompteService {

	@Autowired
	private ICompteDao userDao;
	
	 


	@Autowired
	private IRoleDao roleDao;

	@Autowired
	private IUtilisateurDao personDao;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public List<Role> getAllRoles() {
		return roleDao.getAll();
	}



	public List<Compte> getAllAccounts() {
		return userDao.getAll();
	}
	public String createUser(Long idRole, Long idPerson) {

		// récupérer la personne de la base de données
		Utilisateur person = personDao.findById(idPerson);

		// Créer le compte
		Compte userAccount = new Compte();

		// determiner la personne
		userAccount.setProprietaire(person);

		

		// Affecter le role
		userAccount.setRole(roleDao.findById(idRole));

		// génrer le mot de passe aléatoirement
		String generatedPass = generatePassayPassword();


		// hachage du mot de passe + gain de sel
		String encodedPass = passwordEncoder.encode(generatedPass);
		

		// affecter ce mot de passe
		userAccount.setPassword(encodedPass);

		
		
		//On construit un login de type "nom+prenom " s'il est dispo
		String login = person.getNom() + person.getPrenom();

		List<Compte> accounts = userDao.getEntityByColValue("Compte", "login", login);

		if (accounts == null || accounts.size() == 0) {
			
			userAccount.setLogin(login);
			
			//Créer le compte
			userDao.create(userAccount);
			return generatedPass;
		}

		int i = 0;

		// sinon, on cherche un login de type nom+prenom+"_"+ entier
		while (true) {

			login = person.getNom() + person.getPrenom() + "_" + i;
			accounts = userDao.getEntityByColValue("Compte", "login", login);
			if (accounts == null || accounts.size() == 0) {
				userAccount.setLogin(login);
				
				//Créer le compte
				userDao.create(userAccount);
				return generatedPass;
			}

			i++;
		}
	}
//
 // change mdp
//
	public String changePassword(String login) {
		// récupérer le compte de la base de données
				Compte cpt = new Compte();
				cpt = userDao.findByUsername(login);

				//String oldPassword = userDao.delete(compte,compte.getPassword());
				
				// génrer le mot de passe aléatoirement
				String generatedPass = generatePassayPassword();


				// hachage du mot de passe + gain de sel
				String encodedPass = passwordEncoder.encode(generatedPass);
				

				// affecter ce mot de passe
				cpt.setPassword(encodedPass);
				
				userDao.update(cpt);
				
				return generatedPass;			 
			}

		 
 
	//génère le mot de passe. Il se base sur Passay
	public String generatePassayPassword() {
		CharacterRule digits = new CharacterRule(EnglishCharacterData.Digit);

		PasswordGenerator passwordGenerator = new PasswordGenerator();
		String password = passwordGenerator.generatePassword(10, digits);

		return password;
	}



 
	public Compte getAccountByUserName(String login) {
		if(userDao.getEntityByColValue("Compte", "login", login).isEmpty())
			return null;
		
		return userDao.getEntityByColValue("Compte", "login", login).get(0);
	}
	
	 

	@Override
	public void activeAccount(Long idCompte) {
		 userDao.findById(idCompte).setEnabled(true);
		  
	}
	 
	@Override
	public void desactiveAccount(Long idCompte) {
		 
		  userDao.findById(idCompte).setEnabled(false);
	}

	public void updateAccount(Compte cpt) {
		userDao.update(cpt);

	}

	@Override
	public Compte getAccountById(Long idCompte) {
		 
		return null ;
	}
    
	


	@Override
	public List<Compte> getRoleById() {
		 
		return null;
	}
	
//	@Override
//	public Role getRoleByIdd(Role r) {
//		Role rol = new Role();
//		 roleDao.findById(rol.getIdRole());
//		return  roleDao.findById(rol.getIdRole()) ;
//	}


	@Override
	public Role getRoleByIdd(Long idRole) {
		Role rol = new Role();
		 roleDao.findById(rol.getIdRole());
		return  roleDao.findById(rol.getIdRole()) ;
	}



	 



	 



	 
}

