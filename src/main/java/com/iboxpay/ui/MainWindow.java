package com.iboxpay.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.iboxpay.entity.ConfigInfo;
import com.iboxpay.entity.ErrorInfo;
import com.iboxpay.service.ConfigFileOperationService;
import com.iboxpay.service.DataCenterLogAnalysisService;
import com.iboxpay.service.impl.ConfigFileOperationServiceImpl;
import com.iboxpay.service.impl.DataCenterLogAnalysisServiceImpl;
import com.iboxpay.util.CollectionUtils;
import com.iboxpay.util.DateUtils;
import com.iboxpay.util.DialogUtils;
import com.iboxpay.util.UiUtils;

public class MainWindow {

  protected Shell shell;
  private Table table;

  private Label lb_host;
  private Label lb_date;
  private Combo combo_host;
  private Combo combo_date;
  private Button btn_analyze;
  private Button btn_export;
  private TableCursor tableCursor;

  private MenuItem mi_add;
  private MenuItem mi_open;
  private MenuItem mi_exit;
  private MenuItem mi_modify;

  private ConfigFileOperationService fileOp = new ConfigFileOperationServiceImpl();

  public static void main(String[] args) {
    try {
      MainWindow window = new MainWindow();
      window.open();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void open() {
    Display display = Display.getDefault();
    createContents();
    // UiUtil.showFullScreen(shell);
    shell.open();
    shell.layout();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch()) {
        display.sleep();
      }
    }
  }

