/**
 * 
 */
package repast.simphony.relogo

import repast.simphony.relogo.factories.LinkFactory
import static repast.simphony.relogo.Utility.*
import static repast.simphony.relogo.UtilityG.*
import static java.lang.Math.*


/**
 * @author jozik
 *
 */
public class UtilityTest extends GroovyTestCase{
	
		/**
		show subtract-headings 80 60
		=> 20
		show subtract-headings 60 80
		=> -20
		show subtract-headings 5 355
		=> 10
		show subtract-headings 355 5
		=> -10
		show subtract-headings 180 0
		=> 180
		show subtract-headings 0 180
		=> 180
	 */
	public void testSubtractHeadings(){
		def a1 = [80, 60, 5, 355, 180, 0] as double[]
		def a2 = [60, 80, 355, 5, 0, 180] as double[]
		def r = [20, -20, 10, -10, 180, 180] as double[]
		
		a1.size().times{
			assertEquals(Utility.subtractHeadings(a1[it], a2[it]),r[it])
			println "${a1[it]} ${a2[it]} ${r[it]}"
		}
	}
		
	public void testFirst(){
		assert(first([1,2,3]) == 1)
		assert(first([1,2,3]).getClass() == Integer)
		assert(first([1.0,2.0,3.0]) == 1.0)
		assert(first([1.0,2.0,3.0]).getClass() == BigDecimal)
		assert(first([1.0d,2.0d,3.0d]).getClass() == Double)
		assert(first([]) == null)
		assert(first("hello") == "h")
		assert(first("") == null)
	}
	
	public void testFput(){
		assert(fput(3,[1,2,3]) == [3,1,2,3])
		assert(fput(67,[1]) == [67,1])
	}
	
	public void testLput(){
		assert(lput(3,[1,2,3]) == [1,2,3,3])
		assert(lput(67,[1]) == [1,67])
	}
	
	public void testItem(){
		assert(item(0,[1,2,3]) == 1)
		assert(item(1,[1,2,3]) == 2)
	}
	
	public void testLast(){
		assert(last([1,2,3]) == 3)
		assert(last([1,2,3]).getClass() == Integer)
		assert(last([1.0,2.0,3.0]) == 3.0)
		assert(last([1.0,2.0,3.0]).getClass() == BigDecimal)
		assert(last([1.0d,2.0d,3.0d]).getClass() == Double)
		assert(last([]) == null)
		assert(last("hello") == "o")
		assert(last("") == null)
	}
	
	public void testLength(){
		assert(length([1,2,3]) == 3)
		assert(length("hello") == 5)
	}
	
	public void testShuffle(){
		println shuffle([1,2,3,4,5])
	}

	public void testAnyQ(){
		assert(anyQ([]) == false)
		assert(anyQ([1]) == true)
		assert(anyQ([1,2]) == true)
		assert(anyQ([1,2,2] as Set) == true)
	}
	
	public void testCount(){
		assert(count([]) == 0)
		assert(count([1]) == 1)
		assert(count([1,2]) == 2)
		assert(count([1,2,2] as Set) == 2)
	}

		
	public void testEmptyQ(){
		assert(emptyQ([]) == true)
		assert(emptyQ('') == true)
		assert(emptyQ("") == true)
		assert(emptyQ([1]) == false)
		assert(emptyQ([1,2]) == false)
		assert(emptyQ('a') == false)
		assert(emptyQ("a") == false)
	}
	
	public void testIsStringQ(){
		assert(isStringQ("") == true)
		assert(isStringQ('') == true)
		assert(isStringQ('a') == true)
		assert(isStringQ("a") == true)
		assert(isStringQ(2) == false)
	}
	
	/*
		show substring "apartment" 1 5
		=> "part"
	 */
	public void testSubstring(){
		assert(substring("apartment",1,5) == "part")
	}
		
