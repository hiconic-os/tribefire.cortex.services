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
package com.braintribe.qa.tribefire.qatests.deployables.ddra;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.braintribe.model.resource.source.FileSystemSource;
import com.braintribe.model.resourceapi.stream.GetBinary;
import com.braintribe.product.rat.imp.ImpApi;
import com.braintribe.qa.tribefire.qatests.QaTestHelper;
import com.braintribe.testing.internal.tribefire.helpers.http.HttpPostHelperEx;

public class DdraStreamingSimpleUserTest extends AbstractDdraStreamingTest {

	private static ImpApi imp;

	@BeforeClass
	public static void init() {
		imp = apiFactory().build();

		ensureTestUser();
		ensureTestAccess();
		authenticate();
	}

	@AfterClass
	public static void cleanUp() {
		imp.deployable().access(domainId).delete();
		imp.commit();
	}

	public static void authenticate() {
		try {
			Map<String, Object> authRequest = new HashMap<>();
			authRequest.put("user", TEST_USER_NAME);
			authRequest.put("password", TEST_USER_NAME);
			HttpPostHelperEx httpHelper = new HttpPostHelperEx(TRIBEFIRE_SERVICES_URL + "/api/v1/authenticate", authRequest);
	
			String result = httpHelper.getContent();
			System.out.println(result);
			sessionId = result.replaceAll("\"", "").trim();
		} catch (IOException e) {
			throw new RuntimeException("Could not authenticate in tribefire via rest", e);
		}
	}

	private static void ensureTestAccess() {
		domainId = "ddra-simple-test-access";

		QaTestHelper.ensureSmoodAccess(imp, domainId, imp.model(FileSystemSource.T.getModel().name()).get());
	}

	@Test
	public void executeBinaryRequestTest() {
		// Refuse to access binary requests at all (Expect 403 response)
		factory.get() //
			.path(requestUrlPrefixWithDomain() + GetBinary.T.getTypeSignature()) //
			.urlParameter("serviceId", "binaryProcessor.fileSystem") //
			.urlParameter("resource", "@r") //
			.urlParameter("@s", "com.braintribe.model.resource.source.FileSystemSource") //
			.urlParameter("s.path", "../../setup/data/config.json") //
			.urlParameter("r.resourceSource", "@s") //
			.urlParameter("downloadResource", "true") //
			.urlParameter("domainId", domainId) //
			.urlParameter("sessionId", sessionId)
			.execute(403);
	}

}