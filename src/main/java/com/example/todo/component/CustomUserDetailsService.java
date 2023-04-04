package com.example.todo.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.todo.entities.User;
import com.example.todo.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = this.userRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("error-404 not found");
		}
		//this is for granting permission to users assigned his task
		//(i.e. users will only get his task details
//		 List<GrantedAuthority> authorities = new ArrayList<>();
//	        authorities.add(new SimpleGrantedAuthority(user.getRole().toString()));
	//	return new CustomUserDetails(user);
	        return new CustomUserDetails(user);
	}

}
