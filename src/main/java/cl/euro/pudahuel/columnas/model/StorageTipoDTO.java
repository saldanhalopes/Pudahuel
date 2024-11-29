package cl.euro.pudahuel.columnas.model;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class StorageTipoDTO {

    private Integer id;

    @Size(max = 255)
    private String tipo;

    @Size(max = 255)
    private String condicoesArmazenamento;

    private Short version;

}
