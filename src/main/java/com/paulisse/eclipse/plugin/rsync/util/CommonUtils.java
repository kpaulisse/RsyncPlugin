package com.paulisse.eclipse.plugin.rsync.util;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import java.util.Iterator;

public class CommonUtils {

  /**
   * Get the project associated with an event.
   * @param event The event
   * @return Reference to project
   * @throws ExecutionException ExecutionException
   */
  public static IProject getProject(ExecutionEvent event) throws ExecutionException {
    ISelection selection = HandlerUtil.getCurrentSelection(event);

    if (selection instanceof IStructuredSelection) {
      for (Iterator<?> it = ((IStructuredSelection) selection).iterator(); it.hasNext();) {
        Object element = it.next();
        IProject project = null;
        if (element instanceof IProject) {
          project = (IProject) element;
          return project;
        } else if (element instanceof IAdaptable) {
          project = (IProject) ((IAdaptable) element).getAdapter(IProject.class);
          return project;
        }
        throw new RuntimeException("Failed to get project");
      }
    }
    return null;
  }
}
