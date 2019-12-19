package com.elastic.service;

import com.elastic.beans.domain.User;

/**
 * @Date: 2019/12/19 17:35
 **/
public interface UserInterface {

	public User searchUserById(String id);
}
