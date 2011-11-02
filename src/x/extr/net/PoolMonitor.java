/** Main system          : x
  * Sub system           : net
  * Classname            : PoolMonitor.java
  * Initial date         : 2005.11.04
  * Author               : 황재천
  * Update date/Modifier : 
  * Description          : Socket Pool 모니터링을 위한 클래스
  * Version information  : v 1.0
  *
  * Copyright notice     : Copyright (C)2003 by Tongyang Systems Co. Ltd.
  *                              All right reserved.
*/

package x.extr.net;

import javax.rmi.*;
import java.rmi.*;
import javax.naming.*;
import x.extr.net.*;
import x.extr.lib.*;

/** Socket Pool 모니터링을 위한 클래스
  * @author 황재천
  */
public class PoolMonitor {

	/** Socket Pool 모니터링을 위한 Main모듈
      */
	public static void main(String[] args) {
		String[] state = null;
		StringBuffer sBuf = null;
		System.out.println("Start");

		try{
			TCPClientPool onlineCHome = null;
			TCPClientPool ftpCHome = null;
			TCPClientPool dpsCHome = null;
			TCPServerPool dpsSHome = null;


			PropertyManager pm = PropertyManager.getInstance();
			java.util.Properties pt = new java.util.Properties();
			pt.put(Context.INITIAL_CONTEXT_FACTORY, (String)(pm.getProperty("SYS_FACTORY")));
			pt.put(Context.PROVIDER_URL, (String)(pm.getProperty("NET_URL")));
			Context ctx = new InitialContext(pt);

			try {
				onlineCHome = (TCPClientPool)ctx.lookup("ONLINECLIENT");
			} catch (Exception e) {}
			try {
				ftpCHome = (TCPClientPool)ctx.lookup("FTPCLIENT");
			} catch (Exception e) {}
			try {
				dpsCHome = (TCPClientPool)ctx.lookup("FTPREQCLIENT");
			} catch (Exception e) {}
			try {
				dpsSHome = (TCPServerPool)ctx.lookup("DPSSERVER");
			} catch (Exception e) {}

	        System.out.println("Pool Monitor started...");


			while(true) {
				sBuf = new StringBuffer("");
				sBuf.append("ONLINECLIENT:");
				try {
					state = onlineCHome.getState();
					for(int i=0;i<state.length;i++) {
						sBuf.append(state[i]);
					}
				} catch (Exception e) {
						sBuf.append("DOWN");
				}
				sBuf.append(" FTPCLIENT:");
				try {
					state = ftpCHome.getState();
					for(int i=0;i<state.length;i++) {
						sBuf.append(state[i]);
					}
				} catch (Exception e) {
						sBuf.append("DOWN");
				}
				sBuf.append(" DPSCLIENT:");
				try {
					state = dpsCHome.getState();
					for(int i=0;i<state.length;i++) {
						sBuf.append(state[i]);
					}
				} catch (Exception e) {
						sBuf.append("DOWN");
				}
				sBuf.append(" DPSSERVER:");
				try {
					state = dpsSHome.getState();
					for(int i=0;i<state.length;i++) {
						sBuf.append(state[i]);
					}
				} catch (Exception e) {
						sBuf.append("DOWN");
				}
				System.out.println(sBuf.toString());
				Thread.sleep(1000);
			}

		} catch(Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

	}

}

