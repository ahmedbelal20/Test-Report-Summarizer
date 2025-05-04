import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;

public class MainUI {

	protected Shell mainShell;
	private Text inputText;
	private Text outputText;
	private String inputDirectory;
	private String outputDirectory;
	private String fileName;
	private String outputPath;
	private Text excelText;
	private ArrayList<TestReport> testReports = new ArrayList<>();
	private Text XMLText;
	private Text reportsText;
	private Text testCasesText;
	private int testCasesNumber = 0;
	private int testReportsNumber = 0;

//	public static void main(String[] args) {
//		try {
//			new MainUI().open();
//		} catch (Exception e) {
//			e.printStackTrace();
//		};
//	}

	public void open() {
		Display display = Display.getDefault();
		createContents();
		mainShell.open();
		mainShell.layout();
		while (!mainShell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	protected void createContents() {
		mainShell = new Shell();
		mainShell.setBackground(SWTResourceManager.getColor(192, 192, 192));
		mainShell.setMinimumSize(new Point(650, 500));
		mainShell.setSize(650, 547);
		mainShell.setText("TRS");
		mainShell.setLayout(new FillLayout(SWT.HORIZONTAL));
		Composite parentComposite = new Composite(mainShell, SWT.NONE);
		parentComposite.setLayout(null);
		Composite headerComposite = new Composite(parentComposite, SWT.NONE);
		headerComposite.setBounds(0, 0, 632, 74);
		Label headerLabel = new Label(headerComposite, SWT.NONE);
		headerLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		headerLabel.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		headerLabel.setBounds(10, 10, 179, 29);
		headerLabel.setText("Test Report Summarizer");
		Label mainLabel = new Label(headerComposite, SWT.NONE);
		mainLabel.setBounds(20, 45, 602, 20);
		mainLabel.setText("Where should Test Report Summarizer look for the reports and output the Excel workbook? ");
		Label label1 = new Label(parentComposite, SWT.NONE);
		label1.setFont(SWTResourceManager.getFont("Segoe UI", 8, SWT.NORMAL));
		label1.setText("Select the folder where you would want Test Report Summarizer to look for the reports.");
		label1.setBounds(20, 107, 612, 20);
		Group inputGroup = new Group(parentComposite, SWT.NONE);
		inputGroup.setForeground(SWTResourceManager.getColor(0, 0, 255));
		inputGroup.setText("Reports Folder");
		inputGroup.setBounds(20, 133, 582, 88);
		inputText = new Text(inputGroup, SWT.BORDER);
		inputText.setEnabled(false);
		inputText.setEditable(false);
		inputText.setLocation(10, 37);
		inputText.setSize(430, 26);
		Button inputButton = new Button(inputGroup, SWT.NONE);
		inputButton.setLocation(461, 35);
		inputButton.setSize(109, 30);
		inputButton.setText("Browse...");
		Label separator2 = new Label(parentComposite, SWT.SEPARATOR | SWT.HORIZONTAL);
		separator2.setText("Test Report Summarizer");
		separator2.setBounds(0, 259, 632, 17);
		Label label2 = new Label(parentComposite, SWT.NONE);
		label2.setText("Select the folder where you would want Test Report Summarizer to output the Excel workbook.");
		label2.setFont(SWTResourceManager.getFont("Segoe UI", 8, SWT.NORMAL));
		label2.setBounds(20, 282, 612, 20);
		Group ouputGroup = new Group(parentComposite, SWT.NONE);
		ouputGroup.setText("Output Folder");
		ouputGroup.setForeground(SWTResourceManager.getColor(0, 0, 255));
		ouputGroup.setBounds(20, 308, 582, 88);
		outputText = new Text(ouputGroup, SWT.BORDER);
		outputText.setEnabled(false);
		outputText.setEditable(false);
		outputText.setBounds(10, 37, 430, 26);
		Button outputButton = new Button(ouputGroup, SWT.NONE);
		outputButton.setText("Browse...");
		outputButton.setBounds(461, 35, 109, 30);
		Button generateButton = new Button(parentComposite, SWT.NONE);
		generateButton.setText("Generate Workbook");
		generateButton.setBounds(440, 457, 162, 30);
		Button abortButton = new Button(parentComposite, SWT.NONE);
		abortButton.setText("Abort");
		abortButton.setBounds(20, 457, 109, 30);
		Label separator3 = new Label(parentComposite, SWT.SEPARATOR | SWT.HORIZONTAL);
		separator3.setText("Test Report Summarizer");
		separator3.setBounds(0, 434, 632, 17);
		Label excelLabel = new Label(parentComposite, SWT.NONE);
		excelLabel.setText("Ouput Excel workbook name:");
		excelLabel.setBounds(20, 402, 201, 20);
		excelText = new Text(parentComposite, SWT.BORDER);
		excelText.setBounds(227, 402, 375, 26);
		Label separator1 = new Label(parentComposite, SWT.SEPARATOR | SWT.HORIZONTAL);
		separator1.setBounds(0, 80, 642, 17);
		Button clearButton = new Button(parentComposite, SWT.NONE);
		clearButton.setText("Clear");
		clearButton.setBounds(325, 457, 109, 30);
		Label lblContains = new Label(parentComposite, SWT.NONE);
		lblContains.setText("Contains");
		lblContains.setBounds(20, 233, 73, 20);
		XMLText = new Text(parentComposite, SWT.BORDER | SWT.CENTER);
		XMLText.setEnabled(false);
		XMLText.setEditable(false);
		XMLText.setBounds(83, 230, 40, 26);
		Label lblxmlFiles = new Label(parentComposite, SWT.NONE);
		lblxmlFiles.setText(".xml files, ");
		lblxmlFiles.setBounds(129, 233, 65, 20);
		reportsText = new Text(parentComposite, SWT.BORDER | SWT.CENTER);
		reportsText.setEnabled(false);
		reportsText.setEditable(false);
		reportsText.setBounds(194, 230, 40, 26);
		Label lblOfWhichAre = new Label(parentComposite, SWT.NONE);
		lblOfWhichAre.setText("of which are reports, with a total of");
		lblOfWhichAre.setBounds(240, 233, 237, 20);
		testCasesText = new Text(parentComposite, SWT.BORDER | SWT.CENTER);
		testCasesText.setEnabled(false);
		testCasesText.setEditable(false);
		testCasesText.setBounds(475, 230, 50, 26);
		Label lblTestCases = new Label(parentComposite, SWT.NONE);
		lblTestCases.setText("test cases.");
		lblTestCases.setBounds(531, 233, 71, 20);

		inputButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog directoryDialog = new DirectoryDialog(mainShell);
				String hold = directoryDialog.open();
				if (hold == null)
					return;
				else {
					inputDirectory = hold;
					inputText.setText(inputDirectory);
					// Parse XML
					testReports = XMLParser.parseXML(inputDirectory);
					testCasesNumber = 0;
					testReportsNumber = 0;
					for (int i = 0; i < testReports.size(); i++) {
						if (testReports.get(i).getTestCasesNumber() != 0) {
							testCasesNumber += testReports.get(i).getTestCasesNumber();
							testReportsNumber++;
						}
					}
					XMLText.setText(String.valueOf(testReports.size()));
					reportsText.setText(String.valueOf(testReportsNumber));
					testCasesText.setText(String.valueOf(testCasesNumber));
				}
			}
		});

		outputButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog directoryDialog = new DirectoryDialog(mainShell);
				String hold = directoryDialog.open();
				if (hold == null)
					return;
				else {
					outputDirectory = hold;
					outputText.setText(outputDirectory);
				}
			}
		});

		abortButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				mainShell.close();
			}
		});

		clearButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				inputDirectory = null;
				outputDirectory = null;
				fileName = null;
				outputPath = null;
				inputText.setText("");
				outputText.setText("");
				excelText.setText("");
				testReports = new ArrayList<>();
				XMLText.setText("");
				reportsText.setText("");
				testCasesText.setText("");
				testCasesNumber = 0;
				testReportsNumber = 0;
			}
		});

		generateButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (inputDirectory == null) {
					// Create window with error
					MessageDialog.openError(mainShell, "Error", "Error while generating the summary:\n\n"
							+ "No input directory was selected. Please select an input directory.");
				}
				else if (outputDirectory == null) {
					MessageDialog.openError(mainShell, "Error", "Error while generating the summary:\n\n"
							+ "No output directory was selected. Please select an output directory.");
				}
				else if (excelText.getText() == "") {
					MessageDialog.openError(mainShell, "Error", "Error while generating the summary:\n\n"
							+ "No output file name was selected. Please select an output file name.");
				}
				else if (testCasesNumber == 0) {
					MessageDialog.openError(mainShell, "Error",
							"Error while generating the summary:\n\n"
									+ "The selected directory either has no test reports, or has test reports"
									+ " with no test cases.");
				}
				else {
					fileName = excelText.getText();
					if (!(fileName.endsWith(".xlsx")) && !(fileName.endsWith(".xls")))
						fileName = fileName + ".xlsx";
					outputPath = outputDirectory + "//" + fileName;
					// Create an Excel workbook
					File file = new File(outputPath);
					if (file.exists() && !file.isDirectory()) {
						// Window here to ask for overwrite
						boolean overwrite = false;
						overwrite = MessageDialog.openConfirm(mainShell, "Overwrite", "A worbook with the name \"" + fileName
								+ "\" already exists in the following path:\n\n" + outputDirectory + 
								"\n\nClick OK to overwrite the file or click cancel to stop the operation.");
						if (!overwrite)
							return;
					}
					try {
						ExcelGenerator.generateExcel(testReports, outputPath);
						SuccessDialogue successDialog = new SuccessDialogue(mainShell, SWT.DIALOG_TRIM);
						successDialog.open(fileName, outputDirectory, outputPath);
					} catch (IOException e1) {
						MessageDialog.openError(mainShell, "Error", "Error while generating the summary:\n\n"
								+ "Workbook could not be created. Another file with the same name might exist"
								+ " in the same directory and might be running.");
						return;
					}
				}
			}
		});
	}
}
