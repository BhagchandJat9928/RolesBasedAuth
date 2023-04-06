package com.roles.authenticateroles.handlers;

//@Service
public class CustomAuthenticationManager {
	
	/*
	 * @Autowired UserRepository repository;
	 * 
	 * @Override public Authentication authenticate(Authentication authentication)
	 * throws AuthenticationException { String username=authentication.getName();
	 * String password=authentication.getCredentials().toString();
	 * System.out.println(username+" "+password); if(username!=null &&
	 * password!=null) {
	 * 
	 * Optional<User> us=repository.findByUsername(username); User user=us.get();
	 * System.out.println(user); BCryptPasswordEncoder passwordEncoder = new
	 * BCryptPasswordEncoder(); if(!us.isEmpty() &&passwordEncoder.matches(password,
	 * user.getPassword()) ) { return new UsernamePasswordAuthenticationToken(
	 * username,password,List.of(new SimpleGrantedAuthority("ROLE_"+user.getRoles())
	 * ));
	 * 
	 * }else { throw new BadCredentialsException("Bad Credential"); } }else { throw
	 * new BadCredentialsException("Username or Password Cannot be null"); }
	 * 
	 * 
	 * 
	 * }
	 */
	

}