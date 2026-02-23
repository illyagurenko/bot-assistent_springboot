package org.tgbot.assistant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tgbot.assistant.entity.DotaProfiles;
import org.tgbot.assistant.entity.User;
import java.util.Optional;

@Repository
public interface DotaProfilesRepository extends JpaRepository<DotaProfiles, Long> {
    Optional<DotaProfiles> findByUser(User user);
}
