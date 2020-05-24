package uz.kyuar.controllers;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import uz.kyuar.constants.Roles;
import uz.kyuar.dto.LoginDto;
import uz.kyuar.dto.UserDetailDto;
import uz.kyuar.dto.UserDto;
import uz.kyuar.entities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import uz.kyuar.repositories.RoleRepository;
import uz.kyuar.repositories.UserRepository;
import uz.kyuar.security.jwt.JwtProvider;
import uz.kyuar.security.services.UserDetailServiceImpl;


import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private JwtProvider tokenGenerator;

    @Autowired
    private PasswordEncoder encoder;

    private final UserDetailServiceImpl userDetailService;


    @Autowired
    public AuthController(UserDetailServiceImpl userDetailService) {
        this.userDetailService = userDetailService;
    }

    @GetMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto) {

        Map<Object, Object> message = new HashMap<>();

        Optional<UserEntity> user = userRepository.findByUsername(loginDto.getUsername());
        if (user.isPresent() && encoder.matches(loginDto.getPassword(), user.get().getPassword())) {
            Authentication authentication;
            UserDetails userDetails;
            userDetails = userDetailService.loadUserByUsername(loginDto.getUsername());
            authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            UserDetailDto userDetail = UserDetailDto.builder()
                    .username(loginDto.getUsername())
                    .token(tokenGenerator.generateJwtToken(authentication))
                    .build();

            return new ResponseEntity<>(userDetail, HttpStatus.OK);
        } else {
            message.put("message", "user not found this by username and password or user object in proper format");
        }

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserDto userDto) {

        Map<Object, Object> message = new HashMap<>();

        if (userRepository.existsByUsernameAndPassword(userDto.getUsername(), userDto.getPassword())) {
            message.put("message", "such user exists");
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        } else {
            UserEntity newUser = new UserEntity();
            newUser.setFullname(userDto.getFullname());
            newUser.setUsername(userDto.getUsername());
            newUser.setPassword(encoder.encode(userDto.getPassword()));
            newUser.setCreatedAt(new Date());
            newUser.getRoleEntities().add(roleRepository.findById(Roles.ROLE_OWNNER).get());
            userRepository.save(newUser);

            message.put("message", "user registered successfully");
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
    }
}
