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

import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.processing.query.fluent.EntityQueryBuilder;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.model.query.EntityQuery;
import com.braintribe.model.user.User;
import com.braintribe.product.rat.imp.ImpApi;
import com.braintribe.qa.cartridge.main.model.deployment.access.TestInMemoryAccess;
import com.braintribe.testing.internal.tribefire.tests.AbstractTribefireQaTest;

/**
 * This class tests the CRUD operations on an in memory incremental access of the QaMainCartridge.
 *
 */
public class CartridgeInMemoryAccessCRUDTest extends AbstractTribefireQaTest {

	private static final String USERMODELFULLNAME = "com.braintribe.gm:user-model";

	@After
	@Before
	public void cleanup() {
		eraseTestEntities();
	}

	@Test
	public void testCartridgeInMemoryAccessCRUD() {
		logger.info("Starting DevQA-test: applying CRUD operations on an in memory access of a cartridge...");

		ImpApi imp = apiFactory().build();

		GmMetaModel userModel = imp.model(USERMODELFULLNAME).get();

		String currentAccessName = nameWithTimestamp("Access");
		// @formatter:off
		TestInMemoryAccess inMemoryAccess = imp.deployable()
				.access()
					.createIncremental(TestInMemoryAccess.T, currentAccessName, currentAccessName, userModel)
				.get();
		// @formatter:on

		imp.commit();
		imp.service().deployRequest(inMemoryAccess).call();

		// --------------------------------------------------------------------------------------------------------------------
		logger.info("Checking initial population of access [" + inMemoryAccess.getExternalId() + "] ...");

		PersistenceGmSession crudSession = apiFactory().newSessionForAccess(inMemoryAccess.getExternalId());
		PersistenceGmSession accessCheckerSession = apiFactory().newSessionForAccess(inMemoryAccess.getExternalId());

		EntityQuery query = EntityQueryBuilder.from(User.class).done();
		List<User> users = crudSession.query().entities(query).list();

		assertThat(users).as("Wrong size of initial population in access!").hasSize(3);
		assertThat(users).extracting(User.firstName).as("Wrong initial population in access!").contains("John", "Jane", "Jim");

		logger.info("Checking CREATE operation of a User...");
		User newUser = crudSession.create(User.T);
		newUser.setName("Franz Kafka");
		newUser.setFirstName("Franz");
		crudSession.commit();

		users = accessCheckerSession.query().entities(query).list();
		assertThat(users).as("Wrong size of population after adding new user: %s", newUser.getFirstName()).hasSize(4);
		assertThat(users).extracting(User.firstName).as("Wrong population in access!").contains("John", "Jane", "Jim", "Franz");

		// --------------------------------------------------------------------------------------------------------------------
		logger.info("Checking REMOVE operation of a User...");
		EntityQuery query2 = EntityQueryBuilder.from(User.class).where().property(User.firstName).eq("Jim").done();
		User jim = crudSession.query().entities(query2).first();
		crudSession.deleteEntity(jim);
		crudSession.commit();

		users = accessCheckerSession.query().entities(query).list();
		assertThat(users).as("Wrong size of population after deleting new user: %s", jim.getFirstName()).hasSize(3);
		assertThat(users).extracting(User.firstName).as("Wrong population in access!").contains("John", "Jane", "Franz");

		// --------------------------------------------------------------------------------------------------------------------
		logger.info("Checking UPDATE operation of a User...");
		EntityQuery query3 = EntityQueryBuilder.from(User.class).where().property(User.firstName).eq("Jane").done();
		User jane = crudSession.query().entities(query3).first();
		jane.setFirstName("Paul");
		crudSession.commit();

		users = accessCheckerSession.query().entities(query).list();
		assertThat(users).as("Wrong size of population after updating new user: %s", jane.getFirstName()).hasSize(3);
		assertThat(users).extracting(User.firstName).as("Wrong population in access!").contains("John", "Paul", "Franz");

		logger.info("All assertions have completed succefully!");
		logger.info("Completed DevQA-test: applying CRUD operations on an in memory access of a cartridge...");
	}
}
