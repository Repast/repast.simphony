let content_idx = 0;
let probe_content = new Map();
let menu_items = new Map();
let view_ids = new Map();
let current_item = null;
let init = false;

export function clearProbes() {
    $("#agents-select").children().not(':first-child').remove();
    $("#probe-content").empty();
    content_idx = 0;
    probe_content.clear();
    menu_items.clear();
    view_ids.clear();
    current_item = null;
}

export function createProbes(probes, display_id, display_type) {
    if (!init) {
        let select = document.getElementById("agents-select");
        select.onchange = itemSelected;
        init = true;
    }

    let value = "";
    //console.log(probes[0]);
    probes.forEach( (probe) => {
        console.log(probe);
        let view_id = probe.view_id;
        let menu_view_id = `${view_id}_${display_type}`;
        if (menu_items.has(menu_view_id)) {
            let item = menu_items.get(menu_view_id);
            value = item.getAttribute("value");
            updateForm(probe_content.get(value), probe.props, probe.locations);       

        } else {
            if (view_ids.has(display_type)) {
                if (view_ids.get(display_type).has(display_id)) {
                    view_ids.get(display_type).get(display_id).push(view_id);
                } else {
                    let m = view_ids.get(display_type);
                    m.set(display_id, [view_id]);
                }
            } else {
                let m = new Map();
                m.set(display_id, [view_id]);
                view_ids.set(display_type, m);
            }

            createProbeMenuItem(content_idx, probe.id, menu_view_id);
            createContent(content_idx, probe.props, probe.locations);
            value = content_idx.toString();
            ++content_idx;
        }
    });

    if (probes.length > 0) {
        let select = document.getElementById("agents-select");
        select.value = value;
        selectItem(value);
    }
        
    //option.setAttribute("selected", "selected");
    //let value = option.getAttribute("value");
    //selectItem(value);

    //$('#agent-tabs a[href="#' + `a${ti}` + '"]').tab('show');
}

export function getPickedIds() {
    let result = []
    view_ids.forEach( (v, k) => {
        let display_type = k;
        v.forEach( (v, k) => {
            result.push(
                {
                    display_id: k,
                    picked_ids: v,
                    display_type: display_type
                }
            )
        });
    });
    return result;
}

function updateFormEntry(form, p) {
    let ctrl = form.querySelector(`[pname=${p.name}]`);
    let value = p.value.toLowerCase();
    if (value === "true" || value === "false") {
        ctrl.checked = value == 'true';
    } else {
        ctrl.value = p.value;
    }
}

function updateForm(form, props, locs) {
    props.forEach( (p) => {
        updateFormEntry(form, p);
    });

    locs.forEach((p) => {
        updateFormEntry(form, p);
    });
}

function createFormEntry(form, p) {
    let form_div = document.createElement("div");
    form_div.className = "form-group";
    let label = document.createElement("label");
    label.htmlFor = p.name;
    label.innerHTML = p.display_name;

    var ctrl = document.createElement("input");
    ctrl.setAttribute('readonly', 'readonly');

    let value = p.value.toLowerCase();
    if (value === "true" || value === "false") {
        // show as check box
        ctrl.type = "checkbox";
        ctrl.className = "form-check-input";
        label.className = "form-check-label";
        form_div.className = "form-check mb-2";
        form_div.appendChild(ctrl);
        form_div.appendChild(label);
        ctrl.checked = value == 'true';

    } else {
        let n = Number(p.value);
        if (Number.isNaN(n)) {
            ctrl.type = "text";
        } else {
            ctrl.type = "number";
        }
        ctrl.className = "form-control form-control-sm";
        ctrl.value = p.value;
        form_div.appendChild(label);
        form_div.appendChild(ctrl);
    }
    ctrl.setAttribute("pname", p.name);
    form.appendChild(form_div);
}

function createContent(content_id, props, locs) {
    let form = document.createElement("form");
    form.className = "px-3 mt-1 mb-1";
    props.forEach((p) => {
       createFormEntry(form, p);
    });
    locs.forEach((loc) => {
        createFormEntry(form, loc);
    });
    probe_content.set(content_id.toString(), form);
}

function itemSelected() {
    selectItem(this.value);
}

function selectItem(value) {
    let content = document.getElementById("probe-content");
    if (current_item != null) {
        content.removeChild(current_item);
        current_item = null;
    }
    if (value != "") {
        current_item = probe_content.get(value);
        content.appendChild(current_item);
    }
}

function createProbeMenuItem(value, title, view_id) {
    let menu = document.getElementById("agents-select");
    let item = document.createElement("option");
    item.innerHTML = title;
    item.setAttribute("value", value);
    menu.appendChild(item);
    menu_items.set(view_id, item);
}

function createTab(tab_id, title) {
    // Get a reference to the container element that will hold our scene
    let tabs_container = document.getElementById("agent-tabs")
    let tab = document.createElement("li");
    tab.className = "nav-item";
    let selected = false;
    let tab_anchor = document.createElement("a");
    tab_anchor.className = (selected ? "nav-link active" : "nav-link");
    let tab_anchor_id = `a${tab_id}-tab`;
    let did = `a${tab_id}`;
    tab_anchor.id = tab_anchor_id;

    tab_anchor.setAttribute("data-toggle", "tab");
    tab_anchor.setAttribute("role", "tab");
    tab_anchor.setAttribute("aria-controls", did);
    tab_anchor.setAttribute("href", `#${did}`);

    tab_anchor.setAttribute("aria-selected", (selected ? "true" : "false"));
    tab_anchor.innerHTML = title;

    let agent_content = document.getElementById("agent-content");
    let tab_content = document.createElement("div");
    tab_content.className = (selected ? "tab-pane fade show active" : "tab-pane fade");
    tab_content.id = did;
    tab_content.setAttribute("role", "tabpanel");
    tab_content.setAttribute("aria-labelledby", tab_anchor_id);

    agent_content.appendChild(tab_content);
    tab.appendChild(tab_anchor);
    tabs_container.appendChild(tab);

    return did;

    // $('#display-tabs a[href="#' + did + '"]').tab('show');
    // Tab Events doc: https://getbootstrap.com/docs/4.0/components/navs/#events
    //$('#display-tabs a[href="#' + did + '"]').on('shown.bs.tab', this.checkResize.bind(this));
}