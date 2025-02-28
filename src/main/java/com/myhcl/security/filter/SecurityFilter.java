package com.myhcl.security.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.myhcl.security.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain)
			throws ServletException, IOException
	{
		//read the token from Authorization request header
		String token=request.getHeader("Authorization");
		if(token!=null) {
			String username=jwtUtil.extractUsername(token);
			if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
				UserDetails user=userDetailsService.loadUserByUsername(username);
				//Validate token
				boolean isVailid=jwtUtil.validateToken(token, user.getUsername());
				if(isVailid) {
					UsernamePasswordAuthenticationToken authToken=
							new UsernamePasswordAuthenticationToken(username,
									                                                                                user.getPassword(),
									                                                                                user.getAuthorities()
									                                                                                );
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			}
		}
		filterChain.doFilter(request, response);

	}

}
