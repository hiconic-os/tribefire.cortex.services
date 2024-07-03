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

import com.braintribe.model.user.User;
import com.braintribe.model.usersession.UserSession;

/**
 * Similar to {@link ParallelTestA}.
 */
public class ParallelTestC extends AbstractParallelExecutionTest {

	@Test
	public void testUsers() {
		assertExists(User.T, "user-sessions");
	}

	@Test
	public void testUserSessions() {
		assertExists(UserSession.T, "user-sessions");
	}
}
