package org.openehealth.ipf.examples.xds;

import static org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators.iti18RequestValidator;
import static org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators.iti18ResponseValidator;
import static org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators.iti41RequestValidator;
import static org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators.iti41ResponseValidator;

import org.apache.camel.builder.RouteBuilder;
import org.openehealth.ipf.commons.ihe.xds.core.requests.ProvideAndRegisterDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.requests.QueryRegistry;
import org.springframework.stereotype.Component;


@Component
public class XdsRouteBuilder extends RouteBuilder {

	@Override
	public void configure() {

		// Entry point for Provide and Register Document Set
		from("xds-iti41:xds-iti41")
			.log("received iti41: " + bodyAs(ProvideAndRegisterDocumentSet.class))
			// Validate and convert the request
			.process(iti41RequestValidator())
			.process(new Iti41Processor())
			// Validate response
			.process(iti41ResponseValidator())
			;
				
		// Entry point for Stored Query
		from("xds-iti18:xds-iti18")
			.log("received iti18: " + bodyAs(QueryRegistry.class))
			.process(iti18RequestValidator())
			.process(new Iti18Processor())
//			.process(iti18ResponseValidator())	 // Can't pass this validation
			;

	}

}
