package edu.fdu.se.main.gitgui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;
import javax.swing.text.Highlighter;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import edu.fdu.se.config.ProjectProperties;
import edu.fdu.se.config.PropertyKeys;
import edu.fdu.se.fileutil.FileWriter;
import edu.fdu.se.git.JGitCommand;
import edu.fdu.se.git.JGitTagCommand;
import edu.fdu.se.git.RepoConstants;

public class CommitViewer {

	JGitCommand myCmd;
	private JFrame mainFrame;
	private JPanel controlPanel;
	private JButton jButton;
	private JTextField commitInput;

	public CommitViewer() {
		prepareGUI();
	}

	public static void main(String[] args) {
		CommitViewer swingContainerDemo = new CommitViewer();
		swingContainerDemo.upperPanel();
		swingContainerDemo.subPanel();
	}

	private void prepareGUI() {
		myCmd = new JGitTagCommand(ProjectProperties.getInstance().getValue(PropertyKeys.ANDROID_REPO_PATH2)
				+ RepoConstants.platform_frameworks_base_ + ".git");
		mainFrame = new JFrame("Commit Viewer");
		mainFrame.setSize(1024, 624);
		mainFrame.setLayout(new FlowLayout());
		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});
		controlPanel = new JPanel();
		controlPanel.setLayout(new BorderLayout());
		mainFrame.add(controlPanel);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
	}

	private void upperPanel() {
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		FlowLayout layout = new FlowLayout();
		layout.setHgap(10);
		layout.setVgap(10);
		panel.setLayout(layout);
		panel.add(new JLabel(" Repository:/platform/frameworks/base/.git"));
		commitInput = new JTextField(54);
//		commitInput.setText("c7f502947b5b80baca084101fb7a0aaa74db9974");
		panel.add(commitInput);
		jButton = new JButton("commit");
		JButton jButton2 = new JButton("tag");
		jButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String commitId = commitInput.getText();
				if (commitId.length() != 40) {
					return;
				}
				RepoDataHelper.getInstance().parserCommit(commitId);
				commitIdIndexOfJList = RepoDataHelper.getInstance().updateDataModel(listModel);
				commitLogDetail.setText(RepoDataHelper.getInstance().commitInfoSummary());
			}
		});
		jButton2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String commitId = commitInput.getText();
				if (commitId.length() != 40) {
					return;
				}
				byte[] buffer = RepoDataHelper.getInstance().myCmd.extract(listModel.getElementAt(fileList.getSelectedIndex()), commitId);
				FileWriter.writeInAll("D:/tagFile", buffer);
			}
		});
		panel.add(jButton);
		panel.add(jButton2);
		panel.setPreferredSize(new Dimension(1000, 100));

		controlPanel.add(panel, BorderLayout.NORTH);
		mainFrame.setVisible(true);
	}

	/**
	 * tab1
	 */
	private JTextArea commitLogDetail;
	/**
	 * tab2
	 */
	private JTextPane changedFileContent;
	private JTextPane linePane;
	JList<String> fileList;
	DefaultListModel<String> listModel;
	List<Integer> commitIdIndexOfJList;
	private JTextField tagCommitInput;

	private void subPanel() {
		JTabbedPane jtabpane = new JTabbedPane();
		JPanel tab1 = new JPanel();
		commitLogDetail = new JTextArea("Commit msg text");
		commitLogDetail.setPreferredSize(new Dimension(980, 400));
		tab1.add(commitLogDetail);
		//
		JPanel tab2 = new JPanel(new BorderLayout());
		listModel = new DefaultListModel<String>();

		listModel.addElement("load your commit");

		fileList = new JList<String>(listModel);
		fileList.setPreferredSize(new Dimension(352, 500));
		fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		fileList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (fileList.getValueIsAdjusting()) {
					System.out.println("click");
				} else {
					int index = fileList.getSelectedIndex();
					if (commitIdIndexOfJList == null || commitIdIndexOfJList.contains(index)) {
						System.out.println("contain. not do anything");
					} else {
						int commitRank = -1;
						if (commitIdIndexOfJList.contains(index)) {
							commitRank = commitIdIndexOfJList.indexOf(index);
						}
						String filePath = fileList.getSelectedValue();
						System.out.println(filePath);
						selectedFilePath = filePath;
						String content = RepoDataHelper.getInstance().readFile(filePath);
						fillTextPane(content);
						RepoDataHelper.getInstance().writePrevCurrFiles(filePath);

					}

				}
			}

		});

		JScrollPane fileListPanel = new JScrollPane(fileList);
		fileListPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		changedFileContent = new JTextPane();
		changedFileContent.setPreferredSize(new Dimension(600, 500));
		JScrollPane jsp1 = new JScrollPane(changedFileContent);
		
		linePane = new JTextPane();
		linePane.setPreferredSize(new Dimension(10, 500));
		JScrollPane jsp2 = new JScrollPane(linePane);

		jsp1.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				JScrollBar sBar = jsp1.getVerticalScrollBar();
				int value = sBar.getValue();
				JScrollBar sBar2 = jsp2.getVerticalScrollBar();
				sBar2.setValue(value);

			}

		});
		jsp2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		jsp2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		tab2.add(fileListPanel, BorderLayout.WEST);
		tab2.add(jsp2, BorderLayout.CENTER);
		tab2.add(jsp1, BorderLayout.EAST);
		
		
