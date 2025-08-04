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
package com.braintribe.qa.tribefire.qatests.webauth;

import java.net.HttpCookie;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;
import java.util.Optional;

import org.junit.BeforeClass;
import org.junit.Test;

import com.braintribe.codec.marshaller.api.GmDeserializationOptions;
import com.braintribe.codec.marshaller.api.GmSerializationOptions;
import com.braintribe.codec.marshaller.api.TypeExplicitness;
import com.braintribe.codec.marshaller.api.TypeExplicitnessOption;
import com.braintribe.codec.marshaller.json.JsonStreamMarshaller;
import com.braintribe.gm.model.reason.Maybe;
import com.braintribe.gm.model.reason.Reason;
import com.braintribe.gm.model.reason.essential.InternalError;
import com.braintribe.gm.model.reason.essential.InvalidArgument;
import com.braintribe.gm.model.security.reason.InvalidCredentials;
import com.braintribe.gm.model.security.reason.MissingSession;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.processing.service.common.FailureCodec;
import com.braintribe.model.securityservice.web.GetWebAuthorization;
import com.braintribe.model.securityservice.web.UserPassWebAuthenticate;
import com.braintribe.model.securityservice.web.WebAuthorization;
import com.braintribe.model.service.api.ServiceRequest;
import com.braintribe.model.service.api.result.Failure;
import com.braintribe.model.service.api.result.Neutral;
import com.braintribe.model.service.api.result.ServiceResult;
import com.braintribe.model.service.api.result.Unsatisfied;
import com.braintribe.model.user.User;
import com.braintribe.product.rat.imp.ImpApi;
import com.braintribe.testing.internal.tribefire.tests.AbstractTribefireQaTest;
import com.braintribe.testing.junit.assertions.assertj.core.api.Assertions;

/**
 * This class tests the Resource upload and download.
 *
 */
public class WebAuthorizationTest extends AbstractTribefireQaTest {
	private static ImpApi globalImp;

	@BeforeClass
	public static void beforeClass() {
		globalImp = apiFactory().build();
	}
	
	@Test
	public void testUserPassWebAuthenticateSuccess() throws Exception {
		testUserPassWebAuthenticate("cortex", "cortex", 200, null);
	}
	
	@Test
	public void testUserPassWebAuthenticateInvalidArgument() throws Exception {
		testUserPassWebAuthenticate(null, "cortex", 400, InvalidArgument.T);
		testUserPassWebAuthenticate("", "cortex", 400, InvalidArgument.T);
		testUserPassWebAuthenticate(" ", "cortex", 400, InvalidArgument.T);
		testUserPassWebAuthenticate("user", null, 400, InvalidArgument.T);
	}
	
	@Test
	public void testUserPassWebAuthenticateInvalidCredentials() throws Exception {
		testUserPassWebAuthenticate("vortex", "vortex", 401, InvalidCredentials.T);
	}
	
	@Test
	public void testGetWebAuthorizationWithoutCookie() throws Exception {
		testGetWebAuthorization(null, 401, MissingSession.T);
	}
	
	@Test
	public void testGetWebAuthorizationWithInvalidCookie() throws Exception {
		testGetWebAuthorization("xyz", 401, InvalidCredentials.T);
	}
	
	@Test
	public void testGetWebAuthorizationWithValidCookie() throws Exception {
		String cortexUserSessionId = globalImp.session().getSessionAuthorization().getSessionId();
		testGetWebAuthorization(cortexUserSessionId, 200, null);
	}
	
	
	private void testGetWebAuthorization(String sessionCookieValue, int expectedStatusCode, EntityType<? extends Reason> expectedReasonType) throws Exception {
		String path = "/api/v1/web-auth/authorization";
		
		GetWebAuthorization getWebAuthorization = GetWebAuthorization.T.create();
		
		RestCallResult<WebAuthorization> result = call(path, getWebAuthorization, sessionCookieValue);
		
		Assertions.assertThat(result.statusCode()).isEqualTo(expectedStatusCode);
		
		Assertions.assertThat(result.responseMaybe().isSatisfied()).isEqualTo(expectedReasonType == null);

		if (expectedStatusCode == 200) {
			WebAuthorization authorization = result.responseMaybe().get();
			
			User user = authorization.getUser();
			
			Assertions.assertThat(user).isNotNull();
			Assertions.assertThat(user.getPassword()).isNull();
			Assertions.assertThat(authorization.getEffectiveRoles()).contains("tf-admin", "$all");
		}
	}
	
