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

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.braintribe.model.accessdeployment.smood.CollaborativeSmoodAccess;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.processing.query.fluent.SelectQueryBuilder;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.model.query.SelectQuery;
import com.braintribe.product.rat.imp.ImpApi;
import com.braintribe.qa.cartridge.main.model.data.Address;
import com.braintribe.testing.internal.tribefire.tests.AbstractTribefireQaTest;

/**
 * This class tests the creation and persistence of a {@code SmoodAccess} based on the {@code DataModel}, which was
 * synchronized from the {@code MainQaCartridge}.
 *
 *
 */
public class CartridgeModelSmoodPersistenceTest extends AbstractTribefireQaTest {

	protected final String DATA_MODEL_NAME = Address.T.getModel().name();

	@Before
	@After
	public void cleanup() {
		eraseTestEntities();
	}

	@Test
	public void testPersistenceModelWithSmood() {
		logger.info("Starting DevQA-test: cartridge detection and cartridge model synchronization and persist instances to Smood access ...");

		ImpApi imp = apiFactory().build();

		GmMetaModel dataModel = imp.model(DATA_MODEL_NAME).get();
		String accessName = nameWithTimestamp("Access");
		CollaborativeSmoodAccess smoodAccess = imp.deployable().access().createCsa(accessName, accessName, dataModel).get();
		imp.commit();
		imp.service().deployRequest(smoodAccess).call();

		PersistenceGmSession crudSession = apiFactory().newSessionForAccess(smoodAccess.getExternalId());
		PersistenceGmSession accessCheckerSession = apiFactory().newSessionForAccess(smoodAccess.getExternalId());

		Address address = crudSession.create(Address.T);
		String street = "Address " + smoodAccess.getExternalId();
		address.setStreet(street);
		crudSession.commit();

		SelectQuery selectQuery = new SelectQueryBuilder().from(Address.class, "a").done();
		List<Address> result = accessCheckerSession.query().select(selectQuery).list();

		assertThat(result.size()).isEqualTo(1);
		assertThat(result.get(0).getStreet()).isEqualTo(street);

		logger.info("All assertions have completed succefully!");
		logger.info("Completed DevQA-test: cartridge detection and cartridge model synchronization and persist instances to Smood access.");
	}
}
