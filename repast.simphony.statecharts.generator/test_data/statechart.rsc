<?xml version="1.0" encoding="UTF-8"?>
<xmi:XMI xmi:version="2.0" xmlns:xmi="http://www.omg.org/XMI" xmlns="http://repast.sf.net/statecharts" xmlns:notation="http://www.eclipse.org/gmf/runtime/1.0.2/notation">
  <StateMachine xmi:id="_DJwmYOpmEeK_pvf5APxfpw" agentType="jzombies.Human" package="jzombies.chart" className="DiseaseStatechart" nextID="45" id="Disease Statechart" uuid="_DJvYQOpmEeK_pvf5APxfpw">
    <states xmi:type="PseudoState" xmi:id="_EGgigOpmEeK_pvf5APxfpw" id="Entry State Pointer" type="entry"/>
    <states xmi:type="State" xmi:id="_Eomi8OpmEeK_pvf5APxfpw" id="Uninfected" onEnter="" uuid="_EonKAOpmEeK_pvf5APxfpw"/>
    <states xmi:type="State" xmi:id="_Fu4OgOpmEeK_pvf5APxfpw" id="Infected" uuid="_Fu4OgepmEeK_pvf5APxfpw"/>
    <states xmi:type="FinalState" xmi:id="_Gsd4IOpmEeK_pvf5APxfpw" id="Final State 2" onEnter="agent.die()&#xA;" uuid="_GsefMOpmEeK_pvf5APxfpw"/>
    <states xmi:type="PseudoState" xmi:id="_1CHg0OpmEeK_pvf5APxfpw" id="Choice 9" uuid="_1CIH4OpmEeK_pvf5APxfpw" type="choice"/>
    <transitions xmi:type="Transition" xmi:id="_HT32YOpmEeK_pvf5APxfpw" from="_EGgigOpmEeK_pvf5APxfpw" to="_Eomi8OpmEeK_pvf5APxfpw" id="Transition 3" uuid="_HT32YepmEeK_pvf5APxfpw"/>
    <transitions xmi:type="Transition" xmi:id="_HpT64OpmEeK_pvf5APxfpw" from="_Eomi8OpmEeK_pvf5APxfpw" to="_Fu4OgOpmEeK_pvf5APxfpw" triggerType="message" messageCheckerType="equals" messageCheckerClass="String" messageCheckerCode="&quot;Brains&quot;" id="Transition 4" uuid="_HpUh8OpmEeK_pvf5APxfpw"/>
    <transitions xmi:type="Transition" xmi:id="_IMTTMOpmEeK_pvf5APxfpw" from="_Fu4OgOpmEeK_pvf5APxfpw" to="_1CHg0OpmEeK_pvf5APxfpw" triggerType="timed" messageCheckerClass="Object" id="Transition 5" triggerTimedCode="5" uuid="_IMTTMepmEeK_pvf5APxfpw"/>
    <transitions xmi:type="Transition" xmi:id="_KhawcOpmEeK_pvf5APxfpw" from="_Eomi8OpmEeK_pvf5APxfpw" to="_Eomi8OpmEeK_pvf5APxfpw" onTransition="agent.move()" messageCheckerClass="Object" id="Transition 6" uuid="_KhawcepmEeK_pvf5APxfpw" selfTransition="true"/>
    <transitions xmi:type="Transition" xmi:id="_rKvzEOpmEeK_pvf5APxfpw" from="_Fu4OgOpmEeK_pvf5APxfpw" to="_Fu4OgOpmEeK_pvf5APxfpw" onTransition="agent.run()" messageCheckerClass="Object" id="Transition 7" uuid="_rKwaIOpmEeK_pvf5APxfpw" selfTransition="true"/>
    <transitions xmi:type="Transition" xmi:id="_wBVukOpmEeK_pvf5APxfpw" from="_1CHg0OpmEeK_pvf5APxfpw" to="_Eomi8OpmEeK_pvf5APxfpw" outOfBranch="true" triggerType="condition" triggerConditionCode="import repast.simphony.random.RandomHelper&#xA;&#xA;RandomHelper.nextDouble() &lt; params.getDouble(&quot;immunity&quot;)" messageCheckerClass="Object" id="Transition 8" uuid="_wBWVoOpmEeK_pvf5APxfpw"/>
    <transitions xmi:type="Transition" xmi:id="_2mIRQOpmEeK_pvf5APxfpw" from="_1CHg0OpmEeK_pvf5APxfpw" to="_Gsd4IOpmEeK_pvf5APxfpw" outOfBranch="true" defaultTransition="true" triggerType="condition" messageCheckerClass="Object" id="Transition 10" uuid="_2mI4UOpmEeK_pvf5APxfpw"/>
  </StateMachine>
  <notation:Diagram xmi:id="_DKkesOpmEeK_pvf5APxfpw" type="Statechart" element="_DJwmYOpmEeK_pvf5APxfpw" name="statechart.rsc" measurementUnit="Pixel">
    <children xmi:type="notation:Shape" xmi:id="_EN8AMOpmEeK_pvf5APxfpw" type="2007" element="_EGgigOpmEeK_pvf5APxfpw" fontName="Lucida Grande">
      <layoutConstraint xmi:type="notation:Bounds" xmi:id="_EN8AMepmEeK_pvf5APxfpw" x="84" y="130"/>
    </children>
    <children xmi:type="notation:Shape" xmi:id="_EospkOpmEeK_pvf5APxfpw" type="2003" element="_Eomi8OpmEeK_pvf5APxfpw" fontName="Lucida Grande">
      <children xmi:type="notation:DecorationNode" xmi:id="_EouewOpmEeK_pvf5APxfpw" type="5001"/>
      <layoutConstraint xmi:type="notation:Bounds" xmi:id="_EospkepmEeK_pvf5APxfpw" x="168" y="120"/>
    </children>
    <children xmi:type="notation:Shape" xmi:id="_Fu5coOpmEeK_pvf5APxfpw" type="2003" element="_Fu4OgOpmEeK_pvf5APxfpw" fontName="Lucida Grande">
      <children xmi:type="notation:DecorationNode" xmi:id="_Fu5coupmEeK_pvf5APxfpw" type="5001"/>
      <layoutConstraint xmi:type="notation:Bounds" xmi:id="_Fu5coepmEeK_pvf5APxfpw" x="312" y="120"/>
    </children>
    <children xmi:type="notation:Shape" xmi:id="_GsftUOpmEeK_pvf5APxfpw" type="2008" element="_Gsd4IOpmEeK_pvf5APxfpw" fontName="Lucida Grande">
      <layoutConstraint xmi:type="notation:Bounds" xmi:id="_GsftUepmEeK_pvf5APxfpw" x="468" y="132"/>
    </children>
    <children xmi:type="notation:Shape" xmi:id="_1CJWAOpmEeK_pvf5APxfpw" type="2006" element="_1CHg0OpmEeK_pvf5APxfpw" fontName="Lucida Grande">
      <layoutConstraint xmi:type="notation:Bounds" xmi:id="_1CJWAepmEeK_pvf5APxfpw" x="384" y="128"/>
    </children>
    <styles xmi:type="notation:DiagramStyle" xmi:id="_DKkesepmEeK_pvf5APxfpw"/>
    <edges xmi:type="notation:Edge" xmi:id="_HT-kEOpmEeK_pvf5APxfpw" type="4001" element="_HT32YOpmEeK_pvf5APxfpw" source="_EN8AMOpmEeK_pvf5APxfpw" target="_EospkOpmEeK_pvf5APxfpw">
      <styles xmi:type="notation:RoutingStyle" xmi:id="_HT-kEepmEeK_pvf5APxfpw"/>
      <styles xmi:type="notation:FontStyle" xmi:id="_HT-kEupmEeK_pvf5APxfpw" fontName="Lucida Grande"/>
      <bendpoints xmi:type="notation:RelativeBendpoints" xmi:id="_HT-kE-pmEeK_pvf5APxfpw" points="[0, 1, -105, -62]$[76, 45, -29, -18]"/>
      <sourceAnchor xmi:type="notation:IdentityAnchor" xmi:id="_HUSGEOpmEeK_pvf5APxfpw" id="CENTER"/>
    </edges>
    <edges xmi:type="notation:Edge" xmi:id="_HpVwEOpmEeK_pvf5APxfpw" type="4001" element="_HpT64OpmEeK_pvf5APxfpw" source="_EospkOpmEeK_pvf5APxfpw" target="_Fu5coOpmEeK_pvf5APxfpw">
      <styles xmi:type="notation:RoutingStyle" xmi:id="_HpVwEepmEeK_pvf5APxfpw"/>
      <styles xmi:type="notation:FontStyle" xmi:id="_HpVwEupmEeK_pvf5APxfpw" fontName="Lucida Grande"/>
      <bendpoints xmi:type="notation:RelativeBendpoints" xmi:id="_HpVwE-pmEeK_pvf5APxfpw" points="[0, 2, -132, -1]$[124, -17, -8, -20]"/>
      <sourceAnchor xmi:type="notation:IdentityAnchor" xmi:id="_HpYMUOpmEeK_pvf5APxfpw" id="(1.0,0.55)"/>
    </edges>
    <edges xmi:type="notation:Edge" xmi:id="_IMUhUOpmEeK_pvf5APxfpw" type="4001" element="_IMTTMOpmEeK_pvf5APxfpw" source="_Fu5coOpmEeK_pvf5APxfpw" target="_1CJWAOpmEeK_pvf5APxfpw">
      <styles xmi:type="notation:RoutingStyle" xmi:id="_IMUhUepmEeK_pvf5APxfpw"/>
      <styles xmi:type="notation:FontStyle" xmi:id="_IMUhUupmEeK_pvf5APxfpw" fontName="Lucida Grande"/>
      <bendpoints xmi:type="notation:RelativeBendpoints" xmi:id="_IMUhU-pmEeK_pvf5APxfpw" points="[15, -3, -99, 18]$[107, -21, -7, 0]"/>
      <sourceAnchor xmi:type="notation:IdentityAnchor" xmi:id="_IMXkoOpmEeK_pvf5APxfpw" id="(0.9583333333333334,0.45)"/>
      <targetAnchor xmi:type="notation:IdentityAnchor" xmi:id="_IMXkoepmEeK_pvf5APxfpw" id="WEST"/>
    </edges>
    <edges xmi:type="notation:Edge" xmi:id="_Khb-kOpmEeK_pvf5APxfpw" type="4001" element="_KhawcOpmEeK_pvf5APxfpw" source="_EospkOpmEeK_pvf5APxfpw" target="_EospkOpmEeK_pvf5APxfpw">
      <styles xmi:type="notation:RoutingStyle" xmi:id="_Khb-kepmEeK_pvf5APxfpw"/>
      <styles xmi:type="notation:FontStyle" xmi:id="_Khb-kupmEeK_pvf5APxfpw" fontName="Lucida Grande"/>
      <bendpoints xmi:type="notation:RelativeBendpoints" xmi:id="_Khb-k-pmEeK_pvf5APxfpw" points="[16, -30, 2, -4]$[16, -27, 2, -1]"/>
      <sourceAnchor xmi:type="notation:IdentityAnchor" xmi:id="_KhfB4OpmEeK_pvf5APxfpw" id="(0.864406779661017,0.45)"/>
      <targetAnchor xmi:type="notation:IdentityAnchor" xmi:id="_KhfB4epmEeK_pvf5APxfpw" id="(0.6610169491525424,0.95)"/>
    </edges>
    <edges xmi:type="notation:Edge" xmi:id="_rKxoQOpmEeK_pvf5APxfpw" type="4001" element="_rKvzEOpmEeK_pvf5APxfpw" source="_Fu5coOpmEeK_pvf5APxfpw" target="_Fu5coOpmEeK_pvf5APxfpw">
      <styles xmi:type="notation:RoutingStyle" xmi:id="_rKxoQepmEeK_pvf5APxfpw"/>
      <styles xmi:type="notation:FontStyle" xmi:id="_rKxoQupmEeK_pvf5APxfpw" fontName="Lucida Grande"/>
      <bendpoints xmi:type="notation:RelativeBendpoints" xmi:id="_rKxoQ-pmEeK_pvf5APxfpw" points="[-42, 27, -8, 7]$[-32, 27, 2, 7]"/>
      <sourceAnchor xmi:type="notation:IdentityAnchor" xmi:id="_rK0rkOpmEeK_pvf5APxfpw" id="(0.9375,0.525)"/>
      <targetAnchor xmi:type="notation:IdentityAnchor" xmi:id="_rK0rkepmEeK_pvf5APxfpw" id="(0.5208333333333334,0.9)"/>
    </edges>
    <edges xmi:type="notation:Edge" xmi:id="_wBXjwOpmEeK_pvf5APxfpw" type="4001" element="_wBVukOpmEeK_pvf5APxfpw" source="_1CJWAOpmEeK_pvf5APxfpw" target="_EospkOpmEeK_pvf5APxfpw">
      <styles xmi:type="notation:RoutingStyle" xmi:id="_wBXjwepmEeK_pvf5APxfpw"/>
      <styles xmi:type="notation:FontStyle" xmi:id="_wBXjwupmEeK_pvf5APxfpw" fontName="Lucida Grande"/>
      <bendpoints xmi:type="notation:RelativeBendpoints" xmi:id="_wBXjw-pmEeK_pvf5APxfpw" points="[1, 11, 176, -6]$[0, 37, 175, 20]$[-175, 37, 0, 20]$[-175, 22, 0, 5]"/>
      <sourceAnchor xmi:type="notation:IdentityAnchor" xmi:id="_wBaAAOpmEeK_pvf5APxfpw" id="SOUTH"/>
      <targetAnchor xmi:type="notation:IdentityAnchor" xmi:id="_wBaAAepmEeK_pvf5APxfpw" id="(0.8305084745762712,0.875)"/>
    </edges>
    <edges xmi:type="notation:Edge" xmi:id="_2mKGcOpmEeK_pvf5APxfpw" type="4001" element="_2mIRQOpmEeK_pvf5APxfpw" source="_1CJWAOpmEeK_pvf5APxfpw" target="_GsftUOpmEeK_pvf5APxfpw">
      <styles xmi:type="notation:RoutingStyle" xmi:id="_2mKGcepmEeK_pvf5APxfpw"/>
      <styles xmi:type="notation:FontStyle" xmi:id="_2mKGcupmEeK_pvf5APxfpw" fontName="Lucida Grande"/>
      <bendpoints xmi:type="notation:RelativeBendpoints" xmi:id="_2mKGc-pmEeK_pvf5APxfpw" points="[18, 1, -100, 1]$[105, 1, -13, 1]"/>
      <sourceAnchor xmi:type="notation:IdentityAnchor" xmi:id="_2mMisOpmEeK_pvf5APxfpw" id="EAST"/>
      <targetAnchor xmi:type="notation:IdentityAnchor" xmi:id="_2mMisepmEeK_pvf5APxfpw" id="WEST"/>
    </edges>
  </notation:Diagram>
</xmi:XMI>
