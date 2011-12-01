package repast.simphony.groovy.math

import javax.measure.unit.*
import org.jscience.physics.amount.*
import org.jscience.mathematics.number.* 
import org.jscience.mathematics.vector.*

class RepastMathEMCTest extends GroovyTestCase {
	
	void testSimple() {
		
		RepastMathEMC.initUnits()
		def x = 2.3.meters + 1.2.kilometers
		println x
		assertEquals (x, 1202.3.meters)
		
	}
	
	void testMatrix(){
		
		RepastMathEMC.initMatrix()
		def c = [ [ 2.5.kilograms, 4.3.kilograms], [ 9.6.kilograms, 5.7.kilograms] ].denseMatrix
		println("Creation Dense Matrix of Kilograms: " + c + " " + c.getClass())

		def d = [ [ 2.5.kilograms, 4.3.kilograms], [ 9.6.kilograms, 5.7.kilograms] ].sparseMatrix(0.0.kilograms)
		println("Creation Sprase Matrix of Kilograms: " + d + " " + d.getClass())

		def e = [ [ 2.5.kilograms, 4.3.kilograms], [ 9.6.kilograms, 5.7.kilograms] ].matrix
		println("Creation Default (Dense) Matrix of Kilograms: " + e + " " + e.getClass())
		
		println( c.pow(3) )
		
		println( c + e )

		println( c - e )

		println( c * e )
		
		println( 0.5.kilograms * e )

		println( e * 0.5.kilograms )

		println( e * 0.5 )
		
		println( 0.5 * e )

		println( e ** 2 )
	}
	
	
}