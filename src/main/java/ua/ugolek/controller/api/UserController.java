package ua.ugolek.controller.api;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.ugolek.model.User;
import ua.ugolek.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("")
    public List<User> getAllUsers() {
        return userService.getAll();
    }

    @PostMapping("/lockUser")
    public HttpStatus lockUser(@Valid @RequestBody LockUserRequest lockUserRequest) {
        userService.lockManager(lockUserRequest.userId);
        return HttpStatus.OK;
    }

    @PostMapping("/unlockUser")
    public HttpStatus unlockUser(@Valid @RequestBody LockUserRequest lockUserRequest) {
        userService.unlockManager(lockUserRequest.userId);
        return HttpStatus.OK;
    }

    @Data
    private static class LockUserRequest {
        private Long userId;
    }
}
