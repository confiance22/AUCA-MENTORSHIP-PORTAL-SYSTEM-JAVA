package service;

import model.User;
import model.UserRole;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface UserService extends Remote {
    User registerUserRecord(User theUser) throws RemoteException;
    User updateUserRecord(User theUser) throws RemoteException;
    User deleteUserRecord(User theUser) throws RemoteException;
    User findUserRecordById(User theUser) throws RemoteException;
    List<User> findAllUserRecords() throws RemoteException;
    
    // Additional DAO methods
    User findUserRecordByEmail(String email) throws RemoteException;
    List<User> findUserRecordsByRole(UserRole role) throws RemoteException;
    boolean existsByEmail(String email) throws RemoteException;
    
    // Auth method
    User login(String email, String password) throws RemoteException;
}
