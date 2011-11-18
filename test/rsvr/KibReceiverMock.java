package rsvr;

import java.io.IOException;
import java.net.Socket;

public class KibReceiverMock extends KibReceiver {

	public KibReceiverMock() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	class ClientMock extends KibReceiver.Client {

		ClientMock(Socket con) throws Exception {
			super(con);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public String rcvKibMsg() throws IOException {
			// TODO Auto-generated method stub
			return super.rcvKibMsg();
		}
	}
}
