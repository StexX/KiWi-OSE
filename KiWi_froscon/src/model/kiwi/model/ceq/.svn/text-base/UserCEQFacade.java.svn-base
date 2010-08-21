/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2008-2009, The KiWi Project (http://www.kiwi-project.eu)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * - Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * - Neither the name of the KiWi Project nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * Contributor(s):
 * sschaffe
 *
 */
package kiwi.model.ceq;

import kiwi.model.Constants;
import kiwi.model.annotations.KiWiFacade;
import kiwi.model.annotations.RDF;
import kiwi.model.annotations.RDFType;
import kiwi.model.content.ContentItemI;


/**
 * UserCEQFacade. Represents the Community Equity properties for
 * a user, i.e. contribution equity, participation equity, and
 * personal equity.
 *
 * @deprecated the equity values are now intern calculated,
 *             consider the
 *             kiwi.service.equity.EquityServiceImpl.
 * @author Sebastian Schaffert
 */
@Deprecated
@KiWiFacade
@RDFType( {Constants.NS_KIWI_CORE + "User"})
public interface UserCEQFacade extends ContentItemI {

    /**
     * Get the contribution equity of this user (CQ). The contribution equity is the sum of all
     * information equity of content items created by a user.
     *
     * @return
     */
    @RDF(Constants.NS_KIWI_CEQ + "cq")
    public double getContributionEquity();

    public void setContributionEquity(double v);

    /**
     * Get the participation equity of this user (PQ). The participation equity is the sum of all
     * social activities of a person, e.g. view/download of information, ratings provided, comments
     * provided, etc.
     *
     * @return
     */
    @RDF(Constants.NS_KIWI_CEQ + "pq")
    public double getParticipationEquity();

    public void setParticipationEquity(double v);

    /**
     * Get the personal equity of this user (PEQ). The personal equity is the sum of CQ and PQ and
     * represents the overall community equity value of a person.
     * @return
     */
    @RDF(Constants.NS_KIWI_CEQ + "peq")
    public double getPersonalEquity();

    public void setPersonalEquity(double v);


}
