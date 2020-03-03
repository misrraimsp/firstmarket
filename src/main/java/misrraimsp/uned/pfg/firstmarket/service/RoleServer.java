package misrraimsp.uned.pfg.firstmarket.service;

import misrraimsp.uned.pfg.firstmarket.data.RoleRepository;
import misrraimsp.uned.pfg.firstmarket.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServer {

    private RoleRepository roleRepository;

    @Autowired
    public RoleServer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role findByName(String role_name) {
        return roleRepository.findByName(role_name);
    }

    public Role persist(Role role) {
        return roleRepository.save(role);
    }
}
