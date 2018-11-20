package com.iboxpay.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.printing.PrintDialog;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.printing.PrinterData;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class DialogUtils {

  /**
   * 显示对话框
   * 
   * @param shell
   * @param title
   * @param content
   */
  public static void showErrorDialog(Shell shell, String title, String message) {
    MessageBox mb = new MessageBox(shell, SWT.OK | SWT.ICON_ERROR);
    setDialog(title, message, mb);
  }

  public static void showInformationDialog(Shell shell, String title, String message) {
    MessageBox mb = new MessageBox(shell, SWT.OK | SWT.ICON_INFORMATION);
    setDialog(title, message, mb);
  }

  public static void showWarningDialog(Shell shell, String title, String message) {
    MessageBox mb = new MessageBox(shell, SWT.OK | SWT.ICON_WARNING);
    setDialog(title, message, mb);
  }

  public static int showQuestionDialog(Shell shell, String title, String message) {
    MessageBox mb = new MessageBox(shell, SWT.YES | SWT.NO | SWT.CANCEL | SWT.ICON_QUESTION);
    int result = setDialog(title, message, mb);
    return result;
  }

  private static int setDialog(String title, String message, MessageBox mb) {
    mb.setText(title);
    mb.setMessage(message);
    int result = mb.open();
    return result;
  }

  /**
   * 设置字体对话框
   * 
   * @param shell
   *            窗体
   * @param text
   *            文本
   */
  public static void showFontDialog(Shell shell, Text text) {
    FontDialog fd = new FontDialog(shell, SWT.NONE);
    FontData fontData = fd.open();
    if (fontData != null) {
      Font font = new Font(shell.getDisplay(), fontData);
      text.setFont(font);
      Color color = new Color(shell.getDisplay(), fd.getRGB().red, fd.getRGB().green, fd.getRGB().blue);
      text.setForeground(color);
    }
  }

  /**
   * 设置颜色对话框
   * 
   * @param shell
   *            窗体
   * @param text
   *            文本框
   */
  public static void showColorDialog(Shell shell, Text text) {
    ColorDialog cd = new ColorDialog(shell, SWT.NONE);
    RGB rgb = cd.open();
    if (rgb != null) {
      Color color = new Color(shell.getDisplay(), rgb);
      text.setForeground(color);
    }
  }

  /**
   * 打印对话框
   * 
   * @param shell
   *            窗体
   */
  public static void showPrintDialog(Shell shell) {
    PrintDialog dialog = new PrintDialog(shell, SWT.NONE);
    PrinterData pd = dialog.open();
    if (pd != null) {
      Printer printer = new Printer(pd);
      printer.dispose();
    }
  }

  /**
   * 文件保存对话框
   * 
   * @param shell
   *            窗体
   * @return
   */
  public static String showSaveFileDialog(Shell shell, String[] extensions, String[] names) {
    FileDialog fd = new FileDialog(shell, SWT.SAVE);
    String path = System.getProperty("user.home");
    // 设置打开时文件的默认路径
    fd.setFilterPath(path);
    // 设置所打开文件的扩展名
    fd.setFilterExtensions(extensions);
    // 设置显示到下拉框中的扩展名的名称
    fd.setFilterNames(names);
    // 文件的抽象目录
    String file = fd.open();
    return file;
  }

  /**
   * 文件打开对话框
   * @param shell
   * @return
   */
  public static String showOpenFileDialog(Shell shell, String[] extensions, String[] names) {
    FileDialog fd = new FileDialog(shell, SWT.OPEN);
    String path = System.getProperty("user.home");
    // 设置打开时文件的默认路径
    fd.setFilterPath(path);
    // 设置所打开文件的扩展名
    fd.setFilterExtensions(extensions);
    // 设置显示到下拉框中的扩展名的名称
    fd.setFilterNames(names);
    return fd.open();
  }

  /**
   * 保存文件，目录选择对话框
   * 
   * @param shell
   *            窗体
   * @return
   */
  public static String showDirectoryDialog(Shell shell) {
    DirectoryDialog dd = new DirectoryDialog(shell, SWT.NONE);
    // 设置显示在窗口上方的提示信息
    dd.setMessage("请选择要保存的文件夹");
    // 设置对话框的标题
    dd.setText("请选择文件目录");
    // 设置打开时默认的文件目录
    dd.setFilterPath(System.getProperty("user.home"));
    // 打开窗口，返回用户选择的文件目录
    String fileDir = dd.open();
    return fileDir;
  }
}
