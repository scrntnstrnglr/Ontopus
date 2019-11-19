package com.tcd.ds.kde.ontps.gui;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.ResultSet;

import com.tcd.ds.kde.ontps.model.CreateModel;
import com.tcd.ds.kde.ontps.model.Queries;
import com.tcd.ds.kde.ontps.model.QueryModel;
import com.tcd.ds.kde.ontps.utils.FileManager;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Choice;
import java.awt.Color;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.awt.event.ItemEvent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class ApplicationWindow {

	public JFrame frame;
	private OntModel ont;
	private Queries query1;
	private String queryDirectory;
	private static String imgDirectory;
	private String resourceDirectory;
	private JScrollPane scrollPane, scrollPaneTab;
	private static JTextArea editorPane_1;
	private CreateModel modelCreator = new CreateModel();
	private static JTextArea lines;
	private JTextField textFieldFileImported;
	private JTextField textFieldExecTime;
	private JTextField textFieldCols;
	private JTextField textFieldRows;

	/**
	 * Create the application.
	 * 
	 * @throws IOException
	 * @throws FontFormatException
	 */
	public ApplicationWindow(String resourceDirectory, String queryDirectory, String imgDirectory)
			throws IOException, FontFormatException {
		this.queryDirectory = queryDirectory;
		this.imgDirectory = imgDirectory;
		this.resourceDirectory = resourceDirectory;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @throws IOException
	 * @throws FontFormatException
	 */
	private void initialize() throws IOException, FontFormatException {
		lines = new JTextArea("1");
		lines.setBackground(Color.LIGHT_GRAY);
	    lines.setEditable(false);
		query1 = new Queries();
		frame = new JFrame();
		frame.setTitle("Ontopus");
		// frame.getRootPane().setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK),
		// BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		ImageIcon iconimg = new ImageIcon(imgDirectory + "\\icon.png");
		ImageIcon iconlarge = new ImageIcon(imgDirectory + "\\iconlarge.png");
		frame.setIconImage(iconimg.getImage());
		frame.setBounds(440, 50, 1162, 1028);
		// Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		// frame.setBounds(0,0,screenSize.width, screenSize.height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		InputStream is = new FileInputStream(new File(resourceDirectory + "\\fonts\\font2.otf"));
		Font font = Font.createFont(Font.TRUETYPE_FONT, is);
		is = new FileInputStream(new File(resourceDirectory + "\\fonts\\font3.otf"));
		Font fontLogo = Font.createFont(Font.TRUETYPE_FONT, is);
		

		final JLabel imageLabel = new JLabel(new ImageIcon(iconlarge.getImage()));
		imageLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		final JLabel textLabel = new JLabel("ONTOPUS v0.0");
		textLabel.setFont(fontLogo.deriveFont(32f));
		textLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		textLabel.setBorder(BorderFactory.createEmptyBorder(13, 0, 0, 0));
		final JPanel logoPanel = new JPanel();
		Box box = Box.createVerticalBox();
		box.add(imageLabel);
		box.add(textLabel);
		logoPanel.add(box);
		scrollPane = new JScrollPane(logoPanel);
		scrollPane.setBounds(15, 360, 1112, 320);
		scrollPane.setBackground(Color.WHITE);
		scrollPane.setBorder(null);
		frame.getContentPane().add(scrollPane);

		editorPane_1 = new JTextArea();
		editorPane_1.setSize(169, 445);
		editorPane_1.setBackground(Color.BLACK);
		editorPane_1.setForeground(Color.WHITE);
		editorPane_1.setEditable(false);
		editorPane_1.setCaretColor(Color.WHITE);
		editorPane_1.getCaret().setVisible(true);
		editorPane_1.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		frame.getContentPane().add(editorPane_1);

		PrintStream stream = new PrintStream(System.out) {
			@Override
			public void print(String s) {
				SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
				Date date = new Date(System.currentTimeMillis());
				ApplicationWindow.this.editorPane_1.append(formatter.format(date)+" "+this.getClass().getName()+" "+s+"\n");
			}
		};
		System.setOut(stream);

		/*
		System.out.println("Configuring properties...");
		Properties loggingProperties = new Properties();
		loggingProperties.load(new FileInputStream(resourceDirectory + "\\log4j.properties"));

		PropertyConfigurator.configure(loggingProperties); */
		System.out.print("INFO Application Launched...");

		JScrollPane scrollPaneLog = new JScrollPane(editorPane_1);
		scrollPaneLog.setBounds(15, 679, 813, 277);
		scrollPaneLog.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createCompoundBorder(new MatteBorder(0, 5, 0, 0, Color.decode("#DA450D")),
						new MatteBorder(1, 0, 1, 1, Color.BLACK)),
				"Logger"));
		frame.getContentPane().add(scrollPaneLog);

		JPanel panel = new JPanel();
		panel.setBounds(15, 16, 1112, 311);
		// panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1,
		// 5, 1, 1, Color.MAGENTA), "Query"));
		panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createCompoundBorder(
				new MatteBorder(0, 5, 0, 0, Color.BLUE), new MatteBorder(1, 0, 1, 1, Color.BLACK)), "Query"));
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(71, 53, 1026, 242);
		panel.add(scrollPane_1);

	    JTextArea editorPane = new JTextArea();
	    lines = new JTextArea("1");
	    lines.setBackground(Color.LIGHT_GRAY);
	    lines.setEditable(false);
	    editorPane.getDocument().addDocumentListener(new DocumentListener() {
	          public String getText() {
	             int caretPosition = editorPane.getDocument().getLength();
	             Element root = editorPane.getDocument().getDefaultRootElement();
	             String text = "1" + System.getProperty("line.separator");
	                for(int i = 2; i < root.getElementIndex(caretPosition) + 2; i++) {
	                   text += i + System.getProperty("line.separator");
	                }
	             return text;
	          }
	          @Override
	          public void changedUpdate(DocumentEvent de) {
	             lines.setText(getText());
	          }
	          @Override
	          public void insertUpdate(DocumentEvent de) {
	             lines.setText(getText());
	          }
	          @Override
	          public void removeUpdate(DocumentEvent de) {
	             lines.setText(getText());
	          }
	       });
		editorPane.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				editorPane.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.orange));
			}

			@Override
			public void focusLost(FocusEvent e) {
				editorPane.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
			}
		});
		editorPane.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
		editorPane.setFont(font.deriveFont(17f));
		scrollPane_1.setViewportView(editorPane);
		lines.setBorder(BorderFactory.createEmptyBorder(1,3,1,3));
		editorPane.setBorder(BorderFactory.createEmptyBorder(1, 3, 1, 3));
		scrollPane_1.setRowHeaderView(lines);
		// writeToConsole("##################Welcome To OntoPuss#################\n",
		// editorPane_1);

		FileManager fileManager = new FileManager(queryDirectory);

		Choice choice = new Choice();
		choice.setBounds(10, 21, 1087, 26);
		panel.add(choice);

		/*
		 * JButton button = new JButton(); button.setOpaque(false);
		 * button.setHorizontalAlignment(SwingConstants.LEFT);
		 * button.setContentAreaFilled(false); button.setBorderPainted(true);
		 * button.setBounds(6, 192, 73, 35); formatButton(button, "import.png", null);
		 * panel.add(button);
		 */

		JButton btnImportModel = createButton("Import", "import.png", -12, 53, 86, 32, panel);
		JButton btnCreateModel = createButton("Create Dummy Model", "create.png", -12, 97, 86, 32, panel);
		JButton btnExecute = createButton("Execute", "run.png", -12, 136, 86, 32, panel);
		JButton btnExport = createButton("Export", "export.png", -12, 170, 86, 32, panel);
		JButton btnNewButton = createButton("Clear", "clear.png", -12, 216, 86, 32, panel);
		JButton btnQuit = createButton("Quit", "power.png", -12, 260, 86, 32, panel);
		btnQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?", "WARNING",
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					System.exit(0);

				}
			}
		});

		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				editorPane.setText("");
				scrollPaneTab.setVisible(false);
				scrollPaneTab.removeAll();
				scrollPane.setVisible(true);
				textFieldExecTime.setText("");
				textFieldRows.setText("");
				textFieldCols.setText("");
			}
		});
		btnExecute.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (ont == null) {
					System.out.print("ERROR No model attached. Import or create model first.");
					JOptionPane.showMessageDialog(frame, "No model attached. Import or create model first.");
				} else {
					// writeToConsole("\n>Executing Query...", editorPane_1);
					long startTime = System.currentTimeMillis();
					QueryModel queryModel = new QueryModel(ont);
					String query = editorPane.getText().trim();
					if (query.isEmpty()) {
						System.out.println("ERROR Query editor is blank. No query to execute.");
						JOptionPane.showMessageDialog(frame, "No Query to execute!");
					} else {
						System.out.print("INFO Executing query.");
						ResultSet rs = queryModel.executeQuery(query);
						ArrayList<ArrayList<String>> queryOutput = queryModel.agglomerateResults(rs);
						ArrayList<String> cols = queryOutput.get(0);
						String col[] = new String[cols.size()];
						int i = 0;
						for (String item : cols) {
							System.out.println(item);
							col[i] = item;
							i++;
						}
						DefaultTableModel tableModel = new DefaultTableModel(col, 0);
						for (i = 1; i < queryOutput.size(); i++) {
							tableModel.addRow(queryOutput.get(i).toArray());
						}

						final JTable table = new JTable() {
							private static final long serialVersionUID = 1L;

							public boolean getScrollableTracksViewportWidth() {
								return getPreferredSize().width < getParent().getWidth();
							}
						};

						table.setModel(tableModel);
						scrollPane.setVisible(false);
						table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
						scrollPaneTab.setViewportView(table);
						scrollPaneTab.setVisible(true);

						long stopTime = System.currentTimeMillis();
						long elapsedTime = stopTime - startTime;

						DecimalFormat df = new DecimalFormat("#.#####");
						double elapsed = elapsedTime / 1000.0;

						System.out.print("INFO Execution complete...");
						System.out.print("INFO Columns: " + cols.size() + "\tRows: " + queryOutput.size()
								+ "\tExecution Time: " + " " + df.format(elapsed) + " ms");
						textFieldExecTime.setText(df.format(elapsed) + " ms");
						textFieldCols.setText(cols.size()+"");
						textFieldRows.setText(queryOutput.size()+"");
					}
				}
			}
		});
		btnCreateModel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (ont != null) {
					int opt = JOptionPane.showConfirmDialog(frame, "Ontology already attached. Create new?");
				}
				// writeToConsole("\n>Creating model...", editorPane_1);
				System.out.print("INFO Creating model...");
				ont = modelCreator.createModel();
				// writeToConsole("\n>Model created and attached...", editorPane_1);
				System.out.print("INFO Model created and attached...");
				editorPane.setText(query1.basicQuery1);
			}
		});
		btnImportModel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				int returnVal = chooser.showOpenDialog(frame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					// writeToConsole("\n>Importing model from: " +
					// chooser.getSelectedFile().toString(), editorPane_1);
					System.out.print("INFO Importing model from: " + chooser.getSelectedFile().toString());
					try {
						ont = modelCreator.importFromFile(chooser.getSelectedFile().toString());
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// writeToConsole("\n>Model Imported...", editorPane_1);
					System.out.print("INFO Model Imported");
					textFieldFileImported.setText(chooser.getSelectedFile().toString());
					editorPane.setText(query1.basicQuery1);
				}

			}
		});
		ArrayList<String> itemList = fileManager.getFileNames();
		for (String item : itemList)
			choice.add(item);
		choice.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
			}
		});
		choice.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				System.out.print("INFO Selected item : " + choice.getSelectedItem());
				try {
					editorPane.setText(fileManager.getFileContents(choice.getSelectedItem()));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		JPanel panelOutput = new JPanel();
		panelOutput.setBounds(15, 568, 1112, 360);
		panelOutput.setLayout(null);
		panelOutput.setBackground(Color.cyan);

		JLabel lblColumns = new JLabel();
		lblColumns.setBounds(15, 892, 69, 20);
		frame.getContentPane().add(lblColumns);
		lblColumns.setVisible(false);

		JLabel lblRows = new JLabel();
		lblRows.setBounds(139, 892, 69, 20);
		frame.getContentPane().add(lblRows);
		lblRows.setVisible(false);

		JLabel lblExecutionTime = new JLabel();
		lblExecutionTime.setBounds(249, 892, 200, 20);
		frame.getContentPane().add(lblExecutionTime);
		lblExecutionTime.setVisible(false);

		scrollPaneTab = new JScrollPane();
		scrollPaneTab.setBounds(15, 329, 1112, 320);
		scrollPaneTab.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createCompoundBorder(new MatteBorder(0, 5, 0, 0, Color.decode("#7FC503")),
						new MatteBorder(1, 0, 1, 1, Color.BLACK)),
				"Results"));
		scrollPaneTab.setVisible(false);
		frame.getContentPane().add(scrollPaneTab);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(843, 679, 284, 277);
		panel_1.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createCompoundBorder(new MatteBorder(0, 5, 0, 0, Color.decode("#E3D01F")),
						new MatteBorder(1, 0, 1, 1, Color.BLACK)),
				"Data"));
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblFileImported = new JLabel("File Imported:");
		lblFileImported.setBounds(15, 26, 79, 20);
		panel_1.add(lblFileImported);
		
		textFieldFileImported = new JTextField();
		textFieldFileImported.setEditable(false);
		textFieldFileImported.setBounds(15, 48, 254, 26);
		panel_1.add(textFieldFileImported);
		textFieldFileImported.setColumns(10);
		textFieldFileImported.getCaret().setVisible(true);
		textFieldFileImported.setCaretColor(Color.BLACK);
		
		JLabel lblQueryExecutionTime = new JLabel("Query Execution Time:");
		lblQueryExecutionTime.setBounds(15, 85, 157, 20);
		panel_1.add(lblQueryExecutionTime);
		
		textFieldExecTime = new JTextField();
		textFieldExecTime.setEditable(false);
		textFieldExecTime.setColumns(10);
		textFieldExecTime.setBounds(15, 107, 254, 26);
		panel_1.add(textFieldExecTime);
		
		JLabel lblColumns_1 = new JLabel("Columns:");
		lblColumns_1.setBounds(15, 149, 157, 20);
		panel_1.add(lblColumns_1);
		
		textFieldCols = new JTextField();
		textFieldCols.setEditable(false);
		textFieldCols.setColumns(10);
		textFieldCols.setBounds(15, 172, 254, 26);
		panel_1.add(textFieldCols);
		
		JLabel lblRows_1 = new JLabel("Rows:");
		lblRows_1.setBounds(15, 214, 157, 20);
		panel_1.add(lblRows_1);
		
		textFieldRows = new JTextField();
		textFieldRows.setEditable(false);
		textFieldRows.setColumns(10);
		textFieldRows.setBounds(15, 235, 254, 26);
		panel_1.add(textFieldRows);

	}

	public void writeToConsole(String s, JTextArea pane) {
		try {
			Document doc = pane.getDocument();
			doc.insertString(doc.getLength(), s, null);
		} catch (BadLocationException exc) {
			exc.printStackTrace();
		}
	}

	public void formatButton(JButton button, String image, Color color) {
		button.setIcon(resizeImage(new ImageIcon(imgDirectory + "\\" + image), 23, 23));
		button.setHorizontalAlignment(SwingConstants.LEFT);
		button.setOpaque(false);
		button.setContentAreaFilled(false);
		button.setBorderPainted(true);
		button.setBorder(
				BorderFactory.createCompoundBorder(new MatteBorder(1, 3, 1, 1, color), new EmptyBorder(6, 6, 6, 13)));

	}

	public ImageIcon resizeImage(ImageIcon img, int height, int width) {
		Image image = img.getImage();
		Image newimg = image.getScaledInstance(height, width, java.awt.Image.SCALE_SMOOTH);
		return new ImageIcon(newimg);
	}

	public static JTextArea getTextArea() {
		return editorPane_1;
	}

	private static JButton createButton(String name, String image, int x, int y, int width, int height, JPanel panel) {
		JButton button = new JButton(new ImageIcon(imgDirectory + "\\" + image));
		button.setOpaque(false);
		button.setHorizontalAlignment(SwingConstants.RIGHT);
		button.setContentAreaFilled(false);
		button.setBorderPainted(false);
		button.setFocusPainted(false);
		button.setBounds(x, y, width, height);
		button.setToolTipText(name);
		panel.add(button);
		return button;

	}
}
