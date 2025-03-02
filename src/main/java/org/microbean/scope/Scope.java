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

import java.lang.constant.ClassDesc;
import java.lang.constant.Constable;
import java.lang.constant.ConstantDesc;
import java.lang.constant.DynamicConstantDesc;
import java.lang.constant.MethodHandleDesc;
import java.lang.constant.MethodTypeDesc;

import java.util.List;
import java.util.Optional;

import org.microbean.attributes.Attributes;

import static java.lang.constant.ConstantDescs.BSM_INVOKE;
import static java.lang.constant.ConstantDescs.CD_boolean;
import static java.lang.constant.ConstantDescs.FALSE;
import static java.lang.constant.ConstantDescs.TRUE;

import static java.lang.constant.DirectMethodHandleDesc.Kind.STATIC;

import static org.microbean.assign.Qualifiers.primordialQualifier;
import static org.microbean.assign.Qualifiers.qualifier;

/**
 * A representation of the defining characteristics of a <dfn>scope</dfn>, a logical entity that manages the lifecycle
 * of objects.
 *
 * <p>A {@link Scope} is either a <dfn>{@linkplain #normal() normal} scope</dfn> or a
 * <dfn>pseudo-scope</dfn>. Implementations of normal scopes permit their objects to have circular dependencies, whereas
 * pseudo-scopes do not. Pragmatically, objects managed by normal scopes are usually instances of a particular kind of
 * <dfn>proxy</dfn>, namely a <dfn>client proxy</dfn>. Objects managed by pseudo-scopes may or may not be proxied, but
 * are not client proxies.</p>
 *
 * <p>A {@link Scope} is notionally <dfn>governed</dfn> by another scope which manages its lifecycle. A {@link Scope}
 * that reports that it is governed by itself is known as the <dfn>primordial scope</dfn>. All scopes are ultimately
 * governed by the primordial scope.</p>
 *
 * <p>In any program using {@link Scope}s, behavior is undefined if there exist two {@link Scope}s with equal
 * {@linkplain #id() identifiers} but any other differing attributes. Any collection of {@link Scope}s, in other words,
 * must be a {@link java.util.Set set}.</p>
 *
 * <p>Behavior is also undefined if any two scopes declare each other, by any means, as their respective governing
 * scopes.</p>
 *
 * @param id a non-{@code null} {@link Attributes} identifying this {@link Scope}
 *
 * @param normal whether this {@link Scope} is <dfn>normal</dfn>
 *
 * @author <a href="https://about.me/lairdnelson" target="_top">Laird Nelson</a>
 */
public record Scope(Attributes id, boolean normal) implements Constable {


  /*
   * Static fields.
   */


  /**
   * An {@link Attributes} identifying the <dfn>scope</dfn> designator.
   */
  public static final Attributes SCOPE = Attributes.of("Scope");


  /**
   * An {@link Attributes} identifying the (well-known) <dfn>singleton pseudo-scope</dfn>.
   *
   * <p>The {@link Attributes} constituting the singleton pseudo-scope identifier is {@linkplain Attributes#attributes()
   * attributed} with {@linkplain #scope() the scope designator}, {@linkplain
   * org.microbean.assign.Qualifiers#qualifier() the qualifier designator}, and {@linkplain
   * org.microbean.assign.Qualifiers#primordialQualifier() the primordial qualifier}, indicating that the scope it
   * identifies governs itself.</p>
   *
   * @see #SINGLETON
   */
  public static final Attributes SINGLETON_ID = Attributes.of("Singleton", qualifier(), scope(), primordialQualifier());

  /**
   * The (well-known and primordial) <dfn>singleton pseudo-scope</dfn>.
   *
   * @see #SINGLETON_ID
   */
  public static final Scope SINGLETON = Scope.of(SINGLETON_ID, false);


  /**
   * An {@link Attributes} identifying the (well-known and {@linkplain #normal() normal}) <dfn>application scope</dfn>.
   *
   * @see #APPLICATION
   */
  public static final Attributes APPLICATION_ID = Attributes.of("Application", qualifier(), scope(), SINGLETON_ID);

