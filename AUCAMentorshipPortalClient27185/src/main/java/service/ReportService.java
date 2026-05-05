package service;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ReportService extends Remote {
    byte[] exportUsersToPdf() throws RemoteException;
    byte[] exportProgramsToExcel() throws RemoteException;
    byte[] exportSessionsToPdf() throws RemoteException;
    byte[] exportFeedbackToPdf() throws RemoteException;
}
