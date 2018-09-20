package com.jinyuan.model.table;

import java.util.Comparator;

public class FormatableInteger implements Comparator<FormatableInteger>{
	
	
	private int size;

	public FormatableInteger(int size) {
		this.size = size;
	}
	
	public int getSize(){
		return size;
	}
	
	@Override
	public String toString(){
			String returnValue;
			if(size<= 0){
				returnValue =  "0";}
			
			else if(size<1024){
				returnValue = size + " B";
			}
			else if(size < 1048576){
				returnValue = size/1024 + " kB";
			}else{
				returnValue = size/1048576 + " MB";
			}
			return returnValue;
	}

	@Override
	public int compare(FormatableInteger o1, FormatableInteger o2) {
		Integer int1 = o1.size;
		Integer int2 = o2.size;
		return int1.compareTo(int2);
	}



}
