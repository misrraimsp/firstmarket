package misrraimsp.uned.pfg.firstmarket.core.service;

import misrraimsp.uned.pfg.firstmarket.core.data.RoleRepository;
import misrraimsp.uned.pfg.firstmarket.core.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServer {

    private final RoleRepository roleRepository;

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
