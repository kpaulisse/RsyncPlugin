package com.paulisse.eclipse.plugin.rsync.builder;

import com.paulisse.eclipse.plugin.rsync.rsync.InvokeRsync;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import java.util.Map;

public class RsyncBuilder extends IncrementalProjectBuilder {

  public static final String BUILDER_ID = "com.paulisse.eclipse.plugin.rsync.rsyncBuilder";

  protected IProject[] build(int kind, @SuppressWarnings("rawtypes") Map args,
      IProgressMonitor monitor) throws CoreException {
    InvokeRsync.rsync(getProject());
    return null;
  }
}
