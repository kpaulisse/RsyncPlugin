package com.paulisse.eclipse.plugin.rsync.actions;

import com.paulisse.eclipse.plugin.rsync.builder.RsyncBuilderNature;
import com.paulisse.eclipse.plugin.rsync.util.CommonUtils;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;

public class AddRemoveAutosyncNatureHandler extends AbstractHandler {

  /**
   * Called when the event is executed.
   * @param event The execution event
   * @return null
   */
  public Object execute(ExecutionEvent event) throws ExecutionException {
    IProject project = CommonUtils.getProject(event);
    if (project != null) {
      try {
        toggleNature(project);
      } catch (CoreException e) {
        // TODO log something
        throw new ExecutionException("Failed to toggle nature", e);
      }
    }
    return null;
  }

  /**
   * Toggles nature on a project.
   *
   * @param project to have nature added or removed
   */
  private void toggleNature(IProject project) throws CoreException {
    IProjectDescription description = project.getDescription();
    String[] natures = description.getNatureIds();
    System.err.println(description.toString());

    for (int i = 0; i < natures.length; ++i) {
      if (RsyncBuilderNature.NATURE_ID.equals(natures[i])) {
        // Remove the nature
        String[] newNatures = new String[natures.length - 1];
        System.arraycopy(natures, 0, newNatures, 0, i);
        System.arraycopy(natures, i + 1, newNatures, i, natures.length - i - 1);
        description.setNatureIds(newNatures);
        project.setDescription(description, null);
        return;
      }
    }

    // Add the nature
    String[] newNatures = new String[natures.length + 1];
    System.arraycopy(natures, 0, newNatures, 0, natures.length);
    newNatures[natures.length] = RsyncBuilderNature.NATURE_ID;
    description.setNatureIds(newNatures);
    project.setDescription(description, null);
  }

}
