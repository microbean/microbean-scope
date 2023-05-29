/* -*- mode: Java; c-basic-offset: 2; indent-tabs-mode: nil; coding: utf-8-unix -*-
 *
 * Copyright © 2022–2023 microBean™.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.microbean.scope;

import java.lang.constant.Constable;
import java.lang.constant.ConstantDesc;
import java.lang.constant.DynamicConstantDesc;
import java.lang.constant.MethodHandleDesc;
import java.lang.constant.MethodTypeDesc;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.microbean.qualifier.NamedAttributeMap;

import static java.lang.constant.ConstantDescs.BSM_INVOKE;
import static java.lang.constant.ConstantDescs.CD_boolean;
import static java.lang.constant.ConstantDescs.FALSE;
import static java.lang.constant.ConstantDescs.TRUE;

import static java.lang.constant.DirectMethodHandleDesc.Kind.STATIC;

import static org.microbean.qualifier.ConstantDescs.CD_NamedAttributeMap;

import static org.microbean.scope.ConstantDescs.CD_Scope;

public record Scope(NamedAttributeMap<?> id, boolean normal, NamedAttributeMap<?> governingScopeId)
  implements Constable, ScopeMember {


  /*
   * Static fields.
   */


  private static final NamedAttributeMap<?> QUALIFIER = NamedAttributeMap.of("Qualifier", Map.of(), Map.of(), List.of());

  private static final NamedAttributeMap<?> SCOPE = NamedAttributeMap.of("Scope", Map.of(), Map.of(), List.of(QUALIFIER));

  public static final NamedAttributeMap<?> SINGLETON_ID = NamedAttributeMap.of("Singleton", Map.of(), Map.of(), List.of(SCOPE));

  public static final Scope SINGLETON = Scope.of(SINGLETON_ID, false, SINGLETON_ID);

  public static final NamedAttributeMap<?> NONE_ID = NamedAttributeMap.of("None", Map.of(), Map.of(), List.of(SCOPE));

  public static final Scope NONE = Scope.of(NONE_ID, false, SINGLETON_ID);


  /*
   * Constructors.
   */


  public Scope {
    Objects.requireNonNull(id, "id");
    Objects.requireNonNull(governingScopeId, "governingScopeId");
  }


  /*
   * Instance methods.
   */


  @Override // Constable
  public final Optional<? extends ConstantDesc> describeConstable() {
    return this.id().describeConstable()
      .flatMap(idDesc -> this.governingScopeId().describeConstable()
               .map(governingScopeIdDesc -> DynamicConstantDesc.of(BSM_INVOKE,
                                                                   MethodHandleDesc.ofMethod(STATIC,
                                                                                             CD_Scope,
                                                                                             "of",
                                                                                             MethodTypeDesc.of(CD_Scope,
                                                                                                               CD_NamedAttributeMap,
                                                                                                               CD_boolean,
                                                                                                               CD_NamedAttributeMap)),
                                                                   idDesc,
                                                                   this.normal() ? TRUE : FALSE,
                                                                   governingScopeIdDesc)));
  }

  public final boolean primordial() {
    return this.id().equals(this.governingScopeId());
  }


  /*
   * Static methods.
   */


  // This method is referenced by the describeConstable() method.
  public static final Scope of(final NamedAttributeMap<?> id, final boolean normal, final NamedAttributeMap<?> governingScopeId) {
    return new Scope(id, normal, governingScopeId);
  }


}
