$[ _base.mm $]
$[ eq.mm $]
$[ pair.mm $]
$[ nat.mm $]

${

        $v i j n n' m $.

        $( TODO: using abbreviations

                (Int i)   =>=  (Pair i) AND (=P i (n m))   WITH   (N n) AND (N m)
                (=I i j)  =>=  (=N (+N (fst i) (snd j)) (+N (snd i) (fst j)))
                (+I i j)  =>=  (pair (+N (fst i) (fst j)) (+N (snd i) (snd j)))
                (0I)      =>=  (pair n n)   WITH   (N n)
                (-I i)    =>=  (pair (snd i) (fst i))

           add $p for hand-expanded versions of the following:

                (Int i) ==> (=I (+I i (0I)) i)
                (Int i) AND (Int j) ==> (=I (+I i j) (+I j i))
                (Int i) ==> (=I (+I (-I i) i) (0I))
        $)

        ${
                nat-in-int.plus0.1 $f Pair i $.
                nat-in-int.plus0.2 $f N n $.
                nat-in-int.plus0.3 $f N m $.
                nat-in-int.plus0.4 $f N n' $.
                nat-in-int.plus0.5 $e ( =P i ( pair n m ) ) $.
                $(
                        (Int i) ==> (=I (+I i (0I)) i)

                   expands to

                        (Pair i) AND (=P i (n m)) AND (N n) AND (N m) AND (N n')
                             ==> (=N (+N (fst (+I i (pair n' n'))) (snd i))
                                     (+N (snd (+I i (pair n' n'))) (fst i)))
                $)
                nat-in-int.plus0
                $p ( =N ( +N ( fst ( pair ( +N ( fst i ) ( fst ( pair n' n' ) ) )
                                          ( +N ( snd i ) ( snd ( pair n' n' ) ) ) ) )
                             ( snd i ) )
                        ( +N ( snd ( pair ( +N ( fst i ) ( fst ( pair n' n' ) ) )
                                          ( +N ( snd i ) ( snd ( pair n' n' ) ) ) ) )
                             ( fst i ) ) )
                $=
                  ? ? ? ? ? eqNtrans
                $.
        $}

$}
