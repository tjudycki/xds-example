package org.openehealth.ipf.examples.xds;


import javax.activation.DataHandler;
import javax.activation.DataSource;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.commons.codec.digest.DigestUtils;
import org.joda.time.DateTime;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AssigningAuthority;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AvailabilityStatus;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Code;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Document;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntry;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Identifiable;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.LocalizedString;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Timestamp;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Timestamp.Precision;
import org.openehealth.ipf.commons.ihe.xds.core.requests.QueryRegistry;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindDocumentsQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.Query;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryReturnType;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType;
import org.openehealth.ipf.commons.ihe.xds.core.responses.ErrorCode;
import org.openehealth.ipf.commons.ihe.xds.core.responses.ErrorInfo;
import org.openehealth.ipf.commons.ihe.xds.core.responses.QueryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Severity;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Status;
import org.springframework.stereotype.Component;

import com.sun.istack.ByteArrayDataSource;

@Component
public class Iti18Processor implements Processor {
	
	@Override
	public void process(Exchange exchange) throws Exception {
		Message message = exchange.getIn();
		QueryRegistry request = message.getBody(QueryRegistry.class); //ITI-18 type of request class
		System.out.println("QueryRegistry: " + request);
		System.out.println("=========================================================");

		QueryReturnType queryReturnType = request.getReturnType(); // LeafClass vs ObjectRef
		Query query = request.getQuery();
		QueryType queryType = query.getType(); // FindDocuments, FindFolders etc
		QueryResponse queryResponse = new QueryResponse(Status.SUCCESS);
		switch(queryType) {
		case FIND_DOCUMENTS:
			FindDocumentsQuery findQuery = (FindDocumentsQuery)query;
			 
			DocumentEntry documentEntry = new DocumentEntry();
			// set attributes
			documentEntry.setCreationTime(new Timestamp(new DateTime(), Precision.SECOND));
			documentEntry.getConfidentialityCodes().add(new Code("N", new LocalizedString("Normal", "en_EN", "utf-8"), "2.16.840.1.113883.5.25"));
			documentEntry.setFormatCode(new Code("urn:ihe:iti:xds:2017:mimeTypeSufficient", new LocalizedString("mimeType Sufficient", "en_EN", "utf-8"),"1.3.6.1.4.1.19376.1.2.7.1")); // http://hl7.org/fhir/ValueSet-formatcodes.html
			documentEntry.setPatientId(new Identifiable("121212", new AssigningAuthority("1.3.4.5")));
			documentEntry.setRepositoryUniqueId("1.2.3.4");
			documentEntry.setAvailabilityStatus(AvailabilityStatus.APPROVED);
			documentEntry.setMimeType("text/xml");
			documentEntry.setLanguageCode("en-US");
			
			if(queryReturnType == QueryReturnType.LEAF_CLASS) {
				// Create a fake document
				String textXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Document></Document>";
				final DataSource dataSource1 = new ByteArrayDataSource(
						textXml.getBytes(),
						"text/xml; charset=UTF-8"
					);
				documentEntry.setHash(DigestUtils.sha1Hex( textXml ));
				documentEntry.setSize((long) textXml.length());
				
				Document document = new Document();
				document.setDataHandler(new DataHandler(dataSource1));
				document.setDocumentEntry(documentEntry);
			
				queryResponse.getDocuments().add(document);
			}
			queryResponse.getDocumentEntries().add(documentEntry);
			System.out.println("QueryResponse: " + queryResponse);
			System.out.println("=========================================================");
	        exchange.getOut().setBody(queryResponse);
			break;
		default:
			queryResponse.setStatus(Status.FAILURE);
			queryResponse.getErrors().add(new ErrorInfo(ErrorCode._USER_DEFINED,"", Severity.ERROR,"","This query is not supported yet."));
			break;
		}
        exchange.getOut().setBody(queryResponse);
	}
	
}
