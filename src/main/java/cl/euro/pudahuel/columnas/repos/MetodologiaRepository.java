package cl.euro.pudahuel.columnas.repos;

import cl.euro.pudahuel.columnas.domain.CategoriaMetodologia;
import cl.euro.pudahuel.columnas.domain.Metodologia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MetodologiaRepository extends JpaRepository<Metodologia, Integer> {

    Page<Metodologia> findAllById(Integer id, Pageable pageable);

    Metodologia findFirstByCategoriaMetodologia(CategoriaMetodologia categoriaMetodologia);

}
