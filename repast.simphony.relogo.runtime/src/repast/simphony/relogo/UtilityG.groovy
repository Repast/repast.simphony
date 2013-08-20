package repast.simphony.relogo

import java.awt.Color;
import java.awt.Toolkit
import java.io.File;
import java.lang.reflect.Modifier
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import javax.swing.JFileChooserimport javax.swing.JFrame
import groovy.lang.Closure;
import groovy.swing.SwingBuilder
import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.query.*
import repast.simphony.random.*;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.SpatialException;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import javax.swing.JOptionPane
import java.text.SimpleDateFormat

import javolution.util.FastSet;
import static repast.simphony.essentials.RepastEssentials.*
import repast.simphony.space.graph.Networkimport repast.simphony.util.collections.FilteredIterator
import repast.simphony.engine.environment.RunState
import org.apache.commons.collections15.Predicate;

import org.apache.commons.collections15.functors.EqualPredicate;
import org.apache.commons.collections15.functors.InstanceofPredicateimport org.apache.commons.collections15.functors.NotPredicate;

/**
 * ReLogo Utility (Groovy) class.
 * 
 * @author jozik
 *
 */
public class UtilityG{
	
	/**
	 * Uses Groovy's flatten to flatten a collection.
	 * @param c a collection
	 * @return the flattened collection
	 */
	public static Collection gflatten(Collection c){
		return c.flatten()
	}
	
	/**
	 * Returns a list filtered by a closure.
	 * @param closure boolean closure
	 * @param a a collection
	 * @return the filtered collection consisting of items satisfying the closure
	 */
	public static <E> Collection<E> filter(Closure closure, Collection<E> a){
		return a.findAll(closure)
	}
	
	/**
	 * Executes a set of commands for each item in a collection.
	 * @param commands a set of commands
	 * @param c a collection
	 */
	public static void foreach(Closure commands, Collection c){
		for (def l : c){
			commands.call(l)
		}
	}
	
	/**
	 * Executes a set of commands for each slice of items across the lists.
	 * @param commands a set of commands
	 * @param lists a list of lists
	 */
	public static void foreach(Closure commands, List... lists){
		// TODO: Add examples.
		List aLists = lists as List//def c = [list1,list2]
		List transLists = aLists.transpose()
		aLists[0].size().times{
			commands(*transLists[it])
		}
	}
	
	
	
	/**
	 * Executes a set of commands for each item in a collection and returns the resulting collection.
	 * @param commands a set of commands
	 * @param c a collection
	 * @return collection resulting from executing the set of commands on each item in the collection
	 */
	public static Collection map(Closure commands, Collection c){
		return c.collect(commands)
	}
	
	/**
	 * Executes a set of commands for each slice of items across the lists and returns the results.
	 * @param commands a set of commands
	 * @param lists a list of lists
	 * @return list resulting from executing the set of commands on each slice of items across the lists
	 */
	public static ArrayList map(Closure commands, List... lists){
		ArrayList aLists = lists as ArrayList//def c = [list1,list2]
		ArrayList transLists = aLists.transpose()
		ArrayList result = []
		aLists[0].size().times{
			result << commands(*transLists[it])
		}
		return result
	}
	
	/**
	 * Returns a list of items.
	 * @param args any number of items
	 * @return list of items
	 */
	public static ArrayList list(Object... args){
		return (args as ArrayList)
	}
	
	
	
	
	
