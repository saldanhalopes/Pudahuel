package cl.euro.pudahuel.columnas.repos;

import cl.euro.pudahuel.columnas.domain.Sistema;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SistemaRepository extends JpaRepository<Sistema, Integer> {

    Sistema findBySistemaNome(String nomeSistema);
}
