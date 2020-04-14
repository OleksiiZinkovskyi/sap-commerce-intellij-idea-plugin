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
import com.intellij.ide.util.frameworkSupport.FrameworkSupportProvider;
import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.idea.plugin.hybris.common.utils.HybrisIcons;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.StdModuleTypes;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author Oleksii Zinkovskyi
 */
public class HybrisFrameworkSupportProvider extends FrameworkSupportProvider {

    public HybrisFrameworkSupportProvider() {
        super(HybrisFrameworkSupportProvider.class.getName(), "Hybris");
    }

    @NotNull
    @Override
    public FrameworkSupportConfigurable createConfigurable(@NotNull FrameworkSupportModel model) {
        return new HybrisSupportConfigurable(model);
    }

    @Override
    public boolean isEnabledForModuleBuilder(@NotNull ModuleBuilder builder) {
        return false;
    }

    @Override
    public boolean isSupportAlreadyAdded(@NotNull Module module) {
        return false;
        // Forbids the add Hybris framework support action for projects already imported as Hybris
        //return CommonIdeaService.getInstance().isHybrisProject(module.getProject());

    }

    @Override
    public boolean isEnabledForModuleType(@NotNull ModuleType moduleType) {
        return moduleType.equals(StdModuleTypes.JAVA);
    }

    @Override
    public Icon getIcon() {
        return HybrisIcons.HYBRIS_ICON;
    }

}
