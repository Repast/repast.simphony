import repast.simphony.parameter.groovy.*
import repast.simphony.parameter.*

builder.sweep(runs : 2) {
  constant(name : 'double_const', value: 4.0)
  constant(name : 'long_const', value : 11L)
  constant(name : 'string_const', value : "hello cormac")
  constant(name : 'boolean_const', value : false)

  number(name : 'long_param', start : 1L, end : 3, step : 1) {
    number(name : 'float_param', start : 0.8f, end : 1.0f, step : 0.1f) {
      custom(setter : new StringListSetter()) {
        list(name : 'randomSeed', values : [1, 2])
      }
    }
  }
}

class StringListSetter extends CustomParameterSetter {

  def list = ['foo', 'bar']
  int count = 0

  public StringListSetter() {
    super('string_param', String.class)
  }

  void reset(Parameters params) {
    count = 0
    params.setValue(name, list[count])
    count++
  }

  boolean atEnd() {
    return count == 2
  }

  void next(Parameters params) {
    params.setValue(name, list[count])
    count++
    println "RunState Batch Number: ${runState.runInfo.batchNumber}"
    println "RunState Run Count: ${runState.runInfo.runNumber}"
  }

  void addParameters(ParametersCreator creator) {
    // String name, Class type, Object value, boolean isReadOnly
    creator.addParameter(name, parameterType, list[count], false)
  }
}