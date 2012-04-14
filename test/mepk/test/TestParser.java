package mepk.test;

import static mepk.Expression.*;
import static org.junit.Assert.*;

import java.io.IOException;

import mepk.Expression;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Test;

public class TestParser {
	@Test
	public void test1() throws JsonParseException, JsonMappingException, IOException {
		assertEquals(Var("v"), Expression.FromJSON("\"v\""));
		assertEquals(Var("variable"), Expression.FromJSON("\"variable\""));
		assertEquals(App("c"), Expression.FromJSON("[\"c\"]"));
		assertEquals(AppV("->", "P", "P"), Expression.FromJSON("[\"->\", \"P\", \"P\"]"));
		assertEquals(App("->", Var("P"), App("false")), Expression.FromJSON("[\"->\", \"P\", [\"false\"]]"));
	}
}
