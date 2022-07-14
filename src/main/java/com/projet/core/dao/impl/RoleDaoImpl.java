package com.projet.core.dao.impl;

import org.springframework.stereotype.Repository;

import com.projet.core.bo.Role;
import com.projet.core.dao.IRoleDao;
import com.projet.genericdao.HibernateSpringGenericDaoImpl;

 
@Repository
public class RoleDaoImpl extends HibernateSpringGenericDaoImpl<Role, Long> implements IRoleDao{

	public RoleDaoImpl() {
		super(Role.class);
	}

	
	
}
