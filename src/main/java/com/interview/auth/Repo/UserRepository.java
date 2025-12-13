package com.interview.auth.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.interview.auth.entities.UserInfo;

@Repository
public interface UserRepository extends JpaRepository<UserInfo,String> {
	public UserInfo findByUsername(String username);
}
