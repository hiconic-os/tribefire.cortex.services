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
package com.braintribe.qa.tribefire.qatests.deployables;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Before;

import com.braintribe.model.accessdeployment.smood.CollaborativeSmoodAccess;
import com.braintribe.model.extensiondeployment.StateChangeProcessor;
import com.braintribe.model.extensiondeployment.meta.OnChange;
import com.braintribe.model.generic.reflection.SimpleTypes;
import com.braintribe.model.generic.session.exception.GmSessionException;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.product.rat.imp.ImpApi;
import com.braintribe.product.rat.imp.commons.CommonMetaData;
import com.braintribe.qa.tribefire.test.Invoice;
import com.braintribe.testing.internal.tribefire.tests.AbstractTribefireQaTest;

public class StateChangeProcessorTest extends AbstractTribefireQaTest {

	/*
	private static String beanshellscript = "date = new java.text.SimpleDateFormat(\"MM/dd/yyyy\").format(new Date());"
			+ "valueOfChangedProperty = $.getEntity().getTotal();"
			+ "$.getEntity().setInvoiceState($.getAffectedProperty().getName() + \" was changed to \" + valueOfChangedProperty + \" on \" + date);";

	// We need to assure that we are using javascript. Hence the toLocaleDateString
	private static String javascript = "var d = new Date();date = d.toLocaleDateString();" + "valueOfChangedProperty = $.getEntity().getTotal();"
			+ "$.getEntity().setInvoiceState($.getAffectedProperty().getName() + \" was changed to \" + valueOfChangedProperty + \" on \" + date);";
	 */
	
	private String MODEL_NAME = null;

	@Before
	public void setupScriptStateChangeProcessorTest() throws GmSessionException {
		eraseTestEntities();

		// TODO use model name without timestamp after bug is fixed
		MODEL_NAME = modelNameWithTimestamp("Invoice");

		ImpApi imp = apiFactory().build();

		// @formatter:off
		GmMetaModel currentModel = imp.model().create(MODEL_NAME).get();
		imp.model()
			.entityType()
				.create(Invoice.T, currentModel)
				.addProperty(Invoice.invoiceState, SimpleTypes.TYPE_STRING)
				.addProperty(Invoice.total,	SimpleTypes.TYPE_DOUBLE);
		// @formatter:on

		imp.commit();
	}


	/*
	private void addOnChangeMetadataAndAssertProcessor(ImpApi imp, StateChangeProcessor stateChangeProcessor) {
		OnChange onChange = imp.session().create(CommonMetaData.onChange);
		imp.model(MODEL_NAME).metaDataEditor().onEntityType(Invoice.T).addPropertyMetaData(Invoice.total, onChange);

		onChange.setProcessor(stateChangeProcessor);

		GmMetaModel currentModel = imp.model(MODEL_NAME).get();
		CollaborativeSmoodAccess smoodAccess = imp.deployable().access().createCsa(name("Access"), nameWithTimestamp("Access"), currentModel).get();
		imp.commit();
		imp.service().deployRequest(smoodAccess).call();

		PersistenceGmSession accessSession = imp.switchToAccess(smoodAccess.getExternalId()).session();

		Invoice invoice = accessSession.create(Invoice.T);
		invoice.setTotal(1.0);
		accessSession.commit();

		Invoice invoiceRefreshed = accessSession.query().entity(invoice).refresh();
		assertThat(invoiceRefreshed.getInvoiceState()).isNotNull();
		assertThat(invoiceRefreshed.getInvoiceState()).startsWith("total was changed to 1");

		logger.info("All assertions have completed succefully!");
		logger.info("Completed DevQA-test: Script State Change Processors test.");
	}*/

	@After
	public void tearDown() {
		eraseTestEntities();
	}

}
