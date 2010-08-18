/*
 * File : ImportEquityData.java.java Date : Apr 7, 2010 DO NOT ALTER OR REMOVE
 * COPYRIGHT NOTICES OR THIS HEADER. Copyright 2008 The KiWi Project. All rights
 * reserved. http://www.kiwi-project.eu The contents of this file are subject to
 * the terms of either the GNU General Public License Version 2 only ("GPL") or
 * the Common Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the License.
 * You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html or nbbuild/licenses/CDDL-GPL-2-CP.
 * See the License for the specific language governing permissions and
 * limitations under the License. When distributing the software, include this
 * License Header Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP. KiWi designates this particular file as
 * subject to the "Classpath" exception as provided by Sun in the GPL Version 2
 * section of the License file that accompanied this code. If applicable, add
 * the following below the License Header, with the fields enclosed by brackets
 * [] replaced by your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]" If you wish your
 * version of this file to be governed by only the CDDL or only the GPL Version
 * 2, indicate your decision by adding "[Contributor] elects to include this
 * software in this distribution under the [CDDL or GPL Version 2] license." If
 * you do not indicate a single choice of license, a recipient has the option to
 * distribute your version of this file under either the CDDL, the GPL Version 2
 * or to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL Version
 * 2 license, then the option applies only if the new code is made subject to
 * such option by the copyright holder. Contributor(s):
 */


package kiwi.admin.service.sunspace;


import java.util.EventListener;
import java.util.EventObject;

import org.ajax4jsf.event.PushEventListener;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;

/**
 * This is an observable used to exchange between the view and the deeper
 * layers. <br>
 * You can inject this component register a listener and notify this listener,
 * the notify process can be done together with a String message or without, the
 * message can be obtained using the <code>getMessage</code> method. <br>
 * In most of the cases this component is used like this : two components inject
 * it separately, and one component register a listener and the other one
 * notifies (the listener) and set a message, the register gets the notification
 * and if it consider can get the message also.<br>
 * This observable supports only one lister. <br>
 * The name for this seam component is : "importMonitor";
 * 
 * @author mradules
 * @version 0.9
 * @since 0.9
 */
@Name("importMonitor")
@AutoCreate
public class ImportMonitor {

    /**
     * The listener used to observ this observable.
     */
    @In(value = "importMonitor.Listener", scope = ScopeType.APPLICATION, required = false)
    @Out(value = "importMonitor.Listener", scope = ScopeType.APPLICATION, required = false)
    private EventListener listener;

    /**
     * The message for this observable.
     */
    @In(value = "importMonitor.Message", scope = ScopeType.APPLICATION, required = false)
    @Out(value = "importMonitor.Message", scope = ScopeType.APPLICATION, required = false)
    private String message;

    /**
     * Register a new listener used to observ this observable, if this
     * observable has already an listener registered then this will be replaced.
     * 
     * @param listener the listener to be registered.
     */
    public void addListener(EventListener listener) {
        synchronized (listener) {
            if (this.listener != listener) {
                this.listener = listener;
            }
        }
    }

    /**
     * Notifies changes.
     */
    public void notifyChange() {
        notifyChange(null);
    }

    /**
     * Notifies changes with a given string message. The message can be obtains
     * with the <code>getMessage</code> method.
     * 
     * @param message the string message.
     */
    public void notifyChange(final String message) {
        ((PushEventListener) listener).onEvent(new EventObject(this));
        this.message = message;
    }

    /**
     * The actual message.
     * 
     * @return the actual message.
     */
    public String getMessage() {
        return message;
    }
}
