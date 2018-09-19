package io.alicorn.gl;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ociweb.gl.api.GreenRuntime;
import com.ociweb.gl.test.LoadTester;
import com.ociweb.json.encode.JSONRenderer;
import com.ociweb.pronghorn.network.ClientSocketReaderStage;
import com.ociweb.pronghorn.stage.scheduling.GraphManager;

public class MainTest {

	public class Person {

		public String name;
		public boolean happy;
		public int age;
		
		
		public Person(String name, boolean happy, int age) {
			this.name = name;
			this.happy = happy;
			this.age = age;
		}

	}

	private static GreenRuntime runtime;

	@BeforeClass
	public static void startServer() {

		//GraphManager.combineCommonEdges = false;//TODO: need to add new cycle volume data to common edge...
		
		//GraphManager.showThreadIdOnTelemetry = true;
		//GraphManager.showScheduledRateOnTelemetry = true;
		//GraphManager.showMessageCountRangeOnTelemetry = true;
		
		//ServerSocketWriterStage.lowLatency = false;
		//ServerSocketWriterStage.hardLimtNS = 16_000;//ns
		
		//for cloud testing we bump this up since it may be running on very slow hardware
		//ClientAbandonConnectionScanner.absoluteNSToKeep =      2_000_000_000L; //2sec calls are always OK.
		ClientSocketReaderStage.abandonSlowConnections = false;
		
		runtime = GreenRuntime.run(new GlHello(false, 3302, true, false));
		
	}
		
	@AfterClass
	public static void stopServer() {
		runtime.shutdownRuntime();	
		runtime = null;
	}

	
	private JSONRenderer<Person> renderer = new JSONRenderer<Person>()
			.startObject()
			.string("name", (o,t)->t.append(o.name))
			.bool("happy", o->o.happy)
			.integer( "age", o->o.age)
			.endObject();

	
	@Test
	public void uploadProductsTest() {

		GraphManager.combineCommonEdges = false;
		ClientSocketReaderStage.abandonSlowConnections = false;
		
		final Person samePerson = new Person("Rick", true, 13);
		
		LoadTester.runClient(
				 (i,w) -> renderer.render(w, samePerson),
				 (i,r)->{return true;},
				 "/hello",
				 false,
				 true,				 
				 1,
				 200_000,
				 "127.0.0.1",
				 3302,
				 10_000_000,//timeout MS
				 10,
				 System.out
				);
	}
	
	

}
