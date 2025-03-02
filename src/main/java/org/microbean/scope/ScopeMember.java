/* -*- mode: Java; c-basic-offset: 2; indent-tabs-mode: nil; coding: utf-8-unix -*-
 *
 * Copyright © 2022–2025 microBean™.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.microbean.scope;

import java.util.Objects;

import org.microbean.attributes.Attributes;

/**
 * A governed member of a governing {@link Scope}.
 *
 * @author <a href="https://about.me/lairdnelson" target="_top">Laird Nelson</a>
 *
 * @deprecated This interface will be removed with no replacement.
 */
@Deprecated(forRemoval = true)
@FunctionalInterface
public interface ScopeMember {

  /**
   * Returns an {@link Attributes} identifying the scope that governs this {@link ScopeMember}.
   *
   * @return a non-{@code null} {@link Attributes}
   */
  public Attributes governingScopeId();

  /**
   * Returns {@code true} if and only if this {@link ScopeMember} is governed by a scope identified by the supplied
   * {@code governingScopeId}.
   *
   * @param governingScopeId an {@link Attributes}; may be {@code null} in which case {@code false} will be returned
   *
   * @return {@code true} if and only if this {@link ScopeMember} is governed by a scope identified by the supplied
   * {@code governingScopeId}
   */
  public default boolean governedBy(final Attributes governingScopeId) {
    return Objects.equals(this.governingScopeId(), governingScopeId);
  }

}
