package projeto.data;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.joda.time.DateTime;
import projeto.api.dtos.entities.UserDTO;
import projeto.controller.Utils;
import projeto.controller.exceptions.EntityDoesNotExistException;
import projeto.controller.exceptions.EntityExistsException;
import projeto.controller.exceptions.TransformToEntityException;
import projeto.core.*;
import projeto.core.Process;

import java.util.List;

public class UserDAO extends BaseDAO<User, UserDTO>{

    private RoleDAO roleDAO;

    public UserDAO(SessionFactory sessionFactory, RoleDAO roleDAO) {
        super(sessionFactory);
        this.roleDAO = roleDAO;
    }

    @Override
    public User toEntity(UserDTO dto) throws TransformToEntityException {
        Role role = roleDAO.find(dto.getRole());
        return new User(
                dto.getUsername(),
                Utils.hashPassword( dto.getPassword() ),
                role,
                dto.getName(),
                dto.getEmail());
    }

    @Override
    public UserDTO toDTO(User entity) {
        return new UserDTO(
                entity.getUsername(),
                entity.getRole().getName(),
                entity.getName(),
                entity.getEmail()
        );
    }


    public User findByUsername( String username)
    {
        if( username == null || username.isEmpty() )
            return null;

        Query<User> query = currentSession().createNamedQuery( "User.checkUsername", User.class );
        query.setParameter( "username", username );

        List results = query.getResultList();
        if (results.isEmpty()){
            return null;
        }
        else if (results.size() == 1){
            return (User) results.get(0);
        }

        return query.getSingleResult();
    }

    public List<User> findEmail(String email) {
        return (List<User>) currentSession().createNamedQuery("User.getUsersByEmail").setParameter("email", email).getResultList();
    }

    public User getUserWithTag(String tagRfid) throws EntityDoesNotExistException {
        if( tagRfid.isEmpty() )
            throw new EntityDoesNotExistException("Tag rfid vazia.");

        Query<User> query = currentSession().createNamedQuery( "User.getUsersWithTag", User.class );
        query.setParameter( "tagRfid", tagRfid );

        List results = query.getResultList();
        if (results.isEmpty()){
            throw new EntityDoesNotExistException("Utilizador com tag '" + tagRfid + "' não encontrado.");
        }
        else if (results.size() == 1){
            return (User) results.get(0);
        }

        return query.getSingleResult();
    }

    public void verifyTagIsBeingUsed(String tagRfid, String username) throws EntityExistsException {
        Query<User> query = currentSession().createNamedQuery( "User.getUsersWithTag", User.class );
        query.setParameter( "tagRfid", tagRfid );

        List results = query.getResultList();
        if (!results.isEmpty()){
            User user = (User) results.get(0);
            if(!user.getUsername().equals(username)){
                throw new EntityExistsException("A tag " + tagRfid + "já se encontra associada ao utilizador " + user.getUsername());
            }
        }
    }

    public List<Process> getUserProcesses( String username ) throws EntityDoesNotExistException
    {
        Query<Process> query = currentSession().createNamedQuery( "User.getUserProcesses", Process.class );
        query.setParameter( "username", username );
        List<Process> processes = query.getResultList();

        if( processes.size() == 0 )
            throw new EntityDoesNotExistException( "Não existem processos associados ao utilizador '" + username + "'.");

        return processes;
    }


    public List<ActivityUserEntry> getEntriesAssociatedToUsername(String username, DateTime eventStartDate, DateTime eventEndDate)
    {
        Query<ActivityUserEntry> query = currentSession().createNamedQuery( "ActivitiesUsers.getEntriesAssociatedToUsername", ActivityUserEntry.class );
        query.setParameter( "username", username );
        query.setParameter( "eventStartDate", eventStartDate );
        query.setParameter( "eventEndDate", eventEndDate );
        List<ActivityUserEntry> entries = query.getResultList();

        return entries;
    }

}
