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

import static org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators.iti18RequestValidator;
import static org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators.iti18ResponseValidator;
import static org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators.iti41RequestValidator;
import static org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators.iti41ResponseValidator;
import static org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators.iti43RequestValidator;
import static org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators.iti43ResponseValidator;

import org.apache.camel.builder.RouteBuilder;
import org.openehealth.ipf.commons.ihe.xds.core.requests.ProvideAndRegisterDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.requests.QueryRegistry;
import org.openehealth.ipf.commons.ihe.xds.core.requests.RetrieveDocumentSet;
import org.springframework.stereotype.Component;

/**
 * Building routes.
 *  
 * @author Tomasz Judycki
 */
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
			.process(iti18ResponseValidator())
			;

		// Entry point for iti43 Retrieve Document Set
		from("xds-iti43:xds-iti43")
			.log("received iti43: " + bodyAs(RetrieveDocumentSet.class))
			.process(iti43RequestValidator())
			.process(new Iti43Processor())
			.process(iti43ResponseValidator())
			;
	}

}
