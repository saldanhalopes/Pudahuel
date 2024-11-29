package cl.euro.pudahuel.columnas.repos;

import cl.euro.pudahuel.columnas.domain.Seguranca;
import cl.euro.pudahuel.columnas.enums.SegurancaTipo;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SegurancaRepository extends JpaRepository<Seguranca, Integer> {

    Seguranca findBySegurancaTipo(SegurancaTipo value);
}
