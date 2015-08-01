package com.paulisse.eclipse.plugin.rsync;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle.
 */
public class Activator extends AbstractUIPlugin {

  // The plug-in ID
  public static final String PLUGIN_ID = "com.paulisse.eclipse.plugin.rsync"; //$NON-NLS-1$

  private static final String CONSOLE_NAME = "rsync Console";
  private MessageConsole messageConsole;
  private MessageConsoleStream logStream;

  // The shared instance
  private static Activator plugin;

  /**
   * The constructor.
   */
  public Activator() {}

  /**
   * Start plugin.
   * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
   */
  public void start(BundleContext context) throws Exception {
    initLogStream();
    showMessageConsole();
    super.start(context);
    plugin = this;
  }

  /**
   * Show the message console.
   */
  private void showMessageConsole() {
    IWorkbench wb = PlatformUI.getWorkbench();
    if (wb != null) {
      IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
      if (win != null) {
        IWorkbenchPage page = win.getActivePage();
        if (page != null) {
          String id = IConsoleConstants.ID_CONSOLE_VIEW;
          try {
            IConsoleView view = (IConsoleView) page.showView(id);
            view.display(messageConsole);
          } catch (PartInitException e) {
            return;
          }
        }
      }
    }
  }

  /**
   * Stop the plugin.
   * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
   */
  public void stop(BundleContext context) throws Exception {
    plugin = null;
    super.stop(context);
  }

  /**
   * Returns the shared instance.
   *
   * @return the shared instance
   */
  public static Activator getDefault() {
    return plugin;
  }

  /**
   * Returns an image descriptor for the image file at the given plug-in relative path.
   *
   * @param path the path
   * @return the image descriptor
   */
  public static ImageDescriptor getImageDescriptor(String path) {
    return imageDescriptorFromPlugin(PLUGIN_ID, path);
  }

  /**
   * Initialize the log stream.
   */
  private void initLogStream() {
    ConsolePlugin consolePlugin = ConsolePlugin.getDefault();
    IConsoleManager conMan = consolePlugin.getConsoleManager();
    MessageConsoleStream out = null;
    for (IConsole console : conMan.getConsoles()) {
      if (CONSOLE_NAME.equals(console.getName())) {
        MessageConsole messageConsole = (MessageConsole) console;
        this.messageConsole = messageConsole;
        logStream = messageConsole.newMessageStream();
        return;
      }
    }
    if (out == null) {
      MessageConsole myConsole = new MessageConsole(CONSOLE_NAME, null);
      this.messageConsole = myConsole;
      conMan.addConsoles(new IConsole[] {myConsole});
      logStream = myConsole.newMessageStream();
      return;
    }
  }

  /**
   * Log something to the console.
   * @param message The message to log
   */
  public void log(String message) {
    this.logStream.println(message);
    System.err.println(message);
  }
}
