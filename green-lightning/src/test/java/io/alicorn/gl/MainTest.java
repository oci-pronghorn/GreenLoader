package io.alicorn.gl;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ociweb.gl.api.GreenRuntime;
import com.ociweb.gl.test.LoadTester;
import com.ociweb.json.encode.JSONRenderer;
import com.ociweb.pronghorn.network.ClientSocketReaderStage;
import com.ociweb.pronghorn.network.ServerSocketWriterStage;
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
		GraphManager.showThreadIdOnTelemetry = true;
		ClientSocketReaderStage.abandonSlowConnections = false;
		
		runtime = GreenRuntime.run(new GlHello(false, 3302, false, false));
		
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
	public void uploadTest() {

		ClientSocketReaderStage.abandonSlowConnections = false;
		
		final Person samePerson = new Person("Rick", true, 13);
		
		LoadTester.runClient(
				 (i,w) -> renderer.render(w, samePerson),
				 (i,r)->{return true;},
				 "/hello",
				 false,
				 false,				 
				 1,
				 2_000_000,
				 "127.0.0.1",
				 3302,
				 10_000_000,//timeout MS
				 10,
				 System.out
				);
	}
	
	

}
