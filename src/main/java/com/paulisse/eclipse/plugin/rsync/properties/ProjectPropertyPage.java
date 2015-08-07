package com.paulisse.eclipse.plugin.rsync.properties;

import com.paulisse.eclipse.plugin.rsync.Activator;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.PropertyPage;

import java.util.HashMap;

public class ProjectPropertyPage extends PropertyPage {

  private HashMap<String, Text> propertyMap = new HashMap<String, Text>();

  public ProjectPropertyPage() {
    super();
  }

  /**
   * Prepares the fields on the project properties page.
   */
  private void prepareItems(Composite parent) {
    Composite composite = createDefaultComposite(parent);
    GridData gridData = new GridData();
    gridData.widthHint = convertWidthInCharsToPixels(40);

    GridData gridDataMultiLine = new GridData();
    gridDataMultiLine.widthHint = convertWidthInCharsToPixels(40);
    gridDataMultiLine.heightHint = convertWidthInCharsToPixels(15);

    IAdaptable element = getElement();
    IResource resource;
    try {
      resource = (IResource) element.getAdapter(IResource.class);
      if (element instanceof IResource) {
        resource = (IResource) element;
      }
    } catch (Exception e) {
      Activator.getDefault().log(e.getMessage());
      return;
    }

    for (Properties property : Properties.values()) {
      QualifiedName qn = new QualifiedName(Activator.PLUGIN_ID, property.getVariableName());
      String value;
      try {
        value = resource.getPersistentProperty(qn);
      } catch (CoreException e) {
        value = "";
      }
      if (value == null) {
        value = "";
      }
      Label label = new Label(composite, SWT.NONE);
      label.setText(property.getCaption());

      Text text =
          new Text(composite, (property.getMultiLine() ? SWT.MULTI : SWT.SINGLE) | SWT.BORDER);
      text.setText(value);
      text.setLayoutData(property.getMultiLine() ? gridDataMultiLine : gridData);

      propertyMap.put(property.getVariableName(), text);
    }
  }

  /**
   * Create contents.
   * @see PreferencePage#createContents(Composite)
   */
  protected Control createContents(Composite parent) {
    Composite composite = new Composite(parent, SWT.NONE);
    GridLayout layout = new GridLayout();
    composite.setLayout(layout);
    GridData data = new GridData(GridData.FILL);
    data.grabExcessHorizontalSpace = true;
    composite.setLayoutData(data);

    prepareItems(composite);

    return composite;
  }

  /**
   * Handles a click on "restore defaults".
   */
  protected void performDefaults() {
    for (Properties property : Properties.values()) {
      if (propertyMap.containsKey(property.getVariableName())) {
        Text text = propertyMap.get(property.getVariableName());
        text.setText("");
      }
    }
  }

  /**
   * Handles a click on OK / Apply.
   */
  public boolean performOk() {
    IAdaptable element = getElement();
    IResource resource;
    try {
      resource = (IResource) element.getAdapter(IResource.class);
    } catch (Exception e) {
      Activator.getDefault().log(e.getMessage());
      return false;
    }
    for (Properties property : Properties.values()) {
      String value = "";
      if (propertyMap.containsKey(property.getVariableName())) {
        Text text = propertyMap.get(property.getVariableName());
        value = text.getText();
      }
      try {
        QualifiedName qn = new QualifiedName(Activator.PLUGIN_ID, property.getVariableName());
        resource.setPersistentProperty(qn, value);
      } catch (CoreException e) {
        return false;
      }
    }
    return true;
  }

  /**
   * Creates the default 2 column layout for the property page.
   *
   * @param parent Parent object
   * @return Composite object
   */
  public static Composite createDefaultComposite(Composite parent) {
    Composite composite = new Composite(parent, SWT.NULL);

    GridLayout layout = new GridLayout();
    layout.numColumns = 2;
    composite.setLayout(layout);

    GridData data = new GridData();
    data.verticalAlignment = GridData.FILL;
    data.horizontalAlignment = GridData.FILL;
    composite.setLayoutData(data);

    return composite;
  }

}
