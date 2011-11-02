/** Main system          : x
  * Sub system           : net
  * Classname            : PoolCheck.java
  * Initial date         : 2005.11.15
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
import x.extr.lib.*;
import x.extr.net.*;

/** Socket Pool 모니터링을 위한 클래스
  * @author 황재천
  */
public class PoolCheck {

	public static void main(String[] args) {
		String[] state = null;
		StringBuffer sBuf = null;
		System.out.println("Start");

		try{
			TCPClientPool smHome = null;
			TCPServerPool smHome2 = null;

			PropertyManager pm = PropertyManager.getInstance();
			java.util.Properties pt = new java.util.Properties();
			pt.put(Context.INITIAL_CONTEXT_FACTORY, (String)(pm.getProperty("SYS_FACTORY")));
			pt.put(Context.PROVIDER_URL, (String)(pm.getProperty("NET_URL")));
			Context ctx = new InitialContext(pt);

			if (args[0].indexOf("SERVER") > 0) {
				smHome2 = (TCPServerPool)ctx.lookup(args[0]);
		        System.out.println("TCPServerPool Server Connect...");
			} else {
				smHome = (TCPClientPool)ctx.lookup(args[0]);
				System.out.println("TCPClientPool Server Connect...");
			}

			while(true) {
				if (smHome2 != null) {
					state = smHome2.getState();
				} else {
					state = smHome.getState();
				}
				sBuf = new StringBuffer("");
				for(int i=0;i<state.length;i++) {
					sBuf.append("[" + state[i] + "]");
				}
				System.out.println("STATE : " + sBuf.toString());
				Thread.sleep(1000);
			}


		} catch(Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

	}

}

