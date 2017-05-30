// This is a generated file. Not intended for manual editing.
package com.intellij.idea.plugin.hybris.flexibleSearch.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface FlexibleSearchSubquery extends PsiElement {

    @Nullable
    FlexibleSearchCorrelationName getCorrelationName();

    @NotNull
    FlexibleSearchQuerySpecification getQuerySpecification();

    @NotNull
    List<FlexibleSearchSubquery> getSubqueryList();

    @NotNull
    PsiElement getLeftDoubleBrace();

    @NotNull
    PsiElement getRightDoubleBrace();

}
