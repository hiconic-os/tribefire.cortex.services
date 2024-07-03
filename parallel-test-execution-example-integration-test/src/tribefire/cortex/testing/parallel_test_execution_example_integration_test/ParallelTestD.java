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

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;

import com.braintribe.model.accessdeployment.IncrementalAccess;
import com.braintribe.model.deployment.Deployable;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.meta.GmBaseType;
import com.braintribe.model.meta.GmEntityType;
import com.braintribe.model.meta.GmEnumType;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.meta.GmProperty;
import com.googlecode.junittoolbox.ParallelParameterized;

/**
 * Similar to {@link ParallelTestA}, but uses parameterized tests.
 */
@RunWith(ParallelParameterized.class)
public class ParallelTestD extends AbstractParallelExecutionTest {

	EntityType<?> entityType;
	String accessId;

	public ParallelTestD(EntityType<?> entityType, String accessId) {
		this.entityType = entityType;
		this.accessId = accessId;
	}

	@Test
	public void test() {
		assertExists(entityType, accessId);
	}

	@Parameters(name = "Test {index}: Type {0} on access {1}")
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] {
			{ Deployable.T, "cortex" },
			{ IncrementalAccess.T, "cortex" },
			{ GmMetaModel.T, "cortex" },
			{ GmEntityType.T, "cortex" },
			{ GmEnumType.T, "cortex" },
			{ GmBaseType.T, "cortex" },
			{ GmProperty.T, "cortex" }
		});
	}

}
