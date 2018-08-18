package views;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.ComboBox;
import model.SPField;
import utils.Util;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.event.*;
import java.util.List;

public class SharedPrefsDialog extends JDialog {
    private JPanel contentPane;
    public JButton btnGenerate;
    public JButton btnClose;
    public JTextField textField;
    public JTable tableView;
    private JButton addButton;
    private JComboBox comboBox;

    private onClickListener onClickListener;


    public SharedPrefsDialog(AnActionEvent actionEvent) {
        setContentPane(contentPane);
        setModal(true);


        init();

        btnGenerate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (onClickListener != null) {
                    onClickListener.onOK(SPField.getViewPartListFromTable(tableView));
                }
                onCancel();
            }
        });


        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });


        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<SPField> SPFields = SPField.getViewPartListFromTable(tableView);
                String text = textField.getText();
                if (!text.trim().isEmpty()) {

                    String type = comboBox.getSelectedItem().toString();
                    SPField field = new SPField(text, type);
                    if (SPFields.isEmpty() || !SPFields.contains(field)) {
                        SPFields.add(field);
                        updateTable(field, SPFields);
                    }
                    textField.setText("");
                    textField.requestFocus();


                }
            }
        });


        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });


        contentPane.registerKeyboardAction(new ActionListener() {
                                               @Override
                                               public void actionPerformed(ActionEvent e) {
                                                   SharedPrefsDialog.this.onCancel();
                                               }
                                           },
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);


        contentPane.registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (onClickListener != null) {
                    onClickListener.onOK(SPField.getViewPartListFromTable(tableView));
                }
                SharedPrefsDialog.this.onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }


    private void init() {
        tableView.setRowHeight(25);
        initComboBox();
    }

    private void initComboBox() {
        for (String s : SPField.getDataTypes()) {
            comboBox.addItem(s);
        }
    }

    private void updateTable(SPField SPField, List<SPField> SPFields) {
        DefaultTableModel tableModel = Util.getTableModel(SPFields);

        tableView.setModel(tableModel);


        ButtonColumn deleteButton = new ButtonColumn(tableView, new ButtonColumn.OnButtonClick() {
            @Override
            public void onClick(int row) {
                SPFields.remove(SPField.getViewPartFromRow(tableModel, row));
                tableModel.removeRow(row);

            }
        }, 3);

        deleteButton.setMnemonic(KeyEvent.VK_D);
        addComboBoxColumn(SPField.getType());


    }

    private void onCancel() {
        dispose();
    }


    public interface onClickListener {

        void onOK(List<SPField> SPFieldList);

    }

    public void setOnClickListener(SharedPrefsDialog.onClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    private void addComboBoxColumn(String type) {
        TableColumn typeColumn = tableView.getColumnModel().getColumn(1);
        ComboBox comboBox = new ComboBox();
        for (String s : SPField.getDataTypes()) {
            comboBox.addItem(s);
        }
        comboBox.setSelectedItem(type);
        typeColumn.setCellEditor(new DefaultCellEditor(comboBox));

        //on Change
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = tableView.convertRowIndexToModel(tableView.getEditingRow());
                if (row != -1) {
                    String defaultValue = SPField.getDefaultValue(comboBox.getSelectedItem().toString());
                    tableView.getModel().setValueAt(defaultValue, row, 2);
                }

            }
        });
    }
}
