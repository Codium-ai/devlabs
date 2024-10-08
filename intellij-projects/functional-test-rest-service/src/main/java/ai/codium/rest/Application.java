package ai.codium.rest;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

interface ProfileRepo extends JpaRepository<UserProfile, Long> {
    UserProfile findUserProfileById(Long id);
}

@RestController
@RequestMapping("/profile")
@Repository
@SpringBootApplication
public class Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    private ProfileRepo profileRepo;

    public Application(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfile> getProfile(@PathVariable Long id) {
        LOGGER.info("Asking for profile {}", id);
        return ResponseEntity.ok(this.profileRepo.findUserProfileById(id));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserProfile>> profiles() {
        String sql = "SELECT id, name, age, email FROM USER_PROFILE";
        List<UserProfile> users = jdbcTemplate.query(sql, (resultSet, rowNum) -> {
            UserProfile u = new UserProfile();
            u.setId(resultSet.getLong("id"));
            u.setName(resultSet.getString("name"));
            u.setAge(resultSet.getInt("age"));
            u.setEmail(resultSet.getString("email"));
            return u;
        });
        return ResponseEntity.ok(users);
    }


    @PostMapping("/")
    public UserProfile profile(@RequestBody UserProfile u) {
        return profileRepo.save(u);
    }

    @Bean
    ApplicationListener<ApplicationReadyEvent> applicationReadyEventApplicationListener(ProfileRepo repo) {
        return new ApplicationListener<ApplicationReadyEvent>() {
            @Override
            public void onApplicationEvent(ApplicationReadyEvent event) {
                UserProfile profile = new UserProfile();
                profile.setId(1234L);
                profile.setName("John Smith");
                profile.setEmail("d@david.com");
                profile.setAge(26);
                System.out.println("Inserting a user " + profile);
                repo.save(profile);
            }
        };
    }
}

@Entity
class UserProfile {

    @Id
    private Long id;
    private String name;
    private String email;
    private int age;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

@Configuration
class Config {
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("functional-testing")
                .pathsToMatch("/**")
                .build();
    }
}
