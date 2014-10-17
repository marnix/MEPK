$[ _base.mm $]

$( $c Nat $. $)
$c =N $.

${

        $v a a' b b' c $.

        ${
                _eq-1 $f N a $.
                eqNrefl $a ( =N a a ) $.
        $}

        ${
                _eq-2a $f N a $.
                _eq-2b $f N b $.
                _eq-2c $e ( =N a b ) $.
                eqNsymm $a ( =N b a ) $.
        $}

        ${
                _eq-3a $f N a $.
                _eq-3b $f N b $.
                _eq-3c $f N c $.
                _eq-3d $e ( =N a b ) $.
                _eq-3e $e ( =N b c ) $.
                eqNtrans $a ( =N a c ) $.
        $}

        ${
                _eq-4a $f N a $.
                _eq-4b $f N b $.
                _eq-4c $f N a' $.
                _eq-4d $e ( =N a a' ) $.
                _eq-4e $e ( =N a b ) $.
                eqNleibnizL $p ( =N a' b ) $=
                  _eq-4c _eq-4a _eq-4b _eq-4a _eq-4c _eq-4d eqNsymm _eq-4e eqNtrans
                $.
        $}


        ${
                _eq-5a $f N a $.
                _eq-5b $f N b $.
                _eq-5c $f N b' $.
                _eq-5d $e ( =N b b' ) $.
                _eq-5e $e ( =N a b ) $.
                eqNleibnizR $p ( =N a b' ) $=
                  _eq-5a _eq-5b _eq-5c _eq-5e _eq-5d eqNtrans
                $.
        $}

$}
