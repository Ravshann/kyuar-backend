package uz.kyuar.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "static_qr")
public class StaticQrEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String content;

    private Integer type;

    private String location;


    @JsonFormat(timezone = "Asia/Tashkent")
    private Date createdAt;

}
