package io.alicorn.gl;

import com.ociweb.gl.api.*;
import com.ociweb.pronghorn.network.config.HTTPContentTypeDefaults;

public class SimpleRestListener implements RestListener {

    private final HTTPResponseService cmd;

    public SimpleRestListener(GreenRuntime runtime) {
        // TODO: Does queue length matter too much? 1024 seems like a sane default.
        cmd = runtime.newCommandChannel().newHTTPResponseService(1024, 300);
    }

    @Override
    public boolean restRequest(HTTPRequestReader request) {
        if (!request.isVerbPost()) {
            return cmd.publishHTTPResponse(request, 404);
        }

        return cmd.publishHTTPResponse(request, 200, false,
                                       HTTPContentTypeDefaults.TXT, (writer) -> {
                    writer.writeUTF8Text("You said hello to an alternate POST endpoint!");
                });
    }
}
