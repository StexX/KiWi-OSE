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
public class LastSubmittedComparator implements Comparator<ArtWorkFacade>{
	public int compare(ArtWorkFacade a, ArtWorkFacade b){
		if(a.getCreated().before(b.getCreated())){
			return 1;
		}
		else if(a.getCreated().equals(b.getCreated())){
			return 0;
		}
		else{
			return -1;
		}		
	}
}
