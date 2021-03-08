package projeto.data;

import org.hibernate.SessionFactory;
import projeto.api.dtos.entities.RoleDTO;
import projeto.controller.exceptions.MyException;
import projeto.controller.exceptions.TransformToEntityException;
import projeto.core.Role;

import java.util.List;

public class RoleDAO extends BaseDAO<Role, RoleDTO> {
    public RoleDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Role toEntity(RoleDTO dto) throws TransformToEntityException {
        return null;
    }

    @Override
    public RoleDTO toDTO(Role entity) {
        return null;
    }

    public List<String> getUserRoles() throws MyException {
        List<String> roles = currentSession().createNamedQuery("Role.getUserRoles").getResultList();
        if(roles.size() == 0){
            throw new MyException("Não existem roles.");
        }
        return roles;
    }

}