	/**
	 * Returns the mode(s) of a collection of numbers.
	 * @param c a collection of numbers
	 * @return a list of the mode(s) of collection c
	 */
	public static List modes(Collection c){
		if (c.size() == 0) {
			return new ArrayList()
		}
		ArrayList temp = new ArrayList(c)
		//TODO: might benefit from Java implementation using Collections
		temp = temp.unique()
		ArrayList freq = temp.collect{c.count(it)}
		int max = freq.max()
		ArrayList indices = freq.findIndexValues({it == max})
		return new ArrayList(temp.getAt(indices))
	}
	
	
	/**
	 * Returns a reduced list by repeatedly applying a closure to pairs of inputs. 
	 e.g., reduce({a, b -> a * b }, [3, 1, 3, 2]) results in (((3 * 1) * 3) * 2)
	 @param closure a command
	 @param list a list
	 @return reduced list by applying closure in a pairwise fashion to list
	 */
	public static <E> E reduce(Closure closure, List<E> list){
		if (list.size() == 1){
			return list[0]
		}
		return list.tail().inject(list[0],closure)
	}
	
	
	/**
	 * Returns a list sorted by a closure.
	 * The closure can be a single parameter closure, in which case the value returned
	 * is used to order the entities in ascending order.
	 * The closure can also have two parameters, in which case returning
	 * a negative integer, zero, or a positive integer when 
	 * the first parameter is less than, equal to, 
	 * or greater than the second respectively.
	 * 
	 * See also:
	 * http://groovy.codehaus.org/groovy-jdk/java/util/Collection.html#sort(groovy.lang.Closure)
	 * 
	 * e.g.,
	 * ["a","abc","ab"].sort { a -> a.length() }
	 * or
	 * ["a","abc","ab"].sort { a,b -> a.length() <=> b.length() }
	 * 
	 * both yield ["a","ab","abc"]
	 * 
	 * @param closure closure taking one or two arguments
	 * @param c a collection
	 * @return list sorted by the closure
	 */
	public static <E> List<E> sortBy(Closure closure, Collection<E> c){
		List<E> temp = new ArrayList<>(c)
		temp.sort(closure)
		return temp
	}
	
	
	
	/**
	 * Interprets a string and returns the value.
	 * @param string a string
	 * @return returns the result of reading the string
	 */
	public static def readFromString(String string){
		return Eval.me(string)
	}
	
	
	/**
	 * Returns the value of the first or second set of commands depending on the conditional.
	 @param conditional a boolean closure
	 @param closure1 a closure returning a value
	 @param closure2 a closure returning a value
	 @return the value returned by closure1 or closure2 depending on the conditional
	 */
	public static def ifelseValue(Closure conditional, Closure closure1, Closure closure2){
		if (conditional()){
			return closure1()
		}
		else {
			return closure2()
		}
	}
	
	
	
	private static Map getObjectPropertiesAndMethods(Object o){
		Map oProps =  o.getProperties()
		def methodsList = o.getMetaClass().getMethods()
		Map oMethods = [:]
		methodsList.each {
			oMethods[it.getName()] = this.&"${it.getName()}"
		}
		return oProps + oMethods
	}
	
	private static Map getClassFieldsAndMethods(Class c){
		def cFields =  c.getProperties()["fields"]
		Map cFieldsMap = [:]
		cFields.each {
			if (Modifier.isStatic(it.getModifiers())) {
				cFieldsMap[it.getName()] = it.get(null)
			}
		}
		def cMethods = c.getProperties()["methods"]
		Map cMethodsMap = [:]
		cMethods.each {
			cMethodsMap[it.getName()] = c.&"${it.getName()}"
		}
		return cFieldsMap + cMethodsMap
	}
	
	/**
	 * Interprets a string as commands then runs the commands using an object's bindings.
	 * 
	 * @param string
	 *            a string
	 * @param o
	 *            an object
	 */
	public static void runU(String string, Object o){
		// order here is meaningful, from least important to more important for overriding purposes
		def classes = [repast.simphony.relogo.Utility,repast.simphony.relogo.UtilityG]
		runString(string,o,classes)
	}
	
	/**
	 * Interprets a string as a command using an object's bindings then returns the result.
	 * 
	 * @param string
	 *            a string
	 * @param o
	 *            an object
	 * @return result of interpreting string
	 */
	public static Object runresultU(String string, Object o){
		// order here is meaningful, from least important to more important for overriding purposes
		def classes = [repast.simphony.relogo.Utility,repast.simphony.relogo.UtilityG]
		return runString(string,o,classes)
	}
	
