package projeto.data;

import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import projeto.api.dtos.DTO;
import projeto.controller.exceptions.EntityDoesNotExistException;
import projeto.controller.exceptions.TransformToEntityException;

import javax.persistence.*;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public abstract class BaseDAO<E extends Serializable, D extends DTO> extends AbstractDAO<E> {

    protected Class<E> entityClass;
    protected Class<D> dtoClass;

    private static final String SQL_ALL = "SELECT entity FROM %s entity";
    private static final String SQL_EXISTS = "SELECT COUNT(e) FROM %1$s e WHERE e.%2$s = :%2$s";


    public BaseDAO(SessionFactory sessionFactory)
    {
        super(sessionFactory);
        entityClass = getGenericClass(0);
        dtoClass = getGenericClass(1);
    }


    private <T> Class<T> getGenericClass(int index) {
        ParameterizedType genericType = (ParameterizedType) this.getClass().getGenericSuperclass();
        return (Class<T>) genericType.getActualTypeArguments()[index];
    }


    public Class<E> getEntityClass() { return entityClass; }
    public Class<D> getDtoClass() { return dtoClass; }

    public List<E> getAll() {
        String className = entityClass.getSimpleName();
        String namedQuery = className + ".all";

        if (containsNamedQuery(namedQuery)) {
            //return toDTOs(createNamedQuery(namedQuery).getResultList());
            return createNamedQuery(namedQuery).getResultList();
        }

        String all = String.format(SQL_ALL, className);
        //return toDTOs(query(all).getResultList());
        return createQuery(all).getResultList();
    }

    public D create(D dto) throws EntityExistsException, TransformToEntityException, EntityDoesNotExistException {
        E entity = toEntity(dto);
        entity = create(entity);
        return toDTO(entity);
    }

    public D update( D dto) throws EntityDoesNotExistException, TransformToEntityException {
        E entity = toEntity(dto);
        entity = update(entity);
        return toDTO(entity);
    }

    public E remove( Object primaryKey) throws EntityDoesNotExistException {
        E entity = findOrFail(primaryKey);

        currentSession().refresh(entity);
        currentSession().remove(entity);
        return entity;

    }
    // protected
    public E create(E entity) throws EntityExistsException {

        if ( exists(entity) ) {
            throw new EntityExistsException("A entidade já existe.");
        }

        currentSession().persist(entity);

        return entity;
    }

    // protected
    public E update(E entity) throws EntityDoesNotExistException {
        if (! exists(entity)) {
            throw new EntityDoesNotExistException(entity.getClass().getSimpleName() + " não existe");
        }

        currentSession().merge(entity);

        return entity;
    }

    public boolean exists(E entity) {
        Field primaryKey = getPrimaryKeyField();
        String pkName = primaryKey.getName();

        Query query = currentSession().createQuery(buildQueryExists(), Long.class);
        query = query.setParameter(pkName, getValue(entity, primaryKey));

        Long num = (Long)query.getSingleResult();

        return num > 0;
    }

    public boolean exists(Object primaryKey) {
        String pkName = getPrimaryKeyField().getName();

        Query query = currentSession().createQuery(buildQueryExists(), Long.class);
        query = query.setParameter(pkName, primaryKey);

        return (Long) query.getSingleResult() > 0;
    }

    public E find(Object primaryKey) {
        return currentSession().find(entityClass, primaryKey);
    }

    public E findOrFail( Object primaryKey ) throws EntityDoesNotExistException
    {
        if( primaryKey == null )
            throw new EntityDoesNotExistException( entityClass.getSimpleName() + " '" + primaryKey + "' não encontrado." );

        E entity = null;
        try
        {
            entity = currentSession().find( entityClass, primaryKey );
        }
        catch (Exception ex) {
            throw new EntityDoesNotExistException( entityClass.getSimpleName() + " '" + primaryKey + "' não encontrado." );
        }

        if( entity == null )
            throw new EntityDoesNotExistException( entityClass.getSimpleName() + " '" + primaryKey + "' não encontrado." );


        return entity;

    }

    private String camelToPascalCase(String value) {
        return String.valueOf(value.charAt(0)).toUpperCase() + value.substring(1);
    }

    private Method getMethod(String methodName, Class<?> ...args) throws NoSuchMethodException {
        try {
            return entityClass.getDeclaredMethod(methodName, args);
        } catch (NoSuchMethodException | SecurityException e) {
            return entityClass.getMethod(methodName, args);
        }
    }

    private <T> T getValue(E entity, Field field) {
        try {
            if (field.isAccessible()) {
                return (T) field.get(entity);
            }

            String fieldName = field.getName();
            String methodName = "get" + camelToPascalCase(fieldName);

            Method getter = getMethod(methodName);

            return (T) getter.invoke(entity);
        } catch (NullPointerException | IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
            return null;
        }
    }

    /*
    public D createOrUpdate(D dto) throws EntityDoesNotExistException, EntityExistsException, TransformToEntityException {
        return toDTO(createOrUpdate(toEntity(dto)));
    }

    protected E createOrUpdate(E entity) throws EntityDoesNotExistException, EntityExistsException {
        return exists(entity) ? update(entity) : create(entity);
    }
    */

    private <A extends Annotation> Stream<A> getAnnotations(Class<A> annotationClass) {
        return Arrays.stream(entityClass.getAnnotationsByType(annotationClass));
    }

    private Stream<NamedQuery> getNamedQueries() {
        // 3. Get the @NamedQuery annotations of E
        Stream<NamedQuery> nq = getAnnotations(NamedQuery.class);

        // 4. check also for @NamedQueries annotations.
        Stream<NamedQuery> nq2 = getAnnotations(NamedQueries.class).map(NamedQueries::value).flatMap(Arrays::stream);

        return Stream.concat(nq, nq2);
    }

    private boolean containsNamedQuery(String namedQuery) {
        Stream<NamedQuery> namedQueries = getNamedQueries();
        return namedQueries.map(NamedQuery::name).anyMatch(namedQuery::equals);
    }

    private String buildQueryExists() {
        String entityName = entityClass.getSimpleName();
        String pkName = getPrimaryKeyField().getName();

        return String.format(SQL_EXISTS, entityName, pkName);
    }

    public abstract E toEntity(D dto) throws TransformToEntityException, EntityDoesNotExistException;

    public List<E> toEntities(List<D> dtos) throws TransformToEntityException, EntityDoesNotExistException {   if( dtos == null )
        return null;

        // return dtos.parallelStream().map(d -> toEntity(d) ).collect(Collectors.toList());

        List<E> entities = new ArrayList<>();
        for(D d : dtos)
        {
            entities.add( toEntity(d) );
        }

        return entities;

    }

    public abstract D toDTO(E entity);

    public List<D> toDTOs(List<E> entities)
    {
        if( entities == null )
            return null;

        // return entities.parallelStream().map(e -> toDTO(e) ).collect(Collectors.toList());

        List<D> dtos = new ArrayList<>();
        for(E e : entities)
        {
            dtos.add( toDTO(e) );
        }

        return dtos;

    }

    private <T> List<Field> getAllDeclaredFields(Class<T> tClass) {
        List<Field> fields = new LinkedList<>();

        Class<? super T> parentClass = tClass.getSuperclass();

        if (parentClass != null) {
            fields.addAll(getAllDeclaredFields(parentClass));
        }

        fields.addAll(Arrays.asList(tClass.getDeclaredFields()));

        return fields;
    }

    private Field getPrimaryKeyField() {
        List<Field> fields = getAllDeclaredFields(entityClass);
        Predicate<Field> isPrimaryKey = field -> field.getAnnotationsByType(Id.class).length > 0;

        return fields.stream().filter(isPrimaryKey).findFirst().get();
    }

    public Query createNamedQuery(String name) {
        return currentSession().createNamedQuery(name, entityClass);
    }

    public Query createQuery(String qlString) {
        return currentSession().createQuery(qlString, entityClass);
    }
}
