package cl.euro.pudahuel.columnas.model;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ColunaDTO {

    private Integer id;

    @Size(max = 255)
    private String codigo;

    @Size(max = 255)
    private String partNumber;

    @Size(max = 255)
    private String obs;

    private Integer tipoColuna;
    private String tipoColunaName;

    private Integer fabricanteColuna;
    private String fabricanteColunaName;

    private Integer marcaColuna;
    private String marcaColunaName;

    private Integer faseColuna;
    private String faseColunaName;

    private Integer comprimentoColuna;
    private String comprimentoColunaName;

    private Integer diametroColuna;
    private String diametroColunaName;

    private Integer particulaColuna;
    private String particulaColunaName;

    private Short version;

}
