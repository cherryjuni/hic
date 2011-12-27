package testLogic;

import static org.junit.Assert.*;

import java.sql.Time;
import java.text.SimpleDateFormat;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TimeTest {

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
	public void test() {
		long timeMili = System.currentTimeMillis();
		Time time = new Time(timeMili);
		Time time2 = new Time(System.currentTimeMillis());
		Time time3 = new Time(timeMili + 1000);
		
		SimpleDateFormat sdf = new SimpleDateFormat("H:mm:ss"); //H는 시간 형식이 24
        SimpleDateFormat sdf1 = new SimpleDateFormat("h:mm:ss"); //h는 시간 형식이 12

        System.out.println(timeMili);
        System.out.println(sdf.format(time));
        System.out.println(sdf.format(time2));
        System.out.println(sdf.format(time3));
        
//        System.out.println(sdf.format(time));
//        System.out.println(sdf1.format(time2));
        
        fail("Not yet implemented");
	}

}
