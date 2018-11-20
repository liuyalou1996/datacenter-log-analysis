package com.iboxpay.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.iboxpay.swt.SWTResourceManager;
import com.iboxpay.util.UiUtils;

public class ExtraWindow {

  protected Shell shell;

  private String title;
  private String errDetail;
  private Text text;

  public ExtraWindow() {

  }

  public ExtraWindow(String title, String errDetail) {
    this.title = title;
    this.errDetail = errDetail;
  }

  public static void main(String[] args) {
    try {
      ExtraWindow window = new ExtraWindow();
      window.open();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void open() {
    Display display = Display.getDefault();
    createContents();
    shell.open();
    shell.layout();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch()) {
        display.sleep();
      }
    }
  }

  protected void createContents() {
    shell = new Shell();
    shell.setSize(975, 456);
    shell.setText(title);
    shell.setLayout(new FillLayout(SWT.HORIZONTAL));
    // 窗口居中
    UiUtils.centerShell(shell.getDisplay(), shell);

    Composite composite = new Composite(shell, SWT.BORDER);
    composite.setLayout(new FillLayout(SWT.HORIZONTAL));
    text = new Text(composite, SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
    text.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
    text.setText(errDetail);
  }
}
