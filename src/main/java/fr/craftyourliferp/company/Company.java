package fr.craftyourliferp.company;

import java.util.ArrayList;
import java.util.List;

public class Company {

	private CompanyData data;
	
	private List<CompanyUser> users = new ArrayList();
	
	public Company(CompanyData data, List<CompanyUser> users)
	{
		this.data = data;
		this.users = users;
	}
	
	public List<CompanyUser> getUsers()
	{
		return users;
	}
	
	public CompanyData getData()
	{
		return data;
	}
	
	
	
}
