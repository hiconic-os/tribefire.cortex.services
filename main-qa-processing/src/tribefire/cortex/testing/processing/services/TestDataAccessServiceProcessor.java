// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ============================================================================
package tribefire.cortex.testing.processing.services;

import com.braintribe.logging.Logger;
import com.braintribe.model.processing.accessrequest.api.AccessRequestContext;
import com.braintribe.model.processing.accessrequest.api.AccessRequestProcessor;
import com.braintribe.model.processing.query.fluent.EntityQueryBuilder;
import com.braintribe.model.query.EntityQuery;
import com.braintribe.qa.cartridge.main.model.data.Address;
import com.braintribe.qa.cartridge.main.model.service.TestAccessDataRequest;
import com.braintribe.qa.cartridge.main.model.service.TestAccessDataResponse;
import com.braintribe.utils.genericmodel.GMCoreTools;

public class TestDataAccessServiceProcessor implements AccessRequestProcessor<TestAccessDataRequest, TestAccessDataResponse> {

	private static Logger logger = Logger.getLogger(TestDataAccessServiceProcessor.class);

	@Override
	public TestAccessDataResponse process(AccessRequestContext<TestAccessDataRequest> context) {
		// log detailed info on trace level
		// (instead of checking if logger.isTraceEnabled, we "guard" using lambda expression)
		TestAccessDataRequest request = context.getRequest();
		logger.trace(() -> "Processing request " + GMCoreTools.getDescription(request));

		TestAccessDataResponse response = TestAccessDataResponse.T.create();
		
		EntityQuery query = EntityQueryBuilder.from(Address.T).where().property(Address.street).eq(request.getText()).done();
		Address address = context.getSession().query().entities(query).first();
		address.setStreetNumber(17);
		context.getSession().commit();
		response.setEcho(address.getStreetNumber().toString());
		return response;
	}

}
