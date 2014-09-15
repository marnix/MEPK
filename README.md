[![Build Status](https://buildhive.cloudbees.com/job/marnix/job/MEPK/badge/icon)](https://buildhive.cloudbees.com/job/marnix/job/MEPK/)

MEPK: a Minimal Extensible Proof Kernel
=======================================

Author: Marnix Klooster <marnix.klooster@gmail.com>

License: GPLv3


This is a Java library for building checked Metamath/Ghilbert-like proofs,
which should be sufficient for verifying all Ghilbert and most Metamath proofs.

See [the JavaDoc](https://buildhive.cloudbees.com/job/marnix/job/MEPK/javadoc/?mepk/package-summary.html)
for more information.


To-do list for functionality:

 - Design and implement abbreviations.  My current best design idea is the
   following.
 
    * We do _not_ introduce a new 'abbreviation' proof step.
 
    * Every proof has a set of abbreviations AA, so that a proof means, "From
      grounding statements SS one can construct statements TT-after-expanding-
      all-of-AA, using only proof steps."
    
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
