package cl.euro.pudahuel.columnas.repos;

import cl.euro.pudahuel.columnas.domain.Grandeza;
import cl.euro.pudahuel.columnas.domain.UnidadeMedida;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UnidadeMedidaRepository extends JpaRepository<UnidadeMedida, Integer> {

    Page<UnidadeMedida> findAllById(Integer id, Pageable pageable);

    UnidadeMedida findFirstByGrandeza(Grandeza grandeza);

}
