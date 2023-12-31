package com.practice.moonlightHotel.service;

import java.util.List;

import com.practice.moonlightHotel.model.User;

public interface IUserService {
	User registerUser(User user);

	List<User> getUsers();

	void deleteUser(String email);

	User getUser(String email);
}
