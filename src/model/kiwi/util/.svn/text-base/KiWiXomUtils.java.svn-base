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

package kiwi.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.ParentNode;
import nu.xom.Text;

/**
 * @author Marek Schmidt
 *
 */
public class KiWiXomUtils {
	
	public static Node firstChild (Node n) {
		if (n.getChildCount() == 0) return null;
		return n.getChild(0);
	}
	
	public static Node lastChild (Node n) {
		if (n.getChildCount() == 0) return null;
		return n.getChild(n.getChildCount() - 1);
	}
	
	public static Node nextSibling (Node n) {
		ParentNode p = n.getParent();
		int i = p.indexOf(n);
		if (i == p.getChildCount() - 1) return null;
		return p.getChild(i + 1);
	}
	
	public static Node previousSibling (Node n) {
		ParentNode p = n.getParent();
		int i = p.indexOf(n);
		if (i == 0) return null;
		return p.getChild(i - 1);
	}
	
	public static Node getFirstTextNode (Node root) {
		
		Node node = root;
		
		while (true) {
			while ( !(node instanceof Text) && firstChild (node) != null) {
				node = firstChild (node);
			}
			
			if ( (node instanceof Text) ) {
				return node;
			}
			
			if (node.equals(root)) {
				return null;
			}
			
			while (nextSibling (node) == null) {
				node = node.getParent();
				if (node.equals(root)) {
					return null;
				}
			}
			
			node = nextSibling (node);
		}
	}
	
	public static Node getLastTextNode (Node root) {
		
		Node node = root;
		
		while (true) {
			while ( !(node instanceof Text) && lastChild(node) != null) {
				node = lastChild (node);
			}
			
			if (node instanceof Text)
				return node;
			
			if (node.equals(root))
				return null;
			
			while (previousSibling (node) == null) {
				node = node.getParent();
				if (node.equals(root)) {
					return null;
				}
			}
			
			node = previousSibling(node);
		}
	}
	
	public static List<ParentNode> getParents (ParentNode n) {
		List<ParentNode> ret = new LinkedList<ParentNode> ();
		while (n != null) {
			ret.add(n);
			n = n.getParent();
		}
		
		return ret;
	}
	
	public static Node getLeastCommonParent (Node n1, Node n2) {
		List<ParentNode> ns1 = getParents (n1.getParent());
		List<ParentNode> ns2 = getParents (n2.getParent());
		
		for (Node ni1 : ns1) {
			for (Node ni2 : ns2) {
				if (ni1.equals(ni2)) {
					return ni1;
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Do a shallow copy, but with attributes as well
	 * @param n
	 * @return
	 */
	public static Node shallowCopy (Node n) {
		if (n instanceof Element) {
			Element e = (Element) n;
			Element ret = new Element(e.getLocalName(), e.getNamespaceURI());
			for (int i = 0; i < e.getAttributeCount(); ++i) {
				ret.addAttribute((Attribute)e.getAttribute(i).copy());
			}
			
			return ret;
		}
		else {
			return n.copy();
		}
	}
	
	public static class NodePos {
		
		public NodePos(Node node, int pos, String prefix) {
			this.node = node;
			this.pos = pos;
			this.prefix = prefix;
		}
		
		private int pos;
		private Node node;
		private String prefix;
		
		public int getPos() {
			return pos;
		}
		public void setPos(int pos) {
			this.pos = pos;
		}
		public Node getNode() {
			return node;
		}
		public void setNode(Node node) {
			this.node = node;
		}
		public String getPrefix() {
			return prefix;
		}
		public void setPrefix(String prefix) {
			this.prefix = prefix;
		}
	};
	
	public static class NodePosIterator implements Iterator<NodePos> {

		private List<NodePos> stack = new LinkedList<NodePos>();
		int pos;
		Node node;
		
		public NodePosIterator(Node n) {
			add (n);
			pos = 0;
			node = n;
		}
		
		public NodePosIterator(Node n, int offset) {
			add (n);
			pos = offset;
			node = n;
		}
		
		private boolean isBlockElement(Node n) {
			if (n instanceof Element) {
				Element e = (Element)n;
				if ("p".equals(e.getLocalName()) || "div".equals(e.getLocalName())) {
					return true;
				}
			}
			
			return false;
		}
		
		private void add (Node n) {
			if (isBlockElement(n)) {
				if (stack.size() > 0) {
					stack.get(0).setPrefix("\n" + stack.get(0).getPrefix());
				}
				
				stack.add(0, new NodePos(n, 0, "\n"));
			}
			else {
				stack.add(0, new NodePos(n, 0, ""));
			}
		}
		
		/* (non-Javadoc)
		 * @see java.util.Iterator#hasNext()
		 */
		@Override
		public boolean hasNext() {
			// TODO Auto-generated method stub
			return !stack.isEmpty();
		}

		/* (non-Javadoc)
		 * @see java.util.Iterator#next()
		 */
		@Override
		public NodePos next() {
			NodePos n = stack.remove(0);
			
			for (int i = n.getNode().getChildCount() - 1; i >= 0;  --i) {
				add(n.getNode().getChild(i));
			}
			
			pos += n.getPrefix().length();
			
			n.setPos(pos);
			
			if (n.getNode() instanceof Text) {
				Text text = (Text)n.getNode();
				pos += text.getValue().length();
			}
			node = n.getNode();
			return n;
		}

		/* (non-Javadoc)
		 * @see java.util.Iterator#remove()
		 */
		@Override
		public void remove() {
			node.detach();
		}	
	}
	
	public static String xom2plain (Node n) {
		StringBuilder sb = new StringBuilder();
		NodePosIterator npi = new NodePosIterator(n);
		while(npi.hasNext()) {
			NodePos np = npi.next();
			
			sb.append(np.getPrefix());
			
			if (np.getNode() instanceof Text) {
				// Fill hypothetical generated blanks
				if (np.getPos() > sb.length()) {
					int addlen = np.getPos() - sb.length();
					for (int i = 0; i < addlen; ++i) {
						sb.append(' ');
					}
				}
				
				sb.append (np.getNode().getValue());
			}
		}
		
		return sb.toString();
	}
	
	public static NodePos getNode(Node n, int offset, int pos) {			
		NodePosIterator npi = new NodePosIterator(n, offset);
		NodePos np = null;
		while(npi.hasNext()) {
			np = npi.next();
						
			if (np.getNode() instanceof Text) {
				Text text = (Text)np.getNode();
				String value = text.getValue();
				if ((np.getPos() - np.getPrefix().length()) <= pos && (np.getPos() + value.length()) >= pos) {
					return np;
				}
			}
			else {
				if ((np.getPos() - np.getPrefix().length()) <= pos && np.getPos() > pos) {
					return np;
				}
			}
		}
		
		return np;
	}
	

	public static void insertNodeInsideText (Text t, Node node, int pos) {
		ParentNode pn = t.getParent();
		
		String s = t.getValue();
		String pre = s.substring(0, pos);
		String post = s.substring(pos);
		
		int i = pn.indexOf(t);
		pn.replaceChild(t, node);
		if (post.length() > 0) {
			pn.insertChild(new Text(post), i+1);
		}
		if (pre.length() > 0) {
			pn.insertChild(new Text(pre), i);
		}
	}
	
	public static void insertNodeAtPos (Node root, int offset, int pos, Node node) {
		NodePos np = getNode(root, offset, pos);
		if (np != null) {
			insertNodeInsideText((Text)(np.getNode()), node, pos - np.getPos());
		}
	}
}
