import DenTravakAbstractElement from '../travak-abstract-element.js';

class DenTravakSandwichesOrderConfirmation extends DenTravakAbstractElement {

    connectedCallback() {
        super.connectedCallback();
        this.initEventListeners();
    }

    init(order) {
        this.order = order;
    }

    initEventListeners() {
        this.byId('show-sandwich-list').addEventListener('click', e => this.app().dispatchEvent(new CustomEvent('show-personal-sandwich-list', {detail: this.order.mobilePhoneNumber})));
        this.byId('rating').addEventListener('change', e => {
            this.postPreference(this.order);
        });
    }

    postPreference(order){
        var select = this.byId('rating');
        var score = select.options[select.selectedIndex].value;
        fetch('http://127.0.0.1:8081/recommendation/recommend', {
            method: "POST",
            headers: {
                "Content-Type": "application/json; charset=utf-8"
            },
            body: '{"emailAddress": "' + order.mobilePhoneNumber + '", "ratedItem" : "' + order.sandwichId + '", "rating" : "' + score + '"}',
        }).then(rsp => rsp.json());
    }

    get template() {
        return `
            <style>
                .form-group {
                    margin-bottom: 2rem!important;
                }
                .dt-header {
                    display: flex;
                }
                .dt-header button {
                    margin-left: auto;
                }
                div.dt-sandwich-info {
                    margin-left: auto;
                }
            </style>
            <div class="animate">
                <div class="dt-header">
                    <h3>Welkom bij den Travak</h3>
                    <button id="show-sandwich-list" type="button" class="btn btn-primary">Nieuwe bestelling</button>
                </div>
                <h4>Bedankt!</h4>
                <p>Wij hebben je bestelling goed ontvangen en je kan je broodje komen ophalen vanaf 11u45.</p>
                <p>Tot zo dadelijk!</p>
                <p>Rate uw broodje</p>
                <div>
                    <select id="rating">
                        <option value="1">1</option>
                        <option value="2">2</option>
                        <option value="3">3</option>
                        <option value="4">4</option>
                        <option value="5">5</option>
                    </select> 
                </div>
            </div>
        `;
    }
}

customElements.define('travak-sandwiches-order-confirmation', DenTravakSandwichesOrderConfirmation);