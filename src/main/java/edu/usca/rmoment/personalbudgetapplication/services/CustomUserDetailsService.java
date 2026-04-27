/* This is the custom implementation of the UserDetailsService for the spring security.
* It is responsible for loading use authentication data from the database using the user's
* email address. It converts the applications user entity into a spring security userdetails
* object required for authentication. If user is now found or does not exist it throws an exception.*/

package edu.usca.rmoment.personalbudgetapplication.services;



import edu.usca.rmoment.personalbudgetapplication.model.User; // your entity
import edu.usca.rmoment.personalbudgetapplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));


        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                Collections.emptyList());
    }




}
