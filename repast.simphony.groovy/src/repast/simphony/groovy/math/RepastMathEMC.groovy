package repast.simphony.groovy.math

import javax.measure.unit.*
import org.jscience.physics.amount.*
import org.jscience.mathematics.number.*
import org.jscience.mathematics.vector.*

class RepastMathEMC {
	
	private static boolean unitsInitialized = false
	private static boolean matrixInitialized = false
	private static boolean calculusInitialized = false
	
	public static boolean isUnitsInitialized(){
		return unitsInitialized
	}
	
	public static boolean isMatrixInitialized(){
		return matrixInitialized
	}
	
	public static boolean isCalculusInitialized(){
		return calculusInitialized
	}
	
	public static boolean isAllInitialized(){
		return (isUnitsInitialized() && isMatrixInitialized() && isCalculusInitialized())
	}
	
	public static void initAll(){
		initUnits()
		initMatrix()
		initCalculus()
	}
	/**
	 * These static methods are called if the user wants to have the Units, Matrix, and Calculus
	 * DSL capabilities, respectively.
	 * e.g. def x = 2.3.meters + 1.5.kilometers
	 */
	public static void initUnits(){
		
		/**
		 * Units operations
		 */
		if (!isUnitsInitialized()){
		
			// Define the pure number creator.
			java.math.BigDecimal.metaClass.getPure = { ->
				Amount x = Amount.valueOf(delegate, Unit.ONE)
		        return x
		    }
			
			// Define the pure number creator.
			java.math.BigDecimal.metaClass.getPureNumber = { ->
				Amount x = Amount.valueOf(delegate, Unit.ONE)
		        return x
		    }
			
			// Define the deci converter.
			Amount.metaClass.getDeci = { ->
				Amount a = delegate.to(SI.DECI(delegate.getUnit()))
				a.times(Amount.valueOf(10 ** -1, Unit.ONE))
		        return a
		    }
			
			// Define the deka converter.
			Amount.metaClass.getDeka = { ->
				Amount a = delegate.to(SI.DEKA(delegate.getUnit()))
				a.times(Amount.valueOf(10 ** 1, Unit.ONE))
		        return a
		    }
			
			// Define the centi converter.
			Amount.metaClass.getCenti = { ->
				Amount a = delegate.to(SI.CENTI(delegate.getUnit()))
				a.times(Amount.valueOf(10 ** -2, Unit.ONE))
		        return a
		    }
			
			// Define the hecto converter.
			Amount.metaClass.getHecto = { ->
				Amount a = delegate.to(SI.HECTO(delegate.getUnit()))
				a.times(Amount.valueOf(10 ** 2, Unit.ONE))
		        return a
		    }
			
			// Define the milli converter.
			Amount.metaClass.getMilli = { ->
				Amount a = delegate.to(SI.MILLI(delegate.getUnit()))
				a.times(Amount.valueOf(10 ** -3, Unit.ONE))
		        return a
		    }
			
			// Define the kilo converter.
			Amount.metaClass.getKilo = { ->
				Amount a = delegate.to(SI.KILO(delegate.getUnit()))
				a.times(Amount.valueOf(10 ** 3, Unit.ONE))
		        return a
		    }
			
			// Define the micro converter.
			Amount.metaClass.getMicro = { ->
				Amount a = delegate.to(SI.MICRO(delegate.getUnit()))
				a.times(Amount.valueOf(10 ** -6, Unit.ONE))
		        return a
		    }
			
			// Define the mega converter.
			Amount.metaClass.getMega = { ->
				Amount a = delegate.to(SI.MEGA(delegate.getUnit()))
				a.times(Amount.valueOf(10 ** 6, Unit.ONE))
		        return a
		    }
			
			// Define the nano converter.
			Amount.metaClass.getNano = { ->
				Amount a = delegate.to(SI.NANO(delegate.getUnit()))
				a.times(Amount.valueOf(10 ** -9, Unit.ONE))
		        return a
		    }
			
			// Define the giga converter.
			Amount.metaClass.getGiga = { ->
				Amount a = delegate.to(SI.GIGA(delegate.getUnit()))
				a.times(Amount.valueOf(10 ** 9, Unit.ONE))
		        return a
		    }
			
			// Define the pico converter.
			Amount.metaClass.getPico = { ->
				Amount a = delegate.to(SI.PICO(delegate.getUnit()))
				a.times(Amount.valueOf(10 ** -12, Unit.ONE))
		        return a
		    }
			
			// Define the tera converter.
			Amount.metaClass.getTera = { ->
				Amount a = delegate.to(SI.TERA(delegate.getUnit()))
				a.times(Amount.valueOf(10 ** 12, Unit.ONE))
		        return a
		    }
			
			// Define the femto converter.
			Amount.metaClass.getFemto = { ->
				Amount a = delegate.to(SI.FEMTO(delegate.getUnit()))
				a.times(Amount.valueOf(10 ** -15, Unit.ONE))
		        return a
		    }
			
			// Define the peta converter.
			Amount.metaClass.getPeta = { ->
				Amount a = delegate.to(SI.PETA(delegate.getUnit()))
				a.times(Amount.valueOf(10 ** 15, Unit.ONE))
		        return a
		    }
			
			// Define the atto converter.
			Amount.metaClass.getAtto = { ->
				Amount a = delegate.to(SI.ATTO(delegate.getUnit()))
				a.times(Amount.valueOf(10 ** -18, Unit.ONE))
		        return a
		    }
			
			// Define the exa converter.
			Amount.metaClass.getExa = { ->
				Amount a = delegate.to(SI.EXA(delegate.getUnit()))
				a.times(Amount.valueOf(10 ** 18, Unit.ONE))
		        return a
		    }
			
			// Define the zepto converter.
			Amount.metaClass.getZepto = { ->
				Amount a = delegate.to(SI.ZEPTO(delegate.getUnit()))
				a.times(Amount.valueOf(10 ** -21, Unit.ONE))
		        return a
		    }
			
			// Define the zetta converter.
			Amount.metaClass.getZetta = { ->
				Amount a = delegate.to(SI.ZETTA(delegate.getUnit()))
				a.times(Amount.valueOf(10 ** 21, Unit.ONE))
		        return a
		    }
			
			// Define the yocto converter.
			Amount.metaClass.getYocto = { ->
				Amount a = delegate.to(SI.YOCTO(delegate.getUnit()))
				a.times(Amount.valueOf(10 ** -24, Unit.ONE))
		        return a
		    }
			
			// Define the yotta converter.
			Amount.metaClass.getYotta = { ->
				Amount a = delegate.to(SI.YOTTA(delegate.getUnit()))
				a.times(Amount.valueOf(10 ** 24, Unit.ONE))
		        return a
		    }
			
			// Define the meter creator.
			java.math.BigDecimal.metaClass.getMeters = { ->
				Amount x = Amount.valueOf(delegate, SI.METER)
		        return x
		    }
			
			// Define the kilometers creator.
			java.math.BigDecimal.metaClass.getKilometers = { ->
				Amount x = Amount.valueOf(delegate, SI.KILO(SI.METER))
		        return x
		    }
			
			// Define the centimeters creator.
			java.math.BigDecimal.metaClass.getCentimeters = { ->
				Amount x = Amount.valueOf(delegate, SI.CENTI(SI.METER))
		        return x
		    }
			
			// Define the millimeters creator.
			java.math.BigDecimal.metaClass.getMillimeters = { ->
				Amount x = Amount.valueOf(delegate, SI.MILLI(SI.METER))
		        return x
		    }
			
			// Define the micrometers creator.
			java.math.BigDecimal.metaClass.getMicrometers = { ->
				Amount x = Amount.valueOf(delegate, SI.MICRO(SI.METER))
		        return x
		    }
			
			// Define the decimeters creator.
			java.math.BigDecimal.metaClass.getDecimeters = { ->
				Amount x = Amount.valueOf(delegate, SI.DECI(SI.METER))
		        return x
		    }
			
			// Define the kilograms creator.
			java.math.BigDecimal.metaClass.getKilograms = { ->
				Amount x = Amount.valueOf(delegate, SI.KILOGRAM)
		        return x
		    }
			
			// Define the milligrams creator.
			java.math.BigDecimal.metaClass.getMilligrams = { ->
				Amount x = Amount.valueOf(delegate, SI.MILLI(SI.GRAM))
		        return x
		    }
			
			// Define the angstroms creator.
			java.math.BigDecimal.metaClass.getAngstroms = { ->
				Amount x = Amount.valueOf(delegate, NonSI.ANGSTROM)
		        return x
			}
			
			// Define the pounds creator.
			java.math.BigDecimal.metaClass.getPounds = { ->
				Amount x = Amount.valueOf(delegate, NonSI.POUND)
		        return x
		    }
			
			// Define the grams creator.
			java.math.BigDecimal.metaClass.getGrams = { ->
				Amount x = Amount.valueOf(delegate, SI.GRAM)
		        return x
		    }
			
			// Define the amperes creator.
			java.math.BigDecimal.metaClass.getAmperes = { ->
				Amount x = Amount.valueOf(delegate, SI.AMPERE)
		        return x
		    }
			
			// Define the becquerels creator.
			java.math.BigDecimal.metaClass.getBecquerels = { ->
				Amount x = Amount.valueOf(delegate, SI.BECQUEREL)
		        return x
		    }
			
			// Define the kilobecquerels creator.
			java.math.BigDecimal.metaClass.getKilobecquerels = { ->
				Amount x = Amount.valueOf(delegate, SI.KILO(SI.BECQUEREL))
		        return x
		    }
			
			// Define the megabecquerels creator.
			java.math.BigDecimal.metaClass.getMegabecquerels = { ->
				Amount x = Amount.valueOf(delegate, SI.MEGA(SI.BECQUEREL))
		        return x
		    }
			
			// Define the gigabecquerels creator.
			java.math.BigDecimal.metaClass.getGigabecquerels = { ->
				Amount x = Amount.valueOf(delegate, SI.GIGA(SI.BECQUEREL))
		        return x
		    }
			
			// Define the bits creator.
			java.math.BigDecimal.metaClass.getBits = { ->
				Amount x = Amount.valueOf(delegate, SI.BIT)
		        return x
		    }
			
			// Define the kilobits creator.
			java.math.BigDecimal.metaClass.getKilobits = { ->
				Amount x = Amount.valueOf(delegate, SI.KILO(SI.BIT))
		        return x
		    }
			
			// Define the megabits creator.
			java.math.BigDecimal.metaClass.getMegabits = { ->
				Amount x = Amount.valueOf(delegate, SI.MEGA(SI.BIT))
		        return x
		    }
			
			// Define the gigabits creator.
			java.math.BigDecimal.metaClass.getGigabits = { ->
				Amount x = Amount.valueOf(delegate, SI.GIGA(SI.BIT))
		        return x
		    }
			
			// Define the terabits creator.
			java.math.BigDecimal.metaClass.getTerabits = { ->
				Amount x = Amount.valueOf(delegate, SI.TERA(SI.BIT))
		        return x
		    }
			
			// Define the candelas creator.
			java.math.BigDecimal.metaClass.getCandelas = { ->
				Amount x = Amount.valueOf(delegate, SI.CANDELA)
		        return x
		    }
			
			// Define the celsius creator.
			java.math.BigDecimal.metaClass.getCelsius = { ->
				Amount x = Amount.valueOf(delegate, SI.CELSIUS)
		        return x
		    }
			
			// Define the coulombs creator.
			java.math.BigDecimal.metaClass.getCoulombs = { ->
				Amount x = Amount.valueOf(delegate, SI.COULOMB)
		        return x
		    }
			
			// Define the cubic meters creator.
			java.math.BigDecimal.metaClass.getCubic_meters = { ->
				Amount x = Amount.valueOf(delegate, SI.CUBIC_METER)
		        return x
		    }
			
			// Define the farads creator.
			java.math.BigDecimal.metaClass.getFarads = { ->
				Amount x = Amount.valueOf(delegate, SI.FARAD)
		        return x
		    }
			
			// Define the microfarads creator.
			java.math.BigDecimal.metaClass.getMicrofarads = { ->
				Amount x = Amount.valueOf(delegate, SI.MICRO(SI.FARAD))
		        return x
		    }
			
			// Define the nanofarads creator.
			java.math.BigDecimal.metaClass.getNanofarads = { ->
				Amount x = Amount.valueOf(delegate, SI.NANO(SI.FARAD))
		        return x
		    }
			
			// Define the picofarads creator.
			java.math.BigDecimal.metaClass.getPicofarads = { ->
				Amount x = Amount.valueOf(delegate, SI.PICO(SI.FARAD))
		        return x
		    }
			
			// Define the grays creator.
			java.math.BigDecimal.metaClass.getGrays = { ->
				Amount x = Amount.valueOf(delegate, SI.GRAY)
		        return x
		    }
			
			// Define the milligrays creator.
			java.math.BigDecimal.metaClass.getMilligrays = { ->
				Amount x = Amount.valueOf(delegate, SI.MILLI(SI.GRAY))
		        return x
		    }
			
			// Define the henrys creator.
			java.math.BigDecimal.metaClass.getHenrys = { ->
				Amount x = Amount.valueOf(delegate, SI.HENRY)
		        return x
		    }
			
			// Define the hertz creator.
			java.math.BigDecimal.metaClass.getHertz = { ->
				Amount x = Amount.valueOf(delegate, SI.HERTZ)
		        return x
		    }
			
			// Define the kilohertz creator.
			java.math.BigDecimal.metaClass.getKilohertz = { ->
				Amount x = Amount.valueOf(delegate, SI.KILO(SI.HERTZ))
		        return x
		    }
			
			// Define the megahertz creator.
			java.math.BigDecimal.metaClass.getMegahertz = { ->
				Amount x = Amount.valueOf(delegate, SI.MEGA(SI.HERTZ))
		        return x
		    }
			
			// Define the joules creator.
			java.math.BigDecimal.metaClass.getJoules = { ->
				Amount x = Amount.valueOf(delegate, SI.JOULE)
		        return x
		    }
			
			// Define the kilojoules creator.
			java.math.BigDecimal.metaClass.getKilojoules = { ->
				Amount x = Amount.valueOf(delegate, SI.KILO(SI.JOULE))
		        return x
		    }
			
			// Define the katals creator.
			java.math.BigDecimal.metaClass.getKatals = { ->
				Amount x = Amount.valueOf(delegate, SI.KATAL)
		        return x
		    }
			
			// Define the kelvin creator.
			java.math.BigDecimal.metaClass.getKelvin = { ->
				Amount x = Amount.valueOf(delegate, SI.KELVIN)
		        return x
		    }
	
			// Define the lumens creator.
			java.math.BigDecimal.metaClass.getLumens = { ->
				Amount x = Amount.valueOf(delegate, SI.LUMEN)
		        return x
		    }
			
			// Define the lux creator.
			java.math.BigDecimal.metaClass.getLux = { ->
				Amount x = Amount.valueOf(delegate, SI.LUX)
		        return x
		    }
			
			// Define the microlux creator.
			java.math.BigDecimal.metaClass.getMicrolux = { ->
				Amount x = Amount.valueOf(delegate, SI.MICRO(SI.LUX))
		        return x
		    }
	
			// Define the millilux creator.
			java.math.BigDecimal.metaClass.getMillilux = { ->
				Amount x = Amount.valueOf(delegate, SI.MILLI(SI.LUX))
		        return x
		    }
	
			// Define the kilolux creator.
			java.math.BigDecimal.metaClass.getKilolux = { ->
				Amount x = Amount.valueOf(delegate, SI.KILO(SI.LUX))
		        return x
		    }
			
			// Define the meter per second creator.
			java.math.BigDecimal.metaClass.getMeter_per_second = { ->
				Amount x = Amount.valueOf(delegate, SI.METER_PER_SECOND)
		        return x
		    }
			
			//	 Define the meter per square second creator.
			java.math.BigDecimal.metaClass.getMeter_per_square_second = { ->
				Amount x = Amount.valueOf(delegate, SI.METER_PER_SQUARE_SECOND)
		        return x
		    }
			
			// Define the moles creator.
			java.math.BigDecimal.metaClass.getMoles = { ->
				Amount x = Amount.valueOf(delegate, SI.MOLE)
		        return x
		    }
			
			// Define the newtons creator.
			java.math.BigDecimal.metaClass.getNewtons = { ->
				Amount x = Amount.valueOf(delegate, SI.NEWTON)
				return x
			}
			
			// Define the ohms creator.
			java.math.BigDecimal.metaClass.getOhms = { ->
				Amount x = Amount.valueOf(delegate, SI.OHM)
				return x
			}
			
			// Define the pascals creator.
			java.math.BigDecimal.metaClass.getPascals = { ->
				Amount x = Amount.valueOf(delegate, SI.PASCAL)
				return x
			}
			
			// Define the kilopascals creator.
			java.math.BigDecimal.metaClass.getKilopascals = { ->
				Amount x = Amount.valueOf(delegate, SI.KILO(SI.PASCAL))
				return x
			}
			
			// Define the radians creator.
			java.math.BigDecimal.metaClass.getRadians = { ->
				Amount x = Amount.valueOf(delegate, SI.RADIAN)
				return x
			}
			
			// Define the milliradians creator.
			java.math.BigDecimal.metaClass.getMilliradians = { ->
				Amount x = Amount.valueOf(delegate, SI.MILLI(SI.RADIAN))
				return x
			}
			
			// Define the microradians creator.
			java.math.BigDecimal.metaClass.getMicroradians = { ->
				Amount x = Amount.valueOf(delegate, SI.MICRO(SI.RADIAN))
				return x
			}
			
			// Define the nanoradians creator.
			java.math.BigDecimal.metaClass.getNanoradians = { ->
				Amount x = Amount.valueOf(delegate, SI.NANO(SI.RADIAN))
				return x
			}
			
			// Define the seconds creator.
			java.math.BigDecimal.metaClass.getSeconds = { ->
				Amount x = Amount.valueOf(delegate, SI.SECOND)
				return x
			}
			
			// Define the siemens creator.
			java.math.BigDecimal.metaClass.getSiemens = { ->
				Amount x = Amount.valueOf(delegate, SI.SIEMENS)
				return x
			}
			
			// Define the sieverts creator.
			java.math.BigDecimal.metaClass.getSieverts = { ->
				Amount x = Amount.valueOf(delegate, SI.SIEVERT)
				return x
			}
			
			// Define the millisieverts creator.
			java.math.BigDecimal.metaClass.getMillisieverts = { ->
				Amount x = Amount.valueOf(delegate, SI.MILLI(SI.SIEVERT))
				return x
			}
			
			// Define the microsieverts creator.
			java.math.BigDecimal.metaClass.getMicrosieverts = { ->
				Amount x = Amount.valueOf(delegate, SI.MICRO(SI.SIEVERT))
				return x
			}
			
			// Define the square meters creator.
			java.math.BigDecimal.metaClass.getSquare_meters = { ->
				Amount x = Amount.valueOf(delegate, SI.MICRO(SI.SQUARE_METER))
				return x
			}
			
			// Define the steradians creator.
			java.math.BigDecimal.metaClass.getSteradians = { ->
				Amount x = Amount.valueOf(delegate, SI.STERADIAN)
				return x
			}
			
			// Define the teslas creator.
			java.math.BigDecimal.metaClass.getTeslas = { ->
				Amount x = Amount.valueOf(delegate, SI.TESLA)
				return x
			}
			
			// Define the volts creator.
			java.math.BigDecimal.metaClass.getVolts = { ->
				Amount x = Amount.valueOf(delegate, SI.VOLT)
				return x
			}
			
			// Define the kilovolts creator.
			java.math.BigDecimal.metaClass.getKilovolts = { ->
				Amount x = Amount.valueOf(delegate, SI.KILO(SI.VOLT))
				return x
			}
			
			// Define the watts creator.
			java.math.BigDecimal.metaClass.getWatts = { ->
				Amount x = Amount.valueOf(delegate, SI.WATT)
				return x
			}
			
			// Define the kilowatts creator.
			java.math.BigDecimal.metaClass.getKilowatts = { ->
				Amount x = Amount.valueOf(delegate, SI.KILO(SI.WATT))
				return x
			}
			
			// Define the megawatts creator.
			java.math.BigDecimal.metaClass.getMegawatts = { ->
				Amount x = Amount.valueOf(delegate, SI.MEGA(SI.WATT))
				return x
			}
			
			// Define the webers creator.
			java.math.BigDecimal.metaClass.getWebers = { ->
				Amount x = Amount.valueOf(delegate, SI.WEBER)
				return x
			}
			
			// Define the ares creator.
			java.math.BigDecimal.metaClass.getAres = { ->
				Amount x = Amount.valueOf(delegate, NonSI.ARE)
		        return x
			}
			
			// Define the astronomical units creator.
			java.math.BigDecimal.metaClass.getAstronomical_units = { ->
				Amount x = Amount.valueOf(delegate, NonSI.ASTRONOMICAL_UNIT)
		        return x
			}
			
			// Define the atmospheres creator.
			java.math.BigDecimal.metaClass.getAtmospheres = { ->
				Amount x = Amount.valueOf(delegate, NonSI.ATMOSPHERE)
		        return x
			}
			
			// Define the atoms creator.
			java.math.BigDecimal.metaClass.getAtoms = { ->
				Amount x = Amount.valueOf(delegate, NonSI.ATOM)
		        return x
			}
			
			// Define the atomic mass creator.
			java.math.BigDecimal.metaClass.getAtomic_mass = { ->
				Amount x = Amount.valueOf(delegate, NonSI.ATOMIC_MASS)
		        return x
			}
			
			// Define the bars creator.
			java.math.BigDecimal.metaClass.getBars = { ->
				Amount x = Amount.valueOf(delegate, NonSI.BAR)
		        return x
			}
			
			// Define the bytes creator.
			java.math.BigDecimal.metaClass.getBytes = { ->
				Amount x = Amount.valueOf(delegate, NonSI.BYTE)
		        return x
			}
			
			// Define the c creator.
			java.math.BigDecimal.metaClass.getC = { ->
				Amount x = Amount.valueOf(delegate, NonSI.C)
		        return x
			}
			
			// Define the centiradians creator.
			java.math.BigDecimal.metaClass.getCentiradians = { ->
				Amount x = Amount.valueOf(delegate, NonSI.CENTIRADIAN)
		        return x
			}
			
			// Define the computer point creator.
			java.math.BigDecimal.metaClass.getComputer_point = { ->
				Amount x = Amount.valueOf(delegate, NonSI.COMPUTER_POINT)
		        return x
			}
			
			// Define the cubic inches creator.
			java.math.BigDecimal.metaClass.getCubic_inches = { ->
				Amount x = Amount.valueOf(delegate, NonSI.CUBIC_INCH)
		        return x
			}
			
			// Define the curies creator.
			java.math.BigDecimal.metaClass.getCuries = { ->
				Amount x = Amount.valueOf(delegate, NonSI.CURIE)
		        return x
			}
			
			// Define the days creator.
			java.math.BigDecimal.metaClass.getDays = { ->
				Amount x = Amount.valueOf(delegate, NonSI.DAY)
		        return x
			}
			
			// Define the day sidereal creator.
			java.math.BigDecimal.metaClass.getDay_sidereal = { ->
				Amount x = Amount.valueOf(delegate, NonSI.DAY_SIDEREAL)
		        return x
			}
			
			// Define the decibels creator.
			java.math.BigDecimal.metaClass.getDecibels = { ->
				Amount x = Amount.valueOf(delegate, NonSI.DECIBEL)
		        return x
			}
			
			// Define the degree angle creator.
			java.math.BigDecimal.metaClass.getDegree_angle = { ->
				Amount x = Amount.valueOf(delegate, NonSI.DEGREE_ANGLE)
		        return x
			}
			
			// Define the dynes creator.
			java.math.BigDecimal.metaClass.getDynes = { ->
				Amount x = Amount.valueOf(delegate, NonSI.DYNE)
		        return x
			}
			
			// Define the e creator.
			java.math.BigDecimal.metaClass.getE = { ->
				Amount x = Amount.valueOf(delegate, NonSI.E)
		        return x
			}
			
			// Define the electron mass creator.
			java.math.BigDecimal.metaClass.getElectron_mass = { ->
				Amount x = Amount.valueOf(delegate, NonSI.ELECTRON_MASS)
		        return x
			}
			
			// Define the electron volt creator.
			java.math.BigDecimal.metaClass.getElectron_volt = { ->
				Amount x = Amount.valueOf(delegate, NonSI.ELECTRON_VOLT)
		        return x
			}
			
			// Define the ergs creator.
			java.math.BigDecimal.metaClass.getErgs = { ->
				Amount x = Amount.valueOf(delegate, NonSI.ERG)
		        return x
			}
			
			// Define the fahrenheit creator.
			java.math.BigDecimal.metaClass.getFahrenheit = { ->
				Amount x = Amount.valueOf(delegate, NonSI.FAHRENHEIT)
		        return x
			}
			
			// Define the faradays creator.
			java.math.BigDecimal.metaClass.getFaradays = { ->
				Amount x = Amount.valueOf(delegate, NonSI.FARADAY)
		        return x
			}
			
			// Define the feet creator.
			java.math.BigDecimal.metaClass.getFeet = { ->
				Amount x = Amount.valueOf(delegate, NonSI.FOOT)
		        return x
			}
			
			// Define the foot_survey_us creator.
			java.math.BigDecimal.metaClass.getFoot_survey_us = { ->
				Amount x = Amount.valueOf(delegate, NonSI.FOOT_SURVEY_US)
		        return x
			}
			
			// Define the franklins creator.
			java.math.BigDecimal.metaClass.getFranklins = { ->
				Amount x = Amount.valueOf(delegate, NonSI.FRANKLIN)
		        return x
			}
			
			// Define the g creator.
			java.math.BigDecimal.metaClass.getG = { ->
				Amount x = Amount.valueOf(delegate, NonSI.G)
		        return x
			}
			
			// Define the gallon dry us creator.
			java.math.BigDecimal.metaClass.getGallon_dry_us = { ->
				Amount x = Amount.valueOf(delegate, NonSI.GALLON_DRY_US)
		        return x
			}
			
			// Define the gallon liquid us creator.
			java.math.BigDecimal.metaClass.getGallon_liquid_us = { ->
				Amount x = Amount.valueOf(delegate, NonSI.GALLON_LIQUID_US)
		        return x
			}
			
			// Define the gallon uk creator.
			java.math.BigDecimal.metaClass.getGallon_uk = { ->
				Amount x = Amount.valueOf(delegate, NonSI.GALLON_UK)
		        return x
			}
			
			// Define the gauss creator.
			java.math.BigDecimal.metaClass.getGauss = { ->
				Amount x = Amount.valueOf(delegate, NonSI.GAUSS)
		        return x
			}
			
			// Define the gilberts creator.
			java.math.BigDecimal.metaClass.getGilberts = { ->
				Amount x = Amount.valueOf(delegate, NonSI.GILBERT)
		        return x
			}
			
			// Define the grades creator.
			java.math.BigDecimal.metaClass.getGrades = { ->
				Amount x = Amount.valueOf(delegate, NonSI.GRADE)
		        return x
			}
			
			// Define the hectares creator.
			java.math.BigDecimal.metaClass.getHectares = { ->
				Amount x = Amount.valueOf(delegate, NonSI.HECTARE)
		        return x
			}
			
			// Define the horsepower creator.
			java.math.BigDecimal.metaClass.getHorsepower = { ->
				Amount x = Amount.valueOf(delegate, NonSI.HORSEPOWER)
		        return x
			}
			
			// Define the hours creator.
			java.math.BigDecimal.metaClass.getHours = { ->
				Amount x = Amount.valueOf(delegate, NonSI.HOUR)
		        return x
			}
			
			// Define the inches creator.
			java.math.BigDecimal.metaClass.getInches = { ->
				Amount x = Amount.valueOf(delegate, NonSI.INCH)
		        return x
			}
			
			// Define the inch_of_mercury creator.
			java.math.BigDecimal.metaClass.getInch_of_mercury = { ->
				Amount x = Amount.valueOf(delegate, NonSI.INCH_OF_MERCURY)
		        return x
			}
			
			// Define the kilogram force creator.
			java.math.BigDecimal.metaClass.getKilogram_force = { ->
				Amount x = Amount.valueOf(delegate, NonSI.KILOGRAM_FORCE)
		        return x
			}
			
			// Define the knots creator.
			java.math.BigDecimal.metaClass.getKnots = { ->
				Amount x = Amount.valueOf(delegate, NonSI.KNOT)
		        return x
			}
			
			// Define the lamberts creator.
			java.math.BigDecimal.metaClass.getLamberts = { ->
				Amount x = Amount.valueOf(delegate, NonSI.LAMBERT)
		        return x
			}
			
			// Define the light years creator.
			java.math.BigDecimal.metaClass.getLight_years = { ->
				Amount x = Amount.valueOf(delegate, NonSI.LIGHT_YEAR)
		        return x
			}
			
			// Define the liters creator.
			java.math.BigDecimal.metaClass.getLiters = { ->
				Amount x = Amount.valueOf(delegate, NonSI.LITER)
		        return x
			}
			
			// Define the mach creator.
			java.math.BigDecimal.metaClass.getMach = { ->
				Amount x = Amount.valueOf(delegate, NonSI.MACH)
		        return x
			}
			
			// Define the maxwells creator.
			java.math.BigDecimal.metaClass.getMaxwells = { ->
				Amount x = Amount.valueOf(delegate, NonSI.MAXWELL)
		        return x
			}
			
			// Define the metric tons creator.
			java.math.BigDecimal.metaClass.getMetric_tons = { ->
				Amount x = Amount.valueOf(delegate, NonSI.METRIC_TON)
		        return x
			}
			
			// Define the miles creator.
			java.math.BigDecimal.metaClass.getMiles = { ->
				Amount x = Amount.valueOf(delegate, NonSI.MILE)
		        return x
			}
			
			// Define the millimeters of mercury creator.
			java.math.BigDecimal.metaClass.getMillimeters_of_mercury = { ->
				Amount x = Amount.valueOf(delegate, NonSI.MILLIMETER_OF_MERCURY)
		        return x
			}
			
			// Define the minutes creator.
			java.math.BigDecimal.metaClass.getMinutes = { ->
				Amount x = Amount.valueOf(delegate, NonSI.MINUTE)
		        return x
			}
			
			// Define the minute angle creator.
			java.math.BigDecimal.metaClass.getMinute_angle = { ->
				Amount x = Amount.valueOf(delegate, NonSI.MINUTE_ANGLE)
		        return x
			}
			
			// Define the months creator.
			java.math.BigDecimal.metaClass.getMonths = { ->
				Amount x = Amount.valueOf(delegate, NonSI.MONTH)
		        return x
			}
			
			// Define the nautical miles creator.
			java.math.BigDecimal.metaClass.getNautical_miles = { ->
				Amount x = Amount.valueOf(delegate, NonSI.NAUTICAL_MILE)
		        return x
			}
			
			// Define the octets creator.
			java.math.BigDecimal.metaClass.getOctets = { ->
				Amount x = Amount.valueOf(delegate, NonSI.OCTET)
		        return x
			}
			
			// Define the ounces creator.
			java.math.BigDecimal.metaClass.getOunces = { ->
				Amount x = Amount.valueOf(delegate, NonSI.OUNCE)
		        return x
			}
			
			// Define the ounce liquid uk creator.
			java.math.BigDecimal.metaClass.getOunce_liquid_uk = { ->
				Amount x = Amount.valueOf(delegate, NonSI.OUNCE_LIQUID_UK)
		        return x
			}
			
			// Define the ounce liquid us creator.
			java.math.BigDecimal.metaClass.getOunce_liquid_us = { ->
				Amount x = Amount.valueOf(delegate, NonSI.OUNCE_LIQUID_US)
		        return x
			}
			
			// Define the parsecs creator.
			java.math.BigDecimal.metaClass.getParsecs = { ->
				Amount x = Amount.valueOf(delegate, NonSI.PARSEC)
		        return x
			}
			
			// Define the percent creator.
			java.math.BigDecimal.metaClass.getPercent = { ->
				Amount x = Amount.valueOf(delegate, NonSI.PERCENT)
		        return x
			}
			
			// Define the pixels creator.
			java.math.BigDecimal.metaClass.getPixels = { ->
				Amount x = Amount.valueOf(delegate, NonSI.PIXEL)
		        return x
			}
			
			// Define the points creator.
			java.math.BigDecimal.metaClass.getPoints = { ->
				Amount x = Amount.valueOf(delegate, NonSI.POINT)
		        return x
			}
			
			// Define the poise creator.
			java.math.BigDecimal.metaClass.getPoise = { ->
				Amount x = Amount.valueOf(delegate, NonSI.POISE)
		        return x
			}
			
			// Define the pound force creator.
			java.math.BigDecimal.metaClass.getPound_force = { ->
				Amount x = Amount.valueOf(delegate, NonSI.POUND_FORCE)
		        return x
		    }
			
			// Define the rads creator.
			java.math.BigDecimal.metaClass.getRads = { ->
				Amount x = Amount.valueOf(delegate, NonSI.RAD)
		        return x
		    }
			
			// Define the rankines creator.
			java.math.BigDecimal.metaClass.getRankines = { ->
				Amount x = Amount.valueOf(delegate, NonSI.RANKINE)
		        return x
		    }
			
			// Define the rems creator.
			java.math.BigDecimal.metaClass.getRems = { ->
				Amount x = Amount.valueOf(delegate, NonSI.REM)
		        return x
		    }
			
			// Define the revolutions creator.
			java.math.BigDecimal.metaClass.getRevolutions = { ->
				Amount x = Amount.valueOf(delegate, NonSI.REVOLUTION)
		        return x
		    }
			
			// Define the roentgens creator.
			java.math.BigDecimal.metaClass.getRoentgens = { ->
				Amount x = Amount.valueOf(delegate, NonSI.ROENTGEN)
		        return x
		    }
			
			// Define the rutherfords creator.
			java.math.BigDecimal.metaClass.getRutherfords = { ->
				Amount x = Amount.valueOf(delegate, NonSI.RUTHERFORD)
		        return x
		    }
			
			// Define the second angle creator.
			java.math.BigDecimal.metaClass.getSecond_angle = { ->
				Amount x = Amount.valueOf(delegate, NonSI.SECOND_ANGLE)
		        return x
		    }
			
			// Define the spheres creator.
			java.math.BigDecimal.metaClass.getSpheres = { ->
				Amount x = Amount.valueOf(delegate, NonSI.SPHERE)
		        return x
		    }
			
			// Define the stokes creator.
			java.math.BigDecimal.metaClass.getStokes = { ->
				Amount x = Amount.valueOf(delegate, NonSI.STOKE)
		        return x
		    }
			
			// Define the ton uk creator.
			java.math.BigDecimal.metaClass.getTon_uk = { ->
				Amount x = Amount.valueOf(delegate, NonSI.TON_UK)
		        return x
		    }
			
			// Define the ton us creator.
			java.math.BigDecimal.metaClass.getTon_us = { ->
				Amount x = Amount.valueOf(delegate, NonSI.TON_US)
		        return x
		    }
			
			// Define the weeks creator.
			java.math.BigDecimal.metaClass.getWeeks = { ->
				Amount x = Amount.valueOf(delegate, NonSI.WEEK)
		        return x
		    }
			
			// Define the yards creator.
			java.math.BigDecimal.metaClass.getYards = { ->
				Amount x = Amount.valueOf(delegate, NonSI.YARD)
		        return x
		    }
			
			// Define the years creator.
			java.math.BigDecimal.metaClass.getYears = { ->
				Amount x = Amount.valueOf(delegate, NonSI.YEAR)
		        return x
		    }
			
			// Define the year_calendar creator.
			java.math.BigDecimal.metaClass.getYear_calendar = { ->
				Amount x = Amount.valueOf(delegate, NonSI.YEAR_CALENDAR)
		        return x
		    }
			
			// Define the year_sidereal creator.
			java.math.BigDecimal.metaClass.getYear_sidereal = { ->
				Amount x = Amount.valueOf(delegate, NonSI.YEAR_SIDEREAL)
		        return x
		    }
			
			//	 Define the pure number creator.
			Integer.metaClass.getPure = { ->
				Amount x = Amount.valueOf(delegate, Unit.ONE)
		        return x
		    }
			
			// Define the pure number creator.
			Integer.metaClass.getPureNumber = { ->
				Amount x = Amount.valueOf(delegate, Unit.ONE)
		        return x
		    }
			
			//	 Define the meter creator.
			Integer.metaClass.getMeters = { ->
				Amount x = Amount.valueOf(delegate, SI.METER)
		        return x
		    }
			
			// Define the kilometers creator.
			Integer.metaClass.getKilometers = { ->
				Amount x = Amount.valueOf(delegate, SI.KILO(SI.METER))
		        return x
		    }
			
			// Define the centimeters creator.
			Integer.metaClass.getCentimeters = { ->
				Amount x = Amount.valueOf(delegate, SI.CENTI(SI.METER))
		        return x
		    }
			
			// Define the millimeters creator.
			Integer.metaClass.getMillimeters = { ->
				Amount x = Amount.valueOf(delegate, SI.MILLI(SI.METER))
		        return x
		    }
			
			// Define the micrometers creator.
			Integer.metaClass.getMicrometers = { ->
				Amount x = Amount.valueOf(delegate, SI.MICRO(SI.METER))
		        return x
		    }
			
			// Define the decimeters creator.
			Integer.metaClass.getDecimeters = { ->
				Amount x = Amount.valueOf(delegate, SI.DECI(SI.METER))
		        return x
		    }
			
			// Define the kilograms creator.
			Integer.metaClass.getKilograms = { ->
				Amount x = Amount.valueOf(delegate, SI.KILOGRAM)
		        return x
		    }
			
			// Define the milligrams creator.
			Integer.metaClass.getMilligrams = { ->
				Amount x = Amount.valueOf(delegate, SI.MILLI(SI.GRAM))
		        return x
		    }
			
			// Define the angstroms creator.
			Integer.metaClass.getAngstroms = { ->
				Amount x = Amount.valueOf(delegate, NonSI.ANGSTROM)
		        return x
			}
			
			// Define the pounds creator.
			Integer.metaClass.getPounds = { ->
				Amount x = Amount.valueOf(delegate, NonSI.POUND)
		        return x
		    }
			
			// Define the grams creator.
			Integer.metaClass.getGrams = { ->
				Amount x = Amount.valueOf(delegate, SI.GRAM)
		        return x
		    }
			
			// Define the amperes creator.
			Integer.metaClass.getAmperes = { ->
				Amount x = Amount.valueOf(delegate, SI.AMPERE)
		        return x
		    }
			
			// Define the becquerels creator.
			Integer.metaClass.getBecquerels = { ->
				Amount x = Amount.valueOf(delegate, SI.BECQUEREL)
		        return x
		    }
			
			// Define the kilobecquerels creator.
			Integer.metaClass.getKilobecquerels = { ->
				Amount x = Amount.valueOf(delegate, SI.KILO(SI.BECQUEREL))
		        return x
		    }
			
			// Define the megabecquerels creator.
			Integer.metaClass.getMegabecquerels = { ->
				Amount x = Amount.valueOf(delegate, SI.MEGA(SI.BECQUEREL))
		        return x
		    }
			
			// Define the gigabecquerels creator.
			Integer.metaClass.getGigabecquerels = { ->
				Amount x = Amount.valueOf(delegate, SI.GIGA(SI.BECQUEREL))
		        return x
		    }
			
			// Define the bits creator.
			Integer.metaClass.getBits = { ->
				Amount x = Amount.valueOf(delegate, SI.BIT)
		        return x
		    }
			
			// Define the kilobits creator.
			Integer.metaClass.getKilobits = { ->
				Amount x = Amount.valueOf(delegate, SI.KILO(SI.BIT))
		        return x
		    }
			
			// Define the megabits creator.
			Integer.metaClass.getMegabits = { ->
				Amount x = Amount.valueOf(delegate, SI.MEGA(SI.BIT))
		        return x
		    }
			
			// Define the gigabits creator.
			Integer.metaClass.getGigabits = { ->
				Amount x = Amount.valueOf(delegate, SI.GIGA(SI.BIT))
		        return x
		    }
			
			// Define the terabits creator.
			Integer.metaClass.getTerabits = { ->
				Amount x = Amount.valueOf(delegate, SI.TERA(SI.BIT))
		        return x
		    }
			
			// Define the candelas creator.
			Integer.metaClass.getCandelas = { ->
				Amount x = Amount.valueOf(delegate, SI.CANDELA)
		        return x
		    }
			
			// Define the celsius creator.
			Integer.metaClass.getCelsius = { ->
				Amount x = Amount.valueOf(delegate, SI.CELSIUS)
		        return x
		    }
			
			// Define the coulombs creator.
			Integer.metaClass.getCoulombs = { ->
				Amount x = Amount.valueOf(delegate, SI.COULOMB)
		        return x
		    }
			
			// Define the cubic meters creator.
			Integer.metaClass.getCubic_meters = { ->
				Amount x = Amount.valueOf(delegate, SI.CUBIC_METER)
		        return x
		    }
			
			// Define the farads creator.
			Integer.metaClass.getFarads = { ->
				Amount x = Amount.valueOf(delegate, SI.FARAD)
		        return x
		    }
			
			// Define the microfarads creator.
			Integer.metaClass.getMicrofarads = { ->
				Amount x = Amount.valueOf(delegate, SI.MICRO(SI.FARAD))
		        return x
		    }
			
			// Define the nanofarads creator.
			Integer.metaClass.getNanofarads = { ->
				Amount x = Amount.valueOf(delegate, SI.NANO(SI.FARAD))
		        return x
		    }
			
			// Define the picofarads creator.
			Integer.metaClass.getPicofarads = { ->
				Amount x = Amount.valueOf(delegate, SI.PICO(SI.FARAD))
		        return x
		    }
			
			// Define the grays creator.
			Integer.metaClass.getGrays = { ->
				Amount x = Amount.valueOf(delegate, SI.GRAY)
		        return x
		    }
			
			// Define the milligrays creator.
			Integer.metaClass.getMilligrays = { ->
				Amount x = Amount.valueOf(delegate, SI.MILLI(SI.GRAY))
		        return x
		    }
			
			// Define the henrys creator.
			Integer.metaClass.getHenrys = { ->
				Amount x = Amount.valueOf(delegate, SI.HENRY)
		        return x
		    }
			
			// Define the hertz creator.
			Integer.metaClass.getHertz = { ->
				Amount x = Amount.valueOf(delegate, SI.HERTZ)
		        return x
		    }
			
			// Define the kilohertz creator.
			Integer.metaClass.getKilohertz = { ->
				Amount x = Amount.valueOf(delegate, SI.KILO(SI.HERTZ))
		        return x
		    }
			
			// Define the megahertz creator.
			Integer.metaClass.getMegahertz = { ->
				Amount x = Amount.valueOf(delegate, SI.MEGA(SI.HERTZ))
		        return x
		    }
			
			// Define the joules creator.
			Integer.metaClass.getJoules = { ->
				Amount x = Amount.valueOf(delegate, SI.JOULE)
		        return x
		    }
			
			// Define the kilojoules creator.
			Integer.metaClass.getKilojoules = { ->
				Amount x = Amount.valueOf(delegate, SI.KILO(SI.JOULE))
		        return x
		    }
			
			// Define the katals creator.
			Integer.metaClass.getKatals = { ->
				Amount x = Amount.valueOf(delegate, SI.KATAL)
		        return x
		    }
			
			// Define the kelvin creator.
			Integer.metaClass.getKelvin = { ->
				Amount x = Amount.valueOf(delegate, SI.KELVIN)
		        return x
		    }
	
			// Define the lumens creator.
			Integer.metaClass.getLumens = { ->
				Amount x = Amount.valueOf(delegate, SI.LUMEN)
		        return x
		    }
			
			// Define the lux creator.
			Integer.metaClass.getLux = { ->
				Amount x = Amount.valueOf(delegate, SI.LUX)
		        return x
		    }
			
			// Define the microlux creator.
			Integer.metaClass.getMicrolux = { ->
				Amount x = Amount.valueOf(delegate, SI.MICRO(SI.LUX))
		        return x
		    }
	
			// Define the millilux creator.
			Integer.metaClass.getMillilux = { ->
				Amount x = Amount.valueOf(delegate, SI.MILLI(SI.LUX))
		        return x
		    }
	
			// Define the kilolux creator.
			Integer.metaClass.getKilolux = { ->
				Amount x = Amount.valueOf(delegate, SI.KILO(SI.LUX))
		        return x
		    }
			
			// Define the meter per second creator.
			Integer.metaClass.getMeter_per_second = { ->
				Amount x = Amount.valueOf(delegate, SI.METER_PER_SECOND)
		        return x
		    }
			
			//	 Define the meter per square second creator.
			Integer.metaClass.getMeter_per_square_second = { ->
				Amount x = Amount.valueOf(delegate, SI.METER_PER_SQUARE_SECOND)
		        return x
		    }
			
			// Define the moles creator.
			Integer.metaClass.getMoles = { ->
				Amount x = Amount.valueOf(delegate, SI.MOLE)
		        return x
		    }
			
			// Define the newtons creator.
			Integer.metaClass.getNewtons = { ->
				Amount x = Amount.valueOf(delegate, SI.NEWTON)
				return x
			}
			
			// Define the ohms creator.
			Integer.metaClass.getOhms = { ->
				Amount x = Amount.valueOf(delegate, SI.OHM)
				return x
			}
			
			// Define the pascals creator.
			Integer.metaClass.getPascals = { ->
				Amount x = Amount.valueOf(delegate, SI.PASCAL)
				return x
			}
			
			// Define the kilopascals creator.
			Integer.metaClass.getKilopascals = { ->
				Amount x = Amount.valueOf(delegate, SI.KILO(SI.PASCAL))
				return x
			}
			
			// Define the radians creator.
			Integer.metaClass.getRadians = { ->
				Amount x = Amount.valueOf(delegate, SI.RADIAN)
				return x
			}
			
			// Define the milliradians creator.
			Integer.metaClass.getMilliradians = { ->
				Amount x = Amount.valueOf(delegate, SI.MILLI(SI.RADIAN))
				return x
			}
			
			// Define the microradians creator.
			Integer.metaClass.getMicroradians = { ->
				Amount x = Amount.valueOf(delegate, SI.MICRO(SI.RADIAN))
				return x
			}
			
			// Define the nanoradians creator.
			Integer.metaClass.getNanoradians = { ->
				Amount x = Amount.valueOf(delegate, SI.NANO(SI.RADIAN))
				return x
			}
			
			// Define the seconds creator.
			Integer.metaClass.getSeconds = { ->
				Amount x = Amount.valueOf(delegate, SI.SECOND)
				return x
			}
			
			// Define the siemens creator.
			Integer.metaClass.getSiemens = { ->
				Amount x = Amount.valueOf(delegate, SI.SIEMENS)
				return x
			}
			
			// Define the sieverts creator.
			Integer.metaClass.getSieverts = { ->
				Amount x = Amount.valueOf(delegate, SI.SIEVERT)
				return x
			}
			
			// Define the millisieverts creator.
			Integer.metaClass.getMillisieverts = { ->
				Amount x = Amount.valueOf(delegate, SI.MILLI(SI.SIEVERT))
				return x
			}
			
			// Define the microsieverts creator.
			Integer.metaClass.getMicrosieverts = { ->
				Amount x = Amount.valueOf(delegate, SI.MICRO(SI.SIEVERT))
				return x
			}
			
			// Define the square meters creator.
			Integer.metaClass.getSquare_meters = { ->
				Amount x = Amount.valueOf(delegate, SI.MICRO(SI.SQUARE_METER))
				return x
			}
			
			// Define the steradians creator.
			Integer.metaClass.getSteradians = { ->
				Amount x = Amount.valueOf(delegate, SI.STERADIAN)
				return x
			}
			
			// Define the teslas creator.
			Integer.metaClass.getTeslas = { ->
				Amount x = Amount.valueOf(delegate, SI.TESLA)
				return x
			}
			
			// Define the volts creator.
			Integer.metaClass.getVolts = { ->
				Amount x = Amount.valueOf(delegate, SI.VOLT)
				return x
			}
			
			// Define the kilovolts creator.
			Integer.metaClass.getKilovolts = { ->
				Amount x = Amount.valueOf(delegate, SI.KILO(SI.VOLT))
				return x
			}
			
			// Define the watts creator.
			Integer.metaClass.getWatts = { ->
				Amount x = Amount.valueOf(delegate, SI.WATT)
				return x
			}
			
			// Define the kilowatts creator.
			Integer.metaClass.getKilowatts = { ->
				Amount x = Amount.valueOf(delegate, SI.KILO(SI.WATT))
				return x
			}
			
			// Define the megawatts creator.
			Integer.metaClass.getMegawatts = { ->
				Amount x = Amount.valueOf(delegate, SI.MEGA(SI.WATT))
				return x
			}
			
			// Define the webers creator.
			Integer.metaClass.getWebers = { ->
				Amount x = Amount.valueOf(delegate, SI.WEBER)
				return x
			}
			
			// Define the ares creator.
			Integer.metaClass.getAres = { ->
				Amount x = Amount.valueOf(delegate, NonSI.ARE)
		        return x
			}
			
			// Define the astronomical units creator.
			Integer.metaClass.getAstronomical_units = { ->
				Amount x = Amount.valueOf(delegate, NonSI.ASTRONOMICAL_UNIT)
		        return x
			}
			
			// Define the atmospheres creator.
			Integer.metaClass.getAtmospheres = { ->
				Amount x = Amount.valueOf(delegate, NonSI.ATMOSPHERE)
		        return x
			}
			
			// Define the atoms creator.
			Integer.metaClass.getAtoms = { ->
				Amount x = Amount.valueOf(delegate, NonSI.ATOM)
		        return x
			}
			
			// Define the atomic mass creator.
			Integer.metaClass.getAtomic_mass = { ->
				Amount x = Amount.valueOf(delegate, NonSI.ATOMIC_MASS)
		        return x
			}
			
			// Define the bars creator.
			Integer.metaClass.getBars = { ->
				Amount x = Amount.valueOf(delegate, NonSI.BAR)
		        return x
			}
			
			// Define the bytes creator.
			Integer.metaClass.getBytes = { ->
				Amount x = Amount.valueOf(delegate, NonSI.BYTE)
		        return x
			}
			
			// Define the c creator.
			Integer.metaClass.getC = { ->
				Amount x = Amount.valueOf(delegate, NonSI.C)
		        return x
			}
			
			// Define the centiradians creator.
			Integer.metaClass.getCentiradians = { ->
				Amount x = Amount.valueOf(delegate, NonSI.CENTIRADIAN)
		        return x
			}
			
			// Define the computer point creator.
			Integer.metaClass.getComputer_point = { ->
				Amount x = Amount.valueOf(delegate, NonSI.COMPUTER_POINT)
		        return x
			}
			
			// Define the cubic inches creator.
			Integer.metaClass.getCubic_inches = { ->
				Amount x = Amount.valueOf(delegate, NonSI.CUBIC_INCH)
		        return x
			}
			
			// Define the curies creator.
			Integer.metaClass.getCuries = { ->
				Amount x = Amount.valueOf(delegate, NonSI.CURIE)
		        return x
			}
			
			// Define the days creator.
			Integer.metaClass.getDays = { ->
				Amount x = Amount.valueOf(delegate, NonSI.DAY)
		        return x
			}
			
			// Define the day sidereal creator.
			Integer.metaClass.getDay_sidereal = { ->
				Amount x = Amount.valueOf(delegate, NonSI.DAY_SIDEREAL)
		        return x
			}
			
			// Define the decibels creator.
			Integer.metaClass.getDecibels = { ->
				Amount x = Amount.valueOf(delegate, NonSI.DECIBEL)
		        return x
			}
			
			// Define the degree angle creator.
			Integer.metaClass.getDegree_angle = { ->
				Amount x = Amount.valueOf(delegate, NonSI.DEGREE_ANGLE)
		        return x
			}
			
			// Define the dynes creator.
			Integer.metaClass.getDynes = { ->
				Amount x = Amount.valueOf(delegate, NonSI.DYNE)
		        return x
			}
			
			// Define the e creator.
			Integer.metaClass.getE = { ->
				Amount x = Amount.valueOf(delegate, NonSI.E)
		        return x
			}
			
			// Define the electron mass creator.
			Integer.metaClass.getElectron_mass = { ->
				Amount x = Amount.valueOf(delegate, NonSI.ELECTRON_MASS)
		        return x
			}
			
			// Define the electron volt creator.
			Integer.metaClass.getElectron_volt = { ->
				Amount x = Amount.valueOf(delegate, NonSI.ELECTRON_VOLT)
		        return x
			}
			
			// Define the ergs creator.
			Integer.metaClass.getErgs = { ->
				Amount x = Amount.valueOf(delegate, NonSI.ERG)
		        return x
			}
			
			// Define the fahrenheit creator.
			Integer.metaClass.getFahrenheit = { ->
				Amount x = Amount.valueOf(delegate, NonSI.FAHRENHEIT)
		        return x
			}
			
			// Define the faradays creator.
			Integer.metaClass.getFaradays = { ->
				Amount x = Amount.valueOf(delegate, NonSI.FARADAY)
		        return x
			}
			
			// Define the feet creator.
			Integer.metaClass.getFeet = { ->
				Amount x = Amount.valueOf(delegate, NonSI.FOOT)
		        return x
			}
			
			// Define the foot_survey_us creator.
			Integer.metaClass.getFoot_survey_us = { ->
				Amount x = Amount.valueOf(delegate, NonSI.FOOT_SURVEY_US)
		        return x
			}
			
			// Define the franklins creator.
			Integer.metaClass.getFranklins = { ->
				Amount x = Amount.valueOf(delegate, NonSI.FRANKLIN)
		        return x
			}
			
			// Define the g creator.
			Integer.metaClass.getG = { ->
				Amount x = Amount.valueOf(delegate, NonSI.G)
		        return x
			}
			
			// Define the gallon dry us creator.
			Integer.metaClass.getGallon_dry_us = { ->
				Amount x = Amount.valueOf(delegate, NonSI.GALLON_DRY_US)
		        return x
			}
			
			// Define the gallon liquid us creator.
			Integer.metaClass.getGallon_liquid_us = { ->
				Amount x = Amount.valueOf(delegate, NonSI.GALLON_LIQUID_US)
		        return x
			}
			
			// Define the gallon uk creator.
			Integer.metaClass.getGallon_uk = { ->
				Amount x = Amount.valueOf(delegate, NonSI.GALLON_UK)
		        return x
			}
			
			// Define the gauss creator.
			Integer.metaClass.getGauss = { ->
				Amount x = Amount.valueOf(delegate, NonSI.GAUSS)
		        return x
			}
			
			// Define the gilberts creator.
			Integer.metaClass.getGilberts = { ->
				Amount x = Amount.valueOf(delegate, NonSI.GILBERT)
		        return x
			}
			
			// Define the grades creator.
			Integer.metaClass.getGrades = { ->
				Amount x = Amount.valueOf(delegate, NonSI.GRADE)
		        return x
			}
			
			// Define the hectares creator.
			Integer.metaClass.getHectares = { ->
				Amount x = Amount.valueOf(delegate, NonSI.HECTARE)
		        return x
			}
			
			// Define the horsepower creator.
			Integer.metaClass.getHorsepower = { ->
				Amount x = Amount.valueOf(delegate, NonSI.HORSEPOWER)
		        return x
			}
			
			// Define the hours creator.
			Integer.metaClass.getHours = { ->
				Amount x = Amount.valueOf(delegate, NonSI.HOUR)
		        return x
			}
			
			// Define the inches creator.
			Integer.metaClass.getInches = { ->
				Amount x = Amount.valueOf(delegate, NonSI.INCH)
		        return x
			}
			
			// Define the inch_of_mercury creator.
			Integer.metaClass.getInch_of_mercury = { ->
				Amount x = Amount.valueOf(delegate, NonSI.INCH_OF_MERCURY)
		        return x
			}
			
			// Define the kilogram force creator.
			Integer.metaClass.getKilogram_force = { ->
				Amount x = Amount.valueOf(delegate, NonSI.KILOGRAM_FORCE)
		        return x
			}
			
			// Define the knots creator.
			Integer.metaClass.getKnots = { ->
				Amount x = Amount.valueOf(delegate, NonSI.KNOT)
		        return x
			}
			
			// Define the lamberts creator.
			Integer.metaClass.getLamberts = { ->
				Amount x = Amount.valueOf(delegate, NonSI.LAMBERT)
		        return x
			}
			
			// Define the light years creator.
			Integer.metaClass.getLight_years = { ->
				Amount x = Amount.valueOf(delegate, NonSI.LIGHT_YEAR)
		        return x
			}
			
			// Define the liters creator.
			Integer.metaClass.getLiters = { ->
				Amount x = Amount.valueOf(delegate, NonSI.LITER)
		        return x
			}
			
			// Define the mach creator.
			Integer.metaClass.getMach = { ->
				Amount x = Amount.valueOf(delegate, NonSI.MACH)
		        return x
			}
			
			// Define the maxwells creator.
			Integer.metaClass.getMaxwells = { ->
				Amount x = Amount.valueOf(delegate, NonSI.MAXWELL)
		        return x
			}
			
			// Define the metric tons creator.
			Integer.metaClass.getMetric_tons = { ->
				Amount x = Amount.valueOf(delegate, NonSI.METRIC_TON)
		        return x
			}
			
			// Define the miles creator.
			Integer.metaClass.getMiles = { ->
				Amount x = Amount.valueOf(delegate, NonSI.MILE)
		        return x
			}
			
			// Define the millimeters of mercury creator.
			Integer.metaClass.getMillimeters_of_mercury = { ->
				Amount x = Amount.valueOf(delegate, NonSI.MILLIMETER_OF_MERCURY)
		        return x
			}
			
			// Define the minutes creator.
			Integer.metaClass.getMinutes = { ->
				Amount x = Amount.valueOf(delegate, NonSI.MINUTE)
		        return x
			}
			
			// Define the minute angle creator.
			Integer.metaClass.getMinute_angle = { ->
				Amount x = Amount.valueOf(delegate, NonSI.MINUTE_ANGLE)
		        return x
			}
			
			// Define the months creator.
			Integer.metaClass.getMonths = { ->
				Amount x = Amount.valueOf(delegate, NonSI.MONTH)
		        return x
			}
			
			// Define the nautical miles creator.
			Integer.metaClass.getNautical_miles = { ->
				Amount x = Amount.valueOf(delegate, NonSI.NAUTICAL_MILE)
		        return x
			}
			
			// Define the octets creator.
			Integer.metaClass.getOctets = { ->
				Amount x = Amount.valueOf(delegate, NonSI.OCTET)
		        return x
			}
			
			// Define the ounces creator.
			Integer.metaClass.getOunces = { ->
				Amount x = Amount.valueOf(delegate, NonSI.OUNCE)
		        return x
			}
			
			// Define the ounce liquid uk creator.
			Integer.metaClass.getOunce_liquid_uk = { ->
				Amount x = Amount.valueOf(delegate, NonSI.OUNCE_LIQUID_UK)
		        return x
			}
			
			// Define the ounce liquid us creator.
			Integer.metaClass.getOunce_liquid_us = { ->
				Amount x = Amount.valueOf(delegate, NonSI.OUNCE_LIQUID_US)
		        return x
			}
			
			// Define the parsecs creator.
			Integer.metaClass.getParsecs = { ->
				Amount x = Amount.valueOf(delegate, NonSI.PARSEC)
		        return x
			}
			
			// Define the percent creator.
			Integer.metaClass.getPercent = { ->
				Amount x = Amount.valueOf(delegate, NonSI.PERCENT)
		        return x
			}
			
			// Define the pixels creator.
			Integer.metaClass.getPixels = { ->
				Amount x = Amount.valueOf(delegate, NonSI.PIXEL)
		        return x
			}
			
			// Define the points creator.
			Integer.metaClass.getPoints = { ->
				Amount x = Amount.valueOf(delegate, NonSI.POINT)
		        return x
			}
			
			// Define the poise creator.
			Integer.metaClass.getPoise = { ->
				Amount x = Amount.valueOf(delegate, NonSI.POISE)
		        return x
			}
			
			// Define the pound force creator.
			Integer.metaClass.getPound_force = { ->
				Amount x = Amount.valueOf(delegate, NonSI.POUND_FORCE)
		        return x
		    }
			
			// Define the rads creator.
			Integer.metaClass.getRads = { ->
				Amount x = Amount.valueOf(delegate, NonSI.RAD)
		        return x
		    }
			
			// Define the rankines creator.
			Integer.metaClass.getRankines = { ->
				Amount x = Amount.valueOf(delegate, NonSI.RANKINE)
		        return x
		    }
			
			// Define the rems creator.
			Integer.metaClass.getRems = { ->
				Amount x = Amount.valueOf(delegate, NonSI.REM)
		        return x
		    }
			
			// Define the revolutions creator.
			Integer.metaClass.getRevolutions = { ->
				Amount x = Amount.valueOf(delegate, NonSI.REVOLUTION)
		        return x
		    }
			
			// Define the roentgens creator.
			Integer.metaClass.getRoentgens = { ->
				Amount x = Amount.valueOf(delegate, NonSI.ROENTGEN)
		        return x
		    }
			
			// Define the rutherfords creator.
			Integer.metaClass.getRutherfords = { ->
				Amount x = Amount.valueOf(delegate, NonSI.RUTHERFORD)
		        return x
		    }
			
			// Define the second angle creator.
			Integer.metaClass.getSecond_angle = { ->
				Amount x = Amount.valueOf(delegate, NonSI.SECOND_ANGLE)
		        return x
		    }
			
			// Define the spheres creator.
			Integer.metaClass.getSpheres = { ->
				Amount x = Amount.valueOf(delegate, NonSI.SPHERE)
		        return x
		    }
			
			// Define the stokes creator.
			Integer.metaClass.getStokes = { ->
				Amount x = Amount.valueOf(delegate, NonSI.STOKE)
		        return x
		    }
			
			// Define the ton uk creator.
			Integer.metaClass.getTon_uk = { ->
				Amount x = Amount.valueOf(delegate, NonSI.TON_UK)
		        return x
		    }
			
			// Define the ton us creator.
			Integer.metaClass.getTon_us = { ->
				Amount x = Amount.valueOf(delegate, NonSI.TON_US)
		        return x
		    }
			
			// Define the weeks creator.
			Integer.metaClass.getWeeks = { ->
				Amount x = Amount.valueOf(delegate, NonSI.WEEK)
		        return x
		    }
			
			// Define the yards creator.
			Integer.metaClass.getYards = { ->
				Amount x = Amount.valueOf(delegate, NonSI.YARD)
		        return x
		    }
			
			// Define the years creator.
			Integer.metaClass.getYears = { ->
				Amount x = Amount.valueOf(delegate, NonSI.YEAR)
		        return x
		    }
			
			// Define the year_calendar creator.
			Integer.metaClass.getYear_calendar = { ->
				Amount x = Amount.valueOf(delegate, NonSI.YEAR_CALENDAR)
		        return x
		    }
			
			// Define the year_sidereal creator.
			Integer.metaClass.getYear_sidereal = { ->
				Amount x = Amount.valueOf(delegate, NonSI.YEAR_SIDEREAL)
		        return x
		    }
			
			Amount.metaClass.plus = { Amount amount2->
		        return UnitsOperations.addition(delegate, amount2)
		    }
		
			Amount.metaClass.multiply = { Amount amount2->
		        return UnitsOperations.multiplication(delegate, amount2)
		    }
			
			BigDecimal.metaClass.multiply = { Amount amt->
				return UnitsOperations.multiplication(delegate, amt)
			}
			
			Amount.metaClass.multiply = { BigDecimal number->
				return UnitsOperations.multiplication(delegate, number)
			}
			
			Integer.metaClass.multiply = { Amount amt->
				return UnitsOperations.multiplication(delegate, amt)
			}
			
			Amount.metaClass.multiply = { Integer number->
				return UnitsOperations.multiplication(delegate, number)
			}
			
			Amount.metaClass.minus = { Amount amount2->
		        return UnitsOperations.subtraction(delegate, amount2)
		    }
		
			Amount.metaClass.div = { Amount amount2->
		        return UnitsOperations.division(delegate, amount2)
		    }
			
			Amount.metaClass.div = { BigDecimal number->
				return UnitsOperations.division(delegate, number)
			}
			
			Amount.metaClass.div = { Integer number->
				return UnitsOperations.division(delegate, number)
			}
			
			Amount.metaClass.power = { Integer number->
				return UnitsOperations.exponentiation(delegate, number)
			}
			
			Amount.metaClass.power = { BigDecimal number->
				return UnitsOperations.exponentiation(delegate, number)
			}
			
			Amount.metaClass.power = { Amount amt2->
				return UnitsOperations.exponentiation(delegate, amt2)
			}
			RepastMathEMC.unitsInitialized = true
		}
		
	 }
	
