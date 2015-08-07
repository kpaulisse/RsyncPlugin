package com.paulisse.eclipse.plugin.rsync.rsync;

import com.paulisse.eclipse.plugin.rsync.Activator;
import com.paulisse.eclipse.plugin.rsync.preferences.Preferences;
import com.paulisse.eclipse.plugin.rsync.properties.Properties;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.preference.IPreferenceStore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InvokeRsync {

  private static final String ERROR_PREFIX = "rsync Error: ";

  /**
   * Static method to assemble the 'rsync' command and execute it.
   *
   * @param project Reference to project
   * @return True if command exited with 0, False otherwise
   */
  public static boolean rsync(IProject project) {
    Activator plugin = Activator.getDefault();
    IPreferenceStore prefStore = plugin.getPreferenceStore();
    ArrayList<String> command = new ArrayList<String>();

    try {
      command.add(getPreference(prefStore, Preferences.PATH_RSYNC));

      command.addAll(Arrays.asList(getPreference(prefStore, Preferences.RSYNC_OPTS).split(" ")));

      String exclude =
          getProperty(project, Properties.EXCLUDE) + " "
              + getPreference(prefStore, Preferences.RSYNC_EXCL);
      if (!exclude.trim().equals("")) {
        for (String pattern : exclude.split("[\r\n ]")) {
          if (pattern != null && !pattern.equals("")) {
            command.add("--exclude");
            command.add(pattern.trim());
          }
        }
      }

      String sshPort = getProperty(project, Properties.SSH_PORT).trim();
      String sshArg = getProperty(project, Properties.SSH_OPTIONS).trim();
      StringBuilder sshOpts = new StringBuilder();
      sshOpts.append("ssh");
      if (sshPort != null && ! sshPort.equals("")) {
        if (sshPort.matches("^[0-9]+$")) {
          sshOpts.append(" -p ").append(sshPort);
        } else {
          plugin.log("rsync: Invalid SSH Port specified");
          return false;
        }
      }
      if (sshArg != null && ! sshArg.equals("")) {
        Pattern regex = Pattern.compile("^([a-zA-Z0-9_]+)=([\"']?)([a-zA-Z0-9_ /.\\-:~\\*]+)\\2$");
        for (String pattern : sshArg.split("[\r\n ]")) {
          if (pattern != null && !pattern.equals("")) {
            Matcher match = regex.matcher(pattern);
            if (! match.matches()) {
              plugin.log("rsync: Illegal SSH option " + pattern);
              return false;
            }
            sshOpts.append(String.format(" -o %s='%s'", match.group(1), match.group(3)));
          }
        }
      }
      command.add("-e");
      command.add(sshOpts.toString());

      command.add("--stats");

      command.add(project.getLocation().toString() + "/");

      String remotePath = getProperty(project, Properties.REMOTE_PATH);
      if (remotePath.equals("") || remotePath.equals("/")) {
        plugin.log("rsync: For safety, this plugin will not synchronize to the root file system");
        return false;
      }

      StringBuilder target = new StringBuilder();
      target.append(getProperty(project, Properties.USERNAME)).append("@");
      target.append(getProperty(project, Properties.SERVERNAME)).append(":");
      target.append(remotePath);
      command.add(target.toString());

      StringBuilder displayedCommand = new StringBuilder();
      Iterator<String> it = command.iterator();
      while (it.hasNext()) {
        String commandComponent = it.next();
        if (commandComponent.contains(" ")) {
          displayedCommand.append("\"").append(commandComponent).append("\"");
        } else {
          displayedCommand.append(commandComponent);
        }
        if (it.hasNext()) {
          displayedCommand.append(" ");
        }
      }
      plugin.log("---------- rsync STARTING ----------");
      plugin.log(displayedCommand.toString());
    } catch (MissingRequiredSettingException e) {
      plugin.log(ERROR_PREFIX + e.getMessage());
      return false;
    }

    try {
      ProcessBuilder pb = new ProcessBuilder(command);
      pb.redirectErrorStream(true);
      Process process = pb.start();
      BufferedReader inputReader =
          new BufferedReader(new InputStreamReader(process.getInputStream()));
      String line = null;
      while ((line = inputReader.readLine()) != null) {
        plugin.log(line);
      }
      inputReader.close();
      int errCode = process.waitFor();
      if (errCode == 0) {
        plugin.log("---------- rsync SUCCESSFUL ----------");
        return true;
      }
      plugin.log("---------- rsync FAILED ----------");
      return false;
    } catch (IOException e) {
      plugin.log(ERROR_PREFIX + e.getMessage());
      return false;
    } catch (InterruptedException e) {
      plugin.log(ERROR_PREFIX + e.getMessage());
      return false;
    }
  }

  private static String getPreference(IPreferenceStore prefStore, Preferences preference)
      throws MissingRequiredSettingException {
    String result = prefStore.getString(preference.getVariableName());
    if (result == null || result.equals("")) {
      if (preference.getEmptyOk()) {
        return "";
      }
      throw new MissingRequiredSettingException(String.format("Error: Preference not set (%s)",
          preference.getCaption()));
    }
    return result.replace("\r\n", " ").replace("\n", " ").replace("\r", " ").trim();
  }

  private static String getProperty(IProject project, Properties property)
      throws MissingRequiredSettingException {
    try {
      QualifiedName qn = new QualifiedName(Activator.PLUGIN_ID, property.getVariableName());
      String result = project.getPersistentProperty(qn);
      if (result == null || result.equals("")) {
        if (property.getEmptyOk()) {
          return "";
        }
        throw new MissingRequiredSettingException(String.format(
            "Error: Project property not set (%s)", property.getCaption()));
      }
      return result.trim();
    } catch (CoreException e) {
      throw new RuntimeException(e.getMessage());
    }
  }
}
