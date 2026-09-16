[![CI](https://github.com/marnix/MEPK/actions/workflows/ci.yml/badge.svg)](https://github.com/marnix/MEPK/actions/workflows/ci.yml)
[![Javadoc](https://img.shields.io/badge/Javadoc-online-blue)](https://marnix.github.io/MEPK/)

MEPK: a Minimal Extensible Proof Kernel
=======================================

Author: Marnix Klooster <marnix.klooster@gmail.com>

License: GPLv3


This is a Java library for building checked Metamath/Ghilbert-like proofs,
which should be sufficient for verifying all Ghilbert and most Metamath proofs.


Building
--------

MEPK is a standard Maven project.  To compile, run the tests, and install the
jar into your local repository:

    mvn clean install

To only compile and run the tests:

    mvn clean test

Requirements:

 - **JDK 17 or newer** to build.  The build uses `maven.compiler.release=17`,
   so it always produces **Java 17 class files** (bytecode major version 61)
   no matter which JDK compiles it — the resulting jar runs on **Java 17 or
   newer**.  Because `release` also restricts the compile-time API to Java 17,
   building on a newer JDK cannot accidentally pull in newer-than-17 APIs.
 - **Maven 3.6+** (any recent Maven 3).  All build plugins are pinned in
   `pom.xml`, so the build is reproducible on any Maven-supported platform
   (verified on Linux and Windows).

API documentation can be generated locally with:

    mvn javadoc:javadoc

Continuous integration runs on GitHub Actions (`.github/workflows/ci.yml`):
every pushed commit and pull request is built and tested on JDK 17 using the
runner image's bundled Maven (documented as Maven 3.9.16 / JDK 17.0.20 in the
current `ubuntu-24.04` image); each run logs the exact versions via
`mvn -version`.  The Javadoc for the latest commit on the default branch is
published to GitHub Pages at <https://marnix.github.io/MEPK/>.  (Build outputs
are not distributed; see the "distribute binaries?" to-do below.)


To-do list for functionality:

 - Design and implement abbreviations.  My current best design idea is the
   following.

   (TODO: Try and generalize or replace the idea below by using the concept of
   'profiles'.  See my ["Using 'profiles' for conservative extension /
   definition mechanism"](https://groups.google.com/forum/#!topic/metamath/lcl-OvLyVR0)
   mail to the Metamath mailing list.)
 
    * We do _not_ introduce a new 'abbreviation' proof step.
 
    * Every proof has a set of abbreviations AA, so that a proof means, "From
      grounding statements SS-after-expanding-
      all-of-AA one can construct statements TT-after-expanding-
      all-of-AA, using only proof steps."
      
      _Rationale._ We also expand AA in the grounding statements SS, since I've
      seen a case which I very much would like to work, for which I see no
      other solution.  (TODO: Add succinct description of such a use case.) I
      don't see any downside to this expansion of SS.  The alternatives are to
      forbid AA in SS, or not expand AA in SS, so that there would be no valid
      proof using an abbreviation A where SS uses A.  But that does not seem to
      solve any problem.
    
    * When verifying that a proof really proves statement T, it shows how to
      construct T-after-expanding-all-of-AA.
      
    * Abbreviation introduction is by a (non-kernel) proof which is created
      from a statement T and an abbreviation A: this proof grounds only T; has
      A as its sole abbreviation; and its only grounding statement is
      T-after-expanding-A.
      
    * Abbreviation elimination is by a (non-kernel) proof which is created from
      a proof P (with grounding SS and grounded TT) which has abbreviations A
      and AA: the created proof has grounding statements SS; its grounded
      statements are TT-after-expanding-A; and it has only
      abbreviations AA.

    * An abbreviation can also add hypotheses, so that it is possible to say,
      "(group-elem x) abbreviates (Real x) for which (> x (0))".
    
   Rationale.  The key property for an abbreviation mechanism, and in general
   for _any_ definition mechanism, is that an abbreviation should not allow new
   statements to be proved.  To be more precise, if we can construct T from SS
   using abbreviation A, where this abbreviation is not used in T, then it must
   also be possible to construct T from SS _without_ using abbreviation A.
   
   The above idea makes sure that this property is checked by our proof
   verification algorithm: the only part that will be built in is the
   expansion of an abbreviation.
   
   Implementation idea for verification of abbreviations:
   
    * Create `mepk.kernel.util.ExpandedAbbreviationsProof` which is a wrapper around
      an arbitrary proof.  This expands all the wrapped proof's abbreviations
      (in the grounded statements and in its justification `ProofStep`), and
      wraps the justification `Proof` again in a `NoAbbreviationsProof`.
   
    * `Proof#verify()` then wraps itself in this way, and verifies the result
      using the current verification algorithm.
   
   An alternative is to introduce an 'abbreviation' proof step.  That would
   make our verification algorithm simpler, but it makes it impossible to check
   the key property.
   
   Note: Using an abbreviation elimination proof, a proof "the positive reals
   form a group" can be used to translate statements about a group into
   statements about the positive reals.

   Open issue: Can these features be used to create a proof "the positive reals
   form a group"?  I think they can: it should be possible to create a proof
   based on the real number theorems, with abbreviations
   "(group-elem x) abbreviates (Real x) for which (> x (0))" and
   "(op x y) abbreviates (* x y)", of a statement like
   
   > `(group-elem x) AND (group-elem y) ==> (group-elem (op x y))`
   
   which expands to the two (!) statements
   
   >  `(Real x) AND (> x (0)) AND (Real y) AND (> y (0)) ==> (Real (* x y))`
   
   and
 
   >  `(Real x) AND (> x (0)) AND (Real y) AND (> y (0)) ==> (> (* x y) (0))`
   
 - Perhaps implement export based on a proof's justifications
   (`getJustificationFor()`)?  Idea for a format:
    * Stack-based like Metamath's;
    * For each of `getGrounded()`, first output (the used part of) the `Justification`'s `Proof`
      followed by its `ProofStep`;
    * Every part is output on a separate line, with a prefix HYP for the 'null' justifications,
      and prefixes COMPOSE, SUBSTITUTE, WEAKEN for the proof steps;
    * Compressed in BZip2 format (since the above has a _lot_ of duplication).

Implementation issues:
 
 - Change method names so that every Set<Statement> is called a 'theory', e.g.,
   getGrounding() -> getGroundingTheory()?  Con: The current names are short,
   and that is good.  For now I'll keep the current behavior.

 - `TrustedProof` name/function mismatch.  The class is named "Trusted" but is
   actually the *untrusted* composition layer built on top of `mepk.kernel`
   (it adds no primitive inference; its outputs are re-checkable by the
   kernel's `verify()`).  It lives in `mepk.builtin` on purpose (see commit
   5a9a110 "kernel split off": once `ProofStep` became a `Proof`,
   `TrustedProof` was pure composition).  Two fixes:
    * Rename it to a DSL-ish name (e.g. `Proofs` or `ProofBuilder`) so the
      name reflects "convenience constructor", not "trust boundary".
    * Move the trust-establishing `verify()` call OUT of its constructor
      (added in commit a1a8ff5, and only under `-ea`; see soundness list
      below).  A non-kernel class should not be the thing that (conditionally)
      decides trust.

Kernel correctness / soundness to-do (found in a 2026 audit; do the soundness
items BEFORE any hashing/signing/serialization work — they are ordered by
severity):

 - **S1 (critical): `internal/Substitute` performs NO disjoint-variable (DVR)
   check.**  Its body is `// TODO: Check arguments` then
   `statement.substitute(...)`, which only *propagates* the DVR set, never
   *validates* legality.  Since `Compose` does no unification, all substitution
   soundness rests on this rule -- so the kernel is currently unsound: a
   substitution can collapse a required distinctness (classic
   forall-x-exists-y (x!=y)  =>  exists-y (y!=y)).  Fix: in `Substitute`, for
   every DVR (a,b) of the statement, require the images of a and b to have
   disjoint variable sets after substitution; reject with
   `MEPKVerificationException` otherwise.  Add a test that a DVR-violating
   substitution throws *with assertions OFF*.

 - **S2: `DVRSet.substitute` silently DROPS distinctness for variable-free
   replacements** instead of validating it.  Make it validate rather than drop.

 - **S3: the `DVRSet` symmetry/consistency invariant is only enforced under
   `-ea`** (inside the `try{assert false;}catch(AssertionError){...}` trick), so
   with assertions off a malformed asymmetric `DVRSet` can be built.  A
   soundness *invariant* must ALWAYS hold: move the check out of the assertion
   trick, or make asymmetry unrepresentable (always insert both directions).

 - **S4: `andDistinct(DVRSet)` trusts (unenforced) symmetry of its input**,
   while `andDistinct(String...)` symmetrizes.  Make the merge symmetrize too.

 - **`verify()` is only CALLED under `-ea`.**  `TrustedProof`'s constructor
   gates the `verify()` call behind the assertion trick, so by default (JVM
   assertions off) proofs are constructed and never verified.  Either make
   verification unconditional, or -- better -- fix the constructors (S1) so
   "correct by construction" truly holds and keep `verify()` as a redundant,
   *independent* cross-check that always runs in CI.  NB: `verify()` currently
   only checks the *wiring* between steps and trusts each `ProofStep`'s
   `getGrounded1()`, so it cannot catch S1 by itself -- the DVR check must live
   in the rule constructor regardless.

 - **`ExpandedAbbreviationsProof` is a no-op stub** (`getGrounded()` /
   `getJustificationFor()` have `// TODO: ...with all abbreviations expanded`),
   yet it runs *inside* the TCB as `verify()`'s abbreviation-expansion step.
   Until it truly expands, abbreviations are not soundly handled (the
   `getAbbreviations().isEmpty()` guard papers over this).  Soundness-relevant
   because abbreviation conservativity is a TCB concern.

 - **`Justification` breaks the kernel's immutability discipline**: its
   `proofStep`/`proof` fields are non-`final` (unlike `Statement`/`Expression`/
   `DVRSet`), and its prerequisite check is an `assert`.  Make fields `final`;
   promote the check.

 - **`Var` and `App` are not `final`** (unlike the other kernel value types),
   and **`App`'s constructor stores the `Expression[]` without a defensive
   copy** -- a minor immutability leak inside the TCB.  Make them `final` and
   copy the array on construction.

 - **`verify()` is recursive** with an acknowledged JVM-stack-depth risk on
   deep proofs; convert to an explicit iterative worklist before running large
   corpora (e.g. `set.mm`).

 - **Assurance (not a bug, but needed to trust the above):** add a second,
   *independent* verifier as a differential cross-check on `set.mm`
   (candidate: the author's `marnix/zigmmverify`, or `checkmm`); fuzz the
   parser (which is outside the TCB, so parser bugs yield wrong/malformed
   statements, never false theorems).

 - **Distribute binaries?** Currently no need — CI does not keep build outputs.
   If a need arises, decide how: e.g. split the trusted kernel (`mepk.kernel`)
   into its own jar separate from `mepk.builtin` (parsers/DSL), so consumers
   can depend on just the TCB; and/or publish to Maven Central (which would
   also enable free Javadoc hosting via javadoc.io).

 - **Mark versioned releases** (low priority; prerequisite for the above and
   for javadoc.io): tag releases and drop the `-SNAPSHOT` for a released
   version.

