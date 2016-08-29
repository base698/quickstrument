import java.util.Stack;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.TokenStreamRewriter;

public class InsertMethodLogsListener extends Java8BaseListener {
    TokenStreamRewriter rewriter;
    String currentJava8Class;
    Stack<String> currentMethod = new Stack<String>();

    public InsertMethodLogsListener(TokenStream tokens) {
        rewriter = new TokenStreamRewriter(tokens);
    }

    @Override
    public void enterNormalClassDeclaration(Java8Parser.NormalClassDeclarationContext ctx) {
        currentJava8Class = ctx.Identifier().getText();
    }

    @Override 
    public void enterMethodDeclaration(Java8Parser.MethodDeclarationContext ctx) {
        currentMethod.push(ctx.methodHeader().methodDeclarator().Identifier().getText());
    }

    @Override
    public void enterMethodBody(Java8Parser.MethodBodyContext ctx) {
        if(ctx.block() != null && !ctx.block().getText().trim().equals("{}") ) {
           rewriter.insertAfter(ctx.start, "\n\tlong ___startTime_ = System.nanoTime();\n\ttry {\n");
        }
    }

    @Override
    public void exitMethodBody(Java8Parser.MethodBodyContext ctx) {
        if(ctx.block() != null && !ctx.block().getText().trim().equals("{}")) {
          String methodName;
          if(currentMethod.isEmpty()) {
             methodName = "null";
          } else {
             methodName = currentMethod.pop();
          }
          String fin = "\n\t} finally { long ___endTime_ = System.nanoTime();\n";
          fin += " System.out.println(\"%%!!%%" + currentJava8Class + "." + methodName + "() took \" + ((___endTime_ - ___startTime_) / 1000000) + \" ms\");\n}\n";

          rewriter.insertBefore(ctx.stop, fin);
        } else {
           if(!currentMethod.isEmpty()) {
              currentMethod.pop();
           }
        }

    }
}
