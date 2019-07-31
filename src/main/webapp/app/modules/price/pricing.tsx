import React, { Component } from 'react';

// import { Wrapper, Button, Header, EmptyModal, FormWithChildren, TextInput, NumberInput, Select, Alert } from './library';
// import { ajaxWrapper, format_date } from './functions';

const smallFooter = {
  display: 'block',
  padding: '20px 0 10px',
  textAlign: 'center',
  color: '#666666'
};

class PriceForm extends React.Component<any, any> {
  constructor(props) {
    super(props);
    var date = new Date();
    var formatted_date = date; // TODO: date format
    // var formatted_date = format_date(date, 'mm/dd/yyyy HH:MM');
    this.state = {
      automatic_pricing: false,
      base_price: 0.0,
      modal: false,
      saved: false,
      loaded: false,
      opis_price: 0.0,
      last_update: formatted_date
    };

    this.save = this.save.bind(this);
    this.setGlobalState = this.setGlobalState.bind(this);
    this.truckstop_callback = this.truckstop_callback.bind(this);
    this.save_modal = this.save_modal.bind(this);
    this.onHide = this.onHide.bind(this);
    this.set_auto_price = this.set_auto_price.bind(this);
    this.set_manual_price = this.set_manual_price.bind(this);
    this.refreshData = this.refreshData.bind(this);
  }

  componentDidMount() {
    this.refreshData();
    setInterval(this.refreshData, 300000);
  }

  refreshData() {
    if (this.props.truckstop) {
      console.log('Props', this.props.truckstop);
      // ajaxWrapper('GET', '/api/home/truckstop/' + this.props.truckstop + '/', {}, this.truckstop_callback); // TODO: data calls
    } else {
      // ajaxWrapper( // TODO: data calls
      //   'GET',
      //   '/api/home/truckstop/?owner=' +
      //     // window.cmState.user.id // FIXME: NOO!!!! WHY!?!?! lol
      //     1,
      //   {},
      //   this.truckstop_callback
      // );
    }
  }

  truckstop_callback(result) {
    console.log('Result', result);
    if (result.length > 0) {
      var truckstop = result[0]['truckstop'];
      var date = new Date();
      var formatted_date = date; // TODO: date format
      // format_date(date, 'mm/dd/yyyy HH:MM');
      if (truckstop['base_price']) {
        truckstop['base_price'] = truckstop['base_price'].toFixed(2);
      }

      truckstop['last_update'] = formatted_date;
      truckstop['loaded'] = true;
      this.setState(truckstop);
    } else {
      alert('There is no truckstop that belongs to this account.');
    }
  }

  setGlobalState(name, state) {
    state['base_price'] = +parseFloat(state['base_price']).toFixed(2);
    console.log('Base Price', state['base_price']);
    this.setState(state);
  }

  set_auto_price() {
    this.setState({ automatic_pricing: true });
  }

  set_manual_price() {
    this.setState({ automatic_pricing: false });
  }

  save_modal() {
    this.setState({ modal: true });
  }

  onHide() {
    this.setState({ modal: false });
  }

  save() {
    var data = { automatic_pricing: this.state.automatic_pricing, base_price: this.state.base_price };

    var automatic = 'automatic pricing with an additional charge of ' + this.state.base_price + '.';
    var partner_price = parseFloat(this.state.opis_price) + parseFloat(this.state.base_price);
    if (this.state.automatic_pricing == 'false') {
      automatic = 'manual pricing with a base price of ' + this.state.base_price + '.';
      partner_price = this.state.base_price;
    }

    // TODO: data calls
    // ajaxWrapper('POST', '/api/home/truckstop/' + this.state.id + '/', data, () => this.setState({ saved: true, modal: false }));
    // ajaxWrapper(
    //   'POST',
    //   '/api/email/',
    //   {
    //     to: 'sharon@mudflapinc.com',
    //     from: 'support@mudflap.us',
    //     subject: 'Truckstop Changed Their Pricing',
    //     text: 'The truckstop ' + this.state.name + ' changed their pricing to ' + automatic + '. The new partner price is: ' + partner_price
    //   },
    //   console.log
    // );
  }

