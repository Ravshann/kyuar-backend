package uz.kyuar.services.imp;

import lombok.extern.slf4j.Slf4j;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.kyuar.constants.ContentTypes;
import uz.kyuar.dto.StaticQrDto;
import uz.kyuar.entities.StaticQrEntity;
import uz.kyuar.entities.UserEntity;
import uz.kyuar.repositories.StaticQrRepository;
import uz.kyuar.services.StaticQrService;

import java.io.*;
import java.util.Date;

@Slf4j
@Service
public class StaticQrServiceImpl implements StaticQrService {

    @Autowired
    private StaticQrRepository staticQrRepository;

    @Override
    public ResponseEntity<?> createStaticQr(UserEntity user, StaticQrDto dto) {
        log.info("CREATING STATIC QR FOR USER: " + user.getUsername());

        ByteArrayOutputStream bout = null;

        switch (dto.getType()) {
            case ContentTypes.URL:
                StaticQrEntity newStaticQr = new StaticQrEntity();
                newStaticQr.setContent(dto.getContent());
                newStaticQr.setCreatedAt(new Date());
                newStaticQr.setType(ContentTypes.URL);
//                String generatedString = RandomStringUtils.randomAlphanumeric(10);
                staticQrRepository.save(newStaticQr);

                bout = QRCode.from(dto.getContent())
                        .withSize(250, 250)
                        .to(ImageType.JPG)
                        .stream();
                break;
        }


        try {
            OutputStream out = new FileOutputStream("/tmp/qr-code.jpg");
            bout.writeTo(out);
            out.flush();
            out.close();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "image/jpeg");
            headers.set("Content-Disposition", "attachment; filename=\"" + user.getUsername() + ".jpg\"");
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .body(bout.toByteArray());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
