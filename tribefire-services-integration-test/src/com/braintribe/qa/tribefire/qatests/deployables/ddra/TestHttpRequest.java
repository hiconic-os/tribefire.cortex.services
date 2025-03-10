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
package com.braintribe.qa.tribefire.qatests.deployables.ddra;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;

import com.braintribe.codec.marshaller.api.Marshaller;
import com.braintribe.codec.marshaller.json.JsonStreamMarshaller;

public class TestHttpRequest {

	public static final String JSON = "application/json";

	public static final Marshaller MARSHALLER = new JsonStreamMarshaller();

	private final HttpRequestBase request;

	private final HttpClient client;

	public TestHttpRequest(HttpClient client, HttpRequestBase request) {
		this.client = client;
		this.request = request;
	}

	public TestHttpRequest accept(String... mimeTypes) {
		return header("Accept", mimeTypes);
	}

	public TestHttpRequest contentType(String mimeType) {
		return header("Content-Type", mimeType);
	}

	public TestHttpRequest header(String name, String... values) {
		for (String value : values) {
			request.addHeader(name, value);
		}
		return this;
	}

	public TestHttpRequest urlParameter(String name, String... values) {
		URIBuilder builder = new URIBuilder(request.getURI());
		for (String value : values) {
			builder.addParameter(name, value);
		}
		setURI(builder);
		return this;
	}

	public TestHttpRequest path(String path) {
		URIBuilder builder = new URIBuilder(request.getURI());
		builder.setPath(concatPaths(builder.getPath(), path));
		setURI(builder);
		return this;
	}

	private String concatPaths(String builderPath, String path) {
		return builderPath != null ? builderPath + path : path;
	}

	public TestHttpRequest body(String content) {
		return body(content.getBytes());
	}

	public TestHttpRequest body(byte[] content) {
		((HttpEntityEnclosingRequestBase) request).setEntity(createHttpEntity(content));
		return this;
	}

	private HttpEntity createHttpEntity(byte[] content) {
		return new ByteArrayEntity(content);
	}

	public HttpRequestBase getRequest() {
		return request;
	}

	private void setURI(URIBuilder builder) {
		try {
			request.setURI(builder.build());
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	public TestHttpResponse execute() {
		try {
			return new TestHttpResponse(client.execute(request));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public <T> T execute(int expectedResponseCode) {
		TestHttpResponse response = null;
		try {
			response = execute();
			assertThat(response.getStatusCode()).as("Wrong status code from URL: " + request.getURI().toString()).isEqualTo(expectedResponseCode);
			return response.getContent();

		} finally {
			if (response != null)
				response.consumeEntity();
		}
	}
}
