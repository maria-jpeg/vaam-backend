/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projeto.resources;

import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import projeto.api.dtos.DTO;
import projeto.controller.BaseBean;
import projeto.controller.exceptions.EntityDoesNotExistException;
import projeto.controller.exceptions.EntityExistsException;
import projeto.controller.exceptions.TransformToEntityException;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.Serializable;


/**
 * 
 * @param <E> entity type
 * @param <D> DTO type
 * @param <P> Primary Key type
 */
@ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer ") })
public abstract class CRUDService<E extends Serializable, D extends DTO, P>
{
    private final BaseBean<E, D> bean;

    CRUDService(BaseBean<E, D> bean) {
        this.bean = bean;
    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @UnitOfWork
    @RolesAllowed({"Operador","Gestor","Administrador"})
    public Response getAll()
    {
        try {
            return Response.ok( bean.getDao().toDTOs( bean.getAll() ) )
                    .build();
        } catch (Exception ex)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR) .type( MediaType.TEXT_PLAIN )
                    .entity( ex.getMessage() ) .build();
        }
    }
   
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @Path("{id}")
    @UnitOfWork
    @RolesAllowed({"Operador","Gestor","Administrador"})
    public Response getById( @PathParam("id") P id)
    {
        try {
            E entity = bean.findOrFail( id );
            return Response.status(Response.Status.CREATED)
                    .entity( bean.getDao().toDTO(entity) ) .build();

        }
         catch (EntityDoesNotExistException ex)
         {
            return Response.status(Response.Status.NOT_FOUND) .type( MediaType.TEXT_PLAIN )
                    .entity( ex.getMessage() ) .build();
        }      
               
    }
    
    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    @UnitOfWork
    @RolesAllowed({"Operador","Gestor","Administrador"})
    public Response create( @Valid D dto )
    {

        try {
            D created = bean.create( dto );
            return Response.ok( created ).build();  
            
        } catch (EntityExistsException | TransformToEntityException | EntityDoesNotExistException ex) {
           return Response.status( Response.Status.CONFLICT ) .type( MediaType.TEXT_PLAIN )
                   .entity( bean.getDao().getEntityClass().getSimpleName() + " already exists!" ) .build();
        }

    }
    
    @PUT
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    @UnitOfWork
    @RolesAllowed({"Operador","Gestor","Administrador"})
    public Response update( @Valid D dto )
    {
        try {
            D updated = bean.update( dto );
            return Response.ok( updated ).build();  
            
        } 
         catch (EntityDoesNotExistException ex)
         {
            return Response.status(Response.Status.NOT_FOUND) .type( MediaType.TEXT_PLAIN )
                    .entity( bean.getDao().getEntityClass().getSimpleName() + " not found!" ) .build();
        }
        catch (TransformToEntityException ex)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR) .type( MediaType.TEXT_PLAIN )
                    .entity( ex.getMessage() ) .build();
        }
        
    }
    
    @DELETE
    @Produces({ MediaType.APPLICATION_JSON })
    @Path("{id}")
    @UnitOfWork
    @RolesAllowed({"Operador","Gestor","Administrador"})
    public Response delete(@PathParam("id") P id)
    {
        try
        {
            E removed = bean.remove(id);
            return Response.ok( bean.getDao().toDTO( removed ) ).build();
            
        }
         catch (EntityDoesNotExistException ex) {

            return Response.status(Response.Status.NOT_FOUND).entity( ex.getMessage() ) .build();
        }
  
    }
   
    

}
