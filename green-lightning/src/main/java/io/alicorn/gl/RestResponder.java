package io.alicorn.gl;

import com.ociweb.gl.api.*;
import com.ociweb.json.encode.JSONRenderer;
import com.ociweb.pronghorn.network.config.HTTPContentTypeDefaults;
import com.ociweb.pronghorn.pipe.ChannelReader;
import com.ociweb.pronghorn.pipe.ChannelWriter;

public class RestResponder implements PubSubListener{

    private final GreenCommandChannel cmd;
    private final HTTPResponseService httpResponseService;

    private static final JSONRenderer<ChannelReader> jsonRenderer = new JSONRenderer<ChannelReader>()
            .beginObject()
            .string("message", (channelReader,target) ->  channelReader.readUTF(target.append("Hey, ")).append("!"))
            .bool("happy", channelReader -> !channelReader.readBoolean())
            .integer("age", channelReader -> channelReader.readInt() * 2)
            .endObject();

    private ChannelReader payloadW;
    private Writable w = new Writable() {
        @Override
        public void write(ChannelWriter writer) {
            jsonRenderer.render(writer, payloadW);
        }
    };

    public RestResponder(GreenRuntime runtime) {
        cmd = runtime.newCommandChannel();
        httpResponseService = cmd.newHTTPResponseService();
    }

    @Override
    public boolean message(CharSequence topic, ChannelReader payload) {

        payloadW = payload;
        return httpResponseService.publishHTTPResponse(
                payload.readPackedLong(),
                payload.readPackedLong(),
                200, false, HTTPContentTypeDefaults.JSON, w);
    }

}