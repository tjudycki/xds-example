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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.DataSource;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Document;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntry;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Identifiable;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.SubmissionSet;
import org.openehealth.ipf.commons.ihe.xds.core.requests.ProvideAndRegisterDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.responses.ErrorCode;
import org.openehealth.ipf.commons.ihe.xds.core.responses.ErrorInfo;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Severity;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Processing XDS ITI-41 Provide And Register Document Set.
 *  
 * @author Tomasz Judycki
 */
@Component
public class Iti41Processor implements Processor {
    protected Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public void process(Exchange exchange) throws Exception {
	    
		Message message = exchange.getIn();

		ProvideAndRegisterDocumentSet request = message.getBody(ProvideAndRegisterDocumentSet.class); //ITI-41 type of request class
		log.debug("ProvideAndRegisterDocumentSet: " + request);
		SubmissionSet submissionSet = request.getSubmissionSet();
		Identifiable patientId = submissionSet.getPatientId();
		log.debug("Patient = " + patientId);
		List<Document> documentList = request.getDocuments();
		// create a response
		Response response = new Response();
		if(documentList.size() == 0) {
			// return error message
			response.setStatus(Status.FAILURE);
			response.getErrors().add(new ErrorInfo(ErrorCode._USER_DEFINED,"", Severity.ERROR,"","Empty document list!"));
		} else {
			Document document = documentList.get(0);
			log.debug("Received some documents, first one: "  + document.toString());
			DocumentEntry documentEntry = document.getDocumentEntry();
			// retrieve document content
			DataHandler dataHandler = document.getDataHandler();
			DataSource dataSource = dataHandler.getDataSource();
			InputStream inputStream = dataSource.getInputStream();
			InputStreamReader isReader = new InputStreamReader(inputStream);
			BufferedReader reader = new BufferedReader(isReader);
			StringBuffer sb = new StringBuffer();
			String str;
			while((str = reader.readLine())!= null){
				sb.append(str);
			}
			log.info("Content: " + sb.toString());				
			response.setStatus(Status.SUCCESS);
		}
		exchange.getOut().setBody(response);
	}

}
