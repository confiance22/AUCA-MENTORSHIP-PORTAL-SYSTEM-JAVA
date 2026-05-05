package service.impl;

import dao.UserDao;
import dao.impl.UserDaoImpl;
import model.User;
import model.UserRole;
import service.UserService;
import util.PasswordUtil;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class UserServiceImpl extends UnicastRemoteObject implements UserService {

    private final UserDao userDao;

    public UserServiceImpl() throws RemoteException {
        super();
        this.userDao = new UserDaoImpl();
    }

    @Override
    public User registerUserRecord(User theUser) throws RemoteException {
        String hashedPassword = PasswordUtil.hashPassword(theUser.getPassword());
        theUser.setPassword(hashedPassword);
        userDao.save(theUser);
        return cleanUser(theUser);
    }

    @Override
    public User updateUserRecord(User theUser) throws RemoteException {
        userDao.update(theUser);
        return cleanUser(theUser);
    }

    @Override
    public User deleteUserRecord(User theUser) throws RemoteException {
        userDao.delete(theUser.getId());
        return cleanUser(theUser);
    }

    @Override
    public User findUserRecordById(User theUser) throws RemoteException {
        return cleanUser(userDao.findById(theUser.getId()));
    }

    @Override
    public List<User> findAllUserRecords() throws RemoteException {
        List<User> users = userDao.findAll();
        if (users != null) {
            users.forEach(this::cleanUser);
        }
        return users;
    }

    @Override
    public User findUserRecordByEmail(String email) throws RemoteException {
        return cleanUser(userDao.findByEmail(email));
    }

    @Override
    public List<User> findUserRecordsByRole(UserRole role) throws RemoteException {
        List<User> users = userDao.findByRole(role);
        if (users != null) {
            users.forEach(this::cleanUser);
        }
        return users;
    }

    @Override
    public boolean existsByEmail(String email) throws RemoteException {
        return userDao.existsByEmail(email);
    }

    @Override
    public User login(String email, String password) throws RemoteException {
        User user = userDao.findByEmail(email);
        if (user != null) {
            if (PasswordUtil.checkPassword(password, user.getPassword())) {
                return cleanUser(user);
            }
        }
        return null;
    }

    /**
     * Replaces Hibernate-specific collection types (like PersistentBag) with standard Java collections
     * to avoid ClassNotFoundException on the client side when Hibernate is not present.
     */
    private User cleanUser(User user) {
        if (user == null) return null;
        
        // Null out or replace Hibernate collections with standard ones
        user.setSessionsAsMentor(null);
        user.setSessionsAsMentee(null);
        user.setNotifications(null);
        user.setMentorProfile(null); // OneToOne can also be a proxy
        
        return user;
    }
}
