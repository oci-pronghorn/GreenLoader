package io.alicorn.gl;

import com.ociweb.gl.api.*;
import com.ociweb.pronghorn.pipe.ChannelWriter;

public class RestConsumer implements RestListener {

    private final GreenCommandChannel cmd2;
    private final HTTPResponseService httpResponseService;
    private final long fieldA;
    private final long fieldB;
    private final long fieldC;

    private HTTPRequestReader requestW;
    private PubSubService pubSubService;

    private Writable w = new Writable() {

        @Override
        public void write(ChannelWriter writer) {
            // Write connection data.
            writer.writePackedLong(requestW.getConnectionId());
            writer.writePackedLong(requestW.getSequenceCode());

            // Write JSON data.
            writer.writeUTF(requestW.structured().readText(fieldA));
            writer.writeBoolean(requestW.structured().readBoolean(fieldB));
            writer.writeInt(requestW.structured().readInt(fieldC));
        }

    };
    public RestConsumer(GreenRuntime runtime, long fieldA, long fieldB, long fieldC) {
        this.cmd2 = runtime.newCommandChannel();
        httpResponseService = cmd2.newHTTPResponseService();
        pubSubService = cmd2.newPubSubService();
        this.fieldA = fieldA;
        this.fieldB = fieldB;
        this.fieldC = fieldC;
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