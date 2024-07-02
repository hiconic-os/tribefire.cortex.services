// ============================================================================
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
// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
// 
// This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License along with this library; See http://www.gnu.org/licenses/.
// ============================================================================
package tribefire.cortex.testing.processing.services;

import com.braintribe.logging.Logger;
import com.braintribe.model.processing.service.api.ServiceProcessor;
import com.braintribe.model.processing.service.api.ServiceRequestContext;
import com.braintribe.qa.cartridge.main.model.service.TestEchoRequest;
import com.braintribe.qa.cartridge.main.model.service.TestEchoResponse;
import com.braintribe.utils.genericmodel.GMCoreTools;

public class TestEchoServiceProcessor implements ServiceProcessor<TestEchoRequest, TestEchoResponse> {
//public class TestEchoServiceProcessor implements AccessRequestProcessor<TestEchoRequest, TestEchoResponse> {

	private static Logger logger = Logger.getLogger(TestEchoServiceProcessor.class);

	@Override
	public TestEchoResponse process(ServiceRequestContext requestContext, TestEchoRequest request) {

		// log detailed info on trace level
		// (instead of checking if logger.isTraceEnabled, we "guard" using lambda expression)
		logger.trace(() -> "Processing request " + GMCoreTools.getDescription(request));

		TestEchoResponse response = TestEchoResponse.T.create();
		response.setEcho(request.getText());

		return response;
	}

//	@Override
//	public TestEchoResponse process(AccessRequestContext<TestEchoRequest> context) throws Exception {
//		// log detailed info on trace level
//		// (instead of checking if logger.isTraceEnabled, we "guard" using lambda expression)
//		TestEchoRequest request = context.getRequest();
//		logger.trace(() -> {
//			return "Processing request " + GMCoreTools.getDescription(request);
//		});
//
//		TestEchoResponse response = TestEchoResponse.T.create();
//		response.setEcho(request.getText());
//
//		return response;
//	}

}
