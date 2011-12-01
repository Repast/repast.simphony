package repast.simphony.parameter.groovy

import repast.simphony.parameter.*
import repast.simphony.parameter.xml.ListSetterCreator
import repast.simphony.parameter.xml.NumberSetterCreator;

class ParameterBuilder extends BuilderSupport {

  ParameterTreeSweeper sweeper = new ParameterTreeSweeper();
	Map setterMap = [:]
	ParametersCreator creator = new ParametersCreator();
	private boolean first = true;

  Map methodMap
  Map listTypeMap
  ParameterSetter lastSetter;


  ParameterBuilder() {
    methodMap = [
      'constant' : {Map attributes -> createConstant(attributes)},
      'sweep' : {Map attributes -> createRoot(attributes)},
      'number' : {Map attributes -> createNumber(attributes)},
      'list' : {Map attributes -> createList(attributes)},
      'custom' : {Map attributes -> createCustom(attributes)}
    ]

    listTypeMap = [(String.class) : 'string', (Boolean.class) : 'boolean',
      (Integer.class) : 'int', (BigDecimal.class) : 'double', (Float.class) : 'float',
      (Long.class) : 'long']
  }

  def createNode(name) {
    throw new ParameterFormatException("Invalid Parameter format for ${name}")
  }

  def createNode(name, value) {
    throw new ParameterFormatException("Invalid Parameter format for ${name}")
  }

  def createNode(name, Map attribute, value) {
    throw new ParameterFormatException("Invalid Parameter format for ${name}")
  }

  void setParent(parent, child) {}

  void nodeCompleted(parent, node) {}

  def createNode(name, Map attributes) {
    Closure closure = methodMap[name]
    if (closure != null) closure(attributes)
    else throw new ParameterFormatException("${name} is not a valid parameter type");
    first = false
    return lastSetter
  }

  def createRootParamException() {
    return new ParameterFormatException("'sweep' parameter must be the first parameter")
  }

  def createList(Map attributes) {
    if (first) throw createRootParamException()
    String name = attributes.get('name')
    if (name == null) throw new ParameterFormatException("Parameter must contain the 'name' attribute")
    def values = attributes['values']
    if (values != null && values instanceof List && values.size() > 0) {
      def value = values[0]
      String valueType = listTypeMap[value.class]
      if (valueType == null) {
        throw new ParameterFormatException("'list' parameter can only contains strings, booleans and numbers")
      } else {
        def str = ''
        if (valueType == 'string') {
          values.each({
            if (it[0] != '\'') {
              it = ('\'' << it << '\'')
            }

            str = str << it << ' '
          })

          str = str[0..-2]
          attributes['values'] = str
        } else {
          attributes['values'] = values.join(' ')
        }

        attributes['value_type'] = valueType

        def creator = new ListSetterCreator()
        creator.init(attributes)
        lastSetter = creator.createSetter()
        creator.addParameter(this.creator)
        sweeper.add(getCurrent(), lastSetter)
        setterMap[creator.name] = lastSetter
      }
    } else {
      throw new ParameterFormatException("'list' parameter is missing list-type values attribute")
    }

  }

  def createNumber(Map attributes) {
    if (attributes.keySet().contains('step') && attributes.keySet().contains('end') &&
      attributes.keySet().contains('start') && attributes.keySet().contains('name'))
    {
      if (first) throw createRootParamException()
      // convert to strings so we can use the NumberSetterCreator
      attributes.each(
      {entry ->
        Class c = entry.value.class
        entry.value = entry.value.toString()
        if (c == Float) {
          entry.value = entry.value + "f"
        } else if (c == Long) {
          entry.value = entry.value + "L"
        }
      })
      def creator = new NumberSetterCreator()
      creator.init(attributes)
      lastSetter = creator.createSetter()
      creator.addParameter(this.creator)
      sweeper.add(getCurrent(), lastSetter)
      setterMap[creator.name] = lastSetter
    } else {
      throw new ParameterFormatException("'number' parameter must contain start, end and step attributes")
    }
  }


  def createConstant(Map attributes) {
    if (first) throw createRootParamException()
    String name = attributes.get('name')
    if (name == null) throw new ParameterFormatException("Parameter must contain the 'name' attribute")
    if (getCurrent() != sweeper.rootParameterSetter)
      throw new ParameterFormatException("Constant parameter '${name}' can only be children of the sweep parameter")

    def value = attributes['value']
    if (value != null) {
      def valueClass = value.class
      if (value.class == BigDecimal.class) {
        lastSetter = new ConstantSetter(name, value.doubleValue());
        valueClass = Double.class
        value = value.doubleValue()
      } else {
        lastSetter = new ConstantSetter(name, value)
      }

		  creator.addParameter(name, valueClass, value, false);
		  sweeper.addToRoot(lastSetter)
		  setterMap[(name)] = lastSetter
		}
  }

  def createRoot(Map attributes) {
    if (!first)
      throw new ParameterFormatException("'sweep' parameter must be the first parameter")
    def runs = attributes['runs']
    if (runs != null && runs.class == Integer.class) {
      sweeper.runCount = runs
      lastSetter = sweeper.rootParameterSetter
      setterMap['root'] = lastSetter
    } else {
      throw new ParameterFormatException("'sweep' parameter is missing an integer 'runs' attribute")
    }
  }

  def createCustom(Map attributes) {
     if (first) throw createRootParamException()
     def setter = attributes['setter']
     if (setter != null && setter instanceof CustomParameterSetter) {
      lastSetter = setter
      sweeper.add(getCurrent(), lastSetter)
      setter.addParameters(creator)
      setterMap[(setter.name)] = setter
     } else {
      throw new ParameterFormatException("'custom' parameter is missing the CustomParameterSetter 'setter' attribute");
     }
  }

  Parameters getParameters() {
    return creator.createParameters()
  }
}