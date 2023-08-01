import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
public class Compiler{
    public final Tokenizer Tokens;  //Initialized as token
    public final PrintWriter p_write;   //Initialized as
    Compiler(String input) throws FileNotFoundException, IOException {
// Inputing the jack file from Nand2Tetris and initializing the name of the output file
        Tokens = new Tokenizer("D:\\Projects\\EOC\\21027_Eoc_Project_Compiler-I\\SquareGame.jack");
        p_write = new PrintWriter("Square_Game_output.xml");
        CompileClass();
        p_write.close();
    }
    public void CompileClass() throws IOException {
        //Using compile class method to extract classes
        Tokens.advance();
        p_write.write("<class>");
        p_write.println();

        if (Tokens.keyWord().equals("class")) {
            //extracts keywords from class
            p_write.write("<keyword> class </keyword>");
            p_write.println();
        }
        Tokens.advance();
        if (Tokens.tokenType().equals("IDENTIFIER")) {
            //extracts identifiers from class
            p_write.write("<identifier> " + Tokens.identifier() + " </identifier>");
            p_write.println();
        }
        Tokens.advance();
        if (Tokens.symbol() == '{') {
            //extracts symbols from class
            p_write.write("<symbol> { </symbol>");
            p_write.println();
        }
        Tokens.advance();
        while ((Tokens.keyWord().equals("static") || Tokens.keyWord().equals("field"))) {
            //Checking for Static Variables
            CompileClassVarDec();
        }
        while ((Tokens.keyWord().equals("constructor") | Tokens.keyWord().equals("function") | Tokens.keyWord().equals("method"))) {
            CompileSubroutine();
            Tokens.advance();
            //Checking the Constructors in the code
        }

        if (Tokens.symbol() == '}') {
            p_write.write("<symbol> } </symbol>");
            p_write.println();

        }

        p_write.write("</class>");
        p_write.println();

    }

    public void CompileClassVarDec() throws IOException {
        p_write.write("<classVarDec>");
        p_write.println();

        p_write.write("<keyword> " + Tokens.keyWord() + " </keyword>");
        p_write.println();

        Tokens.advance();

        if ((Tokens.keyWord().equals("int") | Tokens.keyWord().equals("char") | Tokens.keyWord().equals("boolean"))) {
            p_write.write("<keyword> " + Tokens.keyWord() + " </keyword>");
            p_write.println();

        } else if (Tokens.tokenType().equals("IDENTIFIER")) {
            p_write.write("<identifier> " + Tokens.identifier() + " </identifier>");
            p_write.println();

        }

        Tokens.advance();
        if (Tokens.tokenType().equals("IDENTIFIER")) {
            p_write.write("<identifier> " + Tokens.identifier() + " </identifier>");
            p_write.println();

        }

        Tokens.advance();
        while (Tokens.symbol() == ',') {
            p_write.write("<symbol> , </symbol>");
            p_write.println();

            Tokens.advance();
            p_write.write("<identifier> " + Tokens.identifier() + " </identifier>");
            p_write.println();

            Tokens.advance();
        }

        p_write.write("<symbol> ; </symbol>");
        p_write.println();

        Tokens.advance();

        p_write.write("</classVarDec>");
        p_write.println();

    }

    public void CompileSubroutine() throws IOException {
        p_write.write("<subroutineDec>");
        p_write.println();

        p_write.write("<keyword> " + Tokens.keyWord() + " </keyword>");
        p_write.println();

        Tokens.advance();

        if (Tokens.keyWord().equals("void")) {
            p_write.write("<keyword> " + Tokens.keyWord() + " </keyword>");
            p_write.println();

        } else {
            p_write.write("<identifier> " + Tokens.identifier() + " </identifier>");
        }
        p_write.println();

        Tokens.advance();

        p_write.write("<identifier> " + Tokens.identifier() + " </identifier>");
        p_write.println();

        Tokens.advance();

        p_write.write("<symbol> ( </symbol>");
        p_write.println();

        Tokens.advance();
        compileParameterList();

        p_write.write("<symbol> ) </symbol>");
        p_write.println();

        p_write.write("<subroutineBody>");
        p_write.println();

        Tokens.advance();

        p_write.write("<symbol> { </symbol>");
        p_write.println();

        Tokens.advance();

        while (Tokens.keyWord().equals("var")) {
            compileVarDec();
            Tokens.advance();
        }
        compileStatements();

        p_write.write("<symbol> } </symbol>");
        p_write.println();

        p_write.write("</subroutineBody>");
        p_write.println();
        p_write.write("</subroutineDec>");
        p_write.println();

    }

    public void compileParameterList() throws IOException {
        p_write.write("<parameterList>");
        p_write.println();

        if ((Tokens.keyWord().equals("int") || Tokens.keyWord().equals("char")
                || Tokens.keyWord().equals("boolean")
                || Tokens.tokenType().equals("IDENTIFIER"))) {

            p_write.write("<keyword> " + Tokens.keyWord() + "</keyword>");
            p_write.println();
            Tokens.advance();

            p_write.write("<identifier> " + Tokens.identifier() + "</identifier>");
            p_write.println();
            Tokens.advance();
        }

        while (Tokens.symbol() == ',') {
            p_write.write("<symbol> , </symbol>");
            p_write.println();
            Tokens.advance();

            p_write.write("<keyword> " + Tokens.keyWord() + "</keyword>");
            p_write.println();
            Tokens.advance();

            p_write.write("<identifier> " + Tokens.identifier() + "</identifier>");
            p_write.println();
            Tokens.advance();

        }

        p_write.write("</parameterList>");
        p_write.println();

    }

