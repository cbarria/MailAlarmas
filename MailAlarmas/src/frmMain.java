import java.awt.AWTException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingWorker;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.PropertySet;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.BasePropertySet;
import microsoft.exchange.webservices.data.core.enumeration.property.BodyType;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.enumeration.search.LogicalOperator;
import microsoft.exchange.webservices.data.core.enumeration.search.SortDirection;
import microsoft.exchange.webservices.data.core.enumeration.service.ConflictResolutionMode;
import microsoft.exchange.webservices.data.core.service.folder.Folder;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.core.service.schema.EmailMessageSchema;
import microsoft.exchange.webservices.data.core.service.schema.ItemSchema;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.AttachmentCollection;
import microsoft.exchange.webservices.data.property.complex.FileAttachment;
import microsoft.exchange.webservices.data.property.complex.FolderId;
import microsoft.exchange.webservices.data.property.complex.ItemId;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import microsoft.exchange.webservices.data.search.FindItemsResults;
import microsoft.exchange.webservices.data.search.FolderView;
import microsoft.exchange.webservices.data.search.ItemView;
import microsoft.exchange.webservices.data.search.filter.SearchFilter;
import microsoft.exchange.webservices.data.search.filter.SearchFilter.SearchFilterCollection;

public class frmMain extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String user;
	private String pass;
	private JTable table;
	private DefaultTableModel model;
	private JProgressBar progressBar;
	private JLabel lblTiempo;
	private Date horaInicio = new Date();
	private JFrame frmMain;
	private FolderId idCarpeta;
	private JTextPane txtMsj;

	/**
	 * Launch the application.
	 */

	public frmMain(String usr, String pss) {
		//getContentPane().setBackground(new Color(30, 144, 255));
		setTitle("Correo Alertas v1.0");
		setIconImage(Toolkit.getDefaultToolkit().getImage("alarm16x16.png"));
		this.user = usr;
		this.pass = pss;
		System.out.println(horaInicio.toString());

		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 1013, 659);
		setLocationRelativeTo(null);
		setResizable(false);
		getContentPane().setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(10, 32, 977, 302);
		getContentPane().add(scrollPane);

		table = new JTable(new DefaultTableModel(new Object[] { "ID", "Hora", "Correo", "Asunto", "Mensaje", "Imagen" }, 0));

		miRender ft = new miRender(2);
		table.setDefaultRenderer(Object.class, ft);

		TableColumnModel tcm = table.getColumnModel();
		tcm.removeColumn(tcm.getColumn(0)); //saca el ID
		tcm.removeColumn(tcm.getColumn(3)); //saca el mensaje
		tcm.removeColumn(tcm.getColumn(3)); //saca la Imagen

		model = (DefaultTableModel) table.getModel();
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getColumnModel().getColumn(0).setPreferredWidth(175);
		table.getColumnModel().getColumn(1).setPreferredWidth(170);
		table.getColumnModel().getColumn(2).setPreferredWidth((scrollPane.getWidth() - 348));

		ListSelectionModel cellSelectionModel = table.getSelectionModel();
		cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		cellSelectionModel.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				txtMsj.setText("\n" + model.getValueAt(table.getSelectedRow(), 4).toString());
				txtMsj.setCaretPosition(0);
				if (model.getValueAt(table.getSelectedRow(), 5) != null) {
					txtMsj.insertIcon(new ImageIcon(model.getValueAt(table.getSelectedRow(), 5).toString()));
				}
			}
		});

		scrollPane.setViewportView(table);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 368, 977, 242);
		getContentPane().add(scrollPane_1);

		txtMsj = new JTextPane();
		txtMsj.setFont(new Font("Monospaced", Font.PLAIN, 13));
		txtMsj.setEditable(false);

		scrollPane_1.setViewportView(txtMsj);

		JLabel lblMensajeDeAlerta = new JLabel("Mensaje de Alerta");
		lblMensajeDeAlerta.setBounds(10, 343, 122, 14);
		getContentPane().add(lblMensajeDeAlerta);

		progressBar = new JProgressBar();
		progressBar.setForeground(Color.RED);
		progressBar.setBounds(821, 11, 166, 14);
		getContentPane().add(progressBar);

		lblTiempo = new JLabel("Tiempo: 00 min, 00 seg");
		lblTiempo.setBounds(670, 11, 147, 14);
		getContentPane().add(lblTiempo);

		selectFolder();
		gosystray();
		hardWorkThread worker = new hardWorkThread();
		worker.execute();
		setVisible(true);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void selectFolder() {
		JPanel panel = new JPanel();
		panel.add(new JLabel("Seleccione la Carpeta a Monitorear:"));
		DefaultComboBoxModel model = new DefaultComboBoxModel();

		ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2007_SP1);
		ExchangeCredentials credentials = new WebCredentials(user, pass, "vtr");
		service.setCredentials(credentials);
		ArrayList<String> nombreCarpetas = new ArrayList<String>();
		ArrayList<FolderId> idCarpetas = new ArrayList<FolderId>();

		try {
			service.setUrl(new URI("https://m.vtr.cl/EWS/Exchange.asmx"));
			Folder rootfolder = Folder.bind(service, WellKnownFolderName.MsgFolderRoot);

			for (Folder folder : rootfolder.findFolders(new FolderView(rootfolder.getChildFolderCount()))) {
				model.addElement("*" + folder.getDisplayName());
				nombreCarpetas.add(folder.getDisplayName());
				idCarpetas.add(folder.getId());
				//System.out.println(folder.getDisplayName() + " " + folder.getId());

				Folder subfolders = Folder.bind(service, folder.getId());
				if (subfolders.getChildFolderCount() > 0)
					for (Folder subfolder : subfolders.findFolders(new FolderView(subfolders.getChildFolderCount()))) {
						model.addElement("-" + subfolder.getDisplayName());
						nombreCarpetas.add(subfolder.getDisplayName());
						idCarpetas.add(subfolder.getId());
						//System.out.println(subfolder.getDisplayName() + " " + subfolder.getId());
					}
			}
			JComboBox comboBox = new JComboBox(model);
			panel.add(comboBox);
			int result = JOptionPane.showConfirmDialog(null, panel, "Carpetas", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
			switch (result) {
			case JOptionPane.OK_OPTION:
				String carpetaSeleccionada = comboBox.getSelectedItem().toString().replaceAll("[\\*\\-]", "");
				//System.out.println(nombreCarpetas.size() + " " + idCarpetas.size() + " " + carpetaSeleccionada);
				for (int i = 0; i < nombreCarpetas.size(); i++) {
					//System.out.println("Carpeta: " + carpetaSeleccionada + " Nombre: " + nombreCarpetas.get(i));
					if (nombreCarpetas.get(i).equals(carpetaSeleccionada)) {
						idCarpeta = idCarpetas.get(i);
						System.out.println("Carpeta seleccionada encontrada!");
						break;
					}
				}
				break;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

	}

	public void gosystray() {
		//Check the SystemTray is supported
		if (!SystemTray.isSupported()) {
			System.out.println("SystemTray is not supported");
			return;
		}
		final PopupMenu popup = new PopupMenu();
		final TrayIcon trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().getImage("alarm16x16.png"));
		final SystemTray tray = SystemTray.getSystemTray();

		MenuItem openItem = new MenuItem("Open");
		MenuItem exitItem = new MenuItem("Exit");

		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});

		openItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVisible(true);
			}
		});

		popup.add(openItem);
		popup.add(exitItem);

		trayIcon.setPopupMenu(popup);

		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			System.out.println("TrayIcon could not be added.");
		}
	}

	@SuppressWarnings("deprecation")
	public void checkAlarm() {
		progressBar.setIndeterminate(true);
		//model.setRowCount(0);
		//txtMsg.setText(null);
		setCursor(Cursor.WAIT_CURSOR);
		ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2007_SP1);
		ExchangeCredentials credentials = new WebCredentials(user, pass, "vtr");
		service.setCredentials(credentials);

		try {
			service.setUrl(new URI("https://m.vtr.cl/EWS/Exchange.asmx"));
			//service.setTraceEnabled(true);

			//************************ BUSCAR MENSAJES
			PropertySet itempropertyset = new PropertySet(BasePropertySet.FirstClassProperties); // es mucha info, ojala filtrar para disminuir la carga y ganar velocidad. // BasePropertySet.IdOnly, ItemSchema.Subject, ItemSchema.DateTimeReceived,EmailMessageSchema.From																				  
			itempropertyset.setRequestedBodyType(BodyType.Text);

			ItemView view = new ItemView(50);
			view.setPropertySet(itempropertyset);
			view.getOrderBy().add(ItemSchema.DateTimeReceived, SortDirection.Descending);

			SearchFilterCollection searchFilter1 = new SearchFilterCollection(LogicalOperator.And);
			searchFilter1.add(new SearchFilter.IsEqualTo(EmailMessageSchema.IsRead, false));
			searchFilter1.add(new SearchFilter.IsGreaterThan(ItemSchema.DateTimeReceived, horaInicio));
			searchFilter1.add(new SearchFilter.IsEqualTo(EmailMessageSchema.From, "peakflow@columbus.co")); // Peakflow!

			SearchFilterCollection searchFilter2 = new SearchFilterCollection(LogicalOperator.And);
			searchFilter2.add(new SearchFilter.IsEqualTo(EmailMessageSchema.IsRead, false));
			searchFilter2.add(new SearchFilter.IsGreaterThan(ItemSchema.DateTimeReceived, horaInicio));
			searchFilter2.add(new SearchFilter.IsEqualTo(EmailMessageSchema.From, "cacti@onelinkpr.com")); // Monitorea Trafico.
			searchFilter2.add(new SearchFilter.ContainsSubstring(ItemSchema.Subject, "ALERT:"));

			SearchFilterCollection searchFilter3 = new SearchFilterCollection(LogicalOperator.And);
			searchFilter3.add(new SearchFilter.IsEqualTo(EmailMessageSchema.IsRead, false));
			searchFilter3.add(new SearchFilter.IsGreaterThan(ItemSchema.DateTimeReceived, horaInicio));
			searchFilter3.add(new SearchFilter.IsEqualTo(EmailMessageSchema.From, "solarwind@onelinkpr.com")); //  
			searchFilter3.add(new SearchFilter.ContainsSubstring(ItemSchema.Subject, "Interface"));
			searchFilter3.add(new SearchFilter.ContainsSubstring(ItemSchema.Body, "Down."));

			SearchFilterCollection searchFilter4 = new SearchFilterCollection(LogicalOperator.And);
			searchFilter4.add(new SearchFilter.IsEqualTo(EmailMessageSchema.IsRead, false));
			searchFilter4.add(new SearchFilter.IsGreaterThan(ItemSchema.DateTimeReceived, horaInicio));
			searchFilter4.add(new SearchFilter.IsEqualTo(EmailMessageSchema.From, "threat@att.com")); // Peakflow!

			SearchFilterCollection searchFilter5 = new SearchFilterCollection(LogicalOperator.And);
			searchFilter5.add(new SearchFilter.IsEqualTo(EmailMessageSchema.IsRead, false));
			searchFilter5.add(new SearchFilter.IsGreaterThan(ItemSchema.DateTimeReceived, horaInicio));
			searchFilter5.add(new SearchFilter.IsEqualTo(EmailMessageSchema.From, "winpak@liberty.com")); // Energía

			List<SearchFilterCollection> FilterCollectionList = new ArrayList<SearchFilterCollection>();
			FilterCollectionList.add(searchFilter1);
			FilterCollectionList.add(searchFilter2);
			FilterCollectionList.add(searchFilter3);
			FilterCollectionList.add(searchFilter4);
			FilterCollectionList.add(searchFilter5);

			for (SearchFilterCollection filter : FilterCollectionList) {
				//Revisa el contenido de las carpetas fuera del inbox.
				Folder rootfolder = Folder.bind(service, idCarpeta); //WellKnownFolderName.Inbox
				if (rootfolder.getChildFolderCount() > 0) {
					for (Folder folder : rootfolder.findFolders(new FolderView(rootfolder.getChildFolderCount()))) {
						FindItemsResults<Item> resultsroot = service.findItems(folder.getId(), filter, view);
						entregarResultados(resultsroot, service, itempropertyset);
						/*
						Folder subfolders = Folder.bind(service, folder.getId());					
						if (subfolders.getChildFolderCount() > 0)
							for (Folder subfolder : subfolders.findFolders(new FolderView(subfolders.getChildFolderCount()))) {
								FindItemsResults<Item> results = service.findItems(subfolder.getId(), filter, view);
								entregarResultados(results, service, itempropertyset);
							}
						*/
					}
				}
				//Revisa el contenido del inbox.	    
				FindItemsResults<Item> results = service.findItems(idCarpeta, filter, view); //
				entregarResultados(results, service, itempropertyset);
			}

			progressBar.setIndeterminate(false);
			setCursor(Cursor.DEFAULT_CURSOR);

		} catch (Exception e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(null, e1.getMessage(), "ERROR!", JOptionPane.ERROR_MESSAGE);
			System.exit(ERROR);
		}

	}

	@SuppressWarnings("deprecation")
	public void entregarResultados(FindItemsResults<Item> results, ExchangeService service, PropertySet itempropertyset) throws Exception {
		int totalTabla = table.getRowCount();
		if (results.getTotalCount() != 0) { // si no hay correos con el criterio del filtro, return.   
			service.loadPropertiesForItems(results, itempropertyset); //cargar los items
			System.out.println("Total correos encontrados: " + results.getTotalCount());
			for (Item item : results) {
				if (item instanceof EmailMessage) {
					if (table.getRowCount() == 0) {
						model.addRow(new Object[] { item.getId(), item.getDateTimeReceived(), ((EmailMessage) item).getSender().getAddress(), item.getSubject(), item.getBody(), getAttach(item) });
						mensajePopup(item.getId(), item.getDateTimeReceived(), ((EmailMessage) item).getSender().getAddress(), item.getSubject(), item.getBody(), getAttach(item));

					} else {
						for (int i = 0; i < totalTabla; i++) {
							if (model.getValueAt(i, 0).toString().equals(item.getId().toString())) {
								progressBar.setIndeterminate(false);
								setCursor(Cursor.DEFAULT_CURSOR);
								return;
							}
						}
						model.addRow(new Object[] { item.getId(), item.getDateTimeReceived(), ((EmailMessage) item).getSender().getAddress(), item.getSubject(), item.getBody(), getAttach(item) });
						mensajePopup(item.getId(), item.getDateTimeReceived(), ((EmailMessage) item).getSender().getAddress(), item.getSubject(), item.getBody(), getAttach(item));

					}
				}
			}
		}
	}

	public String getAttach(Item item) {
		String filename = null;
		try {
			if (item.getHasAttachments()) {
				AttachmentCollection attachmentsCol = item.getAttachments();
				for (int i = 0; i < attachmentsCol.getCount(); i++) {
					FileAttachment attachment = (FileAttachment) attachmentsCol.getPropertyAtIndex(i);
					System.out.println(attachment.getContentType());
					if (attachment.getContentType().contains("image")) {
						attachment.load(System.getProperty("java.io.tmpdir") + attachment.getName());
						filename = System.getProperty("java.io.tmpdir") + attachment.getName();
						System.out.println(filename);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return filename;
	}

	public void mensajePopup(ItemId itemId, Date hora, String sender, String asunto, MessageBody messageBody, String filename) {
		JDialog popUpAlarma = new JDialog(frmMain, "NOTIFICACION CORREO ALARMA!");
		popUpAlarma.setAlwaysOnTop(true);
		popUpAlarma.setBounds(100, 100, 783, 458);
		//popUpAlarma.getContentPane().setBackground(Color.ORANGE);
		popUpAlarma.setUndecorated(false);
		//popUpAlarma.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
		popUpAlarma.setLocationRelativeTo(null);
		popUpAlarma.setResizable(false);
		popUpAlarma.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

		JPanel contentPane2 = new JPanel();
		contentPane2.setLayout(null);

		JLabel lblHora = new JLabel("Hora: " + hora);
		lblHora.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblHora.setBackground(Color.WHITE);
		lblHora.setBounds(8, 0, 555, 23);
		contentPane2.add(lblHora);

		JLabel lblDe = new JLabel("De: " + sender);
		lblDe.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDe.setBounds(8, 27, 555, 23);
		contentPane2.add(lblDe);

		JLabel lblAsunto = new JLabel("Asunto:");
		lblAsunto.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblAsunto.setBounds(8, 53, 54, 23);
		contentPane2.add(lblAsunto);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(7, 142, 760, 245);
		contentPane2.add(scrollPane);

		JTextArea txtSubject = new JTextArea();
		Border border = BorderFactory.createLineBorder(Color.BLACK);
		txtSubject.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(0, 0, 0, 0)));
		txtSubject.setEditable(false);
		txtSubject.setLineWrap(true);
		txtSubject.setFont(new Font("Monospaced", Font.PLAIN, 13));
		txtSubject.setBounds(8, 77, 760, 58);
		txtSubject.setText(asunto);
		contentPane2.add(txtSubject);

		JTextPane textPane = new JTextPane();
		textPane.setEditable(false);
		textPane.setFont(new Font("Monospaced", Font.PLAIN, 13));
		//textPane.setContentType("text/html");
		textPane.setText("\n" + messageBody.toString());
		if (filename != null) {
			textPane.insertIcon(new ImageIcon(filename));
		}
		scrollPane.setViewportView(textPane);

		JButton btnAceptar = new JButton("Aceptar");
		btnAceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				try {
					ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2007_SP1);
					ExchangeCredentials credentials = new WebCredentials(user, pass, "vtr");
					service.setCredentials(credentials);
					service.setUrl(new URI("https://m.vtr.cl/EWS/Exchange.asmx"));
					EmailMessage emailMessage = EmailMessage.bind(service, itemId);
					emailMessage.setIsRead(true);
					emailMessage.update(ConflictResolutionMode.AlwaysOverwrite);
					//System.out.println(emailMessage.getIsRead());
					popUpAlarma.dispose();

				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR!", JOptionPane.ERROR_MESSAGE);
				}

			}
		});

		btnAceptar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnAceptar.setBounds(357, 392, 86, 23);
		contentPane2.add(btnAceptar);
		contentPane2.setBackground(Color.ORANGE);
		popUpAlarma.getContentPane().add(contentPane2);
		popUpAlarma.setVisible(true);
		popUpAlarma.repaint();
	}

	public class hardWorkThread extends SwingWorker<String, String> {
		@Override
		protected String doInBackground() throws Exception {
			long lastTime = System.currentTimeMillis();
			Boolean SweepNow = true; //Ejecutar la tarea la primera vez
			while (true) {
				if (SweepNow) {
					System.out.println("Auto Start First Time");

					checkAlarm();
					SweepNow = false;
					lastTime = System.currentTimeMillis();

				} else if (System.currentTimeMillis() - lastTime > (1000 * 60 * 0.5)) {
					System.out.println("Auto Start by Time");
					checkAlarm();
					lastTime = System.currentTimeMillis(); //Sacar para repetir!
				}

				String CountDown = String.format("%02d min, %02d seg", TimeUnit.MILLISECONDS.toMinutes((System.currentTimeMillis() - lastTime)), TimeUnit.MILLISECONDS.toSeconds((System.currentTimeMillis() - lastTime)) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((System.currentTimeMillis() - lastTime))));
				lblTiempo.setText("Tiempo: " + CountDown);
			}
		}
	}

	public class miRender extends DefaultTableCellRenderer {

		private static final long serialVersionUID = 1L;
		private int columna_patron;

		public miRender(int Colpatron) {
			this.columna_patron = Colpatron;
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused, int row, int column) {
			setBackground(Color.white);//color de fondo
			table.setForeground(Color.black);//color de texto
			//Si la celda corresponde a una fila con estado FALSE, se cambia el color de fondo a rojo
			if (table.getValueAt(row, columna_patron).toString().toLowerCase().contains("alert")) {
				setBackground(Color.yellow);
			}
			if (table.getValueAt(row, columna_patron).toString().toLowerCase().contains("peakflow")) {
				setBackground(Color.orange);
			}
			if (table.getValueAt(row, columna_patron).toString().toLowerCase().contains("interface")) {
				setBackground(Color.red);
			}
			if (table.getValueAt(row, columna_patron).toString().toLowerCase().contains("alarm")) {
				setBackground(Color.red);
			}
			super.getTableCellRendererComponent(table, value, selected, focused, row, column);
			return this;
		}

	}
}
