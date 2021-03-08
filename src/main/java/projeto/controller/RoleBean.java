package projeto.controller;

import io.dropwizard.hibernate.UnitOfWork;
import projeto.api.dtos.entities.RoleDTO;
import projeto.controller.exceptions.MyException;
import projeto.core.Role;
import projeto.data.BaseDAO;
import projeto.data.RoleDAO;

import java.util.List;

public class RoleBean extends BaseBean<Role, RoleDTO> {
    final private RoleDAO roleDAO;

    public RoleBean(RoleDAO roleDAO) {
        super( roleDAO );
        this.roleDAO = roleDAO;
    }

    public List<String> getUserRoles() throws MyException {
        return roleDAO.getUserRoles();
    }


}
