package com.projet.core.web.controllers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.hibernate.Session;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.projet.core.services.IPersonService;
import com.projet.core.bo.CadreAdministrateur;
import com.projet.core.bo.Compte;
import com.projet.core.bo.Enseignant;
import com.projet.core.bo.Etudiant;
import com.projet.core.bo.JournalisationEvenements;
import com.projet.core.bo.Role;
import com.projet.core.bo.UserPrincipal;
import com.projet.core.bo.Utilisateur;
import com.projet.core.dao.ICompteDao;
import com.projet.core.services.ICompteService;
import com.projet.core.web.models.AccountModel;
import com.projet.core.web.models.PersonModel;
import com.projet.core.web.models.UserActivities;
import com.projet.core.web.models.UserAndAccountInfos;

@Controller
@RequestMapping("/admin") // Très important car, dans Spring security les URL qui commencent par ADMIN
							// sont dédiées
							// à l'admin. Toutes les URL dédinies dans ce controleur définissent des
							// fonctionnalités
							// accessibles uniquement à l'administrateur
public class AccountController {
	
	 
	@Autowired
	private ICompteService userService; // On obtient par injection automatique le service
	
	 
	
	@Autowired
	private ICompteDao userDao; // On obtient par injection automatique le service

	@Autowired
	private IPersonService personService; // On obtient par injection automatique le service
	
	@Autowired
	private HttpSession session ;
	
	//@Autowired
	//private IRoleService role;
	
	//Cette méthode initialise le formulaire de création de compte
	@RequestMapping(value = "createAccountForm/{idPerson}", method = RequestMethod.GET)
	public String createAccountForm(@PathVariable int idPerson, Model model) {

		
		//On crée le model 
		AccountModel accountModel = new AccountModel(Long.valueOf(idPerson));

		//On enregistre le modèle pour le passer à la vue
		model.addAttribute("accountModel", accountModel);

		//On ajoute la liste des roles dans le modele 
		model.addAttribute("roleList", userService.getAllRoles());
		
		//On ajoute également la liste des comptes dans le modèle
		model.addAttribute("accountList", userService.getAllAccounts());

		
		//On affiche la vue
		return "admin/formAccount";
	}

	@GetMapping("manageAccounts")
	public String manageAccounts(@ModelAttribute("accountModel") AccountModel accountModel, Model model) {

		model.addAttribute("accountList", userService.getAllAccounts());

		return "admin/accountList";
	}
	
	@GetMapping("createAccounts")
	public String createAccounts(@ModelAttribute("accountModel") AccountModel accountModel, Model model) {


		model.addAttribute("personList", personService.getAllPersons());

		return "admin/accountCreation";
	}
	
	
	//Cette méthode permet de créer un comote
	@PostMapping("addAccount")
	public String addAccount(@ModelAttribute("accountModel") AccountModel accountModel, Model model) {

		
		//La création du compte est implémenter au niveau service
		//Il suffit de passer l'id du role et l'id de personne
		//à la couche service
		String password = userService.createUser(accountModel.getRoleId(), accountModel.getPersonId());

		//On affiche le mot de passe dans la vue 
		accountModel.setPassword(password);
		
		//On affiche également la liste des comptes dans la vue
		model.addAttribute("accountList", userService.getAllAccounts());

		
		//On affiche la vue 
		return "/admin/accountList";

	}
	
	////
	////
	///
	
	@RequestMapping(value = "updateAccountForm/{login}", method = RequestMethod.GET)
	public String updateAccountForm(@PathVariable String login, Model model) {

		// On recoit comme paramètre le login du compte à mettre à jour
		Compte cpt = userService.getAccountByUserName(login);

		// On construit le modèle
		AccountModel pm = new AccountModel();

		// En fonction due type de l'utilisateur à modifier
		// Ceci va nous pemettre d'afficher un formulaire adapté
		// selon le type de la personne
		 
			 
		BeanUtils.copyProperties(cpt, pm);

		// Initialiser le modele avec la personne
		model.addAttribute("AccountModel", pm);

		return "admin/updateAccountrole";
	}
	
	
	//////
	/////
	@RequestMapping("updateAccount")
	public String updateAccount(@Valid @ModelAttribute("AccountModel") AccountModel account, BindingResult bindingResult,
			Model model) {

		// En cas d'erreur
		if (bindingResult.hasErrors()) {

			return "admin/updateAccountrole";
		}

		// On copie les données du modèle vers l'objet métier puis on appel le service
		// pour faire la mise à jour
		 
			Compte ct = new Compte();
			BeanUtils.copyProperties(account, ct);

			userService.updateAccount(ct);

		 

		// Mettre le message de succès dans le modèle
		model.addAttribute("msg", "Opération effectuée avec succès");

		return "admin/updateForm";
	}

