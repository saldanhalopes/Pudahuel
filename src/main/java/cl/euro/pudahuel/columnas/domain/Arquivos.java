package cl.euro.pudahuel.columnas.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@Audited(withModifiedFlag = true)
public class Arquivos {

    @Version
    private Short version;

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String nome;

    @Column
    private String tipo;

    @Column
    private String descricao;

    @Column
    private Long tamanho;

    @NotAudited
    @Lob
    @Column(name = "arquivo")
    @Basic(fetch = FetchType.LAZY)
    private byte[] arquivo;

    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private LocalDateTime dataCriacao;

}
