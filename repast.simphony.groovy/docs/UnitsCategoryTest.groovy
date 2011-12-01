package repast.simphony.groovy

import java.util.*
import javax.measure.unit.*
import org.jscience.physics.amount.*
import org.jscience.mathematics.number.* 

class UnitsCategoryTest extends GroovyTestCase {

	void testCreation() {

		use(UnitsCategory) {
			
			// Test decimeters.
			println("Creation Decimeters: " + 1.0.meters.deci);
			assert (1.0.meters.deci == 1.0.meters.deci)
			
			// Test dekameters.
			println("Creation Dekameters: " + 1.0.meters.deka);
			assert (1.0.meters.deka == 1.0.meters.deka)
			
			// Test centimeters.
			println("Creation Centimeters: " + 1.0.meters.centi);
			assert (1.0.meters.centi == 1.0.meters.centi)
			
			// Test hectometers.
			println("Creation Hectometers: " + 1.0.meters.hecto);
			assert (1.0.meters.hecto == 1.0.meters.hecto)
			
			// Test millimeters.
			println("Creation Millimeters: " + 1.0.meters.milli);
			assert (1.0.meters.milli == 1.0.meters.milli)
			
			// Test kilometers.
			println("Creation Kilometers: " + 1.0.meters.kilo);
			assert (1.0.meters.kilo == 1.0.meters.kilo)
			
			// Test micrometers.
			println("Creation Micrometers: " + 1.0.meters.micro);
			assert (1.0.meters.micro == 1.0.meters.micro)
			
			// Test megameters.
			println("Creation Megameters: " + 1.0.meters.mega);
			assert (1.0.meters.mega == 1.0.meters.mega)
			
			// Test nanometers.
			println("Creation Nanometers: " + 1.0.meters.nano);
			assert (1.0.meters.nano == 1.0.meters.nano)
			
			// Test gigameters.
			println("Creation Gigameters: " + 1.0.meters.giga);
			assert (1.0.meters.giga == 1.0.meters.giga)
			
			// Test picometers.
			println("Creation Picometers: " + 1.0.meters.pico);
			assert (1.0.meters.pico == 1.0.meters.pico)
			
			// Test terameters.
			println("Creation Terameters: " + 1.0.meters.tera);
			assert (1.0.meters.tera == 1.0.meters.tera)
			
			// Test femtometers.
			println("Creation Femtometers: " + 1.0.meters.femto);
			assert (1.0.meters.femto == 1.0.meters.femto)
			
			// Test petameters.
			println("Creation Petameters: " + 1.0.meters.peta);
			assert (1.0.meters.peta == 1.0.meters.peta)
			
			// Test attometers.
			println("Creation Attometers: " + 1.0.meters.atto);
			assert (1.0.meters.atto == 1.0.meters.atto)
			
			// Test exameters.
			println("Creation Exameters: " + 1.0.meters.exa);
			assert (1.0.meters.exa == 1.0.meters.exa)
			
			// Test zeptometers.
			println("Creation Zeptometers: " + 1.0.meters.zepto);
			assert (1.0.meters.zepto == 1.0.meters.zepto)
			
			// Test zettameters.
			println("Creation Zettameters: " + 1.0.meters.zetta);
			assert (1.0.meters.zetta == 1.0.meters.zetta)
			
			// Test yoctometers.
			println("Creation Yoctometers: " + 1.0.meters.yocto);
			assert (1.0.meters.yocto == 1.0.meters.yocto)
			
			// Test yottameters.
			println("Creation Yottameters: " + 1.0.meters.yotta);
			assert (1.0.meters.yotta == 1.0.meters.yotta)
			
			// Test meters.
			println("Creation Meters: " + 1.0.meters);
			assert (1.0.meters == 1.0.meters)
			
			// Test kilometers.
			println("Creation Kilometers: " + 1.0.kilometers);
			assert (1.0.kilometers == 1.0.kilometers)
			
			// Test centimeters.
			println("Creation Centimeters: " + 1.0.centimeters);
			assert (1.0.centimeters == 1.0.centimeters)
			
			// Test millimeters.
			println("Creation Millimeters: " + 1.0.millimeters);
			assert (1.0.millimeters == 1.0.millimeters)
			
			// Test micrometers.
			println("Creation Micrometers: " + 1.0.micrometers);
			assert (1.0.micrometers == 1.0.micrometers)
			
			// Test decimeters.
			println("Creation Decimeters: " + 1.0.decimeters);
			assert (1.0.decimeters == 1.0.decimeters)
			
			// Test kilograms.
			println("Creation Kilograms: " + 1.0.kilograms);
			assert (1.0.kilograms == 1.0.kilograms)
			
			// Test milligrams.
			println("Creation Milligrams: " + 1.0.milligrams);
			assert (1.0.milligrams == 1.0.milligrams)
			
			// Test angstroms.
			println("Creation Angstroms: " + 1.0.angstroms);
			assert (1.0.angstroms == 1.0.angstroms)
			
			// Test pounds.
			println("Creation Pounds: " + 1.0.pounds);
			assert (1.0.pounds == 1.0.pounds)
			
			// Test grams.
			println("Creation Grams: " + 1.0.grams);
			assert (1.0.grams == 1.0.grams)
			
			// Test amperes.
			println("Creation Amperes: " + 1.0.amperes);
			assert (1.0.amperes == 1.0.amperes)
			
			// Test becquerels.
			println("Creation Becquerels: " + 1.0.becquerels);
			assert (1.0.becquerels == 1.0.becquerels)
			
			// Test kilobecquerels.
			println("Creation Kilobecquerels: " + 1.0.kilobecquerels);
			assert (1.0.kilobecquerels == 1.0.kilobecquerels)
			
			// Test megabecquerels.
			println("Creation Megabecquerels: " + 1.0.megabecquerels);
			assert (1.0.megabecquerels == 1.0.megabecquerels)
			
			// Test gigabecquerels.
			println("Creation Gigabecquerels: " + 1.0.gigabecquerels);
			assert (1.0.gigabecquerels == 1.0.gigabecquerels)
			
			// Test bits.
			println("Creation Bits: " + 1.0.bits);
			assert (1.0.bits == 1.0.bits)
			
			// Test kilobits.
			println("Creation Kilobits: " + 1.0.kilobits);
			assert (1.0.kilobits == 1.0.kilobits)
			
			// Test megabits.
			println("Creation Megabits: " + 1.0.megabits);
			assert (1.0.megabits == 1.0.megabits)
			
			// Test gigabits.
			println("Creation Gigabits: " + 1.0.gigabits);
			assert (1.0.gigabits == 1.0.gigabits)
			
			// Test terabits.
			println("Creation Terabits: " + 1.0.terabits);
			assert (1.0.terabits == 1.0.terabits)
			
			// Test candelas.
			println("Creation Candelas: " + 1.0.candelas);
			assert (1.0.candelas == 1.0.candelas)
			
			// Test celsius.
			println("Creation Celsius: " + 1.0.celsius);
			assert (1.0.celsius == 1.0.celsius)
			
			// Test coulombs.
			println("Creation Coulombs: " + 1.0.coulombs);
			assert (1.0.coulombs == 1.0.coulombs)
			
			// Test cubic meters.
			println("Creation Cubic_meters: " + 1.0.cubic_meters);
			assert (1.0.cubic_meters == 1.0.cubic_meters)
			
			// Test farads.
			println("Creation Farads: " + 1.0.farads);
			assert (1.0.farads == 1.0.farads)
			
			// Test microfarads.
			println("Creation Microfarads: " + 1.0.microfarads);
			assert (1.0.microfarads == 1.0.microfarads)
			
			// Test nanofarads.
			println("Creation Nanofarads: " + 1.0.nanofarads);
			assert (1.0.nanofarads == 1.0.nanofarads)
			
			// Test picofarads.
			println("Creation Picofarads: " + 1.0.picofarads);
			assert (1.0.picofarads == 1.0.picofarads)
			
			// Test grays.
			println("Creation Grays: " + 1.0.grays);
			assert (1.0.grays == 1.0.grays)
			
			// Test milligrays.
			println("Creation Milligrays: " + 1.0.milligrays);
			assert (1.0.milligrays == 1.0.milligrays)
			
			// Test henrys.
			println("Creation Henrys: " + 1.0.henrys);
			assert (1.0.henrys == 1.0.henrys)
			
			// Test hertz.
			println("Creation Hertz: " + 1.0.hertz);
			assert (1.0.hertz == 1.0.hertz)
			
			// Test kilohertz.
			println("Creation Kilohertz: " + 1.0.kilohertz);
			assert (1.0.kilohertz == 1.0.kilohertz)
			
			// Test megahertz.
			println("Creation Megahertz: " + 1.0.megahertz);
			assert (1.0.megahertz == 1.0.megahertz)
			
			// Test joules.
			println("Creation Joules: " + 1.0.joules);
			assert (1.0.joules == 1.0.joules)
			
			// Test kilojoules.
			println("Creation Kilojoules: " + 1.0.kilojoules);
			assert (1.0.kilojoules == 1.0.kilojoules)
			
			// Test katals.
			println("Creation Katals: " + 1.0.katals);
			assert (1.0.katals == 1.0.katals)
			
			// Test kelvin.
			println("Creation Kelvin: " + 1.0.kelvin);
			assert (1.0.kelvin == 1.0.kelvin)
			
			// Test lumens.
			println("Creation Lumens: " + 1.0.lumens);
			assert (1.0.lumens == 1.0.lumens)
			
			// Test lux.
			println("Creation Lux: " + 1.0.lux);
			assert (1.0.lux == 1.0.lux)
			
			// Test microlux.
			println("Creation Microlux: " + 1.0.microlux);
			assert (1.0.microlux == 1.0.microlux)
			
			// Test millilux.
			println("Creation Millilux: " + 1.0.millilux);
			assert (1.0.millilux == 1.0.millilux)
			
			// Test kilolux.
			println("Creation Kilolux: " + 1.0.kilolux);
			assert (1.0.kilolux == 1.0.kilolux)
			
			// Test meter per second.
			println("Creation Meter_per_second: " + 1.0.meter_per_second);
			assert (1.0.meter_per_second == 1.0.meter_per_second)
			
			// Test meter per square second.
			println("Creation Meter_per_square_second: " + 1.0.meter_per_square_second);
			assert (1.0.meter_per_square_second == 1.0.meter_per_square_second)
			
			// Test moles.
			println("Creation Moles: " + 1.0.moles);
			assert (1.0.moles == 1.0.moles)
			
			// Test newtons.
			println("Creation Newtons: " + 1.0.newtons);
			assert (1.0.newtons == 1.0.newtons)
			
			// Test ohms.
			println("Creation Ohms: " + 1.0.ohms);
			assert (1.0.ohms == 1.0.ohms)
			
			// Test pascals.
			println("Creation Pascals: " + 1.0.pascals);
			assert (1.0.pascals == 1.0.pascals)
			
			// Test kilopascals.
			println("Creation Kilopascals: " + 1.0.kilopascals);
			assert (1.0.kilopascals == 1.0.kilopascals)
			
			// Test radians.
			println("Creation Radians: " + 1.0.radians);
			assert (1.0.radians == 1.0.radians)
			
			// Test milliradians.
			println("Creation Milliradians: " + 1.0.milliradians);
			assert (1.0.milliradians == 1.0.milliradians)
			
			// Test microradians.
			println("Creation Microradians: " + 1.0.microradians);
			assert (1.0.microradians == 1.0.microradians)
			
			// Test nanoradians.
			println("Creation Nanoradians: " + 1.0.nanoradians);
			assert (1.0.nanoradians == 1.0.nanoradians)
			
			// Test seconds.
			println("Creation Seconds: " + 1.0.seconds);
			assert (1.0.seconds == 1.0.seconds)
			
			// Test siemens.
			println("Creation Siemens: " + 1.0.siemens);
			assert (1.0.siemens == 1.0.siemens)
			
			// Test sieverts.
			println("Creation Sieverts: " + 1.0.sieverts);
			assert (1.0.sieverts == 1.0.sieverts)
			
			// Test millisieverts.
			println("Creation Millisieverts: " + 1.0.millisieverts);
			assert (1.0.millisieverts == 1.0.millisieverts)
			
			// Test microsieverts.
			println("Creation Microsieverts: " + 1.0.microsieverts);
			assert (1.0.microsieverts == 1.0.microsieverts)
			
			// Test square meters.
			println("Creation Square_meters: " + 1.0.square_meters);
			assert (1.0.square_meters == 1.0.square_meters)
			
			// Test steradians.
			println("Creation Steradians: " + 1.0.steradians);
			assert (1.0.steradians == 1.0.steradians)
			
			// Test teslas.
			println("Creation Teslas: " + 1.0.teslas);
			assert (1.0.teslas == 1.0.teslas)
			
			// Test volts.
			println("Creation Volts: " + 1.0.volts);
			assert (1.0.volts == 1.0.volts)
			
			// Test kilovolts.
			println("Creation Kilovolts: " + 1.0.kilovolts);
			assert (1.0.kilovolts == 1.0.kilovolts)
			
			// Test watts.
			println("Creation Watts: " + 1.0.watts);
			assert (1.0.watts == 1.0.watts)
			
			// Test kilowatts.
			println("Creation Kilowatts: " + 1.0.kilowatts);
			assert (1.0.kilowatts == 1.0.kilowatts)
			
			// Test megawatts.
			println("Creation Megawatts: " + 1.0.megawatts);
			assert (1.0.megawatts == 1.0.megawatts)
			
			// Test webers.
			println("Creation Webers: " + 1.0.webers);
			assert (1.0.webers == 1.0.webers)
			
			// Test ares.
			println("Creation Ares: " + 1.0.ares);
			assert (1.0.ares == 1.0.ares)
			
			// Test astronomical units.
			println("Creation Astronomical_units: " + 1.0.astronomical_units);
			assert (1.0.astronomical_units == 1.0.astronomical_units)
			
			// Test atmospheres.
			println("Creation Atmospheres: " + 1.0.atmospheres);
			assert (1.0.atmospheres == 1.0.atmospheres)
			
			// Test atoms.
			println("Creation Atoms: " + 1.0.atoms);
			assert (1.0.atoms == 1.0.atoms)
			
			// Test atomic mass.
			println("Creation Atomic_mass: " + 1.0.atomic_mass);
			assert (1.0.atomic_mass == 1.0.atomic_mass)
			
			// Test bars.
			println("Creation Bars: " + 1.0.bars);
			assert (1.0.bars == 1.0.bars)
			
			// Test bytes.
			println("Creation Bytes: " + 1.0.bytes);
			assert (1.0.bytes == 1.0.bytes)
			
			// Test c.
			println("Creation C: " + 1.0.c);
			assert (1.0.c == 1.0.c)
			
			// Test centiradians.
			println("Creation Centiradians: " + 1.0.centiradians);
			assert (1.0.centiradians == 1.0.centiradians)
			
			// Test computer point.
			println("Creation Computer_point: " + 1.0.computer_point);
			assert (1.0.computer_point == 1.0.computer_point)
			
			// Test cubic inches.
			println("Creation Cubic_inches: " + 1.0.cubic_inches);
			assert (1.0.cubic_inches == 1.0.cubic_inches)
			
			// Test curies.
			println("Creation Curies: " + 1.0.curies);
			assert (1.0.curies == 1.0.curies)
			
			// Test days.
			println("Creation Days: " + 1.0.days);
			assert (1.0.days == 1.0.days)
			
			// Test day sidereal.
			println("Creation Day_sidereal: " + 1.0.day_sidereal);
			assert (1.0.day_sidereal == 1.0.day_sidereal)
			
			// Test decibels.
			println("Creation Decibels: " + 1.0.decibels);
			assert (1.0.decibels == 1.0.decibels)
			
			// Test degree angle.
			println("Creation Degree_angle: " + 1.0.degree_angle);
			assert (1.0.degree_angle == 1.0.degree_angle)
			
			// Test dynes.
			println("Creation Dynes: " + 1.0.dynes);
			assert (1.0.dynes == 1.0.dynes)
			
			// Test e.
			println("Creation E: " + 1.0.e);
			assert (1.0.e == 1.0.e)
			
			// Test electron mass.
			println("Creation Electron_mass: " + 1.0.electron_mass);
			assert (1.0.electron_mass == 1.0.electron_mass)
			
			// Test electron volt.
			println("Creation Electron_volt: " + 1.0.electron_volt);
			assert (1.0.electron_volt == 1.0.electron_volt)
			
			// Test ergs.
			println("Creation Ergs: " + 1.0.ergs);
			assert (1.0.ergs == 1.0.ergs)
			
			// Test fahrenheit.
			println("Creation Fahrenheit: " + 1.0.fahrenheit);
			assert (1.0.fahrenheit == 1.0.fahrenheit)
			
			// Test faradays.
			println("Creation Faradays: " + 1.0.faradays);
			assert (1.0.faradays == 1.0.faradays)
			
			// Test feet.
			println("Creation Feet: " + 1.0.feet);
			assert (1.0.feet == 1.0.feet)
			
			// Test foot_survey_us.
			println("Creation Foot_survey_us: " + 1.0.foot_survey_us);
			assert (1.0.foot_survey_us == 1.0.foot_survey_us)
			
			// Test franklins.
			println("Creation Franklins: " + 1.0.franklins);
			assert (1.0.franklins == 1.0.franklins)
			
			// Test g.
			println("Creation G: " + 1.0.g);
			assert (1.0.g == 1.0.g)
			
			// Test gallon dry us.
			println("Creation Gallon_dry_us: " + 1.0.gallon_dry_us);
			assert (1.0.gallon_dry_us == 1.0.gallon_dry_us)
			
			// Test gallon liquid us.
			println("Creation Gallon_liquid_us: " + 1.0.gallon_liquid_us);
			assert (1.0.gallon_liquid_us == 1.0.gallon_liquid_us)
			
			// Test gallon uk.
			println("Creation Gallon_uk: " + 1.0.gallon_uk);
			assert (1.0.gallon_uk == 1.0.gallon_uk)
			
			// Test gauss.
			println("Creation Gauss: " + 1.0.gauss);
			assert (1.0.gauss == 1.0.gauss)
			
			// Test gilberts.
			println("Creation Gilberts: " + 1.0.gilberts);
			assert (1.0.gilberts == 1.0.gilberts)
			
			// Test grades.
			println("Creation Grades: " + 1.0.grades);
			assert (1.0.grades == 1.0.grades)
			
			// Test hectares.
			println("Creation Hectares: " + 1.0.hectares);
			assert (1.0.hectares == 1.0.hectares)
			
			// Test horsepower.
			println("Creation Horsepower: " + 1.0.horsepower);
			assert (1.0.horsepower == 1.0.horsepower)
			
			// Test hours.
			println("Creation Hours: " + 1.0.hours);
			assert (1.0.hours == 1.0.hours)
			
			// Test inches.
			println("Creation Inches: " + 1.0.inches);
			assert (1.0.inches == 1.0.inches)
			
			// Test inch of mercury.
			println("Creation Inch_of_mercury: " + 1.0.inch_of_mercury);
			assert (1.0.inch_of_mercury == 1.0.inch_of_mercury)
			
			// Test kilogram force.
			println("Creation Kilogram_force: " + 1.0.kilogram_force);
			assert (1.0.kilogram_force == 1.0.kilogram_force)
			
			// Test knots.
			println("Creation Knots: " + 1.0.knots);
			assert (1.0.knots == 1.0.knots)
			
			// Test lamberts.
			println("Creation Lamberts: " + 1.0.lamberts);
			assert (1.0.lamberts == 1.0.lamberts)
			
			// Test light years.
			println("Creation Light_years: " + 1.0.light_years);
			assert (1.0.light_years == 1.0.light_years)
			
			// Test liters.
			println("Creation Liters: " + 1.0.liters);
			assert (1.0.liters == 1.0.liters)
			
			// Test mach.
			println("Creation Mach: " + 1.0.mach);
			assert (1.0.mach == 1.0.mach)
			
			// Test maxwells.
			println("Creation Maxwells: " + 1.0.maxwells);
			assert (1.0.maxwells == 1.0.maxwells)
			
			// Test metric tons.
			println("Creation Metric_tons: " + 1.0.metric_tons);
			assert (1.0.metric_tons == 1.0.metric_tons)
			
			// Test miles.
			println("Creation Miles: " + 1.0.miles);
			assert (1.0.miles == 1.0.miles)
			
			// Test millimeters of mercury.
			println("Creation Millimeters_of_mercury: " + 1.0.millimeters_of_mercury);
			assert (1.0.millimeters_of_mercury == 1.0.millimeters_of_mercury)
			
			// Test minutes.
			println("Creation Minutes: " + 1.0.minutes);
			assert (1.0.minutes == 1.0.minutes)
			
			// Test minute angle.
			println("Creation Minute_angle: " + 1.0.minute_angle);
			assert (1.0.minute_angle == 1.0.minute_angle)
			
			// Test months.
			println("Creation Months: " + 1.0.months);
			assert (1.0.months == 1.0.months)
			
			// Test nautical miles.
			println("Creation Nautical_miles: " + 1.0.nautical_miles);
			assert (1.0.nautical_miles == 1.0.nautical_miles)
			
			// Test octets.
			println("Creation Octets: " + 1.0.octets);
			assert (1.0.octets == 1.0.octets)
			
			// Test ounces.
			println("Creation Ounces: " + 1.0.ounces);
			assert (1.0.ounces == 1.0.ounces)
			
			// Test ounce liquid uk.
			println("Creation Ounce_liquid_uk: " + 1.0.ounce_liquid_uk);
			assert (1.0.ounce_liquid_uk == 1.0.ounce_liquid_uk)
			
			// Test ounce liquid us.
			println("Creation Ounce_liquid_us: " + 1.0.ounce_liquid_us);
			assert (1.0.ounce_liquid_us == 1.0.ounce_liquid_us)
			
			// Test parsecs.
			println("Creation Parsecs: " + 1.0.parsecs);
			assert (1.0.parsecs == 1.0.parsecs)
			
			// Test percent.
			println("Creation Percent: " + 1.0.percent);
			assert (1.0.percent == 1.0.percent)
			
			// Test pixels.
			println("Creation Pixels: " + 1.0.pixels);
			assert (1.0.pixels == 1.0.pixels)
			
			// Test points.
			println("Creation Points: " + 1.0.points);
			assert (1.0.points == 1.0.points)
			
			// Test poise.
			println("Creation Poise: " + 1.0.poise);
			assert (1.0.poise == 1.0.poise)
			
			// Test pound force.
			println("Creation Pound_force: " + 1.0.pound_force);
			assert (1.0.pound_force == 1.0.pound_force)
			
			// Test rads.
			println("Creation Rads: " + 1.0.rads);
			assert (1.0.rads == 1.0.rads)
			
			// Test rankines.
			println("Creation Rankines: " + 1.0.rankines);
			assert (1.0.rankines == 1.0.rankines)
			
			// Test rems.
			println("Creation Rems: " + 1.0.rems);
			assert (1.0.rems == 1.0.rems)
			
			// Test revolutions.
			println("Creation Revolutions: " + 1.0.revolutions);
			assert (1.0.revolutions == 1.0.revolutions)
			
			// Test roentgens.
			println("Creation Roentgens: " + 1.0.roentgens);
			assert (1.0.roentgens == 1.0.roentgens)
			
			// Test rutherfords.
			println("Creation Rutherfords: " + 1.0.rutherfords);
			assert (1.0.rutherfords == 1.0.rutherfords)
			
			// Test second angle.
			println("Creation Second_angle: " + 1.0.second_angle);
			assert (1.0.second_angle == 1.0.second_angle)
			
			// Test spheres.
			println("Creation Spheres: " + 1.0.spheres);
			assert (1.0.spheres == 1.0.spheres)
			
			// Test stokes.
			println("Creation Stokes: " + 1.0.stokes);
			assert (1.0.stokes == 1.0.stokes)
			
			// Test ton uk.
			println("Creation Ton_uk: " + 1.0.ton_uk);
			assert (1.0.ton_uk == 1.0.ton_uk)
			
			// Test ton us.
			println("Creation Ton_us: " + 1.0.ton_us);
			assert (1.0.ton_us == 1.0.ton_us)
			
			// Test weeks.
			println("Creation Weeks: " + 1.0.weeks);
			assert (1.0.weeks == 1.0.weeks)
			
			// Test yards.
			println("Creation Yards: " + 1.0.yards);
			assert (1.0.yards == 1.0.yards)
			
			// Test years.
			println("Creation Years: " + 1.0.years);
			assert (1.0.years == 1.0.years)
			
			// Test year_calendar.
			println("Creation Year_calendar: " + 1.0.year_calendar);
			assert (1.0.year_calendar == 1.0.year_calendar)
			
			// Test year_sidereal.
			println("Creation Year_sidereal: " + 1.0.year_sidereal);
			assert (1.0.year_sidereal == 1.0.year_sidereal)
			
		}
		
	}
	
