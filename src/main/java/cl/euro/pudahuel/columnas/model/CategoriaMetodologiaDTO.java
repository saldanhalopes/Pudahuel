package cl.euro.pudahuel.columnas.model;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CategoriaMetodologiaDTO {

    private Integer id;

    @Size(max = 255)
    private String categoria;

    private Short version;

}
