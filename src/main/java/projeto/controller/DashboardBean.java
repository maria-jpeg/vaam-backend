package projeto.controller;

import projeto.api.dtos.entities.DashboardDTO;
import projeto.controller.exceptions.EntityDoesNotExistException;
import projeto.core.Dashboard;
import projeto.data.DashboardDAO;

import java.util.*;
import java.util.stream.Collectors;

public class DashboardBean extends BaseBean<Dashboard, DashboardDTO> {

    final private DashboardDAO dashboardDAO;

    public DashboardBean(DashboardDAO dashboardDAO){
        super(dashboardDAO);
        this.dashboardDAO = dashboardDAO;
    }

    public List<DashboardDTO> toDTOsList(List<Dashboard> dashboards) {
        return dashboards.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public void cleanTable() throws EntityDoesNotExistException {
        List<Dashboard> list = dashboardDAO.getAll();
        for (Dashboard dashboard : list) {
            dashboardDAO.remove(dashboard.getId());
        }
    }

    public DashboardDTO toDTO(Dashboard entity){
        return dashboardDAO.toDTO(entity);
    }
}