	/**
	 * Make sure that the Math static import is AFTER the Utility and Utility static imports!
	 */
	public void testMath(){
		
		// abs
		assert(abs(-7) == 7)
		assert(abs(5) == 5)
		
		// acos
		assert(acos(0) == 90.0)
		
		// asin
		assert(asin(0) == 0.0)
		
		/* atan
			show atan 1 -1
			=> 135
			show atan -1 1
			=> 315
		 */
		assert(atan(1, -1) == 135)
		assert(atan(-1, 1) == 315)
		 
		/* ceiling
			show ceiling 4.5
			=> 5
			show ceiling -4.5
			=> -4
		  */
		assert(ceiling(4.5) == 5)
		assert(ceiling(-4.5) == -4)
		
		/* cos
			show cos 180
			=> -1
		 */
		assert(cos(180) == -1)
		 
		/* floor
			show floor 4.5
			=> 4
			show floor -4.5
			=> -5
		 */
		assert(floor(4.5) == 4)
		assert(floor(-4.5) == -5)
		
		/* intPart
			show int 4.7
			=> 4
			show int -3.5
			=> -3
		 */
		assert(intPart(4.7) == 4)
		assert(intPart(-3.5) == -3)
		
		/* log
			show log 64 2
			=> 6
		 */
		assert(log(64,2) == 6)
		
		/* mod
			show 62 mod 5
			=> 2
			show -8 mod 3
			=> 1
		 */
		assert(mod(62,5) == 2)
		assert(mod(-8,3) == 1)
		 
		10.times{
			println newSeed()
		}
			
		/**
		show precision 1.23456789 3
		=> 1.235
		show precision 3834 -3
		=> 4000
	 */
	
		assert(precision(1.23456789,3) == 1.235)
		assert(precision(3834,-3) == 4000)
	
		int seed = newSeed()
		randomSeed(seed)
		def first = []
		10.times{
			first << random(100)
		}
		randomSeed(seed)
		def second =[]
		10.times{
			second << random(100)
		}
		assert(first == second)
		
		/* remainder
			show remainder 62 5
			=> 2
			show remainder -8 3
			=> -2
		 */
		assert(remainder(62,5) == 2)
		assert(remainder(-8,3) == -2)
		
		/* round
			show round 4.2
			=> 4
			show round 4.5
			=> 5
			show round -4.5
			=> -4
		 */
		assert(round(4.2) == 4)
		assert(round(4.5) == 5)
		assert(round(-4.5) == -4)
		
		/* sin
		 	show sin 270
		 	=> -1
		 */
		assert(sin(270) == -1)
		
		assert(sqrt(4) == 2)
	}
	
	public void testTryCatch(){
		try{
			assert(1==0)
		}
		catch(Throwable e){
			println e.getMessage()
		}
	}
	
	public void testWait(){
		println "starting to wait"
		def start = System.currentTimeMillis()
		wait(0.5)
		def end = System.currentTimeMillis()
		println "ending the wait after ${(end - start)/1000} seconds"
	}
	

	
	public void testButFirst(){
		assert(butFirst([1,2,3]) == [2,3])
		assert(butFirst([1]) == [])
		assert(butFirst("hello") == "ello")
		assert(butFirst("h") == "")
	}
	
	public void testButLast(){
		assert(butLast([1,2,3]) == [1,2])
		assert(butLast([1]) == [])
		assert(butLast("hello") == "hell")
		assert(butLast("h") == "")
	}
	
	
	public void testFilter(){
		assert(filter({it > 0},[-1,0,1]) == [1])
		assert(filter({it > 1},[-1,0,1]) == [])
	}
	
	public void testFlatten(){
		def list = [-1,0,1]
		def list2 = [list,[2]]
		assert(Utility.flatten([]) == [])
		assert(Utility.flatten(list) == list)
		assert(Utility.flatten(list2) == [*list,2])
		assert(Utility.flatten(list2) == [-1,0,1,2])
		
		def set = list as Set
		assert(Utility.flatten(set) == set)
	}
	
	
	public void testForeach(){
		/*
		 * Testing the equivalent of:
		 * (foreach [1 2 3] [6 7 8] [9 10 11] [show ?1 + ?3])
		 */
		def a = [1,2,3]
		def b = [6,7,8]
		def c = [9,10,11]
		def command = {Object... Q ->
			println Q[0] + Q[2]
		}
		println 'Testing foreach'
		foreach(command,a,b,c)
		foreach({Object... Q -> println Q[0] + Q[2]},a,b,c)
		foreach({println it}, a)
		foreach({q1,q2 -> println q1 + q2}, a, b)
	}
	
	public void testList(){
		assert(list(1,2,3) == [1,2,3])
	}

	public void testMap(){
		/*
		 * Testing the equivalent of:
		 * (foreach [1 2 3] [6 7 8] [9 10 11] [show ?1 + ?3])
		 */
		def a = [1,2,3]
		def b = [6,7,8]
		def c = [9,10,11]
		def command = {Object... Q ->
			Q[0] + Q[2]
		}
		assert(map(command,a,b,c) == [10,12,14])
		assert(map({Object... Q ->  Q[0] + Q[2]},a,b,c) ==[10,12,14]) 
		assert(map({it}, a) == [1,2,3])
		assert(map({it * it}, a) == [1,4,9])
		assert(map({q1,q2 -> q1 + q2}, a, b) == [7,9,11]) 
	}
	
