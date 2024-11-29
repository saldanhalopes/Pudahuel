package cl.euro.pudahuel.columnas.repos;

import cl.euro.pudahuel.columnas.domain.Arquivos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ArquivosRepository extends JpaRepository<Arquivos, Integer> {

    Page<Arquivos> findAllById(Integer id, Pageable pageable);


}