	void testAddition() {
				  
		use(UnitsCategory) {
		    	
			// Test meters.
			println("Addition Meters: " + 1.1.meters)
			assert (1.1.meters + 2.3.meters == 3.4.meters)
			
			// Test kilometers.
			println("Addition Kilometers: " + 1.1.kilometers)
			assert (1.1.kilometers + 2.3.kilometers == 3.4.kilometers)
			
			// Test centimeters.
			println("Addition Centimeters: " + 1.1.centimeters)
			assert (1.1.centimeters + 2.3.centimeters == 3.4.centimeters)
			
			// Test millimeters.
			println("Addition Millimeters: " + 1.1.millimeters)
			assert (1.1.millimeters + 2.3.millimeters == 3.4.millimeters)
			
			// Test micrometers.
			println("Addition Micrometers: " + 1.1.micrometers)
			assert (1.1.micrometers + 2.3.micrometers == 3.4.micrometers)
			
			// Test decimeters.
			println("Addition Decimeters: " + 1.1.decimeters)
			assert (1.1.decimeters + 2.3.decimeters == 3.4.decimeters)
			
			// Test kilograms.
			println("Addition Kilograms: " + 1.1.kilograms)
			assert (1.1.kilograms + 2.3.kilograms == 3.4.kilograms)
			
			// Test milligrams.
			println("Addition Milligrams: " + 1.1.milligrams)
			assert (1.1.milligrams + 2.3.milligrams == 3.4.milligrams)
			
			// Test angstroms.
			println("Addition Angstroms: " + 1.1.angstroms)
			assert (1.1.angstroms + 2.3.angstroms == 3.4.angstroms)
			
			// Test pounds.
			println("Addition Pounds: " + 1.1.pounds)
			assert (1.1.pounds + 2.3.pounds == 3.4.pounds)
			
			// Test grams.
			println("Addition Grams: " + 1.1.grams)
			assert (1.1.grams + 2.3.grams == 3.4.grams)
			
			// Test amperes.
			println("Addition Amperes: " + 1.1.amperes)
			assert (1.1.amperes + 2.3.amperes == 3.4.amperes)
			
			// Test becquerels.
			println("Addition Becquerels: " + 1.1.becquerels)
			assert (1.1.becquerels + 2.3.becquerels == 3.4.becquerels)
			
			// Test kilobecquerels.
			println("Addition Kilobecquerels: " + 1.1.kilobecquerels)
			assert (1.1.kilobecquerels + 2.3.kilobecquerels == 3.4.kilobecquerels)
			
			// Test megabecquerels.
			println("Addition Megabecquerels: " + 1.1.megabecquerels)
			assert (1.1.megabecquerels + 2.3.megabecquerels == 3.4.megabecquerels)
			
			// Test gigabecquerels.
			println("Addition Gigabecquerels: " + 1.1.gigabecquerels)
			assert (1.1.gigabecquerels + 2.3.gigabecquerels == 3.4.gigabecquerels)
			
			// Test bits.
			println("Addition Bits: " + 1.1.bits)
			assert (1.1.bits + 2.3.bits == 3.4.bits)
			
			// Test kilobits.
			println("Addition Kilobits: " + 1.1.kilobits)
			assert (1.1.kilobits + 2.3.kilobits == 3.4.kilobits)
			
			// Test megabits.
			println("Addition Megabits: " + 1.1.megabits)
			assert (1.1.megabits + 2.3.megabits == 3.4.megabits)
			
			// Test gigabits.
			println("Addition Gigabits: " + 1.1.gigabits)
			assert (1.1.gigabits + 2.3.gigabits == 3.4.gigabits)
			
			// Test terabits.
			println("Addition Terabits: " + 1.1.terabits)
			assert (1.1.terabits + 2.3.terabits == 3.4.terabits)
			
			// Test candelas.
			println("Addition Candelas: " + 1.1.candelas)
			assert (1.1.candelas + 2.3.candelas == 3.4.candelas)
			
			// Test celsius.
			println("Addition Celsius: " + 1.1.celsius)
			assert (1.1.celsius + 2.3.celsius == 3.4.celsius)
			
			// Test coulombs.
			println("Addition Coulombs: " + 1.1.coulombs)
			assert (1.1.coulombs + 2.3.coulombs == 3.4.coulombs)
			
			// Test cubic meters.
			println("Addition Cubic_meters: " + 1.1.cubic_meters)
			assert (1.1.cubic_meters + 2.3.cubic_meters == 3.4.cubic_meters)
			
			// Test farads.
			println("Addition Farads: " + 1.1.farads)
			assert (1.1.farads + 2.3.farads == 3.4.farads)
			
			// Test microfarads.
			println("Addition Microfarads: " + 1.1.microfarads)
			assert (1.1.microfarads + 2.3.microfarads == 3.4.microfarads)
			
			// Test nanofarads.
			println("Addition Nanofarads: " + 1.1.nanofarads)
			assert (1.1.nanofarads + 2.3.nanofarads == 3.4.nanofarads)
			
			// Test picofarads.
			println("Addition Picofarads: " + 1.1.picofarads)
			assert (1.1.picofarads + 2.3.picofarads == 3.4.picofarads)
			
			// Test grays.
			println("Addition Grays: " + 1.1.grays)
			assert (1.1.grays + 2.3.grays == 3.4.grays)
			
			// Test milligrays.
			println("Addition Milligrays: " + 1.1.milligrays)
			assert (1.1.milligrays + 2.3.milligrays == 3.4.milligrays)
			
			// Test henrys.
			println("Addition Henrys: " + 1.1.henrys)
			assert (1.1.henrys + 2.3.henrys == 3.4.henrys)
			
			// Test hertz.
			println("Addition Hertz: " + 1.1.hertz)
			assert (1.1.hertz + 2.3.hertz == 3.4.hertz)
			
			// Test kilohertz.
			println("Addition Kilohertz: " + 1.1.kilohertz)
			assert (1.1.kilohertz + 2.3.kilohertz == 3.4.kilohertz)
			
			// Test megahertz.
			println("Addition Megahertz: " + 1.1.megahertz)
			assert (1.1.megahertz + 2.3.megahertz == 3.4.megahertz)
			
			// Test joules.
			println("Addition Joules: " + 1.1.joules)
			assert (1.1.joules + 2.3.joules == 3.4.joules)
			
			// Test kilojoules.
			println("Addition Kilojoules: " + 1.1.kilojoules)
			assert (1.1.kilojoules + 2.3.kilojoules == 3.4.kilojoules)
			
			// Test katals.
			println("Addition Katals: " + 1.1.katals)
			assert (1.1.katals + 2.3.katals == 3.4.katals)
			
			// Test kelvin.
			println("Addition Kelvin: " + 1.1.kelvin)
			assert (1.1.kelvin + 2.3.kelvin == 3.4.kelvin)
			
			// Test lumens.
			println("Addition Lumens: " + 1.1.lumens)
			assert (1.1.lumens + 2.3.lumens == 3.4.lumens)
			
			// Test lux.
			println("Addition Lux: " + 1.1.lux)
			assert (1.1.lux + 2.3.lux == 3.4.lux)
			
			// Test microlux.
			println("Addition Microlux: " + 1.1.microlux)
			assert (1.1.microlux + 2.3.microlux == 3.4.microlux)
			
			// Test millilux.
			println("Addition Millilux: " + 1.1.millilux)
			assert (1.1.millilux + 2.3.millilux == 3.4.millilux)
			
			// Test kilolux.
			println("Addition Kilolux: " + 1.1.kilolux)
			assert (1.1.kilolux + 2.3.kilolux == 3.4.kilolux)
			
			// Test meter per second.
			println("Addition Meter_per_second: " + 1.1.meter_per_second)
			assert (1.1.meter_per_second + 2.3.meter_per_second == 3.4.meter_per_second)
			
			// Test meter per square second.
			println("Addition Meter_per_square_second: " + 1.1.meter_per_square_second)
			assert (1.1.meter_per_square_second + 2.3.meter_per_square_second == 3.4.meter_per_square_second)
			
			// Test moles.
			println("Addition Moles: " + 1.1.moles)
			assert (1.1.moles + 2.3.moles == 3.4.moles)
			
			// Test newtons.
			println("Addition Newtons: " + 1.1.newtons)
			assert (1.1.newtons + 2.3.newtons == 3.4.newtons)
			
			// Test ohms.
			println("Addition Ohms: " + 1.1.ohms)
			assert (1.1.ohms + 2.3.ohms == 3.4.ohms)
			
			// Test pascals.
			println("Addition Pascals: " + 1.1.pascals)
			assert (1.1.pascals + 2.3.pascals == 3.4.pascals)
			
			// Test kilopascals.
			println("Addition Kilopascals: " + 1.1.kilopascals)
			assert (1.1.kilopascals + 2.3.kilopascals == 3.4.kilopascals)
			
			// Test radians.
			println("Addition Radians: " + 1.1.radians)
			assert (1.1.radians + 2.3.radians == 3.4.radians)
			
			// Test milliradians.
			println("Addition Milliradians: " + 1.1.milliradians)
			assert (1.1.milliradians + 2.3.milliradians == 3.4.milliradians)
			
			// Test microradians.
			println("Addition Microradians: " + 1.1.microradians)
			assert (1.1.microradians + 2.3.microradians == 3.4.microradians)
			
			// Test nanoradians.
			println("Addition Nanoradians: " + 1.1.nanoradians)
			assert (1.1.nanoradians + 2.3.nanoradians == 3.4.nanoradians)
			
			// Test seconds.
			println("Addition Seconds: " + 1.1.seconds)
			assert (1.1.seconds + 2.3.seconds == 3.4.seconds)
			
			// Test siemens.
			println("Addition Siemens: " + 1.1.siemens)
			assert (1.1.siemens + 2.3.siemens == 3.4.siemens)
			
			// Test sieverts.
			println("Addition Sieverts: " + 1.1.sieverts)
			assert (1.1.sieverts + 2.3.sieverts == 3.4.sieverts)
			
			// Test millisieverts.
			println("Addition Millisieverts: " + 1.1.millisieverts)
			assert (1.1.millisieverts + 2.3.millisieverts == 3.4.millisieverts)
			
			// Test microsieverts.
			println("Addition Microsieverts: " + 1.1.microsieverts)
			assert (1.1.microsieverts + 2.3.microsieverts == 3.4.microsieverts)
			
			// Test square meters.
			println("Addition Square_meters: " + 1.1.square_meters)
			assert (1.1.square_meters + 2.3.square_meters == 3.4.square_meters)
			
			// Test steradians.
			println("Addition Steradians: " + 1.1.steradians)
			assert (1.1.steradians + 2.3.steradians == 3.4.steradians)
			
			// Test teslas.
			println("Addition Teslas: " + 1.1.teslas)
			assert (1.1.teslas + 2.3.teslas == 3.4.teslas)
			
			// Test volts.
			println("Addition Volts: " + 1.1.volts)
			assert (1.1.volts + 2.3.volts == 3.4.volts)
			
			// Test kilovolts.
			println("Addition Kilovolts: " + 1.1.kilovolts)
			assert (1.1.kilovolts + 2.3.kilovolts == 3.4.kilovolts)
			
			// Test watts.
			println("Addition Watts: " + 1.1.watts)
			assert (1.1.watts + 2.3.watts == 3.4.watts)
			
			// Test kilowatts.
			println("Addition Kilowatts: " + 1.1.kilowatts)
			assert (1.1.kilowatts + 2.3.kilowatts == 3.4.kilowatts)
			
			// Test megawatts.
			println("Addition Megawatts: " + 1.1.megawatts)
			assert (1.1.megawatts + 2.3.megawatts == 3.4.megawatts)
			
			// Test webers.
			println("Addition Webers: " + 1.1.webers)
			assert (1.1.webers + 2.3.webers == 3.4.webers)
			
			// Test ares.
			println("Addition Ares: " + 1.1.ares)
			assert (1.1.ares + 2.3.ares == 3.4.ares)
			
			// Test astronomical_units.
			println("Addition Astronomical_units: " + 1.1.astronomical_units)
			assert (1.1.astronomical_units + 2.3.astronomical_units == 3.4.astronomical_units)
			
			// Test atmospheres.
			println("Addition Atmospheres: " + 1.1.atmospheres)
			assert (1.1.atmospheres + 2.3.atmospheres == 3.4.atmospheres)
			
			// Test atoms.
			println("Addition Atoms: " + 1.1.atoms)
			assert (1.1.atoms + 2.3.atoms == 3.4.atoms)
			
			// Test atomic mass.
			println("Addition Atomic_mass: " + 1.1.atomic_mass)
			assert (1.1.atomic_mass + 2.3.atomic_mass == 3.4.atomic_mass)
			
			// Test bars.
			println("Addition Bars: " + 1.1.bars)
			assert (1.1.bars + 2.3.bars == 3.4.bars)
			
			// Test bytes.
			println("Addition Bytes: " + 1.1.bytes)
			assert (1.1.bytes + 2.3.bytes == 3.4.bytes)
			
			// Test c.
			println("Addition C: " + 1.1.c)
			assert (1.1.c + 2.3.c == 3.4.c)
			
			// Test centiradians.
			println("Addition Centiradians: " + 1.1.centiradians)
			assert (1.1.centiradians + 2.3.centiradians == 3.4.centiradians)
			
			// Test computer_point.
			println("Addition Computer_point: " + 1.1.computer_point)
			assert (1.1.computer_point + 2.3.computer_point == 3.4.computer_point)
			
			// Test cubic inches.
			println("Addition Cubic_inches: " + 1.1.cubic_inches)
			assert (1.1.cubic_inches + 2.3.cubic_inches == 3.4.cubic_inches)
			
			// Test curies.
			println("Addition Curies: " + 1.1.curies)
			assert (1.1.curies + 2.3.curies == 3.4.curies)
			
			// Test days.
			println("Addition Days: " + 1.1.days)
			assert (1.1.days + 2.3.days == 3.4.days)
			
			// Test day sidereal.
			println("Addition Day_sidereal: " + 1.1.day_sidereal)
			assert (1.1.day_sidereal + 2.3.day_sidereal == 3.4.day_sidereal)
			
			// Test decibels.
			println("Addition Decibels: " + 1.1.decibels)
			assert (1.1.decibels + 2.3.decibels == 3.4.decibels)
			
			// Test degree angle.
			println("Addition Degree_angle: " + 1.1.degree_angle)
			assert (1.1.degree_angle + 2.3.degree_angle == 3.4.degree_angle)
			
			// Test dynes.
			println("Addition Dynes: " + 1.1.dynes)
			assert (1.1.dynes + 2.3.dynes == 3.4.dynes)
			
			// Test e.
			println("Addition E: " + 1.1.e)
			assert (1.1.e + 2.3.e == 3.4.e)
			
			// Test electron mass.
			println("Addition Electron_mass: " + 1.1.electron_mass)
			assert (1.1.electron_mass + 2.3.electron_mass == 3.4.electron_mass)
			
			// Test electron volt.
			println("Addition Electron_volt: " + 1.1.electron_volt)
			assert (1.1.electron_volt + 2.3.electron_volt == 3.4.electron_volt)
			
			// Test ergs.
			println("Addition Ergs: " + 1.1.ergs)
			assert (1.1.ergs + 2.3.ergs == 3.4.ergs)
			
			// Test fahrenheit.
			println("Addition Fahrenheit: " + 1.1.fahrenheit)
			assert (1.1.fahrenheit + 2.3.fahrenheit == 3.4.fahrenheit)
			
			// Test faradays.
			println("Addition Faradays: " + 1.1.faradays)
			assert (1.1.faradays + 2.3.faradays == 3.4.faradays)
			
			// Test feet.
			println("Addition Feet: " + 1.1.feet)
			assert (1.1.feet + 2.3.feet == 3.4.feet)
			
			// Test foot_survey_us.
			println("Addition Foot_survey_us: " + 1.1.foot_survey_us)
			assert (1.1.foot_survey_us + 2.3.foot_survey_us == 3.4.foot_survey_us)
			
			// Test franklins.
			println("Addition Franklins: " + 1.1.franklins)
			assert (1.1.franklins + 2.3.franklins == 3.4.franklins)
			
			// Test g.
			println("Addition G: " + 1.1.g)
			assert (1.1.g + 2.3.g == 3.4.g)
			
			// Test gallon dry us.
			println("Addition Gallon_dry_us: " + 1.1.gallon_dry_us)
			assert (1.1.gallon_dry_us + 2.3.gallon_dry_us == 3.4.gallon_dry_us)
			
			// Test gallon liquid us.
			println("Addition Gallon_liquid_us: " + 1.1.gallon_liquid_us)
			assert (1.1.gallon_liquid_us + 2.3.gallon_liquid_us == 3.4.gallon_liquid_us)
			
			// Test gallon uk.
			println("Addition Gallon_uk: " + 1.1.gallon_uk)
			assert (1.1.gallon_uk + 2.3.gallon_uk == 3.4.gallon_uk)
			
			// Test gauss.
			println("Addition Gauss: " + 1.1.gauss)
			assert (1.1.gauss + 2.3.gauss == 3.4.gauss)
			
			// Test gilberts.
			println("Addition Gilberts: " + 1.1.gilberts)
			assert (1.1.gilberts + 2.3.gilberts == 3.4.gilberts)
			
			// Test grades.
			println("Addition Grades: " + 1.1.grades)
			assert (1.1.grades + 2.3.grades == 3.4.grades)
			
			// Test hectares.
			println("Addition Hectares: " + 1.1.hectares)
			assert (1.1.hectares + 2.3.hectares == 3.4.hectares)
			
			// Test horsepower.
			println("Addition Horsepower: " + 1.1.horsepower)
			assert (1.1.horsepower + 2.3.horsepower == 3.4.horsepower)
			
			// Test hours.
			println("Addition Hours: " + 1.1.hours)
			assert (1.1.hours + 2.3.hours == 3.4.hours)
			
			// Test inches.
			println("Addition Inches: " + 1.1.inches)
			assert (1.1.inches + 2.3.inches == 3.4.inches)
			
			// Test inch of mercury.
			println("Addition Inch_of_mercury: " + 1.1.inch_of_mercury)
			assert (1.1.inch_of_mercury + 2.3.inch_of_mercury == 3.4.inch_of_mercury)
			
			// Test kilogram force.
			println("Addition Kilogram_force: " + 1.1.kilogram_force)
			assert (1.1.kilogram_force + 2.3.kilogram_force == 3.4.kilogram_force)
			
			// Test knots.
			println("Addition Knots: " + 1.1.knots)
			assert (1.1.knots + 2.3.knots == 3.4.knots)
			
			// Test lamberts.
			println("Addition Lamberts: " + 1.1.lamberts)
			assert (1.1.lamberts + 2.3.lamberts == 3.4.lamberts)
			
			// Test light years.
			println("Addition Light_years: " + 1.1.light_years)
			assert (1.1.light_years + 2.3.light_years == 3.4.light_years)
			
			// Test liters.
			println("Addition Liters: " + 1.1.liters)
			assert (1.1.liters + 2.3.liters == 3.4.liters)
			
			// Test mach.
			println("Addition Mach: " + 1.1.mach)
			assert (1.1.mach + 2.3.mach == 3.4.mach)
			
			// Test maxwells.
			println("Addition Maxwells: " + 1.1.maxwells)
			assert (1.1.maxwells + 2.3.maxwells == 3.4.maxwells)
			
			// Test metric tons.
			println("Addition Metric_tons: " + 1.1.metric_tons)
			assert (1.1.metric_tons + 2.3.metric_tons == 3.4.metric_tons)
			
			// Test miles.
			println("Addition Miles: " + 1.1.miles)
			assert (1.1.miles + 2.3.miles == 3.4.miles)
			
			// Test millimeters of mercury.
			println("Addition Millimeters_of_mercury: " + 1.1.millimeters_of_mercury)
			assert (1.1.millimeters_of_mercury + 2.3.millimeters_of_mercury == 3.4.millimeters_of_mercury)
			
			// Test minutes.
			println("Addition Minutes: " + 1.1.minutes)
			assert (1.1.minutes + 2.3.minutes == 3.4.minutes)
			
			// Test minute angle.
			println("Addition Minute_angle: " + 1.1.minute_angle)
			assert (1.1.minute_angle + 2.3.minute_angle == 3.4.minute_angle)
			
			// Test months.
			println("Addition Months: " + 1.1.months)
			assert (1.1.months + 2.3.months == 3.4.months)
			
			// Test nautical miles.
			println("Addition Nautical_miles: " + 1.1.nautical_miles)
			assert (1.1.nautical_miles + 2.3.nautical_miles == 3.4.nautical_miles)
			
			// Test octets.
			println("Addition Octets: " + 1.1.octets)
			assert (1.1.octets + 2.3.octets == 3.4.octets)
			
			// Test ounces.
			println("Addition Ounces: " + 1.1.ounces)
			assert (1.1.ounces + 2.3.ounces == 3.4.ounces)
			
			// Test ounce liquid uk.
			println("Addition Ounce_liquid_uk: " + 1.1.ounce_liquid_uk)
			assert (1.1.ounce_liquid_uk + 2.3.ounce_liquid_uk == 3.4.ounce_liquid_uk)
			
			// Test ounce liquid us.
			println("Addition Ounce_liquid_us: " + 1.1.ounce_liquid_us)
			assert (1.1.ounce_liquid_us + 2.3.ounce_liquid_us == 3.4.ounce_liquid_us)
			
			// Test parsecs.
			println("Addition Parsecs: " + 1.1.parsecs)
			assert (1.1.parsecs + 2.3.parsecs == 3.4.parsecs)
			
			// Test percent.
			println("Addition Percent: " + 1.1.percent)
			assert (1.1.percent + 2.3.percent == 3.4.percent)
			
			// Test pixels.
			println("Addition Pixels: " + 1.1.pixels)
			assert (1.1.pixels + 2.3.pixels == 3.4.pixels)
			
			// Test points.
			println("Addition Points: " + 1.1.points)
			assert (1.1.points + 2.3.points == 3.4.points)
			
			// Test poise.
			println("Addition Poise: " + 1.1.poise)
			assert (1.1.poise + 2.3.poise == 3.4.poise)
			
			// Test pound force.
			println("Addition Pound_force: " + 1.1.pound_force)
			assert (1.1.pound_force + 2.3.pound_force == 3.4.pound_force)
			
			// Test rads.
			println("Addition Rads: " + 1.1.rads)
			assert (1.1.rads + 2.3.rads == 3.4.rads)
			
			// Test rankines.
			println("Addition Rankines: " + 1.1.rankines)
			assert (1.1.rankines + 2.3.rankines == 3.4.rankines)
			
			// Test rems.
			println("Addition Rems: " + 1.1.rems)
			assert (1.1.rems + 2.3.rems == 3.4.rems)
			
			// Test revolutions.
			println("Addition Revolutions: " + 1.1.revolutions)
			assert (1.1.revolutions + 2.3.revolutions == 3.4.revolutions)
			
			// Test roentgens.
			println("Addition Roentgens: " + 1.1.roentgens)
			assert (1.1.roentgens + 2.3.roentgens == 3.4.roentgens)
			
			// Test rutherfords.
			println("Addition Rutherfords: " + 1.1.rutherfords)
			assert (1.1.rutherfords + 2.3.rutherfords == 3.4.rutherfords)
			
			// Test second angle.
			println("Addition Second_angle: " + 1.1.second_angle)
			assert (1.1.second_angle + 2.3.second_angle == 3.4.second_angle)
			
			// Test spheres.
			println("Addition Spheres: " + 1.1.spheres)
			assert (1.1.spheres + 2.3.spheres == 3.4.spheres)
			
			// Test stokes.
			println("Addition Stokes: " + 1.1.stokes)
			assert (1.1.stokes + 2.3.stokes == 3.4.stokes)
			
			// Test ton uk.
			println("Addition Ton_uk: " + 1.1.ton_uk)
			assert (1.1.ton_uk + 2.3.ton_uk == 3.4.ton_uk)
			
			// Test ton us.
			println("Addition Ton_us: " + 1.1.ton_us)
			assert (1.1.ton_us + 2.3.ton_us == 3.4.ton_us)
			
			// Test weeks.
			println("Addition Weeks: " + 1.1.weeks)
			assert (1.1.weeks + 2.3.weeks == 3.4.weeks)
			
			// Test yards.
			println("Addition Yards: " + 1.1.yards)
			assert (1.1.yards + 2.3.yards == 3.4.yards)
			
			// Test years.
			println("Addition Years: " + 1.1.years)
			assert (1.1.years + 2.3.years == 3.4.years)
			
			// Test year_calendar.
			println("Addition Year_calendar: " + 1.1.year_calendar)
			assert (1.1.year_calendar + 2.3.year_calendar == 3.4.year_calendar)
			
			// Test year_sidereal.
			println("Addition Year_sidereal: " + 1.1.year_sidereal)
			assert (1.1.year_sidereal + 2.3.year_sidereal == 3.4.year_sidereal)
					      
		}
		    
	}

