package com.paulisse.eclipse.plugin.rsync.rsync;

public class MissingRequiredSettingException extends Exception {

  private static final long serialVersionUID = -7415704318903832461L;

  public MissingRequiredSettingException() {

  }

  public MissingRequiredSettingException(String arg0) {
    super(arg0);
  }

  public MissingRequiredSettingException(Throwable arg0) {
    super(arg0);
  }

  public MissingRequiredSettingException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }

  public MissingRequiredSettingException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
    super(arg0, arg1, arg2, arg3);
  }

}