    public void compileVarDec() throws IOException {
        p_write.write("<varDec>");
        p_write.println();

        p_write.write("<keyword> var </keyword>");
        p_write.println();

        Tokens.advance();

        p_write.write("<keyword> " + Tokens.keyWord() + " </keyword>");
        p_write.println();
        Tokens.advance();

        p_write.write("<identifier> " + Tokens.identifier() + " </identifier>");
        p_write.println();
        Tokens.advance();

        while (Tokens.symbol() == ',') {
            p_write.write("<symbol> , </symbol>");
            p_write.println();
            Tokens.advance();

            p_write.write("<identifier> " + Tokens.identifier() + " </identifier>");
            p_write.println();

            Tokens.advance();
        }

        p_write.write("<symbol> ; </symbol>");
        p_write.println();

        p_write.write("</varDec>");
        p_write.println();

    }

    public void compileStatements() throws IOException {
        p_write.write("<statements>");
        p_write.println();

        while ((Tokens.keyWord().equals("let") || Tokens.keyWord().equals("if")
                || Tokens.keyWord().equals("while") || Tokens.keyWord().equals("do") || Tokens.keyWord().equals("return"))) {
            if (Tokens.keyWord().equals("let")) {
                compileLet();
                Tokens.advance();
            } else if (Tokens.keyWord().equals("if")) {
                compileIf();
            } else if (Tokens.keyWord().equals("while")) {
                compileWhile();
                Tokens.advance();
            } else if (Tokens.keyWord().equals("do")) {
                compileDo();
                Tokens.advance();
            } else if (Tokens.keyWord().equals("return")) {
                compileReturn();
                Tokens.advance();
            }

        }

        p_write.write("</statements>");
        p_write.println();

    }

    public void compileDo() throws IOException {
        p_write.write("<doStatement>");
        p_write.println();

        p_write.write("<keyword> do </keyword>");
        p_write.println();

        Tokens.advance();

        p_write.write("<identifier> " + Tokens.identifier() + " </identifier>");
        p_write.println();

        Tokens.advance();

        if (Tokens.symbol() == '(') {
            p_write.write("<symbol> ( </symbol>");
            p_write.println();

        } else if (Tokens.symbol() == '.') {
            p_write.write("<symbol> . </symbol>");
            p_write.println();

            Tokens.advance();

            p_write.write("<identifier> " + Tokens.identifier() + " </identifier>");
            p_write.println();

            Tokens.advance();

            p_write.write("<symbol> ( </symbol>");
            p_write.println();

        }

        Tokens.advance();
        CompileExpressionList();

        p_write.write("<symbol> ) </symbol>");
        p_write.println();

        Tokens.advance();

        p_write.write("<symbol> ; </symbol>");
        p_write.println();

        p_write.write("</doStatement>");
        p_write.println();

    }

    public void compileLet() throws IOException {
        p_write.write("<letStatement>");
        p_write.println();

        p_write.write("<keyword> let </keyword>");
        p_write.println();

        Tokens.advance();

        p_write.write("<identifier> " + Tokens.identifier() + " </identifier>");
        p_write.println();

        Tokens.advance();

        if (Tokens.symbol() == '[') {

            p_write.write("<symbol> [ </symbol>");
            p_write.println();

            Tokens.advance();
            CompileExpression();

            p_write.write("<symbol> ] </symbol>");
            p_write.println();

            Tokens.advance();
        }

        p_write.write("<symbol> = </symbol>");
        p_write.println();

        Tokens.advance();
        CompileExpression();

        p_write.write("<symbol> ; </symbol>");
        p_write.println();

        p_write.write("</letStatement>");
        p_write.println();

    }

    public void compileWhile() throws IOException {
        p_write.write("<whileStatement>");
        p_write.println();

        p_write.write("<keyword> while </keyword>");
        p_write.println();

        Tokens.advance();

        p_write.write("<symbol> ( </symbol>");
        p_write.println();

        Tokens.advance();
        CompileExpression();

        p_write.write("<symbol> ) </symbol>");
        p_write.println();
        Tokens.advance();

        p_write.write("<symbol> { </symbol>");
        p_write.println();

        Tokens.advance();
        compileStatements();

        p_write.write("<symbol> } </symbol>");
        p_write.println();

        p_write.write("</whileStatement>");
        p_write.println();

    }

    public void compileReturn() throws IOException {
        p_write.write("<returnStatement>");
        p_write.println();

        p_write.write("<keyword> return </keyword>");
        p_write.println();

        Tokens.advance();

        if (Tokens.symbol() != ';') {

            CompileExpression();
        }

        p_write.write("<symbol> ; </symbol>");
        p_write.println();

        p_write.write("</returnStatement>");
        p_write.println();

    }

