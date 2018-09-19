package com.jinyuan.controller;

import com.jinyuan.model.EmailValidator;
import javafx.scene.control.TextField;

public abstract class AbstractController {
	
	private ModelAccess modelAccess;
	private EmailValidator emailValidator;
	
	public AbstractController(ModelAccess modelAccess) {
		this.modelAccess = modelAccess;
		this.emailValidator = new EmailValidator();
	}
	
	public ModelAccess getModelAccess() {
		return modelAccess;
	}

	public boolean isValidateEmail(String aEmail) {
		return emailValidator.validate(aEmail);
	}

	public void setEmailToOverwriteField(TextField tf, String val) {
		tf.setText(val);
	}

	public void setEmailToField(TextField tf, String val) {
		if (!tf.getText().isEmpty()) {

			String[] mailAry = val.split(",");

			if (mailAry.length > 0) {
				for (String first : mailAry) {
					if (!isDuplicated(tf, first))
						tf.setText(tf.getText() + ", " + first);
				}
			} else {
				if (!isDuplicated(tf, val))
					tf.setText(tf.getText() + ", " + val);
			}

		} else
			tf.setText(val);
	}

	public boolean isDuplicated(TextField tf, String val) {
		System.out.println("val = " + val);
		System.out.println("textfield = " + tf.getText());
		if (tf.getText().toLowerCase().contains(val.toLowerCase().trim()))
			return true;
		return false;
	}
}
