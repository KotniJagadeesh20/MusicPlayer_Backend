package com.example.soundofmeme.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.soundofmeme.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmail(String email);

	User save(User addUser);

	@Query("SELECT u.id FROM User u WHERE u.email = :email")
	Long findUserIdByEmail(@Param("email") String email);
}
