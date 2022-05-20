# Notes

## Singleton Scope

The singleton scope (of JSR-330 etc.) is also modeled here as the
"bootstrap" scope, i.e. if an object enters the DI world from
elsewhere, then it is of course a singleton, and so all other scopes,
being such objects, are governed by the singleton scope.  The
singleton scope is the one and only scope that is governed by itself.

## Dependent Scope

The idea of the dependent scope in CDI or the "per lookup" scope in
HK2 is really just the absence of a scope.  It means that nothing is
in charge of managing any lifecycle so you always get a new thing.
Consequently here the dependent scope is modeled as a _lack_ of scope,
i.e. `null`.

The dependent scope itself in some notional sense is governed by the
singleton scope.  This only matters during bootstrapping operations.

