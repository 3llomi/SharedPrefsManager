package utils;

import model.SPField;

import java.util.List;

public class CodeGenerator {
    public static String generate(List<SPField> SPFieldList) {
        StringBuilder finalCode = new StringBuilder();
        for (SPField SPField : SPFieldList) {
            String voidMethod = generateVoidMethod(SPField.getName(), SPField.getType());
            String returnMethod = generateReturnMethod(SPField.getName(), SPField.getType(), SPField.getDefaultValue());
            finalCode.append(voidMethod);
            finalCode.append(returnMethod);
        }
        return finalCode.toString();
    }

    public static String generateInitMethod() {
        StringBuilder builder = new StringBuilder();
        builder.append("private static SharedPreferences mSharedPref;\n \n");
        builder.append("public static void init(Context context)");
        builder.append("{");
        builder.append("\n");
        builder.append("if (mSharedPref == null)\n");
        builder.append("mSharedPref = PreferenceManager.getDefaultSharedPreferences(context);");
        builder.append("}");
        return builder.toString();
    }

    private static String generateVoidMethod(String fieldName, String methodType) {
        StringBuilder builder = new StringBuilder();
        builder.append("public static ");
        builder.append("void ");
        builder.append(formatMethodName(fieldName, methodType, false));
        builder.append("(");
        builder.append(methodType);
        builder.append(" value");
        builder.append(")");
        builder.append("{");
        builder.append("\n");
        builder.append("mSharedPref.edit().");
        builder.append(getPutWhatString(methodType));
        builder.append("(");
        builder.append("\"");
        builder.append(fieldName);
        builder.append("\"");
        builder.append(",");
        builder.append("value");
        builder.append(")");
        builder.append(".apply()");
        builder.append(";");
        builder.append("\n");
        builder.append("}");
        builder.append("\n");
        return builder.toString();
    }


    private static String generateReturnMethod(String fieldName, String methodType, String defaultValue) {
        StringBuilder builder = new StringBuilder();
        builder.append("public static ");
        builder.append(methodType);
        builder.append(" ");
        builder.append(formatMethodName(fieldName, methodType, true));
        builder.append("()");
        builder.append("{");
        builder.append("\n");
        builder.append("return ");
        builder.append("mSharedPref.");
        builder.append(getGetWhatString(methodType));
        builder.append("(");
        builder.append("\"");
        builder.append(fieldName);
        builder.append("\"");
        builder.append(",");
        builder.append(defaultValue);
        builder.append(")");
        builder.append(";");
        builder.append("\n");
        builder.append("}");
        builder.append("\n");
        return builder.toString();
    }

    private static String formatMethodName(String fieldName, String type, boolean returnType) {
        fieldName = upperCaseFirstChar(fieldName);

        if (returnType) {
            if (type.equals("boolean")) {
                return "is" + fieldName;
            } else {
                return "get" + fieldName;
            }
        } else {
            return "set" + fieldName;
        }
    }

    private static String upperCaseFirstChar(String fieldName) {
        fieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        return fieldName;
    }

    private static String getPutWhatString(String type) {
        switch (type) {
            case "String":
                return "putString";
            case "boolean":
                return "putBoolean";
            case "int":
                return "putInt";
            case "long":
                return "putLong";
            default:
                return "putFloat";
        }
    }

    private static String getGetWhatString(String type) {
        switch (type) {
            case "String":
                return "getString";
            case "boolean":
                return "getBoolean";
            case "int":
                return "getInt";
            case "long":
                return "getLong";
            default:
                return "getFloat";
        }
    }
}