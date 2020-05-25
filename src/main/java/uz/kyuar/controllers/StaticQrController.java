package uz.kyuar.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import uz.kyuar.constants.Roles;
import uz.kyuar.dto.LoginDto;
import uz.kyuar.dto.StaticQrDto;
import uz.kyuar.dto.UserDetailDto;
import uz.kyuar.dto.UserDto;
import uz.kyuar.entities.UserEntity;
import uz.kyuar.repositories.RoleRepository;
import uz.kyuar.repositories.StaticQrRepository;
import uz.kyuar.repositories.UserRepository;
import uz.kyuar.security.jwt.JwtProvider;
import uz.kyuar.security.services.UserDetailServiceImpl;
import uz.kyuar.security.services.UserPrinciple;
import uz.kyuar.services.StaticQrService;
import uz.kyuar.services.imp.StaticQrServiceImpl;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/static-qr")
public class StaticQrController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StaticQrService staticQrService;

    private final UserDetailServiceImpl userDetailService;

    @Autowired
    public StaticQrController(UserDetailServiceImpl userDetailService, StaticQrServiceImpl staticQrService) {
        this.userDetailService = userDetailService;
        this.staticQrService = staticQrService;
    }

    @GetMapping("/post")
    public ResponseEntity<?> createStaticQr(
            @Valid @RequestBody StaticQrDto dto,
            Authentication authentication) {
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        Optional<UserEntity> user = userRepository.findById(userPrinciple.getId());
        Map<Object, Object> message = new HashMap<>();
        if (user.isPresent()) {
            return staticQrService.createStaticQr(user.get(), dto);
        } else {
            log.error("USER NOT FOUND");
            message.put("message", "USER NOT FOUND");
        }
        return new ResponseEntity<>(message, HttpStatus.OK);

    }

}
