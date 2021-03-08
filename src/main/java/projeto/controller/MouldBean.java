package projeto.controller;

import projeto.api.dtos.entities.MouldDTO;
import projeto.api.dtos.entities.ProcessDTO;
import projeto.api.dtos.entities.RoleDTO;
import projeto.controller.exceptions.EntityDoesNotExistException;
import projeto.core.*;
import projeto.core.Process;
import projeto.data.MouldDAO;
import projeto.data.RoleDAO;

import java.util.List;
import java.util.stream.Collectors;

public class MouldBean extends BaseBean<Mould, MouldDTO> {
    final private MouldDAO mouldDAO;

    public MouldBean(MouldDAO mouldDAO) {
        super( mouldDAO );
        this.mouldDAO = mouldDAO;
    }

    public List<MouldDTO> toDTOsList(List<Mould> moulds) {
        return moulds.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public MouldDTO toDTO(Mould entity){
        return mouldDAO.toDTO(entity);
    }


    public List<Part> getPartsByMouldCode(String mouldCode ) throws EntityDoesNotExistException {
        return mouldDAO.getPartsByMouldCode( mouldCode );
    }


    public List<Mould> getAll() {
        return mouldDAO.getAll();
    }

}
