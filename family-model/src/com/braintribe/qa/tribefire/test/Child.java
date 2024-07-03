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
package com.braintribe.qa.tribefire.test;

import java.util.List;

import com.braintribe.model.generic.StandardIdentifiable;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.resource.Resource;

public abstract interface Child extends StandardIdentifiable {

	EntityType<Child> T = EntityTypes.T(Child.class);
	public static final String name = "name";
	public static final String tasks = "tasks";
	public static final String logo = "logo";

	public abstract String getName();

	public abstract void setName(String paramString);

	public abstract List<Task> getTasks();

	public abstract void setTasks(List<Task> paramList);

	public abstract Resource getLogo();

	public abstract void setLogo(Resource paramResource);
}
