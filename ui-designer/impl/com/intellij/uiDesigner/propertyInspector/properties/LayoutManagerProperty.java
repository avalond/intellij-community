package com.intellij.uiDesigner.propertyInspector.properties;

import com.intellij.uiDesigner.propertyInspector.Property;
import com.intellij.uiDesigner.propertyInspector.PropertyEditor;
import com.intellij.uiDesigner.propertyInspector.PropertyRenderer;
import com.intellij.uiDesigner.propertyInspector.editors.ComboBoxPropertyEditor;
import com.intellij.uiDesigner.propertyInspector.renderers.LabelPropertyRenderer;
import com.intellij.uiDesigner.radComponents.RadComponent;
import com.intellij.uiDesigner.radComponents.RadContainer;
import com.intellij.uiDesigner.radComponents.RadLayoutManager;
import com.intellij.uiDesigner.UIFormXmlConstants;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author yole
 */
public class LayoutManagerProperty extends Property<RadContainer> {
  private PropertyRenderer myRenderer = new LabelPropertyRenderer() {
    protected void customize(Object value) {
      setText((String) value);
    }
  };

  private static class LayoutManagerEditor extends ComboBoxPropertyEditor {
    public LayoutManagerEditor() {
      myCbx.setModel(new DefaultComboBoxModel(RadLayoutManager.getLayoutManagerNames()));
    }

    public JComponent getComponent(RadComponent component, Object value, boolean inplace) {
      myCbx.setSelectedItem(value);
      return myCbx;
    }
  }

  private PropertyEditor myEditor = new LayoutManagerEditor();

  public LayoutManagerProperty() {
    super(null, "Layout Manager");
  }

  public Object getValue(RadContainer component) {
    RadContainer container = component;
    while(container != null) {
      final RadLayoutManager layoutManager = container.getLayoutManager();
      if (layoutManager != null) {
        return layoutManager.getName();
      }
      container = container.getParent();
    }
    return UIFormXmlConstants.LAYOUT_INTELLIJ;
  }

  protected void setValueImpl(RadContainer component, Object value) throws Exception {
    if (component.getLayoutManager() != null && component.getLayoutManager().getName().equals(value)) {
      return;
    }

    RadLayoutManager newLayoutManager = RadLayoutManager.createLayoutManager((String) value);
    if (!newLayoutManager.canChangeLayout(component)) {
      throw new Exception("It is not allowed to change layout for non-empty containers");
    }

    component.setLayoutManager(newLayoutManager);
  }

  @NotNull public PropertyRenderer getRenderer() {
    return myRenderer;
  }

  public PropertyEditor getEditor() {
    return myEditor;
  }
}