	void testMultiplication() {
		  
		use(UnitsCategory) {
		    
			//  Test meters.
			println("Multiplication Meters: " +  1.1.meters * 2.3.meters)
			assert (1.1.meters * 2.3.meters == 2.3.meters * 1.1.meters)
			
			//  Test kilometers.
			println("Multiplication Kilometers: " +  1.1.kilometers * 2.3.kilometers)
			assert (1.1.kilometers * 2.3.kilometers == 2.3.kilometers * 1.1.kilometers)
			
			//  Test centimeters.
			println("Multiplication Centimeters: " +  1.1.centimeters * 2.3.centimeters)
			assert (1.1.centimeters * 2.3.centimeters == 2.3.centimeters * 1.1.centimeters)
			
			//  Test millimeters.
			println("Multiplication Millimeters: " +  1.1.millimeters * 2.3.millimeters)
			assert (1.1.millimeters * 2.3.millimeters == 2.3.millimeters * 1.1.millimeters)
			
			//  Test micrometers.
			println("Multiplication Micrometers: " +  1.1.micrometers * 2.3.micrometers)
			assert (1.1.micrometers * 2.3.micrometers == 2.3.micrometers * 1.1.micrometers)
			
			//  Test decimeters.
			println("Multiplication Decimeters: " +  1.1.decimeters * 2.3.decimeters)
			assert (1.1.decimeters * 2.3.decimeters == 2.3.decimeters * 1.1.decimeters)
			
			// Test kilograms.
			println("Multiplication Kilograms: " +  1.1.kilograms * 2.3.kilograms)
			assert (1.1.kilograms * 2.3.kilograms == 2.3.kilograms * 1.1.kilograms)
			
			// Test milligrams.
			println("Multiplication Milligrams: " +  1.1.milligrams * 2.3.milligrams)
			assert (1.1.milligrams * 2.3.milligrams == 2.3.milligrams * 1.1.milligrams)
			
			// Test angstroms.
			println("Multiplication Angstroms: " +  1.1.angstroms * 2.3.angstroms)
			assert (1.1.angstroms * 2.3.angstroms == 2.3.angstroms * 1.1.angstroms)
			
			// Test pounds.
			println("Multiplication Pounds: " +  1.1.pounds * 2.3.pounds)
			assert (1.1.pounds * 2.3.pounds == 2.3.pounds * 1.1.pounds)
			
			// Test grams.
			println("Multiplication Grams: " +  1.1.grams * 2.3.grams)
			assert (1.1.grams * 2.3.grams == 2.3.grams * 1.1.grams)
			
			// Test amperes.
			println("Multiplication Amperes: " +  1.1.amperes * 2.3.amperes)
			assert (1.1.amperes * 2.3.amperes == 2.3.amperes * 1.1.amperes)
			
			// Test becquerels.
			println("Multiplication Becquerels: " +  1.1.becquerels * 2.3.becquerels)
			assert (1.1.becquerels * 2.3.becquerels == 2.3.becquerels * 1.1.becquerels)
			
			// Test kilobecquerels.
			println("Multiplication Kilobecquerels: " +  1.1.kilobecquerels * 2.3.kilobecquerels)
			assert (1.1.kilobecquerels * 2.3.kilobecquerels == 2.3.kilobecquerels * 1.1.kilobecquerels)
			
			// Test megabecquerels.
			println("Multiplication Megabecquerels: " +  1.1.megabecquerels * 2.3.megabecquerels)
			assert (1.1.megabecquerels * 2.3.megabecquerels == 2.3.megabecquerels * 1.1.megabecquerels)
			
			// Test gigabecquerels.
			println("Multiplication Gigabecquerels: " +  1.1.gigabecquerels * 2.3.gigabecquerels)
			assert (1.1.gigabecquerels * 2.3.gigabecquerels == 2.3.gigabecquerels * 1.1.gigabecquerels)
			
			// Test bits.
			println("Multiplication Bits: " +  1.1.bits * 2.3.bits)
			assert (1.1.bits * 2.3.bits == 2.3.bits * 1.1.bits)
			
			// Test kilobits.
			println("Multiplication Kilobits: " +  1.1.kilobits * 2.3.kilobits)
			assert (1.1.kilobits * 2.3.kilobits == 2.3.kilobits * 1.1.kilobits)
			
			// Test megabits.
			println("Multiplication Megabits: " +  1.1.megabits * 2.3.megabits)
			assert (1.1.megabits * 2.3.megabits == 2.3.megabits * 1.1.megabits)
			
			// Test gigabits.
			println("Multiplication Gigabits: " +  1.1.gigabits * 2.3.gigabits)
			assert (1.1.gigabits * 2.3.gigabits == 2.3.gigabits * 1.1.gigabits)
			
			// Test terabits.
			println("Multiplication Terabits: " +  1.1.terabits * 2.3.terabits)
			assert (1.1.terabits * 2.3.terabits == 2.3.terabits * 1.1.terabits)
			
			// Test candelas.
			println("Multiplication Candelas: " +  1.1.candelas * 2.3.candelas)
			assert (1.1.candelas * 2.3.candelas == 2.3.candelas * 1.1.candelas)
			
			// Test celsius.
			println("Multiplication Celsius: " +  1.1.celsius * 2.3.celsius)
			assert (1.1.celsius * 2.3.celsius == 2.3.celsius * 1.1.celsius)
			
			// Test coulombs.
			println("Multiplication Coulombs: " +  1.1.coulombs * 2.3.coulombs)
			assert (1.1.coulombs * 2.3.coulombs == 2.3.coulombs * 1.1.coulombs)
			
			// Test cubic meters.
			println("Multiplication Cubic_meters: " +  1.1.cubic_meters * 2.3.cubic_meters)
			assert (1.1.cubic_meters * 2.3.cubic_meters == 2.3.cubic_meters * 1.1.cubic_meters)
			
			// Test farads.
			println("Multiplication Farads: " +  1.1.farads * 2.3.farads)
			assert (1.1.farads * 2.3.farads == 2.3.farads * 1.1.farads)
			
			// Test microfarads.
			println("Multiplication Microfarads: " +  1.1.microfarads * 2.3.microfarads)
			assert (1.1.microfarads * 2.3.microfarads == 2.3.microfarads * 1.1.microfarads)
			
			// Test nanofarads.
			println("Multiplication Nanofarads: " +  1.1.nanofarads * 2.3.nanofarads)
			assert (1.1.nanofarads * 2.3.nanofarads == 2.3.nanofarads * 1.1.nanofarads)
			
			// Test picofarads.
			println("Multiplication Picofarads: " +  1.1.picofarads * 2.3.picofarads)
			assert (1.1.picofarads * 2.3.picofarads == 2.3.picofarads * 1.1.picofarads)
			
			// Test grays.
			println("Multiplication Grays: " +  1.1.grays * 2.3.grays)
			assert (1.1.grays * 2.3.grays == 2.3.grays * 1.1.grays)
			
			// Test milligrays.
			println("Multiplication Milligrays: " +  1.1.milligrays * 2.3.milligrays)
			assert (1.1.milligrays * 2.3.milligrays == 2.3.milligrays * 1.1.milligrays)
			
			// Test henrys.
			println("Multiplication Henrys: " +  1.1.henrys * 2.3.henrys)
			assert (1.1.henrys * 2.3.henrys == 2.3.henrys * 1.1.henrys)
			
			// Test hertz.
			println("Multiplication Hertz: " +  1.1.hertz * 2.3.hertz)
			assert (1.1.hertz * 2.3.hertz == 2.3.hertz * 1.1.hertz)
			
			// Test kilohertz.
			println("Multiplication Kilohertz: " +  1.1.kilohertz * 2.3.kilohertz)
			assert (1.1.kilohertz * 2.3.kilohertz == 2.3.kilohertz * 1.1.kilohertz)
			
			// Test megahertz.
			println("Multiplication Megahertz: " +  1.1.megahertz * 2.3.megahertz)
			assert (1.1.megahertz * 2.3.megahertz == 2.3.megahertz * 1.1.megahertz)
			
			// Test joules.
			println("Multiplication Joules: " +  1.1.joules * 2.3.joules)
			assert (1.1.joules * 2.3.joules == 2.3.joules * 1.1.joules)
			
			// Test kilojoules.
			println("Multiplication Kilojoules: " +  1.1.kilojoules * 2.3.kilojoules)
			assert (1.1.kilojoules * 2.3.kilojoules == 2.3.kilojoules * 1.1.kilojoules)
			
			// Test katals.
			println("Multiplication Katals: " +  1.1.katals * 2.3.katals)
			assert (1.1.katals * 2.3.katals == 2.3.katals * 1.1.katals)
			
			// Test kelvin.
			println("Multiplication Kelvin: " +  1.1.kelvin * 2.3.kelvin)
			assert (1.1.kelvin * 2.3.kelvin == 2.3.kelvin * 1.1.kelvin)
			
			// Test lumens.
			println("Multiplication Lumens: " +  1.1.lumens * 2.3.lumens)
			assert (1.1.lumens * 2.3.lumens == 2.3.lumens * 1.1.lumens)
			
			// Test lux.
			println("Multiplication Lux: " +  1.1.lux * 2.3.lux)
			assert (1.1.lux * 2.3.lux == 2.3.lux * 1.1.lux)
			
			// Test microlux.
			println("Multiplication Microlux: " +  1.1.microlux * 2.3.microlux)
			assert (1.1.microlux * 2.3.microlux == 2.3.microlux * 1.1.microlux)
			
			// Test millilux.
			println("Multiplication Millilux: " +  1.1.millilux * 2.3.millilux)
			assert (1.1.millilux * 2.3.millilux == 2.3.millilux * 1.1.millilux)
			
			// Test kilolux.
			println("Multiplication Kilolux: " +  1.1.kilolux * 2.3.kilolux)
			assert (1.1.kilolux * 2.3.kilolux == 2.3.kilolux * 1.1.kilolux)
			
			// Test meter per second.
			println("Multiplication Meter_per_second: " +  1.1.meter_per_second * 2.3.meter_per_second)
			assert (1.1.meter_per_second * 2.3.meter_per_second == 2.3.meter_per_second * 1.1.meter_per_second)
			
			// Test meter per square second.
			println("Multiplication Meter_per_square_second: " +  1.1.meter_per_square_second * 2.3.meter_per_square_second)
			assert (1.1.meter_per_square_second * 2.3.meter_per_square_second == 2.3.meter_per_square_second * 1.1.meter_per_square_second)
			
			// Test moles.
			println("Multiplication Moles: " +  1.1.moles * 2.3.moles)
			assert (1.1.moles * 2.3.moles == 2.3.moles * 1.1.moles)
			
			// Test newtons.
			println("Multiplication Newtons: " +  1.1.newtons * 2.3.newtons)
			assert (1.1.newtons * 2.3.newtons == 2.3.newtons * 1.1.newtons)
			
			// Test ohms.
			println("Multiplication Ohms: " +  1.1.ohms * 2.3.ohms)
			assert (1.1.ohms * 2.3.ohms == 2.3.ohms * 1.1.ohms)
			
			// Test pascals.
			println("Multiplication Pascals: " +  1.1.pascals * 2.3.pascals)
			assert (1.1.pascals * 2.3.pascals == 2.3.pascals * 1.1.pascals)
			
			// Test kilopascals.
			println("Multiplication Kilopascals: " +  1.1.kilopascals * 2.3.kilopascals)
			assert (1.1.kilopascals * 2.3.kilopascals == 2.3.kilopascals * 1.1.kilopascals)
			
			// Test radians.
			println("Multiplication Radians: " +  1.1.radians * 2.3.radians)
			assert (1.1.radians * 2.3.radians == 2.3.radians * 1.1.radians)
			
			// Test milliradians.
			println("Multiplication Milliradians: " +  1.1.milliradians * 2.3.milliradians)
			assert (1.1.milliradians * 2.3.milliradians == 2.3.milliradians * 1.1.milliradians)
			
			// Test microradians.
			println("Multiplication Microradians: " +  1.1.microradians * 2.3.microradians)
			assert (1.1.microradians * 2.3.microradians == 2.3.microradians * 1.1.microradians)
			
			// Test nanoradians.
			println("Multiplication Nanoradians: " +  1.1.nanoradians * 2.3.nanoradians)
			assert (1.1.nanoradians * 2.3.nanoradians == 2.3.nanoradians * 1.1.nanoradians)
			
			// Test seconds.
			println("Multiplication Seconds: " +  1.1.seconds * 2.3.seconds)
			assert (1.1.seconds * 2.3.seconds == 2.3.seconds * 1.1.seconds)
			
			// Test siemens.
			println("Multiplication Siemens: " +  1.1.siemens * 2.3.siemens)
			assert (1.1.siemens * 2.3.siemens == 2.3.siemens * 1.1.siemens)
			
			// Test sieverts.
			println("Multiplication Sieverts: " +  1.1.sieverts * 2.3.sieverts)
			assert (1.1.sieverts * 2.3.sieverts == 2.3.sieverts * 1.1.sieverts)
			
			// Test millisieverts.
			println("Multiplication Millisieverts: " +  1.1.millisieverts * 2.3.millisieverts)
			assert (1.1.millisieverts * 2.3.millisieverts == 2.3.millisieverts * 1.1.millisieverts)
			
			// Test microsieverts.
			println("Multiplication Microsieverts: " +  1.1.microsieverts * 2.3.microsieverts)
			assert (1.1.microsieverts * 2.3.microsieverts == 2.3.microsieverts * 1.1.microsieverts)
			
			// Test square meters.
			println("Multiplication Square_meters: " +  1.1.square_meters * 2.3.square_meters)
			assert (1.1.square_meters * 2.3.square_meters == 2.3.square_meters * 1.1.square_meters)
			
			// Test steradians.
			println("Multiplication Steradians: " +  1.1.steradians * 2.3.steradians)
			assert (1.1.steradians * 2.3.steradians == 2.3.steradians * 1.1.steradians)
			
			// Test teslas.
			println("Multiplication Teslas: " +  1.1.teslas * 2.3.teslas)
			assert (1.1.teslas * 2.3.teslas == 2.3.teslas * 1.1.teslas)
			
			// Test volts.
			println("Multiplication Volts: " +  1.1.volts * 2.3.volts)
			assert (1.1.volts * 2.3.volts == 2.3.volts * 1.1.volts)
			
			// Test kilovolts.
			println("Multiplication Kilovolts: " +  1.1.kilovolts * 2.3.kilovolts)
			assert (1.1.kilovolts * 2.3.kilovolts == 2.3.kilovolts * 1.1.kilovolts)
			
			// Test watts.
			println("Multiplication Watts: " +  1.1.watts * 2.3.watts)
			assert (1.1.watts * 2.3.watts == 2.3.watts * 1.1.watts)
			
			// Test kilowatts.
			println("Multiplication Kilowatts: " +  1.1.kilowatts * 2.3.kilowatts)
			assert (1.1.kilowatts * 2.3.kilowatts == 2.3.kilowatts * 1.1.kilowatts)
			
			// Test megawatts.
			println("Multiplication Megawatts: " +  1.1.megawatts * 2.3.megawatts)
			assert (1.1.megawatts * 2.3.megawatts == 2.3.megawatts * 1.1.megawatts)
			
			// Test webers.
			println("Multiplication Webers: " +  1.1.webers * 2.3.webers)
			assert (1.1.webers * 2.3.webers == 2.3.webers * 1.1.webers)
			
			// Test ares.
			println("Multiplication Ares: " +  1.1.ares * 2.3.ares)
			assert (1.1.ares * 2.3.ares == 2.3.ares * 1.1.ares)
			
			// Test astronomical_units.
			println("Multiplication Astronomical_units: " +  1.1.astronomical_units * 2.3.astronomical_units)
			assert (1.1.astronomical_units * 2.3.astronomical_units == 2.3.astronomical_units * 1.1.astronomical_units)
			
			// Test atmospheres.
			println("Multiplication Atmospheres: " +  1.1.atmospheres * 2.3.atmospheres)
			assert (1.1.atmospheres * 2.3.atmospheres == 2.3.atmospheres * 1.1.atmospheres)
			
			// Test atoms.
			println("Multiplication Atoms: " +  1.1.atoms * 2.3.atoms)
			assert (1.1.atoms * 2.3.atoms == 2.3.atoms * 1.1.atoms)
			
			// Test atomic mass.
			println("Multiplication Atomic_mass: " +  1.1.atomic_mass * 2.3.atomic_mass)
			assert (1.1.atomic_mass * 2.3.atomic_mass == 2.3.atomic_mass * 1.1.atomic_mass)
			
			// Test bars.
			println("Multiplication Bars: " +  1.1.bars * 2.3.bars)
			assert (1.1.bars * 2.3.bars == 2.3.bars * 1.1.bars)
			
			// Test bytes.
			println("Multiplication Bytes: " +  1.1.bytes * 2.3.bytes)
			assert (1.1.bytes * 2.3.bytes == 2.3.bytes * 1.1.bytes)
			
			// Test c.
			println("Multiplication C: " +  1.1.c * 2.3.c)
			assert (1.1.c * 2.3.c == 2.3.c * 1.1.c)
			
			// Test centiradians.
			println("Multiplication Centiradians: " +  1.1.centiradians * 2.3.centiradians)
			assert (1.1.centiradians * 2.3.centiradians == 2.3.centiradians * 1.1.centiradians)
			
			// Test computer point.
			println("Multiplication Computer_point: " +  1.1.computer_point * 2.3.computer_point)
			assert (1.1.computer_point * 2.3.computer_point == 2.3.computer_point * 1.1.computer_point)
			
			// Test cubic inches.
			println("Multiplication Cubic_inches: " +  1.1.cubic_inches * 2.3.cubic_inches)
			assert (1.1.cubic_inches * 2.3.cubic_inches == 2.3.cubic_inches * 1.1.cubic_inches)
			
			// Test curies.
			println("Multiplication Curies: " +  1.1.curies * 2.3.curies)
			assert (1.1.curies * 2.3.curies == 2.3.curies * 1.1.curies)
			
			// Test days.
			println("Multiplication Days: " +  1.1.days * 2.3.days)
			assert (1.1.days * 2.3.days == 2.3.days * 1.1.days)
			
			// Test day sidereal.
			println("Multiplication Day_sidereal: " +  1.1.day_sidereal * 2.3.day_sidereal)
			assert (1.1.day_sidereal * 2.3.day_sidereal == 2.3.day_sidereal * 1.1.day_sidereal)
			
			// Test decibels.
			println("Multiplication Decibels: " +  1.1.decibels * 2.3.decibels)
			assert (1.1.decibels * 2.3.decibels == 2.3.decibels * 1.1.decibels)
			
			// Test degree angle.
			println("Multiplication Degree_angle: " +  1.1.degree_angle * 2.3.degree_angle)
			assert (1.1.degree_angle * 2.3.degree_angle == 2.3.degree_angle * 1.1.degree_angle)
			
			// Test dynes.
			println("Multiplication Dynes: " +  1.1.dynes * 2.3.dynes)
			assert (1.1.dynes * 2.3.dynes == 2.3.dynes * 1.1.dynes)
			
			// Test e.
			println("Multiplication E: " +  1.1.e * 2.3.e)
			assert (1.1.e * 2.3.e == 2.3.e * 1.1.e)
			
			// Test elcetron mass.
			println("Multiplication Electron_mass: " +  1.1.electron_mass * 2.3.electron_mass)
			assert (1.1.electron_mass * 2.3.electron_mass == 2.3.electron_mass * 1.1.electron_mass)
			
			// Test elcetron volt.
			println("Multiplication Electron_volt: " +  1.1.electron_volt * 2.3.electron_volt)
			assert (1.1.electron_volt * 2.3.electron_volt == 2.3.electron_volt * 1.1.electron_volt)
			
			// Test ergs.
			println("Multiplication Ergs: " +  1.1.ergs * 2.3.ergs)
			assert (1.1.ergs * 2.3.ergs == 2.3.ergs * 1.1.ergs)
			
			// Test fahrenheit.
			println("Multiplication Fahrenheit: " +  1.1.fahrenheit * 2.3.fahrenheit)
			assert (1.1.fahrenheit * 2.3.fahrenheit == 2.3.fahrenheit * 1.1.fahrenheit)
			
			// Test faradays.
			println("Multiplication Faradays: " +  1.1.faradays * 2.3.faradays)
			assert (1.1.faradays * 2.3.faradays == 2.3.faradays * 1.1.faradays)
			
			// Test feet.
			println("Multiplication Feet: " +  1.1.feet * 2.3.feet)
			assert (1.1.feet * 2.3.feet == 2.3.feet * 1.1.feet)
			
			// Test foot_survey_us.
			println("Multiplication Foot_survey_us: " +  1.1.foot_survey_us * 2.3.foot_survey_us)
			assert (1.1.foot_survey_us * 2.3.foot_survey_us == 2.3.foot_survey_us * 1.1.foot_survey_us)
			
			// Test franklins.
			println("Multiplication Franklins: " +  1.1.franklins * 2.3.franklins)
			assert (1.1.franklins * 2.3.franklins == 2.3.franklins * 1.1.franklins)
			
			// Test g.
			println("Multiplication G: " +  1.1.g * 2.3.g)
			assert (1.1.g * 2.3.g == 2.3.g * 1.1.g)
			
			// Test gallon dry us.
			println("Multiplication Gallon_dry_us: " +  1.1.gallon_dry_us * 2.3.gallon_dry_us)
			assert (1.1.gallon_dry_us * 2.3.gallon_dry_us == 2.3.gallon_dry_us * 1.1.gallon_dry_us)
			
			// Test gallon liquid us.
			println("Multiplication Gallon_liquid_us: " +  1.1.gallon_liquid_us * 2.3.gallon_liquid_us)
			assert (1.1.gallon_liquid_us * 2.3.gallon_liquid_us == 2.3.gallon_liquid_us * 1.1.gallon_liquid_us)
			
			// Test gallon uk.
			println("Multiplication Gallon_uk: " +  1.1.gallon_uk * 2.3.gallon_uk)
			assert (1.1.gallon_uk * 2.3.gallon_uk == 2.3.gallon_uk * 1.1.gallon_uk)
			
			// Test gauss.
			println("Multiplication Gauss: " +  1.1.gauss * 2.3.gauss)
			assert (1.1.gauss * 2.3.gauss == 2.3.gauss * 1.1.gauss)
			
			// Test gilberts.
			println("Multiplication Gilberts: " +  1.1.gilberts * 2.3.gilberts)
			assert (1.1.gilberts * 2.3.gilberts == 2.3.gilberts * 1.1.gilberts)
			
			// Test grades.
			println("Multiplication Grades: " +  1.1.grades * 2.3.grades)
			assert (1.1.grades * 2.3.grades == 2.3.grades * 1.1.grades)
			
			// Test hectares.
			println("Multiplication Hectares: " +  1.1.hectares * 2.3.hectares)
			assert (1.1.hectares * 2.3.hectares == 2.3.hectares * 1.1.hectares)
			
			// Test horsepower.
			println("Multiplication Horsepower: " +  1.1.horsepower * 2.3.horsepower)
			assert (1.1.horsepower * 2.3.horsepower == 2.3.horsepower * 1.1.horsepower)
			
			// Test hours.
			println("Multiplication Hours: " +  1.1.hours * 2.3.hours)
			assert (1.1.hours * 2.3.hours == 2.3.hours * 1.1.hours)
			
			// Test inches.
			println("Multiplication Inches: " +  1.1.inches * 2.3.inches)
			assert (1.1.inches * 2.3.inches == 2.3.inches * 1.1.inches)
			
			// Test inch of mercury.
			println("Multiplication Inch_of_mercury: " +  1.1.inch_of_mercury * 2.3.inch_of_mercury)
			assert (1.1.inch_of_mercury * 2.3.inch_of_mercury == 2.3.inch_of_mercury * 1.1.inch_of_mercury)
			
			// Test kilogram force.
			println("Multiplication Kilogram_force: " +  1.1.kilogram_force * 2.3.kilogram_force)
			assert (1.1.kilogram_force * 2.3.kilogram_force == 2.3.kilogram_force * 1.1.kilogram_force)
			
			// Test knots.
			println("Multiplication Knots: " +  1.1.knots * 2.3.knots)
			assert (1.1.knots * 2.3.knots == 2.3.knots * 1.1.knots)
			
			// Test lamberts.
			println("Multiplication Lamberts: " +  1.1.lamberts * 2.3.lamberts)
			assert (1.1.lamberts * 2.3.lamberts == 2.3.lamberts * 1.1.lamberts)
			
			// Test light years.
			println("Multiplication Light_years: " +  1.1.light_years * 2.3.light_years)
			assert (1.1.light_years * 2.3.light_years == 2.3.light_years * 1.1.light_years)
			
			// Test liters.
			println("Multiplication Liters: " +  1.1.liters * 2.3.liters)
			assert (1.1.liters * 2.3.liters == 2.3.liters * 1.1.liters)
			
			// Test mach.
			println("Multiplication Mach: " +  1.1.mach * 2.3.mach)
			assert (1.1.mach * 2.3.mach == 2.3.mach * 1.1.mach)
			
			// Test maxwells.
			println("Multiplication Maxwells: " +  1.1.maxwells * 2.3.maxwells)
			assert (1.1.maxwells * 2.3.maxwells == 2.3.maxwells * 1.1.maxwells)
			
			// Test metric tons.
			println("Multiplication Metric_tons: " +  1.1.metric_tons * 2.3.metric_tons)
			assert (1.1.metric_tons * 2.3.metric_tons == 2.3.metric_tons * 1.1.metric_tons)
			
			// Test miles.
			println("Multiplication Miles: " +  1.1.miles * 2.3.miles)
			assert (1.1.miles * 2.3.miles == 2.3.miles * 1.1.miles)
			
			// Test millimeters of mercury.
			println("Multiplication Millimeters_of_mercury: " +  1.1.millimeters_of_mercury * 2.3.millimeters_of_mercury)
			assert (1.1.millimeters_of_mercury * 2.3.millimeters_of_mercury == 2.3.millimeters_of_mercury * 1.1.millimeters_of_mercury)
			
			// Test minutes.
			println("Multiplication Minutes: " +  1.1.minutes * 2.3.minutes)
			assert (1.1.minutes * 2.3.minutes == 2.3.minutes * 1.1.minutes)
			
			// Test minute angle.
			println("Multiplication Minute_angle: " +  1.1.minute_angle * 2.3.minute_angle)
			assert (1.1.minute_angle * 2.3.minute_angle == 2.3.minute_angle * 1.1.minute_angle)
			
			// Test months.
			println("Multiplication Months: " +  1.1.months * 2.3.months)
			assert (1.1.months * 2.3.months == 2.3.months * 1.1.months)
			
			// Test nautical miles.
			println("Multiplication Nautical_miles: " +  1.1.nautical_miles * 2.3.nautical_miles)
			assert (1.1.nautical_miles * 2.3.nautical_miles == 2.3.nautical_miles * 1.1.nautical_miles)
			
			// Test octets.
			println("Multiplication Octets: " +  1.1.octets * 2.3.octets)
			assert (1.1.octets * 2.3.octets == 2.3.octets * 1.1.octets)
			
			// Test ounces.
			println("Multiplication Ounces: " +  1.1.ounces * 2.3.ounces)
			assert (1.1.ounces * 2.3.ounces == 2.3.ounces * 1.1.ounces)
			
			// Test ounce liquid uk.
			println("Multiplication Ounce_liquid_uk: " +  1.1.ounce_liquid_uk * 2.3.ounce_liquid_uk)
			assert (1.1.ounce_liquid_uk * 2.3.ounce_liquid_uk == 2.3.ounce_liquid_uk * 1.1.ounce_liquid_uk)
			
			// Test ounce liquid us.
			println("Multiplication Ounce_liquid_us: " +  1.1.ounce_liquid_us * 2.3.ounce_liquid_us)
			assert (1.1.ounce_liquid_us * 2.3.ounce_liquid_us == 2.3.ounce_liquid_us * 1.1.ounce_liquid_us)
			
			// Test parsecs.
			println("Multiplication Parsecs: " +  1.1.parsecs * 2.3.parsecs)
			assert (1.1.parsecs * 2.3.parsecs == 2.3.parsecs * 1.1.parsecs)
			
			// Test percent.
			println("Multiplication Percent: " +  1.1.percent * 2.3.percent)
			assert (1.1.percent * 2.3.percent == 2.3.percent * 1.1.percent)
			
			// Test pixels.
			println("Multiplication Pixels: " +  1.1.pixels * 2.3.pixels)
			assert (1.1.pixels * 2.3.pixels == 2.3.pixels * 1.1.pixels)
			
			// Test points.
			println("Multiplication Points: " +  1.1.points * 2.3.points)
			assert (1.1.points * 2.3.points == 2.3.points * 1.1.points)
			
			// Test poise.
			println("Multiplication Poise: " +  1.1.poise * 2.3.poise)
			assert (1.1.poise * 2.3.poise == 2.3.poise * 1.1.poise)
			
			// Test pound force.
			println("Multiplication Pound_force: " +  1.1.pound_force * 2.3.pound_force)
			assert (1.1.pound_force * 2.3.pound_force == 2.3.pound_force * 1.1.pound_force)
			
			// Test rads.
			println("Multiplication Rads: " +  1.1.rads * 2.3.rads)
			assert (1.1.rads * 2.3.rads == 2.3.rads * 1.1.rads)
			
			// Test rankines.
			println("Multiplication Rankines: " +  1.1.rankines * 2.3.rankines)
			assert (1.1.rankines * 2.3.rankines == 2.3.rankines * 1.1.rankines)
			
			// Test rems.
			println("Multiplication Rems: " +  1.1.rems * 2.3.rems)
			assert (1.1.rems * 2.3.rems == 2.3.rems * 1.1.rems)
			
			// Test revolutions.
			println("Multiplication Revolutions: " +  1.1.revolutions * 2.3.revolutions)
			assert (1.1.revolutions * 2.3.revolutions == 2.3.revolutions * 1.1.revolutions)
			
			// Test roentgens.
			println("Multiplication Roentgens: " +  1.1.roentgens * 2.3.roentgens)
			assert (1.1.roentgens * 2.3.roentgens == 2.3.roentgens * 1.1.roentgens)
			
			// Test rutherfords.
			println("Multiplication Rutherfords: " +  1.1.rutherfords * 2.3.rutherfords)
			assert (1.1.rutherfords * 2.3.rutherfords == 2.3.rutherfords * 1.1.rutherfords)
			
			// Test second angle.
			println("Multiplication Second_angle: " +  1.1.second_angle * 2.3.second_angle)
			assert (1.1.second_angle * 2.3.second_angle == 2.3.second_angle * 1.1.second_angle)
			
			// Test spheres.
			println("Multiplication Spheres: " +  1.1.spheres * 2.3.spheres)
			assert (1.1.spheres * 2.3.spheres == 2.3.spheres * 1.1.spheres)
			
			// Test stokes.
			println("Multiplication Stokes: " +  1.1.stokes * 2.3.stokes)
			assert (1.1.stokes * 2.3.stokes == 2.3.stokes * 1.1.stokes)
			
			// Test ton uk.
			println("Multiplication Ton_uk: " +  1.1.ton_uk * 2.3.ton_uk)
			assert (1.1.ton_uk * 2.3.ton_uk == 2.3.ton_uk * 1.1.ton_uk)
			
			// Test ton us.
			println("Multiplication Ton_us: " +  1.1.ton_us * 2.3.ton_us)
			assert (1.1.ton_us * 2.3.ton_us == 2.3.ton_us * 1.1.ton_us)
			
			// Test weeks.
			println("Multiplication Weeks: " +  1.1.weeks * 2.3.weeks)
			assert (1.1.weeks * 2.3.weeks == 2.3.weeks * 1.1.weeks)
			
			// Test yards.
			println("Multiplication Yards: " +  1.1.yards * 2.3.yards)
			assert (1.1.yards * 2.3.yards == 2.3.yards * 1.1.yards)
			
			// Test years.
			println("Multiplication Years: " +  1.1.years * 2.3.years)
			assert (1.1.years * 2.3.years == 2.3.years * 1.1.years)
			
			// Test year_calendar.
			println("Multiplication Year_calendar: " +  1.1.year_calendar * 2.3.year_calendar)
			assert (1.1.year_calendar * 2.3.year_calendar == 2.3.year_calendar * 1.1.year_calendar)
			
			// Test year_sidereal.
			println("Multiplication Year_sidereal: " +  1.1.year_sidereal * 2.3.year_sidereal)
			assert (1.1.year_sidereal * 2.3.year_sidereal == 2.3.year_sidereal * 1.1.year_sidereal)

		}
		    
	}
			  
