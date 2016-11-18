import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class frmLogin extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private static JTextField txtUser;
	private static JPasswordField txtPswd;
	static frmLogin dialog;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		dialog = new frmLogin();
	}

	public frmLogin() {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(Color.ORANGE);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblMailAlarmas = new JLabel("Mail Alarmas NNOC");
		lblMailAlarmas.setForeground(Color.BLACK);
		lblMailAlarmas.setFont(new Font("Tahoma", Font.PLAIN, 34));
		lblMailAlarmas.setBounds(59, 11, 317, 67);
		contentPanel.add(lblMailAlarmas);

		JLabel lblUsuario = new JLabel("Usuario:");
		lblUsuario.setBounds(127, 114, 69, 14);
		contentPanel.add(lblUsuario);

		JLabel lblContrasea = new JLabel("Contrase\u00F1a:");
		lblContrasea.setBounds(107, 148, 69, 14);
		contentPanel.add(lblContrasea);

		txtUser = new JTextField();
		txtUser.setBounds(193, 114, 86, 21);

		contentPanel.add(txtUser);
		txtUser.setColumns(10);

		txtPswd = new JPasswordField();
		txtPswd.setBounds(193, 146, 86, 17);

		contentPanel.add(txtPswd);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(new Color(100, 149, 237));
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dialog.setVisible(false);
						String user = frmLogin.this.txtUser.getText();
						String password = String.valueOf(frmLogin.this.txtPswd.getPassword());
						frmMain frmMain = new frmMain(user, password);
						frmMain.setVisible(true);
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						System.exit(0);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}

		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);
		setUndecorated(true);
		setVisible(true);

	}
}
