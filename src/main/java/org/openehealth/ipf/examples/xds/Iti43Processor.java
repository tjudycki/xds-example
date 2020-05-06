/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.examples.xds;

import javax.activation.DataHandler;
import javax.activation.DataSource;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.openehealth.ipf.commons.ihe.xds.core.requests.DocumentReference;
import org.openehealth.ipf.commons.ihe.xds.core.requests.RetrieveDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.responses.RetrievedDocument;
import org.openehealth.ipf.commons.ihe.xds.core.responses.RetrievedDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sun.istack.ByteArrayDataSource;

/**
 * Processing XDS ITI-43 Retrieve Document Set.
 *  
 * @author Tomasz Judycki
 */
@Component
public class Iti43Processor implements Processor {
    protected Logger log = LoggerFactory.getLogger(getClass());
	
	@Override
	public void process(Exchange exchange) throws Exception {
		Message message = exchange.getIn();
		RetrieveDocumentSet request = message.getBody(RetrieveDocumentSet.class); //ITI-43 type of request class
		log.debug("RetrieveDocumentSet: " + request);

        // Create a successful response
        RetrievedDocumentSet response = new RetrievedDocumentSet();
        response.setStatus(Status.SUCCESS);

        for (DocumentReference documentReference : request.getDocuments()) {
            log.debug("Requested document reference: repositoryId " + documentReference.getRepositoryUniqueId() + " documentId " + documentReference.getDocumentUniqueId());
            // Create a fake document
            RetrievedDocument retrievedDocument = new RetrievedDocument();
            retrievedDocument.setMimeType("text/xml");
            retrievedDocument.setRequestData(documentReference); // this document is a response for that request
            DataSource dataSource = new ByteArrayDataSource(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Document></Document>".getBytes(),
                "text/xml; charset=UTF-8"
            );
            retrievedDocument.setDataHandler(new DataHandler(dataSource)); // this is actual document
            response.getDocuments().add(retrievedDocument);
        }
        exchange.getOut().setBody(response);
	}
}
