package repast.simphony.parameter.groovy

import groovy.util.GroovyTestCase;
import repast.simphony.parameter.*
import repast.simphony.parameter.xml.*
import repast.simphony.parameter.groovy.CustomParameterSetter

class ParameterBuilderTest extends GroovyTestCase {

  ParameterSweeper sweeper
  Parameters params

  void setUp() {

  }



  void testBadFormat() {

    // unknown parameter type
    try {
      def builder = new ParameterBuilder()
      builder.foo(runs : 3)
      fail()
    } catch (ParameterFormatException ex) {
      assertTrue(true);
    }

    // missing name parameter
    try {
      def builder = new ParameterBuilder()
      builder.sweep(runs : 2) {
        constant(value : 'foo')
      }
      fail()
    } catch (ParameterFormatException ex) {
      assertTrue(true);
    }

    // root not first
    try {
      def builder = new ParameterBuilder()
      builder.number(name : 'foo', start : 1, end : 2, step: 1)
      fail()
    } catch (ParameterFormatException ex) {
      assertTrue(true);
    }

    // constant not on root
    try {
      def builder = new ParameterBuilder()
      builder.sweep(runs : 1) {
        number(name : 'foo', start : 1, end : 2, step: 1) {
          constant(name : 'const', value : 'foo')
        }
      }
      fail()
    } catch (ParameterFormatException ex) {
      assertTrue(true);
    }

    // bad runs attribute
    try {
      def builder = new ParameterBuilder()
      builder.sweep(runs : 1.5)
      fail()
    } catch (ParameterFormatException ex) {
      assertTrue(true);
    }

    // bad list element type
    try {
      def builder = new ParameterBuilder()
      builder.sweep(runs : 1) {
        list(name : 'foo', values : [new ArrayList()]) 
      }
      fail()
    } catch (ParameterFormatException ex) {
      assertTrue(true);
    }

    // missing attributes
    try {
      def builder = new ParameterBuilder()
      builder.sweep(runs : 1) {
        number(name : 'foo', end : 2, step: 1) {
          constant(name : 'const', value : 'foo')
        }
      }
      fail()
    } catch (ParameterFormatException ex) {
      assertTrue(true);
    }

    // number missing name attribute
    try {
      def builder = new ParameterBuilder()
      builder.sweep(runs : 1) {
        number(start : 1, end : 2, step: 1)
      }
      fail()
    } catch (ParameterFormatException ex) {
      assertTrue(true);
    }
  }

  void testConstant() {
    def builder = new ParameterBuilder()
    builder.sweep(runs : 3) {
      constant(name : '1', value : true)
      constant(name : '2', value : 3.14f)
      constant(name : '3', value : 'foo')
    }

    def params = builder.parameters
    assertTrue(params.getValue('1'))
    assertEquals(3.14f, params.getValue('2'))
    assertEquals('foo', params.getValue('3'))
  }

  void testNumber() {
    def builder = new ParameterBuilder()
    builder.sweep(runs : 3) {
      number(name : 'number_1', start: 3.2, end: 4.5, step: 2.4) {
        number(name: 'number_2', start: 1, end: 3, step: 0.5f)
      }
      number(name: 'number_3', start: 10, end: 5, step: -1)
    }

    def params = builder.parameters
    assertEquals(new Double(3.2), params.getValue('number_1'))
    assertEquals(new Float(1f), params.getValue('number_2'))
    assertEquals(new Integer(10), params.getValue('number_3'))
  }

  void testList() {
     def builder = new ParameterBuilder()
    builder.sweep(runs : 3) {
      list(name: 'list_1', values : [3.2f, 3, 5])
      list(name: 'list_2', values : [3.2, 3, 5])
      list(name: 'list_3', values : [true, false])
      list(name: 'list_4', values : ['hello bog', 'bye'])
    }

    def params = builder.parameters
    assertEquals(new Float(3.2), params.getValue('list_1'))
    assertEquals(new Double(3.2), params.getValue('list_2'))
    assertEquals(Boolean.TRUE, params.getValue('list_3'))
    assertEquals('hello bog', params.getValue('list_4'))
  }

