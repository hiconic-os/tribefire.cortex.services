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
package com.braintribe.qa.tribefire.qatests.cartridges;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.braintribe.model.deployment.DeploymentStatus;
import com.braintribe.model.generic.session.exception.GmSessionException;
import com.braintribe.product.rat.imp.ImpApi;
import com.braintribe.qa.cartridge.main.model.deployment.terminal.TestBasicTemplateBasedApp;
import com.braintribe.testing.category.KnownIssue;
import com.braintribe.testing.internal.tribefire.tests.AbstractTribefireQaTest;
import com.braintribe.utils.DOMTools;

/**
 * This class creates a web terminal based on a <code>Velocity</code> context (i.e.
 * <code>BasicTemplateBasedServlet</code>) and tests the server-client HTTP communication.
 *
 */
@Category(KnownIssue.class) // TODO: Resolve issue
public class CartridgeAppTest extends AbstractTribefireQaTest {

	@Before
	@After
	public void cleanup() {
		eraseTestEntities();
	}

	@Test
	public void testCartridgeApp() throws GmSessionException, IOException {
		logger.info("Starting DevQA-test: creating a web terminal (app) in a cartridge...");

		ImpApi imp = apiFactory().build();

		String timestamp = "" + System.currentTimeMillis();
		String currentAppName = name(timestamp + "App");

		// @formatter:off
		TestBasicTemplateBasedApp templateBasedApp = imp.deployable()
				.webTerminal()
				.create(TestBasicTemplateBasedApp.T, currentAppName, currentAppName, currentAppName)
				.get();
		// @formatter:on

		templateBasedApp.setTimestamp(timestamp);

		imp.commit();
		imp.service().deployRequest(templateBasedApp).call();

		assertThat(templateBasedApp.getDeploymentStatus()).isEqualTo(DeploymentStatus.deployed);
		logger.info("Creating an HTTP request to the app...");
		String appURL = imp.getUrl() + "/component/" + currentAppName;

		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(appURL);

		HttpResponse response = client.execute(request);

		assertThat(response).isNotNull();
		assertThat(response.getStatusLine().getStatusCode()).as("Wrong HTTP response code for URL: %s", appURL).isEqualTo(200);

		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}

		logger.info("Checking HTTP response content...");
		Document document = DOMTools.stringToDocument(result.toString());
		Element timestampElement = DOMTools.getElementByXPath(document.getDocumentElement(), "//div[@id='timestamp']");
		assertThat(timestampElement.getTextContent().trim()).isEqualTo(timestamp);

		logger.info("All assertions have completed succefully!");
		logger.info("Completed DevQA-test: creating a web terminal (app) in a cartridge.");
	}

}
