import java.util.*;

public class Main {

    public final static String ADD_SYMBOL = "+";
    public final static String MINUS_SYMBOL = "-";
    public final static String MULTIPLY_SYMBOL = "*";
    public final static String DIVIDE_SYMBOL = "/";
    public final static String LEFT_BRACKET_SYMBOL = "(";
    public final static String RIGHT_BRACKET_SYMBOL = ")";
    public final static String COMMA_SYMBOL = ",";

    // MAX symbol is "@", MIN symbol is "#"
    public final static String M_ALPHABET = "M";
    public final static String A_ALPHABET = "A";
    public final static String X_ALPHABET = "X";
    public final static String I_ALPHABET = "I";
    public final static String N_ALPHABET = "N";
    public final static String MAX_SYMBOL = "@";
    public final static String MIN_SYMBOL = "#";

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        String expression = scan.nextLine();

        List<String> expressionList = new ArrayList<>();
        List<String> calcSymbolList = new ArrayList<>();

        int totalLength = expression.length();
        int tempNum = 0;
        int powOfTen = 0;

        for (int i = totalLength-1; i>=0; i--) {
            String currentSubString = expression.substring(i, i+1);
            // when String is num, calc it
            if (!currentSubString.equals(ADD_SYMBOL) && !currentSubString.equals(MINUS_SYMBOL) &&
                    !currentSubString.equals(MULTIPLY_SYMBOL) && !currentSubString.equals(DIVIDE_SYMBOL) &&
                    !currentSubString.equals(LEFT_BRACKET_SYMBOL) && !currentSubString.equals(RIGHT_BRACKET_SYMBOL) &&
                    !currentSubString.equals(M_ALPHABET) && !currentSubString.equals(A_ALPHABET) &&
                    !currentSubString.equals(X_ALPHABET) && !currentSubString.equals(I_ALPHABET) &&
                    !currentSubString.equals(N_ALPHABET) && !currentSubString.equals(COMMA_SYMBOL)) {
                tempNum += Integer.parseInt(currentSubString) * Math.pow(10, powOfTen);
                powOfTen++;
                // when String is symbol, put num to expressionList and put symbol to calcSymbolList
            } else {
                // put the latest num to expressionList
                if (!currentSubString.equals(RIGHT_BRACKET_SYMBOL) && !expression.substring(i+1, i+2).equals(LEFT_BRACKET_SYMBOL) &&
                        !currentSubString.equals(M_ALPHABET) && !currentSubString.equals(A_ALPHABET) &&
                        !currentSubString.equals(X_ALPHABET) && !currentSubString.equals(I_ALPHABET) &&
                        !currentSubString.equals(N_ALPHABET)) {
                    expressionList.add(Integer.toString(tempNum));
                    tempNum = 0;
                    powOfTen = 0;
                }

                // if end of calcSymbolList is ) , put symbol to end of calcSymbolList
                if (!currentSubString.equals(LEFT_BRACKET_SYMBOL)) {
                    if (currentSubString.equals(RIGHT_BRACKET_SYMBOL) ||
                            currentSubString.equals(MULTIPLY_SYMBOL) || currentSubString.equals(DIVIDE_SYMBOL)) {
                        calcSymbolList.add(currentSubString);
                    } else if (currentSubString.equals(ADD_SYMBOL) || currentSubString.equals(MINUS_SYMBOL)){
                        int calcSymbolListSize = calcSymbolList.size();
                        while (calcSymbolListSize > 0 &&
                                (calcSymbolList.get(calcSymbolListSize - 1).equals(MULTIPLY_SYMBOL) || calcSymbolList.get(calcSymbolListSize - 1).equals(DIVIDE_SYMBOL))) {
                            // if priority of current symbol is lower than end of calcSymbolList,
                            // don't push end of calcSymbolList to expressionList until priority of current symbol is not
                            expressionList.add(calcSymbolList.get(calcSymbolListSize - 1));
                            calcSymbolList.remove(calcSymbolListSize - 1);
                            calcSymbolListSize = calcSymbolList.size();
                        }
                        calcSymbolList.add(currentSubString);
                    } else if (currentSubString.equals(COMMA_SYMBOL)) {
                        int calcSymbolListSize = calcSymbolList.size();
                        while (calcSymbolListSize > 0 && !calcSymbolList.get(calcSymbolListSize - 1).equals(COMMA_SYMBOL)) {
                            // if priority of current symbol is lower than end of calcSymbolList,
                            // don't push end of calcSymbolList to expressionList until priority of current symbol is not
                            expressionList.add(calcSymbolList.get(calcSymbolListSize - 1));
                            calcSymbolList.remove(calcSymbolListSize - 1);
                            calcSymbolListSize = calcSymbolList.size();
                        }
                        calcSymbolList.add(currentSubString);
                    }
                } else {
                    int calcSymbolListSize = calcSymbolList.size();
                    String strPrefix = expression.substring(i-1, i);
                    if (strPrefix.equals(X_ALPHABET) || strPrefix.equals(N_ALPHABET)) {
                        for (int temp = calcSymbolList.size() - 1; temp>=0; temp--) {
                            if (strPrefix.equals(X_ALPHABET)) {
                                calcSymbolList.add(temp, MAX_SYMBOL);
                                calcSymbolList.remove(temp + 1);
                                break;
                            }
                            if (strPrefix.equals(N_ALPHABET)) {
                                calcSymbolList.add(temp, MIN_SYMBOL);
                                calcSymbolList.remove(temp + 1);
                                break;
                            }
                        }
                    }
                    while (calcSymbolListSize > 0 &&
                            !(calcSymbolList.get(calcSymbolListSize - 1).equals(RIGHT_BRACKET_SYMBOL))) {
                        expressionList.add(calcSymbolList.get(calcSymbolListSize - 1));
                        calcSymbolList.remove(calcSymbolList.size() - 1);
                        calcSymbolListSize = calcSymbolList.size();
                    }
                    // remove the right bracket
                    calcSymbolList.remove(calcSymbolList.size() -1 );
                }
            }
        }
        // add the first num
        expressionList.add(Integer.toString(tempNum));

