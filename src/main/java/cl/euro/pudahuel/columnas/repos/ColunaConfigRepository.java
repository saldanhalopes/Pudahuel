package cl.euro.pudahuel.columnas.repos;

import cl.euro.pudahuel.columnas.domain.ColunaConfig;
import cl.euro.pudahuel.columnas.enums.ColunaConfigParametro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ColunaConfigRepository extends JpaRepository<ColunaConfig, Integer> {

    Page<ColunaConfig> findAllById(Integer id, Pageable pageable);

    @Query("Select conf FROM ColunaConfig conf WHERE conf.parametro = :parametro")
    List<ColunaConfig> findByPar(@Param("parametro") ColunaConfigParametro parametro);

}