	void testSubtraction() {

		use(UnitsCategory) {

			// Test meters.
			println("Subtraction Meters: " + (1.1.meters - 2.3.meters).abs())
			assert ((1.1.meters - 2.3.meters - (-1.2).meters).abs() < 0.001.meters)
			
			// Test kilometers.
			println("Subtraction Kilometers: " + (1.1.kilometers - 2.3.kilometers).abs())
			assert ((1.1.kilometers - 2.3.kilometers - (-1.2).kilometers).abs() < 0.001.kilometers)
			
			// Test centimeters.
			println("Subtraction Centimeters: " + (1.1.centimeters - 2.3.centimeters).abs())
			assert ((1.1.centimeters - 2.3.centimeters - (-1.2).centimeters).abs() < 0.001.centimeters)
			
			// Test millimeters.
			println("Subtraction Millimeters: " + (1.1.millimeters - 2.3.millimeters).abs())
			assert ((1.1.millimeters - 2.3.millimeters - (-1.2).millimeters).abs() < 0.001.millimeters)
			
			// Test micrometers.
			println("Subtraction Micrometers: " + (1.1.micrometers - 2.3.micrometers).abs())
			assert ((1.1.micrometers - 2.3.micrometers - (-1.2).micrometers).abs() < 0.001.micrometers)
			
			// Test decimeters.
			println("Subtraction Decimeters: " + (1.1.decimeters - 2.3.decimeters).abs())
			assert ((1.1.decimeters - 2.3.decimeters - (-1.2).decimeters).abs() < 0.001.decimeters)
			
			// Test kilograms.
			println("Subtraction Kilograms: " + (1.1.kilograms - 2.3.kilograms).abs())
			assert ((1.1.kilograms - 2.3.kilograms - (-1.2).kilograms).abs() < 0.001.kilograms)
			
			// Test milligrams.
			println("Subtraction Milligrams: " + (1.1.milligrams - 2.3.milligrams).abs())
			assert ((1.1.milligrams - 2.3.milligrams - (-1.2).milligrams).abs() < 0.001.milligrams)
			
			// Test angstroms.
			println("Subtraction Angstroms: " + (1.1.angstroms - 2.3.angstroms).abs())
			assert ((1.1.angstroms - 2.3.angstroms - (-1.2).angstroms).abs() < 0.001.angstroms)
			
			// Test pounds.
			println("Subtraction Pounds: " + (1.1.pounds - 2.3.pounds).abs())
			assert ((1.1.pounds - 2.3.pounds - (-1.2).pounds).abs() < 0.001.pounds)
			
			// Test grams.
			println("Subtraction Grams: " + (1.1.grams - 2.3.grams).abs())
			assert ((1.1.grams - 2.3.grams - (-1.2).grams).abs() < 0.001.grams)
			
			// Test amperes.
			println("Subtraction Amperes: " + (1.1.amperes - 2.3.amperes).abs())
			assert ((1.1.amperes - 2.3.amperes - (-1.2).amperes).abs() < 0.001.amperes)
			
			// Test becquerels.
			println("Subtraction Becquerels: " + (1.1.becquerels - 2.3.becquerels).abs())
			assert ((1.1.becquerels - 2.3.becquerels - (-1.2).becquerels).abs() < 0.001.becquerels)
			
			// Test kilobecquerels.
			println("Subtraction Kilobecquerels: " + (1.1.kilobecquerels - 2.3.kilobecquerels).abs())
			assert ((1.1.kilobecquerels - 2.3.kilobecquerels - (-1.2).kilobecquerels).abs() < 0.001.kilobecquerels)
			
			// Test megabecquerels.
			println("Subtraction Megabecquerels: " + (1.1.megabecquerels - 2.3.megabecquerels).abs())
			assert ((1.1.megabecquerels - 2.3.megabecquerels - (-1.2).megabecquerels).abs() < 0.001.megabecquerels)
			
			// Test gigabecquerels.
			println("Subtraction Gigabecquerels: " + (1.1.gigabecquerels - 2.3.gigabecquerels).abs())
			assert ((1.1.gigabecquerels - 2.3.gigabecquerels - (-1.2).gigabecquerels).abs() < 0.001.gigabecquerels)
			
			// Test bits.
			println("Subtraction Bits: " + (1.1.bits - 2.3.bits).abs())
			assert ((1.1.bits - 2.3.bits - (-1.2).bits).abs() < 0.001.bits)
			
			// Test kilobits.
			println("Subtraction Kilobits: " + (1.1.kilobits - 2.3.kilobits).abs())
			assert ((1.1.kilobits - 2.3.kilobits - (-1.2).kilobits).abs() < 0.001.kilobits)
			
			// Test megabits.
			println("Subtraction Megabits: " + (1.1.megabits - 2.3.megabits).abs())
			assert ((1.1.megabits - 2.3.megabits - (-1.2).megabits).abs() < 0.001.megabits)
			
			// Test gigabits.
			println("Subtraction Gigabits: " + (1.1.gigabits - 2.3.gigabits).abs())
			assert ((1.1.gigabits - 2.3.gigabits - (-1.2).gigabits).abs() < 0.001.gigabits)
			
			// Test terabits.
			println("Subtraction Terabits: " + (1.1.terabits - 2.3.terabits).abs())
			assert ((1.1.terabits - 2.3.terabits - (-1.2).terabits).abs() < 0.001.terabits)
			
			// Test candelas.
			println("Subtraction Candelas: " + (1.1.candelas - 2.3.candelas).abs())
			assert ((1.1.candelas - 2.3.candelas - (-1.2).candelas).abs() < 0.001.candelas)
			
			// Test celsius.
			println("Subtraction Celsius: " + (1.1.celsius - 2.3.celsius).abs())
			assert ((1.1.celsius - 2.3.celsius - (-1.2).celsius).abs() < 0.001.celsius)
			
			// Test coulombs.
			println("Subtraction Coulombs: " + (1.1.coulombs - 2.3.coulombs).abs())
			assert ((1.1.coulombs - 2.3.coulombs - (-1.2).coulombs).abs() < 0.001.coulombs)
 			
			// Test farads.
			println("Subtraction Farads: " + (1.1.farads - 2.3.farads).abs())
			assert ((1.1.farads - 2.3.farads - (-1.2).farads).abs() < 0.001.farads)
			
			// Test microfarads.
			println("Subtraction Microfarads: " + (1.1.microfarads - 2.3.microfarads).abs())
			assert ((1.1.microfarads - 2.3.microfarads - (-1.2).microfarads).abs() < 0.001.microfarads)
			
			// Test nanofarads.
			println("Subtraction Nanofarads: " + (1.1.nanofarads - 2.3.nanofarads).abs())
			assert ((1.1.nanofarads - 2.3.nanofarads - (-1.2).nanofarads).abs() < 0.001.nanofarads)
			
			// Test picofarads.
			println("Subtraction Picofarads: " + (1.1.picofarads - 2.3.picofarads).abs())
			assert ((1.1.picofarads - 2.3.picofarads - (-1.2).picofarads).abs() < 0.001.picofarads)
						
			// Test grays.
			println("Subtraction Grays: " + (1.1.grays - 2.3.grays).abs())
			assert ((1.1.grays - 2.3.grays - (-1.2).grays).abs() < 0.001.grays)
			
			// Test milligrays.
			println("Subtraction Milligrays: " + (1.1.milligrays - 2.3.milligrays).abs())
			assert ((1.1.milligrays - 2.3.milligrays - (-1.2).milligrays).abs() < 0.001.milligrays)
			
			// Test henrys.
			println("Subtraction Henrys: " + (1.1.henrys - 2.3.henrys).abs())
			assert ((1.1.henrys - 2.3.henrys - (-1.2).henrys).abs() < 0.001.henrys)
			
			// Test hertz.
			println("Subtraction Hertz: " + (1.1.hertz - 2.3.hertz).abs())
			assert ((1.1.hertz - 2.3.hertz - (-1.2).hertz).abs() < 0.001.hertz)
			
			// Test kilohertz.
			println("Subtraction Kilohertz: " + (1.1.kilohertz - 2.3.kilohertz).abs())
			assert ((1.1.kilohertz - 2.3.kilohertz - (-1.2).kilohertz).abs() < 0.001.kilohertz)
			
			// Test megahertz.
			println("Subtraction Megahertz: " + (1.1.megahertz - 2.3.megahertz).abs())
			assert ((1.1.megahertz - 2.3.megahertz - (-1.2).megahertz).abs() < 0.001.megahertz)
			
			// Test joules.
			println("Subtraction Joules: " + (1.1.joules - 2.3.joules).abs())
			assert ((1.1.joules - 2.3.joules - (-1.2).joules).abs() < 0.001.joules)
			
			// Test kilojoules.
			println("Subtraction Kilojoules: " + (1.1.kilojoules - 2.3.kilojoules).abs())
			assert ((1.1.kilojoules - 2.3.kilojoules - (-1.2).kilojoules).abs() < 0.001.kilojoules)
			
			// Test katals.
			println("Subtraction Katals: " + (1.1.katals - 2.3.katals).abs())
			assert ((1.1.katals - 2.3.katals - (-1.2).katals).abs() < 0.001.katals)
			
			// Test kelvin.
			println("Subtraction Kelvin: " + (1.1.kelvin - 2.3.kelvin).abs())
			assert ((1.1.kelvin - 2.3.kelvin - (-1.2).kelvin).abs() < 0.001.kelvin)
			
			// Test lumens.
			println("Subtraction Lumens: " + (1.1.lumens - 2.3.lumens).abs())
			assert ((1.1.lumens - 2.3.lumens - (-1.2).lumens).abs() < 0.001.lumens)
			
			// Test lux.
			println("Subtraction Lux: " + (1.1.lux - 2.3.lux).abs())
			assert ((1.1.lux - 2.3.lux - (-1.2).lux).abs() < 0.001.lux)
			
			// Test microlux.
			println("Subtraction Microlux: " + (1.1.microlux - 2.3.microlux).abs())
			assert ((1.1.microlux - 2.3.microlux - (-1.2).microlux).abs() < 0.001.microlux)
			
			// Test millilux.
			println("Subtraction Millilux: " + (1.1.millilux - 2.3.millilux).abs())
			assert ((1.1.millilux - 2.3.millilux - (-1.2).millilux).abs() < 0.001.millilux)
			
			// Test kilolux.
			println("Subtraction Kilolux: " + (1.1.kilolux - 2.3.kilolux).abs())
			assert ((1.1.kilolux - 2.3.kilolux - (-1.2).kilolux).abs() < 0.001.kilolux)
			
			// Test meter per second.
			println("Subtraction Meter_per_second: " + (1.1.meter_per_second - 2.3.meter_per_second).abs())
			assert ((1.1.meter_per_second - 2.3.meter_per_second - (-1.2).meter_per_second).abs() < 0.001.meter_per_second)
			
			// Test meter per square second.
			println("Subtraction Meter_per_square_second: " + (1.1.meter_per_square_second - 2.3.meter_per_square_second).abs())
			assert ((1.1.meter_per_square_second - 2.3.meter_per_square_second - (-1.2).meter_per_square_second).abs() < 0.001.meter_per_square_second)
			
			// Test moles.
			println("Subtraction Moles: " + (1.1.moles - 2.3.moles).abs())
			assert ((1.1.moles - 2.3.moles - (-1.2).moles).abs() < 0.001.moles)
			
			// Test newtons.
			println("Subtraction Newtons: " + (1.1.newtons - 2.3.newtons).abs())
			assert ((1.1.newtons - 2.3.newtons - (-1.2).newtons).abs() < 0.001.newtons)
			
			// Test ohms.
			println("Subtraction Ohms: " + (1.1.ohms - 2.3.ohms).abs())
			assert ((1.1.ohms - 2.3.ohms - (-1.2).ohms).abs() < 0.001.ohms)
			
			// Test pascals.
			println("Subtraction Pascals: " + (1.1.pascals - 2.3.pascals).abs())
			assert ((1.1.pascals - 2.3.pascals - (-1.2).pascals).abs() < 0.001.pascals)
			
			// Test kilopascals.
			println("Subtraction Kilopascals: " + (1.1.kilopascals - 2.3.kilopascals).abs())
			assert ((1.1.kilopascals - 2.3.kilopascals - (-1.2).kilopascals).abs() < 0.001.kilopascals)
			
			// Test radians.
			println("Subtraction Radians: " + (1.1.radians - 2.3.radians).abs())
			assert ((1.1.radians - 2.3.radians - (-1.2).radians).abs() < 0.001.radians)
			
			// Test milliradians.
			println("Subtraction Milliradians: " + (1.1.milliradians - 2.3.milliradians).abs())
			assert ((1.1.milliradians - 2.3.milliradians - (-1.2).milliradians).abs() < 0.001.milliradians)
			
			// Test microradians.
			println("Subtraction Microradians: " + (1.1.microradians - 2.3.microradians).abs())
			assert ((1.1.microradians - 2.3.microradians - (-1.2).microradians).abs() < 0.001.microradians)
			
			// Test nanoradians.
			println("Subtraction Nanoradians: " + (1.1.nanoradians - 2.3.nanoradians).abs())
			assert ((1.1.nanoradians - 2.3.nanoradians - (-1.2).nanoradians).abs() < 0.001.nanoradians)
			
			// Test seconds.
			println("Subtraction Seconds: " + (1.1.seconds - 2.3.seconds).abs())
			assert ((1.1.seconds - 2.3.seconds - (-1.2).seconds).abs() < 0.001.seconds)
			
			// Test siemens.
			println("Subtraction Siemens: " + (1.1.siemens - 2.3.siemens).abs())
			assert ((1.1.siemens - 2.3.siemens - (-1.2).siemens).abs() < 0.001.siemens)
			
			// Test sieverts.
			println("Subtraction Sieverts: " + (1.1.sieverts - 2.3.sieverts).abs())
			assert ((1.1.sieverts - 2.3.sieverts - (-1.2).sieverts).abs() < 0.001.sieverts)
			
			// Test millisieverts.
			println("Subtraction Millisieverts: " + (1.1.millisieverts - 2.3.millisieverts).abs())
			assert ((1.1.millisieverts - 2.3.millisieverts - (-1.2).millisieverts).abs() < 0.001.millisieverts)
			
			// Test microsieverts.
			println("Subtraction Microsieverts: " + (1.1.microsieverts - 2.3.microsieverts).abs())
			assert ((1.1.microsieverts - 2.3.microsieverts - (-1.2).microsieverts).abs() < 0.001.microsieverts)
			
			// Test square meters.
			println("Subtraction Square_meters: " + (1.1.square_meters - 2.3.square_meters).abs())
			assert ((1.1.square_meters - 2.3.square_meters - (-1.2).square_meters).abs() < 0.001.square_meters)
			
			// Test steradians.
			println("Subtraction Steradians: " + (1.1.steradians - 2.3.steradians).abs())
			assert ((1.1.steradians - 2.3.steradians - (-1.2).steradians).abs() < 0.001.steradians)
			
			// Test teslas.
			println("Subtraction Teslas: " + (1.1.teslas - 2.3.teslas).abs())
			assert ((1.1.teslas - 2.3.teslas - (-1.2).teslas).abs() < 0.001.teslas)
			
			// Test volts.
			println("Subtraction Volts: " + (1.1.volts - 2.3.volts).abs())
			assert ((1.1.volts - 2.3.volts - (-1.2).volts).abs() < 0.001.volts)
			
			// Test kilovolts.
			println("Subtraction Kilovolts: " + (1.1.kilovolts - 2.3.kilovolts).abs())
			assert ((1.1.kilovolts - 2.3.kilovolts - (-1.2).kilovolts).abs() < 0.001.kilovolts)
			
			// Test watts.
			println("Subtraction Watts: " + (1.1.watts - 2.3.watts).abs())
			assert ((1.1.watts - 2.3.watts - (-1.2).watts).abs() < 0.001.watts)
			
			// Test kilowatts.
			println("Subtraction Kilowatts: " + (1.1.kilowatts - 2.3.kilowatts).abs())
			assert ((1.1.kilowatts - 2.3.kilowatts - (-1.2).kilowatts).abs() < 0.001.kilowatts)
			
			// Test megawatts.
			println("Subtraction Megawatts: " + (1.1.megawatts - 2.3.megawatts).abs())
			assert ((1.1.megawatts - 2.3.megawatts - (-1.2).megawatts).abs() < 0.001.megawatts)
			
			// Test webers.
			println("Subtraction Webers: " + (1.1.webers - 2.3.webers).abs())
			assert ((1.1.webers - 2.3.webers - (-1.2).webers).abs() < 0.001.webers)
			
			// Test ares.
			println("Subtraction Ares: " + (1.1.ares - 2.3.ares).abs())
			assert ((1.1.ares - 2.3.ares - (-1.2).ares).abs() < 0.001.ares)
			
			// Test astronomical_units.
			println("Subtraction Astronomical_units: " + (1.1.astronomical_units - 2.3.astronomical_units).abs())
			assert ((1.1.astronomical_units - 2.3.astronomical_units - (-1.2).astronomical_units).abs() < 0.001.astronomical_units)
			
			// Test atmospheres.
			println("Subtraction Atmospheres: " + (1.1.atmospheres - 2.3.atmospheres).abs())
			assert ((1.1.atmospheres - 2.3.atmospheres - (-1.2).atmospheres).abs() < 0.001.atmospheres)
			
			// Test atoms.
			println("Subtraction Atoms: " + (1.1.atoms - 2.3.atoms).abs())
			assert ((1.1.atoms - 2.3.atoms - (-1.2).atoms).abs() < 0.001.atoms)
			
			// Test atomic mass.
			println("Subtraction Atomic_mass: " + (1.1.atomic_mass - 2.3.atomic_mass).abs())
			assert ((1.1.atomic_mass - 2.3.atomic_mass - (-1.2).atomic_mass).abs() < 0.001.atomic_mass)
			
			// Test bars.
			println("Subtraction Bars: " + (1.1.bars - 2.3.bars).abs())
			assert ((1.1.bars - 2.3.bars - (-1.2).bars).abs() < 0.001.bars)
			
			// Test bytes.
			println("Subtraction Bytes: " + (1.1.bytes - 2.3.bytes).abs())
			assert ((1.1.bytes - 2.3.bytes - (-1.2).bytes).abs() < 0.001.bytes)
			
			// Test c.
			println("Subtraction C: " + (1.1.c - 2.3.c).abs())
			assert ((1.1.c - 2.3.c - (-1.2).c).abs() < 0.001.c)
			
			// Test centiradians.
			println("Subtraction Centiradians: " + (1.1.centiradians - 2.3.centiradians).abs())
			assert ((1.1.centiradians - 2.3.centiradians - (-1.2).centiradians).abs() < 0.001.centiradians)
			
			// Test computer point.
			println("Subtraction Computer_point: " + (1.1.computer_point - 2.3.computer_point).abs())
			assert ((1.1.computer_point - 2.3.computer_point - (-1.2).computer_point).abs() < 0.001.computer_point)
			
			// Test cubic inches.
			println("Subtraction Cubic_inches: " + (1.1.cubic_inches - 2.3.cubic_inches).abs())
			assert ((1.1.cubic_inches - 2.3.cubic_inches - (-1.2).cubic_inches).abs() < 0.001.cubic_inches)
			
			// Test curies.
			println("Subtraction Curies: " + (1.1.curies - 2.3.curies).abs())
			assert ((1.1.curies - 2.3.curies - (-1.2).curies).abs() < 0.001.curies)
			
			// Test days.
			println("Subtraction Days: " + (1.1.days - 2.3.days).abs())
			assert ((1.1.days - 2.3.days - (-1.2).days).abs() < 0.001.days)
			
			// Test decibels.
			println("Subtraction Decibels: " + (1.1.decibels - 2.3.decibels).abs())
			assert ((1.1.decibels - 2.3.decibels - (-1.2).decibels).abs() < 0.001.decibels)
			
			// Test degree angle.
			println("Subtraction Degree_angle: " + (1.1.degree_angle - 2.3.degree_angle).abs())
			assert ((1.1.degree_angle - 2.3.degree_angle - (-1.2).degree_angle).abs() < 0.001.degree_angle)
			
			// Test dynes.
			println("Subtraction Dynes: " + (1.1.dynes - 2.3.dynes).abs())
			assert ((1.1.dynes - 2.3.dynes - (-1.2).dynes).abs() < 0.001.dynes)
			
			// Test e.
			println("Subtraction E: " + (1.1.e - 2.3.e).abs())
			assert ((1.1.e - 2.3.e - (-1.2).e).abs() < 0.001.e)
			
			// Test electron mass.
			println("Subtraction Electron_mass: " + (1.1.electron_mass - 2.3.electron_mass).abs())
			assert ((1.1.electron_mass - 2.3.electron_mass - (-1.2).electron_mass).abs() < 0.001.electron_mass)
			
			// Test electron volt.
			println("Subtraction Electron_volt: " + (1.1.electron_volt - 2.3.electron_volt).abs())
			assert ((1.1.electron_volt - 2.3.electron_volt - (-1.2).electron_volt).abs() < 0.001.electron_volt)
			
			// Test ergs.
			println("Subtraction Ergs: " + (1.1.ergs - 2.3.ergs).abs())
			assert ((1.1.ergs - 2.3.ergs - (-1.2).ergs).abs() < 0.001.ergs)
			
			// Test fahrenheit.
			println("Subtraction Fahrenheit: " + (1.1.fahrenheit - 2.3.fahrenheit).abs())
			assert ((1.1.fahrenheit - 2.3.fahrenheit - (-1.2).fahrenheit).abs() < 0.001.fahrenheit)
			
			// Test faradays.
			println("Subtraction Faradays: " + (1.1.faradays - 2.3.faradays).abs())
			assert ((1.1.faradays - 2.3.faradays - (-1.2).faradays).abs() < 0.001.faradays)
			
			// Test feet.
			println("Subtraction Feet: " + (1.1.feet - 2.3.feet).abs())
			assert ((1.1.feet - 2.3.feet - (-1.2).feet).abs() < 0.001.feet)
			
			// Test franklins.
			println("Subtraction Franklins: " + (1.1.franklins - 2.3.franklins).abs())
			assert ((1.1.franklins - 2.3.franklins - (-1.2).franklins).abs() < 0.001.franklins)
			
			// Test g.
			println("Subtraction G: " + (1.1.g - 2.3.g).abs())
			assert ((1.1.g - 2.3.g - (-1.2).g).abs() < 0.001.g)
			
			// Test gallon dry us.
			println("Subtraction Gallon_dry_us: " + (1.1.gallon_dry_us - 2.3.gallon_dry_us).abs())
			assert ((1.1.gallon_dry_us - 2.3.gallon_dry_us - (-1.2).gallon_dry_us).abs() < 0.001.gallon_dry_us)
			
			// Test gallon liquid us.
			println("Subtraction Gallon_liquid_us: " + (1.1.gallon_liquid_us - 2.3.gallon_liquid_us).abs())
			assert ((1.1.gallon_liquid_us - 2.3.gallon_liquid_us - (-1.2).gallon_liquid_us).abs() < 0.001.gallon_liquid_us)
			
			// Test gallon uk.
			println("Subtraction Gallon_uk: " + (1.1.gallon_uk - 2.3.gallon_uk).abs())
			assert ((1.1.gallon_uk - 2.3.gallon_uk - (-1.2).gallon_uk).abs() < 0.001.gallon_uk)
			
			// Test gauss.
			println("Subtraction Gauss: " + (1.1.gauss - 2.3.gauss).abs())
			assert ((1.1.gauss - 2.3.gauss - (-1.2).gauss).abs() < 0.001.gauss)
			
			// Test gilberts.
			println("Subtraction Gilberts: " + (1.1.gilberts - 2.3.gilberts).abs())
			assert ((1.1.gilberts - 2.3.gilberts - (-1.2).gilberts).abs() < 0.001.gilberts)
			
			// Test grades.
			println("Subtraction Grades: " + (1.1.grades - 2.3.grades).abs())
			assert ((1.1.grades - 2.3.grades - (-1.2).grades).abs() < 0.001.grades)
			
			// Test hectares.
			println("Subtraction Hectares: " + (1.1.hectares - 2.3.hectares).abs())
			assert ((1.1.hectares - 2.3.hectares - (-1.2).hectares).abs() < 0.001.hectares)
			
			// Test horsepower.
			println("Subtraction Horsepower: " + (1.1.horsepower - 2.3.horsepower).abs())
			assert ((1.1.horsepower - 2.3.horsepower - (-1.2).horsepower).abs() < 0.001.horsepower)
			
			// Test hours.
			println("Subtraction Hours: " + (1.1.hours - 2.3.hours).abs())
			assert ((1.1.hours - 2.3.hours - (-1.2).hours).abs() < 0.001.hours)
			
			// Test inches.
			println("Subtraction Inches: " + (1.1.inches - 2.3.inches).abs())
			assert ((1.1.inches - 2.3.inches - (-1.2).inches).abs() < 0.001.inches)
			
			// Test inch of mercury.
			println("Subtraction Inch_of_mercury: " + (1.1.inch_of_mercury - 2.3.inch_of_mercury).abs())
			assert ((1.1.inch_of_mercury - 2.3.inch_of_mercury - (-1.2).inch_of_mercury).abs() < 0.001.inch_of_mercury)
			
			// Test kilogram force.
			println("Subtraction Kilogram_force: " + (1.1.kilogram_force - 2.3.kilogram_force).abs())
			assert ((1.1.kilogram_force - 2.3.kilogram_force - (-1.2).kilogram_force).abs() < 0.001.kilogram_force)
			
			// Test knots.
			println("Subtraction Knots: " + (1.1.knots - 2.3.knots).abs())
			assert ((1.1.knots - 2.3.knots - (-1.2).knots).abs() < 0.001.knots)
			
			// Test lamberts.
			println("Subtraction Lamberts: " + (1.1.lamberts - 2.3.lamberts).abs())
			assert ((1.1.lamberts - 2.3.lamberts - (-1.2).lamberts).abs() < 0.001.lamberts)
			
			// Test light years.
			println("Subtraction Light_years: " + (1.1.light_years - 2.3.light_years).abs())
			assert ((1.1.light_years - 2.3.light_years - (-1.2).light_years).abs() < 0.001.light_years)
			
			// Test liters.
			println("Subtraction Liters: " + (1.1.liters - 2.3.liters).abs())
			assert ((1.1.liters - 2.3.liters - (-1.2).liters).abs() < 0.001.liters)
			
			// Test mach.
			println("Subtraction Mach: " + (1.1.mach - 2.3.mach).abs())
			assert ((1.1.mach - 2.3.mach - (-1.2).mach).abs() < 0.001.mach)
			
			// Test maxwells.
			println("Subtraction Maxwells: " + (1.1.maxwells - 2.3.maxwells).abs())
			assert ((1.1.maxwells - 2.3.maxwells - (-1.2).maxwells).abs() < 0.001.maxwells)
			
			// Test metric tons.
			println("Subtraction Metric_tons: " + (1.1.metric_tons - 2.3.metric_tons).abs())
			assert ((1.1.metric_tons - 2.3.metric_tons - (-1.2).metric_tons).abs() < 0.001.metric_tons)
			
			// Test miles.
			println("Subtraction Miles: " + (1.1.miles - 2.3.miles).abs())
			assert ((1.1.miles - 2.3.miles - (-1.2).miles).abs() < 0.001.miles)
			
			// Test millimeters of mercury.
			println("Subtraction Millimeters_of_mercury: " + (1.1.millimeters_of_mercury - 2.3.millimeters_of_mercury).abs())
			assert ((1.1.millimeters_of_mercury - 2.3.millimeters_of_mercury - (-1.2).millimeters_of_mercury).abs() < 0.001.millimeters_of_mercury)
			
			// Test minutes.
			println("Subtraction Minutes: " + (1.1.minutes - 2.3.minutes).abs())
			assert ((1.1.minutes - 2.3.minutes - (-1.2).minutes).abs() < 0.001.minutes)
			
			// Test minute angle.
			println("Subtraction Minute_angle: " + (1.1.minute_angle - 2.3.minute_angle).abs())
			assert ((1.1.minute_angle - 2.3.minute_angle - (-1.2).minute_angle).abs() < 0.001.minute_angle)
			
			// Test months.
			println("Subtraction Months: " + (1.1.months - 2.3.months).abs())
			assert ((1.1.months - 2.3.months - (-1.2).months).abs() < 0.001.months)
			
			// Test nautical miles.
			println("Subtraction Nautical_miles: " + (1.1.nautical_miles - 2.3.nautical_miles).abs())
			assert ((1.1.nautical_miles - 2.3.nautical_miles - (-1.2).nautical_miles).abs() < 0.001.nautical_miles)
			
			// Test octets.
			println("Subtraction Octets: " + (1.1.octets - 2.3.octets).abs())
			assert ((1.1.octets - 2.3.octets - (-1.2).octets).abs() < 0.001.octets)
			
			// Test ounces.
			println("Subtraction Ounces: " + (1.1.ounces - 2.3.ounces).abs())
			assert ((1.1.ounces - 2.3.ounces - (-1.2).ounces).abs() < 0.001.ounces)
			
			// Test ounce liquid uk.
			println("Subtraction Ounce_liquid_uk: " + (1.1.ounce_liquid_uk - 2.3.ounce_liquid_uk).abs())
			assert ((1.1.ounce_liquid_uk - 2.3.ounce_liquid_uk - (-1.2).ounce_liquid_uk).abs() < 0.001.ounce_liquid_uk)
			
			// Test ounce liquid us.
			println("Subtraction Ounce_liquid_us: " + (1.1.ounce_liquid_us - 2.3.ounce_liquid_us).abs())
			assert ((1.1.ounce_liquid_us - 2.3.ounce_liquid_us - (-1.2).ounce_liquid_us).abs() < 0.001.ounce_liquid_us)
			
			// Test parsecs.
			println("Subtraction Parsecs: " + (1.1.parsecs - 2.3.parsecs).abs())
			assert ((1.1.parsecs - 2.3.parsecs - (-1.2).parsecs).abs() < 0.001.parsecs)
			
			// Test percent.
			println("Subtraction Percent: " + (1.1.percent - 2.3.percent).abs())
			assert ((1.1.percent - 2.3.percent - (-1.2).percent).abs() < 0.001.percent)
			
			// Test pixels.
			println("Subtraction Pixels: " + (1.1.pixels - 2.3.pixels).abs())
			assert ((1.1.pixels - 2.3.pixels - (-1.2).pixels).abs() < 0.001.pixels)
			
			// Test points.
			println("Subtraction Points: " + (1.1.points - 2.3.points).abs())
			assert ((1.1.points - 2.3.points - (-1.2).points).abs() < 0.001.points)
			
			// Test poise.
			println("Subtraction Poise: " + (1.1.poise - 2.3.poise).abs())
			assert ((1.1.poise - 2.3.poise - (-1.2).poise).abs() < 0.001.poise)
			
			// Test pound force.
			println("Subtraction Pound_force: " + (1.1.pound_force - 2.3.pound_force).abs())
			assert ((1.1.pound_force - 2.3.pound_force - (-1.2).pound_force).abs() < 0.001.pound_force)
			
			// Test rads.
			println("Subtraction Rads: " + (1.1.rads - 2.3.rads).abs())
			assert ((1.1.rads - 2.3.rads - (-1.2).rads).abs() < 0.001.rads)
			
			// Test rankines.
			println("Subtraction Rankines: " + (1.1.rankines - 2.3.rankines).abs())
			assert ((1.1.rankines - 2.3.rankines - (-1.2).rankines).abs() < 0.001.rankines)
			
			// Test rems.
			println("Subtraction Rems: " + (1.1.rems - 2.3.rems).abs())
			assert ((1.1.rems - 2.3.rems - (-1.2).rems).abs() < 0.001.rems)
			
			// Test revolutions.
			println("Subtraction Revolutions: " + (1.1.revolutions - 2.3.revolutions).abs())
			assert ((1.1.revolutions - 2.3.revolutions - (-1.2).revolutions).abs() < 0.001.revolutions)
			
			// Test roentgens.
			println("Subtraction Roentgens: " + (1.1.roentgens - 2.3.roentgens).abs())
			assert ((1.1.roentgens - 2.3.roentgens - (-1.2).roentgens).abs() < 0.001.roentgens)
			
			// Test rutherfords.
			println("Subtraction Rutherfords: " + (1.1.rutherfords - 2.3.rutherfords).abs())
			assert ((1.1.rutherfords - 2.3.rutherfords - (-1.2).rutherfords).abs() < 0.001.rutherfords)
			
			// Test second angle.
			println("Subtraction Second_angle: " + (1.1.second_angle - 2.3.second_angle).abs())
			assert ((1.1.second_angle - 2.3.second_angle - (-1.2).second_angle).abs() < 0.001.second_angle)
			
			// Test spheres.
			println("Subtraction Spheres: " + (1.1.spheres - 2.3.spheres).abs())
			assert ((1.1.spheres - 2.3.spheres - (-1.2).spheres).abs() < 0.001.spheres)
			
			// Test stokes.
			println("Subtraction Stokes: " + (1.1.stokes - 2.3.stokes).abs())
			assert ((1.1.stokes - 2.3.stokes - (-1.2).stokes).abs() < 0.001.stokes)
			
			// Test ton uk.
			println("Subtraction Ton uk: " + (1.1.ton_uk - 2.3.ton_uk).abs())
			assert ((1.1.ton_uk - 2.3.ton_uk - (-1.2).ton_uk).abs() < 0.001.ton_uk)
			
			// Test ton us.
			println("Subtraction Ton us: " + (1.1.ton_us - 2.3.ton_us).abs())
			assert ((1.1.ton_us - 2.3.ton_us - (-1.2).ton_us).abs() < 0.001.ton_us)
			
			// Test weeks.
			println("Subtraction Weeks: " + (1.1.weeks - 2.3.weeks).abs())
			assert ((1.1.weeks - 2.3.weeks - (-1.2).weeks).abs() < 0.001.weeks)
			
			// Test yards.
			println("Subtraction Yards: " + (1.1.yards - 2.3.yards).abs())
			assert ((1.1.yards - 2.3.yards - (-1.2).yards).abs() < 0.001.yards)
			
			// Test years.
			println("Subtraction Years: " + (1.1.years - 2.3.years).abs())
			assert ((1.1.years - 2.3.years - (-1.2).years).abs() < 0.001.years)
			      
		}
			  
	}
			  
