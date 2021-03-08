package projeto.controller;

import projeto.api.dtos.DTO;
import projeto.controller.exceptions.EntityDoesNotExistException;
import projeto.controller.exceptions.EntityExistsException;
import projeto.controller.exceptions.TransformToEntityException;
import projeto.data.BaseDAO;

import java.io.Serializable;
import java.util.List;

public abstract class BaseBean<E extends Serializable, D extends DTO>
{
    private final BaseDAO<E,D> dao;

    BaseBean(BaseDAO<E,D> dao) {
        this.dao = dao;
    }

    // DAO functions

    public List<E> getAll() { return dao.getAll(); }

    public D create(D dto) throws EntityExistsException, TransformToEntityException, EntityDoesNotExistException { return dao.create( dto ); }

    public D update( D dto) throws EntityDoesNotExistException, TransformToEntityException { return dao.update( dto ); }

    public E remove( Object primaryKey) throws EntityDoesNotExistException { return dao.remove( primaryKey ); }

    public E create(E entity) throws EntityExistsException { return dao.create( entity ); }

    public E update(E entity) throws EntityDoesNotExistException { return dao.update( entity ); }

    public boolean exists(E entity) { return dao.exists( entity );
    }
    public boolean exists(Object primaryKey) { return dao.exists( primaryKey ); }

    public E find(Object primaryKey) { return dao.find( primaryKey ); }

    public E findOrFail( Object primaryKey ) throws EntityDoesNotExistException { return dao.findOrFail( primaryKey ); }

    public BaseDAO<E, D> getDao() { return dao; }

    // ===============

}
