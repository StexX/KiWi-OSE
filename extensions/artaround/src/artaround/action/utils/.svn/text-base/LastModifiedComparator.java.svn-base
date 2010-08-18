/*
	Copyright © Rolf Sint, Michael Schneider, Christian Osterrieder, 2010
 */

package artaround.action.utils;

import java.util.Comparator;

import artaround.datamodel.artwork.ArtWorkFacade;

/**
 * @author Rolf Sint
 * @version 0.7
 * @since 0.7
 *
 */
public class LastModifiedComparator implements Comparator<ArtWorkFacade>{
	public int compare(ArtWorkFacade a, ArtWorkFacade b){
		if(a.getModified().before(b.getModified())){
			return 1;
		}
		else if(a.getModified().equals(b.getModified())){
			return 0;
		}
		else{
			return -1;
		}		
	}
}
