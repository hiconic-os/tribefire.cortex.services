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

import java.net.URISyntaxException;
import java.net.URL;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;

public class TestHttpRequestFactory {

	private final HttpClient client;
	
	private final URL serverUrl;

	public TestHttpRequestFactory(HttpClient client, URL serverUrl) {
		super();
		this.client = client;
		this.serverUrl = serverUrl;
	}

	public TestHttpRequest get() {
		return request(RequestMethod.get);
	}
	
	public TestHttpRequest post() {
		return request(RequestMethod.post);
	}
	
	public TestHttpRequest put() {
		return request(RequestMethod.put);
	}

	public TestHttpRequest delete() {
		return request(RequestMethod.delete);
	}

	public TestHttpRequest request(RequestMethod method) {
		try {
			switch (method) {
				case get:
					return new TestHttpRequest(client, new HttpGet(serverUrl.toURI()));
				case post:
					return new TestHttpRequest(client, new HttpPost(serverUrl.toURI()));
				case put:
					return new TestHttpRequest(client, new HttpPut(serverUrl.toURI()));
				case delete:
					return new TestHttpRequest(client, new HttpDelete(serverUrl.toURI()));
				default:
					throw new UnsupportedOperationException("Method " + method + " not supported.");
			}
		} catch(URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}
}