        // when scan over and calcSymbolList is not empty, put calcSymbolList to expressionList
        int calcSymbolListSize = calcSymbolList.size();
        while (calcSymbolListSize > 0) {
            expressionList.add(calcSymbolList.get(calcSymbolListSize - 1));
            calcSymbolList.remove(calcSymbolListSize - 1);
            calcSymbolListSize = calcSymbolList.size();
        }

        System.out.println(calcExpression(expressionList));
    }

    public static int calcExpression(List<String> str) {
        List<String> strExp = new ArrayList<>();
        int strExpLen = str.size();
        for (int i = strExpLen - 1; i>=0; i--) {
            strExp.add(str.get(i));
            System.out.print(str.get(i) + ",");
        }
        System.out.println();
        for (int i = strExpLen - 1; i>=0; i--) {
            int tmpResult = 0;
            if (strExp.get(i).equals(ADD_SYMBOL)) {
                tmpResult = myAdd(strExp.get(i+1) , strExp.get(i+2));
                i = afterCalc(strExp, tmpResult, i)- 1;
            } else if (strExp.get(i).equals(MINUS_SYMBOL)) {
                tmpResult = myMinus(strExp.get(i+1) ,strExp.get(i+2));
                i = afterCalc(strExp, tmpResult, i)- 1;
            } else if (strExp.get(i).equals(MULTIPLY_SYMBOL)) {
                tmpResult = myMultiply(strExp.get(i+1) , strExp.get(i+2));
                i = afterCalc(strExp, tmpResult, i)- 1;
            } else if (strExp.get(i).equals(DIVIDE_SYMBOL)) {
                tmpResult = myDivide(strExp.get(i+1) , strExp.get(i+2));
                i = afterCalc(strExp, tmpResult, i)- 1;
            } else if (strExp.get(i).equals(MAX_SYMBOL)) {
                tmpResult = myMax(strExp.get(i+1) , strExp.get(i+2));
                i = afterCalc(strExp, tmpResult, i)- 1;
            } else if (strExp.get(i).equals(MIN_SYMBOL)) {
                tmpResult = myMin(strExp.get(i+1) , strExp.get(i+2));
                i = afterCalc(strExp, tmpResult, i)- 1;
            }
        }

        return Integer.parseInt(strExp.get(0));
    }

    public static int myAdd(String a, String b) {
        return Integer.parseInt(a) + Integer.parseInt(b);
    }

    public static int myMinus(String a, String b) {
        return Integer.parseInt(a) - Integer.parseInt(b);
    }

    public static int myMultiply(String a, String b) {
        return Integer.parseInt(a) * Integer.parseInt(b);
    }

    public static int myDivide(String a, String b) {
        return Integer.parseInt(a) / Integer.parseInt(b);
    }

    public static int myMax(String a, String b) {
        return Integer.parseInt(a) > Integer.parseInt(b) ? Integer.parseInt(a): Integer.parseInt(b);
    }

    public static int myMin(String a, String b) {
        return Integer.parseInt(a) < Integer.parseInt(b) ? Integer.parseInt(a): Integer.parseInt(b);
    }

    public static int afterCalc(List<String> str, int tmpResult, int i) {
        str.add(i, Integer.toString(tmpResult));
        str.remove(i+3);
        str.remove(i+2);
        str.remove(i+1);
        myPrintArrayList(str);
        return str.size() - 1;
    }

    public static void myPrintArrayList(List<String> strings) {
        for (String str : strings) {
            System.out.print(str + ",");
        }
        System.out.println();
    }

}
