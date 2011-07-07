/*
 * Copyright (c) 2011 Socialize Inc. 
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
package com.socialize.entity.factory;

import org.json.JSONException;
import org.json.JSONObject;

import com.socialize.entity.ActionError;

/**
 * @author Jason Polites
 */
public class ErrorFactory extends JSONFactory<ActionError> {
	
	private static final String MESSAGE = "error";

	@Override
	public ActionError instantiateObject() {
		return new ActionError();
	}

	@Override
	protected void fromJSON(JSONObject from, ActionError to) throws JSONException {
		if(from.has(MESSAGE) && !from.isNull(MESSAGE)) {
			to.setMessage(from.getString(MESSAGE));
		}
	}

	@Override
	protected void toJSON(ActionError from, JSONObject to) throws JSONException {
		if(from.getMessage() != null) {
			to.put(MESSAGE, from.getMessage());
		}
	}
}
