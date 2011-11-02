/** Main system          : x
  * Sub system           : net
  * Classname            : TCPServerPool.java
  * Initial date         : 2005.11.04
  * Author               : 황재천
  * Update date/Modifier : 
  * Description          : TCPServerPool의 인터페이스
  * Version information  : v 1.0
  *
*/

package x.extr.net;

import java.rmi.Remote;
import java.rmi.RemoteException;

/** TCPServerPool의 인터페이스
  * 상세내용은 TCPServerPoolImpl을 참조
  * @author 황재천
*/
public interface TCPServerPool extends Remote {

	public void initSock() throws RemoteException;

	public String[] getState() throws RemoteException;
}