  /**
   * The (well-known and {@linkplain #normal() normal}) <dfn>application scope</dfn>.
   *
   * @see #APPLICATION_ID
   */
  public static final Scope APPLICATION = Scope.of(APPLICATION_ID, true);


  /**
   * An{@link Attributes} identifying the (well-known) <dfn>none pseudo-scope</dfn>.
   *
   * @see #NONE
   */
  public static final Attributes NONE_ID = Attributes.of("None", qualifier(), scope(), SINGLETON_ID);

  /**
   * The (well-known) <dfn>none pseudo-scope</dfn>.
   *
   * @see #NONE_ID
   */
  public static final Scope NONE = Scope.of(NONE_ID, false);


  /*
   * Constructors.
   */


  /**
   * Creates a new {@link Scope}.
   *
   * @param id a non-{@code null} {@link Attributes} identifying this {@link Scope}; must be a {@linkplain #scope()
   * scope}
   *
   * @param normal whether this {@link Scope} is <dfn>normal</dfn>
   *
   * @exception NullPointerException if any argument is {@code null}
   *
   * @exception IllegalArgumentException if any argument is not a {@linkplain #scope() scope}
   *
   * @see #of(Attributes, boolean)
   */
  public Scope {
    final Attributes qualifier = qualifier();
    final Attributes scope = scope();
    final List<Attributes> attributes = id.attributes();
    if (!attributes.contains(scope) || !attributes.contains(qualifier)) {
      throw new IllegalArgumentException("id: " + id);
    }
    Attributes governingScopeId = null;
    for (final Attributes a : attributes) {
      if (primordialQualifier(a)) {
        governingScopeId = id;
        break;
      } else {
        final List<Attributes> l = a.attributes();
        if (l.contains(scope) && l.contains(qualifier)) {
          governingScopeId = a;
          break;
        }
      }
    }
    if (governingScopeId == null) {
      throw new IllegalArgumentException("id: " + id);
    }
  }


  /*
   * Instance methods.
   */


  /**
   * Returns a non-{@code null} {@link Optional} housing a {@link ConstantDesc} describing this {@link Scope}, or an {@linkplain
   * Optional#empty() empty} {@link Optional} if this {@link Scope} could not be so described.
   *
   * @return a non-{@code null} {@link Optional}
   */
  @Override // Constable
  public final Optional<? extends ConstantDesc> describeConstable() {
    final ClassDesc CD_Scope = ClassDesc.of(Scope.class.getName());
    final ClassDesc CD_Attributes = ClassDesc.of(Attributes.class.getName());
    return this.id().describeConstable()
      .map(idDesc -> DynamicConstantDesc.of(BSM_INVOKE,
                                            MethodHandleDesc.ofMethod(STATIC,
                                                                      CD_Scope,
                                                                      "of",
                                                                      MethodTypeDesc.of(CD_Scope,
                                                                                        CD_Attributes,
                                                                                        CD_boolean)),
                                            idDesc,
                                            this.normal() ? TRUE : FALSE));
  }


  /*
   * Static methods.
   */


  /**
   * Returns a {@link Scope} suitable for the supplied arguments.
   *
   * @param id a non-{@code null} {@link Attributes} identifying this {@link Scope}
   *
   * @param normal whether this {@link Scope} is <dfn>normal</dfn>
   *
   * @return a non-{@code null} {@link Scope}
   *
   * @exception NullPointerException if any argument is {@code null}
   *
   */
  // This method is referenced by the describeConstable() method.
  public static final Scope of(final Attributes id, final boolean normal) {
    return new Scope(id, normal);
  }

  /**
   * Returns the {@linkplain #SCOPE scope designator}.
   *
   * @return a non-{@code null} {@link Attributes}
   */
  public static final Attributes scope() {
    return SCOPE;
  }

}
