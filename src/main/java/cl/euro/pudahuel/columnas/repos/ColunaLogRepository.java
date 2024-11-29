package cl.euro.pudahuel.columnas.repos;

import cl.euro.pudahuel.columnas.domain.Arquivos;
import cl.euro.pudahuel.columnas.domain.ColunaLog;
import cl.euro.pudahuel.columnas.domain.ColunaUtil;
import cl.euro.pudahuel.columnas.domain.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ColunaLogRepository extends JpaRepository<ColunaLog, Integer> {

    Page<ColunaLog> findAllById(Integer id, Pageable pageable);

    ColunaLog findFirstByUsuarioInicio(Usuario usuario);

    ColunaLog findFirstByUsuarioFim(Usuario usuario);

    ColunaLog findFirstByAnexo(Arquivos arquivos);

    ColunaLog findFirstByColunaUtil(ColunaUtil colunaUtil);

}
