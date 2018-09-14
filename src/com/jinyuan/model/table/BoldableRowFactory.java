package com.jinyuan.model.table;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableRow;

public class BoldableRowFactory<T extends AbstractTableItem> extends TableRow<T>{
	
	private final SimpleBooleanProperty bold = new SimpleBooleanProperty();
	private T currentItem = null;
	
	public BoldableRowFactory() {
		super();
		
		bold.addListener((ObservableValue<? extends Boolean> observable, Boolean olValue, Boolean NewValue) ->{
			if(currentItem != null && currentItem==getItem()){
				updateItem(getItem(), isEmpty());
			}
		});
		itemProperty().addListener((ObservableValue<? extends T> observable, T olValue, T NewValue) ->{
			bold.unbind();
			if(NewValue != null){
				bold.bind(NewValue.getReadProperty());
				currentItem = NewValue;
			}
		});
	}
	
	@Override
	final protected void updateItem(T item, boolean empty){
		super.updateItem(item, empty);
		if(item !=null && !item.isRead()){
			setStyle("-fx-font-weight: bold");
		}else{
			setStyle("");
		}
		
	}
	
}
