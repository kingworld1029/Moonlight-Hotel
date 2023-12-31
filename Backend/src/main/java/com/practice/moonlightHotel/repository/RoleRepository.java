package com.practice.moonlightHotel.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.practice.moonlightHotel.model.Role;
import com.practice.moonlightHotel.model.User;

public interface RoleRepository extends JpaRepository<Role, Long> {

	Optional<Role> findByName(String string);

	boolean existsByName(String roleName);

}
