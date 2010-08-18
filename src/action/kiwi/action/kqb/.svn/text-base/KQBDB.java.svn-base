package kiwi.action.kqb;

/**
 * helper class to allow the KWQL Query Builder to
 * save and load queries
 * 
 * @author Andreas Hartl
 */


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import kiwi.model.Constants;
import kiwi.model.annotations.RDF;
import kiwi.model.kbase.KiWiAnonResource;
import kiwi.model.kbase.KiWiEntity;
import kiwi.model.kbase.KiWiResource;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.NotNull;

@Entity
@Table(name="kqbdbtable")
public class KQBDB implements KiWiEntity, Serializable
{
   private static final long serialVersionUID = 3881413500711441952L;
   
   @Id
   @GeneratedValue(strategy=GenerationType.SEQUENCE)
   private long ID;
   
   private String name;
   private String data;
   
   @NotNull
   @OneToOne(fetch=FetchType.EAGER)
   @Fetch(FetchMode.JOIN)
   @Cascade({CascadeType.PERSIST})
   private KiWiResource resource = new KiWiAnonResource();

   @NotNull    
   private boolean deleted = false;
   
   /** the deletionDate date of the ContentItem **/
   @Temporal(value = TemporalType.TIMESTAMP)
   @Column(updatable = true, nullable=true)
   @RDF(Constants.NS_KIWI_CORE+"deletedOn")
   private Date deletedOn;
   
   public KQBDB(String n, String d)
   {
	   this.name = n;
       this.data = d;
   }
   
   public KQBDB() {}
   
   @NotNull
   public String getData()
   {
      return data;
   }

   public void setData(String d)
   {
      this.data = d;
   }
   
   public String getName()
   {
	   return name;
   }
   public void setName (String n)
   {
	   this.name = n;
   }
   
	public KiWiResource getResource() {
		return resource;
	}

	public void setResource(KiWiResource resource) {
		this.resource = resource;
	}
   
	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	@Override
	public Long getId() {
		return ID;
	}

	@Override
	public void setId(Long id) {
		this.ID = id;
	}

	public Date getDeletedOn() {
		return deletedOn;
	}

	public void setDeletedOn(Date deletedOn) {
		this.deletedOn = deletedOn;
	}
   
}
