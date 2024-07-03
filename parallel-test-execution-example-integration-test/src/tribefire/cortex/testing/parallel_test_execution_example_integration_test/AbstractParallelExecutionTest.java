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
package tribefire.cortex.testing.parallel_test_execution_example_integration_test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.processing.query.fluent.EntityQueryBuilder;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSessionFactory;
import com.braintribe.product.rat.imp.ImpApi;
import com.braintribe.testing.internal.tribefire.tests.AbstractTribefireQaTest;
import com.braintribe.utils.CommonTools;

/**
 * A simple abstract test class which is used to implement example integration tests. These tests take a few seconds to
 * run and thus can be used to demonstrate/test parallel integration test execution, see method
 * {@link #assertExists(EntityType, String)}.
 * <p>
 * Tribefire Services URL and credentials can be set via system properties:<br>
 * qa.force.url, qa.force.username, qa.force.password<br>
 * ... or via environment variables: <br>
 * QA_FORCE_URL, QA_FORCE_USERNAME, QA_FORCE_PASSWORD
 */
public abstract class AbstractParallelExecutionTest extends AbstractTribefireQaTest {

	protected ImpApi globalImp;
	protected PersistenceGmSessionFactory globalCortexSessionFactory;
	protected int fakeCheckDelayCount;
	protected int fakeCheckDelayInMilliseconds;
	
	protected AbstractParallelExecutionTest() {
		globalImp = apiFactory().build();
		globalCortexSessionFactory = apiFactory().buildSessionFactory();
		fakeCheckDelayCount = Integer.parseInt(System.getProperty(AbstractParallelExecutionTest.class.getSimpleName() + ".fakeCheckDelayCount", "5"));
		fakeCheckDelayInMilliseconds = Integer.parseInt(System.getProperty(AbstractParallelExecutionTest.class.getSimpleName() + ".fakeCheckDelayInMilliseconds", "1000"));
	}

	protected PersistenceGmSession newRemoteSession(String accessId) {
		return globalCortexSessionFactory.newSession(accessId);
	}

	<T extends GenericEntity> void assertExists(EntityType<T> entityType, String accessId) {
		String logMessagePrefix = entityType.getShortName() + " Test in access " + accessId + ": ";

		logger.info(logMessagePrefix + "Running test ...");

		logger.info(logMessagePrefix + "Creating remote session ...");
		PersistenceGmSession session = newRemoteSession(accessId);
		logger.info(logMessagePrefix + "Created remote session.");

		logger.info(logMessagePrefix + "Querying for entities of type " + entityType.getTypeSignature() + " ...");
		List<T> entities = session.query().entities(EntityQueryBuilder.from(entityType).done()).list();
		logger.info(logMessagePrefix + "Query finished successfully. Got " + entities.size() + " result(s).");

		logger.info(logMessagePrefix + "Checking results ...");
		assertThat(entities.size()).isGreaterThanOrEqualTo(1);
		logger.info(logMessagePrefix + "Entity count okay.");

		for (int i = 1; i <= fakeCheckDelayCount; i++) {
			CommonTools.sleep(fakeCheckDelayInMilliseconds);
			logger.info(logMessagePrefix + "Fake check " + i + " okay.");
		}

		assertThat(entities).hasOnlyElementsOfTypes(entityType.getJavaType());
		logger.info(logMessagePrefix + "Types okay.");

		logger.info(logMessagePrefix + "Test finished successfully.");
	}
}