	/**
		show member? 2 [1 2 3]
		=> true
		show member? 4 [1 2 3]
		=> false
		show member? "bat" "abate"
		=> true
		show member? turtle 0 turtles
		=> true
		show member? turtle 0 patches
		=> false
	 */
	public void testMemberQ(){
		assert(memberQ(2,[1,2,3]) == true)
		assert(memberQ(4,[1,2,3]) == false)
		assert(memberQ("bat","abate") == true)
		assert(memberQ("cat","abate") == false)
		assert(memberQ(1,new AgentSet([1,2,3])) == true)
		assert(memberQ(4,new AgentSet([1,2,3])) == false)
	}
		
	/**
		show modes [1 2 2 3 4]
		=> [2]
		show modes [1 2 2 3 3 4]
		=> [2 3]
		show modes [ [1 2 [3]] [1 2 [3]] [2 3 4] ]
		=> [[1 2 [3]]
	 */
	public void testModes(){
		assert(modes([]) == [])
		assert(modes([1,2,2,3,4]) == [2])
		assert(modes([1,2,2,3,3,4]) == [2,3])
		assert(modes([[1,2,[3]],[1,2,[3]],[2,3,4]]) == [[1,2,[3]]])
		assert(modes([1,2,2,3,3,4][0..4]) == [2,3])
	}
		
	public void testNOf(){
		assert(nOf(3,[1,2,3]) == [1,2,3])
		println nOf(3,new AgentSet([1,2,3,5,6]))
		println nOf(3,[1,1,2,3] as Set)
	}
	
	public void testNValues(){
		def a = {1}
		assert(nValues(5,{1}) == [1,1,1,1,1])
		assert(nValues(5,a) == [1,1,1,1,1])
		assert(nValues(5,{it}) == [0,1,2,3,4])
		assert(nValues(5,{it * it}) == [0, 1, 4, 9, 16])		
	}
	

//	public void testOf(){
//		assert(of({it * it}, new AgentSet([1,2,3])) == [1,4,9])
//	}
	
	public void testPosition(){
		assert(position(1,[1,2,3]) == 0)
		assert(position(1,[1,2,3,1]) == 0)
		assert(position(3,[1,2,3,1]) == 2)
		assert(position(4,[1,2,3]) == false)
		assert(position("in","string") == 3)
		assert(position("in","strong") == false)
	}
	
	public void testOneOf(){
		assert(oneOf(new AgentSet([1d,2d,3d])).getClass() == Double)
		assert(oneOf(new AgentSet([])) == null)
		assert(oneOf([1,2,3]).getClass() == Integer)
	}
	
	/**
		show reduce [?1 + ?2] [1 2 3]
		=> 6
		show reduce [?1 - ?2] [1 2 3]
		=> -4
		show reduce [?2 - ?1] [1 2 3]
		=> 2
		show reduce [?1] [1 2 3]
		=> 1
		show reduce [?2] [1 2 3]
		=> 3
		show reduce [sentence ?1 ?2] [[1 2] [3 [4]] 5]
		=> [1 2 3 [4] 5]
		show reduce [fput ?2 ?1] (fput [] [1 2 3 4 5])
		=> [5 4 3 2 1]
	 */
	public void testReduce(){
		assert(reduce({q1, q2 -> q1 + q2}, [1,2,3]) == 6)
		assert(reduce({q1, q2 -> q1 - q2}, [1,2,3]) == -4)
		assert(reduce({q1, q2 -> q2 - q1}, [1,2,3]) == 2)
		assert(reduce({q1, q2 -> q1}, [1,2,3]) == 1)
		assert(reduce({q1, q2 -> q2}, [1,2,3]) == 3)
		assert(reduce({q1, q2 -> sentence(q1,q2)}, [[1,2],[3,[4]],5]) == [1,2,3,[4],5])
		assert(reduce({q1, q2 -> Utility.fput(q2,q1)},Utility.fput([],[1,2,3,4,5])) == [5,4,3,2,1])
	}

	/**
		set mylist [2 7 4 7 "Bob"]
		set mylist remove 7 mylist
		;; mylist is now [2 4 "Bob"]
		show remove "to" "phototonic"
		=> "phonic"
	 */
	public void testRemove(){
		assert(remove(1,[1,2,3])==[2,3])
		assert(remove(1,[1,2,3,1])==[2,3])
		assert(remove(4,[1,2,3])==[1,2,3])
		assert(remove("ab", "abcab") == "c")
		assert(remove(7,[2,7,4,7,"Bob"]) == [2,4,"Bob"])
		assert(remove("to", "phototonic") == "phonic")
	}
	
	/**
		set mylist [2 7 4 7 "Bob" 7]
		set mylist remove-duplicates mylist
		;; mylist is now [2 7 4 "Bob"]
	 */
	public void testRemoveDuplicates(){
		assert(removeDuplicates([1,2,3,1]) == [1,2,3])
		assert(removeDuplicates([2,7,4,7,"Bob",7]) == [2,7,4,"Bob"])
	}
	
