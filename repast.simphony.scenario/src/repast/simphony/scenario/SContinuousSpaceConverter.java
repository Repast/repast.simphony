///*CopyrightHere*/
//package repast.simphony.scenario;
//
//import repast.score.SContinuousSpace;
//import repast.score.SNamed;
//import repast.score.ScoreFactory;
//
//public class SContinuousSpaceConverter extends SNamedConverter {
//
//    public boolean canConvert(Class clazz) {
//        return SContinuousSpace.class.isAssignableFrom(clazz);
//    }
//
//    public SNamed createClass() {
//        return ScoreFactory.eINSTANCE.createSContinuousSpace();
//    }
//
//}