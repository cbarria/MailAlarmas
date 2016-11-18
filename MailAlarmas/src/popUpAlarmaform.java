import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

public class popUpAlarmaform extends JFrame {

	private JPanel contentPane;

	public popUpAlarmaform() {

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 817, 458);
		contentPane = new JPanel();
		contentPane.setBackground(Color.ORANGE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblHora = new JLabel("Hora:");
		lblHora.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblHora.setBackground(Color.WHITE);
		lblHora.setBounds(8, 0, 555, 23);
		contentPane.add(lblHora);

		JLabel lblDe = new JLabel("De: ");
		lblDe.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDe.setBounds(8, 27, 555, 23);
		contentPane.add(lblDe);

		JLabel lblAsunto = new JLabel("Asunto:");
		lblAsunto.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblAsunto.setBounds(8, 53, 54, 23);
		contentPane.add(lblAsunto);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(7, 142, 783, 245);
		contentPane.add(scrollPane);

		JTextPane textPane = new JTextPane();
		scrollPane.setViewportView(textPane);

		JButton btnAceptar = new JButton("Aceptar");
		btnAceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

			}
		});
		btnAceptar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnAceptar.setBounds(357, 392, 86, 23);
		contentPane.add(btnAceptar);

		JTextArea txtSubject = new JTextArea();
		txtSubject.setEditable(false);
		txtSubject.setLineWrap(true);
		txtSubject.setBounds(8, 77, 556, 58);
		contentPane.add(txtSubject);
	}
}