	/**
	 * Returns the result of interpreting the string as a script with Object o's class bindings,
	 * also adding the classes static methods and fields to the script binding.
	 */
	private static Object runString(String string, Object o, List classes){
		Map objectPropertiesAndMethods = getObjectPropertiesAndMethods(o)
		Map classesFieldsAndMethods = [:]
		classes.each{ classesFieldsAndMethods += getClassFieldsAndMethods(it) }
		def binding = new Binding(classesFieldsAndMethods + objectPropertiesAndMethods)
		def shell = new GroovyShell(binding)
		return shell.evaluate(string)
	}
	
	
	/**
	 * Sets the current directory to string. 
	 * @param string a string
	 */
	public static void setCurrentDirectory(String string){
		File file = new File(string);
		ReLogoModel.getInstance().setCurrentDirectory(file);
	}
	
	
	/**
	 * Opens a file.
	 * @param string a string path
	 */
	public static void fileOpen(String string){
		
		// define file according to absolute, default, or current directory
		File file = resolveFileFromString(string)
		
		// check for existence of file in Model's file list
		def fileInfoList = ReLogoModel.getInstance().getFileInfoList()
		FileInfo foundFileInfo = fileInfoList.find {it.getFile() == file}
		if (foundFileInfo) {
			ReLogoModel.getInstance().setCurrentFileInfo(foundFileInfo)
		}
		else {
			FileInfo newFileInfo = new FileInfo(file)
			fileInfoList.add(newFileInfo)
			ReLogoModel.getInstance().setCurrentFileInfo(newFileInfo)
		}
	}
	
	private static File resolveFileFromString(String string){
		File file = new File(string)
		if (file.isAbsolute() || ReLogoModel.getInstance().getCurrentDirectory() == null){
			file = file.getCanonicalFile()
		}
		else {
			file = new File(ReLogoModel.getInstance().getCurrentDirectory(),string)
		}
		return file
	}
	
	public static void checkAndSetWritable(){
		FileInfo fileInfo = ReLogoModel.getInstance().getCurrentFileInfo()
		if (fileInfo.isFileReadable() && fileInfo.getBufferedReader() != null){
			throw(new RuntimeException("File is not writable."))
		}
		if ( fileInfo.isFileReadable()) {
			fileInfo.setFileReadable(false)
		}
		if (! fileInfo.getFile().exists()){
			fileInfo.getFile().getParentFile().mkdirs()
		}
	}
	
	/**
	 * Prints a value to the current file with a newline.
	 * @param value an object
	 */
	public static void filePrint(Object value){
		checkAndSetWritable()
		File file = ReLogoModel.getInstance().getCurrentFileInfo().getFile()
		if (file != null){
			file.append(value)
			file.append('\n')
		}
	}
	
	/**
	 * Writes a value to the current file so that fileRead can subsequently read it.
	 * @param value any object
	 */
	public static void fileWrite(Object value){
		checkAndSetWritable()
		File file = ReLogoModel.getInstance().getCurrentFileInfo().getFile()
		if (file != null){
			String outValue = value.toString()
			if (!(value instanceof String)){
				outValue = outValue.replace(' ','')
			}
			else{
				outValue = '"' + outValue + '"'
			}
			file.append(' ')
			file.append(outValue)
		}
	}
	
	/**
	 * Writes a value (enclosed in quotes) to the console without a newline.
	 @param value an object
	 */
	public static void write(Object value){
		String outValue = value.toString()
		if (!(value instanceof String)){
			outValue = outValue.replace(' ','')
		}
		else{
			outValue = '"' + outValue + '"'
		}
		System.out.print(' ' + outValue)
	}
	
	/**
	 * Prints value to current file without a newline.
	 * @param value any object
	 */
	public static void fileType(Object value){
		checkAndSetWritable()
		File file = ReLogoModel.getInstance().getCurrentFileInfo().getFile()
		if (file != null){
			file.append(value)
		}
	}
	
	/**
	 * Queries if the file exists.
	 * @param string a string path
	 * @return true or false based on whether the file exists
	 */
	public static boolean fileExistsQ(String string){
		return resolveFileFromString(string).exists()
	}
	
	/**
	 * Deletes a file.
	 * 
	 * @param string a string
	 */
	public static void fileDelete(String string){
		File file = resolveFileFromString(string)
		def fileInfoList = ReLogoModel.getInstance().getFileInfoList()
		boolean fileInOpenedFileList = fileInfoList.any {it.getFile() == file}
		if (!fileInOpenedFileList) {
			file.delete()
		}
		else {
			println("Can't delete file ${file.name} since it is still open.")
		}
	}
	
	/**
	 * Closes a file.
	 */
	public static void fileClose(){
		def removeFile = ReLogoModel.getInstance().getCurrentFileInfo()
		ReLogoModel.getInstance().getFileInfoList().remove(removeFile)
		ReLogoModel.getInstance().setCurrentFileInfo(null)
	}
	
