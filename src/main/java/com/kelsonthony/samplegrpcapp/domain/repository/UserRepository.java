package com.kelsonthony.samplegrpcapp.domain.repository;

import com.kelsonthony.samplegrpcapp.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
