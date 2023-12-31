package com.practice.moonlightHotel.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.practice.moonlightHotel.exception.RoleAlreadyExistException;
import com.practice.moonlightHotel.model.Role;
import com.practice.moonlightHotel.model.User;
import com.practice.moonlightHotel.service.IRoleService;
import static org.springframework.http.HttpStatus.FOUND;

public class RoleController {

	@Autowired
	private IRoleService roleService;

	@GetMapping("/all-roles")
	public ResponseEntity<List<Role>> getAllRoles() {
		return new ResponseEntity<>(roleService.getRoles(), FOUND);
	}

	@PostMapping("/create-new-role")
	public ResponseEntity<String> createRole(@RequestBody Role theRole) {
		try {
			roleService.createRole(theRole);
			return ResponseEntity.ok("New role created successfully!");
		} catch (RoleAlreadyExistException re) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(re.getMessage());

		}
	}

	@DeleteMapping("/delete/{roleId}")
	public void deleteRole(@PathVariable("roleId") Long roleId) {
		roleService.deleteRole(roleId);
	}

	@PostMapping("/remove-all-users-from-role/{roleId}")
	public Role removeAllUsersFromRole(@PathVariable("roleId") Long roleId) {
		return roleService.removeAllUsersFromRole(roleId);
	}

	@PostMapping("/remove-user-from-role")
	public User removeUserFromRole(@RequestParam("userId") Long userId, @RequestParam("roleId") Long roleId) {
		return roleService.removeUserFromRole(userId, roleId);
	}

	@PostMapping("/assign-user-to-role")
	public User assignUserToRole(@RequestParam("userId") Long userId, @RequestParam("roleId") Long roleId) {
		return roleService.assignRoleToUser(userId, roleId);
	}
}
