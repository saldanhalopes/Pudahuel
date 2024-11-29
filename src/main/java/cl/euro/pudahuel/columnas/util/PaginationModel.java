package cl.euro.pudahuel.columnas.util;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class PaginationModel {

    private List<PaginationStep> steps;
    private String elements;

}