    public void compileIf() throws IOException {
        p_write.write("<ifStatement>");
        p_write.println();

        p_write.write("<keyword> if </keyword>");
        p_write.println();
        Tokens.advance();

        p_write.write("<symbol> ( </symbol>");
        p_write.println();

        Tokens.advance();
        CompileExpression();

        p_write.write("<symbol> ) </symbol>");
        p_write.println();

        Tokens.advance();

        p_write.write("<symbol> { </symbol>");
        p_write.println();

        Tokens.advance();
        compileStatements();

        p_write.write("<symbol> } </symbol>");
        p_write.println();

        Tokens.advance();
        if (Tokens.keyWord().equals("else")) {
            p_write.write("<keyword> else </keyword>");
            p_write.println();

            Tokens.advance();

            p_write.write("<symbol> { </symbol>");
            p_write.println();

            Tokens.advance();
            compileStatements();

            p_write.write("<symbol> } </symbol>");
            p_write.println();

            Tokens.advance();
        }

        p_write.write("</ifStatement>");
        p_write.println();

    }

    public void CompileExpression() throws IOException {
        p_write.write("<expression>");
        p_write.println();

        CompileTerm();

        char sym = Tokens.symbol();
        while ((sym == '+' || sym == '-' || sym == '*' || sym == '/' || sym == '&' || sym == '|' || sym == '<'
                || sym == '>' || sym == '=')) {
            String temp = "";

            if (sym == '<') {
                temp = "&lt;";
            } else if (sym == '>') {
                temp = "&gt;";
            } else if (sym == '&') {
                temp = "&amp;";
            } else {
                temp = sym + "";
            }

            p_write.write("<symbol> " + temp + " </symbol>");
            p_write.println();

            Tokens.advance();
            CompileTerm();
            sym = Tokens.symbol();
        }

        p_write.write("</expression>");
        p_write.println();

    }

    public void CompileTerm() throws IOException {
        p_write.write("<term>");
        p_write.println();

        if (Tokens.tokenType().equals("INT_CONST")) {
            p_write.write("<integerConstant> " + Tokens.intVal() + " </integerConstant>");
            p_write.println();

            Tokens.advance();
        } else if (Tokens.tokenType().equals("STRING_CONST")) {
            p_write.write("<stringConstant> " + Tokens.stringVal() + " </stringConstant>");
            p_write.println();

            Tokens.advance();
        } else if (Tokens.tokenType().equals("KEYWORD")) {
            p_write.write("<keyword> " + Tokens.keyWord() + " </keyword>");
            p_write.println();

            Tokens.advance();
        } else if (Tokens.tokenType().equals("IDENTIFIER")) {

            p_write.write("<identifier> " + Tokens.identifier() + " </identifier>");
            p_write.println();

            Tokens.advance();
            if (Tokens.symbol() == '[') {
                p_write.write("<symbol> [ </symbol>");
                p_write.println();

                Tokens.advance();
                CompileExpression();

                p_write.write("<symbol> ] </symbol>");
                p_write.println();

                Tokens.advance();
            } else if ((Tokens.symbol() == '(' || Tokens.symbol() == '.')) {
                if (Tokens.symbol() == '(') {
                    p_write.write("<symbol> ( </symbol>");
                    p_write.println();

                } else if (Tokens.symbol() == '.') {
                    p_write.write("<symbol> . </symbol>");
                    p_write.println();

                    Tokens.advance();

                    p_write.write("<identifier> " + Tokens.identifier() + " </identifier>");
                    p_write.println();

                    Tokens.advance();

                    p_write.write("<symbol> ( </symbol>");
                    p_write.println();

                }

                Tokens.advance();
                CompileExpressionList();

                if (Tokens.symbol() == ')') {
                    p_write.write("<symbol> ) </symbol>");
                    p_write.println();

                }

                Tokens.advance();
            } else {

            }
        } else if (Tokens.tokenType().equals("SYMBOL") && Tokens.symbol() == '(') {
            p_write.write("<symbol> ( </symbol>");
            p_write.println();

            Tokens.advance();
            CompileExpression();

            if (Tokens.symbol() == ')') {
                p_write.write("<symbol> ) </symbol>");
                p_write.println();

            }

            Tokens.advance();
        } else if (Tokens.tokenType().equals("SYMBOL") && (Tokens.symbol() == '-' || Tokens.symbol() == '~')) {
            p_write.write("<symbol> " + Tokens.symbol() + " </symbol>");
            p_write.println();

            Tokens.advance();
            CompileTerm();
        }

        p_write.write("</term>");
        p_write.println();

    }

    public void CompileExpressionList() throws IOException {
        p_write.write("<expressionList>");
        p_write.println();
        if (Tokens.symbol() != ')') {
            CompileExpression();
            while (Tokens.symbol() == ',') {
                p_write.write("<symbol> , </symbol>");
                p_write.println();

                Tokens.advance();
                CompileExpression();
            }
        }
        p_write.write("</expressionList>");
        p_write.println();
    }
}
