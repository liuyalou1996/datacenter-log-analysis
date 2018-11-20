package com.iboxpay.ui;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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
import com.iboxpay.util.DialogUtils;
import com.iboxpay.util.UiUtils;

public class ModifyConfigWindow {

  protected Shell shell;
  private Text tx_username;
  private Text tx_password;
  private Text tx_err_dir;
  private Text tx_inter_dir;
  private Text tx_inter_filename;
  private Text tx_err_filename;
  private Spinner spinner_port;
  private Combo combo_host;
  private Button btn_modify;
  private Button btn_delete;
  private Combo main_combo_host;

  private ConfigFileOperationService fileOp = new ConfigFileOperationServiceImpl();

  /**
   * 配置
   */
  private Map<String, ConfigInfo> configMap;
  private Button btn_cancel;

  public ModifyConfigWindow() {

  }

  public ModifyConfigWindow(Combo main_combo_host) {
    this.main_combo_host = main_combo_host;
    this.configMap = fileOp.readConfigFileToMap();
  }

  public static void main(String[] args) {
    try {
      ModifyConfigWindow window = new ModifyConfigWindow(null);
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
    shell = new Shell(SWT.DIALOG_TRIM);
    shell.setSize(522, 395);
    shell.setText("修改/删除配置");
    shell.setLayout(new FillLayout(SWT.HORIZONTAL));
    UiUtils.centerShell(shell.getDisplay(), shell);

    Group group = new Group(shell, SWT.NONE);
    group.setText("配置信息");
    group.setLayout(new FillLayout(SWT.HORIZONTAL));

    Composite composite = new Composite(group, SWT.NONE);
    composite.setLayout(null);

    combo_host = new Combo(composite, SWT.READ_ONLY);
    combo_host.setBounds(157, 17, 112, 25);
    for (String host : configMap.keySet()) {
      combo_host.add(host);
    }

    Label lb_host = new Label(composite, SWT.NONE);
    lb_host.setBounds(24, 20, 86, 17);
    lb_host.setText("请选择主机(H)：");

    Label lb_port = new Label(composite, SWT.NONE);
    lb_port.setText("端口号(P)：");
    lb_port.setBounds(24, 55, 61, 17);

    spinner_port = new Spinner(composite, SWT.BORDER);
    spinner_port.setBounds(157, 52, 61, 23);
    spinner_port.setSelection(22);

    Label lb_username = new Label(composite, SWT.NONE);
    lb_username.setBounds(24, 90, 61, 17);
    lb_username.setText("用户名(U)：");

    Label lb_password = new Label(composite, SWT.NONE);
    lb_password.setBounds(24, 125, 61, 17);
    lb_password.setText("密码(P)：");

    Label lb_err_dir = new Label(composite, SWT.NONE);
    lb_err_dir.setText("错误日志目录：");
    lb_err_dir.setBounds(24, 160, 83, 17);

    Label lb_inter_dir = new Label(composite, SWT.NONE);
    lb_inter_dir.setText("请求响应日志目录：");
    lb_inter_dir.setBounds(24, 195, 104, 17);

    Label lb_err_filename = new Label(composite, SWT.NONE);
    lb_err_filename.setText("错误日志文件名：");
    lb_err_filename.setBounds(24, 230, 104, 17);

    Label lb_inter_filename = new Label(composite, SWT.NONE);
    lb_inter_filename.setText("请求响应日志文件名：");
    lb_inter_filename.setBounds(24, 265, 116, 17);

    tx_username = new Text(composite, SWT.BORDER);
    tx_username.setBounds(157, 86, 155, 23);

    tx_password = new Text(composite, SWT.BORDER | SWT.PASSWORD);
    tx_password.setBounds(157, 122, 155, 23);

    tx_err_dir = new Text(composite, SWT.BORDER);
    tx_err_dir.setBounds(157, 157, 307, 23);

    tx_inter_dir = new Text(composite, SWT.BORDER);
    tx_inter_dir.setBounds(157, 192, 307, 23);

    tx_inter_filename = new Text(composite, SWT.BORDER);
    tx_inter_filename.setBounds(157, 257, 155, 23);

    tx_err_filename = new Text(composite, SWT.BORDER);
    tx_err_filename.setBounds(157, 224, 155, 23);

    btn_modify = new Button(composite, SWT.NONE);
    btn_modify.setBounds(232, 310, 80, 27);
    btn_modify.setText("修改");

    btn_delete = new Button(composite, SWT.NONE);
    btn_delete.setBounds(325, 310, 80, 27);
    btn_delete.setText("删除");

    btn_cancel = new Button(composite, SWT.NONE);
    btn_cancel.setBounds(420, 310, 80, 27);
    btn_cancel.setText("取消");
    // 为组件添加监听器
    addListenerForWidget();
  }

  private void addListenerForWidget() {
    // 根据选择的host加载配置
    combo_host.addModifyListener(new ModifyListener() {

      public void modifyText(ModifyEvent e) {
        String host = combo_host.getText();
        ConfigInfo configInfo = configMap.get(combo_host.getText());
        if (configInfo == null) {
          DialogUtils.showInformationDialog(shell, "提示", "找不到" + host + "对应的配置!");
          return;
        }
        tx_username.setText(configInfo.getUsername());
        tx_password.setText(configInfo.getPassword());
        tx_err_dir.setText(configInfo.getLogErrDir());
        tx_err_filename.setText(configInfo.getLogErrFileName());
        tx_inter_dir.setText(configInfo.getLogInterDir());
        tx_inter_filename.setText(configInfo.getLogInterFileName());
      }
    });

    // 修改配置
    btn_modify.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(SelectionEvent e) {
        String host = combo_host.getText();
        if (StringUtils.isBlank(host)) {
          DialogUtils.showInformationDialog(shell, "提示", "请选择主机!");
          return;
        }
        // 取出配置
        ConfigInfo configInfo = configMap.get(host);
        configInfo.setUsername(tx_username.getText());
        configInfo.setPassword(tx_password.getText());
        configInfo.setPort(spinner_port.getSelection());
        configInfo.setLogErrDir(tx_err_dir.getText());
        configInfo.setLogErrFileName(tx_err_filename.getText());
        configInfo.setLogInterDir(tx_inter_dir.getText());
        configInfo.setLogInterFileName(tx_inter_filename.getText());
        // 重新写入
        fileOp.writeToConfigFile(configMap);
        DialogUtils.showInformationDialog(shell, "提示", "配置修改成功!");
        shell.dispose();
      }
    });

    // 删除配置
    btn_delete.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(SelectionEvent e) {
        String host = combo_host.getText();
        if (StringUtils.isBlank(host)) {
          DialogUtils.showInformationDialog(shell, "提示", "请选择主机!");
          return;
        }

        // 删除host对应配置
        configMap.remove(host);
        // 先清除
        main_combo_host.removeAll();
        // 更新host下拉框
        for (String key : configMap.keySet()) {
          main_combo_host.add(key);
        }
        // 写入配置文件
        fileOp.writeToConfigFile(configMap);
        DialogUtils.showInformationDialog(shell, "提示", "配置删除成功!");
        shell.dispose();
      }
    });
    // 取消
    btn_cancel.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(SelectionEvent e) {
        shell.dispose();
      }
    });

  }

}
