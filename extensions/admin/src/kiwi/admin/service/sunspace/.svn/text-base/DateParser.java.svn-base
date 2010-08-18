/*
 * File : DateParser.java.java
 * Date : Apr 22, 2010
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


package kiwi.admin.service.sunspace;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Parses a given string in to a <code>java.util.Date</code>.
 * This parser can only parse strings with the format :
 * <ul>
 * <li>yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
 * <li>dd.MM.yy
 * <li>MM.yy
 * <li>.MM.yy
 * <li>yy
 * <li>.yy
 * </ul>
 * For other date formats the parse method returns null.
 * 
 * @author mradules
 * @version 0.9
 * @since 0.9
 */
final class DateParser {

    /**
     * Used to format/parse date using the "dd.MM.yy" pattern.
     */
    private static final SimpleDateFormat DD_MM_YY_FORMAT =
            new SimpleDateFormat("dd.MM.yy");

    /**
     * Used to format/parse date using the "MM.yy" pattern.
     */
    private static final SimpleDateFormat MM_YY_FORMAT = new SimpleDateFormat(
            "MM.yy");

    /**
     * Used to format/parse date using the ".MM.yy" pattern.
     */
    private static final SimpleDateFormat POINT_MM_YY_FORMAT =
            new SimpleDateFormat(".MM.yy");

    /**
     * Used to format/parse date using the "dd.MM.yy" pattern.
     */
    private static final SimpleDateFormat YY_FORMAT =
            new SimpleDateFormat("yy");

    /**
     * Used to format/parse date using the ".yy" pattern.
     */
    private static final SimpleDateFormat POINT_YY_FORMAT =
            new SimpleDateFormat(".yy");

    /**
     * Used to format/parse date using the
     * "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" pattern.
     */
    private static final SimpleDateFormat BIG_FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    /**
     * Don't lent anybody to instantiate this class.
     */
    private DateParser() {
        // UNIMPLEMTED
    }

    /**
     * Parse a String in to a date, this parser can only parse
     * strings with the format :
     * <ul>
     * <li>yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
     * <li>dd.MM.yy
     * <li>MM.yy
     * <li>.MM.yy
     * <li>yy
     * <li>.yy
     * </ul>
     * For other date formats the parse method returns null.
     * 
     * @param strDate the string to parse.
     * @return the parsed string or null if the String to parse
     *         can not be parsed.
     */
    static Date parseDate(String strDate) {

        try {
            return BIG_FORMAT.parse(strDate);
        } catch (final ParseException e) {
            // I don't care about
        }

        try {
            return DD_MM_YY_FORMAT.parse(strDate);
        } catch (final ParseException e) {
            // I don't care about
        }

        try {
            return MM_YY_FORMAT.parse(strDate);
        } catch (final ParseException e) {
            // I don't care about
        }

        try {
            return POINT_MM_YY_FORMAT.parse(strDate);
        } catch (final ParseException e) {
            // I don't care about
        }

        try {
            return YY_FORMAT.parse(strDate);
        } catch (final ParseException e) {
            // I don't care about
        }

        try {
            return POINT_YY_FORMAT.parse(strDate);
        } catch (final ParseException e) {
            // I don't care about
        }

        return null;
    }

}
