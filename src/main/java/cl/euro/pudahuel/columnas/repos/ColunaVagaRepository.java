package cl.euro.pudahuel.columnas.repos;

import cl.euro.pudahuel.columnas.domain.ColunaStorage;
import cl.euro.pudahuel.columnas.domain.ColunaVaga;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ColunaVagaRepository extends JpaRepository<ColunaVaga, Integer> {

    Page<ColunaVaga> findAllById(Integer id, Pageable pageable);

    ColunaVaga findFirstByColunaStorage(ColunaStorage colunaStorage);

}
