package com.practice.moonlightHotel.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "TB_ROLE")
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "NAME")
	private String name;

	@JsonIgnore
	@ManyToMany(mappedBy = "roles")
	private Collection<User> users = new HashSet<>();

	public Role() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Role(String name) {
		this.name = name;
	}

	public void assignRoleToUser(User user) {
		user.getRoles().add(this);
		this.getUsers().add(user);
	}

	public void removeUserFromRole(User user) {
		user.getRoles().remove(this);
		this.getUsers().remove(user);

	}

	public void removeAllUsersFromRole() {
		if (this.getUsers() != null) {
			List<User> roleUsers = this.getUsers().stream().collect(Collectors.toList());
			roleUsers.forEach(this::removeUserFromRole);
		}
	}

	public String getName() {
		return name != null ? name : "";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Collection<User> getUsers() {
		return users;
	}

	public void setUsers(Collection<User> users) {
		this.users = users;
	}

	public void setName(String name) {
		this.name = name;
	}

}
