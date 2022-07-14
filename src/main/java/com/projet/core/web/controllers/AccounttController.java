package com.projet.core.web.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.projet.core.bo.CadreAdministrateur;
import com.projet.core.bo.Compte;
import com.projet.core.bo.Enseignant;
import com.projet.core.bo.Etudiant;
import com.projet.core.bo.JournalisationEvenements;
import com.projet.core.bo.Role;
import com.projet.core.bo.Utilisateur;
import com.projet.core.dao.ICompteDao;
import com.projet.core.services.ICompteService;
import com.projet.core.services.IPersonService;
import com.projet.core.web.models.AccountModel;
import com.projet.core.web.models.PersonModel;
import com.projet.core.web.models.UserActivities;

@Controller
@RequestMapping("/admin") 
public class AccounttController {
	 
	@Autowired
	private ICompteService userService; // injection automatique of service
	
	 
	
	 
	@Autowired
	private IPersonService personService; // injection automatique of service
	
	@Autowired
	private HttpSession session ;
	
	
	// this method is for fetch user activities from httpServletRequest Object to 
	//a database JournalisationEvenements
	
	@RequestMapping(value = "fetchUserActivities", method = RequestMethod.GET)
	public String fetchUserActivities(@RequestParam int typePerson, Model model
			,HttpServletRequest rq) {
		
		HttpSession session = rq.getSession();

		// 1-first thing I want to get data(url's visited, ip adress of user and date&time)
		//from the session as httpRequestServlet
		
		Date d=new Date();
		int id = Integer.valueOf(rq.getParameter("id"));
		String url = rq.getPathInfo();
		String IP = rq.getLocalAddr();
		
		// 2 -Pass the data fetched to a model 'userActivities' that is already created
		// it can be modifiable
		 
		UserActivities  userActivities = new UserActivities();
		model.addAttribute("userActivities", userActivities);
		return null;
		
		//3-add model information to 'JournalisationEvenements' database
		
			
		}

		 
		
		// This second method is for fetching data from database'JournalisationEvenements'
		// and show it in a view
		
		@PostMapping("getDataFromdb")
		public String getDataFromdb(@Valid @ModelAttribute("userActivities") UserActivities activity,
				Model model, HttpServletRequest rq) {
			
			 
				JournalisationEvenements active = new JournalisationEvenements();
				return null;
			
			 
		}
	
	
}
