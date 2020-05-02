package org.openehealth.ipf.examples.xds;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Document;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntry;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Identifiable;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.SubmissionSet;
import org.openehealth.ipf.commons.ihe.xds.core.requests.ProvideAndRegisterDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Status;
import org.springframework.stereotype.Component;

@Component
public class Iti41Processor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		Message message = exchange.getIn();

		ProvideAndRegisterDocumentSet request = message.getBody(ProvideAndRegisterDocumentSet.class); //ITI-41 type of request class
		System.out.println("ProvideAndRegisterDocumentSet: " + request);
		SubmissionSet submissionSet = request.getSubmissionSet();
		Identifiable patientId = submissionSet.getPatientId();
		System.out.println("Patient = " + patientId);
		List<Document> documentList = request.getDocuments();
		if(documentList.size() == 0) {
			System.out.println("Empty document list!");
		} else {
			Document document = documentList.get(0);
			System.out.println("Received some documents: "  + document.toString());
			DocumentEntry documentEntry = document.getDocumentEntry();
		}

		Response response = new Response();
		
		response.setStatus(Status.SUCCESS);
		// or
//		response.setStatus(Status.FAILURE);
//		response.getErrors().add(new ErrorInfo(ErrorCode._USER_DEFINED,"", Severity.ERROR,"","We don't accept patients named James."));
		
        exchange.getOut().setBody(response);
    }

}
