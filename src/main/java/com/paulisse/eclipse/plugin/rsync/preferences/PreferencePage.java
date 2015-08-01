package com.paulisse.eclipse.plugin.rsync.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;

import com.paulisse.eclipse.plugin.rsync.Activator;
import com.paulisse.eclipse.plugin.rsync.preferences.thirdparty.MultiLineStringFieldEditor;

public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

  private static final String PREFERENCE_PAGE_TITLE = "Rsync Preferences";

  /**
   * The constructor.
   */
  public PreferencePage() {
    super(GRID);
    setPreferenceStore(Activator.getDefault().getPreferenceStore());
    setDescription(PREFERENCE_PAGE_TITLE);
  }

  /**
   * Creates the field editors. Field editors are abstractions of the common GUI blocks needed to
   * manipulate various types of preferences. Each field editor knows how to save and restore
   * itself.
   */
  public void createFieldEditors() {
    addField(new FileFieldEditor(Preferences.PATH_RSYNC.getVariableName(),
        Preferences.PATH_RSYNC.getCaption(), getFieldEditorParent()));
    addField(new StringFieldEditor(Preferences.RSYNC_OPTS.getVariableName(),
        Preferences.RSYNC_OPTS.getCaption(), getFieldEditorParent()));
    addField(new MultiLineStringFieldEditor(Preferences.RSYNC_EXCL.getVariableName(),
        Preferences.RSYNC_EXCL.getCaption(), getFieldEditorParent()));
  }

  /*
   * (non-Javadoc)
   *
   * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
   */
  public void init(IWorkbench workbench) {}

}