  render() {
    var truckstopname_div = (
      <div className="truckstopname-div">
        <h4>{this.state.name}</h4>
        <p>
          {this.state.city}, {this.state.state}
        </p>
      </div>
    );
    var partner_price = (
      <div className="partnerprice-list">
        <p>Mudflap Rate: </p>
        <div>
          <small>$</small>
          <span>{this.state.base_price}</span>
        </div>
      </div>
    );
    var price_text = 'Set this as the Mudflap Rate:';
    var cssclass_update = 'manualselected setpricemode-container';
    var modal_text = (
      <div className="modalprice-inner">
        <Header size={2} text={'Are you sure?'} />
        <p>
          The partner price will be set to ${parseFloat(this.state.base_price).toFixed(2)}. To change this, come back anytime to the
          dashboard and enter a new price, or save time by opting for Automatic Pricing
        </p>
        <Button type="pricechange-save" text="APPLY MANUAL PRICE" onClick={this.save} />
        <Button type="cancel-button" text="CANCEL" onClick={this.onHide} />
      </div>
    );
    var opis_price = null;
    if (this.state.automatic_pricing == 'true' || this.state.automatic_pricing == true) {
      opis_price = (
        <div className="opisprice-list">
          <p>Rack + Taxes:</p>
          <div>
            <small>$</small>
            <span>{this.state.opis_price.toFixed(2)}</span>
          </div>
        </div>
      );
      var partner_price = (
        <div className="partnerprice-list">
          <p>Mudflap Rate: </p>
          <div>
            <small>$</small>
            <span>{(parseFloat(this.state.base_price) + parseFloat(this.state.opis_price)).toFixed(2)}</span>
          </div>
        </div>
      );
      price_text = 'The Mudflap Rate is cost plus this markup:';
      var cssclass_update = 'autoSelected setpricemode-container';
      var modal_text = (
        <div className="modalprice-inner">
          <Header size={2} text={'Are you sure?'} />
          <p>
            The partner price will be set to ${this.state.base_price} + Your OPIS Price. To change this, come back anytime to the dashboard
            and enter a new price, or manually control your partner price with Manual Pricing
          </p>
          <Button type="pricechange-save" text="APPLY AUTOMATIC PRICE" onClick={this.save} />
          <Button type="cancel-button" text="CANCEL" onClick={this.onHide} />
        </div>
      );
    }

    var saved = null;
    if (this.state.saved) {
      saved = <Alert text="Saved" type="success" />;
    }

    var content = (
      <div className="setpricemaster-div">
        {truckstopname_div}
        <div className="setprice-container">
          <div className="setprice-inner">
            <h3>Set Mudflap Rate</h3>
            <div className={cssclass_update}>
              <div className="setprice-auto" onClick={this.set_auto_price}>
                <span>auto</span>
              </div>
              <div className="setprice-manual" onClick={this.set_manual_price}>
                <span>manual</span>
              </div>
            </div>
            <div className="pricechange-detail">
              <div className="pricechange-desc">{price_text}</div>
              <div className="pricechange-number">
                <div className="pricechange-dollar">$</div>
                <FormWithChildren
                  key={this.state.automatic_pricing}
                  defaults={this.state}
                  setGlobalState={this.setGlobalState}
                  autoSetGlobalState={true}
                  globalStateName={'truckstop_form'}
                >
                  <NumberInput name="base_price" step={0.01} />
                </FormWithChildren>
              </div>
            </div>
            <Button type="pricechange-save" onClick={this.save_modal} text="Save" />
            {saved}
            <div className="pricelist-ctr">
              {opis_price}
              {partner_price}
            </div>

            <EmptyModal onHide={this.onHide} show={this.state.modal}>
              {modal_text}
            </EmptyModal>
          </div>
        </div>
        {
          // style={smallFooter}
        }
        <small>Data Refreshed As Of: {this.state.last_update}</small>
      </div>
    );

    return <Wrapper content={content} loaded={this.state.loaded} />;
  }
}

export default PriceForm;
