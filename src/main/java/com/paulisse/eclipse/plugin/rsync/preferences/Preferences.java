package com.paulisse.eclipse.plugin.rsync.preferences;

public enum Preferences {
  PATH_RSYNC("Path to rsync", "/usr/bin/rsync", "pathrsync", false, false),
  RSYNC_OPTS("rsync options", "-anvx", "rsyncopts", false, false),
  RSYNC_EXCL("rsync excludes\n(1 per line)", ".git\n.classpath\n.gradle\n.project\n.settings",
      "rsyncexcludes", true, true);

  private String caption;
  private String defaultValue;
  private String variableName;
  private Boolean multiLine;
  private Boolean emptyOk;

  private Preferences(String description, String defaultValue, String variableName,
      Boolean multiLine, Boolean emptyOk) {
    this.setCaption(description);
    this.setDefaultValue(defaultValue);
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
   * Get the default value for the field.
   * @return the defaultValue
   */
  public String getDefaultValue() {
    return defaultValue;
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
   * Set the default value for the field.
   * @param defaultValue the defaultValue to set
   */
  public void setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
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