	/**
		set mylist [2 7 4 7 "Bob"]
		set mylist remove-item 2 mylist
		;; mylist is now [2 7 7 "Bob"]
		show remove-item 2 "string"
		=> "sting"
	 */
	public void testRemoveItem(){
		assert(removeItem(2, [1,2,3]) == [1,2])
		assert(removeItem(2,[2,7,4,7,"Bob"]) == [2,7,7,"Bob"])
		assert(removeItem(2,"string") == "sting")
	}
		
	/**
		show replace-item 2 [2 7 4 5] 15
		=> [2 7 15 5]
		show replace-item 1 "cat" "are"
		=> "caret"
	 */
	public void testReplaceItem(){
		assert(replaceItem(2,[2,7,4,5],15) == [2,7,15,5])
		assert(replaceItem(1,"cat","are") == "caret")
	}
	
	/**
		show mylist
		;; mylist is [2 7 4 "Bob"]
		set mylist reverse mylist
		;; mylist now is ["Bob" 4 7 2]
		show reverse "live"
		=> "evil"
	 */
	public void testReverse(){
		assert(reverse([2,7,4,"Bob"]) == ["Bob",4,7,2])
		assert(reverse("live") == "evil")
	}
	
	/**
		show sentence 1 2
		=> [1 2]
		show sentence [1 2] 3
		=> [1 2 3]
		show sentence 1 [2 3]
		=> [1 2 3]
		show sentence [1 2] [3 4]
		=> [1 2 3 4]
		show sentence [[1 2]] [[3 4]]
		=> [[1 2] [3 4]]
		show (sentence [1 2] 3 [4 5] (3 + 3) 7)
		=> [1 2 3 4 5 6 7]
	 */
	public void testSentence(){
		assert(sentence(1,2) == [1,2])
		assert(sentence([1,2],3) == [1,2,3])
		assert(sentence([1,2],[3,4]) == [1,2,3,4])
		assert(sentence([[1,2]],[[3,4]] ) == [[1,2],[3,4]])
		assert(sentence([1,2],3,[4,5], (3 + 3), 7 ) == [1,2,3,4,5,6,7])
	}
	
	/**
		show sort [3 1 4 2]
		=> [1 2 3 4]
	 */
	public void testSort(){
		assert(sort([3,1,4,2]) == [1,2,3,4])
		assert(sort(["car","boy","apple","dog"]) == ["apple","boy","car","dog"])
		
		// TODO: test the agentset sort
	}
		
	/**
		show sort-by [length ?1 < length ?2] ["Grumpy" "Doc" "Happy"]
		=> ["Doc" "Happy" "Grumpy"]
	 */
	public void testSortBy(){
		assert(sortBy({q1,q2 -> q1 < q2 ? -1 : 1}, [3,2,4,1]) == [1,2,3,4])
		assert(sortBy({q1,q2 -> q1.size() < q2.size() ? -1 : 1}, ["Grumpy", "Doc", "Happy"]) == ["Doc", "Happy", "Grumpy"])
		assert(sortBy({q1,q2 -> q1 < q2 ? -1 : 1}, new AgentSet([3,2,4,1])) == [1,2,3,4])
	}
	
	/**
		show sublist [99 88 77 66] 1 3
		=> [88 77]
	 */
	public void testSublist(){
		assert(sublist([1,2,3,4],0,1) == [1])
		assert(sublist([99,88,77,66],1,3) == [88,77])
	}
	
	/**
		show read-from-string "3" + read-from-string "5"
		=> 8
		show length read-from-string "[1 2 3]"
		=> 3
	 */
	public void readFromString(){
		assert(readFromString("3") + readFromString("5") == 8)
		assert(length(readFromString("[1,2,3]")) == 3)
		assert(readFromString('"str"') == "str")
	}
		
	/**
		show word "tur" "tle"
		=> "turtle"
		word "a" 6
		=> "a6"
		show word [1 54 8] "fishy"
		=> "[1 54 8]fishy"
		show (word 3)
		=> "3"
		show (word "a" "b" "c" 1 23)
		=> "abc123"
	 */
	public void testWord(){
		assert(word("tur","tle") == "turtle")
		assert(word("a",6) == "a6")
		assert(word([1,54,8],"fishy") == "[1, 54, 8]fishy")
		assert(word(3) == "3")
		assert(word("a","b","c",1,23) == "abc123")
	}
		
	public void testMax(){
		assert(max([1,2,3]) == 3)
		assert(max([1,2,3][0..1]) == 2)
		assert(max([1,3,3] as Set) == 3)
	}
	
