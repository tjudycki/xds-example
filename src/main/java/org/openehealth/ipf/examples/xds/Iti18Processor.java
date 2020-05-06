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


import java.util.UUID;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.commons.codec.digest.DigestUtils;
import org.joda.time.DateTime;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AssigningAuthority;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AvailabilityStatus;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Code;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntry;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntryType;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Identifiable;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.LocalizedString;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Timestamp;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Timestamp.Precision;
import org.openehealth.ipf.commons.ihe.xds.core.requests.QueryRegistry;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindDocumentsQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.Query;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType;
import org.openehealth.ipf.commons.ihe.xds.core.responses.ErrorCode;
import org.openehealth.ipf.commons.ihe.xds.core.responses.ErrorInfo;
import org.openehealth.ipf.commons.ihe.xds.core.responses.QueryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Severity;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Processing XDS ITI-18 Stored Query
 *  
 * @author Tomasz Judycki
 */
@Component
public class Iti18Processor implements Processor {
    protected Logger log = LoggerFactory.getLogger(getClass());
	
	@Override
	public void process(Exchange exchange) throws Exception {
		Message message = exchange.getIn();
		QueryRegistry request = message.getBody(QueryRegistry.class); //ITI-18 type of request class
		log.debug("QueryRegistry: " + request);

		Query query = request.getQuery();
		QueryType queryType = query.getType();
		QueryResponse queryResponse = new QueryResponse(Status.SUCCESS);
		switch(queryType) {
		case FIND_DOCUMENTS:
			FindDocumentsQuery findQuery = (FindDocumentsQuery)query;

            // Create a fake document reference
			DocumentEntry documentEntry = new DocumentEntry();
			// set attributes
			documentEntry.setCreationTime(new Timestamp(new DateTime(), Precision.SECOND));
			documentEntry.getConfidentialityCodes().add(new Code("N", new LocalizedString("Normal", "en_EN", "utf-8"), "2.16.840.1.113883.5.25"));
			documentEntry.setFormatCode(new Code("urn:ihe:iti:xds:2017:mimeTypeSufficient", new LocalizedString("mimeType Sufficient", "en_EN", "utf-8"),"1.3.6.1.4.1.19376.1.2.7.1")); // http://hl7.org/fhir/ValueSet-formatcodes.html
			documentEntry.setPatientId(new Identifiable("121212", new AssigningAuthority("1.3.4.5")));
			documentEntry.setSourcePatientId(new Identifiable("121212", new AssigningAuthority("1.3.4.5")));
			documentEntry.setRepositoryUniqueId("1.2.3.4");
			documentEntry.setAvailabilityStatus(AvailabilityStatus.APPROVED);
			documentEntry.setMimeType("text/xml");
			documentEntry.setLanguageCode("en-US");
			
			// we can't return actual document in response, this is defined here to show how to set some attributes  
			String textXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Document></Document>";
			documentEntry.setHash(DigestUtils.sha1Hex( textXml ));
			documentEntry.setSize((long) textXml.length());

			documentEntry.setEntryUuid("urn:uuid:" + UUID.randomUUID().toString());
			documentEntry.setClassCode(new Code("HnP", new LocalizedString("History and Physical", "en_EN", "utf-8"), "L"));
			documentEntry.setTypeCode(new Code("O", new LocalizedString("Outpatient", "en_EN", "utf-8"), "L"));
			documentEntry.setType(DocumentEntryType.STABLE);
			documentEntry.setPracticeSettingCode(new Code("GM", new LocalizedString("General Medicine", "en_EN", "utf-8"), "L"));
			documentEntry.setHealthcareFacilityTypeCode(new Code("HnP", new LocalizedString("History and Physical", "en_EN", "utf-8"), "L"));

			queryResponse.getDocumentEntries().add(documentEntry);
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
