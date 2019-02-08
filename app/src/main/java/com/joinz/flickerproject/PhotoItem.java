package com.joinz.flickerproject;

import com.google.gson.annotations.SerializedName;

public class PhotoItem{

	@SerializedName("owner")
	private String owner;

	@SerializedName("server")
	private String server;

	@SerializedName("ispublic")
	private int ispublic;

	@SerializedName("isfriend")
	private int isfriend;

	@SerializedName("farm")
	private int farm;

	@SerializedName("id")
	private String id;

	@SerializedName("secret")
	private String secret;

	@SerializedName("title")
	private String title;

	@SerializedName("isfamily")
	private int isfamily;

	public void setOwner(String owner){
		this.owner = owner;
	}

	public String getOwner(){
		return owner;
	}

	public void setServer(String server){
		this.server = server;
	}

	public String getServer(){
		return server;
	}

	public void setIspublic(int ispublic){
		this.ispublic = ispublic;
	}

	public int getIspublic(){
		return ispublic;
	}

	public void setIsfriend(int isfriend){
		this.isfriend = isfriend;
	}

	public int getIsfriend(){
		return isfriend;
	}

	public void setFarm(int farm){
		this.farm = farm;
	}

	public int getFarm(){
		return farm;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setSecret(String secret){
		this.secret = secret;
	}

	public String getSecret(){
		return secret;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setIsfamily(int isfamily){
		this.isfamily = isfamily;
	}

	public int getIsfamily(){
		return isfamily;
	}

	@Override
 	public String toString(){
		return 
			"PhotoItem{" + 
			"owner = '" + owner + '\'' + 
			",server = '" + server + '\'' + 
			",ispublic = '" + ispublic + '\'' + 
			",isfriend = '" + isfriend + '\'' + 
			",farm = '" + farm + '\'' + 
			",id = '" + id + '\'' + 
			",secret = '" + secret + '\'' + 
			",title = '" + title + '\'' + 
			",isfamily = '" + isfamily + '\'' + 
			"}";
		}
}