	public void testMean(){
		assert(mean([1,2,3]) == 2)
		assert(mean([1,2,3,4,5,6]) == 3.5)
		
		assert(mean([1,2,3,3] as Set) == 2)
		
	}
	
	public void testMedian(){
		assert(median([1,2,3]) == 2)
		assert(median([1,2,3,4,5,6]) == 3.5)
		assert(median([1,1,1,2]) == 1)
		
		assert(median([1,1,2,3] as Set) == 2)
	}
	
	public void testMin(){
		assert(min([1,2,3]) == 1)
		assert(min([1,2,3][1..2]) == 2)
	}
	
	/**
		show standard-deviation [1 2 3 4 5 6]
		=> 1.8708286933869707
	 */
	public void testStandardDeviation(){
		assert(standardDeviation([1,2,3,4,5,6]) == 1.8708286933869707)
	}
		
	public void testSum(){
		assert(sum([1,2,3,4]) == 10)
	}
	
	/**
		show variance [2 7 4 3 5]
		=> 3.7
	 */
	public void testVariance(){
		assert(variance([2,7,4,3,5]) == 3.7)
	}
		
	public void testIfelseValue(){
		assert(ifelseValue({(1>0)},{34},{45}) == 34)
		assert(ifelseValue({1<0},{34},{45}) == 45)
	}
	
	public void testRepeat(){
		def a = 0
		repeat(3,{a++})
		assert(a == 3)
		repeat(3){
			a++
		}
		assert(a == 6)
	}
	
	public void testRunResult(){
		assert(runresultU('abs(-1)',null) == 1)
		assert(runresultU('min([1,2,3])',null) == 1)
		assert(runresultU('max([1,2,3])',null) == 3)
		assert(runresultU(word('max([','1,2,3])'),null) == 3)
		String string = reverse(reverse('max([1,2,3])'))
		assert(runresultU(string,null) == 3)
	}
	
	/*
	public void testWithLocalRandomness(){
		RandomHelper.setSeed(1234567)
		10.times {
			println random(10)
		}
		RandomHelper.getGenerator().
		def resultingSeed = RandomHelper.getDefaultRegistry().getSeed()
		println "resultingSeed = $resultingSeed"
	}
	*/
	
//	public void testFileReading(){
//		def file = new File('.') 
//		println file.name 
//		println file.absolutePath 
//		println file.canonicalPath 
//		println file.directory
//		file = new File('hello.txt')
//		def a = 67
//		file << a
//		file
//	}
	
	public void testFileOpen(){
		def string = './hello.txt'
		fileOpen(string)
		File file = ReLogoModel.getInstance().getCurrentFileInfo().getFile()
		assert(file.name == 'hello.txt')
		
		/*
		println ''
		println file.name
		println file.absolutePath 
		println file.canonicalPath 
		println file.directory
		*/
	}
	
	public void testFilePrint(){
		
		def file = './hello.txt'
		fileOpen(file)
		if (ReLogoModel.getInstance().getCurrentFileInfo().getFile().exists()){
			ReLogoModel.getInstance().getCurrentFileInfo().getFile().delete()
		}
		assertTrue(ReLogoModel.getInstance().getCurrentFileInfo().isFileReadable())
		
		def string = 'this is a test' 
		filePrint(string)
		assert(ReLogoModel.getInstance().getCurrentFileInfo().getFile().text == string + '\n')
		assertFalse(ReLogoModel.getInstance().getCurrentFileInfo().isFileReadable())
		
		filePrint('hello')
		def lines = [string,'hello']
		assert(ReLogoModel.getInstance().getCurrentFileInfo().getFile().readLines() == lines)
		
		ReLogoModel.getInstance().getCurrentFileInfo().getFile().delete()
		assert(ReLogoModel.getInstance().getFileInfoList().size() > 0)
		fileCloseAll()
		assert(ReLogoModel.getInstance().getFileInfoList().size() == 0)
	}
	
	public void testFilePrint2(){
		def string = 'hello.txt'
		if (fileExistsQ(string)){
			fileDelete(string)
		}
		fileOpen(string)
		def number = 42
		filePrint(number)
		fileClose()
		fileOpen(string)
		assert(fileReadCharacters(4) == '42\n')
		
		fileCloseAll()
		fileDelete(string)
		
	}
	
