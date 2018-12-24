import DenTravakAbstractElement from '../travak-abstract-element.js';

class DenTravakOrderList extends DenTravakAbstractElement {

    constructor() {
        super('travak-admin-app')
        setInterval(this.update.bind(this), 1000);
    }

    connectedCallback() {
        super.connectedCallback();
        this.update();
        this.initEventListeners();
    }

    initEventListeners() {
        this.byId('edit-sandwiches-btn').addEventListener('click', (e) => this.app().showSandwichList());
        this.app().addEventListener('order-succeeded', (e) => {
            console.log("event triggered");
            this.update();
        });
        this.byId('Download').addEventListener('click', (e) => this.toCSV())
    }

    toCSV(){
        fetch('http://127.0.0.1:8080/orders?print=true')
            .then(resp => resp.json())
            .then(json => {
                const header = ["name", "breadType", "creationDate", "price", "mobilePhoneNumber"]
                var replacer = function(key, value) { return value === null ? '' : value } 
                let csv = json.map(row => header.map(fieldName => JSON.stringify(row[fieldName], replacer)).join(','))
                csv.unshift(header.join(','))
                csv = csv.join('\r\n')
                console.log(csv);
            });
    }

    update(){
        fetch('http://127.0.0.1:8080/orders')
            .then(resp => resp.json())
            .then(json => this.updateOrderList(json));
    }

    updateOrderList(orders) {
        let orderList = this.byId('orders');
        orderList.innerHTML = ``;
        orders.forEach(order => {
            let orderEl = htmlToElement(this.getOrderTemplate(order));
            orderList.appendChild(orderEl);
        });
    }

    get template() {
        return `
            <style>
                div.dt-order-info {
                    margin-left: auto;
                }
                .bmd-list-group-col {
                    width: 70%;
                }
                p.list-group-item-heading {
                    display:flex;
                    justify-content: space-between;
                }
                span.creationDate {
                    display:inline-block;
                    float: right;
                }
                .travak-header {
                    display: flex;
                }
                .travak-header button {
                    margin-left: auto;
                }
            </style>
            <div class="animate">
                <div class="travak-header">
                    <h4>Den Travak Bestellingen</h4>
                    <button id="edit-sandwiches-btn" type="button" class="btn btn-primary">Bewerk broodjeslijst</button>
                </div>
                <div>
                <ul id="orders" class="list-group">
                </ul>
                </div>
                <button type="button" class="btn btn-primary" id="Download">
                Download CSV
                </button>
            </div>
        `;
    }

    getOrderTemplate(order) {
        return `
            <a class="list-group-item">
                <button type="button" class="btn btn-primary bmd-btn-fab">
                    ${order.name.charAt(0)}
                </button>
                <div class="bmd-list-group-col">
                    <p class="list-group-item-heading">${order.mobilePhoneNumber}<span class="creationDate">${dateFns.distanceInWordsToNow(order.creationDate)} ago</span><span>${(order.printed) ? "printed" : "not printed" }</p>
                    <p class="list-group-item-text">${order.name} - ${order.breadType.toLowerCase()}</p>
                </div>
                <div class="dt-order-info">
                    <p class="list-group-item-text">${order.price}</p>
                </div>
            </a>
        `;
    }
}

customElements.define('travak-order-list', DenTravakOrderList);