package programmerzamannow.restfull.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import programmerzamannow.restfull.entity.User;
import programmerzamannow.restfull.exception.ApiException;
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
    private Validator validator;

    public void register(RegisterUserRequest registerUserRequest) {
        Set<ConstraintViolation<RegisterUserRequest>> constraintViolations = validator.validate(registerUserRequest);
        if (constraintViolations.size() != 0) {
            throw new ConstraintViolationException(constraintViolations);
        }

        if (userRepository.existsById(registerUserRequest.getUsername())) {
            throw new ApiException("Username Already register");

        }

        User user = new User();
        user.setUsername(registerUserRequest.getUsername());
        user.setPassword(BCrypt.hashpw(registerUserRequest.getPassword(), BCrypt.gensalt()));
        user.setName(registerUserRequest.getName());
        userRepository.save(user);
    }
}
