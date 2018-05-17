package com.ociweb.ph;

import com.ociweb.json.JSONAccumRule;
import com.ociweb.json.JSONExtractorCompleted;
import com.ociweb.json.JSONExtractorImpl;
import com.ociweb.json.JSONType;
import com.ociweb.json.decode.JSONExtractor;
import com.ociweb.pronghorn.network.DummyRestStage;
import com.ociweb.pronghorn.network.HTTPServerConfig;
import com.ociweb.pronghorn.network.NetGraphBuilder;
import com.ociweb.pronghorn.network.ServerCoordinator;
import com.ociweb.pronghorn.network.ServerFactory;
import com.ociweb.pronghorn.network.http.CompositeRoute;
import com.ociweb.pronghorn.network.http.ModuleConfig;
import com.ociweb.pronghorn.network.http.RouterStageConfig;
import com.ociweb.pronghorn.network.schema.HTTPRequestSchema;
import com.ociweb.pronghorn.network.schema.NetPayloadSchema;
import com.ociweb.pronghorn.network.schema.ReleaseSchema;
import com.ociweb.pronghorn.network.schema.ServerResponseSchema;
import com.ociweb.pronghorn.pipe.Pipe;
import com.ociweb.pronghorn.pipe.RawDataSchema;
import com.ociweb.pronghorn.stage.file.FileBlobReadStage;
import com.ociweb.pronghorn.stage.route.ReplicatorStage;
import com.ociweb.pronghorn.stage.scheduling.GraphManager;
import com.ociweb.pronghorn.stage.scheduling.StageScheduler;
import com.ociweb.pronghorn.stage.test.ConsoleJSONDumpStage;
import com.ociweb.pronghorn.stage.test.PipeCleanerStage;
import com.ociweb.pronghorn.util.MainArgs;

public class PHWebServer  {

	
	public static void main(String[] args) {
		
		new PHWebServer(true, 8088, true, true);
	}
	
	public PHWebServer(boolean tls, int port, boolean telemtry, boolean logging) {

		GraphManager gm = new GraphManager();		
		HTTPServerConfig serverConfig = NetGraphBuilder.serverConfig(port, gm);
		
		//show all these
		serverConfig.setConcurrentChannelsPerDecryptUnit(10);
		serverConfig.setConcurrentChannelsPerEncryptUnit(10);
		serverConfig.setTracks(4);
		serverConfig.setHost("127.0.0.1");
		
		serverConfig.logTraffic(logging);
		
		if (!tls) {
			serverConfig.useInsecureServer();
		}
		
		
		NetGraphBuilder.buildServerGraph(gm, serverConfig.buildServerCoordinator(), new ServerFactory() {
		
			@Override
			public void buildServer(GraphManager gm, 
									ServerCoordinator coordinator,
									Pipe<ReleaseSchema>[] releaseAfterParse, 
									Pipe<NetPayloadSchema>[] receivedFromNet,
									Pipe<NetPayloadSchema>[] sendingToNet) {
								
				NetGraphBuilder.buildHTTPStages(gm, coordinator, moduleConfig, 
										        releaseAfterParse, receivedFromNet, sendingToNet);
			}
		
		});		
		
		if (telemtry) {
			gm.enableTelemetry(8089);
		}
		StageScheduler.defaultScheduler(gm).startup();
		
	}


	static ModuleConfig moduleConfig = new ModuleConfig() {

		@Override
		public int moduleCount() {
			return 2;
		}

		@Override
		public Pipe<ServerResponseSchema>[] registerModule(
				int moduleInstance,
				GraphManager graphManager,
				RouterStageConfig routerConfig, 
				Pipe<HTTPRequestSchema>[] inputPipes) {
			
			if (0==moduleInstance) {
				Pipe<ServerResponseSchema>[] responses = Pipe.buildPipes(inputPipes.length, 
						 ServerResponseSchema.instance.newPipeConfig(2, 1<<9));
					
				int i = inputPipes.length;
				while (--i>=0) {
					ExampleRestStage.newInstance(
							graphManager, inputPipes[i], responses[i], 
							routerConfig.httpSpec()
							);
				}
	
				JSONExtractorCompleted extractor =
						new JSONExtractor()
						 .begin()
						 
					     .element(JSONType.TypeString, false, JSONAccumRule.First)					 
						 	.asField("name",Fields.name)
						 	
					     .element(JSONType.TypeBoolean, false, JSONAccumRule.First)					 
						 	.asField("happy",Fields.happy)
						 	
					     .element(JSONType.TypeInteger, false, JSONAccumRule.First)					 
						 	.asField("age",Fields.age)	 
						 
						 .finish();
				
				routerConfig.registerCompositeRoute(extractor).path("/hello").routeId(Routes.primary);
				return responses;
			} else {
				
				Pipe<ServerResponseSchema>[] responses = Pipe.buildPipes(inputPipes.length, 
						 ServerResponseSchema.instance.newPipeConfig(2, 1<<9));
					
				DummyRestStage.newInstance(
						graphManager, inputPipes, responses, 
						routerConfig.httpSpec()
						);
				
				CompositeRoute registerCompositeRoute = routerConfig.registerCompositeRoute();
				int i = 19;
				while (--i>=0) {
					registerCompositeRoute.path("/hello"+moduleInstance);//add all these paths.
				};
				registerCompositeRoute.routeId(Routes.others);
				return responses;
			}
		}
	};
          
}
