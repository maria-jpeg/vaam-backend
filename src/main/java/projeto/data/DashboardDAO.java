package projeto.data;

import org.hibernate.query.Query;
import projeto.api.dtos.entities.DashboardDTO;
import projeto.api.dtos.entities.ProcessDTO;
import projeto.controller.exceptions.EntityDoesNotExistException;
import projeto.controller.exceptions.TransformToEntityException;
import projeto.core.Dashboard;
import org.hibernate.SessionFactory;
import projeto.core.Event;
import projeto.core.Process;

import java.util.List;

public class DashboardDAO extends BaseDAO<Dashboard, DashboardDTO> {

    public DashboardDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Dashboard toEntity(DashboardDTO dto) throws TransformToEntityException, EntityDoesNotExistException {
        Dashboard dashboard;

        try {
            dashboard = findOrFail(dto.getId());
        } catch (EntityDoesNotExistException e) {
            throw new TransformToEntityException(e.getMessage());
        }

        return dashboard;
    }

    @Override
    public DashboardDTO toDTO(Dashboard entity) {
        if(entity.getProcess()==null){
            return new DashboardDTO(
                    entity.getDate(),
                    entity.getValue(),
                    entity.getUnit(),
                    entity.getDescription()
            );
        }
        return new DashboardDTO(
                entity.getDate(),
                entity.getValue(),
                entity.getUnit(),
                entity.getDescription(),
                processToDTO(entity.getProcess())
        );
    }

    public ProcessDTO processToDTO(Process process)
    {
        return new ProcessDTO(
                process.getId(),
                process.getName(),
                process.getDescription(),
                process.getStartDate(),
                process.getEndDate(),
                process.getNumberOfCases(),
                process.getNumberOfActivities()
        );
    }

}
