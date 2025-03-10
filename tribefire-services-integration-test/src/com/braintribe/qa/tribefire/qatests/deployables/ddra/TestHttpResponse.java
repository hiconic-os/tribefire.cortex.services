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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.apache.http.util.EntityUtils;

public class TestHttpResponse {

	private final org.apache.http.HttpResponse response;

	public TestHttpResponse(org.apache.http.HttpResponse response) {
		this.response = response;
	}

	public org.apache.http.HttpResponse getResponse() {
		return response;
	}

	public int getStatusCode() {
		return response.getStatusLine().getStatusCode();
	}

	public <T> T getContent() {
		try (InputStream content = response.getEntity().getContent()) {
			return (T) TestHttpRequest.MARSHALLER.unmarshall(content);
		} catch (UnsupportedOperationException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	public String getContentStringValue() {
		try (InputStream content = response.getEntity().getContent()) {
			StringBuilder textBuilder = new StringBuilder();
			try (Reader reader = new BufferedReader(new InputStreamReader(content, Charset.forName(StandardCharsets.UTF_8.name())))) {
				int c = 0;
				while ((c = reader.read()) != -1) {
					textBuilder.append((char) c);
				}
			}

			return textBuilder.toString();
		} catch (UnsupportedOperationException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void consumeEntity() {
		EntityUtils.consumeQuietly(response.getEntity());
	}
}
