package cl.euro.pudahuel.columnas.enums;

public enum Color {

    CINZA("bg-light"),
    AZUL("bg-primary"),
    AMARELO("bg-warning"),
    VERDE("bg-success"),
    PRETO("bg-dark"),
    VERMELHO("bg-danger");

    private final String cor;

    Color(String descricao) {
        this.cor = descricao;
    }

    public String getCor() {
        return cor;
    }

}