	///////
	
	///
	//Cette méthode permet de renitialiser un mdp
	//
	@RequestMapping(value = "changePassword/{login}", method = RequestMethod.GET)
	public String changePassword(@PathVariable String login,Model model) {

		AccountModel accountModel = new AccountModel();
		accountModel.setUsername(String.valueOf(login));
		
		String password = userService.changePassword(login);
		
		// Alternative
		Compte compte = userService.getAccountByUserName(login);
		List<Compte> comptes =  new ArrayList<Compte>();
		comptes.add(compte);
		////
		
		//La création du compte est implémenter au niveau service
		//Il suffit de passer l'id du role et l'id de personne
		//à la couche service 
		 

		//String password = userService.changePassword(accountModel.getUsername());
		//On affiche le mot de passe dans la vue 
		accountModel.setPassword(password);
		model.addAttribute("accountModel", accountModel);
		model.addAttribute("accountList",comptes);
		
		
		 
		
		//On affiche la vue 
		return "/admin/accountList";

	}
	//role methods
/////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////
	// this method is for updating role (first method) for getting login account
	//
	
	@RequestMapping(value = "updateRole/{login}", method = RequestMethod.GET)
	public String updateRole(@PathVariable String login,Model model) {
		
		//search about an account by login 
		AccountModel accountModel = new AccountModel();
		accountModel.setUsername(String.valueOf(login));
		
		Compte cpt = new Compte();
		cpt= userService.getAccountByUserName(login);
		 
		 
		model.addAttribute("accountModel", accountModel);
		model.addAttribute("roleList",userService.getAllRoles());
		 
		
		//show view 
		return "/admin/formAccountt";

	}
	
	
	// this is the second method for updating role for place the new role

	
	
	//@PostMapping(path = "/modifyRole/{username}")
	@RequestMapping(value = "modifyRole/{username}", method = RequestMethod.POST)
	public String modifyRole(@PathVariable String username,@ModelAttribute("accountModel") AccountModel accountModel,Model model2) {

		 
		// Alternative
		Compte compte = userService.getAccountByUserName(accountModel.getUsername());
		
		Role rolec = userService.getRoleByIdd(accountModel.getRoleId());
		
		compte.setRole(rolec);
		userService.updateAccount(compte);
		//
		List<Compte> comptess =  new ArrayList<Compte>();
		comptess.add(compte);
		////
		 
		 
		model2.addAttribute("accountList",comptess);
		
		 
		
		//On affiche la vue 
		return "/admin/accountList";

	}
	
	/////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////
	
	
//	@PostMapping("modifyRole")
//	public String updateRole2(@PathVariable String login,Model model,@ModelAttribute("accountModel") AccountModel accountModel,Model model2) {
//
//		 
//		// Alternative
//		Compte compte = userService.getAccountByUserName(accountModel.getUsername());
//		
//		Role rolec = role.getRoleById(accountModel.getRoleId());
//		
//		compte.setRole(rolec);
//		userService.updateAccount(compte);
//		//
//		List<Compte> comptess =  new ArrayList<Compte>();
//		comptess.add(compte);
//		////
//		 
//		 
//		model2.addAttribute("accountList",comptess);
//		
//		 
//		
//		//On affiche la vue 
//		return "/admin/accountList";
//
//	}
	
	
	
	 
	
	
	
	//avtiver un compte 
	@RequestMapping(value = "activeAccount/{idCompte}", method = RequestMethod.GET)
	public String activeAccount(@PathVariable int idCompte) {

		userService.activeAccount(Long.valueOf(idCompte));

		return "redirect:/admin/manageAccounts";
	}
	
	//desactiver un compte
	@RequestMapping(value = "desactiveAccount/{idCompte}", method = RequestMethod.GET)
	public String desactiveAccount(@PathVariable int idCompte) {

		userService.desactiveAccount(Long.valueOf(idCompte));

		return "redirect:/admin/manageAccounts";
	}
	
	//
	//
	
	 
	
 
}
