import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;

public class SuccessDialogue extends Dialog {

	protected Object result;
	protected Shell shell;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public SuccessDialogue(Shell parent, int style) {
		super(parent, style);
		setText("Success");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open(String fileName, String outputPath, String filePath) {
		createContents(fileName, outputPath, filePath);
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents(String fileName, String outputPath, String filePath) {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(650, 200);
		shell.setText(getText());
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setBounds(0, 0, 632, 153);
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setBounds(10, 10, 612, 83);
		lblNewLabel.setText("Workbook \"" + fileName + "\" was successfully created in the following directory:\n\n" + outputPath);
		Button OKButton = new Button(composite, SWT.NONE);
		OKButton.setBounds(532, 111, 90, 30);
		OKButton.setText("OK");
		Button directoryButton = new Button(composite, SWT.NONE);
		directoryButton.setText("Open Containing Folder");
		directoryButton.setBounds(196, 111, 180, 30);
		Button workbookButton = new Button(composite, SWT.NONE);
		workbookButton.setText("Open Excel Workbook");
		workbookButton.setBounds(10, 111, 180, 30);
		
		OKButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent ev) {
            	shell.close();
            }
        });
		
		directoryButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent ev) {
            	File file = new File(outputPath);
            	Desktop desktop = Desktop.getDesktop();
            	try {
                    desktop.open(file);
                    shell.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
		
		workbookButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent ev) {
            	File file = new File(filePath);
            	Desktop desktop = Desktop.getDesktop();
            	try {
                    desktop.open(file);
                    shell.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

	}
}
