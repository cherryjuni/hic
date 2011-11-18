package ssvr;

import java.io.IOException;
import java.net.Socket;

import rsvr.KibReceiver;

public class SendDaemonMock extends SendDaemon {

	public SendDaemonMock() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	class ClientMock extends SendDaemon.Client {

		ClientMock(Socket con) throws Exception {
			super(con);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public String rcvMsg() throws IOException {
			// TODO Auto-generated method stub
			return super.rcvMsg();
		}
	}

}