//		JPanel tab3 = new JPanel(new BorderLayout());
//		tagCommitInput = new JTextField();
//		tagCommitInput.setPreferredSize(new Dimension(100, 20));
//		tagCommitInput.addKeyListener(new KeyListener(){
//			@Override
//			public void keyPressed(KeyEvent arg0) {
//				
//			}
//			@Override
//			public void keyReleased(KeyEvent arg0) {
//				if(arg0.getKeyCode()== 10){
//					String fileTag = JGitRepoManager.getInstance().getFileContent(tagCommitInput.getText(),selectedFilePath);
//					int index = fileList.getSelectedIndex();
//					String prevCommitId = null;
//					if(commitIdIndexOfJList.size()==1){
//						prevCommitId = fileList.getModel().getElementAt(0);
//					}else if(commitIdIndexOfJList.size()==2){
//						if(index<commitIdIndexOfJList.get(1)){
//							prevCommitId = fileList.getModel().getElementAt(0);
//						}else{
//							prevCommitId = fileList.getModel().getElementAt(commitIdIndexOfJList.get(1));
//						}
//					}
//					String fileCommit = JGitRepoManager.getInstance().getFileContent(prevCommitId, selectedFilePath);
//					FileWriter.writeInAll("D:/tag", fileTag);
//					FileWriter.writeInAll("D:/prev", fileCommit);
//				}
//				
//			}
//			@Override
//			public void keyTyped(KeyEvent arg0) {
//				
//			}
//		});
//		tab3.add(tagCommitInput,BorderLayout.NORTH);
		
		jtabpane.addTab("Commit", tab1);
		jtabpane.addTab("Diff", tab2);
//		jtabpane.addTab("Tag Commit", tab3);

		jtabpane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		jtabpane.setPreferredSize(new Dimension(1000, 400));
		controlPanel.add(jtabpane, BorderLayout.SOUTH);
		mainFrame.setVisible(true);
	}
	private String selectedFilePath;

	public void fillTextPane(InputStream fis, Map<Integer, Integer> redGreenFlag) {
		InputStreamReader ir = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(ir);
		StyledDocument doc = changedFileContent.getStyledDocument();
		Style style = changedFileContent.addStyle("mStyle", null);
		String line = null;
		int index = 0;
		try {
			while ((line = br.readLine()) != null) {
				if (redGreenFlag.containsKey(index)) {
					int value = redGreenFlag.get(index);
					if (value == 1) {
						// red
						StyleConstants.setForeground(style, Color.red);
						try {
							doc.insertString(doc.getLength(), line, style);
						} catch (BadLocationException e) {
						}
					} else {
						// green
						StyleConstants.setForeground(style, Color.green);
						try {
							doc.insertString(doc.getLength(), line, style);
						} catch (BadLocationException e) {
						}
					}
				} else {
					// normal
					StyleConstants.setForeground(style, Color.white);
					try {
						doc.insertString(doc.getLength(), "BLEH", style);
					} catch (BadLocationException e) {
					}
				}

			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		StyledDocument lineDoc = linePane.getStyledDocument();
		Style style2 = lineDoc.addStyle("mStyle", null);
		for (int i = 0; i < index; i++) {
			try {
				lineDoc.insertString(doc.getLength(), String.valueOf(i + 1) + "\n", style2);
			} catch (BadLocationException e) {
			}
		}

	}

	public void fillTextPane(String fileContent) {
		InputStream is = new ByteArrayInputStream(fileContent.getBytes());
		InputStreamReader isb = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isb);
		StyledDocument doc = changedFileContent.getStyledDocument();
		changedFileContent.setText("");
		Style style = changedFileContent.addStyle("mStyle", null);
		String line = null;
		int lineNum = 0;
		try {
			while ((line = br.readLine()) != null) {
				lineNum++;

				if (line.startsWith("+-")) {
					StyleConstants.setForeground(style, Color.blue);
				} else if (line.startsWith("+")) {
					StyleConstants.setForeground(style, Color.green);
				} else if (line.startsWith("-")) {
					StyleConstants.setForeground(style, Color.red);
				} else {
					StyleConstants.setForeground(style, Color.black);
				}
				doc.insertString(doc.getLength(), line + "\n", style);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		StyledDocument lineDoc = linePane.getStyledDocument();

		Style style2 = lineDoc.addStyle("mStyle", null);
		for (int i = 0; i < lineNum; i++) {
			try {
				lineDoc.insertString(lineDoc.getLength(), String.valueOf(i + 1) + "\n", style2);
			} catch (BadLocationException e) {
			}
		}

	}

}