/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.configuration;

import org.gradle.api.internal.GradleInternal;
import org.gradle.api.internal.initialization.ClassLoaderScope;
import org.gradle.initialization.buildsrc.BuildSourceBuilder;
import org.gradle.internal.build.BuildStateRegistry;

public class DefaultBuildsPreparer implements BuildsPreparer {

    private final BuildSourceBuilder buildSourceBuilder;
    private final BuildStateRegistry buildStateRegistry;

    public DefaultBuildsPreparer(BuildSourceBuilder buildSourceBuilder, BuildStateRegistry buildStateRegistry) {
        this.buildSourceBuilder = buildSourceBuilder;
        this.buildStateRegistry = buildStateRegistry;
    }

    @Override
    public void prepareBuilds(GradleInternal gradle) {
        if (gradle.isRootBuild()) {
            buildStateRegistry.beforeConfigureRootBuild();
        }

        ClassLoaderScope baseProjectClassLoaderScope = buildSourceBuilder.buildAndCreateClassLoader(gradle);
        gradle.setBaseProjectClassLoaderScope(baseProjectClassLoaderScope);
    }
}
