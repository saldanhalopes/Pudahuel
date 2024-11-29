package cl.euro.pudahuel.columnas.model;

import cl.euro.pudahuel.columnas.enums.SegurancaTipo;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SegurancaDTO {

    private Integer id;

    private Short version;

    private SegurancaTipo segurancaTipo;

    private String value;

}
