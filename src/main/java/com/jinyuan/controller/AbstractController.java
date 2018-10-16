package com.jinyuan.controller;

import com.jinyuan.model.EmailValidator;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.swing.filechooser.FileSystemView;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;

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

	public ImageView getFileIcon(File aFile) {
		Image fxImage;

		File selectedFile = aFile;
		String fileName = selectedFile.getName();
		String fileExtension = "";
		if (fileName.indexOf('.') > 0) {
			fileExtension = fileName.substring(fileName.lastIndexOf("."), selectedFile.getName().length());
		}

		if (!selectedFile.exists()) {
			try {
				selectedFile = File.createTempFile("icon", fileExtension);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		FileSystemView view = FileSystemView.getFileSystemView();
		javax.swing.Icon icon = view.getSystemIcon(selectedFile);

		BufferedImage bufferedImage = new BufferedImage(
				icon.getIconWidth(),
				icon.getIconHeight(),
				BufferedImage.TYPE_INT_ARGB
		);
		icon.paintIcon(null, bufferedImage.getGraphics(), 0, 0);

		fxImage = SwingFXUtils.toFXImage(bufferedImage, null);
		ImageView retV = new javafx.scene.image.ImageView(fxImage);
		retV.setUserData(selectedFile);
		return retV;
	}

	public boolean isDuplicated(TextField tf, String val) {
		System.out.println("val = " + val);
		System.out.println("textfield = " + tf.getText());
		if (tf.getText().toLowerCase().contains(val.toLowerCase().trim()))
			return true;
		return false;
	}

	private static final ObjectProperty<Locale> locale;

	static {
		locale = new SimpleObjectProperty<>(getDefaultLocale());
		locale.addListener((observable, oldValue, newValue) -> Locale.setDefault(newValue));
	}

	/**
	 * get the supported Locales.
	 *
	 * @return List of Locale objects.
	 */
	public static List<Locale> getSupportedLocales() {
		return new ArrayList<>(Arrays.asList(Locale.ENGLISH, Locale.CHINA));
	}

	public static String get(final String key, final Object... args) {
		ResourceBundle bundle = ResourceBundle.getBundle("com/jinyuan/resources/lang", getLocale());
		return MessageFormat.format(bundle.getString(key), args);
	}

	/* get the default locale. This is the systems default if contained in the supported locales, english otherwise.
	 *
	 * @return
	 */
	public static Locale getDefaultLocale() {
		Locale sysDefault = Locale.getDefault();
		return getSupportedLocales().contains(sysDefault) ? sysDefault : Locale.ENGLISH;
	}

	public static Locale getLocale() {
		return locale.get();
	}

	public static void setLocale(Locale locale) {
		localeProperty().set(locale);
		Locale.setDefault(locale);
	}

	public static ObjectProperty<Locale> localeProperty() {
		return locale;
	}
}
