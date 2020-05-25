package uz.kyuar.services;

import org.springframework.http.ResponseEntity;
import uz.kyuar.dto.StaticQrDto;
import uz.kyuar.entities.UserEntity;

public interface StaticQrService {
    ResponseEntity<?> createStaticQr(UserEntity user, StaticQrDto dto);
}
