package cl.euro.pudahuel.columnas.repos;

import cl.euro.pudahuel.columnas.domain.Coluna;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface ColunaRepository extends JpaRepository<Coluna, Integer> {

    Page<Coluna> findAllById(Integer id, Pageable pageable);

    @Query("Select col FROM Coluna col " +
            "LEFT JOIN FETCH col.tipoColuna tipo " +
            "LEFT JOIN FETCH col.fabricanteColuna fab " +
            "LEFT JOIN FETCH col.marcaColuna marca " +
            "LEFT JOIN FETCH col.faseColuna fase " +
            "LEFT JOIN FETCH col.comprimentoColuna compr " +
            "LEFT JOIN FETCH col.diametroColuna diam " +
            "LEFT JOIN FETCH col.particulaColuna part " +
            "WHERE CONCAT(col.id, ' ', tipo.configuracao, ' ', fab.configuracao, ' ', marca.configuracao, ' ', " +
            "fase.configuracao, ' ',compr.configuracao, ' ',diam.configuracao, ' ',part.configuracao) LIKE %?1%")
    Page<Coluna> findAllByKeyword(String keyword, Pageable pageable);


    @Query("Select col FROM Coluna col " +
            "LEFT JOIN FETCH col.tipoColuna tipo " +
            "LEFT JOIN FETCH col.fabricanteColuna fab " +
            "LEFT JOIN FETCH col.marcaColuna marca " +
            "LEFT JOIN FETCH col.faseColuna fase " +
            "LEFT JOIN FETCH col.comprimentoColuna compr " +
            "LEFT JOIN FETCH col.diametroColuna diam " +
            "LEFT JOIN FETCH col.particulaColuna part ")
    Page<Coluna> findAllOfColuna(Pageable pageable);

}
