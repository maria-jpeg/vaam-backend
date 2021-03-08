package projeto.data;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import projeto.api.dtos.entities.PartDTO;
import projeto.api.dtos.entities.TagDTO;
import projeto.controller.exceptions.EntityDoesNotExistException;
import projeto.controller.exceptions.TransformToEntityException;
import projeto.core.Part;
import projeto.core.Tag;

import javax.ws.rs.NotAllowedException;
import java.util.List;

public class TagDAO extends BaseDAO<Tag, TagDTO> {
    public TagDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Tag toEntity(TagDTO dto) throws TransformToEntityException {

        Tag tag;

        try {
            tag = findOrFail(dto.getRfid());
        } catch ( EntityDoesNotExistException e )
        {
            throw new TransformToEntityException( e.getMessage() );
        }

        return tag;
    }

    @Override
    public TagDTO toDTO(Tag entity)
    {
        return new TagDTO(
                entity.getRfid(),
                entity.getIsAvailable(),
                entity.getIsUser()
        );
    }

    public List<Tag> getAvailableOrUserTags()
    {
        Query<Tag> query = currentSession().createNamedQuery( "Tag.getTagsAvailableOrUser", Tag.class );
        List<Tag> tags = query.getResultList();
        return tags;
    }

}
