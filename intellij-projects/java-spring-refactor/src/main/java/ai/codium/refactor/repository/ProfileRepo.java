package ai.codium.refactor.repository;

import ai.codium.refactor.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepo extends JpaRepository<UserProfile, Long> {
    UserProfile findUserProfileById(Long id);
}