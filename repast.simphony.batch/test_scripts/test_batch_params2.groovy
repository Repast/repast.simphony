builder.sweep(runs : 2) {
  constant(name : 'double_const', value: 4.0)
  constant(name : 'long_const', value : 11L)
  constant(name : 'string_const', value : "hello cormac")
  constant(name : 'boolean_const', value : false)

  number(name : 'long_param', start : 1L, end : 3, step : 1) {
    number(name : 'float_param', start : 0.8f, end : 1.0f, step : 0.1f) {
      list(name : 'string_param', values : ['foo', 'bar']) {
        list(name : 'randomSeed', values : [1, 2])
      }
    }
  }
}