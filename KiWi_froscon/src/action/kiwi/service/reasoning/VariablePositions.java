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
 * Contributor(s): Jakub Kotowski
 */
package kiwi.service.reasoning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kiwi.service.reasoning.ast.Variable;

/**
 * Holds a map from variables to variable positions.
 * 
 * For example:
 * 
 * $1 = [{1, SUBJECT, TRIPLE_PATTERN}, {2, OBJECT, TRIPLE_PATTERN}]
 * 
 * @author Jakub Kotowski
 * 
 */
public class VariablePositions {
	Map<Variable, List<VariablePosition>> map = new HashMap<Variable, List<VariablePosition>>();

	public List<VariablePosition> getVariablePositions(Variable var) {
		return map.get(var);
	}

	public Set<Variable> getVariables() {
		return map.keySet();
	}

	public void addPosition(Variable var, VariablePosition pos) {
		List<VariablePosition> positions = map.get(var);
		if (positions == null) {
			positions = new ArrayList<VariablePosition>();
			map.put(var, positions);
		}
		positions.add(pos);
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		for (Variable var : map.keySet()) {
			sb.append(",");
			sb.append(var).append(" = ");
			sb.append(map.get(var));
		}
		
		sb.replace(0, 1, "["); //change the leading comma to [
		
		return sb.append("]").toString(); 
	}
}
