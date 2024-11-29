package cl.euro.pudahuel.columnas.repos;

import cl.euro.pudahuel.columnas.domain.Departamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DepartamentoRepository extends JpaRepository<Departamento, Integer> {

    Page<Departamento> findAllById(Integer id, Pageable pageable);

}
