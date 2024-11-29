package cl.euro.pudahuel.columnas.model;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;


@Getter
@Setter
public class ArquivosDTO {

    private Integer id;

    @Size(max = 255, message = "Nome do arquivo invalido")
    private String nome;

    private String tipo;

    private String descricao;

    @Size(max = 10485760, message = "Tamanho do arquivo invalido")
    private Long tamanho;

    private byte[] arquivo;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataCriacao;

    private Short version;

}
