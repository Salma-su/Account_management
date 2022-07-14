package com.projet.core.services;

import java.util.List;

import com.projet.core.bo.Compte;
import com.projet.core.bo.Role;
import com.projet.core.bo.Utilisateur;
 

public interface ICompteService {
	
	public List<Role> getAllRoles();
	
	public List<Compte> getAllAccounts();
	
	public List<Compte> getRoleById();
    
	public Role getRoleByIdd(Long r);
	
	public Compte getAccountByUserName(String login);
	
	public String createUser(Long idRole, Long idPerson);
	 
	public Compte getAccountById(Long idCompte);
	
	
	
	 
	public void updateAccount(Compte cpt);
	public void activeAccount(Long idCompte);
	public void desactiveAccount(Long idCompte);
	public String changePassword(String login);
	
	
}
