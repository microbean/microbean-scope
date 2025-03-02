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

import java.lang.invoke.MethodHandles;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import org.microbean.attributes.Attributes;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.microbean.assign.Qualifiers.qualifier;

import static org.microbean.scope.Scope.SINGLETON_ID;
import static org.microbean.scope.Scope.scope;

final class TestConstableSemantics {

  private TestConstableSemantics() {
    super();
  }

  @Test
  final void testConstableSemantics() throws ReflectiveOperationException {
    final Attributes governingScopeId = Attributes.of("d.e.f", qualifier(), scope(), SINGLETON_ID);
    final Attributes scopeId = Attributes.of("a.b.c", qualifier(), scope(), governingScopeId);
    Scope scope = Scope.of(scopeId, true);
    assertEquals(scope, scope.describeConstable().orElseThrow().resolveConstantDesc(MethodHandles.publicLookup().in(Scope.class)));
  }

}
