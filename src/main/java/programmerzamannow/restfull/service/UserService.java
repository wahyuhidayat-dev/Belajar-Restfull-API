package programmerzamannow.restfull.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
import programmerzamannow.restfull.entity.User;
import programmerzamannow.restfull.model.RegisterUserRequest;
import programmerzamannow.restfull.repository.UserRepository;
import programmerzamannow.restfull.security.BCrypt;

@Service
@Transactional
// Logic for user
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    public void register(RegisterUserRequest registerUserRequest) {

        validationService.validate(registerUserRequest);

        if (userRepository.existsById(registerUserRequest.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username Already register");

        }

        User user = new User();
        user.setUsername(registerUserRequest.getUsername());
        user.setPassword(BCrypt.hashpw(registerUserRequest.getPassword(), BCrypt.gensalt()));
        user.setName(registerUserRequest.getName());
        userRepository.save(user);
    }

}
