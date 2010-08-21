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
 * 
 * 
 */
package kiwi.service.query.sparql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import kiwi.api.triplestore.TripleStore;
import kiwi.model.kbase.KiWiNode;
import kiwi.service.triplestore.TripleStoreUtil;

import org.openrdf.model.Value;


/**
 *
 * @author Sebastian Schaffert
 */
public class QueryBinding {
    
	private List<String>             bindingKeys;
    private HashMap<String,KiWiNode> bindingsKiWi;
    private HashMap<String,Value>    bindingsNative;
    private TripleStore		         context;

    public QueryBinding(TripleStore context) {
        this.context        = context;
        this.bindingsKiWi   = new HashMap<String,KiWiNode>();
        this.bindingsNative = new HashMap<String,Value>();
        this.bindingKeys    = new ArrayList<String>();
        
    }
    
    public KiWiNode getBinding(String key) {
        return bindingsKiWi.get(key);
    }
    
    public Value getBindingNative(String key) {
        return bindingsNative.get(key);
    }
    
    public void setBinding(String key, Value v) {
    	bindingKeys.add(key);
        bindingsKiWi.put(key, TripleStoreUtil.transformSesameToKiWi(context,v));
        bindingsNative.put(key,v);
    }

    public Collection<String> getKeys() {
    	return bindingKeys;
    }
}
