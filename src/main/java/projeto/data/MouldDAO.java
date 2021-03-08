package projeto.data;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import projeto.api.dtos.entities.EventDTO;
import projeto.api.dtos.entities.MouldDTO;
import projeto.api.dtos.entities.RoleDTO;
import projeto.controller.exceptions.EntityDoesNotExistException;
import projeto.controller.exceptions.TransformToEntityException;
import projeto.core.Event;
import projeto.core.Mould;
import projeto.core.Part;
import projeto.core.Role;

import java.util.List;

public class MouldDAO extends BaseDAO<Mould, MouldDTO> {
    public MouldDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Mould toEntity(MouldDTO dto) throws TransformToEntityException {

        Mould mould;

        try {
            mould = findOrFail(dto.getCode());
        } catch ( EntityDoesNotExistException e )
        {
            throw new TransformToEntityException( e.getMessage() );
        }

        return mould;
    }

    @Override
    public MouldDTO toDTO(Mould entity)
    {
        return new MouldDTO(
                entity.getCode(),
                entity.getDescription()
        );

    }

    public List<Part> getPartsByMouldCode(String mouldCode ) throws EntityDoesNotExistException
    {
        Query<Part> query = currentSession().createNamedQuery( "Mould.getPartsByMouldCode", Part.class );
        query.setParameter( "mouldCode", mouldCode );
        List<Part> parts = query.getResultList();

        if( parts.size() == 0 )
            throw new EntityDoesNotExistException( "Não existem peças associadas ao molde '" + mouldCode + "'." );

        return parts;
    }


}