	void testDivision() {
		  
		use(UnitsCategory) {

			// Test meters.
			println("Division Meters: " + 1.1.meters / 2.3.meters)
			assert (1.1.meters / 2.3.meters == 0.4782608695652174.meters / 1.0.meters)
			
			// Test kilometers.
			println("Division Kilometers: " + 1.1.kilometers / 2.3.kilometers)
			assert (1.1.kilometers / 2.3.kilometers == 0.4782608695652174.kilometers / 1.0.kilometers)
			
			// Test centimeters.
			println("Division Centimeters: " + 1.1.centimeters / 2.3.centimeters)
			assert (1.1.centimeters / 2.3.centimeters == 0.4782608695652174.centimeters / 1.0.centimeters)
			
			// Test millimeters.
			println("Division Millimeters: " + 1.1.millimeters / 2.3.millimeters)
			assert (1.1.millimeters / 2.3.millimeters == 0.4782608695652174.millimeters / 1.0.millimeters)
			
			// Test micrometers.
			println("Division Micrometers: " + 1.1.micrometers / 2.3.micrometers)
			assert (1.1.micrometers / 2.3.micrometers == 0.4782608695652174.micrometers / 1.0.micrometers)
			
			// Test decimeters.
			println("Division Decimeters: " + 1.1.decimeters / 2.3.decimeters)
			assert (1.1.decimeters / 2.3.decimeters == 0.4782608695652174.decimeters / 1.0.decimeters)
			
			// Test kilograms.
			println("Division Kilograms: " + 1.1.kilograms / 2.3.kilograms)
			assert (1.1.kilograms / 2.3.kilograms == 0.4782608695652174.kilograms / 1.0.kilograms)
			
			// Test milligrams.
			println("Division Milligrams: " + 1.1.milligrams / 2.3.milligrams)
			assert (1.1.milligrams / 2.3.milligrams == 0.4782608695652174.milligrams / 1.0.milligrams)
						
			// Test angstroms.
			println("Division Angstroms: " + 1.1.angstroms / 2.3.angstroms)
			assert (1.1.angstroms / 2.3.angstroms == 0.4782608695652174.angstroms / 1.0.angstroms)
			
			// Test pounds.
			println("Division Pounds: " + 1.1.pounds / 2.3.pounds)
			assert (1.1.pounds / 2.3.pounds == 0.4782608695652174.pounds / 1.0.pounds)
			
			// Test grams.
			println("Division Grams: " + 1.1.grams / 2.3.grams)
			assert (1.1.grams / 2.3.grams == 0.4782608695652174.grams / 1.0.grams)
			
			// Test amperes.
			println("Division Amperes: " + 1.1.amperes / 2.3.amperes)
			assert (1.1.amperes / 2.3.amperes == 0.4782608695652174.amperes / 1.0.amperes)
			
			// Test becquerels.
			println("Division Becquerels: " + 1.1.becquerels / 2.3.becquerels)
			assert (1.1.becquerels / 2.3.becquerels == 0.4782608695652174.becquerels / 1.0.becquerels)
			
			// Test kilobecquerels.
			println("Division Kilobecquerels: " + 1.1.kilobecquerels / 2.3.kilobecquerels)
			assert (1.1.kilobecquerels / 2.3.kilobecquerels == 0.4782608695652174.kilobecquerels / 1.0.kilobecquerels)
			
			// Test megabecquerels.
			println("Division Megabecquerels: " + 1.1.megabecquerels / 2.3.megabecquerels)
			assert (1.1.megabecquerels / 2.3.megabecquerels == 0.4782608695652174.megabecquerels / 1.0.megabecquerels)
			
			// Test gigabecquerels.
			println("Division Gigabecquerels: " + 1.1.gigabecquerels / 2.3.gigabecquerels)
			assert (1.1.gigabecquerels / 2.3.gigabecquerels == 0.4782608695652174.gigabecquerels / 1.0.gigabecquerels)
			
			// Test bits.
			println("Division Bits: " + 1.1.bits / 2.3.bits)
			assert (1.1.bits / 2.3.bits == 0.4782608695652174.bits / 1.0.bits)
			
			// Test kilobits.
			println("Division Kilobits: " + 1.1.kilobits / 2.3.kilobits)
			assert (1.1.kilobits / 2.3.kilobits == 0.4782608695652174.kilobits / 1.0.kilobits)
			
			// Test megabits.
			println("Division Megabits: " + 1.1.megabits / 2.3.megabits)
			assert (1.1.megabits / 2.3.megabits == 0.4782608695652174.megabits / 1.0.megabits)
			
			// Test gigabits.
			println("Division Gigabits: " + 1.1.gigabits / 2.3.gigabits)
			assert (1.1.gigabits / 2.3.gigabits == 0.4782608695652174.gigabits / 1.0.gigabits)
			
			// Test terabits.
			println("Division Terabits: " + 1.1.terabits / 2.3.terabits)
			assert (1.1.terabits / 2.3.terabits == 0.4782608695652174.terabits / 1.0.terabits)
			
			// Test candelas.
			println("Division Candelas: " + 1.1.candelas / 2.3.candelas)
			assert (1.1.candelas / 2.3.candelas == 0.4782608695652174.candelas / 1.0.candelas)
			
			// Test celsius.
			println("Division Celsius: " + 1.1.celsius / 2.3.celsius)
			assert (1.1.celsius / 2.3.celsius == 0.4782608695652174.celsius / 1.0.celsius)
			
			// Test coulombs.
			println("Division Coulombs: " + 1.1.coulombs / 2.3.coulombs)
			assert (1.1.coulombs / 2.3.coulombs == 0.4782608695652174.coulombs / 1.0.coulombs)
			
			// Test cubic meters.
			println("Division Cubic_meters: " + 1.1.cubic_meters / 2.3.cubic_meters)
			assert (1.1.cubic_meters / 2.3.cubic_meters == 0.4782608695652174.cubic_meters / 1.0.cubic_meters)
			
			// Test farads.
			println("Division Farads: " + 1.1.farads / 2.3.farads)
			assert (1.1.farads / 2.3.farads == 0.4782608695652174.farads / 1.0.farads)
			
			// Test microfarads.
			println("Division Microfarads: " + 1.1.microfarads / 2.3.microfarads)
			assert (1.1.microfarads / 2.3.microfarads == 0.4782608695652174.microfarads / 1.0.microfarads)
			
			// Test nanofarads.
			println("Division Nanofarads: " + 1.1.nanofarads / 2.3.nanofarads)
			assert (1.1.nanofarads / 2.3.nanofarads == 0.4782608695652174.nanofarads / 1.0.nanofarads)
			
			// Test picofarads.
			println("Division Picofarads: " + 1.1.picofarads / 2.3.picofarads)
			assert (1.1.picofarads / 2.3.picofarads == 0.4782608695652174.picofarads / 1.0.picofarads)
			
			// Test grays.
			println("Division Grays: " + 1.1.grays / 2.3.grays)
			assert (1.1.grays / 2.3.grays == 0.4782608695652174.grays / 1.0.grays)
			
			// Test milligrays.
			println("Division Milligrays: " + 1.1.milligrays / 2.3.milligrays)
			assert (1.1.milligrays / 2.3.milligrays == 0.4782608695652174.milligrays / 1.0.milligrays)
			
			// Test henrys.
			println("Division Henrys: " + 1.1.henrys / 2.3.henrys)
			assert (1.1.henrys / 2.3.henrys == 0.4782608695652174.henrys / 1.0.henrys)
			
			// Test hertz.
			println("Division Hertz: " + 1.1.hertz / 2.3.hertz)
			assert (1.1.hertz / 2.3.hertz == 0.4782608695652174.hertz / 1.0.hertz)
			
			// Test kilohertz.
			println("Division Kilohertz: " + 1.1.kilohertz / 2.3.kilohertz)
			assert (1.1.kilohertz / 2.3.kilohertz == 0.4782608695652174.kilohertz / 1.0.kilohertz)
			
			// Test megahertz.
			println("Division Megahertz: " + 1.1.megahertz / 2.3.megahertz)
			assert (1.1.megahertz / 2.3.megahertz == 0.4782608695652174.megahertz / 1.0.megahertz)
			
			// Test joules.
			println("Division Joules: " + 1.1.joules / 2.3.joules)
			assert (1.1.joules / 2.3.joules == 0.4782608695652174.joules / 1.0.joules)
			
			// Test kilojoules.
			println("Division Kilojoules: " + 1.1.kilojoules / 2.3.kilojoules)
			assert (1.1.kilojoules / 2.3.kilojoules == 0.4782608695652174.kilojoules / 1.0.kilojoules)
			
			// Test katals.
			println("Division Katals: " + 1.1.katals / 2.3.katals)
			assert (1.1.katals / 2.3.katals == 0.4782608695652174.katals / 1.0.katals)
			
			// Test kelvin.
			println("Division Kelvin: " + 1.1.kelvin / 2.3.kelvin)
			assert (1.1.kelvin / 2.3.kelvin == 0.4782608695652174.kelvin / 1.0.kelvin)
			
			// Test lumens.
			println("Division Lumens: " + 1.1.lumens / 2.3.lumens)
			assert (1.1.lumens / 2.3.lumens == 0.4782608695652174.lumens / 1.0.lumens)
			
			// Test lux.
			println("Division Lux: " + 1.1.lux / 2.3.lux)
			assert (1.1.lux / 2.3.lux == 0.4782608695652174.lux / 1.0.lux)
			
			// Test microlux.
			println("Division Microlux: " + 1.1.microlux / 2.3.microlux)
			assert (1.1.microlux / 2.3.microlux == 0.4782608695652174.microlux / 1.0.microlux)
			
			// Test millilux.
			println("Division Millilux: " + 1.1.millilux / 2.3.millilux)
			assert (1.1.millilux / 2.3.millilux == 0.4782608695652174.millilux / 1.0.millilux)
			
			// Test kilolux.
			println("Division Kilolux: " + 1.1.kilolux / 2.3.kilolux)
			assert (1.1.kilolux / 2.3.kilolux == 0.4782608695652174.kilolux / 1.0.kilolux)
			
			// Test meter per second.
			println("Division Meter_per_second: " + 1.1.meter_per_second / 2.3.meter_per_second)
			assert (1.1.meter_per_second / 2.3.meter_per_second == 0.4782608695652174.meter_per_second / 1.0.meter_per_second)
			
			// Test meter per square second.
			println("Division Meter_per_square_second: " + 1.1.meter_per_square_second / 2.3.meter_per_square_second)
			assert (1.1.meter_per_square_second / 2.3.meter_per_square_second == 0.4782608695652174.meter_per_square_second / 1.0.meter_per_square_second)
			
			// Test moles.
			println("Division Moles: " + 1.1.moles / 2.3.moles)
			assert (1.1.moles / 2.3.moles == 0.4782608695652174.moles / 1.0.moles)
			
			// Test newtons.
			println("Division Newtons: " + 1.1.newtons / 2.3.newtons)
			assert (1.1.newtons / 2.3.newtons == 0.4782608695652174.newtons / 1.0.newtons)
			
			// Test ohms.
			println("Division Ohms: " + 1.1.ohms / 2.3.ohms)
			assert (1.1.ohms / 2.3.ohms == 0.4782608695652174.ohms / 1.0.ohms)
			
			// Test pascals.
			println("Division Pascals: " + 1.1.pascals / 2.3.pascals)
			assert (1.1.pascals / 2.3.pascals == 0.4782608695652174.pascals / 1.0.pascals)
			
			// Test kilopascals.
			println("Division Kilopascals: " + 1.1.kilopascals / 2.3.kilopascals)
			assert (1.1.kilopascals / 2.3.kilopascals == 0.4782608695652174.kilopascals / 1.0.kilopascals)
			
			// Test radians.
			println("Division Radians: " + 1.1.radians / 2.3.radians)
			assert (1.1.radians / 2.3.radians == 0.4782608695652174.radians / 1.0.radians)
			
			// Test milliradians.
			println("Division Milliradians: " + 1.1.milliradians / 2.3.milliradians)
			assert (1.1.milliradians / 2.3.milliradians == 0.4782608695652174.milliradians / 1.0.milliradians)
			
			// Test microradians.
			println("Division Microradians: " + 1.1.microradians / 2.3.microradians)
			assert (1.1.microradians / 2.3.microradians == 0.4782608695652174.microradians / 1.0.microradians)
			
			// Test nanoradians.
			println("Division Nanoradians: " + 1.1.nanoradians / 2.3.nanoradians)
			assert (1.1.nanoradians / 2.3.nanoradians == 0.4782608695652174.nanoradians / 1.0.nanoradians)
			
			// Test seconds.
			println("Division Seconds: " + 1.1.seconds / 2.3.seconds)
			assert (1.1.seconds / 2.3.seconds == 0.4782608695652174.seconds / 1.0.seconds)
			
			// Test siemens.
			println("Division Siemens: " + 1.1.siemens / 2.3.siemens)
			assert (1.1.siemens / 2.3.siemens == 0.4782608695652174.siemens / 1.0.siemens)
			
			// Test sieverts.
			println("Division Sieverts: " + 1.1.sieverts / 2.3.sieverts)
			assert (1.1.sieverts / 2.3.sieverts == 0.4782608695652174.sieverts / 1.0.sieverts)
			
			// Test millisieverts.
			println("Division Millisieverts: " + 1.1.millisieverts / 2.3.millisieverts)
			assert (1.1.millisieverts / 2.3.millisieverts == 0.4782608695652174.millisieverts / 1.0.millisieverts)
			
			// Test microsieverts.
			println("Division Microsieverts: " + 1.1.microsieverts / 2.3.microsieverts)
			assert (1.1.microsieverts / 2.3.microsieverts == 0.4782608695652174.microsieverts / 1.0.microsieverts)
			
			// Test square meters.
			println("Division Square_meters: " + 1.1.square_meters / 2.3.square_meters)
			assert (1.1.square_meters / 2.3.square_meters == 0.4782608695652174.square_meters / 1.0.square_meters)
			
			// Test steradians.
			println("Division Steradians: " + 1.1.steradians / 2.3.steradians)
			assert (1.1.steradians / 2.3.steradians == 0.4782608695652174.steradians / 1.0.steradians)
			
			// Test teslas.
			println("Division Teslas: " + 1.1.teslas / 2.3.teslas)
			assert (1.1.teslas / 2.3.teslas == 0.4782608695652174.teslas / 1.0.teslas)
			
			// Test volts.
			println("Division Volts: " + 1.1.volts / 2.3.volts)
			assert (1.1.volts / 2.3.volts == 0.4782608695652174.volts / 1.0.volts)
			
			// Test kilovolts.
			println("Division Kilovolts: " + 1.1.kilovolts / 2.3.kilovolts)
			assert (1.1.kilovolts / 2.3.kilovolts == 0.4782608695652174.kilovolts / 1.0.kilovolts)
			
			// Test watts.
			println("Division Watts: " + 1.1.watts / 2.3.watts)
			assert (1.1.watts / 2.3.watts == 0.4782608695652174.watts / 1.0.watts)
			
			// Test kilowatts.
			println("Division Kilowatts: " + 1.1.kilowatts / 2.3.kilowatts)
			assert (1.1.kilowatts / 2.3.kilowatts == 0.4782608695652174.kilowatts / 1.0.kilowatts)
			
			// Test megawatts.
			println("Division Megawatts: " + 1.1.megawatts / 2.3.megawatts)
			assert (1.1.megawatts / 2.3.megawatts == 0.4782608695652174.megawatts / 1.0.megawatts)
			
			// Test webers.
			println("Division Webers: " + 1.1.webers / 2.3.webers)
			assert (1.1.webers / 2.3.webers == 0.4782608695652174.webers / 1.0.webers)
			
			// Test ares.
			println("Division Ares: " + 1.1.ares / 2.3.ares)
			assert (1.1.ares / 2.3.ares == 0.4782608695652174.ares / 1.0.ares)
			
			// Test astronomical_units.
			println("Division Astronomical_units: " + 1.1.astronomical_units / 2.3.astronomical_units)
			assert (1.1.astronomical_units / 2.3.astronomical_units == 0.4782608695652174.astronomical_units / 1.0.astronomical_units)
			
			// Test atmospheres.
			println("Division Atmospheres: " + 1.1.atmospheres / 2.3.atmospheres)
			assert (1.1.atmospheres / 2.3.atmospheres == 0.4782608695652174.atmospheres / 1.0.atmospheres)
			
			// Test atoms.
			println("Division Atoms: " + 1.1.atoms / 2.3.atoms)
			assert (1.1.atoms / 2.3.atoms == 0.4782608695652174.atoms / 1.0.atoms)
			
			// Test atomic mass.
			println("Division Atomic_mass: " + 1.1.atomic_mass / 2.3.atomic_mass)
			assert (1.1.atomic_mass / 2.3.atomic_mass == 0.4782608695652174.atomic_mass / 1.0.atomic_mass)
			
			// Test bars.
			println("Division Bars: " + 1.1.bars / 2.3.bars)
			assert (1.1.bars / 2.3.bars == 0.4782608695652174.bars / 1.0.bars)
			
			// Test bytes.
			println("Division Bytes: " + 1.1.bytes / 2.3.bytes)
			assert (1.1.bytes / 2.3.bytes == 0.4782608695652174.bytes / 1.0.bytes)
			
			// Test c.
			println("Division C: " + 1.1.c / 2.3.c)
			assert (1.1.c / 2.3.c == 0.4782608695652174.c / 1.0.c)
			
			// Test centiradians.
			println("Division Centiradians: " + 1.1.centiradians / 2.3.centiradians)
			assert (1.1.centiradians / 2.3.centiradians == 0.4782608695652174.centiradians / 1.0.centiradians)
			
			// Test computer point.
			println("Division Computer_point: " + 1.1.computer_point / 2.3.computer_point)
			assert (1.1.computer_point / 2.3.computer_point == 0.4782608695652174.computer_point / 1.0.computer_point)
			
			// Test cubic inches.
			println("Division Cubic_inches: " + 1.1.cubic_inches / 2.3.cubic_inches)
			assert (1.1.cubic_inches / 2.3.cubic_inches == 0.4782608695652174.cubic_inches / 1.0.cubic_inches)
			
			// Test curies.
			println("Division Curies: " + 1.1.curies / 2.3.curies)
			assert (1.1.curies / 2.3.curies == 0.4782608695652174.curies / 1.0.curies)
			
			// Test days.
			println("Division Days: " + 1.1.days / 2.3.days)
			assert (1.1.days / 2.3.days == 0.4782608695652174.days / 1.0.days)
			
			// Test day sidereal.
			println("Division Day_sidereal: " + 1.1.day_sidereal / 2.3.day_sidereal)
			assert (1.1.day_sidereal / 2.3.day_sidereal == 0.4782608695652174.day_sidereal / 1.0.day_sidereal)
			
			// Test decibels.
			println("Division Decibels: " + 1.1.decibels / 2.3.decibels)
			assert (1.1.decibels / 2.3.decibels == 0.4782608695652174.decibels / 1.0.decibels)
			
			// Test degree angle.
			println("Division Degree_angle: " + 1.1.degree_angle / 2.3.degree_angle)
			assert (1.1.degree_angle / 2.3.degree_angle == 0.4782608695652174.degree_angle / 1.0.degree_angle)
			
			// Test dynes.
			println("Division Dynes: " + 1.1.dynes / 2.3.dynes)
			assert (1.1.dynes / 2.3.dynes == 0.4782608695652174.dynes / 1.0.dynes)
			
			// Test e.
			println("Division E: " + 1.1.e / 2.3.e)
			assert (1.1.e / 2.3.e == 0.4782608695652174.e / 1.0.e)
			
			// Test electron mass.
			println("Division Electron_mass: " + 1.1.electron_mass / 2.3.electron_mass)
			assert (1.1.electron_mass / 2.3.electron_mass == 0.4782608695652174.electron_mass / 1.0.electron_mass)
			
			// Test electron volt.
			println("Division Electron_volt: " + 1.1.electron_volt / 2.3.electron_volt)
			assert (1.1.electron_volt / 2.3.electron_volt == 0.4782608695652174.electron_volt / 1.0.electron_volt)
			
			// Test ergs.
			println("Division Ergs: " + 1.1.ergs / 2.3.ergs)
			assert (1.1.ergs / 2.3.ergs == 0.4782608695652174.ergs / 1.0.ergs)
			
			// Test fahrenheit.
			println("Division Fahrenheit: " + 1.1.fahrenheit / 2.3.fahrenheit)
			assert (1.1.fahrenheit / 2.3.fahrenheit == 0.4782608695652174.fahrenheit / 1.0.fahrenheit)
			
			// Test faradays.
			println("Division Faradays: " + 1.1.faradays / 2.3.faradays)
			assert (1.1.faradays / 2.3.faradays == 0.4782608695652174.faradays / 1.0.faradays)
			
			// Test feet.
			println("Division Feet: " + 1.1.feet / 2.3.feet)
			assert (1.1.feet / 2.3.feet == 0.4782608695652174.feet / 1.0.feet)
			
			// Test foot_survey_us.
			println("Division Foot_survey_us: " + 1.1.foot_survey_us / 2.3.foot_survey_us)
			assert (1.1.foot_survey_us / 2.3.foot_survey_us == 0.4782608695652174.foot_survey_us / 1.0.foot_survey_us)
			
			// Test franklins.
			println("Division Franklins: " + 1.1.franklins / 2.3.franklins)
			assert (1.1.franklins / 2.3.franklins == 0.4782608695652174.franklins / 1.0.franklins)
			
			// Test g.
			println("Division G: " + 1.1.g / 2.3.g)
			assert (1.1.g / 2.3.g == 0.4782608695652174.g / 1.0.g)
			
			// Test gallon dry us.
			println("Division Gallon_dry_us: " + 1.1.gallon_dry_us / 2.3.gallon_dry_us)
			assert (1.1.gallon_dry_us / 2.3.gallon_dry_us == 0.4782608695652174.gallon_dry_us / 1.0.gallon_dry_us)
			
			// Test gallon liquid us.
			println("Division Gallon_liquid_us: " + 1.1.gallon_liquid_us / 2.3.gallon_liquid_us)
			assert (1.1.gallon_liquid_us / 2.3.gallon_liquid_us == 0.4782608695652174.gallon_liquid_us / 1.0.gallon_liquid_us)
			
			// Test gallon uk.
			println("Division Gallon_uk: " + 1.1.gallon_uk / 2.3.gallon_uk)
			assert (1.1.gallon_uk / 2.3.gallon_uk == 0.4782608695652174.gallon_uk / 1.0.gallon_uk)
			
			// Test gauss.
			println("Division Gauss: " + 1.1.gauss / 2.3.gauss)
			assert (1.1.gauss / 2.3.gauss == 0.4782608695652174.gauss / 1.0.gauss)
			
			// Test gilberts.
			println("Division Gilberts: " + 1.1.gilberts / 2.3.gilberts)
			assert (1.1.gilberts / 2.3.gilberts == 0.4782608695652174.gilberts / 1.0.gilberts)
			
			// Test grades.
			println("Division Grades: " + 1.1.grades / 2.3.grades)
			assert (1.1.grades / 2.3.grades == 0.4782608695652174.grades / 1.0.grades)
			
			// Test hectares.
			println("Division Hectares: " + 1.1.hectares / 2.3.hectares)
			assert (1.1.hectares / 2.3.hectares == 0.4782608695652174.hectares / 1.0.hectares)
			
			// Test horsepower.
			println("Division Horsepower: " + 1.1.horsepower / 2.3.horsepower)
			assert (1.1.horsepower / 2.3.horsepower == 0.4782608695652174.horsepower / 1.0.horsepower)
			
			// Test hours.
			println("Division Hours: " + 1.1.hours / 2.3.hours)
			assert (1.1.hours / 2.3.hours == 0.4782608695652174.hours / 1.0.hours)
			
			// Test inches.
			println("Division Inches: " + 1.1.inches / 2.3.inches)
			assert (1.1.inches / 2.3.inches == 0.4782608695652174.inches / 1.0.inches)
			
			// Test inches of mercury.
			println("Division Inch_of_mercury: " + 1.1.inch_of_mercury / 2.3.inch_of_mercury)
			assert (1.1.inch_of_mercury / 2.3.inch_of_mercury == 0.4782608695652174.inch_of_mercury / 1.0.inch_of_mercury)
			
			// Test kilogram force.
			println("Division Kilogram_force: " + 1.1.kilogram_force / 2.3.kilogram_force)
			assert (1.1.kilogram_force / 2.3.kilogram_force == 0.4782608695652174.kilogram_force / 1.0.kilogram_force)
			
			// Test knots.
			println("Division Knots: " + 1.1.knots / 2.3.knots)
			assert (1.1.knots / 2.3.knots == 0.4782608695652174.knots / 1.0.knots)
			
			// Test lamberts.
			println("Division Lamberts: " + 1.1.lamberts / 2.3.lamberts)
			assert (1.1.lamberts / 2.3.lamberts == 0.4782608695652174.lamberts / 1.0.lamberts)
			
			// Test light years.
			println("Division Light_years: " + 1.1.light_years / 2.3.light_years)
			assert (1.1.light_years / 2.3.light_years == 0.4782608695652174.light_years / 1.0.light_years)
			
			// Test liters.
			println("Division Liters: " + 1.1.liters / 2.3.liters)
			assert (1.1.liters / 2.3.liters == 0.4782608695652174.liters / 1.0.liters)
			
			// Test mach.
			println("Division Mach: " + 1.1.mach / 2.3.mach)
			assert (1.1.mach / 2.3.mach == 0.4782608695652174.mach / 1.0.mach)
			
			// Test maxwells.
			println("Division Maxwells: " + 1.1.maxwells / 2.3.maxwells)
			assert (1.1.maxwells / 2.3.maxwells == 0.4782608695652174.maxwells / 1.0.maxwells)
			
			// Test metric tons.
			println("Division Metric_tons: " + 1.1.metric_tons / 2.3.metric_tons)
			assert (1.1.metric_tons / 2.3.metric_tons == 0.4782608695652174.metric_tons / 1.0.metric_tons)
			
			// Test miles.
			println("Division Miles: " + 1.1.miles / 2.3.miles)
			assert (1.1.miles / 2.3.miles == 0.4782608695652174.miles / 1.0.miles)
			
			// Test millimeters of mercury.
			println("Division Millimeters_of_mercury: " + 1.1.millimeters_of_mercury / 2.3.millimeters_of_mercury)
			assert (1.1.millimeters_of_mercury / 2.3.millimeters_of_mercury == 0.4782608695652174.millimeters_of_mercury / 1.0.millimeters_of_mercury)
			
			// Test minutes.
			println("Division Minutes: " + 1.1.minutes / 2.3.minutes)
			assert (1.1.minutes / 2.3.minutes == 0.4782608695652174.minutes / 1.0.minutes)
			
			// Test minute angle.
			println("Division Minute_angle: " + 1.1.minute_angle / 2.3.minute_angle)
			assert (1.1.minute_angle / 2.3.minute_angle == 0.4782608695652174.minute_angle / 1.0.minute_angle)
			
			// Test months.
			println("Division Months: " + 1.1.months / 2.3.months)
			assert (1.1.months / 2.3.months == 0.4782608695652174.months / 1.0.months)
			
			// Test nautical miles.
			println("Division Nautical_miles: " + 1.1.nautical_miles / 2.3.nautical_miles)
			assert (1.1.nautical_miles / 2.3.nautical_miles == 0.4782608695652174.nautical_miles / 1.0.nautical_miles)
			
			// Test octets.
			println("Division Octets: " + 1.1.octets / 2.3.octets)
			assert (1.1.octets / 2.3.octets == 0.4782608695652174.octets / 1.0.octets)
			
			// Test ounces.
			println("Division Ounces: " + 1.1.ounces / 2.3.ounces)
			assert (1.1.ounces / 2.3.ounces == 0.4782608695652174.ounces / 1.0.ounces)
			
			// Test ounce liquid uk.
			println("Division Ounce_liquid_uk: " + 1.1.ounce_liquid_uk / 2.3.ounce_liquid_uk)
			assert (1.1.ounce_liquid_uk / 2.3.ounce_liquid_uk == 0.4782608695652174.ounce_liquid_uk / 1.0.ounce_liquid_uk)
			
			// Test ounce liquid us.
			println("Division Ounce_liquid_us: " + 1.1.ounce_liquid_us / 2.3.ounce_liquid_us)
			assert (1.1.ounce_liquid_us / 2.3.ounce_liquid_us == 0.4782608695652174.ounce_liquid_us / 1.0.ounce_liquid_us)
			
			// Test parsecs.
			println("Division Parsecs: " + 1.1.parsecs / 2.3.parsecs)
			assert (1.1.parsecs / 2.3.parsecs == 0.4782608695652174.parsecs / 1.0.parsecs)
			
			// Test percent.
			println("Division Percent: " + 1.1.percent / 2.3.percent)
			assert (1.1.percent / 2.3.percent == 0.4782608695652174.percent / 1.0.percent)
			
			// Test pixels.
			println("Division Pixels: " + 1.1.pixels / 2.3.pixels)
			assert (1.1.pixels / 2.3.pixels == 0.4782608695652174.pixels / 1.0.pixels)
			
			// Test points.
			println("Division Points: " + 1.1.points / 2.3.points)
			assert (1.1.points / 2.3.points == 0.4782608695652174.points / 1.0.points)
			
			// Test poise.
			println("Division Poise: " + 1.1.poise / 2.3.poise)
			assert (1.1.poise / 2.3.poise == 0.4782608695652174.poise / 1.0.poise)
			
			// Test pound force.
			println("Division Pound_force: " + 1.1.pound_force / 2.3.pound_force)
			assert (1.1.pound_force / 2.3.pound_force == 0.4782608695652174.pound_force / 1.0.pound_force)
			
			// Test rads.
			println("Division Rads: " + 1.1.rads / 2.3.rads)
			assert (1.1.rads / 2.3.rads == 0.4782608695652174.rads / 1.0.rads)
			
			// Test rankines.
			println("Division Rankines: " + 1.1.rankines / 2.3.rankines)
			assert (1.1.rankines / 2.3.rankines == 0.4782608695652174.rankines / 1.0.rankines)
			
			// Test rems.
			println("Division Rems: " + 1.1.rems / 2.3.rems)
			assert (1.1.rems / 2.3.rems == 0.4782608695652174.rems / 1.0.rems)
			
			// Test revolutions.
			println("Division Revolutions: " + 1.1.revolutions / 2.3.revolutions)
			assert (1.1.revolutions / 2.3.revolutions == 0.4782608695652174.revolutions / 1.0.revolutions)
			
			// Test roentgens.
			println("Division Roentgens: " + 1.1.roentgens / 2.3.roentgens)
			assert (1.1.roentgens / 2.3.roentgens == 0.4782608695652174.roentgens / 1.0.roentgens)
			
			// Test rutherfords.
			println("Division Rutherfords: " + 1.1.rutherfords / 2.3.rutherfords)
			assert (1.1.rutherfords / 2.3.rutherfords == 0.4782608695652174.rutherfords / 1.0.rutherfords)
			
			// Test second angle.
			println("Division Second_angle: " + 1.1.second_angle / 2.3.second_angle)
			assert (1.1.second_angle / 2.3.second_angle == 0.4782608695652174.second_angle / 1.0.second_angle)
			
			// Test spheres.
			println("Division Spheres: " + 1.1.spheres / 2.3.spheres)
			assert (1.1.spheres / 2.3.spheres == 0.4782608695652174.spheres / 1.0.spheres)
			
			// Test stokes.
			println("Division Stokes: " + 1.1.stokes / 2.3.stokes)
			assert (1.1.stokes / 2.3.stokes == 0.4782608695652174.stokes / 1.0.stokes)
			
			// Test ton uk.
			println("Division Ton_uk: " + 1.1.ton_uk / 2.3.ton_uk)
			assert (1.1.ton_uk / 2.3.ton_uk == 0.4782608695652174.ton_uk / 1.0.ton_uk)
			
			// Test ton us.
			println("Division Ton_us: " + 1.1.ton_us / 2.3.ton_us)
			assert (1.1.ton_us / 2.3.ton_us == 0.4782608695652174.ton_us / 1.0.ton_us)
			
			// Test weeks.
			println("Division Weeks: " + 1.1.weeks / 2.3.weeks)
			assert (1.1.weeks / 2.3.weeks == 0.4782608695652174.weeks / 1.0.weeks)
			
			// Test yards.
			println("Division Yards: " + 1.1.yards / 2.3.yards)
			assert (1.1.yards / 2.3.yards == 0.4782608695652174.yards / 1.0.yards)
			
			// Test years.
			println("Division Years: " + 1.1.years / 2.3.years)
			assert (1.1.years / 2.3.years == 0.4782608695652174.years / 1.0.years)
			
			// Test year_calendar.
			println("Division Year_calendar: " + 1.1.year_calendar / 2.3.year_calendar)
			assert (1.1.year_calendar / 2.3.year_calendar == 0.4782608695652174.year_calendar / 1.0.year_calendar)
			
			// Test year_sidereal.
			println("Division Year_sidereal: " + 1.1.year_sidereal / 2.3.year_sidereal)
			assert (1.1.year_sidereal / 2.3.year_sidereal == 0.4782608695652174.year_sidereal / 1.0.year_sidereal)
			
		}
		    
	}
	
