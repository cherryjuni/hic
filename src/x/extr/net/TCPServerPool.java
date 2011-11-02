/** Main system          : x
  * Sub system           : net
  * Classname            : TCPServerPool.java
  * Initial date         : 2005.11.04
  * Author               : Ȳ��õ
  * Update date/Modifier : 
  * Description          : TCPServerPool�� �������̽�
  * Version information  : v 1.0
  *
*/

package x.extr.net;

import java.rmi.Remote;
import java.rmi.RemoteException;

/** TCPServerPool�� �������̽�
  * �󼼳����� TCPServerPoolImpl�� ����
  * @author Ȳ��õ
*/
public interface TCPServerPool extends Remote {

	public void initSock() throws RemoteException;

	public String[] getState() throws RemoteException;
}