package projeto.controller;

import projeto.api.dtos.entities.MouldDTO;
import projeto.api.dtos.entities.PartDTO;
import projeto.api.dtos.entities.RoleDTO;
import projeto.controller.exceptions.EntityDoesNotExistException;
import projeto.core.Mould;
import projeto.core.Part;
import projeto.core.Role;
import projeto.core.Tag;
import projeto.data.MouldDAO;
import projeto.data.PartDAO;
import projeto.data.RoleDAO;
import projeto.data.TagDAO;

import java.util.List;
import java.util.stream.Collectors;

public class PartBean extends BaseBean<Part, PartDTO> {
    final private PartDAO partDAO;
    final private TagDAO tagDAO;

    public PartBean(PartDAO partDAO, TagDAO tagDAO) {
        super( partDAO);
        this.partDAO = partDAO;
        this.tagDAO = tagDAO;
    }

    public List<PartDTO> toDTOsList(List<Part> parts) {
        return parts.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public PartDTO toDTO(Part entity){
        return partDAO.toDTO(entity);
    }


    public PartDTO setTagRfid(String partCode, String tagRfid) throws EntityDoesNotExistException {

        Part part = partDAO.findOrFail(partCode);

        List<Part> parts = partDAO.getPartsWithTag(tagRfid);
        for (Part p :
                parts) {
            p.setTag(null);
        }

        Tag tag = tagDAO.findOrFail(tagRfid);

        part.setTag(tag);
        tag.setIsAvailable(false);
        tag.setIsUser(false);

        return this.toDTO(part);
    }

    public String getPartMouldCode(String partCode){
        return partDAO.getPartMouldCode(partCode);
    }
}
