package io.alicorn.gl;

import com.ociweb.gl.api.*;
import com.ociweb.pronghorn.pipe.ChannelWriter;
import com.ociweb.pronghorn.pipe.StructuredReader;
import com.ociweb.pronghorn.pipe.StructuredWriter;

public class RestConsumer implements RestListener {

    private final GreenCommandChannel cmd2;
    private final HTTPResponseService httpResponseService;

    private HTTPRequestReader requestW;
    private PubSubService pubSubService;

    private Writable w = new Writable() {

        @Override
        public void write(ChannelWriter writer) {
            // Write connection data.
            writer.writePackedLong(requestW.getConnectionId());
            writer.writePackedLong(requestW.getSequenceCode());
            
            StructuredReader sReader = requestW.structured();
			StructuredWriter sWriter = writer.structured();
			
			//sReader.readText(Field.NAME, sWriter.writeBlob(Field.NAME)); //is this broken??
			sWriter.writeText(Field.NAME, sReader.readText(Field.NAME));
            sWriter.writeBoolean(Field.HAPPY, sReader.readBoolean(Field.HAPPY));
            sWriter.writeInt(Field.AGE, sReader.readInt(Field.AGE));
            sWriter.selectStruct(Struct.PAYLOAD);
		
        }

    };
    public RestConsumer(GreenRuntime runtime) {
        this.cmd2 = runtime.newCommandChannel();
        httpResponseService = cmd2.newHTTPResponseService();
        pubSubService = cmd2.newPubSubService();

    }


    @Override
    public boolean restRequest(final HTTPRequestReader request) {

        if (!request.isVerbPost()) {
            httpResponseService.publishHTTPResponse(request, 404);
        }

        requestW = request;

        return pubSubService.publishTopic("/send/200", w);
    }
}