package uz.kyuar.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.kyuar.entities.StaticQrEntity;

@Repository
public interface StaticQrRepository extends JpaRepository<StaticQrEntity, Long> {
}