	public void testFileExistsQ(){
		// testing nonexistent file
		String string = 'nonexistent'
		assert(fileExistsQ(string) == false)
		
		// testing file before and after creation
		string = 'hello1.txt'
		fileOpen(string)
		assertFalse(fileExistsQ(string))
		filePrint('hello1')
		assertTrue(fileExistsQ(string))
		
		// testing if the current directory is properly consulted when a non absolute path string is supplied
		String curDir = ReLogoModel.getInstance().getCurrentFileInfo().getFile().getParent() + '/temp/'
//		File currentDir = new File(Model.getInstance().getCurrentFileInfo().getFile().getParent() + '/temp/')
		setCurrentDirectory(curDir)
//		println ""
//		println ("the canonical path of the current directory is: " + Model.getInstance().getCurrentDirectory().getCanonicalPath())
		assertFalse(fileExistsQ(string))
		
		// deleting the file hello1.txt
		ReLogoModel.getInstance().getCurrentFileInfo().getFile().delete()
		assertFalse(fileExistsQ(ReLogoModel.getInstance().getCurrentFileInfo().getFile().getCanonicalPath()))
		
		// testing another current directory usage
		string = 'hello2.txt'
		fileOpen(string)
		filePrint('hello2')
		assertTrue(fileExistsQ(string))

		// deleting the created test file and directory
		assertTrue(ReLogoModel.getInstance().getCurrentFileInfo().getFile().delete())
		assertTrue(ReLogoModel.getInstance().getCurrentFileInfo().getFile().getParentFile().delete())
		
		ReLogoModel.getInstance().setCurrentDirectory(null)
		
		// resetting the Model instance
		fileCloseAll()

	}
	
	public void testBufferedReading(){
		
		def string = 'hello.txt'
		fileOpen(string)
		if (ReLogoModel.getInstance().getCurrentFileInfo().getFile().exists()){
			ReLogoModel.getInstance().getCurrentFileInfo().getFile().delete()
		}
		filePrint('first line')
		filePrint('second line')
		filePrint('third line')
		def file = ReLogoModel.getInstance().getCurrentFileInfo().getFile()
		def br = file.newReader()
		assert ( br.readLine() == 'first line' )
		assert ( br.readLine() == 'second line' )
		assertTrue(ReLogoModel.getInstance().getCurrentFileInfo().getFile().delete())
		
		fileCloseAll()
	}
	

	public void testFileAtEndQ(){
		def string = 'hello.txt'
		fileOpen(string)
		if (ReLogoModel.getInstance().getCurrentFileInfo().getFile().exists()){
			ReLogoModel.getInstance().getCurrentFileInfo().getFile().delete()
		}
		filePrint('first line')
		fileClose()
		fileOpen(string)

		assert(fileAtEndQ() == false)
		ReLogoModel.getInstance().getCurrentFileInfo().getBufferedReader().readLine()
		assert(fileAtEndQ() == true)
		ReLogoModel.getInstance().getCurrentFileInfo().getFile().delete()
		
		fileCloseAll()
	}
	
	public void testFileDelete(){
		def string = 'hello.txt'
		fileOpen(string)
		if (ReLogoModel.getInstance().getCurrentFileInfo().getFile().exists()){
			ReLogoModel.getInstance().getCurrentFileInfo().getFile().delete()
		}
		filePrint("hello")
		assertTrue(fileExistsQ(string))
		fileDelete(string)
		assertTrue(fileExistsQ(string))
		fileClose()
		fileDelete(string)
		assertFalse(fileExistsQ(string))
		
		fileCloseAll()
	}
	
	public void testFileReadCharacters(){
		def string = 'hello.txt'
		fileOpen(string)
		if (ReLogoModel.getInstance().getCurrentFileInfo().getFile().exists()){
			ReLogoModel.getInstance().getCurrentFileInfo().getFile().delete()
		}
		filePrint("hello")
		fileClose()
		fileOpen(string)
		assert(fileReadCharacters(3) == "hel")
		fileClose()
		fileOpen(string)
		assert(fileReadCharacters(72) == "hello\n")
		fileClose()
		fileDelete(string)
		fileCloseAll()
	}
	
	public void testFileWriteAndRead(){
		def string = 'hello.txt'
		fileOpen(string)
		if (ReLogoModel.getInstance().getCurrentFileInfo().getFile().exists()){
			ReLogoModel.getInstance().getCurrentFileInfo().getFile().delete()
		}
		String testString = 'test string'
		fileWrite(testString)
		def list = [1,2,3,4]
		fileWrite(list)
		def special = null
		fileWrite(special)
		fileWrite(testString)
		fileClose()
		
		fileOpen(string)
		def result = fileRead()
		assert(result.getClass() == String)
		assert(result == testString)
		result = fileRead()
		assert(result.getClass() == ArrayList)
		assert(result == list)
		result = fileRead()
		assert(result == null)
		result = fileRead()
		assert(result.getClass() == String)
		assert(result == testString)
		
		fileCloseAll()
		fileDelete(string)
	}
	
	public void testReadLine(){
		def string = 'hello.txt'
		if (fileExistsQ(string)){
			fileDelete(string)
		}
		fileOpen(string)
		filePrint('first line')
		filePrint('second line')
		fileClose()
		fileOpen(string)
		assert(fileReadLine() == 'first line')
		assert(fileReadLine() == 'second line')
		
		fileCloseAll()
		fileDelete(string)
	}
	
	
	
