package com.roles.authenticateroles.handlers;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.SavedRequest;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LoginSuccessHandler  implements AuthenticationSuccessHandler{
	
	protected Log logger =LogFactory.getLog(this.getClass());

	private RedirectStrategy redirectStrategy=new DefaultRedirectStrategy();

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		if(SecurityContextHolder.getContext().getAuthentication()==null) {
		SecurityContextHolder.getContext().setAuthentication(authentication);
		}else {
			logger.warn("context "+SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		}
		 clearAuthenticationAttributes(request);
		
		

	        handle(request, response, authentication);

		}
		
		protected void handle(
		        HttpServletRequest request,
		        HttpServletResponse response, 
		        Authentication authentication
		) throws IOException {

			SavedRequest savedRequest = (SavedRequest) request.getSession()
					.getAttribute("SPRING_SECURITY_SAVED_REQUEST");

			System.out.println(savedRequest.getRedirectUrl());
			String targetUrl = determineTargetUrl(authentication, savedRequest.getRedirectUrl());

		    if (response.isCommitted()) {
		      
				logger.debug(
		                "Response has already been committed. Unable to redirect to "
		                        + targetUrl);
		        return;
		    }
		    
		    try {
				boolean auth=request.authenticate(response);
				System.out.println(auth);
				if(auth) {
					 redirectStrategy.sendRedirect(request, response, targetUrl);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ServletException e) {
				
				e.printStackTrace();
			}

		   
		}

		
		protected String determineTargetUrl(final Authentication authentication, String url) {

		    Map<String, String> roleTargetUrlMap = new HashMap<>();
			roleTargetUrlMap.put("ROLE_USER", url == null ? "/user" : url);
		    roleTargetUrlMap.put("ROLE_ADMIN", "/admin");

		    final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		    System.out.println("simple authentication role"+authorities);
		    for (final GrantedAuthority grantedAuthority : authorities) {
		        String authorityName = grantedAuthority.getAuthority();
		        if(roleTargetUrlMap.containsKey(authorityName)) {
		            return roleTargetUrlMap.get(authorityName);
		        }
		    }

		    throw new IllegalStateException();
		}
		
		protected void clearAuthenticationAttributes(HttpServletRequest request) {
		    HttpSession session = request.getSession(false);
		    if (session == null) {
		        return;
		    }
		    session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		}
}
