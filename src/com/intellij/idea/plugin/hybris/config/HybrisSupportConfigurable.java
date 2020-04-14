/*
 * This file is part of "SAP Commerce Developers Toolset" plugin for Intellij IDEA.
 * Copyright (C) 2019 EPAM Systems <hybrisideaplugin@epam.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.intellij.idea.plugin.hybris.config;


import com.intellij.ide.util.frameworkSupport.FrameworkSupportConfigurable;
import com.intellij.ide.util.frameworkSupport.FrameworkSupportModel;
import com.intellij.ide.util.newProjectWizard.AddModuleWizard;
import com.intellij.ide.util.newProjectWizard.StepSequence;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.idea.plugin.hybris.project.HybrisProjectImportProvider;
import com.intellij.idea.plugin.hybris.project.wizard.HybrisWorkspaceRootStep;
import com.intellij.idea.plugin.hybris.project.wizard.NonGuiSupport;
import com.intellij.idea.plugin.hybris.settings.HybrisProjectSettings;
import com.intellij.idea.plugin.hybris.settings.HybrisProjectSettingsComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.CompilerProjectExtension;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.projectImport.ProjectImportProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;


/**
 * @author Oleksii Zinkovskyi
 */
public class HybrisSupportConfigurable extends FrameworkSupportConfigurable {

    private static final Logger LOG = Logger.getInstance(HybrisSupportConfigurable.class);
    private FrameworkSupportModel frameworkSupportModel;

    private JPanel rootPanel;
    private AddModuleWizard moduleWizard;

    public HybrisSupportConfigurable(final FrameworkSupportModel model) {
        this.frameworkSupportModel = model;
    }

    @Override
    public JComponent getComponent() {

        try {
            moduleWizard = getWizard(frameworkSupportModel.getProject());

            rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.Y_AXIS));
            rootPanel.add(moduleWizard.getContentComponent());

            skipStepIfNotVisible();

            Button nextStep = new Button("Next Step");
            nextStep.addActionListener(e -> {
                moduleWizard.doNextAction();
                skipStepIfNotVisible();
            });

            Button navigateToStep = new Button("Navigate to Step");
            navigateToStep.addActionListener(e -> moduleWizard.navigateToStep(func -> func.getClass().equals(HybrisWorkspaceRootStep.class)));

            Button previousStep = new Button("Previous Step");
            //TODO: Add logic to skip over previous step if it is hidden or remain at the current step if not possible to skip
            previousStep.addActionListener(e -> moduleWizard.navigateToStep(
                func -> func.getClass().equals(moduleWizard.getSequence().getAllSteps().get(moduleWizard.getCurrentStep() - 1).getClass())));

            Button cancel = new Button("Cancel");
            cancel.addActionListener(e -> moduleWizard.doCancelAction());

            Button finish = new Button("Finish");
            finish.addActionListener(e -> moduleWizard.doFinishAction());

            rootPanel.add(nextStep);
            rootPanel.add(navigateToStep);
            rootPanel.add(previousStep);
            rootPanel.add(cancel);
            rootPanel.add(finish);

            return rootPanel;

        } catch (ConfigurationException e) {
            LOG.error(e);
        }

        return null;

    }

    private AddModuleWizard getWizard(final Project project) throws ConfigurationException {
        final HybrisProjectImportProvider provider = getHybrisProjectImportProvider();
        final String basePath = project.getBasePath();
        final String projectName = project.getName();
        final Sdk jdk = ProjectRootManager.getInstance(project).getProjectSdk();
        final String compilerOutputUrl = CompilerProjectExtension.getInstance(project).getCompilerOutputUrl();
        final HybrisProjectSettings settings = HybrisProjectSettingsComponent.getInstance(project).getState();

        final AddModuleWizard wizard = new AddModuleWizard(null, basePath, provider) {

            @Override
            protected void init() {
                // non GUI mode
            }
        };
        final WizardContext wizardContext = wizard.getWizardContext();
        wizardContext.setProjectJdk(jdk);
        wizardContext.setProjectName(projectName);
        wizardContext.setCompilerOutputDirectory(compilerOutputUrl);
        final StepSequence stepSequence = wizard.getSequence();
        for (ModuleWizardStep step : stepSequence.getAllSteps()) {
            if (step instanceof NonGuiSupport) {
                ((NonGuiSupport) step).nonGuiModeImport(settings);
            }
        }
        return wizard;
    }

    //TODO: Modify this one later on to remove unnecessary steps or hide/alter them in the wizard itself
    private HybrisProjectImportProvider getHybrisProjectImportProvider() {
        for (ProjectImportProvider provider : Extensions.getExtensions(ProjectImportProvider.PROJECT_IMPORT_PROVIDER)) {
            if (provider instanceof HybrisProjectImportProvider) {
                return (HybrisProjectImportProvider) provider;
            }
        }
        return null;
    }

    private void skipStepIfNotVisible() {
        if (!moduleWizard.getCurrentStepObject().isStepVisible()) {
            moduleWizard.doNextAction();
        }
    }

    @Override
    public void addSupport(@NotNull Module module, @NotNull ModifiableRootModel model, @Nullable Library library) {

        LOG.info("CALL SOMETHING FROM HERE!!!!!!");
        moduleWizard.doFinishAction();    //TODO: Currently doesn't seem to trigger import
        //MessageUtil.showOkCancelDialog()
    }

}
