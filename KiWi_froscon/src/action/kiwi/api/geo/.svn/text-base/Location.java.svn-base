/*
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
 *
 *
 */
package kiwi.api.geo;


import java.io.Serializable;


/**
 * Object used to describe a geographical location via latitude
 * and longitude.
 *
 * @author Sebastian Schaffert
 * @version 00.01-1
 * @since 00.01-1
 */
public class Location implements Serializable {

    /**
     * A version number for this class so that serialization can
     * occur without worrying about the underlying class changing
     * between serialization and deserialization.
     */
    private static final long serialVersionUID = 7596953607360241281L;

    private double longitude;

    private double latitude;

    public Location() {
        // UNIMPLEMETNED
    }

    public Location(double latitude, double longitude) {
        this();
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Location) {
            final Location l = (Location) o;
            return l.getLatitude() == this.getLatitude()
                    && l.getLongitude() == this.getLongitude();
        } else {
            return false;
        }
    }

    /**
     * Returns a String representation for this object. The
     * String representation look like :
     * <ul>
     * <li>the latitude value
     * <li>camma space (", ") sequence.
     * <li>the longitude value
     * </ul>
     */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(latitude);
        result.append(", ");
        result.append(longitude);

        return result.toString();
    }
}
