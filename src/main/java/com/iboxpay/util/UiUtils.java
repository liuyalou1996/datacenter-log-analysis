package com.iboxpay.util;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class UiUtils {

  /**
   * 显示窗口为全屏的帮助方法
   */
  public static void showFullScreen(Shell shell) {
    shell.setBounds(0, 0, shell.getDisplay().getBounds().width, shell.getDisplay().getBounds().height);
  }

  /**
   * 将窗口显示在屏幕中间
   * @author liuyalou
   *
   */
  public static void centerShell(Display display, Shell shell) {
    Rectangle displayBounds = display.getPrimaryMonitor().getBounds();
    Rectangle shellBounds = shell.getBounds();
    int x = displayBounds.x + (displayBounds.width - shellBounds.width) >> 1;
    int y = displayBounds.y + (displayBounds.height - shellBounds.height) >> 1;
    shell.setLocation(x, y);
  }

}
