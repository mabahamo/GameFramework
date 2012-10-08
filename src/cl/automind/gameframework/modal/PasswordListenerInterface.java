package cl.automind.gameframework.modal;

public interface PasswordListenerInterface {

	public void doLogin(String text, char[] password);

	public void loggedIn();

	public void loginFailed(String arg0);

	public void disconnected(boolean arg0, String arg1);

}
