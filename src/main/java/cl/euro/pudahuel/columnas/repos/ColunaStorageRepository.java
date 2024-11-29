package cl.euro.pudahuel.columnas.repos;

import cl.euro.pudahuel.columnas.domain.ColunaStorage;
import cl.euro.pudahuel.columnas.domain.ColunaStorageTipo;
import cl.euro.pudahuel.columnas.domain.Setor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ColunaStorageRepository extends JpaRepository<ColunaStorage, Integer> {

    Page<ColunaStorage> findAllById(Integer id, Pageable pageable);

    ColunaStorage findFirstBySetor(Setor setor);

    ColunaStorage findFirstByTipo(ColunaStorageTipo colunaStorageTipo);

}
