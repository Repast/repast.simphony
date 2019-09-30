import { empty } from '/js/utils.js';

export class Parameters {
    constructor() {
        this.container = document.querySelector('#params-container');
        this.reset_to_defaults = this.reset_to_defaults.bind(this);
    }

    get_values() {
        let vals = [];
        this.inputs.forEach((i) => {
            let elem = document.getElementById(i);
            let val = elem.value;
            if (elem.type === 'checkbox') {
                val = elem.checked;
            }
            let v = {
                name: i,
                value: val
            }
            vals.push(v);
        });
        return vals;
    }

    // ctrl.className = "form-control form-control-sm is-invalid";
    clear_errors() {
        this.inputs.forEach( (i) => {
            let ctrl = document.getElementById(i);
            if (ctrl.className.indexOf("is-invalid") != -1) {
                ctrl.className = "form-control form-control-sm";
            }
        });
    }

    set_errors(ids) {
        ids.forEach((i) => {
            let ctrl = document.getElementById(i);
            ctrl.className = "form-control form-control-sm is-invalid";
        });
    }

    reset_to_defaults() {
        this.inputs.forEach((i) => {
            let elem = document.getElementById(i);
            let value = this.default_values.get(i);
            if (elem.type === 'checkbox') {
                elem.checked = value == 'true';
            } else {
                elem.value = value;
            }
        });
    }

    reset(params) {
        empty(this.container);
        this.inputs = [];
        this.default_values = new Map();
        let form = document.createElement("form");
        form.className = "px-3 mt-1 mb-1";
        params.value.forEach((p) =>{
            this.default_values.set(p.name, p.default_value);
            
            let form_div = document.createElement("div");
            form_div.className = "form-group";
            let label = document.createElement("label");
            label.htmlFor = p.name;
            label.innerHTML = p.display_name;

            if (p.values.length == 0 || p.readonly == 'true') {
                let ptype = p.type.toLowerCase();
                var ctrl = document.createElement("input");

                if (p.readonly == 'true') {
                    ctrl.setAttribute('readonly', 'readonly');
                }

                if (ptype == 'boolean') {
                    ctrl.type = "checkbox";
                    ctrl.className = "form-check-input";
                    label.className = "form-check-label";
                    form_div.className = "form-check mb-2";
                    form_div.appendChild(ctrl);
                    form_div.appendChild(label);
                    ctrl.checked = p.default_value == 'true';

                } else {

                    if (ptype == 'int' || ptype == 'double' || ptype == 'short' ||
                        ptype == 'long' || ptype == 'byte' || ptype == 'float') {
                        ctrl.type = "number";
                    }  else {
                        ctrl.type = "text";
                    
                    }
                    ctrl.className = "form-control form-control-sm";
                    form_div.appendChild(label);
                    form_div.appendChild(ctrl);

                    var div = document.createElement("div");
                    div.className = "invalid-feedback";
                    div.innerHTML = "Please provide a valid " + ptype;
                    form_div.appendChild(div);

                    ctrl.value = p.default_value;
                }
            } else {
                var ctrl = document.createElement("select");
                ctrl.className = "form-control form-control-sm";
                p.values.forEach((v) => {
                    let opt = document.createElement("option");
                    opt.innerHTML = v;
                    ctrl.appendChild(opt);
                });
                ctrl.value = p.default_value;
                form_div.appendChild(label);
                form_div.appendChild(ctrl);
            }
                   
            ctrl.id = p.name;
            this.inputs.push(p.name);
            form.appendChild(form_div);
        });

        let button = document.createElement("button");
        button.className = "btn btn-primary" ;
        button.type="button";
        button.innerHTML = "Reset";
        button.onclick=this.reset_to_defaults;
        form.appendChild(button);
        this.container.appendChild(form);
    }
}