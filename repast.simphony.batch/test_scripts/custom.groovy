import repast.simphony.parameter.groovy.CustomParameterSetter
import repast.simphony.parameter.*

builder.sweep(runs : 3) {
  custom(setter : new TestSetter("num_1", Integer.class))
}

class TestSetter extends CustomParameterSetter {

  public TestSetter(String name, Class type) {
    super(name, type)
  }

  void reset(Parameters params) {
    params.setValue(name, 2)so
  }

  boolean atEnd() {
    return count == 4
  }

  void next(Parameters params) {
    params.setValue(name, 2)
  }

  void addParameters(ParametersCreator creator) {
    // String name, Class type, Object value, boolean isReadOnly
    creator.addParameter(name, parameterType, new Integer(2), false)
  }
}

