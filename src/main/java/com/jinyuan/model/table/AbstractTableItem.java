package com.jinyuan.model.table;

import javafx.beans.property.SimpleBooleanProperty;

public abstract class AbstractTableItem {
	
	private final SimpleBooleanProperty read = new SimpleBooleanProperty();
	
	public AbstractTableItem (boolean isRead){
		this.setRead(isRead);
	}
	
	public SimpleBooleanProperty getReadProperty(){
		return read;
	}
	
	public void setRead(boolean isRead){
		read.set(isRead);
	}
	public boolean isRead(){
		return read.get();
	}
	

}
