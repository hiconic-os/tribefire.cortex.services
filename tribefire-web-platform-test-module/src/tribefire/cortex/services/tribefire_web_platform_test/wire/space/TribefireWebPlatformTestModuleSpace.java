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
package tribefire.cortex.services.tribefire_web_platform_test.wire.space;

import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;

import tribefire.cortex.services.tribefire_web_platform_test.tests.PlatformHolder;
import tribefire.cortex.services.tribefire_web_platform_test.tests.accessory.ModelAccessoryFactoryTests;
import tribefire.cortex.services.tribefire_web_platform_test.tests.hardwired.HardwiredBindOnConfigurationModelTests;
import tribefire.cortex.services.tribefire_web_platform_test.tests.hardwired.HardwiredCmdSelectorExpertTests;
import tribefire.cortex.services.tribefire_web_platform_test.tests.hardwired.HardwiredDenotationTransformerTests;
import tribefire.cortex.services.tribefire_web_platform_test.tests.hardwired.HardwiredDeployablesTests;
import tribefire.cortex.testing.junit.wire.space.JUnitTestRunnerModuleSpace;
import tribefire.module.api.InitializerBindingBuilder;
import tribefire.module.wire.contract.TribefireWebPlatformContract;

/**
 * @see JUnitTestRunnerModuleSpace
 */
@Managed
public class TribefireWebPlatformTestModuleSpace extends JUnitTestRunnerModuleSpace {

	@Import
	public TribefireWebPlatformContract tfPlatform;

	@Override
	public void bindHardwired() {
		HardwiredDeployablesTests.bindHardwired(tfPlatform);
		HardwiredBindOnConfigurationModelTests.bindHardwired(tfPlatform);
		HardwiredCmdSelectorExpertTests.bindHardwired(tfPlatform);
		HardwiredDenotationTransformerTests.bindHardwired(tfPlatform);
	}

	@Override
	public void bindInitializers(InitializerBindingBuilder bindings) {
		ModelAccessoryFactoryTests.bindInitializers(bindings);
		HardwiredCmdSelectorExpertTests.bindInitializers(bindings);
	}

	@Override
	public void onAfterBinding() {
		/* This only works because this module is also the runner module, thus this loaded PlatformHolder is also used when the tests are running */
		PlatformHolder.platformContract = this.tfPlatform;
	}

}
