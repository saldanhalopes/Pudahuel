package cl.euro.pudahuel.columnas.repos;

import cl.euro.pudahuel.columnas.domain.Fabricante;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FabricanteRepository extends JpaRepository<Fabricante, Integer> {

    Page<Fabricante> findAllById(Integer id, Pageable pageable);

}
