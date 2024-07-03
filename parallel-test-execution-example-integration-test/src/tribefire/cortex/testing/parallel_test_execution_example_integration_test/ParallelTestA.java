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

import org.junit.Test;

import com.braintribe.model.accessdeployment.IncrementalAccess;
import com.braintribe.model.deployment.Deployable;
import com.braintribe.model.meta.GmMetaModel;

/**
 * Provides example tests which take a few seconds to execute. This can be used to test parallel test execution. For
 * more information see {@link AbstractParallelExecutionTest}.
 */
public class ParallelTestA extends AbstractParallelExecutionTest {

	@Test
	public void testDeployables() {
		assertExists(Deployable.T, "cortex");
	}

	@Test
	public void testIncrementAccesses() {
		assertExists(IncrementalAccess.T, "cortex");
	}

	@Test
	public void testGmMetaModels() {
		assertExists(GmMetaModel.T, "cortex");
	}
}
