package com.paulisse.eclipse.plugin.rsync.actions;

import com.paulisse.eclipse.plugin.rsync.Activator;
import com.paulisse.eclipse.plugin.rsync.rsync.InvokeRsync;
import com.paulisse.eclipse.plugin.rsync.util.CommonUtils;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.resources.IProject;

public class SynchronizeNow implements IHandler {

  @Override
  public void addHandlerListener(IHandlerListener handlerListener) {
    // TODO Auto-generated method stub

  }

  @Override
  public void dispose() {
    // TODO Auto-generated method stub

  }

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    Activator plugin = Activator.getDefault();
    IProject project = CommonUtils.getProject(event);
    if (project != null) {
      InvokeRsync.rsync(project);
    } else {
      plugin.log("Unable to retrieve project for 'sync now' event");
    }
    return null;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public boolean isHandled() {
    return true;
  }

  @Override
  public void removeHandlerListener(IHandlerListener handlerListener) {
    // TODO Auto-generated method stub

  }

}
