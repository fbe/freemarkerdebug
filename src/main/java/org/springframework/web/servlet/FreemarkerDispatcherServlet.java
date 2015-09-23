/*
 * Copyright 2002-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import name.felixbecker.freemarkerdebug.FreemarkerInstructionsThreadLocal;

public class FreemarkerDispatcherServlet extends DispatcherServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void render(ModelAndView mv, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			FreemarkerInstructionsThreadLocal.initialize();
			super.render(mv, request, response);
		} finally {
			FreemarkerInstructionsThreadLocal.clear();
		}
	}

}