	public void testFileType(){
		def string = 'hello.txt'
		if (fileExistsQ(string)){
			fileDelete(string)
		}
		fileOpen(string)
		fileType('hello')
		fileType(' ')
		fileType('there')
		fileClose()
		fileOpen(string)
		assert(fileReadLine() == 'hello there')
		fileClose()
		fileOpen(string)
		assert(fileReadCharacters(20) == 'hello there')
		
		fileCloseAll()
		fileDelete(string)
	}
	
	// uncomment to run test
	public void testUserDirectory_UserFile_UserNewFile(){
//		println userDirectory()
//		println userFile()
//		println userNewFile()
	}
	
	public void testFileShow(){
		String id = 'turtle 1'
		def string = 'hello.txt'
		if (fileExistsQ(string)){
			fileDelete(string)
		}
		fileOpen(string)
		String testString = 'test string'
		fileShowU(id,testString)
		def list = [1,2,3,4]
		fileShowU(id,list)
		def special = null
		fileShowU(id, special)
		fileClose()
		
		fileOpen(string)
		assert(fileReadLine() == '(turtle 1): "test string"')
		assert(fileReadLine() == '(turtle 1): [1,2,3,4]')
		assert(fileReadLine() == '(turtle 1): null')
		
		fileCloseAll()
		fileDelete(string)
	}
	
	public void testShow(){
		println ''
		println '*** In testShow.'
		String id = 'turtle 1'
		String testString = 'test string'
		showU(id,testString)
		def list = [1,2,3,4]
		showU(id,list)
		def special = null
		showU(id, special)
		println '*** End of testShow.'
	}
	
	public void testDateAndTime(){
		println ''
		println '*** In testDateAndTime.'
		println dateAndTime()
		println '*** End of testDateAndTime.'
	}
	
	public void testPrint(){
		println ''
		println '*** In testPrint.'
		def list = [1,2,3,4]
		def str = "print string"
		print(list)
		print(str)
		println '*** End of testPrint.'
	}
	
	public void testTimer(){
		println ''
		println '*** In testTimer.'
		println timer()
		resetTimer()
		wait(0.1)
		println timer()
		println '*** End of testTimer.'
	}
		
	public void testType(){
		println ''
		println '*** In testType.'
		def list = [1,2,3,4]
		def str = "print string"
		type(list)
		type(' ')
		type(str)
		type('\n')
		println '*** End of testType.'
	}
	
	/**
	 * Uncomment to run
	 */
//	public void testUserInput(){
//		println ''
//		println '*** In testUserInput.'
//		def str = userInput("Dialog text")
//		println "str.getClass() = ${str.getClass()}"
//		println "str = $str"
//		println '*** End of testUserInput.'
//	}
	
	/**
	 * Uncomment to run
	 */
//	public void testUserMessage(){
//		println ''
//		println '*** In testUserMessage.'
//		def list = [1,2,3,4]
//		def str = userMessage("Dialog text and ${list}")
//		println "str.getClass() = ${str.getClass()}"
//		println "str = $str"
//		println '*** End of testUserMessage.'
//	}
	
	/**
	 * Uncomment to run
	 */
//	public void testUserOneOf(){
//		println ''
//		println '*** In testUserOneOf.'
//		Object[] list = [1,2,3,4]
//		def result = userOneOf("Dialog text",list)
//		assert(result.getClass() == Integer)
//		println result
//		println '*** End of testUserOneOf.'
//	}
	
	/**
	 * Uncomment to run
	 */
//	public void testUserYesOrNoQ(){
//		println ''
//		println '*** In testUserYesOrNoQ.'
//		def result = userYesOrNoQ("The question")
//		assert(result.getClass() == Boolean)
//		println result
//		println '*** End of testUserYesOrNoQ.'
//	}
	
	public void testWrite(){
		println ''
		println '*** In testWrite.'
		def list = [1,2,3,4]
		def str = "print string"
		write(list)
		write(str)
		println ''
		println '*** End of testWrite.'
	}
	
	/*public void testIs(){
		boolean a = true
		assert(isBooleanQ(a) == true)
		Boolean b = true
		assert(isBooleanQ(b) == true)
		AgentSet aS = new AgentSet()
		assert(isAgentSetQ(aS) == true)
		LinkFactory lf = new LinkFactoryImpl(BaseLink)
		lf.init(new BaseObserver()) //initialization needed to create links
		AgentSet lS = linkSet(lf.createLink("from","to",true))
		assert(isLinkSetQ(lS) == true)
		assert(isAgentSetQ(lS) == true)
		assert(isTurtleSetQ(lS) == false)
		// empty linkSet
		lS = linkSet()
		assert(isLinkSetQ(lS) == true)
		assert(isAgentSetQ(lS) == true)
		assert(isTurtleSetQ(lS) == true)
	}*/
	
