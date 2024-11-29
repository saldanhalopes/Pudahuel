package cl.euro.pudahuel.columnas.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;
import java.util.Set;


@Entity
@Getter
@Setter
@Audited(withModifiedFlag = true)
public class ColunaUtil {

    @Version
    private Short version;

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String codigoColuna;

    @Column
    private String serialNumber;

    @Column
    private LocalDateTime dataAtivacao;

    @Column
    private LocalDateTime dataVerificacao;

    @Column
    private LocalDateTime dataDescarte;

    @Column
    private Boolean estoque;

    @Column
    private String obs;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coluna_id")
    private Coluna coluna;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "setor_id")
    private Setor setor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "metodologia_id")
    private Metodologia metodologia;

    @Column
    private String analise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coluna_vaga_id")
    private ColunaVaga colunaVaga;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "certificado_id")
    private Arquivos certificado;

    @ManyToMany
    @JoinTable(
            name = "arquivos_coluna_util",
            joinColumns = @JoinColumn(name = "coluna_util_id"),
            inverseJoinColumns = @JoinColumn(name = "arquivos_id")
    )
    private Set<Arquivos> anexos;

}
