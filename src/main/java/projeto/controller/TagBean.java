package projeto.controller;

import projeto.api.dtos.entities.PartDTO;
import projeto.api.dtos.entities.TagDTO;
import projeto.controller.exceptions.EntityDoesNotExistException;
import projeto.core.Part;
import projeto.core.Tag;
import projeto.data.PartDAO;
import projeto.data.TagDAO;

import java.util.List;
import java.util.stream.Collectors;

public class TagBean extends BaseBean<Tag, TagDTO> {
    final private TagDAO tagDAO;

    public TagBean(TagDAO tagDAO) {
        super( tagDAO );
        this.tagDAO = tagDAO;
    }

    public List<TagDTO> toDTOsList(List<Tag> tags) {
        return tags.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public TagDTO toDTO(Tag entity){
        return tagDAO.toDTO(entity);
    }

    public List<Tag> getAvailableOrUserTags (){
        return tagDAO.getAvailableOrUserTags();
    }

}
