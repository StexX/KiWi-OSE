/*
 * File : KiWiSetupFilter.java
 * Date : Dec 9, 2009
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2008 The KiWi Project. All rights reserved.
 * http://www.kiwi-project.eu
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  KiWi designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 *
 * Contributor(s):
 */


package kiwi.wsetup;


import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;


/**
 * @author mradules
 * @version 07-pre
 * @since 07-pre
 */
@Scope(ScopeType.APPLICATION)
@Name("kiwiSetupFilter")
public class KiWiSetupFilter implements Filter {

    private String earRealPath;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        final ServletContext servletContext = filterConfig.getServletContext();
        String webDescrPath = "/WEB-INF/web.xml";
        final String realPath = servletContext.getRealPath(webDescrPath);
        final StringBuffer result = new StringBuffer(realPath);
        final int indexOf = realPath.indexOf("kiwi.web.setup.war");
        if (indexOf >= 0) {
            result.delete(indexOf, realPath.length());
            earRealPath = result.toString();
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain filterChain) throws IOException, ServletException {
        // TODO : very ugly way to pass values between java classes.
        // The reason is, the seam filter does not allows me to share
        // information via context (or it is possible how but I can not
        // figure it out how).
        if (earRealPath != null) {
            System.setProperty("earPath", earRealPath);
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // UNIMPLEMENTED
    }
}
