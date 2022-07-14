package com.projet.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.hibernate5.support.OpenSessionInViewInterceptor;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import com.projet.core.bo.CadreAdministrateur;
import com.projet.core.bo.Compte;
import com.projet.core.bo.Enseignant;
import com.projet.core.bo.Etudiant;
import com.projet.core.bo.JournalisationEvenements;
import com.projet.core.bo.Role;
import com.projet.core.bo.Utilisateur;

 

//Configuration d'une application Spring MVC (@EnableWebMvc)
//Avec support des transactions (@EnableTransactionManagement)

@EnableWebMvc // Configuration d'une Application Spring MVC
@Configuration // Classe de configuration qui va contenir des bean à créer automatiquement par
				// Spring
@ComponentScan(basePackages = { "com.projet" }) // Packages à scanner pour chercher les bean spring de type component
												// (càd controller, repository, service)
@EnableTransactionManagement // support des transactions
public class AppConfig implements WebMvcConfigurer {

	/**
	 * A utiliser si vous voulez faire la journalisation. Voir le fichier log4j.xml
	 */
	private Logger LOGGER = Logger.getLogger(getClass().getName());

	public AppConfig() {

		// On enregistre une trace dans le journal
		LOGGER.debug(" configuration init...");
	}

	// Configuration du ViewResolver

	@Bean
	public ViewResolver internalResourceViewResolver() {
		InternalResourceViewResolver bean = new InternalResourceViewResolver();
		bean.setViewClass(JstlView.class);

		bean.setPrefix("/WEB-INF/view/");
		bean.setSuffix(".jsp");
		return bean;
	}

	// Configuration de la Template HibernateTemplate

	@Bean // Nécessaire pour que Spring créera automatiquement HibernateTemplate et lui
			// injecter les dépendances nécessaires
	@Autowired // Injection de la session factory (HibernateTemplate a besoin de créer les
				// sessions via la session Factory)
	public HibernateTemplate hibernateTemplate(final SessionFactory sessionFactory) {

		// Création du bean HibernateTemplate
		HibernateTemplate hibernateTemplate = new HibernateTemplate();

		// Injection de la session Factory reçu par autowired
		hibernateTemplate.setSessionFactory(sessionFactory);

		// Tracer la bonne création de la template dans le journal
		if (hibernateTemplate != null) {
			LOGGER.debug(" HibernateTemplate created ...");
		}

		return hibernateTemplate;
	}

	// Configuration de la Session Factory de Hibernate
	@Bean // Nécessaire pour que Spring créra automatiquemnt la sessionFactory
	public LocalSessionFactoryBean sessionFactory() {

		// Code copié de la documentation

		final LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();

		sessionFactory.setDataSource(getDataSource());
		sessionFactory.setHibernateProperties(hibernateProperties());

		// TODO: Indiquer vos classes annotées par @Entity ici
		sessionFactory.setAnnotatedClasses(CadreAdministrateur.class, Compte.class,  
			  Enseignant.class, Etudiant.class,JournalisationEvenements.class,
				  Role.class, Utilisateur.class);

		// Tracer dans le journal pour des raisons juste de débougage
		// que la session Factory a été bien crée
		if (sessionFactory != null) {
			LOGGER.debug(" sessionFactory created ...");
		}

		return sessionFactory;
	}

	// Les propriétés de la configuration Hibernate
	public Properties hibernateProperties() {

		Properties hibernateProperties = new Properties();

		hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "update");
		hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MariaDB103Dialect");
		hibernateProperties.setProperty("hibernate.show_sql", "true");

		// D'autres propriétés si nécessaire ....

		return hibernateProperties;
	}

	// Configuration de la source de données
	@Bean // nécessaire car Spring va créer la datasource automatiquement et l'injecter
			// apres dans la session factory
	public DataSource getDataSource() {

		// Les informations de votre base de données

		BasicDataSource dataSource = new BasicDataSource();

		// TODO : A mettre à jour en fonction de votre base de données
		dataSource.setDriverClassName("org.mariadb.jdbc.Driver"); // Driver
		dataSource.setUrl("jdbc:mysql://localhost:3306/accounts"); // L'url d'accès à la base de données
		dataSource.setUsername("root"); // login
		dataSource.setPassword(""); // mot de passe
		return dataSource;
	}

	// Permet de traduire toutes les exceptions de la couche persistance en une
	// seule exception
	// de type PersistenceExceptionTranslationPostProcessor (embalage des execptions
	// de la couche de persistance)
	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

	// Configuration du Gestionnaire des Transactions
	// Spring définit une API pour la gestion des Transactions
	// (PlatformTransactionManager) qui cache l'implémentation réelle du
	// gestionnaire
	// de transactions pour ne pas dépendre l'application (couche service) avec
	// les implémentations (avec les frameworks sous-jacents utilisés dans la couche
	// dao)
	// Ici nous avons configué un gestionnaire de transactions pour Hibernate
	// (HibernateTransactionManager) mais que nous retournons sous forme de
	// PlatformTransactionManager
	// (car HibernateTransactionManager est une implémentation de
	// HibernateTransactionManager)t

	@Bean // Permet à Spring de créer automatiquement ce bean de gestion des transaction
			// et de l'injecter dans son bon endroit pour l'utilsier dans la couche service
			// dans les services annotées par @transactional et @service
	@Autowired // Permet d'injecter sessionFactory nécessaire pour le gestionnaire de
				// transaction
	public PlatformTransactionManager transactionManager(final SessionFactory sessionFactory) {

		// Création du gestionnaire de transaction de Hibernate
		final HibernateTransactionManager txManager = new HibernateTransactionManager();

		// On lui injecte la session Factory crée dans un autre bean un peu plus haut
		// dans cette classe
		txManager.setSessionFactory(sessionFactory);

		// Tracer si vous voulez
		if (txManager != null) {
			LOGGER.debug(" Hibernate Transaction Manager created ...");

		}

		return txManager;

	}

	// Spring security Custom Success Handler: Permet de définir un gestionnaire
	// personnalisé pour la
	// redirection après authenitication avec succès
	//
	@Bean // nécessaire car c'est Spring qui créer automatiquement cette classe de type
			// MySimpleUrlAuthenticationSuccessHandler
	public AuthenticationSuccessHandler redirectionAfterAuthenticationSuccessHandler() {
		return new RedirectionAfterAuthenticationSuccessHandler();
	}

	// Permet d'éviter les exception Lazy
	public void addInterceptors(InterceptorRegistry registry) {
		OpenSessionInViewInterceptor openSessionInViewInterceptor = new OpenSessionInViewInterceptor();
		openSessionInViewInterceptor.setSessionFactory(sessionFactory().getObject());
		
		
		registry.addWebRequestInterceptor(openSessionInViewInterceptor).addPathPatterns("/**").order(Ordered.HIGHEST_PRECEDENCE);
		registry.addInterceptor(myInterceptor()).addPathPatterns("/*");
		registry.addInterceptor(myInterceptor()).addPathPatterns("/admin/*");
	}
	

	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
		
		
	}@Bean
	MyInterceptor myInterceptor() {
		return new MyInterceptor();
	}

}