	/**
	 * Matrix operations
	 */
	
	public static void initMatrix(){
		
		if (!isMatrixInitialized()){
		
		 
//		 This necessary for Matrix operations to run properly.
		 
			ExpandoMetaClass.enableGlobally()
			 
			ArrayList.metaClass.getDenseMatrix = { ->
		        return MatrixOperations.createDenseMatrix(delegate);
		    }
		
			ArrayList.metaClass.getMatrix = { ->
		        return MatrixOperations.createDenseMatrix(delegate);
		    }
			
			ArrayList.metaClass.sparseMatrix = { Amount zero ->
		        return MatrixOperations.createSparseMatrix(delegate, zero);
		    }
		
			Matrix.metaClass.plus = { Matrix m2 ->
		        return MatrixOperations.addition(delegate, m2);
		    }
		
			Matrix.metaClass.minus = { Matrix m2 ->
		        return MatrixOperations.subtraction(delegate, m2);
		    }
			
			Matrix.metaClass.multiply = { Matrix m2 ->
		        return MatrixOperations.multiplication(delegate, m2);
		    }
		
			Matrix.metaClass.multiply = { Amount amount ->
		        return MatrixOperations.multiplication(delegate, amount);
		    }
			
			Matrix.metaClass.div = { Amount amount ->
				return MatrixOperations.division(delegate, amount);
			}
		
			Amount.metaClass.multiply = { Matrix m1 ->
		        return MatrixOperations.multiplication(m1, delegate);
		    }
			
			Matrix.metaClass.multiply = { java.lang.Number number ->
		        return MatrixOperations.multiplication(delegate, number);
		    }
			
			Matrix.metaClass.div = { java.lang.Number number ->
				return MatrixOperations.division(delegate, number);
			}
		
			java.lang.Number.metaClass.multiply = { Matrix m1 ->
		        return MatrixOperations.multiplication(m1, delegate);
		    }
			
			Matrix.metaClass.power = { int number ->
		        return MatrixOperations.exponentiation(delegate, number);
		    }
			RepastMathEMC.matrixInitialized = true
		}
	}
		/**
		 * Calculus operations
		 */
		public static void initCalculus(){	 
			
			if (!isCalculusInitialized()){
				Closure.metaClass.derivative = {Closure targetFunction, Amount point ->
					return CalculusOperations.derivative(targetFunction, point)
				}
		
				Closure.metaClass.derivative = {Closure targetFunction, Amount point, Amount secantLength ->
					return CalculusOperations.derivative(targetFunction, point, secantLength)
				}
		
				Closure.metaClass.derivative = {Closure targetFunction, Amount point, Amount secantLength, Amount tolerance ->
					return CalculusOperations.derivative(targetFunction, point, secantLength, tolerance)
				}
					
				Closure.metaClass.derivative = {Closure targetFunction, Amount point, Amount secantLength, Amount tolerance, Amount iterations ->
					return CalculusOperations.derivative(targetFunction, point, secantLength, tolerance, iterations)
				}
		
				Closure.metaClass.integral = {Closure integrand, Amount lowerBound, Amount upperBound ->
					return CalculusOperations.integral(integrand, lowerBound, upperBound)
				}
					
				Closure.metaClass.integral = {Closure integrand, Amount lowerBound, Amount upperBound, int type ->
					return CalculusOperations.integral(integrand, lowerBound, upperBound, type)
				}
					
				Closure.metaClass.integral = {Closure integrand, Amount lowerBound, Amount upperBound, int type, Amount stepSize ->
					return CalculusOperations.integral(integrand, lowerBound, upperBound, type, stepSize)
				}
					
				Closure.metaClass.integral = {Closure integrand, Amount lowerBound, Amount upperBound, int type, Amount stepSize, Amount tolerance, Amount iterations ->
					return CalculusOperations.integral(integrand, lowerBound, upperBound, type, stepSize, tolerance, iterations)
				}
				RepastMathEMC.calculusInitialized = true
			}

	}
}