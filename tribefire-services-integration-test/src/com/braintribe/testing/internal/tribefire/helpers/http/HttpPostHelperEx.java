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
package com.braintribe.testing.internal.tribefire.helpers.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import com.braintribe.codec.marshaller.json.JsonStreamMarshaller;

public class HttpPostHelperEx extends AbstractHttpHelper<HttpPost> {

	public HttpPostHelperEx(String url) throws ClientProtocolException, IOException {
		super(new HttpPost(url));
	}

	public HttpPostHelperEx(String url, Object body) throws ClientProtocolException, IOException {
		super(generateRequest(url, body));
	}

	private static HttpPost generateRequest(String url, Object body) throws UnsupportedEncodingException {
		HttpPost request = new HttpPost(url);
		request.setHeader("Content-Type", "application/json");
		request.setEntity(new StringEntity(objectToJson(body)));

		return request;
	}

	private static String objectToJson(Object value) {
		JsonStreamMarshaller jsonMarshaller = new JsonStreamMarshaller();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		jsonMarshaller.marshall(stream, value);

		try {
			return stream.toString("UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("UTF-8 seems not to be supported", e);
		}
	}

}
