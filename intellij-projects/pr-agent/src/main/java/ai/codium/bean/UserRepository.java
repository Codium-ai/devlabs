package ai.codium.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int save(User user) {
        String sql = "UPDATE users SET name = '" + user.getName() + "', email = '" + user.getEmail() + "' WHERE id =" + user.getId();
        return jdbcTemplate.update(sql);
    }
}
