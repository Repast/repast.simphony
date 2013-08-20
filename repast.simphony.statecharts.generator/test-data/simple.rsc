<?xml version="1.0" encoding="UTF-8"?>
<xmi:XMI xmi:version="2.0" xmlns:xmi="http://www.omg.org/XMI" xmlns="http://repast.sf.net/statecharts" xmlns:notation="http://www.eclipse.org/gmf/runtime/1.0.2/notation">
  <StateMachine xmi:id="_2Ni38EP7EeKahreWGKXckw" nextID="12">
    <states xmi:type="State" xmi:id="_3Qrv8EP7EeKahreWGKXckw" id="One" uuid="_3QxPgEP7EeKahreWGKXckw"/>
    <states xmi:type="PseudoState" xmi:id="_5Ox0UEP7EeKahreWGKXckw" id="Entry State Pointer" type="entry"/>
    <states xmi:type="CompositeState" xmi:id="_9lShkEP7EeKahreWGKXckw" id="Three" uuid="_9lTIoEP7EeKahreWGKXckw">
      <children xmi:type="State" xmi:id="_-UUx0EP7EeKahreWGKXckw" id="Two" uuid="_-UUx0UP7EeKahreWGKXckw"/>
      <children xmi:type="PseudoState" xmi:id="_igRmoEP8EeKahreWGKXckw" id="Initial State Pointer 5" uuid="_igSNsEP8EeKahreWGKXckw"/>
      <children xmi:type="History" xmi:id="_rb7TAEP8EeKahreWGKXckw" id="Shallow History 7" uuid="_rb7TAUP8EeKahreWGKXckw" shallow="true"/>
      <children xmi:type="History" xmi:id="_r5Z5MEP8EeKahreWGKXckw" id="Deep History 8" uuid="_r5Z5MUP8EeKahreWGKXckw"/>
    </states>
    <states xmi:type="FinalState" xmi:id="_vPsfkEP8EeKahreWGKXckw" id="Final State 9" uuid="_vPsfkUP8EeKahreWGKXckw"/>
    <states xmi:type="PseudoState" xmi:id="_xCddsEP8EeKahreWGKXckw" id="Choice 10" uuid="_xCeEwEP8EeKahreWGKXckw" type="choice"/>
    <transitions xmi:type="Transition" xmi:id="_66ZBkEP7EeKahreWGKXckw" from="_5Ox0UEP7EeKahreWGKXckw" to="_3Qrv8EP7EeKahreWGKXckw" ID="Transition 1" uuid="_66ZBkUP7EeKahreWGKXckw"/>
    <transitions xmi:type="Transition" xmi:id="_AgDCgEP8EeKahreWGKXckw" from="_3Qrv8EP7EeKahreWGKXckw" to="_-UUx0EP7EeKahreWGKXckw" ID="Transition 4" uuid="_AgDpkEP8EeKahreWGKXckw"/>
    <transitions xmi:type="Transition" xmi:id="_luTo4EP8EeKahreWGKXckw" from="_igRmoEP8EeKahreWGKXckw" to="_-UUx0EP7EeKahreWGKXckw" ID="Transition 6" uuid="_luTo4UP8EeKahreWGKXckw"/>
    <transitions xmi:type="Transition" xmi:id="_x4H6YEP8EeKahreWGKXckw" from="_3Qrv8EP7EeKahreWGKXckw" to="_xCddsEP8EeKahreWGKXckw" ID="Transition 11" uuid="_x4H6YUP8EeKahreWGKXckw"/>
  </StateMachine>
  <notation:Diagram xmi:id="_2N6EUEP7EeKahreWGKXckw" type="Statechart" element="_2Ni38EP7EeKahreWGKXckw" name="simple.rsc" measurementUnit="Pixel">
    <children xmi:type="notation:Shape" xmi:id="_3Q2IAEP7EeKahreWGKXckw" type="2003" element="_3Qrv8EP7EeKahreWGKXckw" fontName="Lucida Grande">
      <children xmi:type="notation:DecorationNode" xmi:id="_3Q2vEEP7EeKahreWGKXckw" type="5001"/>
      <layoutConstraint xmi:type="notation:Bounds" xmi:id="_3Q2IAUP7EeKahreWGKXckw" x="171" y="180" width="64" height="64"/>
    </children>
    <children xmi:type="notation:Shape" xmi:id="_5OzpgEP7EeKahreWGKXckw" type="2007" element="_5Ox0UEP7EeKahreWGKXckw" fontName="Lucida Grande">
      <layoutConstraint xmi:type="notation:Bounds" xmi:id="_5OzpgUP7EeKahreWGKXckw" x="162" y="81" width="37" height="37"/>
    </children>
    <children xmi:type="notation:Shape" xmi:id="_9lUWwEP7EeKahreWGKXckw" type="2004" element="_9lShkEP7EeKahreWGKXckw" fontName="Lucida Grande">
      <children xmi:type="notation:DecorationNode" xmi:id="_9lUWwkP7EeKahreWGKXckw" type="5004"/>
      <children xmi:type="notation:DecorationNode" xmi:id="_9lUWw0P7EeKahreWGKXckw" type="7001">
        <children xmi:type="notation:Shape" xmi:id="_-UV_8EP7EeKahreWGKXckw" type="3001" element="_-UUx0EP7EeKahreWGKXckw" fontName="Lucida Grande">
          <children xmi:type="notation:DecorationNode" xmi:id="_-UWnAEP7EeKahreWGKXckw" type="5002"/>
          <layoutConstraint xmi:type="notation:Bounds" xmi:id="_-UV_8UP7EeKahreWGKXckw" x="96" y="79" width="100" height="64"/>
        </children>
        <children xmi:type="notation:Shape" xmi:id="_igTb0EP8EeKahreWGKXckw" type="3003" element="_igRmoEP8EeKahreWGKXckw" fontName="Lucida Grande">
          <layoutConstraint xmi:type="notation:Bounds" xmi:id="_igTb0UP8EeKahreWGKXckw" x="114" y="25"/>
        </children>
        <children xmi:type="notation:Shape" xmi:id="_rb-9YEP8EeKahreWGKXckw" type="3008" element="_rb7TAEP8EeKahreWGKXckw" fontName="Lucida Grande">
          <layoutConstraint xmi:type="notation:Bounds" xmi:id="_rb-9YUP8EeKahreWGKXckw" x="241" y="45"/>
        </children>
        <children xmi:type="notation:Shape" xmi:id="_r5buYEP8EeKahreWGKXckw" type="3009" element="_r5Z5MEP8EeKahreWGKXckw" fontName="Lucida Grande">
          <layoutConstraint xmi:type="notation:Bounds" xmi:id="_r5buYUP8EeKahreWGKXckw" x="250" y="84"/>
        </children>
      </children>
      <layoutConstraint xmi:type="notation:Bounds" xmi:id="_9lUWwUP7EeKahreWGKXckw" x="333" y="171" width="334" height="280"/>
    </children>
    <children xmi:type="notation:Shape" xmi:id="_vPttsEP8EeKahreWGKXckw" type="2008" element="_vPsfkEP8EeKahreWGKXckw" fontName="Lucida Grande">
      <layoutConstraint xmi:type="notation:Bounds" xmi:id="_vPttsUP8EeKahreWGKXckw" x="261" y="351"/>
    </children>
    <children xmi:type="notation:Shape" xmi:id="_xCfS4EP8EeKahreWGKXckw" type="2006" element="_xCddsEP8EeKahreWGKXckw" fontName="Lucida Grande">
      <layoutConstraint xmi:type="notation:Bounds" xmi:id="_xCfS4UP8EeKahreWGKXckw" x="203" y="288"/>
    </children>
    <styles xmi:type="notation:DiagramStyle" xmi:id="_2N6EUUP7EeKahreWGKXckw"/>
    <edges xmi:type="notation:Edge" xmi:id="_66a2wEP7EeKahreWGKXckw" type="4001" element="_66ZBkEP7EeKahreWGKXckw" source="_5OzpgEP7EeKahreWGKXckw" target="_3Q2IAEP7EeKahreWGKXckw">
      <styles xmi:type="notation:RoutingStyle" xmi:id="_66a2wUP7EeKahreWGKXckw"/>
      <styles xmi:type="notation:FontStyle" xmi:id="_66a2wkP7EeKahreWGKXckw" fontName="Lucida Grande"/>
      <bendpoints xmi:type="notation:RelativeBendpoints" xmi:id="_66a2w0P7EeKahreWGKXckw" points="[-9, 1, -13, -94]$[-28, 100, -32, 5]"/>
      <sourceAnchor xmi:type="notation:IdentityAnchor" xmi:id="_66d6EEP7EeKahreWGKXckw" id="(1.0,0.972972972972973)"/>
    </edges>
    <edges xmi:type="notation:Edge" xmi:id="_AgEQoEP8EeKahreWGKXckw" type="4001" element="_AgDCgEP8EeKahreWGKXckw" source="_3Q2IAEP7EeKahreWGKXckw" target="_-UV_8EP7EeKahreWGKXckw">
      <styles xmi:type="notation:RoutingStyle" xmi:id="_AgEQoUP8EeKahreWGKXckw"/>
      <styles xmi:type="notation:FontStyle" xmi:id="_AgEQokP8EeKahreWGKXckw" fontName="Lucida Grande"/>
      <bendpoints xmi:type="notation:RelativeBendpoints" xmi:id="_AgEQo0P8EeKahreWGKXckw" points="[32, 12, -256, -87]$[238, 100, -50, 1]"/>
    </edges>
    <edges xmi:type="notation:Edge" xmi:id="_luU3AEP8EeKahreWGKXckw" type="4001" element="_luTo4EP8EeKahreWGKXckw" source="_igTb0EP8EeKahreWGKXckw" target="_-UV_8EP7EeKahreWGKXckw">
      <styles xmi:type="notation:RoutingStyle" xmi:id="_luU3AUP8EeKahreWGKXckw"/>
      <styles xmi:type="notation:FontStyle" xmi:id="_luU3AkP8EeKahreWGKXckw" fontName="Lucida Grande"/>
      <bendpoints xmi:type="notation:RelativeBendpoints" xmi:id="_luU3A0P8EeKahreWGKXckw" points="[8, 4, -88, -53]$[94, 46, -2, -11]"/>
      <targetAnchor xmi:type="notation:IdentityAnchor" xmi:id="_luXTQEP8EeKahreWGKXckw" id="(0.47,0.171875)"/>
    </edges>
    <edges xmi:type="notation:Edge" xmi:id="_x4JIgEP8EeKahreWGKXckw" type="4001" element="_x4H6YEP8EeKahreWGKXckw" source="_3Q2IAEP7EeKahreWGKXckw" target="_xCfS4EP8EeKahreWGKXckw">
      <styles xmi:type="notation:RoutingStyle" xmi:id="_x4JIgUP8EeKahreWGKXckw"/>
      <styles xmi:type="notation:FontStyle" xmi:id="_x4JIgkP8EeKahreWGKXckw" fontName="Lucida Grande"/>
      <bendpoints xmi:type="notation:RelativeBendpoints" xmi:id="_x4JIg0P8EeKahreWGKXckw" points="[3, 14, -3, -53]$[3, 58, -3, -9]"/>
      <sourceAnchor xmi:type="notation:IdentityAnchor" xmi:id="_x4LkwEP8EeKahreWGKXckw" id="(0.546875,0.78125)"/>
    </edges>
  </notation:Diagram>
</xmi:XMI>
