package action;

import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.generation.actions.BaseGenerateAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.psi.PsiClass;
import model.SPField;
import org.jetbrains.annotations.NotNull;
import utils.CodeGenerator;
import utils.Util;
import views.SharedPrefsDialog;

import java.util.List;

public class SharedPrefsAction extends BaseGenerateAction {
    public SharedPrefsAction() {
        super(null);
    }

    public SharedPrefsAction(CodeInsightActionHandler handler) {
        super(handler);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {

        SharedPrefsDialog dialog = new SharedPrefsDialog(e);
        dialog.setOnClickListener(new SharedPrefsDialog.onClickListener() {
            @Override
            public void onOK(List<SPField> SPFieldList) {

                if (SPFieldList.isEmpty())
                    return;

                FileEditorManager manager = FileEditorManager.getInstance(e.getProject());
                final Editor editor = manager.getSelectedTextEditor();

                final Document document = editor.getDocument();

                new WriteCommandAction(e.getProject()) {
                    @Override
                    protected void run(@NotNull Result result) throws Throwable {
                        PsiClass psiClassFromContext = Util.getPsiClassFromContext(e);


                        int textOffset = psiClassFromContext.getLastChild().getTextOffset();


                        if (!Util.isInitMethodExists(psiClassFromContext)) {
                            String code = CodeGenerator.generateInitMethod();
                            code += "\n \n" + CodeGenerator.generate(SPFieldList);
                            document.insertString(textOffset, code);


                        } else {
                            document.insertString(textOffset, CodeGenerator.generate(SPFieldList));

                        }

                    }
                }.execute();

            }


        });

        dialog.pack();
        //show dialog
        dialog.setVisible(true);

    }

}
