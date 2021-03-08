package projeto.data;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import projeto.api.dtos.entities.MouldDTO;
import projeto.api.dtos.entities.PartDTO;
import projeto.api.dtos.entities.RoleDTO;
import projeto.controller.exceptions.EntityDoesNotExistException;
import projeto.controller.exceptions.TransformToEntityException;
import projeto.core.*;

import java.util.List;

public class PartDAO extends BaseDAO<Part, PartDTO> {
    public PartDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Part toEntity(PartDTO dto) throws TransformToEntityException {

        Part part;

        try {
            part = findOrFail(dto.getCode());
        } catch ( EntityDoesNotExistException e )
        {
            throw new TransformToEntityException( e.getMessage() );
        }

        return part;
    }

    @Override
    public PartDTO toDTO(Part entity)
    {
        String tagRfid = null;
        Tag aux = entity.getTag();
        if(aux!= null){
             tagRfid = aux.getRfid();
        }


        return new PartDTO(
                entity.getCode(),
                entity.getDescription(),
                tagRfid
        );

    }

    public List<Part> getPartsWithTag(String tagRfid )
    {
        Query<Part> query = currentSession().createNamedQuery( "Part.getPartsWithTag", Part.class );
        query.setParameter( "tagRfid", tagRfid );
        List<Part> parts = query.getResultList();

        return parts;
    }

    public String getPartMouldCode(String partCode )
    {
        Query<String> query = currentSession().createNamedQuery( "Part.getPartMouldCode", String.class );
        query.setParameter( "partCode", partCode );
        String mouldCode = query.getSingleResult();

        System.out.println(mouldCode);

        return mouldCode;
    }
}
