/**
 * Copyright © Magento, Inc. All rights reserved.
 * See COPYING.txt for license details.
 */
package com.magento.idea.magento2plugin.reference.php;

import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceRegistrar;
import com.jetbrains.php.lang.PhpLanguage;
import com.jetbrains.php.lang.documentation.phpdoc.lexer.PhpDocTokenTypes;
import com.jetbrains.php.lang.documentation.phpdoc.psi.impl.tags.PhpDocTagImpl;
import com.jetbrains.php.lang.patterns.PhpPatterns;
import com.jetbrains.php.lang.psi.elements.impl.PhpPsiElementImpl;
import com.magento.idea.magento2plugin.reference.provider.FixtureReferenceProvider;
import com.magento.idea.magento2plugin.util.php.PhpPatternsHelper;
import com.magento.idea.magento2plugin.reference.provider.EventDispatchReferenceProvider;
import org.jetbrains.annotations.NotNull;

public class PhpReferenceContributor extends PsiReferenceContributor {
    public static final ElementPattern<? extends PsiElement> FIXTURE_DOC_TAG =
            PhpPatterns.psiElement(PhpDocTokenTypes.DOC_IDENTIFIER)
                    .withLanguage(PhpLanguage.INSTANCE);

    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        // ->dispatch("event_name")
        registrar.registerReferenceProvider(
                PhpPatternsHelper.STRING_METHOD_ARGUMENT,
                new EventDispatchReferenceProvider()
        );
        // Fixture
        registrar.registerReferenceProvider(
                FIXTURE_DOC_TAG,
                new FixtureReferenceProvider()
        );
    }
}
