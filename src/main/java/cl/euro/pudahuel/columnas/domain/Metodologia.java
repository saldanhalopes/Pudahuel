package cl.euro.pudahuel.columnas.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Entity
@Getter
@Setter
@Audited(withModifiedFlag = true)
public class Metodologia {

    @Version
    private Short version;

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String codigo;

    @Column
    private String metodo;

    @Column
    private String obs;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_metodologia_id")
    private CategoriaMetodologia categoriaMetodologia;


}
