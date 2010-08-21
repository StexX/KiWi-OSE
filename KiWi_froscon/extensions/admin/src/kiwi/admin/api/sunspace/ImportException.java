/*
 * File : ImportException.java
 * Date : May 8, 2010
 * 
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


package kiwi.admin.api.sunspace;


/**
 * A <code>ImportException</code> indicates a failure which
 * occurs during import (data from the Sun Space). Most of the
 * times it wraps the real cause for the exception, this cause
 * can be obtained by using the <code>getCause()</code> method.
 * 
 * @author mradules
 * @version 0.7
 * @since 0.7
 */
public class ImportException extends Exception {

    /**
     * A version number for this class so that serialization can
     * occur without worrying about the underlying class changing
     * between serialization and deserialization.
     */
    private static final long serialVersionUID = 5165L;

    /**
     * Constructs an <code>HandleException</code> without a
     * detail message or a specified cause.
     */
    public ImportException() {
        super();
    }

    /**
     * Constructs an <code>HandleException</code> with a detail
     * message.
     * 
     * @param message the detail message.
     */
    public ImportException(String message) {
        super(message);
    }

    /**
     * Constructs an <code>HandleException</code> with a
     * specified <code>Exception</code> cause, this cause can be
     * obtained using the <code>getCause()</code> method.
     * 
     * @param cause the cause for this exception. This is the
     *            exception to wrap and chain.
     * @see Exception#getCause()
     */
    public ImportException(Exception cause) {
        super(cause);
    }

    /**
     * Constructs an <code>HandleException</code> with a detail
     * message and a specified <code>Exception</code> cause, this
     * cause can be obtained using the <code>getCause()</code>
     * method.
     * 
     * @param message the detail message.
     * @param cause the cause for this exception. This is the
     *            exception to wrap and chain.
     * @see Exception#getCause()
     */
    public ImportException(String message, Exception cause) {
        super(message, cause);
    }
}
