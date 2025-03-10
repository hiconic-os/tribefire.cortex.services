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

import java.util.Collection;

import com.braintribe.model.accessdeployment.IncrementalAccess;
import com.braintribe.model.accessdeployment.smood.CollaborativeSmoodAccess;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.processing.query.fluent.EntityQueryBuilder;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.model.query.EntityQuery;
import com.braintribe.model.user.User;
import com.braintribe.product.rat.imp.ImpApi;
import com.braintribe.qa.tribefire.qatests.QaTestHelper;
import com.braintribe.testing.internal.tribefire.tests.AbstractTribefireQaTest;

public class AbstractPersistenceTest extends AbstractTribefireQaTest {
	public final static String FAMILY_ACCESS_SUFFIX = "FamilyAccess";
	public final static String FAMILY_MODEL_INFIX = "Family";
	public static final String TEST_USER_NAME = "test-user";


	protected GmMetaModel createFamilyModel(ImpApi imp) {
		return QaTestHelper.createFamilyModel(imp, modelName());
	}

	protected CollaborativeSmoodAccess createAndDeployFamilyAccess(ImpApi imp) {
		return QaTestHelper.createAndDeployTestFamilySmoodAccess(imp, modelName(FAMILY_MODEL_INFIX), name(FAMILY_ACCESS_SUFFIX));
	}

	protected CollaborativeSmoodAccess createAndDeployFamilyAccessWithTimestamp(ImpApi imp) {
		return QaTestHelper.createAndDeployTestFamilySmoodAccess(imp, modelNameWithTimestamp(FAMILY_MODEL_INFIX),
				nameWithTimestamp(FAMILY_ACCESS_SUFFIX));
	}

	public void eraseFamilyAccessesAndModel() {
		ImpApi imp = apiFactory().build();

		imp.model().allLike("*" + testId() + FAMILY_MODEL_INFIX + "*").deleteRecursively();

		Collection<IncrementalAccess> familyAccesses = imp.deployable().access().findAll("*" + testId() + FAMILY_ACCESS_SUFFIX + "*");

		if (!familyAccesses.isEmpty()) {
			imp.service().undeployRequest(familyAccesses).call();
			imp.deployable().access().with(familyAccesses).delete();
		}

		imp.commit();
	}
	
	protected static void ensureTestUser() {
		PersistenceGmSession authAccessSession = apiFactory().newSessionForAccess("auth");
		
		EntityQuery userQuery = EntityQueryBuilder.from(User.T).where().property(User.name).eq(TEST_USER_NAME).done();
		User testUser = authAccessSession.query().entities(userQuery).unique();
		
		if (testUser != null)
			return;
		
		testUser = authAccessSession.create(User.T);
		testUser.setName(TEST_USER_NAME);
		testUser.setPassword(TEST_USER_NAME);
		
		authAccessSession.commit();
	}
}