	private void testUserPassWebAuthenticate(String username, String password, int expectedStatusCode, EntityType<? extends Reason> expectedReasonType) throws Exception {
		String path = "/api/v1/web-auth/user-pass-authenticate";
		
		UserPassWebAuthenticate login = UserPassWebAuthenticate.T.create();
		login.setUser(username);
		login.setPassword(password);
		
		RestCallResult<WebAuthorization> result = call(path, login, null);
		
		Assertions.assertThat(result.statusCode()).isEqualTo(expectedStatusCode);
		
		Assertions.assertThat(result.responseMaybe().isSatisfied()).isEqualTo(expectedReasonType == null);

		if (expectedStatusCode == 200) {
			Assertions.assertThat(result.sessionCookieValue()).isNotNull();
			WebAuthorization authorization = result.responseMaybe().get();
			
			User user = authorization.getUser();
			
			Assertions.assertThat(user).isNotNull();
			Assertions.assertThat(user.getPassword()).isNull();
			Assertions.assertThat(authorization.getEffectiveRoles()).contains("tf-admin", "$all");
		}
		else {
			Assertions.assertThat(result.sessionCookieValue()).isNull();
		}
	}
	
	record RestCallResult<O>(Maybe<O> responseMaybe, HttpResponse<String> httpResponse) {
		int statusCode() {
			return httpResponse().statusCode();
		}
		
		String sessionCookieValue() {
			return Optional.ofNullable(sessionCookie()).map(HttpCookie::getValue).orElse(null);
		}
		
		HttpCookie sessionCookie() {
			String expectedCookieName = "tfsessionId";
			
			List<String> allCookieHeaderValues = httpResponse().headers().allValues("Set-Cookie");
			for (String cookieHeaderValue : allCookieHeaderValues) {
				List<HttpCookie> list = HttpCookie.parse(cookieHeaderValue);
				for (HttpCookie cookie : list) {
					String cookieName = cookie.getName();
					if (expectedCookieName.equals(cookieName)) {
						return cookie;
					}
				}
			}
			return null;
		}
	}
	
	public <O> RestCallResult<O> call(String endpointPath, ServiceRequest request, String sessionCookieValue)
			throws Exception {
		JsonStreamMarshaller marshaller = new JsonStreamMarshaller();
		String json = marshaller.encode(request, //
				GmSerializationOptions.deriveDefaults().set(TypeExplicitnessOption.class, TypeExplicitness.never).build());
		
		HttpClient httpClient = HttpClient.newBuilder().build();
		
		String servicesUrl = globalImp.getUrl();
		
		URI uri = URI.create(servicesUrl + endpointPath);
		Builder requestBuilder = HttpRequest.newBuilder(uri) //
				.header("Content-Type", "application/json") //
				.header("Accept", "application/json");
		
		if (sessionCookieValue != null)
			requestBuilder.setHeader("Cookie", "tfsessionId=" + sessionCookieValue);
		
		HttpRequest httpRequest = requestBuilder //
				.POST(BodyPublishers.ofString(json)).build();
		
		HttpResponse<String> response = httpClient.send(httpRequest, BodyHandlers.ofString());
		
		String body = response.body();
		
		int sc = response.statusCode();
		
		if (sc == 404)
			throw new IllegalStateException("unexpected 404");
		if (sc == 204)
			return new RestCallResult<O>(Maybe.complete((O)Neutral.NEUTRAL), response);
		else if (sc == 200)
			return new RestCallResult<O>(
					Maybe.complete((O)marshaller.decode(body, GmDeserializationOptions.deriveDefaults().setInferredRootType(request.entityType().getEvaluatesTo()).build())),
					response
			);
		else {
			ServiceResult value = (ServiceResult) marshaller.decode(body, GmDeserializationOptions.deriveDefaults().setInferredRootType(ServiceResult.T).build());
			
			if (value instanceof Failure failure) {
				Maybe<O> maybe = InternalError.from(FailureCodec.INSTANCE.decode(failure), "Server Error").asMaybe();
				return new RestCallResult<O>(maybe, response);
			}
			else if (value instanceof Unsatisfied unsatisfied) 
				return new RestCallResult<O>(unsatisfied.toMaybe(), response);
			else
				throw new IllegalStateException("unexpected error body: " + body);
		}
	}

}
