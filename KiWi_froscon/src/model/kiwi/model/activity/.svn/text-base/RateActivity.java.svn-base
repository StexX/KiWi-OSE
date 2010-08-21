/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER. Copyright (c)
 * 2008-2009, The KiWi Project (http://www.kiwi-project.eu) All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: -
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. - Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. - Neither the name of the KiWi Project nor the names
 * of its contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission. THIS SOFTWARE IS
 * PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. Contributor(s):
 */


package kiwi.model.activity;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import kiwi.api.event.KiWiEvents;

/**
 * Activity modelling that a user rate a content item.
 * 
 * @author Mihai Radulescu
 */
@Entity
@DiscriminatorValue("RATE_CONTENTITEM")
public class RateActivity extends ContentItemActivity {

    /**
     * The rate amount.
     */
    private int rate;

    /**
     * Builds a new <code>RateActivity</code> instance.
     */
    public RateActivity() {
            // UNIMPLEMENTD
    }

    /**
     * Returns the message identifier of this activity, "activity.groupCreated".
     * 
     * @return KiWiEvents.ACTIVITY_RATECONTENTITEM (="activity.groupCreated")
     * @see kiwi.model.activity.Activity#getMessageIdentifier()
     */
    @Override
    public String getMessageIdentifier() {
        return KiWiEvents.ACTIVITY_RATECONTENTITEM;
    }

    /**
     * Returns the rate amount for this content item.
     * 
     * @return the rate amount for this content item.
     */
    public int getRate() {
        return rate;
    }

    /**
     * Registers a new value for this content item.
     * 
     * @param rate the new rate value.
     */
    public void setRate(final int rate) {
        this.rate = rate;
    }
}
