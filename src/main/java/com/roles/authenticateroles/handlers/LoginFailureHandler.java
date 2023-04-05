package com.roles.authenticateroles.handlers;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoginFailureHandler implements AuthenticationFailureHandler {
	
	private RedirectStrategy redirectStrategy=new DefaultRedirectStrategy();
	protected Log logger =LogFactory.getLog(this.getClass());

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		String url="/login?error=true";
		logger.error("error "+exception.getMessage());
		request.setAttribute("error", exception.getMessage());
		redirectStrategy.sendRedirect(request, response, url);
		

	}

}
