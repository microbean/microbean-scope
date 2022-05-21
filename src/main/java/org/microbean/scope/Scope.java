/* -*- mode: Java; c-basic-offset: 2; indent-tabs-mode: nil; coding: utf-8-unix -*-
 *
 * Copyright © 2022 microBean™.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 */
package org.microbean.scope;

import java.lang.constant.Constable;
import java.lang.constant.ConstantDesc;
import java.lang.constant.DynamicConstantDesc;
import java.lang.constant.MethodHandleDesc;
import java.lang.constant.MethodTypeDesc;

import java.util.Objects;
import java.util.Optional;

import org.microbean.qualifier.Qualifier;

import static java.lang.constant.ConstantDescs.BSM_INVOKE;
import static java.lang.constant.ConstantDescs.CD_boolean;
import static java.lang.constant.ConstantDescs.FALSE;
import static java.lang.constant.ConstantDescs.TRUE;

import static java.lang.constant.DirectMethodHandleDesc.Kind.STATIC;

import static org.microbean.qualifier.ConstantDescs.CD_Qualifier;

import static org.microbean.scope.ConstantDescs.CD_Scope;

public final class Scope implements Constable, ScopeMember {


  /*
   * Static fields.
   */


  public static final Qualifier<?> SINGLETON_ID = Qualifier.of("Singleton");

  public static final Scope SINGLETON = Scope.of(SINGLETON_ID, false, SINGLETON_ID);

  public static final Qualifier<?> NONE_ID = Qualifier.of("None");

  public static final Scope NONE = Scope.of(NONE_ID, false, SINGLETON_ID);


  /*
   * Instance fields.
   */


  private final Qualifier<?> id;

  private final boolean normal;

  private final Qualifier<?> governingScopeId;


  /*
   * Constructors.
   */


  private Scope(final Qualifier<?> id,
                final boolean normal,
                final Qualifier<?> governingScopeId) {
    super();
    this.id = Objects.requireNonNull(id, "id");
    this.normal = normal;
    this.governingScopeId = Objects.requireNonNull(governingScopeId, "governingScopeId");
  }


  /*
   * Instance methods.
   */


  public final Qualifier<?> id() {
    return this.id;
  }

  public final boolean normal() {
    return this.normal;
  }

  @Override // ScopeMember
  public final Qualifier<?> governingScopeId() {
    return this.governingScopeId;
  }

  @Override // Constable
  public final Optional<? extends ConstantDesc> describeConstable() {
    final ConstantDesc idCd = this.id().describeConstable().orElse(null);
    if (idCd != null) {
      final ConstantDesc governingScopeIdCd = this.governingScopeId().describeConstable().orElse(null);
      if (governingScopeIdCd != null) {
        return
          Optional.of(DynamicConstantDesc.of(BSM_INVOKE,
                                             MethodHandleDesc.ofMethod(STATIC,
                                                                       CD_Scope,
                                                                       "of",
                                                                       MethodTypeDesc.of(CD_Scope,
                                                                                         CD_Qualifier,
                                                                                         CD_boolean,
                                                                                         CD_Qualifier)),
                                             idCd,
                                             this.normal() ? TRUE : FALSE,
                                             governingScopeIdCd));
      }
    }
    return Optional.empty();
  }

  public final boolean primordial() {
    return this.id().equals(this.governingScopeId());
  }

  @Override // Object
  public final int hashCode() {
    int hashCode = 1;
    hashCode = 31 * hashCode + this.id().hashCode();
    hashCode = 31 * hashCode + (this.normal() ? 1 : 0);
    hashCode = 31 * hashCode + this.governingScopeId().hashCode();
    return hashCode;
  }

  @Override // Object
  public final boolean equals(final Object other) {
    if (other == this) {
      return true;
    } else if (other != null && this.getClass() == other.getClass()) {
      final Scope her = (Scope)other;
      return
        this.id().equals(her.id()) &&
        this.normal() == her.normal() &&
        this.governingScopeId().equals(her.governingScopeId());
    } else {
      return false;
    }
  }

  @Override // Object
  public final String toString() {
    return this.id().name();
  }


  /*
   * Static methods.
   */


  // This method is referenced by the describeConstable() method.
  public static final Scope of(final Qualifier<?> id, final boolean normal, final Qualifier<?> governingScopeId) {
    return new Scope(id, normal, governingScopeId);
  }

}
