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

package org.gradle.jvm.toolchain.internal;

import org.gradle.api.provider.ProviderFactory;

import javax.inject.Inject;
import java.io.File;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class LinuxInstallationSupplier extends AutoDetectingInstallationSupplier {

    private final String[] roots;

    @Inject
    public LinuxInstallationSupplier(ProviderFactory factory) {
        this(factory, "/usr/lib/jvm", "/usr/java");
    }

    public LinuxInstallationSupplier(ProviderFactory factory, String... roots) {
        super(factory);
        this.roots = roots;
    }

    @Override
    protected Set<InstallationLocation> findCandidates() {
        return Arrays.stream(roots)
            .map(root -> FileBasedInstallationFactory.fromDirectory(new File(root), "linux"))
            .flatMap(Set::stream)
            .collect(Collectors.toSet());
    }

}
