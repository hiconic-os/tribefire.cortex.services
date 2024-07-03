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
package tribefire.cortex.testing.main_qa.wire.space;

import com.braintribe.model.processing.deployment.api.binding.DenotationBindingBuilder;
import com.braintribe.qa.cartridge.main.model.deployment.access.TestInMemoryAccess;
import com.braintribe.qa.cartridge.main.model.deployment.service.TestDataAccessServiceProcessor;
import com.braintribe.qa.cartridge.main.model.deployment.service.TestEchoServiceProcessor;
import com.braintribe.qa.cartridge.main.model.deployment.terminal.TestBasicTemplateBasedApp;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;

import tribefire.module.wire.contract.TribefireModuleContract;
import tribefire.module.wire.contract.TribefireWebPlatformContract;

@Managed
public class MainQaModuleSpace implements TribefireModuleContract {

	@Import
	private TribefireWebPlatformContract tfPlatform;

	@Import
	private MainQaDeployablesSpace deployables;

	@Override
	public void bindDeployables(DenotationBindingBuilder bindings) {
		// @formatter:off
		bindings.bind(TestEchoServiceProcessor.T)
				.component(tfPlatform.binders().serviceProcessor())
				.expertSupplier(deployables::testEchoProcessor);
		bindings.bind(TestDataAccessServiceProcessor.T)
				.component(tfPlatform.binders().accessRequestProcessor())
				.expertSupplier(deployables::testDataAccessProcessor);
		bindings.bind(TestInMemoryAccess.T)
				.component(tfPlatform.binders().incrementalAccess())
				.expertFactory(deployables::testInMemoryAccess);
		bindings.bind(TestBasicTemplateBasedApp.T)
				.component(tfPlatform.binders().webTerminal())
				.expertFactory(deployables::testBasicTemplateBasedApp);
		// @formatter:on
	}

}
