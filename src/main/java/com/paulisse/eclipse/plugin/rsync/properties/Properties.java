package com.paulisse.eclipse.plugin.rsync.properties;

public enum Properties {
  SERVERNAME("Server Name", "serverName", false, false),
  USERNAME("Username", "username", false, false),
  SSH_PORT("SSH Port", "sshPort", false, true),
  SSH_OPTIONS("SSH Options", "sshOptions", true, true),
  REMOTE_PATH("Remote Path", "remotePath", false, false),
  EXCLUDE("Exclude dirs/files:\n(One pattern per line)", "exclude", true, true);

  private String caption;
  private String variableName;
  private Boolean multiLine;
  private Boolean emptyOk;

  private Properties(String caption, String variableName, Boolean multiLine, Boolean emptyOk) {
    this.setCaption(caption);
    this.setVariableName(variableName);
    this.setMultiLine(multiLine);
    this.setEmptyOk(emptyOk);
  }

  /**
   * Get the caption for the field. This is displayed in the UI.
   * @return the caption
   */
  public String getCaption() {
    return caption;
  }

  /**
   * Get whether it is OK for this field to be empty.
   * @return true if empty is OK, false if empty is not OK
   */
  public Boolean getEmptyOk() {
    return emptyOk;
  }

  /**
   * Get whether the field is multi-line.
   * @return true if the field is multi-line, false if the field is not multi-line
   */
  public Boolean getMultiLine() {
    return multiLine;
  }

  /**
   * Get the variable name. This is used internally by eclipse.
   * @return the variable name
   */
  public String getVariableName() {
    return variableName;
  }

  /**
   * Set the caption for the field. This is displayed in the UI.
   * @param caption the caption to set
   */
  public void setCaption(String caption) {
    this.caption = caption;
  }

  /**
   * Set whether it is OK for this field to be empty.
   * @param emptyOk true if empty is OK, false if empty is not OK
   */
  public void setEmptyOk(Boolean emptyOk) {
    this.emptyOk = emptyOk;
  }

  /**
   * Set whether the field is multi-line.
   * @param multiLine true if the field is multi-line, false if the field is not multi-line
   */
  public void setMultiLine(Boolean multiLine) {
    this.multiLine = multiLine;
  }

  /**
   * Set the variable name.  This is used internally by eclipse.
   * @param variableName the variable name
   */
  public void setVariableName(String variableName) {
    this.variableName = variableName;
  }
}