	// Test if the breed call in BaseTurtle properly creates the isBreedQ method in Utility
	/*public void testBreedIs(){
		BaseTurtle bT = new BaseTurtle(breed:"rabbits")
		bT.breed("rabbits","rabbit")
		assertTrue(Utility.isRabbitQ(bT))
	}*/
	
	public void testMinOneOf(){
		def one = new SimpleReLogoAgent(id:1);
		def two = new SimpleReLogoAgent(id:2);
		def three = new SimpleReLogoAgent(id:3);
		AgentSet a = new AgentSet([one,two,three])
		assertTrue(Utility.minOneOfU(a,{it.id}) == one)
		assertTrue(Utility.minOneOfU(a,{3-it.id}) == three)
		
		def b = [1,2,3,4] as Set
		assertTrue(Utility.minOneOfU(5,b,{it}) == null)
	}
	
	public void testMaxOneOf(){
		def one = new SimpleReLogoAgent(id:1);
		def two = new SimpleReLogoAgent(id:2);
		def three = new SimpleReLogoAgent(id:3);
		def four = new SimpleReLogoAgent(id:4);
		AgentSet a = new AgentSet([one,two,three,four])
		assertTrue(Utility.maxOneOfU(a,{it.id}) == four)
		assertTrue(Utility.maxOneOfU(a,{4-it.id}) == one)
		
		def b = [1,2,3,4] as Set
		assertTrue(Utility.maxOneOfU(5,b,{it}) == null)
	}
	
	public void testMinNOf(){
		def one = new SimpleReLogoAgent(id:1);
		def two = new SimpleReLogoAgent(id:2);
		def three = new SimpleReLogoAgent(id:3);
		def four = new SimpleReLogoAgent(id:4);
		AgentSet a = new AgentSet([one,two,three,four])
		assertTrue(Utility.minNOfU(2,a,{it.id}) == [one,two])
		assertTrue(Utility.minNOfU(2,a,{4-it.id}) == [four,three])
		
		def b = [1,2,3,4] as Set
		assertTrue(Utility.minNOfU(5,b,{it}).isEmpty())
		
	}
	
	public void testMaxNOf(){
		def one = new SimpleReLogoAgent(id:1);
		def two = new SimpleReLogoAgent(id:2);
		def three = new SimpleReLogoAgent(id:3);
		def four = new SimpleReLogoAgent(id:4);
		AgentSet a = new AgentSet([one,two,three,four])
		assertTrue(Utility.maxNOfU(2,a,{id}) == [four,three])
		assertTrue(Utility.maxNOfU(2,a,{4-id}) == [one,two])
		assertTrue(Utility.maxNOfU(5,a,{4-id}) == [one,two,three,four])
		
		def b = [1,2,3,4] as Set
		assertTrue(Utility.maxNOfU(5,b,{it}).isEmpty())
	}
	
	public void testAllQ(){
		def one = new SimpleReLogoAgent(id:1);
		def two = new SimpleReLogoAgent(id:2);
		def three = new SimpleReLogoAgent(id:3);
		def four = new SimpleReLogoAgent(id:4);
		def a = [one,two,three,four,four] as Set
		assertTrue(Utility.allQU(a,{id < 3}) == false)
		assertTrue(Utility.allQU(a,{id < 5}) == true)
	}
	
	public void testShadeOfQ(){
		double color1 = orange()
		double color2 = 27
		assertTrue(shadeOfQ(color1,color2))
		assertFalse(shadeOfQ(color1,pink()))
	}
	
	public void testScaleColor(){
		double color = orange()
		double variableValue = 1.5
		double r1 = 1
		double r2 = 3
		assertTrue(scaleColor(color,variableValue,r1,r2) == 22.5)
		assertTrue(scaleColor(color,0.5,r1,r2) == 20)
		assertTrue(scaleColor(color,1.0,r1,r2) == 20)
		assertTrue(scaleColor(color,3.5,r1,r2) == 29.9999)
		assertTrue(scaleColor(color,3.0,r1,r2) == 29.9999)
		variableValue = 2.5
		r1 = 4
		r2 = 2
		assertTrue(scaleColor(color,variableValue,r1,r2) == 27.5)
		assertTrue(scaleColor(color,5,r1,r2) == 20)
		assertTrue(scaleColor(color,4,r1,r2) == 20)
		assertTrue(scaleColor(color,1,r1,r2) == 29.9999)
		assertTrue(scaleColor(color,2,r1,r2) == 29.9999)
	}
	
}
