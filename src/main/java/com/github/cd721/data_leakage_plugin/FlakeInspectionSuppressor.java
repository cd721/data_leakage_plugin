package com.github.cd721.data_leakage_plugin;

import com.intellij.codeInspection.InspectionSuppressor;
import com.intellij.codeInspection.SuppressQuickFix;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FlakeInspectionSuppressor implements InspectionSuppressor {
    @Override
    //If we return true, every warning would be supressed
    //Inspection passed in as unique ID
    public boolean isSuppressedFor(@NotNull PsiElement element, @NotNull String toolId) {
        //return as quick as possible if we're not working w Python
        //don't suppress err
        if (!toolId.startsWith("Py")) {
            return false;
        }
        //look at structure of the file to see if there's a comment at the end of line
        return isSuppressedByEndOfLine(element) || isSuppressedByLineComment(element);
    }

    //looking at "leaf" elements
    //find line comment in any place
    //***This method is neither clean nor performant***, just an example
    //this method is called in main method in the extension point
    private boolean isSuppressedByLineComment(PsiElement element) {
        PsiFile file = element.getContainingFile();

        boolean[] found = {false};

        //visitor - calls method for every element
        //visit top level
        file.acceptChildren(new PsiRecursiveElementVisitor() {
            //visit child elements
            @Override
            public void visitComment(@NotNull PsiComment comment) {
                //check every comment along the way
                if(comment.getText().startsWith("# flake8: noqa")) {
                    //suppress
                    found[0] = true;
                }
            }
        });
        return found[0];
    }

    private boolean isSuppressedByEndOfLine(PsiElement element) {
        for (PsiElement leaf = PsiTreeUtil.nextLeaf(element); leaf != null; leaf = PsiTreeUtil.nextLeaf(leaf)) {
            // if we see a whitespace character that contains a new line, don't suppress
            if (leaf instanceof PsiWhiteSpace && leaf.textContains('\n')) {
                return false;
            }

            //but if the element has a specific marker,  we return true
            if (leaf instanceof PsiComment && leaf.getText().startsWith("# noqa")) {
                return true;
            }


        }
        return false;
    }

    @NotNull
    @Override
    public SuppressQuickFix[] getSuppressActions(@Nullable PsiElement element, @NotNull String toolId) {
        return new SuppressQuickFix[0];
    }
}
