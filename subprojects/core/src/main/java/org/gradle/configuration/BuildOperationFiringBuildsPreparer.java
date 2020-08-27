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
import org.gradle.internal.operations.BuildOperationCategory;
import org.gradle.internal.operations.BuildOperationContext;
import org.gradle.internal.operations.BuildOperationDescriptor;
import org.gradle.internal.operations.BuildOperationExecutor;
import org.gradle.internal.operations.RunnableBuildOperation;

public class BuildOperationFiringBuildsPreparer implements BuildsPreparer {
    private final BuildOperationExecutor buildOperationExecutor;
    private final BuildsPreparer delegate;

    public BuildOperationFiringBuildsPreparer(BuildOperationExecutor buildOperationExecutor, BuildsPreparer delegate) {
        this.buildOperationExecutor = buildOperationExecutor;
        this.delegate = delegate;
    }

    @Override
    public void prepareBuilds(GradleInternal gradle) {
        buildOperationExecutor.run(new ConfigureBuilds(gradle));
    }

    private class ConfigureBuilds implements RunnableBuildOperation {
        private final GradleInternal gradle;

        public ConfigureBuilds(GradleInternal gradle) {
            this.gradle = gradle;
        }

        @Override
        public void run(BuildOperationContext context) {
            delegate.prepareBuilds(gradle);
        }

        @Override
        public BuildOperationDescriptor.Builder description() {
            BuildOperationDescriptor.Builder builder = BuildOperationDescriptor.displayName(gradle.contextualize("Preparing builds"));
            builder.metadata(BuildOperationCategory.CONFIGURE_COMPOSITE);
            return builder;
        }
    }
}
