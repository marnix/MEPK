Introduction
============

Here is an example of several MEPK _theories_, for which we should be able to construct proofs.

The topic itself is trivial: we show how to construct the integers with addition from the natural numbers with addition,
and that the latter can be embedded in the former.

This example has several features that are helpful: it is small and self-contained, there are abbreviations with 'floating'
variables, and it does not require any specific logic rules.


Theories
========

Logic
-----

(not yet needed)

Equivalence
-----------

    # =T is an equivalence
    (T a) ==> (=T a a) # reflexive
    (T a) AND (T b) AND (=T a b) ==> (=T b a) # symmetric
    # Leibniz
    (T a) AND (T b) AND T(a2) AND (=T a a2) AND (=T a b) ==> (=T a2 b) 
    (T a) AND (T b) AND T(b2) AND (=T b b2) AND (=T a b) ==> (=T a b2) # transitive 

Untyped Pairs
-------------

    IMPORT Equivalence
    
    (T a) AND (T b) ==> (Pair (pair a b))
    (T a) AND (T b) AND (T c) AND (T d) AND (=P (pair a b) (pair c d)) ==> (=T a c)
    (=P (pair a b) (pair c d)) ==> (=T b d)

    # Leibniz    
    (=T a a2) ==> (=P (pair a b) (pair a2 b))
    (=T b b2) ==> (=P (pair a b) (pair a b2))
    
    # projections
    (=P p (pair a b)) ==> (=T (fst p) a)
    (=P p (pair a b)) ==> (=T (snd p) a)
    
Curious question: Is this sufficient to prove Leibniz for projections, i.e.,

    (=P p q) ==> (=T (fst p) (fst q))
    
and its `snd` equivalent?  And

    IMPORT Equivalence with T and =T replaced by Pair and =P

i.e., that `=P` is an equivalence? I think so, but would have to try.

Naturals
--------

    (Nat n) ==> (=N (+N n (0N)) n)
    (Nat n) AND (Nat m) ==> (=N (+N n m) (+N m n))
    
Integers
--------

    (Int i) ==> (=I (+I i (0I)) i)
    (Int i) AND (Int j) ==> (=I (+I i j) (+I j i))
    (Int i) ==> (=I (+I (-I i) i) (0I))


The proof
=========

To be able to do this proof, the following abbreviations will be used:

    (Int i)   =>=  (Pair i) AND (=P i (n m))   WITH   (Nat n) AND (Nat m)
    (=I i j)  =>=  (=N (+N (fst i) (snd j)) (+N (snd i) (fst j)))
    (+I i j)  =>=  (pair (+N (fst i) (fst j)) (+N (snd i) (snd j)))
    (0I)      =>=  (pair n n)   WITH   (Nat n)

With these,

    (Int i) ==> (=I (+I i (0I)) i)

expands to

    (Pair i) AND (=P i (n m)) AND (Nat n) AND (Nat m) AND (Nat n2)
     ==> (=N (+N (fst (+I i (pair n2 n2))) (snd i)) (+N (snd (+I i (pair n2 n2))) (fst i)))
 
and that looks like something that can be proved from  

    IMPORT Untyped Pairs with =T and T replaced by =N and Nat
    IMPORT Naturals
