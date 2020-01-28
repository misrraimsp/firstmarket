package misrraimsp.uned.pfg.firstmarket.service;

import misrraimsp.uned.pfg.firstmarket.data.UserRepository;
import misrraimsp.uned.pfg.firstmarket.model.Role;
import misrraimsp.uned.pfg.firstmarket.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserServer implements UserDetailsService {

    private UserRepository userRepository;
    private RoleServer roleServer;

    @Autowired
    public UserServer(UserRepository userRepository, RoleServer roleServer) {
        this.userRepository = userRepository;
        this.roleServer = roleServer;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User '" + username + "' not found");
        }
        return user;
    }

    //if role is not specified it is by default assigned to ROLE_USER
    public User persist(User user, PasswordEncoder passwordEncoder){
        return this.persist(user, passwordEncoder, Arrays.asList(roleServer.findByName("ROLE_USER")));
    }

    public User persist(User user, PasswordEncoder passwordEncoder, List<Role> roles){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(roles);
        return userRepository.save(user);
    }
}