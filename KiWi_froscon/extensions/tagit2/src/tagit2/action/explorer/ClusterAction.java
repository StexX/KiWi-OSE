package tagit2.action.explorer;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;

import tagit2.util.query.Cluster;

@Scope(ScopeType.APPLICATION)
//@Transactional
@Name("tagit2.clusterAction")
public class ClusterAction {

	private Cluster cluster;
	
	public void setCluster(Cluster cluster) {
		this.cluster = cluster;
	}
	
	public String getCurrentClusterXML() {
		if( cluster != null ) {
			return cluster.toXML();
		} else return "<?xml version='1.0' encoding='UTF-8'?><cluster></cluster>";
 	}
	
	public String getCurrentClusterJSON(int start, int size) {
		if( cluster != null ) {
			return cluster.toJSON(start,size);
		} else return "{[]}";
 	}
	
	public String getCurrentClusterJSON(int start, int size,String c) {
		if( cluster != null ) {
			return cluster.toJSON(start,size,c);
		} else return "{[]}";
	}
	
	public void resetCluster() {
		cluster = null;
	}
	
}
