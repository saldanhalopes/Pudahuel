package cl.euro.pudahuel.columnas.repos;

import cl.euro.pudahuel.columnas.domain.Grandeza;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface GrandezaRepository extends JpaRepository<Grandeza, Integer> {

    Page<Grandeza> findAllById(Integer id, Pageable pageable);

}
