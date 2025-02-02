package cl.euro.pudahuel.columnas.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@Audited(withModifiedFlag = true)
public class ColunaLog {

    @Version
    private Short version;

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private LocalDateTime dataIncio;

    @Column
    private LocalDateTime dataFim;

    @Column
    private String sentido;

    @Column
    private String precoluna;

    @Column
    private String prefiltro;

    @Column
    private Integer injecoes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coluna_util_id")
    private ColunaUtil colunaUtil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_inicio_id")
    private Usuario usuarioInicio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_fim_id")
    private Usuario usuarioFim;

    @Column
    private String campanha;

    @Column
    private String analise;

    @Column
    private String equipamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "anexo_id")
    private Arquivos anexo;

}
