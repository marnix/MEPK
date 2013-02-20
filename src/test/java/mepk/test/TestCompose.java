package mepk.test;

import static mepk.kernel.Expression.*;
import static mepk.kernel.Statement.*;

import java.util.Arrays;

import mepk.kernel.Expression;
import mepk.kernel.ProofStep;
import mepk.kernel.Statement;

import org.junit.Test;

public class TestCompose {

	@Test
	public void test() {
		Statement mp1 = Stat(Arrays.asList(Type("P", "bool"), Type("Q", "bool"), Var("P"), Expression.AppV("=>", "P","Q")), Var("Q"));
		Statement mp2 = Stat(Arrays.asList(Type("Q", "bool"), Type("R", "bool"), Var("Q"), Expression.AppV("=>", "Q","R")), Var("R"));
		ProofStep.Compose(mp2, mp1);
		// TODO: Enhance test
	}

}
