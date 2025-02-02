package cl.euro.pudahuel.columnas.repos;

import cl.euro.pudahuel.columnas.domain.Storage;
import cl.euro.pudahuel.columnas.domain.StorageEndereco;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface StorageEnderecoRepository extends JpaRepository<StorageEndereco, Integer> {

    Page<StorageEndereco> findAllById(Integer id, Pageable pageable);

    @Query("Select stEndereco FROM StorageEndereco stEndereco " +
            "LEFT JOIN FETCH stEndereco.storage store " +
            "LEFT JOIN FETCH store.tipo tipo " +
            "LEFT JOIN FETCH store.setor setor ")
    List<StorageEndereco> findListOfStorageEnderecos(Sort sort);

    @Query("Select stEndereco FROM StorageEndereco stEndereco " +
            "LEFT JOIN FETCH stEndereco.storage store " +
            "WHERE stEndereco.id = :id")
    Optional<StorageEndereco> findStorageEnderecoById(@Param("id") Integer id);

    @Query("Select stEndereco FROM StorageEndereco stEndereco LEFT JOIN FETCH stEndereco.storage store")
    Page<StorageEndereco> findAllOfStorageEndereco(Pageable pageable);

    StorageEndereco findFirstByStorage(Storage storage);

}
