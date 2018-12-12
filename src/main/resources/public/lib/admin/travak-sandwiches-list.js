import DenTravakAbstractElement from '../travak-abstract-element.js';

class DenTravakSandwichesList extends DenTravakAbstractElement {

    constructor() {
        super('travak-admin-app')
        setInterval(this.update.bind(this), 1000);
    }

    connectedCallback() {
        super.connectedCallback();
        fetch('http://127.0.0.1:8080/sandwiches')
            .then(resp => resp.json())
            .then(json => this.updateSandwichesList(json));
        this.initEventListeners();
    }

    initEventListeners() {
        this.byId('new-sandwich-btn').addEventListener('click', () => this.app().showEditSandwich())
        this.byId('show-orders-btn').addEventListener('click', () => this.app().showOrderList())
    }

    updateSandwichesList(sandwiches) {
        let sandwichesList = this.byId('sandwiches');
        sandwichesList.innerHTML = ``;
        sandwiches.forEach(sandwich => {
            let sandwichEl = htmlToElement(this.getSandwichTemplate(sandwich));
            sandwichEl.addEventListener('click', () => this.app().dispatchEvent(new CustomEvent('edit-sandwich', {detail: sandwich})));
            sandwichesList.appendChild(sandwichEl);
        });
    }

    update(){
        console.log("lalalallalalalala");
        fetch('http://127.0.0.1:8080/sandwiches')
            .then(resp => resp.json())
            .then(json => this.updateSandwichesList(json));
    }


    get template() {
        return `
            <style>
                div.dt-sandwich-info {
                    margin-left: auto;
                }
                .travak-header {
                    display: flex;
                }
                .travak-header div.buttons {
                    margin-left: auto;
                }
            </style>
            <div class="animate">
                <div class="travak-header">
                    <h4>Den Travak Broodjesbeheer</h4>
                    <div class="buttons">
                        <button id="show-orders-btn" type="button" class="btn btn-primary">Bestellingen</button>
                        <button id="new-sandwich-btn" type="button" class="btn btn-primary">Nieuw broodje</button>
                    </div>
                </div>
                <div>
                <ul id="sandwiches" class="list-group">
                </ul>
                </div>
            </div>
        `;
    }

    getSandwichTemplate(sandwich) {
        return `
            <a class="list-group-item">
                <button type="button" class="btn btn-primary bmd-btn-fab">
                    ${sandwich.name.charAt(0)}
                </button>
                <div class="bmd-list-group-col">
                    <p class="list-group-item-heading">${sandwich.name}</p>
                    <p class="list-group-item-text">${sandwich.ingredients}</p>
                </div>
                <div class="dt-sandwich-info">
                    <p class="list-group-item-text">${sandwich.price}</p>
                </div>
            </a>
        `;
    }
}
customElements.define('travak-sandwiches-list', DenTravakSandwichesList);