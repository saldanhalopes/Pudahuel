package cl.euro.pudahuel.columnas.repos;

import cl.euro.pudahuel.columnas.domain.StorageTipo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StorageTipoRepository extends JpaRepository<StorageTipo, Integer> {

    Page<StorageTipo> findAllById(Integer id, Pageable pageable);

}
