package org.tgbot.assistant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tgbot.assistant.entity.User;

import java.util.Optional;

/**
 * Интерфейс служит мостом между кодом и таблицами
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Поиск юзера по tgId
    Optional<User> findByTgId(Long tgId);
}
