package com.example.testforapp2;

public class Singleton {
	
    private static Singleton _sharedInstance = null;
    
    /*****Variable************/
    String site;
	/*************************/
    
    private Singleton(){//私有 不給他人宣告
        super();
    }
    public static Singleton getSharedInstance(){
        if(_sharedInstance == null){
        	_sharedInstance = new Singleton();
        }
        return _sharedInstance;
    }
    
    /*****Getting and Setting*****/
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	/*****************************/
    
    
}
