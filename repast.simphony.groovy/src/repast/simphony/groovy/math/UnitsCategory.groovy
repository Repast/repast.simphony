/*
 * Author: Kevin L. Poch and Michael J. North
 */

package repast.simphony.groovy.math

import java.util.*
import javax.measure.unit.*
import org.jscience.physics.amount.*
import org.jscience.mathematics.number.*


// SI units: http://jscience.org/api/javax/measure/unit/SI.html
// NonSI Units: http://jscience.org/api/javax/measure/unit/NonSI.html

class UnitsCategory {
	
	// Define the pure number creator.
	static Amount getPure(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, Unit.ONE)
        return x
    }
	
	// Define the pure number creator.
	static Amount getPureNumber(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, Unit.ONE)
        return x
    }
	
	// Define the deci converter.
	static Amount getDeci(Amount amount) {
		Amount a = amount.to(SI.DECI(amount.getUnit()))
		a.times(Amount.valueOf(10 ** -1, Unit.ONE))
        return a
    }
	
	// Define the deka converter.
	static Amount getDeka(Amount amount) {
		Amount a = amount.to(SI.DEKA(amount.getUnit()))
		a.times(Amount.valueOf(10 ** 1, Unit.ONE))
        return a
    }
	
	// Define the centi converter.
	static Amount getCenti(Amount amount) {
		Amount a = amount.to(SI.CENTI(amount.getUnit()))
		a.times(Amount.valueOf(10 ** -2, Unit.ONE))
        return a
    }
	
	// Define the hecto converter.
	static Amount getHecto(Amount amount) {
		Amount a = amount.to(SI.HECTO(amount.getUnit()))
		a.times(Amount.valueOf(10 ** 2, Unit.ONE))
        return a
    }
	
	// Define the milli converter.
	static Amount getMilli(Amount amount) {
		Amount a = amount.to(SI.MILLI(amount.getUnit()))
		a.times(Amount.valueOf(10 ** -3, Unit.ONE))
        return a
    }
	
	// Define the kilo converter.
	static Amount getKilo(Amount amount) {
		Amount a = amount.to(SI.KILO(amount.getUnit()))
		a.times(Amount.valueOf(10 ** 3, Unit.ONE))
        return a
    }
	
	// Define the micro converter.
	static Amount getMicro(Amount amount) {
		Amount a = amount.to(SI.MICRO(amount.getUnit()))
		a.times(Amount.valueOf(10 ** -6, Unit.ONE))
        return a
    }
	
	// Define the mega converter.
	static Amount getMega(Amount amount) {
		Amount a = amount.to(SI.MEGA(amount.getUnit()))
		a.times(Amount.valueOf(10 ** 6, Unit.ONE))
        return a
    }
	
	// Define the nano converter.
	static Amount getNano(Amount amount) {
		Amount a = amount.to(SI.NANO(amount.getUnit()))
		a.times(Amount.valueOf(10 ** -9, Unit.ONE))
        return a
    }
	
	// Define the giga converter.
	static Amount getGiga(Amount amount) {
		Amount a = amount.to(SI.GIGA(amount.getUnit()))
		a.times(Amount.valueOf(10 ** 9, Unit.ONE))
        return a
    }
	
	// Define the pico converter.
	static Amount getPico(Amount amount) {
		Amount a = amount.to(SI.PICO(amount.getUnit()))
		a.times(Amount.valueOf(10 ** -12, Unit.ONE))
        return a
    }
	
	// Define the tera converter.
	static Amount getTera(Amount amount) {
		Amount a = amount.to(SI.TERA(amount.getUnit()))
		a.times(Amount.valueOf(10 ** 12, Unit.ONE))
        return a
    }
	
	// Define the femto converter.
	static Amount getFemto(Amount amount) {
		Amount a = amount.to(SI.FEMTO(amount.getUnit()))
		a.times(Amount.valueOf(10 ** -15, Unit.ONE))
        return a
    }
	
	// Define the peta converter.
	static Amount getPeta(Amount amount) {
		Amount a = amount.to(SI.PETA(amount.getUnit()))
		a.times(Amount.valueOf(10 ** 15, Unit.ONE))
        return a
    }
	
	// Define the atto converter.
	static Amount getAtto(Amount amount) {
		Amount a = amount.to(SI.ATTO(amount.getUnit()))
		a.times(Amount.valueOf(10 ** -18, Unit.ONE))
        return a
    }
	
	// Define the exa converter.
	static Amount getExa(Amount amount) {
		Amount a = amount.to(SI.EXA(amount.getUnit()))
		a.times(Amount.valueOf(10 ** 18, Unit.ONE))
        return a
    }
	
	// Define the zepto converter.
	static Amount getZepto(Amount amount) {
		Amount a = amount.to(SI.ZEPTO(amount.getUnit()))
		a.times(Amount.valueOf(10 ** -21, Unit.ONE))
        return a
    }
	
	// Define the zetta converter.
	static Amount getZetta(Amount amount) {
		Amount a = amount.to(SI.ZETTA(amount.getUnit()))
		a.times(Amount.valueOf(10 ** 21, Unit.ONE))
        return a
    }
	
	// Define the yocto converter.
	static Amount getYocto(Amount amount) {
		Amount a = amount.to(SI.YOCTO(amount.getUnit()))
		a.times(Amount.valueOf(10 ** -24, Unit.ONE))
        return a
    }
	
	// Define the yotta converter.
	static Amount getYotta(Amount amount) {
		Amount a = amount.to(SI.YOTTA(amount.getUnit()))
		a.times(Amount.valueOf(10 ** 24, Unit.ONE))
        return a
    }
	
	// Define the meter creator.
	static Amount getMeters(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.METER)
        return x
    }
	
	// Define the kilometers creator.
	static Amount getKilometers(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.KILO(SI.METER))
        return x
    }
	
	// Define the centimeters creator.
	static Amount getCentimeters(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.CENTI(SI.METER))
        return x
    }
	
	// Define the millimeters creator.
	static Amount getMillimeters(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.MILLI(SI.METER))
        return x
    }
	
	// Define the micrometers creator.
	static Amount getMicrometers(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.MICRO(SI.METER))
        return x
    }
	
	// Define the decimeters creator.
	static Amount getDecimeters(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.DECI(SI.METER))
        return x
    }
	
	// Define the kilograms creator.
	static Amount getKilograms(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.KILOGRAM)
        return x
    }
	
	// Define the milligrams creator.
	static Amount getMilligrams(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.MILLI(SI.GRAM))
        return x
    }
	
	// Define the angstroms creator.
	static Amount getAngstroms(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.ANGSTROM)
        return x
	}
	
	// Define the pounds creator.
	static Amount getPounds(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.POUND)
        return x
    }
	
	// Define the grams creator.
	static Amount getGrams(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.GRAM)
        return x
    }
	
	// Define the amperes creator.
	static Amount getAmperes(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.AMPERE)
        return x
    }
	
	// Define the becquerels creator.
	static Amount getBecquerels(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.BECQUEREL)
        return x
    }
	
	// Define the kilobecquerels creator.
	static Amount getKilobecquerels(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.KILO(SI.BECQUEREL))
        return x
    }
	
	// Define the megabecquerels creator.
	static Amount getMegabecquerels(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.MEGA(SI.BECQUEREL))
        return x
    }
	
	// Define the gigabecquerels creator.
	static Amount getGigabecquerels(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.GIGA(SI.BECQUEREL))
        return x
    }
	
	// Define the bits creator.
	static Amount getBits(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.BIT)
        return x
    }
	
	// Define the kilobits creator.
	static Amount getKilobits(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.KILO(SI.BIT))
        return x
    }
	
	// Define the megabits creator.
	static Amount getMegabits(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.MEGA(SI.BIT))
        return x
    }
	
	// Define the gigabits creator.
	static Amount getGigabits(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.GIGA(SI.BIT))
        return x
    }
	
	// Define the terabits creator.
	static Amount getTerabits(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.TERA(SI.BIT))
        return x
    }
	
	// Define the candelas creator.
	static Amount getCandelas(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.CANDELA)
        return x
    }
	
	// Define the celsius creator.
	static Amount getCelsius(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.CELSIUS)
        return x
    }
	
	// Define the coulombs creator.
	static Amount getCoulombs(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.COULOMB)
        return x
    }
	
	// Define the cubic meters creator.
	static Amount getCubic_meters(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.CUBIC_METER)
        return x
    }
	
	// Define the farads creator.
	static Amount getFarads(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.FARAD)
        return x
    }
	
	// Define the microfarads creator.
	static Amount getMicrofarads(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.MICRO(SI.FARAD))
        return x
    }
	
	// Define the nanofarads creator.
	static Amount getNanofarads(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.NANO(SI.FARAD))
        return x
    }
	
	// Define the picofarads creator.
	static Amount getPicofarads(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.PICO(SI.FARAD))
        return x
    }
	
	// Define the grays creator.
	static Amount getGrays(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.GRAY)
        return x
    }
	
	// Define the milligrays creator.
	static Amount getMilligrays(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.MILLI(SI.GRAY))
        return x
    }
	
	// Define the henrys creator.
	static Amount getHenrys(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.HENRY)
        return x
    }
	
	// Define the hertz creator.
	static Amount getHertz(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.HERTZ)
        return x
    }
	
	// Define the kilohertz creator.
	static Amount getKilohertz(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.KILO(SI.HERTZ))
        return x
    }
	
	// Define the megahertz creator.
	static Amount getMegahertz(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.MEGA(SI.HERTZ))
        return x
    }
	
	// Define the joules creator.
	static Amount getJoules(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.JOULE)
        return x
    }
	
	// Define the kilojoules creator.
	static Amount getKilojoules(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.KILO(SI.JOULE))
        return x
    }
	
	// Define the katals creator.
	static Amount getKatals(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.KATAL)
        return x
    }
	
	// Define the kelvin creator.
	static Amount getKelvin(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.KELVIN)
        return x
    }

	// Define the lumens creator.
	static Amount getLumens(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.LUMEN)
        return x
    }
	
	// Define the lux creator.
	static Amount getLux(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.LUX)
        return x
    }
	
	// Define the microlux creator.
	static Amount getMicrolux(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.MICRO(SI.LUX))
        return x
    }

	// Define the millilux creator.
	static Amount getMillilux(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.MILLI(SI.LUX))
        return x
    }

	// Define the kilolux creator.
	static Amount getKilolux(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.KILO(SI.LUX))
        return x
    }
	
	// Define the meter per second creator.
	static Amount getMeter_per_second(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.METER_PER_SECOND)
        return x
    }
	
	//	 Define the meter per square second creator.
	static Amount getMeter_per_square_second(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.METER_PER_SQUARE_SECOND)
        return x
    }
	
	// Define the moles creator.
	static Amount getMoles(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.MOLE)
        return x
    }
	
	// Define the newtons creator.
	static Amount getNewtons(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.NEWTON)
		return x
	}
	
	// Define the ohms creator.
	static Amount getOhms(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.OHM)
		return x
	}
	
	// Define the pascals creator.
	static Amount getPascals(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.PASCAL)
		return x
	}
	
	// Define the kilopascals creator.
	static Amount getKilopascals(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.KILO(SI.PASCAL))
		return x
	}
	
	// Define the radians creator.
	static Amount getRadians(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.RADIAN)
		return x
	}
	
	// Define the milliradians creator.
	static Amount getMilliradians(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.MILLI(SI.RADIAN))
		return x
	}
	
	// Define the microradians creator.
	static Amount getMicroradians(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.MICRO(SI.RADIAN))
		return x
	}
	
	// Define the nanoradians creator.
	static Amount getNanoradians(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.NANO(SI.RADIAN))
		return x
	}
	
	// Define the seconds creator.
	static Amount getSeconds(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.SECOND)
		return x
	}
	
	// Define the siemens creator.
	static Amount getSiemens(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.SIEMENS)
		return x
	}
	
	// Define the sieverts creator.
	static Amount getSieverts(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.SIEVERT)
		return x
	}
	
	// Define the millisieverts creator.
	static Amount getMillisieverts(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.MILLI(SI.SIEVERT))
		return x
	}
	
	// Define the microsieverts creator.
	static Amount getMicrosieverts(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.MICRO(SI.SIEVERT))
		return x
	}
	
	// Define the square meters creator.
	static Amount getSquare_meters(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.MICRO(SI.SQUARE_METER))
		return x
	}
	
	// Define the steradians creator.
	static Amount getSteradians(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.STERADIAN)
		return x
	}
	
	// Define the teslas creator.
	static Amount getTeslas(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.TESLA)
		return x
	}
	
	// Define the volts creator.
	static Amount getVolts(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.VOLT)
		return x
	}
	
	// Define the kilovolts creator.
	static Amount getKilovolts(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.KILO(SI.VOLT))
		return x
	}
	
	// Define the watts creator.
	static Amount getWatts(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.WATT)
		return x
	}
	
	// Define the kilowatts creator.
	static Amount getKilowatts(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.KILO(SI.WATT))
		return x
	}
	
	// Define the megawatts creator.
	static Amount getMegawatts(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.MEGA(SI.WATT))
		return x
	}
	
	// Define the webers creator.
	static Amount getWebers(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, SI.WEBER)
		return x
	}
	
	// Define the ares creator.
	static Amount getAres(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.ARE)
        return x
	}
	
	// Define the astronomical units creator.
	static Amount getAstronomical_units(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.ASTRONOMICAL_UNIT)
        return x
	}
	
	// Define the atmospheres creator.
	static Amount getAtmospheres(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.ATMOSPHERE)
        return x
	}
	
	// Define the atoms creator.
	static Amount getAtoms(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.ATOM)
        return x
	}
	
	// Define the atomic mass creator.
	static Amount getAtomic_mass(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.ATOMIC_MASS)
        return x
	}
	
	// Define the bars creator.
	static Amount getBars(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.BAR)
        return x
	}
	
	// Define the bytes creator.
	static Amount getBytes(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.BYTE)
        return x
	}
	
	// Define the c creator.
	static Amount getC(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.C)
        return x
	}
	
	// Define the centiradians creator.
	static Amount getCentiradians(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.CENTIRADIAN)
        return x
	}
	
	// Define the computer point creator.
	static Amount getComputer_point(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.COMPUTER_POINT)
        return x
	}
	
	// Define the cubic inches creator.
	static Amount getCubic_inches(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.CUBIC_INCH)
        return x
	}
	
	// Define the curies creator.
	static Amount getCuries(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.CURIE)
        return x
	}
	
	// Define the days creator.
	static Amount getDays(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.DAY)
        return x
	}
	
	// Define the day sidereal creator.
	static Amount getDay_sidereal(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.DAY_SIDEREAL)
        return x
	}
	
	// Define the decibels creator.
	static Amount getDecibels(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.DECIBEL)
        return x
	}
	
	// Define the degree angle creator.
	static Amount getDegree_angle(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.DEGREE_ANGLE)
        return x
	}
	
	// Define the dynes creator.
	static Amount getDynes(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.DYNE)
        return x
	}
	
	// Define the e creator.
	static Amount getE(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.E)
        return x
	}
	
	// Define the electron mass creator.
	static Amount getElectron_mass(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.ELECTRON_MASS)
        return x
	}
	
	// Define the electron volt creator.
	static Amount getElectron_volt(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.ELECTRON_VOLT)
        return x
	}
	
	// Define the ergs creator.
	static Amount getErgs(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.ERG)
        return x
	}
	
	// Define the fahrenheit creator.
	static Amount getFahrenheit(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.FAHRENHEIT)
        return x
	}
	
	// Define the faradays creator.
	static Amount getFaradays(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.FARADAY)
        return x
	}
	
	// Define the feet creator.
	static Amount getFeet(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.FOOT)
        return x
	}
	
	// Define the foot_survey_us creator.
	static Amount getFoot_survey_us(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.FOOT_SURVEY_US)
        return x
	}
	
	// Define the franklins creator.
	static Amount getFranklins(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.FRANKLIN)
        return x
	}
	
	// Define the g creator.
	static Amount getG(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.G)
        return x
	}
	
	// Define the gallon dry us creator.
	static Amount getGallon_dry_us(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.GALLON_DRY_US)
        return x
	}
	
	// Define the gallon liquid us creator.
	static Amount getGallon_liquid_us(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.GALLON_LIQUID_US)
        return x
	}
	
	// Define the gallon uk creator.
	static Amount getGallon_uk(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.GALLON_UK)
        return x
	}
	
	// Define the gauss creator.
	static Amount getGauss(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.GAUSS)
        return x
	}
	
	// Define the gilberts creator.
	static Amount getGilberts(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.GILBERT)
        return x
	}
	
	// Define the grades creator.
	static Amount getGrades(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.GRADE)
        return x
	}
	
	// Define the hectares creator.
	static Amount getHectares(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.HECTARE)
        return x
	}
	
	// Define the horsepower creator.
	static Amount getHorsepower(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.HORSEPOWER)
        return x
	}
	
	// Define the hours creator.
	static Amount getHours(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.HOUR)
        return x
	}
	
	// Define the inches creator.
	static Amount getInches(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.INCH)
        return x
	}
	
	// Define the inch_of_mercury creator.
	static Amount getInch_of_mercury(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.INCH_OF_MERCURY)
        return x
	}
	
	// Define the kilogram force creator.
	static Amount getKilogram_force(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.KILOGRAM_FORCE)
        return x
	}
	
	// Define the knots creator.
	static Amount getKnots(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.KNOT)
        return x
	}
	
	// Define the lamberts creator.
	static Amount getLamberts(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.LAMBERT)
        return x
	}
	
	// Define the light years creator.
	static Amount getLight_years(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.LIGHT_YEAR)
        return x
	}
	
	// Define the liters creator.
	static Amount getLiters(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.LITER)
        return x
	}
	
	// Define the mach creator.
	static Amount getMach(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.MACH)
        return x
	}
	
	// Define the maxwells creator.
	static Amount getMaxwells(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.MAXWELL)
        return x
	}
	
	// Define the metric tons creator.
	static Amount getMetric_tons(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.METRIC_TON)
        return x
	}
	
	// Define the miles creator.
	static Amount getMiles(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.MILE)
        return x
	}
	
	// Define the millimeters of mercury creator.
	static Amount getMillimeters_of_mercury(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.MILLIMETER_OF_MERCURY)
        return x
	}
	
	// Define the minutes creator.
	static Amount getMinutes(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.MINUTE)
        return x
	}
	
	// Define the minute angle creator.
	static Amount getMinute_angle(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.MINUTE_ANGLE)
        return x
	}
	
	// Define the months creator.
	static Amount getMonths(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.MONTH)
        return x
	}
	
	// Define the nautical miles creator.
	static Amount getNautical_miles(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.NAUTICAL_MILE)
        return x
	}
	
	// Define the octets creator.
	static Amount getOctets(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.OCTET)
        return x
	}
	
	// Define the ounces creator.
	static Amount getOunces(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.OUNCE)
        return x
	}
	
	// Define the ounce liquid uk creator.
	static Amount getOunce_liquid_uk(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.OUNCE_LIQUID_UK)
        return x
	}
	
	// Define the ounce liquid us creator.
	static Amount getOunce_liquid_us(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.OUNCE_LIQUID_US)
        return x
	}
	
	// Define the parsecs creator.
	static Amount getParsecs(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.PARSEC)
        return x
	}
	
	// Define the percent creator.
	static Amount getPercent(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.PERCENT)
        return x
	}
	
	// Define the pixels creator.
	static Amount getPixels(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.PIXEL)
        return x
	}
	
	// Define the points creator.
	static Amount getPoints(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.POINT)
        return x
	}
	
	// Define the poise creator.
	static Amount getPoise(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.POISE)
        return x
	}
	
	// Define the pound force creator.
	static Amount getPound_force(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.POUND_FORCE)
        return x
    }
	
	// Define the rads creator.
	static Amount getRads(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.RAD)
        return x
    }
	
	// Define the rankines creator.
	static Amount getRankines(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.RANKINE)
        return x
    }
	
	// Define the rems creator.
	static Amount getRems(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.REM)
        return x
    }
	
	// Define the revolutions creator.
	static Amount getRevolutions(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.REVOLUTION)
        return x
    }
	
	// Define the roentgens creator.
	static Amount getRoentgens(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.ROENTGEN)
        return x
    }
	
	// Define the rutherfords creator.
	static Amount getRutherfords(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.RUTHERFORD)
        return x
    }
	
	// Define the second angle creator.
	static Amount getSecond_angle(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.SECOND_ANGLE)
        return x
    }
	
	// Define the spheres creator.
	static Amount getSpheres(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.SPHERE)
        return x
    }
	
	// Define the stokes creator.
	static Amount getStokes(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.STOKE)
        return x
    }
	
	// Define the ton uk creator.
	static Amount getTon_uk(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.TON_UK)
        return x
    }
	
	// Define the ton us creator.
	static Amount getTon_us(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.TON_US)
        return x
    }
	
	// Define the weeks creator.
	static Amount getWeeks(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.WEEK)
        return x
    }
	
	// Define the yards creator.
	static Amount getYards(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.YARD)
        return x
    }
	
	// Define the years creator.
	static Amount getYears(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.YEAR)
        return x
    }
	
	// Define the year_calendar creator.
	static Amount getYear_calendar(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.YEAR_CALENDAR)
        return x
    }
	
	// Define the year_sidereal creator.
	static Amount getYear_sidereal(java.math.BigDecimal amount) {
		Amount x = Amount.valueOf(amount, NonSI.YEAR_SIDEREAL)
        return x
    }
	
	//	 Define the pure number creator.
	static Amount getPure(Integer amount) {
		Amount x = Amount.valueOf(amount, Unit.ONE)
        return x
    }
	
	// Define the pure number creator.
	static Amount getPureNumber(Integer amount) {
		Amount x = Amount.valueOf(amount, Unit.ONE)
        return x
    }
	
	//	 Define the meter creator.
	static Amount getMeters(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.METER)
        return x
    }
	
	// Define the kilometers creator.
	static Amount getKilometers(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.KILO(SI.METER))
        return x
    }
	
	// Define the centimeters creator.
	static Amount getCentimeters(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.CENTI(SI.METER))
        return x
    }
	
	// Define the millimeters creator.
	static Amount getMillimeters(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.MILLI(SI.METER))
        return x
    }
	
	// Define the micrometers creator.
	static Amount getMicrometers(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.MICRO(SI.METER))
        return x
    }
	
	// Define the decimeters creator.
	static Amount getDecimeters(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.DECI(SI.METER))
        return x
    }
	
	// Define the kilograms creator.
	static Amount getKilograms(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.KILOGRAM)
        return x
    }
	
	// Define the milligrams creator.
	static Amount getMilligrams(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.MILLI(SI.GRAM))
        return x
    }
	
	// Define the angstroms creator.
	static Amount getAngstroms(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.ANGSTROM)
        return x
	}
	
	// Define the pounds creator.
	static Amount getPounds(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.POUND)
        return x
    }
	
	// Define the grams creator.
	static Amount getGrams(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.GRAM)
        return x
    }
	
	// Define the amperes creator.
	static Amount getAmperes(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.AMPERE)
        return x
    }
	
	// Define the becquerels creator.
	static Amount getBecquerels(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.BECQUEREL)
        return x
    }
	
	// Define the kilobecquerels creator.
	static Amount getKilobecquerels(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.KILO(SI.BECQUEREL))
        return x
    }
	
	// Define the megabecquerels creator.
	static Amount getMegabecquerels(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.MEGA(SI.BECQUEREL))
        return x
    }
	
	// Define the gigabecquerels creator.
	static Amount getGigabecquerels(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.GIGA(SI.BECQUEREL))
        return x
    }
	
	// Define the bits creator.
	static Amount getBits(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.BIT)
        return x
    }
	
	// Define the kilobits creator.
	static Amount getKilobits(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.KILO(SI.BIT))
        return x
    }
	
	// Define the megabits creator.
	static Amount getMegabits(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.MEGA(SI.BIT))
        return x
    }
	
	// Define the gigabits creator.
	static Amount getGigabits(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.GIGA(SI.BIT))
        return x
    }
	
	// Define the terabits creator.
	static Amount getTerabits(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.TERA(SI.BIT))
        return x
    }
	
	// Define the candelas creator.
	static Amount getCandelas(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.CANDELA)
        return x
    }
	
	// Define the celsius creator.
	static Amount getCelsius(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.CELSIUS)
        return x
    }
	
	// Define the coulombs creator.
	static Amount getCoulombs(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.COULOMB)
        return x
    }
	
	// Define the cubic meters creator.
	static Amount getCubic_meters(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.CUBIC_METER)
        return x
    }
	
	// Define the farads creator.
	static Amount getFarads(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.FARAD)
        return x
    }
	
	// Define the microfarads creator.
	static Amount getMicrofarads(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.MICRO(SI.FARAD))
        return x
    }
	
	// Define the nanofarads creator.
	static Amount getNanofarads(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.NANO(SI.FARAD))
        return x
    }
	
	// Define the picofarads creator.
	static Amount getPicofarads(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.PICO(SI.FARAD))
        return x
    }
	
	// Define the grays creator.
	static Amount getGrays(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.GRAY)
        return x
    }
	
	// Define the milligrays creator.
	static Amount getMilligrays(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.MILLI(SI.GRAY))
        return x
    }
	
	// Define the henrys creator.
	static Amount getHenrys(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.HENRY)
        return x
    }
	
	// Define the hertz creator.
	static Amount getHertz(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.HERTZ)
        return x
    }
	
	// Define the kilohertz creator.
	static Amount getKilohertz(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.KILO(SI.HERTZ))
        return x
    }
	
	// Define the megahertz creator.
	static Amount getMegahertz(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.MEGA(SI.HERTZ))
        return x
    }
	
	// Define the joules creator.
	static Amount getJoules(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.JOULE)
        return x
    }
	
	// Define the kilojoules creator.
	static Amount getKilojoules(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.KILO(SI.JOULE))
        return x
    }
	
	// Define the katals creator.
	static Amount getKatals(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.KATAL)
        return x
    }
	
	// Define the kelvin creator.
	static Amount getKelvin(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.KELVIN)
        return x
    }

	// Define the lumens creator.
	static Amount getLumens(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.LUMEN)
        return x
    }
	
	// Define the lux creator.
	static Amount getLux(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.LUX)
        return x
    }
	
	// Define the microlux creator.
	static Amount getMicrolux(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.MICRO(SI.LUX))
        return x
    }

	// Define the millilux creator.
	static Amount getMillilux(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.MILLI(SI.LUX))
        return x
    }

	// Define the kilolux creator.
	static Amount getKilolux(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.KILO(SI.LUX))
        return x
    }
	
	// Define the meter per second creator.
	static Amount getMeter_per_second(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.METER_PER_SECOND)
        return x
    }
	
	//	 Define the meter per square second creator.
	static Amount getMeter_per_square_second(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.METER_PER_SQUARE_SECOND)
        return x
    }
	
	// Define the moles creator.
	static Amount getMoles(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.MOLE)
        return x
    }
	
	// Define the newtons creator.
	static Amount getNewtons(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.NEWTON)
		return x
	}
	
	// Define the ohms creator.
	static Amount getOhms(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.OHM)
		return x
	}
	
	// Define the pascals creator.
	static Amount getPascals(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.PASCAL)
		return x
	}
	
	// Define the kilopascals creator.
	static Amount getKilopascals(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.KILO(SI.PASCAL))
		return x
	}
	
	// Define the radians creator.
	static Amount getRadians(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.RADIAN)
		return x
	}
	
	// Define the milliradians creator.
	static Amount getMilliradians(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.MILLI(SI.RADIAN))
		return x
	}
	
	// Define the microradians creator.
	static Amount getMicroradians(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.MICRO(SI.RADIAN))
		return x
	}
	
	// Define the nanoradians creator.
	static Amount getNanoradians(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.NANO(SI.RADIAN))
		return x
	}
	
	// Define the seconds creator.
	static Amount getSeconds(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.SECOND)
		return x
	}
	
	// Define the siemens creator.
	static Amount getSiemens(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.SIEMENS)
		return x
	}
	
	// Define the sieverts creator.
	static Amount getSieverts(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.SIEVERT)
		return x
	}
	
	// Define the millisieverts creator.
	static Amount getMillisieverts(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.MILLI(SI.SIEVERT))
		return x
	}
	
	// Define the microsieverts creator.
	static Amount getMicrosieverts(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.MICRO(SI.SIEVERT))
		return x
	}
	
	// Define the square meters creator.
	static Amount getSquare_meters(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.MICRO(SI.SQUARE_METER))
		return x
	}
	
	// Define the steradians creator.
	static Amount getSteradians(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.STERADIAN)
		return x
	}
	
	// Define the teslas creator.
	static Amount getTeslas(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.TESLA)
		return x
	}
	
	// Define the volts creator.
	static Amount getVolts(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.VOLT)
		return x
	}
	
	// Define the kilovolts creator.
	static Amount getKilovolts(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.KILO(SI.VOLT))
		return x
	}
	
	// Define the watts creator.
	static Amount getWatts(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.WATT)
		return x
	}
	
	// Define the kilowatts creator.
	static Amount getKilowatts(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.KILO(SI.WATT))
		return x
	}
	
	// Define the megawatts creator.
	static Amount getMegawatts(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.MEGA(SI.WATT))
		return x
	}
	
	// Define the webers creator.
	static Amount getWebers(Integer amount) {
		Amount x = Amount.valueOf(amount, SI.WEBER)
		return x
	}
	
	// Define the ares creator.
	static Amount getAres(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.ARE)
        return x
	}
	
	// Define the astronomical units creator.
	static Amount getAstronomical_units(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.ASTRONOMICAL_UNIT)
        return x
	}
	
	// Define the atmospheres creator.
	static Amount getAtmospheres(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.ATMOSPHERE)
        return x
	}
	
	// Define the atoms creator.
	static Amount getAtoms(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.ATOM)
        return x
	}
	
	// Define the atomic mass creator.
	static Amount getAtomic_mass(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.ATOMIC_MASS)
        return x
	}
	
	// Define the bars creator.
	static Amount getBars(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.BAR)
        return x
	}
	
	// Define the bytes creator.
	static Amount getBytes(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.BYTE)
        return x
	}
	
	// Define the c creator.
	static Amount getC(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.C)
        return x
	}
	
	// Define the centiradians creator.
	static Amount getCentiradians(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.CENTIRADIAN)
        return x
	}
	
	// Define the computer point creator.
	static Amount getComputer_point(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.COMPUTER_POINT)
        return x
	}
	
	// Define the cubic inches creator.
	static Amount getCubic_inches(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.CUBIC_INCH)
        return x
	}
	
	// Define the curies creator.
	static Amount getCuries(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.CURIE)
        return x
	}
	
	// Define the days creator.
	static Amount getDays(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.DAY)
        return x
	}
	
	// Define the day sidereal creator.
	static Amount getDay_sidereal(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.DAY_SIDEREAL)
        return x
	}
	
	// Define the decibels creator.
	static Amount getDecibels(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.DECIBEL)
        return x
	}
	
	// Define the degree angle creator.
	static Amount getDegree_angle(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.DEGREE_ANGLE)
        return x
	}
	
	// Define the dynes creator.
	static Amount getDynes(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.DYNE)
        return x
	}
	
	// Define the e creator.
	static Amount getE(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.E)
        return x
	}
	
	// Define the electron mass creator.
	static Amount getElectron_mass(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.ELECTRON_MASS)
        return x
	}
	
	// Define the electron volt creator.
	static Amount getElectron_volt(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.ELECTRON_VOLT)
        return x
	}
	
	// Define the ergs creator.
	static Amount getErgs(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.ERG)
        return x
	}
	
	// Define the fahrenheit creator.
	static Amount getFahrenheit(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.FAHRENHEIT)
        return x
	}
	
	// Define the faradays creator.
	static Amount getFaradays(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.FARADAY)
        return x
	}
	
	// Define the feet creator.
	static Amount getFeet(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.FOOT)
        return x
	}
	
	// Define the foot_survey_us creator.
	static Amount getFoot_survey_us(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.FOOT_SURVEY_US)
        return x
	}
	
	// Define the franklins creator.
	static Amount getFranklins(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.FRANKLIN)
        return x
	}
	
	// Define the g creator.
	static Amount getG(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.G)
        return x
	}
	
	// Define the gallon dry us creator.
	static Amount getGallon_dry_us(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.GALLON_DRY_US)
        return x
	}
	
	// Define the gallon liquid us creator.
	static Amount getGallon_liquid_us(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.GALLON_LIQUID_US)
        return x
	}
	
	// Define the gallon uk creator.
	static Amount getGallon_uk(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.GALLON_UK)
        return x
	}
	
	// Define the gauss creator.
	static Amount getGauss(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.GAUSS)
        return x
	}
	
	// Define the gilberts creator.
	static Amount getGilberts(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.GILBERT)
        return x
	}
	
	// Define the grades creator.
	static Amount getGrades(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.GRADE)
        return x
	}
	
	// Define the hectares creator.
	static Amount getHectares(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.HECTARE)
        return x
	}
	
	// Define the horsepower creator.
	static Amount getHorsepower(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.HORSEPOWER)
        return x
	}
	
	// Define the hours creator.
	static Amount getHours(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.HOUR)
        return x
	}
	
	// Define the inches creator.
	static Amount getInches(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.INCH)
        return x
	}
	
	// Define the inch_of_mercury creator.
	static Amount getInch_of_mercury(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.INCH_OF_MERCURY)
        return x
	}
	
	// Define the kilogram force creator.
	static Amount getKilogram_force(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.KILOGRAM_FORCE)
        return x
	}
	
	// Define the knots creator.
	static Amount getKnots(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.KNOT)
        return x
	}
	
	// Define the lamberts creator.
	static Amount getLamberts(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.LAMBERT)
        return x
	}
	
	// Define the light years creator.
	static Amount getLight_years(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.LIGHT_YEAR)
        return x
	}
	
	// Define the liters creator.
	static Amount getLiters(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.LITER)
        return x
	}
	
	// Define the mach creator.
	static Amount getMach(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.MACH)
        return x
	}
	
	// Define the maxwells creator.
	static Amount getMaxwells(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.MAXWELL)
        return x
	}
	
	// Define the metric tons creator.
	static Amount getMetric_tons(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.METRIC_TON)
        return x
	}
	
	// Define the miles creator.
	static Amount getMiles(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.MILE)
        return x
	}
	
	// Define the millimeters of mercury creator.
	static Amount getMillimeters_of_mercury(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.MILLIMETER_OF_MERCURY)
        return x
	}
	
	// Define the minutes creator.
	static Amount getMinutes(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.MINUTE)
        return x
	}
	
	// Define the minute angle creator.
	static Amount getMinute_angle(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.MINUTE_ANGLE)
        return x
	}
	
	// Define the months creator.
	static Amount getMonths(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.MONTH)
        return x
	}
	
	// Define the nautical miles creator.
	static Amount getNautical_miles(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.NAUTICAL_MILE)
        return x
	}
	
	// Define the octets creator.
	static Amount getOctets(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.OCTET)
        return x
	}
	
	// Define the ounces creator.
	static Amount getOunces(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.OUNCE)
        return x
	}
	
	// Define the ounce liquid uk creator.
	static Amount getOunce_liquid_uk(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.OUNCE_LIQUID_UK)
        return x
	}
	
	// Define the ounce liquid us creator.
	static Amount getOunce_liquid_us(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.OUNCE_LIQUID_US)
        return x
	}
	
	// Define the parsecs creator.
	static Amount getParsecs(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.PARSEC)
        return x
	}
	
	// Define the percent creator.
	static Amount getPercent(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.PERCENT)
        return x
	}
	
	// Define the pixels creator.
	static Amount getPixels(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.PIXEL)
        return x
	}
	
	// Define the points creator.
	static Amount getPoints(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.POINT)
        return x
	}
	
	// Define the poise creator.
	static Amount getPoise(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.POISE)
        return x
	}
	
	// Define the pound force creator.
	static Amount getPound_force(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.POUND_FORCE)
        return x
    }
	
	// Define the rads creator.
	static Amount getRads(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.RAD)
        return x
    }
	
	// Define the rankines creator.
	static Amount getRankines(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.RANKINE)
        return x
    }
	
	// Define the rems creator.
	static Amount getRems(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.REM)
        return x
    }
	
	// Define the revolutions creator.
	static Amount getRevolutions(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.REVOLUTION)
        return x
    }
	
	// Define the roentgens creator.
	static Amount getRoentgens(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.ROENTGEN)
        return x
    }
	
	// Define the rutherfords creator.
	static Amount getRutherfords(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.RUTHERFORD)
        return x
    }
	
	// Define the second angle creator.
	static Amount getSecond_angle(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.SECOND_ANGLE)
        return x
    }
	
	// Define the spheres creator.
	static Amount getSpheres(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.SPHERE)
        return x
    }
	
	// Define the stokes creator.
	static Amount getStokes(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.STOKE)
        return x
    }
	
	// Define the ton uk creator.
	static Amount getTon_uk(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.TON_UK)
        return x
    }
	
	// Define the ton us creator.
	static Amount getTon_us(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.TON_US)
        return x
    }
	
	// Define the weeks creator.
	static Amount getWeeks(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.WEEK)
        return x
    }
	
	// Define the yards creator.
	static Amount getYards(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.YARD)
        return x
    }
	
	// Define the years creator.
	static Amount getYears(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.YEAR)
        return x
    }
	
	// Define the year_calendar creator.
	static Amount getYear_calendar(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.YEAR_CALENDAR)
        return x
    }
	
	// Define the year_sidereal creator.
	static Amount getYear_sidereal(Integer amount) {
		Amount x = Amount.valueOf(amount, NonSI.YEAR_SIDEREAL)
        return x
    }
		
	static Amount plus(Amount amount1, Amount amount2) {
        return UnitsOperations.addition(amount1, amount2)
    }

	static Amount multiply(Amount amount1, Amount amount2) {
        return UnitsOperations.multiplication(amount1, amount2)
    }
	
	static Amount multiply(BigDecimal number, Amount amt) {
		return UnitsOperations.multiplication(number, amt)
	}
	
	static Amount multiply(Amount amt, BigDecimal number) {
		return UnitsOperations.multiplication(amt, number)
	}
	
	static Amount multiply(Integer number, Amount amt) {
		return UnitsOperations.multiplication(number, amt)
	}
	
	static Amount multiply(Amount amt, Integer number) {
		return UnitsOperations.multiplication(amt, number)
	}
	
	static Amount minus(Amount amount1, Amount amount2) {
        return UnitsOperations.subtraction(amount1, amount2)
    }

	static Amount div(Amount amount1, Amount amount2) {
        return UnitsOperations.division(amount1, amount2)
    }
	
	static Amount div(Amount amt, BigDecimal number) {
		return UnitsOperations.division(amt, number)
	}
	
	static Amount div(Amount amt, Integer number) {
		return UnitsOperations.division(amt, number)
	}
	
	static Amount power(Amount amt, Integer number) {
		return UnitsOperations.exponentiation(amt, number)
	}
	
	static Amount power(Amount amt, BigDecimal number) {
		return UnitsOperations.exponentiation(amt, number)
	}
	
	static Amount power(Amount amt, Amount amt2) {
		return UnitsOperations.exponentiation(amt, amt2)
	}

}