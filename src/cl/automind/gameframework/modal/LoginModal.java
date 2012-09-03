package cl.automind.gameframework.modal;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginModal extends JPanel{

	private static final long serialVersionUID = 1L;
	private JTextField username;
	private JPasswordField password;
	private final JButton btn = new JButton("Ingresar");

	public LoginModal(final PasswordListenerInterface pl){
		username = new JTextField();
		password = new JPasswordField();
		setLayout(new GridLayout(3,2));
		setBackground(new Color(80,80,80));
		
		JLabel lblTeam = new JLabel("Equipo");
		JLabel lblPassword = new JLabel("Contraseña");
		lblTeam.setForeground(Color.WHITE);
		lblPassword.setForeground(Color.WHITE);
		add(lblTeam);
		add(username);
		add(lblPassword);
		add(password);
		add(new JLabel(""));
		ActionListener al = new ActionListener(){
			public void actionPerformed(ActionEvent e){
				disableLoginButton();
				pl.doLogin(username.getText(), password.getPassword());
			}
		};
		username.addActionListener(al);
		password.addActionListener(al);
		btn.addActionListener(al);
		
		add(btn);
	}

	public void disableLoginButton() {
		btn.setEnabled(false);
	}

	public void enableLoginButton() {
		btn.setEnabled(true);
	}
	
	

	
}