	/**
	 * Closes all files.
	 */
	public static void fileCloseAll(){
		ReLogoModel.getInstance().setCurrentFileInfo(null)
		ReLogoModel.getInstance().setFileInfoList(new ArrayList<FileInfo>())
	}
	
	/**
	 * Writes the file to disk.
	 */
	public static void fileFlush(){
		//do nothing
	}
	
	// ***** file readers and associated utility method *****
	/**
	 * If file is readable, returns the relevant BufferedReader object
	 */
	private static BufferedReader checkAndSetReadable(){
		FileInfo fileInfo = ReLogoModel.getInstance().getCurrentFileInfo()
		if (!fileInfo.isFileReadable()){
			throw(new RuntimeException("File is not readable."))
		}
		if ( fileInfo.getBufferedReader() == null) {
			fileInfo.setBufferedReader(fileInfo.getFile().newReader())
		}
		return fileInfo.getBufferedReader()
	}
	
	
	/**
	 * Queries if at end of the file.
	 * @return true or false based on whether at end of the file 
	 */
	public static boolean fileAtEndQ(){
		def br = checkAndSetReadable()
		boolean atEnd
		br.mark(1)
		if (br.read() == -1){
			atEnd = true
		}
		else {
			atEnd = false
		}
		br.reset()
		return atEnd
	}
	
	/**
	 * Returns the next value from the current file.
	 * @return next value from current file
	 */
	public static def fileRead(){
		def br = checkAndSetReadable()
		int temp = 0
		String str = ''
		while ( Character.isWhitespace(temp = br.read())) {
		}
		if (temp == '"'){
			while ( ((temp = br.read()) != '"')){
				str += (char) temp
			}
			str = '"' + str + '"'
		}
		else {
			str += (char) temp
			while ( !Character.isWhitespace(temp = br.read()) && temp != -1){
				str += (char) temp
			}
		}
		return Eval.me(str)
	}
	
	/**
	 * Returns a number of characters from the current file.
	 * @param number an integer
	 * @return number of characters from the current file
	 */
	public static String fileReadCharacters(int number){
		def br = checkAndSetReadable()
		int temp = 0
		int counter = 0
		String result = ''
		while (counter < number && ((temp = br.read()) != -1)){
			result += (char) temp
			counter++
		}
		return result
	}
	
	/**
	 * Returns the next line from the current file.
	 * 
	 * @return next line from the current file
	 */
	public static String fileReadLine(){
		BufferedReader br = checkAndSetReadable()
		return br.readLine()
	}
	// ***** end of file readers *****
	
	/**
	 * Allows the user to choose a user directory and returns the directory path.
	 * @return the chosen directory's absolute path
	 */
	public static String userDirectory(){
		
		SwingBuilder swing = new SwingBuilder()
		JFrame frame = swing.frame()
		JFileChooser fc = swing.fileChooser(
				id: 'fileChooser',
				fileSelectionMode: JFileChooser.DIRECTORIES_ONLY, 
				dialogTitle: 'Choose a directory'
				)
		
		int rc = swing.fileChooser.showDialog(frame, 'Choose')
		if ( 0 == rc ) {
			return (swing.fileChooser.getSelectedFile().toString())
		}
		else return null
	}
	
	/**
	 * Allows the user to choose a user file and returns the file path.
	 * @return the chosen file's absolute path
	 */
	public static String userFile(){
		SwingBuilder swing = new SwingBuilder()
		JFrame frame = swing.frame()
		JFileChooser fc = swing.fileChooser(
				id: 'fileChooser',
				fileSelectionMode: JFileChooser.FILES_ONLY, 
				dialogTitle: 'Choose a file'
				)
		
		int rc = fc.showDialog(frame, 'Choose')
		if ( 0 == rc ) {
			return (fc.getSelectedFile().toString())
		}
		else return null
	}
	