  void testTree() {
    def builder = new ParameterBuilder()
    builder.sweep(runs: 2) {
      constant(name: 'const_1', value : 0.3f)
      constant(name: 'const_2', value : 0.2)
      constant(name: 'const_3', value : 11)
      constant(name: 'const_4', value : 11L)
      constant(name: 'const_5', value : 'hello cormac')
      constant(name: 'const_6', value : false)

      number(name: 'num_1', start : 1L, end : 4, step : 1) {
        number(name: 'num_2', start : 0.3f, end : 1f, step : 0.1f) {
          list(name: 'list_val', values: ['foo', 'bar', 'baz'])
        }
        number(name : 'num_3', start: 0.9, end : 1.0, step : 0.1)
        number(name : 'num_4', start: 2, end : 3, step : 1)
      }
    }


    try {
      ParameterTreeSweeper sweeper = builder.sweeper
			assertEquals(2, sweeper.getRunCount());
			ParameterSetter root = sweeper.getRootParameterSetter();
			Collection children = sweeper.getChildren(root);
			assertEquals(7, children.size());

			Set names = new HashSet();
			LongSteppedSetter iSetter = null;
			for (setter in children) {
				 if (setter instanceof LongSteppedSetter) {
					 iSetter = (LongSteppedSetter)setter;
					 names.add(((LongSteppedSetter)setter).getParameterName());
				 }
				 if (setter instanceof ConstantSetter) names.add(((ConstantSetter)setter).getParameterName());
			}
			assertTrue(names.contains("num_1"));
			assertTrue(names.contains("const_1"));
			assertTrue(names.contains("const_2"));
			assertTrue(names.contains("const_3"));
			assertTrue(names.contains("const_4"));
			assertTrue(names.contains("const_5"));
			assertTrue(names.contains("const_6"));

			assertEquals("num_1", iSetter.getParameterName());

			children = sweeper.getChildren(iSetter);
			assertEquals(3, children.size());
			Set types = new HashSet();
			types.add(DoubleSteppedSetter.class);
			types.add(FloatSteppedSetter.class);
			types.add(IntSteppedSetter.class);
			Map map = new HashMap();
			for (ParameterSetter ps in children) {
				assertTrue(types.remove(ps.getClass()));
				if (ps instanceof DoubleSteppedSetter) map.put(((DoubleSteppedSetter)ps).getParameterName(), ps);
				if (ps instanceof FloatSteppedSetter) map.put(((FloatSteppedSetter)ps).getParameterName(), ps);
				if (ps instanceof IntSteppedSetter) map.put(((IntSteppedSetter)ps).getParameterName(), ps);
			}
			assertEquals(0, types.size());
			assertTrue(map.containsKey("num_2"));
			assertTrue(map.containsKey("num_3"));
			assertTrue(map.containsKey("num_4"));

			children = sweeper.getChildren(map.get("num_2"));
			assertEquals(1, children.size());
			ListParameterSetter lSetter = (ListParameterSetter) children.iterator().next();
			assertEquals("list_val", lSetter.getParameterName());
			children = sweeper.getChildren(lSetter);
			assertEquals(0, children.size());

			children = sweeper.getChildren(map.get("num_3"));
			assertEquals(0, children.size());
			children = sweeper.getChildren(map.get("num_4"));
			assertEquals(0, children.size());

			Parameters params = builder.parameters
			names = new HashSet();
			names.add("num_1");
			names.add("num_2");
			names.add("num_3");
			names.add("num_4");
			names.add("list_val");
			names.add("const_1");
			names.add("const_2");
			names.add("const_3");
			names.add("const_4");
			names.add("const_5");
			names.add("const_6");
			for (String name in params.getSchema().parameterNames()) {
				assertTrue(names.remove(name));
			}

			assertEquals(new Long(1), params.getValue("num_1"));
			assertEquals(new Float(0.3), params.getValue("num_2"));
			assertEquals(new Double(0.9), params.getValue("num_3"));
			assertEquals(new Integer(2), params.getValue("num_4"));
			assertEquals(new Float(0.3f), params.getValue("const_1"));
			assertEquals(new Double(0.2), params.getValue("const_2"));
			assertEquals(new Integer(11), params.getValue("const_3"));
			assertEquals(new Long(11), params.getValue("const_4"));
			assertEquals("hello cormac", params.getValue("const_5"));
			assertEquals(Boolean.FALSE, params.getValue("const_6"));

		} catch (Exception ex) {
			ex.printStackTrace();
			assertTrue(false);
		}
  }

  private void incrementSweeper() {
    sweeper.next(params);
    //println(params.getValue("name") + ", " + params.getValue("intRange") + ", " + params.getValue("doubleRange") + ", "
    //        + params.getValue("doubleList"));
  }

  private void helperTestSweep(expected) {
     expected.each(
     {
        assertTrue("Sweeper has no more steps.", !sweeper.atEnd());
        incrementSweeper();
        assertEquals(it[0], params.getValue("name"));
        assertEquals(it[1], params.getValue("intRange"));
        assertEquals((Double)it[2], (Double)params.getValue("doubleRange"), 0.00001);
        assertEquals(new Double(it[3]), params.getValue("doubleList"));
      })

      if (!sweeper.atEnd()) {
          System.out.println("Failure: sweeper steps remain:");
          while (!sweeper.atEnd()) {
              incrementSweeper();
          }
          fail("Sweeper steps remain. See above.");
      }
      assertFalse(!sweeper.atEnd());
  }


