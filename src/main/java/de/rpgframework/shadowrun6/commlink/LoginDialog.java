package de.rpgframework.shadowrun6.commlink;

import java.util.ResourceBundle;

import org.prelle.javafx.CloseType;
import org.prelle.javafx.JavaFXConstants;
import org.prelle.javafx.ManagedDialog;

import de.rpgframework.ResourceI18N;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * @author prelle
 *
 */
public class LoginDialog extends ManagedDialog {

	private final static ResourceBundle RES = ResourceBundle.getBundle(LoginDialog.class.getName());;
	
	private TextField tfUser;
	private PasswordField tfPass;

	//-------------------------------------------------------------------
	public LoginDialog(int failedAttempts) {
		super(ResourceI18N.get(RES, "title"), null, CloseType.OK, CloseType.CANCEL);
		initComponents();
		initLayout(failedAttempts);
		initInteractivity();
	}

	//-------------------------------------------------------------------
	private void initComponents() {
		tfUser = new TextField();
		tfUser.setPromptText(ResourceI18N.get(RES,"field.user.prompt"));
		
		tfPass = new PasswordField();
	}

	//-------------------------------------------------------------------
	private void initLayout(int failed) {
		Label lbUser = new Label(ResourceI18N.get(RES,"field.user.label"));
		lbUser.getStyleClass().add(JavaFXConstants.STYLE_HEADING5);
		Label lbPass = new Label(ResourceI18N.get(RES,"field.pass.label"));
		lbPass.getStyleClass().add(JavaFXConstants.STYLE_HEADING5);
		
		VBox layout = new VBox(lbUser, tfUser, lbPass, tfPass);
		if (failed>0) {
			Label warn = new Label(ResourceI18N.format(RES, "warn.attempts", failed));
			warn.setStyle("-fx-text-fill: red");
			layout.getChildren().add(0, warn);
			VBox.setMargin(lbUser, new Insets(20, 0, 0, 0));
		}
		
		VBox.setMargin(lbPass, new Insets(20, 0, 0, 0));
		
		setContent(layout);
	}

	//-------------------------------------------------------------------
	private void initInteractivity() {
		tfUser.textProperty().addListener( (ov,o,n) -> {
			if (n==null || n.length()<4) {
				tfUser.getStyleClass().add("invalid-content");
			} else {
				tfUser.getStyleClass().remove("invalid-content");
			}
			refreshButtons();
		});
		tfPass.textProperty().addListener( (ov,o,n) -> {
			if (n==null || n.length()<6) {
				tfPass.getStyleClass().add("invalid-content");
			} else {
				tfPass.getStyleClass().remove("invalid-content");
			}
			refreshButtons();
		});
	}

	//-------------------------------------------------------------------
	private boolean isDataValid() {
		String user = tfUser.getText();
		String pass = tfPass.getText();
		if (user==null || user.length()<4 )
			return false;
		if (pass==null || pass.length()<6 )
			return false;
		return true;
	}

	//-------------------------------------------------------------------
	private void refreshButtons() {
		boolean valid = isDataValid();
		buttonDisabledProperty().put(CloseType.OK, !valid);
	}

	//-------------------------------------------------------------------
	public String getLogin() { return tfUser.getText(); }
	public String getPassword() { return tfPass.getText(); }
	//-------------------------------------------------------------------
	public void setLogin(String value) { tfUser.setText(value); refreshButtons();}
	public void setPassword(String value) { tfPass.setText(value); refreshButtons();}

}
