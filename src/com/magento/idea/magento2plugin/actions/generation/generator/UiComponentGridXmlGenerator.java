/*
 * Copyright © Magento, Inc. All rights reserved.
 * See COPYING.txt for license details.
 */

package com.magento.idea.magento2plugin.actions.generation.generator;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.magento.idea.magento2plugin.actions.generation.data.UiComponentGridData;
import com.magento.idea.magento2plugin.actions.generation.data.UiComponentGridToolbarData;
import com.magento.idea.magento2plugin.actions.generation.generator.util.DirectoryGenerator;
import com.magento.idea.magento2plugin.actions.generation.generator.util.FileFromTemplateGenerator;
import com.magento.idea.magento2plugin.indexes.ModuleIndex;
import com.magento.idea.magento2plugin.magento.files.UiComponentGridXml;
import com.magento.idea.magento2plugin.magento.packages.File;
import com.magento.idea.magento2plugin.magento.packages.Package;
import java.util.Properties;

public class UiComponentGridXmlGenerator extends FileGenerator {
    private final UiComponentGridData uiComponentGridData;
    private final Project project;
    private final DirectoryGenerator directoryGenerator;
    private final ModuleIndex moduleIndex;
    private final FileFromTemplateGenerator fileFromTemplateGenerator;

    /**
     * UI component grid XML generator constructor.
     *
     * @param uiComponentGridData UiComponentGridData
     * @param project Project
     */
    public UiComponentGridXmlGenerator(
            final UiComponentGridData uiComponentGridData,
            final Project project
    ) {
        super(project);

        this.uiComponentGridData = uiComponentGridData;
        this.project = project;
        this.directoryGenerator = DirectoryGenerator.getInstance();
        this.moduleIndex = ModuleIndex.getInstance(project);
        this.fileFromTemplateGenerator = FileFromTemplateGenerator.getInstance(project);
    }

    @Override
    public PsiFile generate(String actionName) {
        final String moduleName = this.uiComponentGridData.getModuleName();
        PsiDirectory uiComponentDirectory = this.moduleIndex.getModuleDirectoryByModuleName(
                moduleName
        );
        final String subdirectory = String.format(
                "%s/%s/%s",
                Package.moduleViewDir,
                this.uiComponentGridData.getArea(),
                Package.moduleViewUiComponentDir
        );

        for (final String directory: subdirectory.split(File.separator)) {
            uiComponentDirectory = directoryGenerator.findOrCreateSubdirectory(
                    uiComponentDirectory,
                    directory
            );
        }

        UiComponentGridXml uiGridXml = new UiComponentGridXml(uiComponentGridData.getName());

        return this.fileFromTemplateGenerator.generate(
                uiGridXml,
                getAttributes(),
                uiComponentDirectory,
                actionName
        );
    }

    @Override
    protected void fillAttributes(Properties attributes) {
        attributes.setProperty("NAME", uiComponentGridData.getName());
        attributes.setProperty("ID_FIELD_NAME", uiComponentGridData.getIdFieldName());
        attributes.setProperty("ID_FIELD_NAME", uiComponentGridData.getIdFieldName());
        attributes.setProperty("PROVIDER_CLASS", uiComponentGridData.getProviderClassName());
        attributes.setProperty("ACL", uiComponentGridData.getAcl());
        final UiComponentGridToolbarData toolbarData = uiComponentGridData.getGridToolbarData();

        if (toolbarData.isAddToolbar()) {
            attributes.setProperty("TOOLBAR", "true");

            if (toolbarData.isAddBookmarks()) {
                attributes.setProperty("BOOKMARKS", "true");
            }

            if (toolbarData.isAddColumnsControls()) {
                attributes.setProperty("COLUMNS_CONTROLS", "true");
            }

            if (toolbarData.isAddFulltextSearch()) {
                attributes.setProperty("FULLTEXT_SEARCH", "true");
            }

            if (toolbarData.isAddListingFilters()) {
                attributes.setProperty("LISTING_FILTERS", "true");
            }

            if (toolbarData.isAddListingPaging()) {
                attributes.setProperty("LISTING_PAGING", "true");
            }
        }
    }
}
