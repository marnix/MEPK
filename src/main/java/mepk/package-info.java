/**
 * This package implements MEPK, a minimal extensible proof kernel, with a Metamath/Ghilbert-like
 * language.
 * <p>
 * It is a <i>proof kernel</i>, i.e., a trusted core to create Java objects representing
 * mathematical proofs.  It is <i>minimal</i> in the sense that all valid proofs can be created
 * using this core alone.  It is <i>extensible</i>, so that it is possible (probably for
 * reasons of performance) to create the same proofs through new mechanisms.
 * <p>
 * The trusted kernel is the {@link mepk.kernel} package (with its subpackages):
 * all other proofs, including those in {@link mepk.builtin}, are built on top of that kernel, and
 * each proof can be verified using {@link mepk.kernel.Proof#verify()}.
 * <p> 
 * The {@link mepk.builtin} package contains some basic proofs and proof combinators.
 * The main entry point is {@link mepk.builtin.TrustedProof}.
 * The code was carefully designed to make sure that {@link mepk.kernel.Proof#verify()}
 * always succeeds for a {@code TrustedProof};
 * but in case of doubt you can always {@code verify()} that. 
 */
package mepk;