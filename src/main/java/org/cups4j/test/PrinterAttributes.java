package org.cups4j.test;

import ch.ethz.vppserver.ippclient.IppResult;
import ch.ethz.vppserver.schema.ippclient.Attribute;
import ch.ethz.vppserver.schema.ippclient.AttributeGroup;
import ch.ethz.vppserver.schema.ippclient.AttributeValue;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
// TODO Remove the use of vector (Obsolete Collection) and use ArrayList instead.
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import org.cups4j.CupsClient;
import org.cups4j.CupsPrinter;
import org.cups4j.operations.ipp.IppGetPrinterAttributesOperation;

public class PrinterAttributes {
  private JTabbedPane mainTab = new JTabbedPane();
  private String hostname = "localhost";

  public static void main(String[] args) {
    PrinterAttributes pattr = new PrinterAttributes((args.length > 0) ? args[0] : null);
  }

  /**
   * @param args
   */
  public PrinterAttributes(String host) {
    try {
      if (host != null)
        hostname = host;

      JFrame frame = new JFrame("Drucker auf " + hostname);
      frame.setSize(800, 600);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setContentPane(mainTab);

      List<CupsPrinter> printers = new CupsClient().getPrinters();

      for (CupsPrinter p : printers) {
        IppGetPrinterAttributesOperation o = new IppGetPrinterAttributesOperation();
        IppResult result = o.request(p.getPrinterURL(), null);
        // IppResultPrinter.print(result);
        addPrinterPanel(p.getName(), result);
      }

      frame.setVisible(true);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      System.err.println(e.getMessage());
    }
  }

  private void addPrinterPanel(String name, IppResult result) {
    mainTab.add(getPrinterPanel(result), name);
  }

  private Container getPrinterPanel(IppResult result) {
    JPanel jp = new JPanel();
    jp.setLayout(new BorderLayout());
    JTabbedPane tab = new JTabbedPane();

    for (AttributeGroup group : result.getAttributeGroupList()) {
      if (group.getAttribute().size() > 0) {
        tab.add(gatAttributeTab(group), group.getTagName());
      }
    }

    jp.add(tab, BorderLayout.CENTER);
    return jp;
  }

  private Component gatAttributeTab(AttributeGroup group) {
    JPanel jp = new JPanel(new BorderLayout());
    ScrollPane scp = new ScrollPane();
    jp.add(scp, BorderLayout.CENTER);

    FormLayout layout = new FormLayout("12dlu, pref, 6dlu, 30dlu:grow, 3dlu");
    DefaultFormBuilder builder = new DefaultFormBuilder(layout);
    builder.setLeadingColumnOffset(1);

    Collections.sort(group.getAttribute(), new Comparator<Attribute>() {

      @Override
      public int compare(Attribute a1, Attribute a2) {
        return a1.getName().compareTo(a2.getName());
      }
    });

    for (Attribute att : group.getAttribute()) {
      JComponent valueComponent = null;
      if (att.getAttributeValue().size() > 0) {
        JPanel panel = new JPanel(new BorderLayout());

        AttributeValueTable table = new AttributeValueTable((getAttributeTableModel(att
            .getAttributeValue())));
        panel.add(table.getTableHeader(), BorderLayout.NORTH);
        panel.add(table, BorderLayout.CENTER);
        valueComponent = panel;

      } else {
        JLabel lb = new JLabel("no value reported");
        lb.setForeground(Color.red);
        valueComponent = lb;
      }
      builder.appendSeparator();
      builder.append(att.getName(), valueComponent);
      builder.nextLine();
    }
    scp.add(builder.getPanel());

    return jp;
  }

  private DefaultTableModel getAttributeTableModel(List<AttributeValue> list) {
    // TODO Remove the use of vector (Obsolete Collection) and use ArrayList instead.
    Vector<Vector<String>> data = new Vector<Vector<String>>();
    // TODO Remove the use of vector (Obsolete Collection) and use ArrayList instead.
    Vector<String> names = new Vector<String>();
    names.add("Tag Name");
    names.add("Tag (Hex)");
    names.add("Tag Value");
    for (AttributeValue attrValue : list) {
      data.add(getAttributeValue(attrValue));
    }
    return new DefaultTableModel(data, names);

  }

  private Vector<String> getAttributeValue(AttributeValue attrValue) {
    // TODO Remove the use of vector (Obsolete Collection) and use ArrayList instead.
    Vector<String> values = new Vector<String>();
    values.add(attrValue.getTagName());
    values.add(attrValue.getTag());
    values.add(attrValue.getValue());

    return values;
  }

  public class AttributeValueTable extends JTable {
    private static final long serialVersionUID = -9079318497719930285L;

    public AttributeValueTable(TableModel model) {
      super(model);
      TableColumnModel colmodel = getColumnModel();

      // Set column widths
      colmodel.getColumn(0).setPreferredWidth(100);
      colmodel.getColumn(1).setPreferredWidth(30);
      colmodel.getColumn(2).setPreferredWidth(150);
    }
  }

}