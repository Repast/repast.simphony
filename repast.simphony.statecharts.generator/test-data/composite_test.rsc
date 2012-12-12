<?xml version="1.0" encoding="UTF-8"?>
<xmi:XMI xmi:version="2.0" xmlns:xmi="http://www.omg.org/XMI" xmlns="http://repast.sf.net/statecharts" xmlns:notation="http://www.eclipse.org/gmf/runtime/1.0.2/notation">
  <StateMachine xmi:id="_HER2UECgEeKuNabxF1X6mw" agentType="my.statechart.Agent" package="anl.statechart" className="CompositeExample" nextID="70" id="Composite State Example 3">
    <states xmi:type="CompositeState" xmi:id="_HgyMIECgEeKuNabxF1X6mw" id="Composite State 0" uuid="_Hg2dkECgEeKuNabxF1X6mw">
      <children xmi:type="CompositeState" xmi:id="_Kc-QcECgEeKuNabxF1X6mw" id="Composite State 2" onEnter="agent.run();" uuid="_Kc-QcUCgEeKuNabxF1X6mw">
        <children xmi:type="CompositeState" xmi:id="_TtUacECgEeKuNabxF1X6mw" id="Composite State 7" uuid="_TtUacUCgEeKuNabxF1X6mw">
          <children xmi:type="State" xmi:id="_IXZzoEC3EeKuNabxF1X6mw" id="State 18" uuid="_IXaasEC3EeKuNabxF1X6mw"/>
          <children xmi:type="PseudoState" xmi:id="_JWl_wEC3EeKuNabxF1X6mw" id="Initial State Pointer 19" uuid="_JWmm0EC3EeKuNabxF1X6mw"/>
        </children>
        <children xmi:type="State" xmi:id="_7lOhoEC2EeKuNabxF1X6mw" id="State 12" onEnter="agent.left();" onExit="agent.right();" uuid="_7lOhoUC2EeKuNabxF1X6mw"/>
        <children xmi:type="State" xmi:id="_8HjyoEC2EeKuNabxF1X6mw" id="State 13" uuid="_8HjyoUC2EeKuNabxF1X6mw"/>
        <children xmi:type="PseudoState" xmi:id="_91yPAEC2EeKuNabxF1X6mw" id="Initial State Pointer 14" uuid="_91yPAUC2EeKuNabxF1X6mw"/>
      </children>
      <children xmi:type="CompositeState" xmi:id="_L70NAECgEeKuNabxF1X6mw" id="Composite State 3" uuid="_L700EECgEeKuNabxF1X6mw">
        <children xmi:type="CompositeState" xmi:id="_OvvGIECgEeKuNabxF1X6mw" id="Composite State 4" uuid="_OvvtMECgEeKuNabxF1X6mw">
          <children xmi:type="State" xmi:id="_RJkbMELYEeKp6c68Mc9ylQ" id="State 33" uuid="_RJkbMULYEeKp6c68Mc9ylQ"/>
          <children xmi:type="State" xmi:id="_RrrCsELYEeKp6c68Mc9ylQ" id="State 34" uuid="_RrrCsULYEeKp6c68Mc9ylQ"/>
          <children xmi:type="PseudoState" xmi:id="_WqqFMELYEeKp6c68Mc9ylQ" id="Initial State Pointer 37" uuid="_WqqFMULYEeKp6c68Mc9ylQ"/>
        </children>
        <children xmi:type="CompositeState" xmi:id="_QLQo8ECgEeKuNabxF1X6mw" id="Composite State 5" uuid="_QLQo8UCgEeKuNabxF1X6mw">
          <children xmi:type="State" xmi:id="_SFl-kELYEeKp6c68Mc9ylQ" id="State 35" uuid="_SFl-kULYEeKp6c68Mc9ylQ"/>
          <children xmi:type="State" xmi:id="_ScTOYELYEeKp6c68Mc9ylQ" id="State 36" uuid="_ScTOYULYEeKp6c68Mc9ylQ"/>
          <children xmi:type="PseudoState" xmi:id="_XCevUELYEeKp6c68Mc9ylQ" id="Initial State Pointer 38" uuid="_XCevUULYEeKp6c68Mc9ylQ"/>
        </children>
        <children xmi:type="PseudoState" xmi:id="_OEZSAEC_EeKGv_UdGig-Kw" id="Initial State Pointer 23" uuid="_OEbHMEC_EeKGv_UdGig-Kw"/>
        <children xmi:type="History" xmi:id="_UweFEEMHEeKp6c68Mc9ylQ" id="Deep History 56" onEnter="agent.up();" uuid="_UweFEUMHEeKp6c68Mc9ylQ"/>
      </children>
      <children xmi:type="PseudoState" xmi:id="_3cApgEC2EeKuNabxF1X6mw" id="Initial State Pointer 10" uuid="_3cBQkEC2EeKuNabxF1X6mw"/>
      <children xmi:type="History" xmi:id="_TqgikEMHEeKp6c68Mc9ylQ" id="Shallow History 55" onEnter="int val = params.getInteger(&quot;amt&quot;);&#xA;agent.up(val);" uuid="_TqgikUMHEeKp6c68Mc9ylQ" shallow="true"/>
    </states>
    <states xmi:type="CompositeState" xmi:id="_IWUtAECgEeKuNabxF1X6mw" id="Composite State 1" uuid="_IWVUEECgEeKuNabxF1X6mw">
      <children xmi:type="CompositeState" xmi:id="_SiwEwECgEeKuNabxF1X6mw" id="Composite State 6" uuid="_Siwr0ECgEeKuNabxF1X6mw">
        <children xmi:type="State" xmi:id="_GH3BQELYEeKp6c68Mc9ylQ" id="State 28" uuid="_GH4PYELYEeKp6c68Mc9ylQ"/>
        <children xmi:type="PseudoState" xmi:id="_JJyhMELYEeKp6c68Mc9ylQ" id="Initial State Pointer 29" uuid="_JJzvUELYEeKp6c68Mc9ylQ"/>
        <children xmi:type="State" xmi:id="_KS_k8ELYEeKp6c68Mc9ylQ" id="State 31" uuid="_KTAMAELYEeKp6c68Mc9ylQ"/>
      </children>
      <children xmi:type="PseudoState" xmi:id="_RWQEUEC_EeKGv_UdGig-Kw" id="Initial State Pointer 25" uuid="_RWQEUUC_EeKGv_UdGig-Kw"/>
    </states>
    <states xmi:type="PseudoState" xmi:id="_wG0koEC2EeKuNabxF1X6mw" id="Entry State Pointer" type="entry"/>
    <states xmi:type="PseudoState" xmi:id="_MRwzYEMHEeKp6c68Mc9ylQ" id="Choice 50" uuid="_MRwzYUMHEeKp6c68Mc9ylQ" type="choice"/>
    <states xmi:type="State" xmi:id="_PDPcgEMHEeKp6c68Mc9ylQ" id="State 51" uuid="_PDPcgUMHEeKp6c68Mc9ylQ"/>
    <states xmi:type="FinalState" xmi:id="_9edhsEMHEeKp6c68Mc9ylQ" id="Final State 60" onEnter="agent.end();" uuid="_9ehzIEMHEeKp6c68Mc9ylQ"/>
    <transitions xmi:type="Transition" xmi:id="_xuDoMEC2EeKuNabxF1X6mw" from="_wG0koEC2EeKuNabxF1X6mw" to="_HgyMIECgEeKuNabxF1X6mw" id="Transition 9" uuid="_xuEPQEC2EeKuNabxF1X6mw"/>
    <transitions xmi:type="Transition" xmi:id="_41I_oEC2EeKuNabxF1X6mw" from="_3cApgEC2EeKuNabxF1X6mw" to="_Kc-QcECgEeKuNabxF1X6mw" id="Transition 11" uuid="_41JmsEC2EeKuNabxF1X6mw"/>
    <transitions xmi:type="Transition" xmi:id="__YXcEEC2EeKuNabxF1X6mw" from="_91yPAEC2EeKuNabxF1X6mw" to="_7lOhoEC2EeKuNabxF1X6mw" id="Transition 15" uuid="__YYDIEC2EeKuNabxF1X6mw"/>
    <transitions xmi:type="Transition" xmi:id="_J75FMEC3EeKuNabxF1X6mw" from="_JWl_wEC3EeKuNabxF1X6mw" to="_IXZzoEC3EeKuNabxF1X6mw" id="Transition Foo" uuid="_J75FMUC3EeKuNabxF1X6mw"/>
    <transitions xmi:type="Transition" xmi:id="_P2JjgEC_EeKGv_UdGig-Kw" from="_OEZSAEC_EeKGv_UdGig-Kw" to="_OvvGIECgEeKuNabxF1X6mw" id="Transition 24" uuid="_P2JjgUC_EeKGv_UdGig-Kw"/>
    <transitions xmi:type="Transition" xmi:id="_R256UEC_EeKGv_UdGig-Kw" from="_RWQEUEC_EeKGv_UdGig-Kw" to="_SiwEwECgEeKuNabxF1X6mw" id="Transition 26" uuid="_R256UUC_EeKGv_UdGig-Kw"/>
    <transitions xmi:type="Transition" xmi:id="_J2UQAELYEeKp6c68Mc9ylQ" from="_JJyhMELYEeKp6c68Mc9ylQ" to="_GH3BQELYEeKp6c68Mc9ylQ" id="Transition 30" uuid="_J2UQAULYEeKp6c68Mc9ylQ"/>
    <transitions xmi:type="Transition" xmi:id="_Lhc8IELYEeKp6c68Mc9ylQ" from="_GH3BQELYEeKp6c68Mc9ylQ" to="_KS_k8ELYEeKp6c68Mc9ylQ" id="Transition 32" uuid="_Lhc8IULYEeKp6c68Mc9ylQ"/>
    <transitions xmi:type="Transition" xmi:id="_Xj4zQELYEeKp6c68Mc9ylQ" from="_XCevUELYEeKp6c68Mc9ylQ" to="_ScTOYELYEeKp6c68Mc9ylQ" id="Transition 39" uuid="_Xj4zQULYEeKp6c68Mc9ylQ"/>
    <transitions xmi:type="Transition" xmi:id="_YRNMQELYEeKp6c68Mc9ylQ" from="_WqqFMELYEeKp6c68Mc9ylQ" to="_RrrCsELYEeKp6c68Mc9ylQ" id="Transition 40" uuid="_YRNMQULYEeKp6c68Mc9ylQ"/>
    <transitions xmi:type="Transition" xmi:id="_YuBEEELYEeKp6c68Mc9ylQ" from="_RrrCsELYEeKp6c68Mc9ylQ" to="_RJkbMELYEeKp6c68Mc9ylQ" id="Transition 41" uuid="_YuBEEULYEeKp6c68Mc9ylQ"/>
    <transitions xmi:type="Transition" xmi:id="_ZJs6gELYEeKp6c68Mc9ylQ" from="_ScTOYELYEeKp6c68Mc9ylQ" to="_SFl-kELYEeKp6c68Mc9ylQ" id="Transition 42" uuid="_ZJs6gULYEeKp6c68Mc9ylQ"/>
    <transitions xmi:type="Transition" xmi:id="_aDxWEELYEeKp6c68Mc9ylQ" from="_7lOhoEC2EeKuNabxF1X6mw" to="_8HjyoEC2EeKuNabxF1X6mw" id="Transition 43" uuid="_aDxWEULYEeKp6c68Mc9ylQ"/>
    <transitions xmi:type="Transition" xmi:id="_af8UsELYEeKp6c68Mc9ylQ" from="_8HjyoEC2EeKuNabxF1X6mw" to="_TtUacECgEeKuNabxF1X6mw" id="Transition 44" uuid="_af8UsULYEeKp6c68Mc9ylQ"/>
    <transitions xmi:type="Transition" xmi:id="_PyoSEEMHEeKp6c68Mc9ylQ" from="_HgyMIECgEeKuNabxF1X6mw" to="_MRwzYEMHEeKp6c68Mc9ylQ" id="Transition 52" uuid="_Pyo5IEMHEeKp6c68Mc9ylQ"/>
    <transitions xmi:type="Transition" xmi:id="_QuW6gEMHEeKp6c68Mc9ylQ" from="_MRwzYEMHEeKp6c68Mc9ylQ" to="_PDPcgEMHEeKp6c68Mc9ylQ" outOfBranch="true" triggerType="condition" id="Transition 53" uuid="_QuXhkEMHEeKp6c68Mc9ylQ"/>
    <transitions xmi:type="Transition" xmi:id="_R0hRUEMHEeKp6c68Mc9ylQ" from="_MRwzYEMHEeKp6c68Mc9ylQ" to="_IWUtAECgEeKuNabxF1X6mw" outOfBranch="true" triggerType="condition" id="Transition 54" uuid="_R0hRUUMHEeKp6c68Mc9ylQ"/>
    <transitions xmi:type="Transition" xmi:id="_98wnQEMHEeKp6c68Mc9ylQ" from="_IWUtAECgEeKuNabxF1X6mw" to="_9edhsEMHEeKp6c68Mc9ylQ" id="Transition 61" uuid="_98wnQUMHEeKp6c68Mc9ylQ"/>
  </StateMachine>
  <notation:Diagram xmi:id="_HER2UUCgEeKuNabxF1X6mw" type="Statechart" element="_HER2UECgEeKuNabxF1X6mw" name="composite_test.rsc" measurementUnit="Pixel">
    <children xmi:type="notation:Shape" xmi:id="_Hg3rsECgEeKuNabxF1X6mw" type="2004" element="_HgyMIECgEeKuNabxF1X6mw" fontName="Lucida Grande">
      <children xmi:type="notation:DecorationNode" xmi:id="_Hg3rskCgEeKuNabxF1X6mw" type="5004"/>
      <children xmi:type="notation:DecorationNode" xmi:id="_Hg4SwECgEeKuNabxF1X6mw" type="7001">
        <children xmi:type="notation:Shape" xmi:id="_Kc_ekECgEeKuNabxF1X6mw" type="3002" element="_Kc-QcECgEeKuNabxF1X6mw" fontName="Lucida Grande">
          <children xmi:type="notation:DecorationNode" xmi:id="_Kc_ekkCgEeKuNabxF1X6mw" type="5003"/>
          <children xmi:type="notation:DecorationNode" xmi:id="_KdAFoECgEeKuNabxF1X6mw" type="7002">
            <children xmi:type="notation:Shape" xmi:id="_TtVokECgEeKuNabxF1X6mw" type="3002" element="_TtUacECgEeKuNabxF1X6mw" fontName="Lucida Grande">
              <children xmi:type="notation:DecorationNode" xmi:id="_WHTJkECgEeKuNabxF1X6mw" type="5003"/>
              <children xmi:type="notation:DecorationNode" xmi:id="_WHTJkUCgEeKuNabxF1X6mw" type="7002">
                <children xmi:type="notation:Shape" xmi:id="_IXbBwEC3EeKuNabxF1X6mw" type="3001" element="_IXZzoEC3EeKuNabxF1X6mw" fontName="Lucida Grande">
                  <children xmi:type="notation:DecorationNode" xmi:id="_IXbo0EC3EeKuNabxF1X6mw" type="5002"/>
                  <layoutConstraint xmi:type="notation:Bounds" xmi:id="_IXbBwUC3EeKuNabxF1X6mw" x="43" y="16"/>
                </children>
                <children xmi:type="notation:Shape" xmi:id="_JWnN4EC3EeKuNabxF1X6mw" type="3003" element="_JWl_wEC3EeKuNabxF1X6mw" fontName="Lucida Grande">
                  <layoutConstraint xmi:type="notation:Bounds" xmi:id="_JWnN4UC3EeKuNabxF1X6mw" x="129" y="12"/>
                </children>
              </children>
              <layoutConstraint xmi:type="notation:Bounds" xmi:id="_TtVokUCgEeKuNabxF1X6mw" x="24" y="106" width="163" height="127"/>
            </children>
            <children xmi:type="notation:Shape" xmi:id="_7lPvwEC2EeKuNabxF1X6mw" type="3001" element="_7lOhoEC2EeKuNabxF1X6mw" fontName="Lucida Grande">
              <children xmi:type="notation:DecorationNode" xmi:id="_7lPvwkC2EeKuNabxF1X6mw" type="5002"/>
              <layoutConstraint xmi:type="notation:Bounds" xmi:id="_7lPvwUC2EeKuNabxF1X6mw" x="24" y="52"/>
            </children>
            <children xmi:type="notation:Shape" xmi:id="_8HlAwEC2EeKuNabxF1X6mw" type="3001" element="_8HjyoEC2EeKuNabxF1X6mw" fontName="Lucida Grande">
              <children xmi:type="notation:DecorationNode" xmi:id="_8HlAwkC2EeKuNabxF1X6mw" type="5002"/>
              <layoutConstraint xmi:type="notation:Bounds" xmi:id="_8HlAwUC2EeKuNabxF1X6mw" x="117" y="52"/>
            </children>
            <children xmi:type="notation:Shape" xmi:id="_91zdIEC2EeKuNabxF1X6mw" type="3003" element="_91yPAEC2EeKuNabxF1X6mw" fontName="Lucida Grande">
              <layoutConstraint xmi:type="notation:Bounds" xmi:id="_91zdIUC2EeKuNabxF1X6mw" x="30" y="11"/>
            </children>
          </children>
          <layoutConstraint xmi:type="notation:Bounds" xmi:id="_Kc_ekUCgEeKuNabxF1X6mw" x="24" y="43" width="244" height="280"/>
        </children>
        <children xmi:type="notation:Shape" xmi:id="_L71bIECgEeKuNabxF1X6mw" type="3002" element="_L70NAECgEeKuNabxF1X6mw" fontName="Lucida Grande">
          <children xmi:type="notation:DecorationNode" xmi:id="_L72CMECgEeKuNabxF1X6mw" type="5003"/>
          <children xmi:type="notation:DecorationNode" xmi:id="_L72CMUCgEeKuNabxF1X6mw" type="7002">
            <children xmi:type="notation:Shape" xmi:id="_OvwUQECgEeKuNabxF1X6mw" type="3002" element="_OvvGIECgEeKuNabxF1X6mw" fontName="Lucida Grande">
              <children xmi:type="notation:DecorationNode" xmi:id="_OvwUQkCgEeKuNabxF1X6mw" type="5003"/>
              <children xmi:type="notation:DecorationNode" xmi:id="_Ovw7UECgEeKuNabxF1X6mw" type="7002">
                <children xmi:type="notation:Shape" xmi:id="_RJlpUELYEeKp6c68Mc9ylQ" type="3001" element="_RJkbMELYEeKp6c68Mc9ylQ" fontName="Lucida Grande">
                  <children xmi:type="notation:DecorationNode" xmi:id="_RJlpUkLYEeKp6c68Mc9ylQ" type="5002"/>
                  <layoutConstraint xmi:type="notation:Bounds" xmi:id="_RJlpUULYEeKp6c68Mc9ylQ" x="21" y="35"/>
                </children>
                <children xmi:type="notation:Shape" xmi:id="_RrrpwELYEeKp6c68Mc9ylQ" type="3001" element="_RrrCsELYEeKp6c68Mc9ylQ" fontName="Lucida Grande">
                  <children xmi:type="notation:DecorationNode" xmi:id="_RrsQ0ELYEeKp6c68Mc9ylQ" type="5002"/>
                  <layoutConstraint xmi:type="notation:Bounds" xmi:id="_RrrpwULYEeKp6c68Mc9ylQ" x="96" y="46"/>
                </children>
                <children xmi:type="notation:Shape" xmi:id="_WqqsQELYEeKp6c68Mc9ylQ" type="3003" element="_WqqFMELYEeKp6c68Mc9ylQ" fontName="Lucida Grande">
                  <layoutConstraint xmi:type="notation:Bounds" xmi:id="_WqrTUELYEeKp6c68Mc9ylQ" x="112" y="12"/>
                </children>
              </children>
              <layoutConstraint xmi:type="notation:Bounds" xmi:id="_OvwUQUCgEeKuNabxF1X6mw" x="24" y="16" width="226" height="154"/>
            </children>
            <children xmi:type="notation:Shape" xmi:id="_QLRQAECgEeKuNabxF1X6mw" type="3002" element="_QLQo8ECgEeKuNabxF1X6mw" fontName="Lucida Grande">
              <children xmi:type="notation:DecorationNode" xmi:id="_QLR3EECgEeKuNabxF1X6mw" type="5003"/>
              <children xmi:type="notation:DecorationNode" xmi:id="_QLR3EUCgEeKuNabxF1X6mw" type="7002">
                <children xmi:type="notation:Shape" xmi:id="_SFnMsELYEeKp6c68Mc9ylQ" type="3001" element="_SFl-kELYEeKp6c68Mc9ylQ" fontName="Lucida Grande">
                  <children xmi:type="notation:DecorationNode" xmi:id="_SFnMskLYEeKp6c68Mc9ylQ" type="5002"/>
                  <layoutConstraint xmi:type="notation:Bounds" xmi:id="_SFnMsULYEeKp6c68Mc9ylQ" x="20" y="20"/>
                </children>
                <children xmi:type="notation:Shape" xmi:id="_ScT1cELYEeKp6c68Mc9ylQ" type="3001" element="_ScTOYELYEeKp6c68Mc9ylQ" fontName="Lucida Grande">
                  <children xmi:type="notation:DecorationNode" xmi:id="_ScUcgULYEeKp6c68Mc9ylQ" type="5002"/>
                  <layoutConstraint xmi:type="notation:Bounds" xmi:id="_ScUcgELYEeKp6c68Mc9ylQ" x="97" y="26"/>
                </children>
                <children xmi:type="notation:Shape" xmi:id="_XCfWYELYEeKp6c68Mc9ylQ" type="3003" element="_XCevUELYEeKp6c68Mc9ylQ" fontName="Lucida Grande">
                  <layoutConstraint xmi:type="notation:Bounds" xmi:id="_XCf9cELYEeKp6c68Mc9ylQ" x="185" y="28"/>
                </children>
              </children>
              <layoutConstraint xmi:type="notation:Bounds" xmi:id="_QLRQAUCgEeKuNabxF1X6mw" x="24" y="187" width="262" height="118"/>
            </children>
            <children xmi:type="notation:Shape" xmi:id="_OEf_sEC_EeKGv_UdGig-Kw" type="3003" element="_OEZSAEC_EeKGv_UdGig-Kw" fontName="Lucida Grande">
              <layoutConstraint xmi:type="notation:Bounds" xmi:id="_OEf_sUC_EeKGv_UdGig-Kw" x="6" y="2"/>
            </children>
            <children xmi:type="notation:Shape" xmi:id="_UwfTMEMHEeKp6c68Mc9ylQ" type="3009" element="_UweFEEMHEeKp6c68Mc9ylQ" fontName="Lucida Grande">
              <layoutConstraint xmi:type="notation:Bounds" xmi:id="_UwfTMUMHEeKp6c68Mc9ylQ" x="285" y="79"/>
            </children>
          </children>
          <layoutConstraint xmi:type="notation:Bounds" xmi:id="_L71bIUCgEeKuNabxF1X6mw" x="294" y="43" width="343" height="361"/>
        </children>
        <children xmi:type="notation:Shape" xmi:id="_3cCesEC2EeKuNabxF1X6mw" type="3003" element="_3cApgEC2EeKuNabxF1X6mw" fontName="Lucida Grande">
          <layoutConstraint xmi:type="notation:Bounds" xmi:id="_3cCesUC2EeKuNabxF1X6mw" x="116" y="10"/>
        </children>
        <children xmi:type="notation:Shape" xmi:id="_TqhwsEMHEeKp6c68Mc9ylQ" type="3008" element="_TqgikEMHEeKp6c68Mc9ylQ" fontName="Lucida Grande">
          <layoutConstraint xmi:type="notation:Bounds" xmi:id="_TqhwsUMHEeKp6c68Mc9ylQ" x="490" y="7"/>
        </children>
      </children>
      <layoutConstraint xmi:type="notation:Bounds" xmi:id="_Hg3rsUCgEeKuNabxF1X6mw" x="18" y="81" width="685" height="451"/>
    </children>
    <children xmi:type="notation:Shape" xmi:id="_IWWiMECgEeKuNabxF1X6mw" type="2004" element="_IWUtAECgEeKuNabxF1X6mw" fontName="Lucida Grande">
      <children xmi:type="notation:DecorationNode" xmi:id="_JDyP8kCgEeKuNabxF1X6mw" type="5004"/>
      <children xmi:type="notation:DecorationNode" xmi:id="_JDyP80CgEeKuNabxF1X6mw" type="7001">
        <children xmi:type="notation:Shape" xmi:id="_SixS4ECgEeKuNabxF1X6mw" type="3002" element="_SiwEwECgEeKuNabxF1X6mw" fontName="Lucida Grande">
          <children xmi:type="notation:DecorationNode" xmi:id="_Six58ECgEeKuNabxF1X6mw" type="5003"/>
          <children xmi:type="notation:DecorationNode" xmi:id="_Six58UCgEeKuNabxF1X6mw" type="7002">
            <children xmi:type="notation:Shape" xmi:id="_GH8g0ELYEeKp6c68Mc9ylQ" type="3001" element="_GH3BQELYEeKp6c68Mc9ylQ" fontName="Lucida Grande">
              <children xmi:type="notation:DecorationNode" xmi:id="_GH9H4ELYEeKp6c68Mc9ylQ" type="5002"/>
              <layoutConstraint xmi:type="notation:Bounds" xmi:id="_GH8g0ULYEeKp6c68Mc9ylQ" x="19" y="46"/>
            </children>
            <children xmi:type="notation:Shape" xmi:id="_JJ2LkELYEeKp6c68Mc9ylQ" type="3003" element="_JJyhMELYEeKp6c68Mc9ylQ" fontName="Lucida Grande">
              <layoutConstraint xmi:type="notation:Bounds" xmi:id="_JJ2LkULYEeKp6c68Mc9ylQ" x="49" y="14"/>
            </children>
            <children xmi:type="notation:Shape" xmi:id="_KTAzEELYEeKp6c68Mc9ylQ" type="3001" element="_KS_k8ELYEeKp6c68Mc9ylQ" fontName="Lucida Grande">
              <children xmi:type="notation:DecorationNode" xmi:id="_KTBaIELYEeKp6c68Mc9ylQ" type="5002"/>
              <layoutConstraint xmi:type="notation:Bounds" xmi:id="_KTAzEULYEeKp6c68Mc9ylQ" x="84" y="59"/>
            </children>
          </children>
          <layoutConstraint xmi:type="notation:Bounds" xmi:id="_SixS4UCgEeKuNabxF1X6mw" x="15" y="7" width="208" height="226"/>
        </children>
        <children xmi:type="notation:Shape" xmi:id="_RWRScEC_EeKGv_UdGig-Kw" type="3003" element="_RWQEUEC_EeKGv_UdGig-Kw" fontName="Lucida Grande">
          <layoutConstraint xmi:type="notation:Bounds" xmi:id="_RWRScUC_EeKGv_UdGig-Kw" x="168" y="286"/>
        </children>
      </children>
      <layoutConstraint xmi:type="notation:Bounds" xmi:id="_IWWiMUCgEeKuNabxF1X6mw" x="792" y="81" width="244" height="568"/>
    </children>
    <children xmi:type="notation:Shape" xmi:id="_wG1ywEC2EeKuNabxF1X6mw" type="2007" element="_wG0koEC2EeKuNabxF1X6mw" fontName="Lucida Grande">
      <layoutConstraint xmi:type="notation:Bounds" xmi:id="_wG1ywUC2EeKuNabxF1X6mw" x="270"/>
    </children>
    <children xmi:type="notation:Shape" xmi:id="_MRyBgEMHEeKp6c68Mc9ylQ" type="2006" element="_MRwzYEMHEeKp6c68Mc9ylQ" fontName="Lucida Grande">
      <layoutConstraint xmi:type="notation:Bounds" xmi:id="_MRyBgUMHEeKp6c68Mc9ylQ" x="749" y="108"/>
    </children>
    <children xmi:type="notation:Shape" xmi:id="_PDQqoEMHEeKp6c68Mc9ylQ" type="2003" element="_PDPcgEMHEeKp6c68Mc9ylQ" fontName="Lucida Grande">
      <children xmi:type="notation:DecorationNode" xmi:id="_PDQqokMHEeKp6c68Mc9ylQ" type="5001"/>
      <layoutConstraint xmi:type="notation:Bounds" xmi:id="_PDQqoUMHEeKp6c68Mc9ylQ" x="749" y="37"/>
    </children>
    <children xmi:type="notation:Shape" xmi:id="_9ejoUEMHEeKp6c68Mc9ylQ" type="2008" element="_9edhsEMHEeKp6c68Mc9ylQ" fontName="Lucida Grande">
      <layoutConstraint xmi:type="notation:Bounds" xmi:id="_9ejoUUMHEeKp6c68Mc9ylQ" x="948" y="31"/>
    </children>
    <styles xmi:type="notation:DiagramStyle" xmi:id="_HER2UkCgEeKuNabxF1X6mw"/>
    <edges xmi:type="notation:Edge" xmi:id="_xuE2UEC2EeKuNabxF1X6mw" type="4001" element="_xuDoMEC2EeKuNabxF1X6mw" source="_wG1ywEC2EeKuNabxF1X6mw" target="_Hg3rsECgEeKuNabxF1X6mw">
      <styles xmi:type="notation:RoutingStyle" xmi:id="_xuE2UUC2EeKuNabxF1X6mw"/>
      <styles xmi:type="notation:FontStyle" xmi:id="_xuE2UkC2EeKuNabxF1X6mw" fontName="Lucida Grande"/>
      <bendpoints xmi:type="notation:RelativeBendpoints" xmi:id="_xuE2U0C2EeKuNabxF1X6mw" points="[-1, 0, 111, -28]$[-110, 26, 2, -2]"/>
      <sourceAnchor xmi:type="notation:IdentityAnchor" xmi:id="_xuH5oEC2EeKuNabxF1X6mw" id="(0.058823529411764705,0.5294117647058824)"/>
      <targetAnchor xmi:type="notation:IdentityAnchor" xmi:id="_xuH5oUC2EeKuNabxF1X6mw" id="(0.4490909090909091,0.00904977375565611)"/>
    </edges>
    <edges xmi:type="notation:Edge" xmi:id="_41KNwEC2EeKuNabxF1X6mw" type="4001" element="_41I_oEC2EeKuNabxF1X6mw" source="_3cCesEC2EeKuNabxF1X6mw" target="_Kc_ekECgEeKuNabxF1X6mw">
      <styles xmi:type="notation:RoutingStyle" xmi:id="_41KNwUC2EeKuNabxF1X6mw"/>
      <styles xmi:type="notation:FontStyle" xmi:id="_41KNwkC2EeKuNabxF1X6mw" fontName="Lucida Grande"/>
      <bendpoints xmi:type="notation:RelativeBendpoints" xmi:id="_41K00EC2EeKuNabxF1X6mw" points="[-1, 8, 0, -54]$[-1, 62, 0, 0]"/>
      <targetAnchor xmi:type="notation:IdentityAnchor" xmi:id="_41MqAEC2EeKuNabxF1X6mw" id="(0.577922077922078,0.0)"/>
    </edges>
    <edges xmi:type="notation:Edge" xmi:id="__YYqMEC2EeKuNabxF1X6mw" type="4001" element="__YXcEEC2EeKuNabxF1X6mw" source="_91zdIEC2EeKuNabxF1X6mw" target="_7lPvwEC2EeKuNabxF1X6mw">
      <styles xmi:type="notation:RoutingStyle" xmi:id="__YYqMUC2EeKuNabxF1X6mw"/>
      <styles xmi:type="notation:FontStyle" xmi:id="__YYqMkC2EeKuNabxF1X6mw" fontName="Lucida Grande"/>
      <bendpoints xmi:type="notation:RelativeBendpoints" xmi:id="__YYqM0C2EeKuNabxF1X6mw" points="[4, 8, -7, -46]$[4, 74, -7, 20]"/>
    </edges>
    <edges xmi:type="notation:Edge" xmi:id="_J76TUEC3EeKuNabxF1X6mw" type="4001" element="_J75FMEC3EeKuNabxF1X6mw" source="_JWnN4EC3EeKuNabxF1X6mw" target="_IXbBwEC3EeKuNabxF1X6mw">
      <styles xmi:type="notation:RoutingStyle" xmi:id="_J76TUUC3EeKuNabxF1X6mw"/>
      <styles xmi:type="notation:FontStyle" xmi:id="_J76TUkC3EeKuNabxF1X6mw" fontName="Lucida Grande"/>
      <bendpoints xmi:type="notation:RelativeBendpoints" xmi:id="_J76TU0C3EeKuNabxF1X6mw" points="[-7, 2, 45, -9]$[-45, 8, 7, -3]"/>
      <targetAnchor xmi:type="notation:IdentityAnchor" xmi:id="_J78vkEC3EeKuNabxF1X6mw" id="(0.8541666666666666,0.35)"/>
    </edges>
    <edges xmi:type="notation:Edge" xmi:id="_P2LYsEC_EeKGv_UdGig-Kw" type="4001" element="_P2JjgEC_EeKGv_UdGig-Kw" source="_OEf_sEC_EeKGv_UdGig-Kw" target="_OvwUQECgEeKuNabxF1X6mw">
      <styles xmi:type="notation:RoutingStyle" xmi:id="_P2LYsUC_EeKGv_UdGig-Kw"/>
      <styles xmi:type="notation:FontStyle" xmi:id="_P2LYskC_EeKGv_UdGig-Kw" fontName="Lucida Grande"/>
      <bendpoints xmi:type="notation:RelativeBendpoints" xmi:id="_P2LYs0C_EeKGv_UdGig-Kw" points="[1, 8, -9, -62]$[-11, 52, -21, -18]"/>
      <targetAnchor xmi:type="notation:IdentityAnchor" xmi:id="_P2PDEEC_EeKGv_UdGig-Kw" id="(0.21649484536082475,0.23595505617977527)"/>
    </edges>
    <edges xmi:type="notation:Edge" xmi:id="_R27IcEC_EeKGv_UdGig-Kw" type="4001" element="_R256UEC_EeKGv_UdGig-Kw" source="_RWRScEC_EeKGv_UdGig-Kw" target="_SixS4ECgEeKuNabxF1X6mw">
      <styles xmi:type="notation:RoutingStyle" xmi:id="_R27IcUC_EeKGv_UdGig-Kw"/>
      <styles xmi:type="notation:FontStyle" xmi:id="_R27IckC_EeKGv_UdGig-Kw" fontName="Lucida Grande"/>
      <bendpoints xmi:type="notation:RelativeBendpoints" xmi:id="_R27Ic0C_EeKGv_UdGig-Kw" points="[2, -7, -41, 186]$[14, -60, -29, 133]"/>
      <targetAnchor xmi:type="notation:IdentityAnchor" xmi:id="_R29ksEC_EeKGv_UdGig-Kw" id="(0.9797979797979798,0.4117647058823529)"/>
    </edges>
    <edges xmi:type="notation:Edge" xmi:id="_J2WFMELYEeKp6c68Mc9ylQ" type="4001" element="_J2UQAELYEeKp6c68Mc9ylQ" source="_JJ2LkELYEeKp6c68Mc9ylQ" target="_GH8g0ELYEeKp6c68Mc9ylQ">
      <styles xmi:type="notation:RoutingStyle" xmi:id="_J2WFMULYEeKp6c68Mc9ylQ"/>
      <styles xmi:type="notation:FontStyle" xmi:id="_J2WFMkLYEeKp6c68Mc9ylQ" fontName="Lucida Grande"/>
      <bendpoints xmi:type="notation:RelativeBendpoints" xmi:id="_J2WFM0LYEeKp6c68Mc9ylQ" points="[-2, 3, 14, -23]$[-17, 25, -1, -1]"/>
      <sourceAnchor xmi:type="notation:IdentityAnchor" xmi:id="_J2ZvkELYEeKp6c68Mc9ylQ" id="(0.13333333333333333,0.4666666666666667)"/>
      <targetAnchor xmi:type="notation:IdentityAnchor" xmi:id="_J2ZvkULYEeKp6c68Mc9ylQ" id="(0.3333333333333333,0.025)"/>
    </edges>
    <edges xmi:type="notation:Edge" xmi:id="_LheKQELYEeKp6c68Mc9ylQ" type="4001" element="_Lhc8IELYEeKp6c68Mc9ylQ" source="_GH8g0ELYEeKp6c68Mc9ylQ" target="_KTAzEELYEeKp6c68Mc9ylQ">
      <styles xmi:type="notation:RoutingStyle" xmi:id="_LheKQULYEeKp6c68Mc9ylQ"/>
      <styles xmi:type="notation:FontStyle" xmi:id="_LheKQkLYEeKp6c68Mc9ylQ" fontName="Lucida Grande"/>
      <bendpoints xmi:type="notation:RelativeBendpoints" xmi:id="_LheKQ0LYEeKp6c68Mc9ylQ" points="[24, 6, -23, -7]$[41, 13, -6, 0]"/>
      <targetAnchor xmi:type="notation:IdentityAnchor" xmi:id="_LhgmgELYEeKp6c68Mc9ylQ" id="(0.125,0.5)"/>
    </edges>
    <edges xmi:type="notation:Edge" xmi:id="_Xj6BYELYEeKp6c68Mc9ylQ" type="4001" element="_Xj4zQELYEeKp6c68Mc9ylQ" source="_XCfWYELYEeKp6c68Mc9ylQ" target="_ScT1cELYEeKp6c68Mc9ylQ">
      <styles xmi:type="notation:RoutingStyle" xmi:id="_Xj6BYULYEeKp6c68Mc9ylQ"/>
      <styles xmi:type="notation:FontStyle" xmi:id="_Xj6BYkLYEeKp6c68Mc9ylQ" fontName="Lucida Grande"/>
      <bendpoints xmi:type="notation:RelativeBendpoints" xmi:id="_Xj6BY0LYEeKp6c68Mc9ylQ" points="[-4, 7, 64, -8]$[-59, -5, 9, -20]"/>
      <sourceAnchor xmi:type="notation:IdentityAnchor" xmi:id="_Xj8doELYEeKp6c68Mc9ylQ" id="(0.26666666666666666,0.2)"/>
    </edges>
    <edges xmi:type="notation:Edge" xmi:id="_YROaYELYEeKp6c68Mc9ylQ" type="4001" element="_YRNMQELYEeKp6c68Mc9ylQ" source="_WqqsQELYEeKp6c68Mc9ylQ" target="_RrrpwELYEeKp6c68Mc9ylQ">
      <styles xmi:type="notation:RoutingStyle" xmi:id="_YROaYULYEeKp6c68Mc9ylQ"/>
      <styles xmi:type="notation:FontStyle" xmi:id="_YROaYkLYEeKp6c68Mc9ylQ" fontName="Lucida Grande"/>
      <bendpoints xmi:type="notation:RelativeBendpoints" xmi:id="_YROaY0LYEeKp6c68Mc9ylQ" points="[-1, 8, 0, -22]$[-2, 27, -1, -3]"/>
      <targetAnchor xmi:type="notation:IdentityAnchor" xmi:id="_YRQ2oELYEeKp6c68Mc9ylQ" id="(0.4583333333333333,0.075)"/>
    </edges>
    <edges xmi:type="notation:Edge" xmi:id="_YueXEELYEeKp6c68Mc9ylQ" type="4001" element="_YuBEEELYEeKp6c68Mc9ylQ" source="_RrrpwELYEeKp6c68Mc9ylQ" target="_RJlpUELYEeKp6c68Mc9ylQ">
      <styles xmi:type="notation:RoutingStyle" xmi:id="_YueXEULYEeKp6c68Mc9ylQ"/>
      <styles xmi:type="notation:FontStyle" xmi:id="_YueXEkLYEeKp6c68Mc9ylQ" fontName="Lucida Grande"/>
      <bendpoints xmi:type="notation:RelativeBendpoints" xmi:id="_YueXE0LYEeKp6c68Mc9ylQ" points="[-24, -6, 51, 5]$[-68, 9, 7, 20]"/>
    </edges>
    <edges xmi:type="notation:Edge" xmi:id="_ZKKNgELYEeKp6c68Mc9ylQ" type="4001" element="_ZJs6gELYEeKp6c68Mc9ylQ" source="_ScT1cELYEeKp6c68Mc9ylQ" target="_SFnMsELYEeKp6c68Mc9ylQ">
      <styles xmi:type="notation:RoutingStyle" xmi:id="_ZKK0kELYEeKp6c68Mc9ylQ"/>
      <styles xmi:type="notation:FontStyle" xmi:id="_ZKK0kULYEeKp6c68Mc9ylQ" fontName="Lucida Grande"/>
      <bendpoints xmi:type="notation:RelativeBendpoints" xmi:id="_ZKK0kkLYEeKp6c68Mc9ylQ" points="[-24, 0, 53, 6]$[-80, 14, -3, 20]"/>
    </edges>
    <edges xmi:type="notation:Edge" xmi:id="_aEPQIELYEeKp6c68Mc9ylQ" type="4001" element="_aDxWEELYEeKp6c68Mc9ylQ" source="_7lPvwEC2EeKuNabxF1X6mw" target="_8HlAwEC2EeKuNabxF1X6mw">
      <styles xmi:type="notation:RoutingStyle" xmi:id="_aEPQIULYEeKp6c68Mc9ylQ"/>
      <styles xmi:type="notation:FontStyle" xmi:id="_aEPQIkLYEeKp6c68Mc9ylQ" fontName="Lucida Grande"/>
      <bendpoints xmi:type="notation:RelativeBendpoints" xmi:id="_aEPQI0LYEeKp6c68Mc9ylQ" points="[24, 0, -69, 0]$[69, 0, -24, 0]"/>
    </edges>
    <edges xmi:type="notation:Edge" xmi:id="_agZnsELYEeKp6c68Mc9ylQ" type="4001" element="_af8UsELYEeKp6c68Mc9ylQ" source="_8HlAwEC2EeKuNabxF1X6mw" target="_TtVokECgEeKuNabxF1X6mw">
      <styles xmi:type="notation:RoutingStyle" xmi:id="_agZnsULYEeKp6c68Mc9ylQ"/>
      <styles xmi:type="notation:FontStyle" xmi:id="_agZnskLYEeKp6c68Mc9ylQ" fontName="Lucida Grande"/>
      <bendpoints xmi:type="notation:RelativeBendpoints" xmi:id="_agZns0LYEeKp6c68Mc9ylQ" points="[-5, 20, 5, -24]$[-6, 34, 4, -10]"/>
      <targetAnchor xmi:type="notation:IdentityAnchor" xmi:id="_agcD8ELYEeKp6c68Mc9ylQ" id="(0.656441717791411,0.07874015748031496)"/>
    </edges>
    <edges xmi:type="notation:Edge" xmi:id="_PypgMEMHEeKp6c68Mc9ylQ" type="4001" element="_PyoSEEMHEeKp6c68Mc9ylQ" source="_Hg3rsECgEeKuNabxF1X6mw" target="_MRyBgEMHEeKp6c68Mc9ylQ">
      <styles xmi:type="notation:RoutingStyle" xmi:id="_PypgMUMHEeKp6c68Mc9ylQ"/>
      <styles xmi:type="notation:FontStyle" xmi:id="_PypgMkMHEeKp6c68Mc9ylQ" fontName="Lucida Grande"/>
      <bendpoints xmi:type="notation:RelativeBendpoints" xmi:id="_PypgM0MHEeKp6c68Mc9ylQ" points="[4, -3, -46, 0]$[48, -4, -2, -1]"/>
      <sourceAnchor xmi:type="notation:IdentityAnchor" xmi:id="_PysjgEMHEeKp6c68Mc9ylQ" id="(0.9941605839416059,0.10199556541019955)"/>
      <targetAnchor xmi:type="notation:IdentityAnchor" xmi:id="_PysjgUMHEeKp6c68Mc9ylQ" id="(0.10526315789473684,0.3684210526315789)"/>
    </edges>
    <edges xmi:type="notation:Edge" xmi:id="_QuYIoEMHEeKp6c68Mc9ylQ" type="4001" element="_QuW6gEMHEeKp6c68Mc9ylQ" source="_MRyBgEMHEeKp6c68Mc9ylQ" target="_PDQqoEMHEeKp6c68Mc9ylQ">
      <styles xmi:type="notation:RoutingStyle" xmi:id="_QuYIoUMHEeKp6c68Mc9ylQ"/>
      <styles xmi:type="notation:FontStyle" xmi:id="_QuYIokMHEeKp6c68Mc9ylQ" fontName="Lucida Grande"/>
      <bendpoints xmi:type="notation:RelativeBendpoints" xmi:id="_QuYvsEMHEeKp6c68Mc9ylQ" points="[10, -9, -5, 51]$[10, -80, -5, -20]"/>
    </edges>
    <edges xmi:type="notation:Edge" xmi:id="_R1C1wEMHEeKp6c68Mc9ylQ" type="4001" element="_R0hRUEMHEeKp6c68Mc9ylQ" source="_MRyBgEMHEeKp6c68Mc9ylQ" target="_IWWiMECgEeKuNabxF1X6mw">
      <styles xmi:type="notation:RoutingStyle" xmi:id="_R1C1wUMHEeKp6c68Mc9ylQ"/>
      <styles xmi:type="notation:FontStyle" xmi:id="_R1C1wkMHEeKp6c68Mc9ylQ" fontName="Lucida Grande"/>
      <bendpoints xmi:type="notation:RelativeBendpoints" xmi:id="_R1C1w0MHEeKp6c68Mc9ylQ" points="[10, 6, -27, -18]$[34, 18, -3, -6]"/>
      <targetAnchor xmi:type="notation:IdentityAnchor" xmi:id="_R1F5EEMHEeKp6c68Mc9ylQ" id="(0.012295081967213115,0.1056338028169014)"/>
    </edges>
    <edges xmi:type="notation:Edge" xmi:id="_98x1YEMHEeKp6c68Mc9ylQ" type="4001" element="_98wnQEMHEeKp6c68Mc9ylQ" source="_IWWiMECgEeKuNabxF1X6mw" target="_9ejoUEMHEeKp6c68Mc9ylQ">
      <styles xmi:type="notation:RoutingStyle" xmi:id="_98x1YUMHEeKp6c68Mc9ylQ"/>
      <styles xmi:type="notation:FontStyle" xmi:id="_98x1YkMHEeKp6c68Mc9ylQ" fontName="Lucida Grande"/>
      <bendpoints xmi:type="notation:RelativeBendpoints" xmi:id="_98x1Y0MHEeKp6c68Mc9ylQ" points="[-3, -1, 0, 43]$[-3, -51, 0, -7]"/>
      <sourceAnchor xmi:type="notation:IdentityAnchor" xmi:id="_980RoEMHEeKp6c68Mc9ylQ" id="(0.680327868852459,0.0017605633802816902)"/>
    </edges>
  </notation:Diagram>
</xmi:XMI>
