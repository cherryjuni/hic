package rsvr;


import static org.junit.Assert.*;

import java.net.Socket;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import rsvr.IKibGramEnum;
import rsvr.KibGram;
import rsvr.KibReceiver;

public class KibGramTest {

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testParserKibHeader()
  {
    int position[] = KibGram.getHeaderpositon();
    //                   000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000011111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111112222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222
    //                   000000000011111111112222222222333333333334444444444555555555666666666677777777778888888888999999999900000000001111111111222222222233333333333444444444455555555566666666667777777777888888888899999999990000000000111111111122222222223333333333344444444445555555556666666666777777777788888888889999999999
    //                   012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789
    String msg    = "030045678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789";
    // msg 길이 오류
    String errMsg = "0300456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789";
    
//    assertEquals("MSG Length check..", "0300", msg.substring(position[0], position[1]));

    KibReceiver kr = null;
    Socket s = null;
    KibReceiver.Client client = null;
	try {
		kr = new KibReceiver();
		client = kr.new Client(s);
		client.parseKibHeader(msg);
	} catch (Exception e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}

    KibGram kib;
    try {
      kib = KibGram.create(msg);
    
      String[] kibHeader = kib.getKibHeader();
      
      assertEquals("header count check..", position.length-1, kibHeader.length);
      
      for(int i = 0; i < position.length - 1; i++) {
        assertEquals(msg.substring(position[i], position[i+1]), kibHeader[i]);
      }
    } catch (Exception e) {
      e.printStackTrace();
      fail("Message parsing header Error..");
    }
    
    IKibGramEnum kib2;
    try {
      kib = KibGram.create(errMsg);
    
      String[] kibHeader = kib.getKibHeader();
      
      assertEquals("header count check..", position.length-1, kibHeader.length);
      
      for(int i = 0; i < position.length - 1; i++) {
        assertEquals(msg.substring(position[i], position[i+1]), kibHeader[i]);
      }
      fail("why is this check pass.... errMsg");
    } catch (Exception e) {
//	      e.printStackTrace();
    }
  }

  @Test
  public void testIsStartGram() {
	  assertEquals(300, Integer.parseInt("0300"));
  }
  
}
