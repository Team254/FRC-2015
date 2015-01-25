package com.team254.lib;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import com.team254.lib.util.ConstantsBase;
import com.team254.lib.util.ConstantsBase.Constant;

public class ConstantsBaseTest {

	public static class ConstantsTest extends ConstantsBase {
		public static int thingA = 5;
		public static double thingB = 5.123;
		public static Double thingC = new Double(12.4);

		@Override
		public String getFileLocation() {
			return "~/constants_test.txt";
		}
	}

	@Test
	public void testKeyExtraction() {
		Collection<Constant> constants = (new ConstantsTest()).getConstants();
		Constant[] expected = new Constant[] {
				new Constant("thingA", int.class, 5),
				new Constant("thingB", double.class, 5.123),
				new Constant("thingC", Double.class, 12.4) };
		assertArrayEquals("Key extraction broken", expected,
				constants.toArray(new Object[0]));
	}

	@Test
	public void testCanSetField() {
		int old = ConstantsTest.thingA;
		boolean set = new ConstantsTest().setConstant("thingA", 8);
		assertEquals(ConstantsTest.thingA, 8);
		assertTrue(set);
		set = new ConstantsTest().setConstant("thingA", old);
		assertEquals(ConstantsTest.thingA, old);
		assertTrue(set);
	}

	@Test
	public void testWrongTypeFails() {
		int old = ConstantsTest.thingA;
		boolean set = new ConstantsTest().setConstant("thingA", 8.6);
		assertFalse(set);
		assertEquals(ConstantsTest.thingA, 5);
		set = new ConstantsTest().setConstant("thingA", old);
		assertEquals(ConstantsTest.thingA, old);
		assertTrue(set);
	}

	@Test
	public void testReadFromFile() {
		int old = ConstantsTest.thingA;
		JSONObject j = new JSONObject();
		j.put("thingA", 123);
		File f = new ConstantsTest().getFile();
		try {
			FileWriter w = new FileWriter(f);
			w.write(j.toJSONString());
			w.close();

		} catch (IOException e) {
			assertTrue("couldn't write file", false);
		}
		new ConstantsTest().loadFromFile();

		assertEquals(ConstantsTest.thingA, 123);

		new ConstantsTest().setConstant("thingA", old);
	}

	@Test
	public void testWriteToFile() {
		ConstantsTest t = new ConstantsTest();
		int oldA = ConstantsTest.thingA;
		double oldB = ConstantsTest.thingB;
		t.setConstant("thingB", 987.76);
		t.setConstant("thingA", 1234);

		t.saveToFile();

		try {
			JSONObject j = t.getJSONObjectFromFile();
			assertEquals(j.get("thingB"), 987.76);
			assertEquals(j.get("thingA"), 1234L);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
			assertTrue("couldn't get file jsonobject", false);
		}

		t.setConstant("thingA", oldA);
		t.setConstant("thingB", oldB);

	}

}
