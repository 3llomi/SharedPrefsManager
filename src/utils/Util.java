package utils;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import model.SPField;

import javax.swing.table.DefaultTableModel;
import java.util.List;

public class Util {
    public static PsiClass getPsiClassFromContext(AnActionEvent e) {
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        Editor editor = e.getData(PlatformDataKeys.EDITOR);


        if (psiFile == null || editor == null) {
            return null;
        }

        int offset = editor.getCaretModel().getOffset();
        PsiElement element = psiFile.findElementAt(offset);

        return PsiTreeUtil.getParentOfType(element, PsiClass.class);
    }

    public static boolean isInitMethodExists(PsiClass psiClassFromContext) {
        return psiClassFromContext.findMethodsByName("init", true).length > 0;
    }

    public static DefaultTableModel getTableModel(List<SPField> SPFields) {
        String[] HEADERS = {"name", "type", "defaultValue", ""};

        DefaultTableModel tableModel;
        int size = SPFields.size();
        Object[][] cellData = new Object[size][4];
        for (int i = 0; i < size; i++) {
            SPField SPField = SPFields.get(i);
            for (int j = 0; j < 3; j++) {
                switch (j) {
                    case 0:
                        cellData[i][j] = SPField.getName();
                        break;
                    case 1:
                        cellData[i][j] = SPField.getType();
                        break;

                    case 2:
                        cellData[i][j] = SPField.getDefaultValue();
                        break;

                    case 3:
                        cellData[i][j] = "";
                        break;


                }
            }
        }


        tableModel = new DefaultTableModel(cellData, HEADERS) {
            final Class[] typeArray = {Object.class, Object.class, Object.class, Object.class};

            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }

            @SuppressWarnings("rawtypes")
            public Class getColumnClass(int column) {
                return typeArray[column];
            }
        };


        return tableModel;
    }
}