	void testUnitConversions() {
		  
		use(UnitsCategory) {
			
			// Test kilograms.
			println("Conversion Kilograms: " + (1.1.kilograms + 1.2.pounds))
			assert((1.1.kilograms + 1.2.pounds) == 1.6443108439999996.kilograms)
			
			// Test pounds.
			println("Conversion Pounds: " + (1.1.pounds + 1.2.kilograms))
			assert(1.1.pounds + 1.2.kilograms)
			
			// Test feet.
			println("Conversion Feet: " + (1.1.feet + 1.2.meters))
			assert(1.1.feet + 1.2.meters)
			
			// Test meters.
			println("Conversion Meters: " + (1.1.meters + 1.2.feet))
			assert(1.1.meters + 1.2.feet)
			
			// Test yards.
			println("Conversion Yards: " + (1.1.yards + 1.2.meters))
			assert(1.1.yards + 1.2.meters)
			
			// Test inches.
			println("Conversion Inches: " + (1.1.inches + 1.2.centimeters))
			assert(1.1.inches + 1.2.centimeters)
			
			// Test centimeters.
			println("Conversion Centimeters: " + (1.1.centimeters + 1.2.inches))
			assert(1.1.centimeters + 1.2.inches)
			
			// Test kilometers.
			println("Conversion Kilometers: " + (1.1.kilometers + 1.2.miles))
			assert(1.1.kilometers + 1.2.miles)
			
			// Test miles.
			println("Conversion Miles: " + (1.1.miles + 1.2.kilometers))
			assert(1.1.miles + 1.2.kilometers)
			
			// Test fahrenheit.
			println("Conversion Fahrenheit: " + (1.1.fahrenheit + 1.2.celsius))
			assert(1.1.fahrenheit + 1.2.celsius)
			
			// Test celsius.
			println("Conversion Celsius: " + (1.1.celsius + 1.2.fahrenheit))
			assert(1.1.celsius + 1.2.fahrenheit)
			
			// Test kelvin.
			println("Conversion Kelvin: " + (1.1.kelvin + 1.2.rankines))
			assert(1.1.kelvin + 1.2.rankines)
			
			// Test rankines.
			println("Conversion Rankines: " + (1.1.rankines + 1.2.kelvin))
			assert(1.1.rankines + 1.2.kelvin)
			
			// Test grams.
			println("Conversion Grams: " + (1.1.grams + 1.2.ounces))
			assert(1.1.grams + 1.2.ounces)
			
			// Test ounces.
			println("Conversion Ounces: " + (1.1.ounces + 1.2.grams))
			assert(1.1.ounces + 1.2.grams)
			
			// Test seconds.
			println("Conversion Seconds: " + (1.1.seconds + 1.2.minutes))
			assert(1.1.seconds + 1.2.minutes)
			
			// Test minutes.
			println("Conversion Minutes: " + (1.1.minutes + 1.2.hours))
			assert(1.1.minutes + 1.2.hours)
			
			// Test hours.
			println("Conversion Hours: " + (1.1.hours + 1.2.days))
			assert(1.1.hours + 1.2.days)
			
			// Test days.
			println("Conversion Days: " + (1.1.days + 1.2.weeks))
			assert(1.1.days + 1.2.weeks)
			
			// Test weeks.
			println("Conversion Weeks: " + (1.1.weeks + 1.2.months))
			assert(1.1.weeks + 1.2.months)
			
			// Test months.
			println("Conversion Months: " + (1.1.months + 1.2.years))
			assert(1.1.months + 1.2.years)
			
			// Test years.
			println("Conversion Years: " + (1.1.years + 1.2.months))
			assert(1.1.years + 1.2.months)
			
			// Test g.
			println("Conversion G: " + (1.1.g + 1.2.meter_per_square_second))
			assert(1.1.g + 1.2.meter_per_square_second)
			
			// Test meter per square second.
			println("Conversion Meter_per_square_second: " + (1.1.meter_per_square_second + 1.2.g))
			assert(1.1.meter_per_square_second + 1.2.g)
			
			// Test liters.
			println("Conversion Liters: " + (1.1.liters + 1.2.gallon_liquid_us))
			assert(1.1.liters + 1.2.gallon_liquid_us)
			
			// Test gallon liquid us.
			println("Conversion Gallon_liquid_us: " + (1.1.gallon_liquid_us + 1.2.ounce_liquid_us))
			assert(1.1.gallon_liquid_us + 1.2.ounce_liquid_us)
			
			// Test ounce liquid us.
			println("Conversion Ounce_liquid_us: " + (1.1.ounce_liquid_us + 1.2.gallon_liquid_us))
			assert(1.1.ounce_liquid_us + 1.2.gallon_liquid_us)
			
			// Test gallon uk.
			println("Conversion Gallon_uk: " + (1.1.gallon_uk + 1.2.ounce_liquid_uk))
			assert(1.1.gallon_uk + 1.2.ounce_liquid_uk)
			
			// Test ounce_liquid_uk.
			println("Conversion Ounce_liquid_uk: " + (1.1.ounce_liquid_uk + 1.2.gallon_uk))
			assert(1.1.ounce_liquid_uk + 1.2.gallon_uk)
			
			// Test gallon_dry_us.
			println("Conversion Gallon_dry_us: " + (1.1.gallon_dry_us + 1.2.liters))
			assert(1.1.gallon_dry_us + 1.2.liters)
			
			// Test ares.
			println("Conversion Ares: " + (1.1.ares + 1.2.hectares))
			assert(1.1.ares + 1.2.hectares)
			
			// Test hectares.
			println("Conversion Hectares: " + (1.1.hectares + 1.2.ares))
			assert(1.1.hectares + 1.2.ares)
			
			// Test joules.
			println("Conversion Joules: " + (1.1.joules + 1.2.ergs))
			assert(1.1.joules + 1.2.ergs)
			
			// Test ergs.
			println("Conversion Ergs: " + (1.1.ergs + 1.2.joules))
			assert(1.1.ergs + 1.2.joules)
			
			// Test newtons.
			println("Conversion Newtons: " + (1.1.newtons + 1.2.dynes))
			assert(1.1.newtons + 1.2.dynes)
			
			// Test dynes.
			println("Conversion Dynes: " + (1.1.dynes + 1.2.newtons))
			assert(1.1.dynes + 1.2.newtons)
			
			// Test pound force.
			println("Conversion Pound_force: " + (1.1.pound_force + 1.2.kilogram_force))
			assert(1.1.pound_force + 1.2.kilogram_force)
			
			// Test kilogram force.
			println("Conversion Kilogram_force: " + (1.1.kilogram_force + 1.2.pound_force))
			assert(1.1.kilogram_force + 1.2.pound_force)
			
			// Test nautical miles.
			println("Conversion Nautical_miles: " + (1.1.nautical_miles + 1.2.kilometers))
			assert(1.1.nautical_miles + 1.2.kilometers)
			
			// Test parsecs.
			println("Conversion Parsecs: " + (1.1.parsecs + 1.2.astronomical_units))
			assert(1.1.parsecs + 1.2.astronomical_units)
			
			// Test astronomical units.
			println("Conversion Astronomical_Units: " + (1.1.astronomical_units + 1.2.parsecs))
			assert(1.1.astronomical_units + 1.2.parsecs)
			
			// Test angstroms.
			println("Conversion Angstroms: " + (1.1.angstroms + 1.2.millimeters))
			assert(1.1.angstroms + 1.2.millimeters)
			
			// Test light years.
			println("Conversion Light_years: " + (1.1.light_years + 1.2.astronomical_units))
			assert(1.1.light_years + 1.2.astronomical_units)
			
			// Test ton us.
			println("Conversion ton_us: " + (1.1.ton_us + 1.2.pounds))
			assert(1.1.ton_us + 1.2.pounds)
			
			// Test ton uk.
			println("Conversion ton_uk: " + (1.1.ton_uk + 1.2.pounds))
			assert(1.1.ton_us + 1.2.pounds)
			
			// Test pascals.
			println("Conversion pascals: " + (1.1.pascals + 1.2.inch_of_mercury))
			assert(1.1.pascals + 1.2.inch_of_mercury)
			
			// Test inch of mercury.
			println("Conversion inch_of_mercury: " + (1.1.inch_of_mercury + 1.2.pascals))
			assert(1.1.inch_of_mercury + 1.2.pascals)
			
			// Test bars.
			println("Conversion bars: " + (1.1.bars + 1.2.atmospheres))
			assert(1.1.bars + 1.2.atmospheres)
			
			// Test atmospheres.
			println("Conversion atmospheres: " + (1.1.atmospheres + 1.2.bars))
			assert(1.1.atmospheres + 1.2.bars)
			
			// Test cubic inches.
			println("Conversion Cubic_inches: " + (1.1.cubic_inches + 1.2.liters))
			assert(1.1.cubic_inches + 1.2.liters)
			
			// Test bytes.
			println("Conversion Bytes: " + (1.1.bytes + 1.2.bits))
			assert(1.1.bytes + 1.2.bits)
			
			// Test bits.
			println("Conversion Bits: " + (1.1.bits + 1.2.bytes))
			assert(1.1.bits + 1.2.bytes)
			
			// Test points.
			println("Conversion Points: " + (1.1.points + 1.2.pixels))
			assert(1.1.points + 1.2.pixels)
			
			// Test pixels.
			println("Conversion Pixels: " + (1.1.pixels + 1.2.points))
			assert(1.1.pixels + 1.2.points)
	      
		}

	}
	
}