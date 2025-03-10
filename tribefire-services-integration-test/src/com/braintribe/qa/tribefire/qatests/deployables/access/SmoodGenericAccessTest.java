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
package com.braintribe.qa.tribefire.qatests.deployables.access;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.braintribe.model.accessdeployment.IncrementalAccess;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSessionFactory;
import com.braintribe.product.rat.imp.ImpApi;
import com.braintribe.testing.internal.suite.crud.AccessTester;

public class SmoodGenericAccessTest extends AbstractPersistenceTest {

	@Test
	public void test() {
		ImpApi imp = apiFactory().build();

		PersistenceGmSessionFactory factory = apiFactory().buildSessionFactory();

		IncrementalAccess familyAccess = createAndDeployFamilyAccessWithTimestamp(imp);

		logger.info("Created access " + familyAccess.getExternalId());

		AccessTester tester = new AccessTester(familyAccess.getExternalId(), factory, familyAccess.getMetaModel());
		tester.executeTests();
	}

	@Before
	@After
	public void tearDown() {
		eraseTestEntities();
	}
}
