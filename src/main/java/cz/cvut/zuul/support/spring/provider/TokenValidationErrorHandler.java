/*
 * The MIT License
 *
 * Copyright 2013-2014 Czech Technical University in Prague.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package cz.cvut.zuul.support.spring.provider;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

/**
 * Decorator of ResponseErrorHandler used in
 * {@link RemoteResourceTokenServices} that handles HTTP status 409
 * and delegates all other to the decorated ErrorHandler.
 */
public class TokenValidationErrorHandler implements ResponseErrorHandler {

    private ResponseErrorHandler parentHandler;


    public TokenValidationErrorHandler(ResponseErrorHandler parentHandler) {
        this.parentHandler = parentHandler;
    }


    public boolean hasError(ClientHttpResponse response) throws IOException {
        return parentHandler.hasError(response);
    }

    public void handleError(ClientHttpResponse response) throws IOException {
        switch (response.getStatusCode()) {
            case CONFLICT:
                throw new InvalidClientTokenException(response.getStatusText());
            default:
                parentHandler.handleError(response);
        }
    }
}
