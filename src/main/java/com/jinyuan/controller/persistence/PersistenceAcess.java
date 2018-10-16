package com.jinyuan.controller.persistence;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.jinyuan.controller.ModelAccess;
import com.jinyuan.controller.services.CreateAndRegisterEmailAccountService;

public class PersistenceAcess {
	
	private List<ValidAccount> persistedList = null;
	private List<ValidAddressBook> addressBookList = null;
	public PersistenceAcess(ModelAccess modelAccess) {
		this.modelAccess = modelAccess;
//		loadFromPersistence();
	}

	public ModelAccess modelAccess;	
	
	/**
	 * Call on program start
	 */
	@SuppressWarnings("unchecked")
	public void loadFromPersistence(){


		try {
			FileInputStream fileIn = new FileInputStream(/*System.getenv("APPDATA") + */"validAddressBook.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			addressBookList = (List<ValidAddressBook>) in.readObject();
			in.close();
			fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		try {
			FileInputStream fileIn = new FileInputStream(/*System.getenv("APPDATA") + */"validAccounts.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			persistedList = (List<ValidAccount>) in.readObject();
			in.close();
			fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		} 	
		
		for(ValidAccount account: persistedList){
			modelAccess.setMailType(account.getType());
			CreateAndRegisterEmailAccountService service = 
					new CreateAndRegisterEmailAccountService(account.getAddress(), account.getPassword(), modelAccess);
			service.start();
		}
			
	}
	
	/**
	 * Call on program exit
	 */
	public void SavePersistence(){
		try {
			FileOutputStream fileOut = new FileOutputStream(/*System.getenv("APPDATA") + */"validAccounts.ser");
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(modelAccess.getValidAccountList());
	         out.close();
	         fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			FileOutputStream fileOut = new FileOutputStream(/*System.getenv("APPDATA") + */"validAddressBook.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(new ArrayList<ValidAddressBook>(modelAccess.getValidAddressBookList()));
			out.close();
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean validPersistencefound() {
		return persistedList != null && persistedList.size() > 0;
	}
	
	public boolean validAddressBookfound() {
		return addressBookList != null && addressBookList.size() > 0;
	}
	
	public List<ValidAddressBook> getAddressbook() {
		return addressBookList;
	}

	public void clear() {
		// TODO Auto-generated method stub
		try {
			FileOutputStream fileOut = new FileOutputStream(/*System.getenv("APPDATA") + */"validAccounts.ser");
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject("");
	         out.close();
	         fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			FileOutputStream fileOut = new FileOutputStream(/*System.getenv("APPDATA") + */"validAddressBook.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject("");
			out.close();
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		modelAccess.clearModel();
	}

}
