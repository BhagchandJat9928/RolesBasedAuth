package com.roles.authenticateroles.configuration;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.GenericFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

public class UserPasswordFilter extends GenericFilter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub

	}

	

}
