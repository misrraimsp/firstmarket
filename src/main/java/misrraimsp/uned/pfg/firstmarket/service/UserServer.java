package misrraimsp.uned.pfg.firstmarket.service;

import misrraimsp.uned.pfg.firstmarket.data.RoleRepository;
import misrraimsp.uned.pfg.firstmarket.data.UserRepository;
import misrraimsp.uned.pfg.firstmarket.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class UserServer implements UserDetailsService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;

    @Autowired
    public UserServer(UserRepository userRepository, PasswordEncoder passwordEncoder,
                      RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User '" + username + "' not found");
        }
        return user;
    }

    public void persist(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        //Role role = roleRepository.findByName("ROLE_USER");

        /*
        List<User> users = role.getUsers();
        users.add(user);
        role.setUsers(users);
         */

        //All new users are set to ROLE_USER
        /*List<Role> roles = user.getRoles();roles.add(role);user.setRoles(roles);*/
        user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")));

        userRepository.save(user);
        //roleRepository.save(role);
    }

}