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

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.braintribe.model.resourceapi.stream.GetBinary;
import com.braintribe.testing.category.KnownIssue;
import com.braintribe.testing.internal.tribefire.helpers.http.HttpPostHelperEx;

public class DdraStreamingSystemUserTest extends AbstractDdraStreamingTest {

	@BeforeClass
	public static void authenticate() {
		domainId = "cortex";
		try {
			Map<String, Object> authRequest = new HashMap<>();
			authRequest.put("user", "cortex");
			authRequest.put("password", "cortex");
			HttpPostHelperEx httpHelper = new HttpPostHelperEx(TRIBEFIRE_SERVICES_URL + "/api/v1/authenticate", authRequest);
	
			String result = httpHelper.getContent();
			System.out.println(result);
			sessionId = result.replaceAll("\"", "").trim();
		} catch (IOException e) {
			throw new RuntimeException("Could not authenticate in tribefire via rest", e);
		}
	}

	
	@Test
	// NOR 2.Dec, 2020: Security fix was temporarily disabled.
	@Category(KnownIssue.class)
	public void executeBinaryRequestTest() {
		// Refuse to access files outside FSBP's base path (expect 400 response)
		factory.get() //
			.path(requestUrlPrefixWithDomain() + GetBinary.T.getTypeSignature()) //
			.urlParameter("serviceId", "binaryProcessor.fileSystem") //
			.urlParameter("resource", "@r") //
			.urlParameter("@s", "com.braintribe.model.resource.source.FileSystemSource") //
			.urlParameter("s.path", "../../setup/data/config.json") //
			.urlParameter("r.resourceSource", "@s") //
			.urlParameter("downloadResource", "true") //
			.urlParameter("projection", "resource") //
			.urlParameter("domainId", domainId) //
			.urlParameter("sessionId", sessionId)
			.execute(400);
	}
}