package cl.euro.pudahuel.columnas.repos;

import cl.euro.pudahuel.columnas.domain.Grupo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface GrupoRepository extends JpaRepository<Grupo, Integer> {

    Grupo findTopByRegra(String regra);

    Page<Grupo> findAllById(Integer id, Pageable pageable);

}
