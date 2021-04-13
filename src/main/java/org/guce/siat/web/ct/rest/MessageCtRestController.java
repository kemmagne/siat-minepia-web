package org.guce.siat.web.ct.rest;

import java.util.Map;
import org.guce.orchestra.core.OrchestraEbxmlMessage;
import org.guce.siat.common.service.ProcessMessageService;
import org.guce.siat.common.utils.EbxmlUtils;
import org.guce.siat.core.ct.service.CtDocumentReciever;
import org.guce.siat.web.ct.rest.exceptions.MessageProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author tadzotsa
 */
@RequestMapping("ws")
@RestController
@Transactional
public class MessageCtRestController {

    private static final Logger LOG = LoggerFactory.getLogger(MessageCtRestController.class);

    @Autowired
    private CtDocumentReciever documentReciever;
    @Autowired
    private ProcessMessageService processMessageService;

    @RequestMapping(value = "hello", method = RequestMethod.GET)
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello World !");
    }

    @RequestMapping(value = "ebxml", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> processMessage(@RequestBody byte[] ebxml) throws Exception {
        try {
            final byte[] response = processMessageService.process(ebxml, documentReciever);
            final String responseStr = new String(response);
            return ResponseEntity.ok(responseStr);
        } catch (Exception ex) {
            return exceptionHandler(new MessageProcessingException(ebxml, ex.getMessage(), ex));
        }
    }

//    @ExceptionHandler(MessageProcessingException.class)
    public ResponseEntity<String> exceptionHandler(MessageProcessingException ex) throws Exception {

        final byte[] ebxml = ex.getEbxml();
        final Map<String, Object> messageMap = EbxmlUtils.ebxmlToMap(ebxml);
        final Map<String, Object> exceptionResult = documentReciever.generateAperakCFile(messageMap, ex.getMessage());
        final OrchestraEbxmlMessage exResponse = EbxmlUtils.mapToEbxml(exceptionResult);

        return ResponseEntity.ok(new String(exResponse.getData()));
    }

}
