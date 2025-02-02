package cl.euro.pudahuel.columnas.config;

import cl.euro.pudahuel.columnas.domain.CustomRevisionEntity;

public class EntityRevision<T> {

    private CustomRevisionEntity revisao;

    private T entidade;

    public EntityRevision(CustomRevisionEntity revision, T entity) {
        this.revisao = revision;
        this.entidade = entity;
    }

    public CustomRevisionEntity getRevisao() {
        return revisao;
    }

    public void setRevisao(CustomRevisionEntity revisao) {
        this.revisao = revisao;
    }

    public T getEntidade() {
        return entidade;
    }

    public void setEntidade(T entidade) {
        this.entidade = entidade;
    }
}
