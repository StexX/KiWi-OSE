/*
 * Filename     : FacadeUtilsTest.java
 * Version      : 0.00.01-01
 * Date         : Dec 6, 2008
 * Copyright :
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


package kiwi.test.unit;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kiwi.service.entity.FacadeUtils;

import org.testng.Assert;
import org.testng.annotations.Test;




/**
 * Used to tests the <code>FacadeUtils</code> class mechanisms.
 *
 * @author mihai
 * @version 0.00.01-01
 * @since 0.00.01-01
 * @see FacadeUtils
 */
@Test
public final class FacadeUtilsTest {

    /**
     * Builds a <code>FacadeUtilsTest</code> instance.
     */
    public FacadeUtilsTest() {
        // UNIMPLEMENTED
    }


    /**
     * Test the {@link FacadeUtils#isBaseType(Class)} method with
     * various input.
     *
     * @see FacadeUtils#isBaseType(Class)
     */
    @Test
    public void testIsBaseType() {
        assertFalse(FacadeUtils.isBaseType(null));
        assertFalse(FacadeUtils.isBaseType(Object.class));

        assertTrue(FacadeUtils.isBaseType(Byte.class));
        assertTrue(FacadeUtils.isBaseType(Short.class));
        assertTrue(FacadeUtils.isBaseType(Character.class));
        assertTrue(FacadeUtils.isBaseType(Integer.class));
        assertTrue(FacadeUtils.isBaseType(Long.class));

        assertTrue(FacadeUtils.isBaseType(Float.class));
        assertTrue(FacadeUtils.isBaseType(Double.class));
    }

    /**
     * Test the {@link FacadeUtils#isCollection(Class)} method
     * with various input.
     *
     * @see FacadeUtils#isCollection(Class)
     */
    @Test
    public void testIsCollection() {
        assertFalse(FacadeUtils.isCollection(null));
        assertFalse(FacadeUtils.isCollection(Object.class));

        assertTrue(FacadeUtils.isCollection(Set.class));
        assertTrue(FacadeUtils.isCollection(List.class));

        // the map is not a collection.
        assertFalse(FacadeUtils.isCollection(Map.class));
    }

    /**
     * Test the {@link FacadeUtils#isPrimitive(Class)} method
     * with various input.
     *
     * @see FacadeUtils#isPrimitive(Class)
     */
    @Test
    public void testIsPrimitiveClass() {

        assertFalse(FacadeUtils.isPrimitive(null));
        assertFalse(FacadeUtils.isPrimitive(String.class));

        assertTrue(FacadeUtils.isPrimitive(Byte.class));
        assertTrue(FacadeUtils.isPrimitive(Short.class));
        assertTrue(FacadeUtils.isPrimitive(Character.class));
        assertTrue(FacadeUtils.isPrimitive(Integer.class));
        assertTrue(FacadeUtils.isPrimitive(Long.class));

        assertTrue(FacadeUtils.isPrimitive(Float.class));
        assertTrue(FacadeUtils.isPrimitive(Double.class));

    }

    /**
     * Test the {@link FacadeUtils#isPrimitive(Object)} method
     * with various input.
     *
     * @see FacadeUtils#isPrimitive(Object)
     */
    @Test
    public void testIsPrimitiveObject() {

        assertFalse(FacadeUtils.isPrimitive(null));
        assertFalse(FacadeUtils.isPrimitive("dddd"));

        assertTrue(FacadeUtils.isPrimitive((byte) 1));
        assertTrue(FacadeUtils.isPrimitive((short) 1));
        assertTrue(FacadeUtils.isPrimitive('f'));
        assertTrue(FacadeUtils.isPrimitive(1));
        assertTrue(FacadeUtils.isPrimitive((long) 3));

        assertTrue(FacadeUtils.isPrimitive((float) 1.2));
        assertTrue(FacadeUtils.isPrimitive(1.2));

    }
    
    
    @Test
    public void transformToBaseType() {
    	
    	Assert.assertEquals(FacadeUtils.transformToBaseType("1", Integer.class), Integer.valueOf(1));
    	Assert.assertEquals(FacadeUtils.transformToBaseType(null, Integer.class), Integer.valueOf(0));
    	
    	Assert.assertEquals(FacadeUtils.transformToBaseType("1", Long.class), Long.valueOf(1));
    	Assert.assertEquals(FacadeUtils.transformToBaseType(null, Long.class), Long.valueOf(0));
    	
    	Assert.assertEquals(FacadeUtils.transformToBaseType("1", Byte.class), Byte.valueOf((byte) 1));
    	Assert.assertEquals(FacadeUtils.transformToBaseType(null, Byte.class), Byte.valueOf((byte) 0));
    	
    	Assert.assertEquals(FacadeUtils.transformToBaseType("1.1", Double.class), Double.valueOf(1.1));
    	Assert.assertEquals(FacadeUtils.transformToBaseType(null, Double.class), Double.valueOf(0));
    	
    	Assert.assertEquals(FacadeUtils.transformToBaseType("1.1", Float.class), Float.valueOf((float) 1.1));
    	Assert.assertEquals(FacadeUtils.transformToBaseType(null, Float.class), Float.valueOf((float) 0));
    	
    	Assert.assertEquals(FacadeUtils.transformToBaseType("true", Boolean.class), Boolean.valueOf(true));
    	Assert.assertEquals(FacadeUtils.transformToBaseType(null, Boolean.class), Boolean.valueOf(false));

    	Assert.assertEquals(FacadeUtils.transformToBaseType("Hello", Character.class), Character.valueOf('H'));
    	Assert.assertEquals(FacadeUtils.transformToBaseType("H", Character.class), Character.valueOf('H'));
    	Assert.assertNull(FacadeUtils.transformToBaseType("", Character.class));
    	Assert.assertNull(FacadeUtils.transformToBaseType(null, Character.class));
    	
    	
    	Assert.assertEquals(FacadeUtils.transformToBaseType("2009-01-01T12:00:00.000+0100", Date.class).getYear(), 109);
    }
    
    
    @Test
    public void transformFromBaseType() {
    	Assert.assertNull(FacadeUtils.transformFromBaseType(null));
    	Assert.assertEquals(FacadeUtils.transformFromBaseType(1), "1");
    	Assert.assertEquals(FacadeUtils.transformFromBaseType((byte)1), "1");
    	Assert.assertEquals(FacadeUtils.transformFromBaseType(1L), "1");
    	Assert.assertEquals(FacadeUtils.transformFromBaseType(1.0), "1.0");
    	Assert.assertEquals(FacadeUtils.transformFromBaseType(1.0f), "1.0");
    	Assert.assertEquals(FacadeUtils.transformFromBaseType(true), "true");
    	Assert.assertEquals(FacadeUtils.transformFromBaseType('H'), "H");
    }
}
