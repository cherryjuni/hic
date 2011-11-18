package ssvr;

import java.io.IOException;
import java.net.Socket;

import rsvr.KibReceiver;

public class SendDaemonMock extends SendDaemon {

	public SendDaemonMock() throws Exception {
		super();
	}

	class ClientMock extends SendDaemon.Client {

		ClientMock(Socket con) throws Exception {
			super(con);
		}
		
		@Override
		public String rcvMsg() throws IOException {
			return super.rcvMsg();
		}

		@Override
		public void sendMsg(String msg) throws IOException {
			super.sendMsg(msg);
		}
	}

}
