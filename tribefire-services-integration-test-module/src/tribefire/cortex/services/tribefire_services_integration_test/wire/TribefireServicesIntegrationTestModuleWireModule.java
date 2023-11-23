// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
// 
// This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License along with this library; See http://www.gnu.org/licenses/.
// ============================================================================
package tribefire.cortex.services.tribefire_services_integration_test.wire;

import static java.util.Collections.singletonList;

import java.util.List;

import com.braintribe.wire.api.module.WireModule;

import tribefire.cortex.services.tribefire_services_integration_test.wire.space.TribefireServicesIntegrationTestModuleSpace;
import tribefire.cortex.testing.junit.wire.JUnitTestingWireModule;
import tribefire.module.wire.contract.StandardTribefireModuleWireModule;
import tribefire.module.wire.contract.TribefireModuleContract;

public enum TribefireServicesIntegrationTestModuleWireModule implements StandardTribefireModuleWireModule {

	INSTANCE;

	@Override
	public Class<? extends TribefireModuleContract> moduleSpaceClass() {
		return TribefireServicesIntegrationTestModuleSpace.class;
	}

	@Override
	public List<WireModule> dependencies() {
		return singletonList(JUnitTestingWireModule.INSTANCE);
	}
	
}
