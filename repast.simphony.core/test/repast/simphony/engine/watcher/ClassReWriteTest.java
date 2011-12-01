package repast.simphony.engine.watcher;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;


/**
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class ClassReWriteTest {

	public void run() {
		try {
			ClassPool pool = ClassPool.getDefault();
			File f = new File("../plugins/repast.simphony.core/test/bin/repast/engine/watcher/ReWriteTarget.class");
			BufferedInputStream is = new BufferedInputStream(new FileInputStream(f));
			CtClass clazz = pool.makeClass(is);
			CtMethod m = clazz.getDeclaredMethod("run");
			m.insertBefore("{ System.out.println(\"Inserted\");}");
			clazz.toClass();

			ReWriteTarget target = new ReWriteTarget();
			target.run();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ClassReWriteTest test = new ClassReWriteTest();
		test.run();
	}

}
