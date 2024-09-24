package com.jar.kirana.repositories;

import com.jar.kirana.entities.UserToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserTokenRepository extends MongoRepository<UserToken, String> {
    Optional<UserToken> findByToken(String token);
    List<UserToken> findByUsername(String username);
}