  @SuppressWarnings("unused")
  protected void createContents() {
    shell = new Shell(SWT.SHELL_TRIM | SWT.BORDER);
    shell.setSize(1521, 688);
    shell.setText("简单日志分析");
    shell.setLayout(new FillLayout(SWT.HORIZONTAL));
    UiUtils.centerShell(shell.getDisplay(), shell);

    Composite mainComp = new Composite(shell, SWT.NONE);
    mainComp.setLayout(new FillLayout(SWT.VERTICAL));
    SashForm mainSashForm = new SashForm(mainComp, SWT.VERTICAL);

    SashForm sashFormTop = new SashForm(mainSashForm, SWT.NONE);
    SashForm sashFormBottom = new SashForm(mainSashForm, SWT.NONE);

    Composite compTop = new Composite(sashFormTop, SWT.BORDER);
    Composite comp_bottom = new Composite(sashFormBottom, SWT.NONE);
    comp_bottom.setLayout(new FillLayout(SWT.HORIZONTAL));

    lb_host = new Label(compTop, SWT.NONE);
    lb_host.setBounds(10, 13, 89, 17);
    lb_host.setText("请选择主机(H)：");
    combo_host = new Combo(compTop, SWT.NONE);
    combo_host.setBounds(105, 10, 99, 25);
    combo_host.forceFocus();
    setValueForHostCombo();

    lb_date = new Label(compTop, SWT.NONE);
    lb_date.setBounds(268, 13, 89, 19);
    lb_date.setText("请选择日期(D)：");
    combo_date = new Combo(compTop, SWT.NONE);
    combo_date.setBounds(363, 10, 99, 25);
    // 为日期下拉框设置值，只分析7天内的日志
    setValueForDateCombo();

    btn_analyze = new Button(compTop, SWT.NONE);
    btn_analyze.setBounds(586, 9, 65, 25);
    btn_analyze.setText("分析(A)");

    btn_export = new Button(compTop, SWT.NONE);
    btn_export.setBounds(705, 8, 65, 27);
    btn_export.setText("导出(E)");
    btn_export.setEnabled(false);

    Group tableGroup = new Group(comp_bottom, SWT.NONE);
    tableGroup.setText("异常接口信息：");
    tableGroup.setLayout(new FillLayout(SWT.HORIZONTAL));

    table = new Table(tableGroup, SWT.BORDER | SWT.FULL_SELECTION);
    table.setHeaderVisible(true);
    table.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));

    TableColumn col_interface = new TableColumn(table, SWT.NONE);
    col_interface.setWidth(143);
    col_interface.setText("接口名");

    TableColumn col_err_location = new TableColumn(table, SWT.NONE);
    col_err_location.setWidth(190);
    col_err_location.setText("错误日志所在文件");

    TableColumn col_time = new TableColumn(table, SWT.NONE);
    col_time.setWidth(141);
    col_time.setText("时间");

    TableColumn col_lineNo = new TableColumn(table, SWT.NONE);
    col_lineNo.setWidth(50);
    col_lineNo.setText("行号");

    TableColumn col_transId = new TableColumn(table, SWT.NONE);
    col_transId.setWidth(189);
    col_transId.setText("交易ID");

    TableColumn col_request = new TableColumn(table, SWT.NONE);
    col_request.setWidth(222);
    col_request.setText("请求/响应");

    TableColumn col_errDesc = new TableColumn(table, SWT.NONE);
    col_errDesc.setWidth(205);
    col_errDesc.setText("错误描述");

    TableColumn col_errDetail = new TableColumn(table, SWT.NONE);
    col_errDetail.setMoveable(true);
    col_errDetail.setWidth(413);
    col_errDetail.setText("错误详情");

    tableCursor = new TableCursor(table, SWT.NONE);
    tableCursor.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
    tableCursor.setForeground(shell.getDisplay().getSystemColor(SWT.COLOR_WHITE));

    mainSashForm.setWeights(new int[] { 46, 581 });

    Menu menu = new Menu(shell, SWT.BAR);
    shell.setMenuBar(menu);

    MenuItem mi_file = new MenuItem(menu, SWT.CASCADE);
    mi_file.setText("文件(F)");

    Menu mu_file = new Menu(mi_file);
    mi_file.setMenu(mu_file);

    mi_add = new MenuItem(mu_file, SWT.NONE);
    mi_add.setText("添加配置\tCtrl+N");
    mi_add.setAccelerator(SWT.CTRL | 'N');

    mi_modify = new MenuItem(mu_file, SWT.NONE);
    mi_modify.setText("修改/删除配置\tCtrl+M");
    mi_modify.setAccelerator(SWT.CTRL | 'M');

    mi_open = new MenuItem(mu_file, SWT.NONE);
    mi_open.setText("打开\tCtrl+O");
    mi_open.setAccelerator(SWT.CTRL | 'O');

    MenuItem mi_seperator = new MenuItem(mu_file, SWT.SEPARATOR);

    mi_exit = new MenuItem(mu_file, SWT.NONE);
    mi_exit.setText("退出\tCtrl+Q");
    mi_exit.setAccelerator(SWT.CTRL | 'Q');

    // 为表格添加事件
    addListenterForTable();
    // 为表格光标增加选择事件
    addSelectionListenerForTableCursor();
    // 为menuItem添加选择监听器
    addSelectionListenerForMenuItem();
    // 为按钮添加选择监听
    addSelectionListenerForButton();
  }

  private void addSelectionListenerForButton() {
    // 日志分析入口
    btn_analyze.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(SelectionEvent e) {
        String host = combo_host.getText();
        if (StringUtils.isBlank(host)) {
          DialogUtils.showInformationDialog(shell, "提示", "host不能为空!");
          return;
        }

        String dateStr = combo_date.getText();
        if (StringUtils.isBlank(dateStr)) {
          DialogUtils.showInformationDialog(shell, "提示", "日期不能为空!");
          return;
        }

        // 将分析按钮设为不可用
        btn_analyze.setEnabled(false);
        btn_analyze.setText("正在分析...");
        // 清楚表格中原有的行
        table.removeAll();
        // 耗时操作，开启线程进行分析
        new Thread(() -> {
          Map<String, ConfigInfo> configMap = fileOp.readConfigFileToMap();
          ConfigInfo configInfo = configMap.get(host);
          configInfo.setHost(host);

          try {
            DataCenterLogAnalysisService logAnaysis = new DataCenterLogAnalysisServiceImpl(configInfo);
            List<ErrorInfo> errInfoList = logAnaysis.analyzeLogFromDataCenter(dateStr);

            // 通过UI线程为表格设置值
            Display.getDefault().asyncExec(() -> {

              if (CollectionUtils.isEmpty(errInfoList)) {
                btn_analyze.setEnabled(true);
                btn_analyze.setText("分析(A)");
                DialogUtils.showInformationDialog(shell, "提示", "无异常记录!");
                return;
              }

              // 为表格设置值
              errInfoList.forEach(errorInfo -> {
                TableItem ti = new TableItem(table, SWT.NONE);
                ti.setForeground(shell.getDisplay().getSystemColor(SWT.COLOR_BLACK));
                ti.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
                ti.setText(0, errorInfo.getInterfaceName());
                ti.setText(1, errorInfo.getErrLocation());
                ti.setText(2, errorInfo.getDate());
                ti.setText(3, String.valueOf(errorInfo.getLineNo()));
                ti.setText(4, errorInfo.getTransId());
                ti.setText(5, errorInfo.getReqAndResp());
                ti.setText(6, errorInfo.getErrDesc());
                ti.setText(7, errorInfo.getErrDetail());
              });

              // 将分析按钮重新设为可用
              btn_analyze.setEnabled(true);
              btn_analyze.setText("分析(A)");
              btn_export.setEnabled(true);
            });

          } catch (Exception exception) {
            Display.getDefault().asyncExec(() -> {
              btn_analyze.setEnabled(true);
              btn_analyze.setText("分析(A)");
              DialogUtils.showErrorDialog(shell, "错误", exception.getMessage());
            });
          }

        }).start();

      }

    });
    // 导出
    btn_export.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(SelectionEvent e) {
        String path =
            DialogUtils.showSaveFileDialog(shell, new String[] { "*.txt" }, new String[] { "Txt Files (*.txt)" });
        if (StringUtils.isBlank(path)) {
          return;
        }

        StringBuilder sb = new StringBuilder();
        TableItem[] tis = table.getItems();
        for (TableItem ti : tis) {
          sb.append(
              "------------------------------------------------------start----------------------------------------------")
              .append("\r\n");
          sb.append("接口名：").append(ti.getText(0)).append("\r\n");
          sb.append("错误日志文件：").append(ti.getText(1)).append("\r\n");
          sb.append("时间：").append(ti.getText(2)).append("\r\n");
          sb.append("行号：").append(ti.getText(3)).append("\r\n");
          sb.append("transactionId：").append(ti.getText(4)).append("\r\n");
          sb.append("请求/响应：\r\n").append(ti.getText(5)).append("\r\n");
          sb.append("错误详情：\r\n").append(ti.getText(7)).append("\r\n");
          sb.append(
              "-------------------------------------------------------end-----------------------------------------------")
              .append("\r\n\r\n");
        }

        try (PrintWriter writer = new PrintWriter(new FileOutputStream(path));) {
          writer.println(sb.toString());
          DialogUtils.showInformationDialog(shell, "提示", "导出成功!");
        } catch (FileNotFoundException exception) {
          DialogUtils.showWarningDialog(shell, "警告", "导出失败!");
        }
      }
    });
  }

  private void addListenterForTable() {
    table.addListener(SWT.MeasureItem, new Listener() {

      @Override
      public void handleEvent(Event event) {
        event.height = 30;
      }
    });
  }

  private void setValueForDateCombo() {
    Date currentDate = new Date();
    combo_date.add(DateUtils.parseDateToString(currentDate, DateUtils.DATE_FORMAT));
    Calendar c = Calendar.getInstance();
    c.setTime(currentDate);
    for (int count = 0; count < 6; count++) {
      c.add(Calendar.DAY_OF_MONTH, -1);
      combo_date.add(DateUtils.parseDateToString(c.getTime(), DateUtils.DATE_FORMAT));
    }
  }

  private void setValueForHostCombo() {
    Map<String, ConfigInfo> configMap = fileOp.readConfigFileToMap();
    if (CollectionUtils.isEmpty(configMap)) {
      return;
    }
    for (String host : configMap.keySet()) {
      combo_host.add(host);
    }
  }

  private void addSelectionListenerForTableCursor() {
    tableCursor.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetDefaultSelected(SelectionEvent e) {
        openExtraWindow();
      }

    });

    // 添加双击鼠标事件
    tableCursor.addMouseListener(new MouseAdapter() {

      @Override
      public void mouseDoubleClick(MouseEvent e) {
        openExtraWindow();
      }
    });

  }

  private void addSelectionListenerForMenuItem() {
    // 添加配置,打开对话窗口
    mi_add.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(SelectionEvent e) {
        NewConfigWindow window = new NewConfigWindow(combo_host);
        window.open();
      }
    });

    // 修改或删除配置
    mi_modify.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(SelectionEvent e) {
        ModifyConfigWindow window = new ModifyConfigWindow(combo_host);
        window.open();
      }
    });

    // 打开对话框
    mi_open.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(SelectionEvent event) {
        String[] extensions = { "*.json" };
        String[] names = { "Json Files(*.json)" };
        // 打开文件选择对话框
        String path = DialogUtils.showOpenFileDialog(shell, extensions, names);
        if (StringUtils.isEmpty(path)) {
          return;
        }

        Map<String, ConfigInfo> configMap = fileOp.readConfigFileToMap(new File(path));
        if (CollectionUtils.isEmpty(configMap)) {
          DialogUtils.showInformationDialog(shell, "提示", "配置文件内容为空或者格式不正确!");
          return;
        }

        // 先清空
        combo_host.removeAll();
        // 更新host下拉框
        for (String host : configMap.keySet()) {
          combo_host.add(host);
        }
        // 提示对话框
        DialogUtils.showInformationDialog(shell, "提示", "导入成功!");
        // 写入配置文件
        fileOp.writeToConfigFile(configMap);
      }
    });

    mi_exit.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(SelectionEvent e) {
        System.exit(0);
      }
    });

  }

  private void openExtraWindow() {
    // 弹出框显示详细信息
    int colNo = tableCursor.getColumn();
    String title = table.getColumn(colNo).getText();
    String errDetail = tableCursor.getRow().getText(colNo);

    // 打开错误异常接口
    ExtraWindow errDetailWindow = new ExtraWindow(title, errDetail);
    errDetailWindow.open();
  }
}