      /*
       * Test method for 'repast.parameter.sweep.DefaultParameter!sweeper.atEnd()'
       */
      public void testExpectedOutputChain() {
      /*
        ParameterSetter nameInit = new ConstantSetter<String>("name", "nameValue");
        ParameterSetter intRangeInit = new IntSteppedSetter("intRange", 1, 3, 1);
        ParameterSetter doubleRangeInit = new DoubleSteppedSetter("doubleRange", 1.0 / 3.0, 1.0, 1.0 / 3.0);
        ParameterSetter doubleListInit = new ListParameterSetter<Double>("doubleList", new Double[]{100.0, 200.0});
        sweeper.add(sweeper.getRootParameterSetter(), nameInit);
        sweeper.add(nameInit, intRangeInit);
        sweeper.add(intRangeInit, doubleRangeInit);
        sweeper.add(doubleRangeInit, doubleListInit);
        */

        def builder = new ParameterBuilder()
        builder.sweep(runs : 1) {
          constant(name : 'name', value : 'nameValue') {
            number(name : 'intRange', start : 1, end : 3, step : 1) {
             number(name : 'doubleRange', start: 1.0 / 3.0, end : 1.0, step : 1.0 / 3.0) {
              list(name : 'doubleList', values : [100.0, 200.0])
             }
            }
          }
        }

        sweeper = builder.sweeper
        params = builder.parameters

         def expectedValuesChain = [
                  [ "nameValue", 1, 1.0 / 3.0, 100.0],
                  [ "nameValue", 1, 1.0 / 3.0, 200.0],
                  [ "nameValue", 1, 2.0 / 3.0, 100.0],
                  [ "nameValue", 1, 2.0 / 3.0, 200.0],
                  [ "nameValue", 1, 1.0, 100.0],
                  [ "nameValue", 1, 1.0, 200.0],
                  [ "nameValue", 2, 1.0 / 3.0, 100.0],
                  [ "nameValue", 2, 1.0 / 3.0, 200.0],
                  [ "nameValue", 2, 2.0 / 3.0, 100.0],
                  [ "nameValue", 2, 2.0 / 3.0, 200.0],
                  [ "nameValue", 2, 1.0, 100.0],
                  [ "nameValue", 2, 1.0, 200.0],
                  [ "nameValue", 3, 1.0 / 3.0, 100.0],
                  [ "nameValue", 3, 1.0 / 3.0, 200.0],
                  [ "nameValue", 3, 2.0 / 3.0, 100.0],
                  [ "nameValue", 3, 2.0 / 3.0, 200.0],
                  [ "nameValue", 3, 1.0, 100.0],
                  [ "nameValue", 3, 1.0, 200.0]
          ]

          helperTestSweep(expectedValuesChain);
      }


      /*
       * Test method for 'repast.parameter.sweep.DefaultParameter!sweeper.atEnd()'
       */
  public void testExpectedOutputRuns() {
      /*
    params.setValue("name", "nameValue");
    params.setValue("intRange", 1);
    params.setValue("doubleRange", .5);
    params.setValue("doubleList", 100.0);
    */

    def builder = new ParameterBuilder()
    builder.sweep(runs : 1) {
      constant(name : 'name', value : 'nameValue')
      constant(name : 'doubleRange', value : 0.5)
      constant(name : 'doubleList', value : 100.0) {
        number(name : 'intRange', start : 1, end : 3, step : 1) {
      }
    }

    sweeper = builder.sweeper
    params = builder.parameters

    def expectedValuesChain =  [
            [ "nameValue", 1,  0.5, 100.0],
            [ "nameValue", 2,  0.5, 100.0],
            [ "nameValue", 3,  0.5, 100.0],
    ]
    helperTestSweep(expectedValuesChain)

    sweeper.reset(params)
    sweeper.setRunCount(2)
    expectedValuesChain = [
            [ "nameValue", 1,  0.5, 100.0],
            [ "nameValue", 2,  0.5, 100.0],
            [ "nameValue", 3,  0.5, 100.0],
            [ "nameValue", 1,  0.5, 100.0],
            [ "nameValue", 2,  0.5, 100.0],
            [ "nameValue", 3,  0.5, 100.0]
    ];
    helperTestSweep(expectedValuesChain)
   }
  }

  public void testCustom() {
    def builder = new ParameterBuilder()
    builder.sweep(runs : 1) {
      custom(setter : new SinSetter("sinValue", Double.class))
    }

    def params = builder.parameters
    assertEquals(new Double(Math.sin(1)), params.getValue('sinValue'))
    builder.sweeper.next(params)
    assertEquals(new Double(Math.sin(1)), params.getValue('sinValue'))
    builder.sweeper.next(params)
    assertEquals(new Double(Math.sin(2)), params.getValue('sinValue'))
    builder.sweeper.next(params)
    assertEquals(new Double(Math.sin(3)), params.getValue('sinValue'))
    assertEquals(Boolean.TRUE, builder.sweeper.getChildren(builder.sweeper.rootParameterSetter)[0].atEnd())
  }
}

class SinSetter extends CustomParameterSetter {

  int count = 1;

  public SinSetter(String name, Class type) {
    super(name, type)
  }

  // reset is called on the first sweeper.next()
  void reset(Parameters params) {
    count = 1
    params.setValue(name, Math.sin(count))
  }

  boolean atEnd() {
    print count
    return count == 3
  }

  void next(Parameters params) {
    count++
    params.setValue(name, Math.sin(count))
  }

  void addParameters(ParametersCreator creator) {
    // String name, Class type, Object value, boolean isReadOnly
    creator.addParameter(name, parameterType, Math.sin(1), false)
  }
}