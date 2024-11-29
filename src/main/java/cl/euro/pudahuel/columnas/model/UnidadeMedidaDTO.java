package cl.euro.pudahuel.columnas.model;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UnidadeMedidaDTO {

    private Integer id;

    @Size(max = 255)
    private String unidade;

    private Integer grandeza;

    private Short version;

}