	/**
	 * Returns a new file chosen by the user.
	 * @return new file's absolute path
	 */
	public static String userNewFile(){
		SwingBuilder swing = new SwingBuilder()
		JFrame frame = swing.frame()
		JFileChooser fc = swing.fileChooser(
				id: 'fileChooser',
				fileSelectionMode: JFileChooser.FILES_ONLY,
				dialogType: JFileChooser.SAVE_DIALOG,
				dialogTitle: 'Choose file location and name'
				)
		
		int rc = swing.fileChooser.showDialog(frame, 'Choose')
		if ( 0 == rc ) {
			File newFile = swing.fileChooser.getSelectedFile()
			if (newFile.exists()){
				JOptionPane op = swing.optionPane()
				int response = op.showConfirmDialog (null,
						"This file already exists, choose anyway?","Choose",
						JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE);
				if (response == JOptionPane.CANCEL_OPTION) return null;
			}
			return (newFile.toString())
		}
		else return null
	}
	
	/**
	 * Returns a value that the user enters.
	 * @param value the title of the dialog screen
	 * @returns string input by user
	 */
	public static String userInput(Object value){		
		return (String) JOptionPane.showInputDialog(null, value, "User Input", JOptionPane.PLAIN_MESSAGE)
	}
	
	/**
	 * Displays a message to the user.
	 * @param value message
	 */
	public static void userMessage(Object value){
		JOptionPane.showMessageDialog(null, value, "User Message", JOptionPane.PLAIN_MESSAGE)
	}
	
	/**
	 * Returns an object that the user chose from a list.
	 * @param value message
	 * @param list list
	 * @return object that the user chose from list
	 */
	public static Object userOneOf(Object value, Object[] list){
		return JOptionPane.showInputDialog( null, value , "User One Of", JOptionPane.PLAIN_MESSAGE, null, list, null);
	}
	
	/**
	 * Queries for user's answer to a question.
	 * @param value question
	 * @return  true or false based on the user's answer to value
	 */
	public static boolean userYesOrNoQ(Object value){
		if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null,value,
		"User Yes Or No",
		JOptionPane.YES_NO_OPTION)){
			return true
		}
		else return false
	}
	
	/**
	 * Prints value with agent identifier to current file with a newline.
	 * @param id a string
	 * @param value any object
	 * @exclude 
	 */
	public static void fileShowU(String id, Object value){
		checkAndSetWritable()
		String prefix = '(' + id +'):'
		File file = ReLogoModel.getInstance().getCurrentFileInfo().getFile()
		if (file != null){
			String outValue = value.toString()
			if (!(value instanceof String)){
				outValue = outValue.replace(' ','')
			}
			else{
				outValue = '"' + outValue + '"'
			}
			file.append(prefix)
			file.append(' ')
			file.append(outValue)
			file.append('\n')
		}
	}
	
	/**
	 * Prints value in the Console, preceded by the calling agent, and followed by a carriage return. 
	 * (The calling agent is included to help you keep track of what agents are producing which lines of output.) 
	 * Also, all strings have their quotes included similar to write.
	 */
	public static void showU(String id, Object value){
		String prefix = '(' + id +'):'
		
		String outValue = value.toString()
		if (!(value instanceof String)){
			outValue = outValue.replace(' ','')
		}
		else{
			outValue = '"' + outValue + '"'
		}
		println (prefix + ' ' + outValue)
	}
	
	/**
	 * Sounds an audible tone.
	 */
	public static void beep(){
		Toolkit.getDefaultToolkit().beep()
	}
	
	
	
	/**
	 * Prints an object.
	 * @param value an object 
	 */
	public static void print(Object value){
		println value
	}
	
	
	/**
	 * Prints a value to the console without a newline.
	 * @param value any object
	 */
	public static void type(Object value){
		System.out.print(value)
	}
	
	
	
	/**
	 * Capitalizes a string. 
	 * @param s a string
	 * @return the capitalized string s
	 */
	public static String capitalize(String s){
		if (s.size() > 1){
			return(s[0].toUpperCase() + s[1..-1])
		}
		else {
			return s.toUpperCase()
		}
	}
	
	
	public static void dressClosure(){
		Closure.metaClass.of = { value -> 
			def rv = delegate
			rv.setResolveStrategy(Closure.DELEGATE_FIRST)
			// check if it's not a list, then do below
			if (!(value instanceof Collection)){
				rv.setDelegate(value)
				rv.call(value)
			}
			else {
				def returnList = []
				value.each{ item ->
					rv.setDelegate(item)
					returnList << rv.call(item)
				}
				returnList
			}
		}
	}
}
