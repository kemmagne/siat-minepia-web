package org.guce.siat.web.ct.rest;

import java.io.IOException;
import org.guce.siat.common.service.ProcessMessageService;
import org.guce.siat.core.ct.service.CtDocumentReciever;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author tadzotsa
 */
@Transactional
@RequestMapping("ws")
@RestController
public class MessageRestController {

    @Autowired
    private CtDocumentReciever documentReciever;
    @Autowired
    private ProcessMessageService processMessageService;

    @RequestMapping(value = "hello/{name}", method = RequestMethod.GET)
    public ResponseEntity<String> hello(@PathVariable("name") String name) {
        return ResponseEntity.ok(String.format("Hello %s", name));
    }

    @RequestMapping(value = "ebxml", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> processMessage(@RequestBody byte[] ebxml) throws IOException {
        final byte[] response = processMessageService.process(ebxml, documentReciever);
        final String responseStr = new String(response);
        return ResponseEntity.ok(responseStr);
    }

}
