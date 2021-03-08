package projeto.data;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import projeto.api.dtos.entities.ActivityDTO;
import projeto.api.dtos.entities.WorkstationDTO;
import projeto.controller.exceptions.EntityDoesNotExistException;
import projeto.controller.exceptions.EntityExistsException;
import projeto.controller.exceptions.TransformToEntityException;
import projeto.core.Activity;
import projeto.core.Process;
import projeto.core.Workstation;

import java.util.List;

public class WorkstationDAO extends BaseDAO<Workstation, WorkstationDTO> {

    public WorkstationDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Workstation toEntity(WorkstationDTO dto) throws TransformToEntityException {
        Workstation workstation;

        try {
            workstation = findOrFail(dto.getId());
        } catch ( EntityDoesNotExistException e )
        {
            throw new TransformToEntityException( e.getMessage() );
        }

        return workstation;
    }

    public List<Workstation> getTaggingWorkstations()
    {
        Query<Workstation> query = currentSession().createNamedQuery( "Workstation.getTaggingWorkstations", Workstation.class );
        List<Workstation> workstations = query.getResultList();

        return workstations;
    }

    @Override
    public WorkstationDTO toDTO(Workstation entity) {
        return new WorkstationDTO(
                entity.getId(),
                entity.getName(),
                entity.getActivity().getId(),
                entity.getIsTagging(),
                entity.getIsEndWorkstation()
        );
    }

    public List<Workstation> findName(String name) throws EntityExistsException {
        List<Workstation> workstations = (List<Workstation>) currentSession().createNamedQuery("Workstation.getWorkstationsByName").setParameter("name", name).getResultList();

        if(workstations.size() > 0){
            throw new EntityExistsException("Máquina com o nome " + name + " já existe");
        }

        return workstations;
    }
}
