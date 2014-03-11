/**
 * This package implements extensions on top of the {@link mepk.kernel}.
 * The main entry point is {@link mepk.builtin.TrustedProof}.
 * The code was carefully designed to make sure that {@link mepk.kernel.Proof#verify()}
 * always succeeds for a {@code TrustedProof};
 * but in case of doubt you can always {@code verify()} that. 
 * <p>
 * This package also contains a set of parsers in {@link mepk.builtin.MEPKParsers}.
 */
package mepk.builtin;