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
package com.braintribe.qa.tribefire.qatests.models;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.braintribe.model.cortexapi.model.ValidateModel;
import com.braintribe.model.cortexapi.model.ValidateModelResponse;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.product.rat.imp.ImpApi;
import com.braintribe.product.rat.imp.ImpApiFactory;
import com.braintribe.qa.tribefire.qatests.QaTestHelper;
import com.braintribe.testing.internal.tribefire.tests.AbstractTribefireQaTest;

public class ValidateModelTest extends AbstractTribefireQaTest {
	private ImpApi imp;
	private GmMetaModel familyModel;

	@Before
	public void init() {
		imp = ImpApiFactory.buildWithDefaultProperties();
		familyModel = QaTestHelper.createFamilyModel(imp, modelName("Family"));
	}

	@Test
	public void test() {

		ValidateModel validateModel = ValidateModel.T.create();
		validateModel.setModel(familyModel);

		// @formatter:off
		List<String> responseNotifications = imp.service()
			.withNotificationResponse(validateModel, ValidateModelResponse.T)
			.callAndGetMessages();
		// @formatter:on

		logger.info("Recieved response messages: " + responseNotifications);
		Assertions.assertThat(responseNotifications).hasSize(1);
	}

	@After
	public void cleanUp() {
		eraseTestEntities();
	}
}
