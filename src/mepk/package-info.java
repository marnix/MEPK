/**
 * This package implements a minimal extensible proof kernel, with a Metamath/Ghilbert-like
 * language.
 * <p>
 * It is a <i>proof kernel</i>, i.e., a trusted core to create Java objects representing
 * mathematical proofs.  It is <i>minimal</i> in the sense that all valid proofs can be created
 * using this core alone.  It is <i>extensible</i>, so that it is possible (probably for
 * reasons of performance) to create the same proofs through new mechanisms.
 * <p>
 * The basic building block is a {@link mepk.Statement}, representing statements like "for every
 * natural number n, n &gt;= 0". Any syntactically correct statement can be constructed, and
 * statements represent both axioms and theorems.
 * Then a {@link mepk.ProofStep} shows how to construct new statements from
 * existing statements: there are only a few built-in proof steps (substitute, compose, weaken)
 * and none can be added.
 * Finally a {@link mepk.Proof} represents a recipe for constructing one set of
 * statements for another using only ProofSteps.
 * <p>
 * The language of statements and their {@link mepk.Expression expressions} is that of Ghilbert,
 * but using Metamath's philosophy of types:
 * expressions are trees of constants and variables;
 * statements have hypotheses
 * and a conclusion; 
 * and dummy variables are handled using 'distinct variable restrictions'.
 * <p>
 * Every Proof can be verified mechanically by asking it to justify each of its
 * statements.  It should then provide a ProofStep which constructs that statement,
 * together with Proofs for the prerequisites of that ProofStep.
 * <p>
 * A {@link mepk.TrustedProof} is a Proof which is part of the trusted kernel.
 */
package mepk;