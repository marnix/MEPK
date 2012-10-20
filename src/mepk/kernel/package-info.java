/**
 * This package implements the trusted kernel of MEPK.
 * <p>
 * The basic building block is a {@link mepk.kernel.Statement}, representing statements like "for every
 * natural number n, n &gt;= 0". Any syntactically correct statement can be constructed, and
 * statements represent both axioms and theorems.
 * Then a {@link mepk.kernel.ProofStep} shows how to construct new statements from
 * existing statements: there are only a few built-in proof steps (substitute, compose, weaken)
 * and none can be added.
 * Finally a {@link mepk.kernel.Proof} represents a recipe for constructing one set of
 * statements for another using only ProofSteps.
 * <p>
 * The language of statements and their {@link mepk.kernel.Expression expressions} is that of Ghilbert,
 * but using Metamath's philosophy of types:
 * expressions are trees of constants and variables;
 * statements have hypotheses
 * and a conclusion; 
 * and dummy variables are handled using 'distinct variable restrictions'.
 * <p>
 * Every Proof can be verified mechanically by asking it to justify each of its
 * statements: this is implemented in {@link mepk.kernel.Proof#verify()}.
 * It should then provide a ProofStep which constructs that statement,
 * together with Proofs for the prerequisites of that ProofStep.
 */
package mepk.kernel;