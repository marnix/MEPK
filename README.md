[![Build Status](https://buildhive.cloudbees.com/job/marnix/job/MEPK/badge/icon)](https://buildhive.cloudbees.com/job/marnix/job/MEPK/)

MEPK: a Minimal Extensible Proof Kernel
=======================================

Author: Marnix Klooster <marnix.klooster@gmail.com>

License: GPLv3


This is a Java library for building checked Metamath/Ghilbert-like proofs,
which should be sufficient for verifying all Ghilbert and most Metamath proofs.

See [the JavaDoc](https://buildhive.cloudbees.com/view/My%20Repositories/job/marnix/job/MEPK/javadoc/?mepk/package-summary.html)
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
      S-after-expanding-A.
      
    * Abbreviation elimination is by a (non-kernel) proof which is created from
      a proof P (with grounding SS and grounded TT) which has abbreviations A
      and AA: the created proof has grounding statements SS; its grounded
      statements are TT-after-expanding-all-of-AA; and it has only
      abbreviations AA.

    * An abbreviation can also add hypotheses, so that it is possible to say,
      "(group-elem x) abbreviates (Real x) for which (> x 0)".
    
   Rationale.  The key property for an abbreviation mechanism, and in general
   for _any_ definition mechanism, is that an abbreviation should not allow new
   statements to be proved.  To be more precise, if we can construct T from SS
   using abbreviation A, where this abbreviation is not used in T, then it must
   also be possible to construct T from SS _without_ using abbreviation A.
   
   The above idea makes sure that this property is checked by our proof
   verification algorithm.
   
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
   form a group"?  I think they can, if we also have a 'hypothesis' proof step,
   which grounds any statement whose conclusion is identical to one of it
   hypotheses.
 
 - Perhaps implement export based on a proof's justifications
   (`getJustificationFor()`)?

Implementation issues:
 
 - Change method names so that every Set<Statement> is called a 'theory', e.g.,
   getGrounding() -> getGroundingTheory()?  Con: The current names are short,
   and that is good.
   
 - Implement a non-recursive version of the verification mechanism, to prevent
   stack overflow.
