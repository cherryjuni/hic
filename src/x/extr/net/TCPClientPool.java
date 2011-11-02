/** Main system          : x
  * Sub system           : net
  * Classname            : TCPClientPool.java
  * Initial date         : 2005.11.16
  * Author               : 황재천
  * Update date/Modifier : 
  * Description          : TCPClientPool의 인터페이스
  * Version information  : v 1.0
  *
  * Copyright notice     : Copyright (C)2003 by Tongyang Systems Co. Ltd.
  *                              All right reserved.
*/

package x.extr.net;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.net.*;
import java.io.*;
import x.extr.net.*;
import x.extr.log.*;

/** TCPClientPool의 인터페이스, 상세내역은 TCPClientPoolImpl을 참조.
  * @author 황재천
*/
public interface TCPClientPool extends Remote {

	public void initSock(int cnt,String host,int port) throws RemoteException;
	public boolean closeSock() throws RemoteException;
	public int alloc() throws RemoteException;
	public boolean free(int index) throws RemoteException;
	public String state() throws RemoteException;
	public int sendFTP(int index,String fileName,int startCount) throws Exception,RemoteException;
	public void send(int index,String data) throws Exception,RemoteException;
	public void send(int index,String data,byte[] bData,int offset,int len) throws Exception,RemoteException;
	public String recv(int index) throws Exception,RemoteException;
	public String call(int index,String data) throws Exception,RemoteException;
	public String call(String data) throws Exception,RemoteException;
	public void callTemp(String data) throws Exception,RemoteException;
	public String callWithoutSeq(String data) throws Exception,RemoteException;
	public String callWithoutSeqForHome(String data) throws Exception,RemoteException;
	public void callWithoutSeqForPDS(String data) throws Exception,RemoteException;

	public String[] getState() throws RemoteException;
}