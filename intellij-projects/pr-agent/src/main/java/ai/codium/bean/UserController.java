package ai.codium.bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public String createUser(@RequestBody User user) {
        int result = userService.saveUser(user);
        return result == 1 ? "User created successfully" : "Error creating user";
    }
}
