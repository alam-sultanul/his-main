package com.bz.demo.service.impl;


import com.bz.demo.repository.RoleRepository;
import com.bz.demo.model.security.Role;
import com.bz.demo.service.RoleService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class RoleServiceImpl implements RoleService {
    @NonNull private final RoleRepository roleRepository;

    @Override public List<Role> getRoles() {
        return roleRepository.findAll();
    }

}
