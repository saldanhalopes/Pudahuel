package cl.euro.pudahuel.columnas.repos;

import cl.euro.pudahuel.columnas.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ColunaUtilRepository extends JpaRepository<ColunaUtil, Integer> {

    Page<ColunaUtil> findAllById(Integer id, Pageable pageable);

    ColunaUtil findFirstBySetor(Setor setor);


    List<ColunaUtil> findAllByAnexos(Arquivos arquivos);

    ColunaUtil findFirstByCertificado(Arquivos arquivos);

    ColunaUtil findFirstByAnexos(Arquivos arquivos);

    ColunaUtil findFirstByColunaVaga(ColunaVaga colunaVaga);

    ColunaUtil findFirstByColuna(Coluna coluna);

}
