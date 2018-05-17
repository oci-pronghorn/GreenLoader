package com.ociweb.ph;

import com.ociweb.json.encode.JSONRenderer;
import com.ociweb.pronghorn.network.config.HTTPSpecification;
import com.ociweb.pronghorn.network.schema.HTTPRequestSchema;
import com.ociweb.pronghorn.network.schema.ServerResponseSchema;
import com.ociweb.pronghorn.pipe.DataInputBlobReader;
import com.ociweb.pronghorn.pipe.DataOutputBlobWriter;
import com.ociweb.pronghorn.pipe.Pipe;
import com.ociweb.pronghorn.pipe.StructuredReader;
import com.ociweb.pronghorn.stage.PronghornStage;
import com.ociweb.pronghorn.stage.scheduling.GraphManager;

public class ExampleRestStage extends PronghornStage {

	private static final int READ_SIZE = Pipe.sizeOf(HTTPRequestSchema.instance, HTTPRequestSchema.MSG_RESTREQUEST_300);
	private final Pipe<HTTPRequestSchema>[] inputPipes;
	private final Pipe<ServerResponseSchema>[] outputPipes; 
	
    private static final JSONRenderer<StructuredReader> jsonRenderer = new JSONRenderer<StructuredReader>()
            .beginObject()
            .string("message", (reader,target) ->  reader.readText(Fields.name, target.append("Hey, ")).append("!"))
            .bool("happy", reader -> !reader.readBoolean(Fields.happy))
            .integer("age", reader -> reader.readInt(Fields.age) * 2)
            .endObject();
	
	public static ExampleRestStage newInstance(GraphManager graphManager, 
			Pipe<HTTPRequestSchema>[] inputPipes,
			Pipe<ServerResponseSchema>[] outputPipe, 
			HTTPSpecification httpSpec) {
		return new ExampleRestStage(graphManager, inputPipes, outputPipe, httpSpec);
	}
	
	public ExampleRestStage(GraphManager graphManager, 
			Pipe<HTTPRequestSchema>[] inputPipes,
			Pipe<ServerResponseSchema>[] outputPipes, 
			HTTPSpecification httpSpec) {
		
		super(graphManager, inputPipes, outputPipes);
		assert(inputPipes.length == outputPipes.length);
		
		this.inputPipes = inputPipes;
		this.outputPipes = outputPipes;
		
	}

	@Override
	public void run() {

		int i = inputPipes.length;
		while (--i>=0) {
			process(inputPipes[i], outputPipes[i]);
		}
	}

	private void process(Pipe<HTTPRequestSchema> input, Pipe<ServerResponseSchema> output) {
		
		while (Pipe.hasRoomForWrite(output) && 
			   Pipe.hasContentToRead(input) ) {
						
			int msgIdx = Pipe.takeMsgIdx(input);
			assert(msgIdx == HTTPRequestSchema.MSG_RESTREQUEST_300);
			
			long channelId = Pipe.takeLong(input);
			int sequenceNo = Pipe.takeInt(input);
			int verb = Pipe.takeInt(input);
			
			DataInputBlobReader<HTTPRequestSchema> inputStream = Pipe.openInputStream(input);
			int revision = Pipe.takeInt(input);			
			int context  = Pipe.takeInt(input);

			int outMsgSize = Pipe.addMsgIdx(output, ServerResponseSchema.MSG_TOCHANNEL_100);
			Pipe.addLongValue(channelId, output);
			Pipe.addIntValue(sequenceNo, output);
			
			DataOutputBlobWriter<ServerResponseSchema> outputStream = Pipe.openOutputStream(output);
			jsonRenderer.render(outputStream, inputStream.structured());
			DataOutputBlobWriter.closeLowLevelField(outputStream);
			
			Pipe.addIntValue(context, output);
			Pipe.confirmLowLevelWrite(output, outMsgSize);
			Pipe.publishWrites(output);
			
			Pipe.confirmLowLevelRead(input, READ_SIZE);
			Pipe.releaseReadLock(input);
			
		}		
	}
}
