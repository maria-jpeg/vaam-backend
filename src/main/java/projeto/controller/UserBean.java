package projeto.controller;


import io.dropwizard.hibernate.UnitOfWork;
import org.joda.time.DateTime;
import projeto.api.dtos.entities.PartDTO;
import projeto.api.dtos.entities.UserDTO;
import projeto.controller.exceptions.EntityDoesNotExistException;
import projeto.controller.exceptions.EntityExistsException;
import projeto.controller.exceptions.MyException;
import projeto.core.*;
import projeto.core.Process;
import projeto.data.TagDAO;
import projeto.data.UserDAO;

import javax.ws.rs.NotAllowedException;
import java.util.List;
import java.util.stream.Collectors;


public class UserBean extends BaseBean<User, UserDTO>
{
    final private UserDAO userDAO;

    public UserBean(UserDAO userDAO) {
        super( userDAO );
        this.userDAO = userDAO;
    }

    public UserDTO toDTO(User entity){
        return userDAO.toDTO(entity);
    }

    public List<UserDTO> toDTOsList(List<User> users) {
        return users.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<ActivityUserEntry> getEntriesAssociatedToUsername(String username, DateTime eventStartDate, DateTime eventEndDate) {
        return userDAO.getEntriesAssociatedToUsername( username , eventStartDate, eventEndDate);
    }

    @Override
    @UnitOfWork
    public User create(User entity) throws EntityExistsException {
        return super.create(entity);
    }

    @Override
    @UnitOfWork
    public User findOrFail(Object primaryKey) throws EntityDoesNotExistException {
        return super.findOrFail(primaryKey);
    }

    @Override
    @UnitOfWork
    public User find(Object primaryKey) {
        return super.find(primaryKey);
    }

    public List<Process> getUserProcesses(String username ) throws EntityDoesNotExistException {
        return userDAO.getUserProcesses( username );
    }

    public User findUserCreate(String username){
        return userDAO.findByUsername(username);
    }

    public User findByUsername( String username ) throws EntityDoesNotExistException
    {

        User utilizador = userDAO.findByUsername( username );
        if (utilizador != null){
            return utilizador;
        }else{
            throw new EntityDoesNotExistException("Utilizador com o username " + username + " não encontrado");
        }

    }

    public List<User> findEmail(String email){
        return userDAO.findEmail(email);
    }


    public User authenticate(final String username, final String password) throws MyException {
        User user = userDAO.findByUsername(username);
        if (user != null && user.getPassword().equals(Utils.hashPassword(password))) {
            return user;
        }
        throw new MyException("Falha na auntenticação com username '" + username + "': username desconhecido ou password incorreta");
    }

    public UserDTO findByTag(String tagRfid) throws EntityDoesNotExistException {

        User user = userDAO.getUserWithTag(tagRfid);

        if (user != null) {
            return this.toDTO(user);
        } else{
            throw new EntityDoesNotExistException("Tag not associated with user");
        }

    }

    public UserDTO setTagRfid(TagBean tagBean, String username, String tagRfid) throws EntityDoesNotExistException, EntityExistsException {

        User user = userDAO.findOrFail(username);

        userDAO.verifyTagIsBeingUsed(tagRfid, username);

        Tag tag = tagBean.findOrFail(tagRfid);
        if (tag == null) {
            throw new EntityDoesNotExistException("Tag não encontrada");
        }

        if(!tag.getIsAvailable() || !tag.getIsUser()){
            throw new EntityExistsException("Tag não está disponível");
        }

        user.setTag(tag);
        tag.setIsAvailable(false);
        tag.setIsUser(true);

        return this.toDTO(user);
    }


    public UserDTO removeTagFromUserByUsername(String username) throws EntityDoesNotExistException {

        User user = userDAO.findOrFail(username);

        Tag tag = user.getTag();

        if (tag == null) {
            throw new EntityDoesNotExistException("O utilizador '" + username + "' não tem tag associada.");
        }

        user.setTag(null);
        tag.setIsAvailable(true);

        return this.toDTO(user);

    }

    public UserDTO removeTagFromUserByTagRfid(String tagRfid) throws EntityDoesNotExistException {

        User user = userDAO.getUserWithTag(tagRfid);

        Tag tag = user.getTag();

        user.setTag(null);
        tag.setIsAvailable(true);

        return this.toDTO(user);

    }



}
