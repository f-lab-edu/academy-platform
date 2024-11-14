package spring.academyPlatform.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import spring.academyPlatform.user.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	User findByUserName(String username);
}
