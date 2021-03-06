package de.felixbruns.jotify.media;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.felixbruns.jotify.util.Hex;
import de.felixbruns.jotify.util.XMLElement;

public class Media {
	/**
	 * Identifier for this media (32-character string).
	 */
	protected String id;
	
	/**
	 * Popularity of this media (from 0.0 to 1.0).
	 */
	protected float popularity;
	
	/**
	 * Restrictions of this media.
	 */
	private List<Restriction> restrictions;
	
	/**
	 * External ids of this media.
	 */
	private Map<String, String> externalIds;
	
	/**
	 * Creates an empty {@link Media} object.
	 */
	protected Media(){
		this.id           = null;
		this.popularity   = Float.NaN;
		this.restrictions = new ArrayList<Restriction>();
		this.externalIds  = new HashMap<String, String>();
	}
	
	/**
	 * Creates a {@link Media} object with the specified {@code id}.
	 * 
	 * @param id Id of the track.
	 * 
	 * @throws IllegalArgumentException If the given id is invalid.
	 */
	protected Media(String id){
		/* Check if id string is valid. */
		if(id == null || id.length() != 32 || !Hex.isHex(id)){
			throw new IllegalArgumentException("Expecting a 32-character hex string.");
		}
		
		this.id           = id;
		this.popularity   = Float.NaN;
		this.restrictions = new ArrayList<Restriction>();
		this.externalIds  = new HashMap<String, String>();
	}
	
	/**
	 * Get the medias identifier.
	 * 
	 * @return A 32-character identifier.
	 */
	public String getId(){
		return this.id;
	}
	
	/**
	 * Set the medias identifier.
	 * 
	 * @param id A 32-character identifier.
	 * 
	 * @throws IllegalArgumentException If the given id is invalid.
	 */
	public void setId(String id){
		/* Check if id string is valid. */
		if(id == null || id.length() != 32 || !Hex.isHex(id)){
			throw new IllegalArgumentException("Expecting a 32-character hex string.");
		}
		
		this.id = id;
	}
	
	/**
	 * Get the medias popularity.
	 * 
	 * @return A decimal value between 0.0 and 1.0 or {@link Float.NAN} if not available.
	 */
	public float getPopularity(){
		return this.popularity;
	}
	
	/**
	 * Set the medias popularity.
	 * 
	 * @param popularity A decimal value between 0.0 and 1.0 or {@link Float.NAN}.
	 * 
	 * @throws IllegalArgumentException If the given popularity value is invalid.
	 */
	public void setPopularity(float popularity){
		/* Check if popularity value is valid. */
		if(popularity != Float.NaN && (popularity < 0.0 || popularity > 1.0)){
			throw new IllegalArgumentException("Expecting a value from 0.0 to 1.0 or Float.NAN.");
		}
		
		this.popularity = popularity;
	}
	
	/**
	 * Get the medias restrictions.
	 * 
	 * @return A {@link List} of {@link Restriction} objects.
	 */
	public List<Restriction> getRestrictions(){
		return this.restrictions;
	}
	
	/**
	 * Check if the media is restricted for the given {@code country} and {@code catalogue}.
	 * 
	 * @param country   A 2-letter country code.
	 * @param catalogue The catalogue to check.
	 * 
	 * @return true if it is restricted, false otherwise.
	 * 
	 * @throws IllegalArgumentException If the given 2-letter country code is invalid.
	 */
	public boolean isRestricted(String country, String catalogue){
		if(country.length() != 2){
			throw new IllegalArgumentException("Expecting a 2-letter country code!");
		}
		
		for(Restriction restriction : this.restrictions){
			if(restriction.isCatalogue(catalogue) &&
			   (restriction.isForbidden(country) ||
			   !restriction.isAllowed(country))){
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Set the medias restrictions.
	 * 
	 * @param restrictions A {@link List} of {@link Restriction} objects.
	 */
	public void setRestrictions(List<Restriction> restrictions){
		this.restrictions = restrictions;
	}
	
	/**
	 * Get the medias external identifiers.
	 * 
	 * @return A {@link Map} of external services and their identifers for the media.
	 */
	public Map<String, String> getExternalIds(){
		return this.externalIds;
	}
	
	/**
	 * Get an external identifier for the specified {@code service}.
	 * 
	 * @param service The service to get the identifer for.
	 * 
	 * @return An identifier or null if not available.
	 */
	public String getExternalId(String service){
		return this.externalIds.get(service);
	}
	
	/**
	 * Set the medias external identifiers.
	 * 
	 * @param externalIds A {@link Map} of external services and their identifers for the media.
	 */
	public void setExternalIds(Map<String, String> externalIds){
		this.externalIds = externalIds;
	}
	
	/**
	 * Create a {@link Media} object from an {@link XMLElement} holding media information.
	 * 
	 * @param mediaElement An {@link XMLElement} holding media information.
	 * 
	 * @return A {@link Media} object.
	 */
	public static Media fromXMLElement(XMLElement mediaElement){
		Media media = new Media();
		
		/* Set id. */
		if(mediaElement.hasChild("id")){
			media.id = mediaElement.getChildText("id");
		}
		
		/* Set popularity. */
		if(mediaElement.hasChild("popularity")){
			media.popularity = Float.parseFloat(mediaElement.getChildText("popularity"));
		}
		
		return media;
	}
}
