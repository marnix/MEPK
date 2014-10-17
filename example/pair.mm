$[ _base.mm $]
$[ eq.mm $]

$c Pair pair =P fst snd $.

${

        $v a a' b b' c d $.

        ${
                _pair-1a $f N a $.
                _pair-1b $f N b $.
                pairType $a Pair ( pair a b ) $.
        $}

        ${
                _pair-2a $f N a $.
                _pair-2b $f N b $.
                _pair-2c $f N c $.
                _pair-2d $f N d $.
                _pair-2e $e ( =P ( pair a b ) ( pair c d ) ) $.
                pairEqL $a ( =N a c )$.
        $}

        ${
                _pair-3a $f N a $.
                _pair-3b $f N b $.
                _pair-3c $f N c $.
                _pair-3d $f N d $.
                _pair-3e $e ( =P ( pair a b ) ( pair c d ) ) $.
                pairEqR $a ( =N b d )$.
        $}

        $( TODO: Add left/right Leibniz and left/right projections $)

$}
