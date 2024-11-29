package cl.euro.pudahuel.columnas.repos;

import cl.euro.pudahuel.columnas.domain.Departamento;
import cl.euro.pudahuel.columnas.domain.Setor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SetorRepository extends JpaRepository<Setor, Integer> {

    Page<Setor> findAllById(Integer id, Pageable pageable);

    Setor findFirstByDepartamento(Departamento departamento);

}
