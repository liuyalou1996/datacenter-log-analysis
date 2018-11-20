package com.iboxpay.ui;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

import com.iboxpay.entity.ConfigInfo;
import com.iboxpay.service.ConfigFileOperationService;
import com.iboxpay.service.impl.ConfigFileOperationServiceImpl;
import com.iboxpay.util.CollectionUtils;
import com.iboxpay.util.DialogUtils;
import com.iboxpay.util.UiUtils;

public class NewConfigWindow {

  protected Shell shell;
  private Text tx_host;
  private Text tx_username;
  private Text tx_password;
  private Label lb_err_dir;
  private Label lb_port;
  private Label lb_interface_dir;
  private Text tx_err_dir;
  private Text tx_interface_dir;
  private Label lb_err_filename;
  private Label lb_interface_filename;
  private Text tx_err_filename;
  private Text tx_interface_filename;
  private Button btn_confirm;
  private Button btn_cancel;
  private Group group;

  private Spinner spinner;

  private Combo combo_host;

  public NewConfigWindow() {
  }

  public NewConfigWindow(Combo hostCombo) {
    this.combo_host = hostCombo;
  }

  /**
   * Launch the application.
   * @param args
   */
  public static void main(String[] args) {
    try {
      NewConfigWindow window = new NewConfigWindow();
      window.open();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Open the window.
   */
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

  /**
   * Create contents of the window.
   */
  protected void createContents() {
    shell = new Shell(SWT.DIALOG_TRIM);
    shell.setSize(506, 388);
    shell.setText("添加配置");
    shell.setLayout(new FillLayout(SWT.HORIZONTAL));
    UiUtils.centerShell(shell.getDisplay(), shell);

    group = new Group(shell, SWT.NONE);
    group.setText("配置信息");
    group.setLayout(new FillLayout(SWT.HORIZONTAL));

    Composite composite = new Composite(group, SWT.NONE);
    composite.setLayout(null);

    Label lb_host = new Label(composite, SWT.NONE);
    lb_host.setBounds(25, 25, 83, 17);
    lb_host.setText("主机地址(H)：");

    Label lb_username = new Label(composite, SWT.NONE);
    lb_username.setBounds(25, 93, 61, 17);
    lb_username.setText("用户名(U)：");

    Label lb_password = new Label(composite, SWT.NONE);
    lb_password.setBounds(25, 128, 61, 17);
    lb_password.setText("密码(P)：");

    tx_host = new Text(composite, SWT.BORDER);
    tx_host.setBounds(167, 22, 155, 23);

    tx_username = new Text(composite, SWT.BORDER);
    tx_username.setBounds(167, 90, 155, 23);

    tx_password = new Text(composite, SWT.BORDER | SWT.PASSWORD);
    tx_password.setBounds(167, 125, 155, 23);

    lb_err_dir = new Label(composite, SWT.NONE);
    lb_err_dir.setBounds(25, 163, 83, 17);
    lb_err_dir.setText("错误日志目录：");

    lb_port = new Label(composite, SWT.NONE);
    lb_port.setBounds(25, 59, 61, 17);
    lb_port.setText("端口号(P)：");

    lb_interface_dir = new Label(composite, SWT.NONE);
    lb_interface_dir.setBounds(25, 196, 104, 17);
    lb_interface_dir.setText("请求响应日志目录：");

    tx_err_dir = new Text(composite, SWT.BORDER);
    tx_err_dir.setBounds(167, 160, 307, 23);

    tx_interface_dir = new Text(composite, SWT.BORDER);
    tx_interface_dir.setBounds(167, 193, 307, 23);

    lb_err_filename = new Label(composite, SWT.NONE);
    lb_err_filename.setBounds(25, 230, 104, 17);
    lb_err_filename.setText("错误日志文件名：");

    lb_interface_filename = new Label(composite, SWT.NONE);
    lb_interface_filename.setBounds(25, 263, 116, 17);
    lb_interface_filename.setText("请求响应日志文件名：");

    tx_err_filename = new Text(composite, SWT.BORDER);
    tx_err_filename.setBounds(167, 227, 155, 23);

    tx_interface_filename = new Text(composite, SWT.BORDER);
    tx_interface_filename.setBounds(167, 263, 155, 23);

    btn_confirm = new Button(composite, SWT.NONE);
    btn_confirm.setBounds(302, 305, 81, 27);
    btn_confirm.setText("确认(Y)");
    shell.setDefaultButton(btn_confirm);

    btn_cancel = new Button(composite, SWT.NONE);
    btn_cancel.setBounds(403, 305, 81, 27);
    btn_cancel.setText("取消(N)");

    spinner = new Spinner(composite, SWT.BORDER);
    spinner.setSelection(22);
    spinner.setBounds(167, 55, 61, 23);

    // 确认后保存信息至配置文件
    btn_confirm.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(SelectionEvent e) {
        saveConfig();
      }
    });

    // 取消则关闭shell
    btn_cancel.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(SelectionEvent e) {
        shell.dispose();
      }
    });
  }

  private void saveConfig() {
    String host = tx_host.getText();
    String port = spinner.getText();
    String username = tx_username.getText();
    String password = tx_password.getText();
    String logErrDir = tx_err_dir.getText();
    String logErrFileName = tx_err_filename.getText();
    String logInterDir = tx_interface_dir.getText();
    String logInterFileName = tx_interface_filename.getText();

    if (StringUtils.isBlank(host) || StringUtils.isBlank(port) || StringUtils.isBlank(username)
        || StringUtils.isBlank(password) || StringUtils.isBlank(logErrDir) || StringUtils.isBlank(logErrFileName)
        || StringUtils.isBlank(logInterDir) || StringUtils.isBlank(logInterFileName)) {
      DialogUtils.showWarningDialog(shell, "警告", "以上内容为必填项!");
      return;
    }

    ConfigInfo configInfo = new ConfigInfo();
    configInfo.setPort(Integer.parseInt(port));
    configInfo.setUsername(username);
    configInfo.setPassword(password);
    configInfo.setLogErrDir(logErrDir);
    configInfo.setLogErrFileName(logErrFileName);
    configInfo.setLogInterDir(logInterDir);
    configInfo.setLogInterFileName(logInterFileName);

    // 先读取配置文件,查看是否有配置
    ConfigFileOperationService fileOp = new ConfigFileOperationServiceImpl();
    Map<String, ConfigInfo> configMap = fileOp.readConfigFileToMap();
    if (CollectionUtils.isEmpty(configMap)) {
      configMap = new LinkedHashMap<>();
    }

    // 如果已包含弹出对话框
    if (configMap.containsKey(host)) {
      int result = DialogUtils.showQuestionDialog(shell, "提示", "host为" + host + "的配置已存在，确认覆盖吗?");
      if (result != SWT.YES) {
        return;
      }
    }

    configMap.put(host, configInfo);
    // 清空所有
    combo_host.removeAll();
    // 刷新host下拉框
    for (Map.Entry<String, ConfigInfo> entry : configMap.entrySet()) {
      combo_host.add(entry.getKey());
    }
    // 写入配置
    fileOp.writeToConfigFile(configMap);
    // 对话框提示
    DialogUtils.showInformationDialog(shell, "提示", "导入成功!");
    // 关闭shell
    shell.dispose();

  }
}
