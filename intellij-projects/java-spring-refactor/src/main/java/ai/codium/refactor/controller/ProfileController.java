package ai.codium.refactor.controller;

import ai.codium.refactor.entity.UserProfile;
import ai.codium.refactor.service.ProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileController.class);
    private final ProfileService profileService;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ProfileController(ProfileService profileService, JdbcTemplate jdbcTemplate) {
        this.profileService = profileService;
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfile> getProfile(@PathVariable Long id) {
        LOGGER.info("Asking for profile {}", id);
        return ResponseEntity.ok(this.profileService.findUserProfileById(id));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserProfile>> profiles() {
        String sql = "SELECT id, name FROM USER_PROFILE";
        List<UserProfile> users = jdbcTemplate.query(sql, (resultSet, rowNum) -> {
            UserProfile u = new UserProfile();
            u.setId(resultSet.getLong("id"));
            u.setName(resultSet.getString("name"));
            return u;
        });
        return ResponseEntity.ok(users);
    }

    @PostMapping("/")
    public UserProfile profile(@RequestBody UserProfile u) {
        return profileService.save(u);
    }
}