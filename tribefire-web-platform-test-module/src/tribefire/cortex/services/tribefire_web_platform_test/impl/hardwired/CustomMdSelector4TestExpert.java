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
package tribefire.cortex.services.tribefire_web_platform_test.impl.hardwired;

import java.util.Collection;
import java.util.Collections;

import com.braintribe.model.processing.meta.cmd.context.SelectorContext;
import com.braintribe.model.processing.meta.cmd.context.SelectorContextAspect;
import com.braintribe.model.processing.meta.cmd.context.experts.CmdSelectorExpert;

import tribefire.cortex.services.model.test.hardwired.mdselector.CustomMdSelector4Test;

/**
 * @author peter.gazdik
 */
public class CustomMdSelector4TestExpert implements CmdSelectorExpert<CustomMdSelector4Test> {

	@Override
	public Collection<Class<? extends SelectorContextAspect<?>>> getRelevantAspects(CustomMdSelector4Test selector) throws Exception {
		return Collections.emptyList();
	}

	@Override
	public boolean matches(CustomMdSelector4Test selector, SelectorContext context) throws Exception {
		return selector.getIsActive();
	}

}
