MEPK: a Minimal Extensible Proof Kernel
=======================================

Author: Marnix Klooster <marnix.klooster@gmail.com>
License: GPLv3


This is a Java library for building checked Metamath/Ghilbert-like proofs, which should
be sufficient for verifying all Ghilbert and most Metamath proofs.

See the Javadoc for more information.


To-do list for functionality:

 - Implement the 'compose' proof step.
 
 - Support (non-empty) DVRSets.
 
 - Design and implement abbreviations.  Idea:

    * An 'abbreviate' proof step, which is created from a statement S and an abbreviation;
      the proof step grounds S, and it's sole grounding statement is 'S with each instance
      of the abbreviation expanded'.
      
      This is the only way to introduce an abbreviation.
    
    * An 'expanded' proof, which is created from a proof P and an abbreviation name; the
      resulting proof has the same grounding statements as P, but its grounded statements
      are those of P, but with each instance of the abbreviation expanded.
      
      This is the only way to eliminate an abbreviation.
    
   With 'expanded', a proof "the positive reals form a group" can be used to translate
   statements about a group into statements about the positive reals.
   
   Open issue: can these features be used to create a proof "the positive reals form a
   group"?  I think they can, if we also have a 'hypothesis' proof step, which
   grounds any statement whose conclusion is identical to one of it hypotheses.


Implementation issues:
 
 - Change method names to that every Set<Statement> is called a 'theory', e.g., getGrounding()
   -> getGroundingTheory()?  Con: The current names are short, and that is good.
   
 - Implement a non-recursive version of the verification mechanism, to prevent stack overflow.
