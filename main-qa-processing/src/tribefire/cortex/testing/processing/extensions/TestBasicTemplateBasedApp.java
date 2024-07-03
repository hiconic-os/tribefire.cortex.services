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
package tribefire.cortex.testing.processing.extensions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.VelocityContext;

import com.braintribe.cfg.Configurable;
import com.braintribe.cfg.InitializationAware;
import com.braintribe.cfg.Required;
import com.braintribe.model.processing.bootstrapping.TribefireRuntime;
import com.braintribe.web.servlet.BasicTemplateBasedServlet;

public class TestBasicTemplateBasedApp extends BasicTemplateBasedServlet implements InitializationAware {

	private static final long serialVersionUID = -984142689914854564L;
	private static final String TEMPLATE_PATH = "tribefire/cortex/testing/processing/templates/test.html.vm";

	private String timestamp;

	@Required
	@Configurable
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public void postConstruct() {
		setTemplateLocation(TEMPLATE_PATH);
	}

	@Override
	protected VelocityContext createContext(HttpServletRequest request, HttpServletResponse response) {
		VelocityContext context = new VelocityContext();
		context.put("timestamp", timestamp);
		context.put("tribefireRuntime", new TribefireRuntime());
		return context;
	}

}
