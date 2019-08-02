import './dashboard.scss';
import React, { Component } from 'react';
import { Alert, Modal, ModalHeader, Button } from 'reactstrap';
import { AvForm, AvField } from 'availity-reactstrap-validation';
import TruckStopNav from './truck-stop-nav';

class PriceForm extends React.Component<any, any> {
  constructor(props) {
    super(props);
    const date = new Date();
    const formatted_date = date; // TODO: date format
    // const formatted_date = format_date(date, 'mm/dd/yyyy HH:MM');
    this.state = {
      automatic_pricing: true,
      base_price: 0.0,
      modal: false,
      saved: false,
      loaded: false,
      opis_price: 0.0,
      last_update: formatted_date,
      subRoute: '#pricing'
    };
  }

  componentDidMount() {
    this.refreshData();
    setInterval(this.refreshData, 300000);
  }

  refreshData = () => {
    if (this.props.truckstop) {
      // ajaxWrapper('GET', '/api/home/truckstop/' + this.props.truckstop + '/', {}, this.truckstop_callback); // TODO: data calls
    } else {
      // ajaxWrapper( // TODO: data calls
      //   'GET',
      //   '/api/home/truckstop/?owner=' +
      //   '/api/home/truckstop/?owner=' +
      //     // window.cmState.user.id // FIXME: NOO!!!! WHY!?!?! lol
      //     1,
      //   {},
      //   this.truckstop_callback
      // );
    }
  };

  truckstop_callback = result => {
    if (result.length > 0) {
      const truckstop = result[0]['truckstop'];
      const date = new Date();
      const formatted_date = date; // TODO: date format
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
  };

  setDashboardRoute = subRoute => {
    // state['base_price'] = +parseFloat(state['base_price']).toFixed(2); // TODO: What is this doing? This doesn't look right
    this.setState({ subRoute });
  };

  set_auto_price = () => {
    this.setState({ automatic_pricing: true });
  };

  set_manual_price = () => {
    this.setState({ automatic_pricing: false });
  };

  save_modal = () => {
    this.setState({ modal: true });
  };

  onHide = () => {
    this.setState({ modal: false });
  };

  save = () => {
    const data = { automatic_pricing: this.state.automatic_pricing, base_price: this.state.base_price };

    let automatic = 'automatic pricing with an additional charge of ' + this.state.base_price + '.';
    let partner_price = parseFloat(this.state.opis_price) + parseFloat(this.state.base_price);
    if (this.state.automatic_pricing === 'false') {
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
  };

  render() {
    const truckstopname_div = (
      <div className="truckstopname-div">
        <h4>{this.state.name}</h4>
        <p>
          {this.state.city}, {this.state.state}
        </p>
      </div>
    );
    let partner_price = (
      <div className="partnerprice-list">
        <p>Mudflap Rate: </p>
        <div>
          <small>$</small>
          <span>{this.state.base_price}</span>
        </div>
      </div>
    );
    let price_text = 'Set this as the Mudflap Rate:';
    let cssclass_update = 'manualselected setpricemode-container';
    let modal_text = (
      <div className="modalprice-inner">
        <ModalHeader size={2} text={'Are you sure?'} />
        <p>
          The partner price will be set to ${parseFloat(this.state.base_price).toFixed(2)}. To change this, come back anytime to the
          dashboard and enter a new price, or save time by opting for Automatic Pricing
        </p>
        <Button type="pricechange-save" text="APPLY MANUAL PRICE" onClick={this.save} />
        <Button type="cancel-button" text="CANCEL" onClick={this.onHide} />
      </div>
    );
    let opis_price = null;
    if (this.state.automatic_pricing === 'true' || this.state.automatic_pricing === true) {
      opis_price = (
        <div className="opisprice-list">
          <p>Rack + Taxes:</p>
          <div>
            <small>$</small>
            <span>{this.state.opis_price.toFixed(2)}</span>
          </div>
        </div>
      );
      partner_price = (
        <div className="partnerprice-list">
          <p>Mudflap Rate: </p>
          <div>
            <small>$</small>
            <span>{(parseFloat(this.state.base_price) + parseFloat(this.state.opis_price)).toFixed(2)}</span>
          </div>
        </div>
      );
      price_text = 'The Mudflap Rate is cost plus this markup:';
      cssclass_update = 'autoSelected setpricemode-container';
      modal_text = (
        <div className="modalprice-inner">
          <ModalHeader size={2} text={'Are you sure?'} />
          <p>
            The partner price will be set to ${this.state.base_price} + Your OPIS Price. To change this, come back anytime to the dashboard
            and enter a new price, or manually control your partner price with Manual Pricing
          </p>
          <Button type="pricechange-save" text="APPLY AUTOMATIC PRICE" onClick={this.save} />
          <Button type="cancel-button" text="CANCEL" onClick={this.onHide} />
        </div>
      );
    }

    let saved = null;
    if (this.state.saved) {
      saved = <Alert text="Saved" type="success" />;
    }

    const content = (
      <div>
        <TruckStopNav subRoute={this.state.subRoute} setDashboardRoute={this.setDashboardRoute} />
        <div style={{ display: 'flex', justifyContent: 'center' }}>
          <div id="ported-styles" className="setpricemaster-div">
            {truckstopname_div}
            <div className="setprice-container">
              <div className="setprice-inner">
                <h3>Set Mudflap Rate</h3>
                <div className={cssclass_update}>
                  <div className="setprice-auto" onClick={this.set_auto_price}>
                    <span>auto</span>
                  </div>
                  {
                    // <div className="setprice-manual" onClick={this.set_manual_price}>
                    //   <span>manual</span>
                    // </div>
                  }
                </div>
                <div className="pricechange-detail">
                  <div className="pricechange-desc">{price_text}</div>
                  <div className="pricechange-number">
                    <AvForm
                      // style={{ display: 'flex', justifyContent: 'center' }}
                      key={this.state.automatic_pricing}
                      defaults={this.state}
                      // setDashboardState={this.setDashboardState}
                      // autosetDashboardState
                      // globalStateName={'truckstop_form'}
                    >
                      <div className="d-flex justify-content-center">
                        <div className="pricechange-dollar">$</div>
                        <AvField
                          id="truck-stop-basePrice"
                          type="number"
                          className="form-control"
                          name="base_price"
                          step="0.01"
                          validate={{
                            required: { value: true, errorMessage: 'This field is required.' },
                            min: { value: 0, errorMessage: 'This field should be at least 0.' },
                            number: { value: true, errorMessage: 'This field should be a number.' }
                          }}
                        />
                      </div>
                      <Button color="primary" type="pricechange-save" className="btn-pricechange-save" onClick={this.save_modal}>
                        Save
                      </Button>
                    </AvForm>
                  </div>
                </div>
                {saved}
                <div className="pricelist-ctr">
                  {opis_price}
                  {partner_price}
                </div>

                {/* toggle={this.handleClose} */}
                {/* onHide={this.onHide} */}
                <Modal isOpen={this.state.modal}>{modal_text}</Modal>
              </div>
            </div>
            <small style={{ display: 'block', padding: '20px 0 10px', textAlign: 'center', color: '#666666' }}>
              <span>{`Data Refreshed As Of: ${this.state.last_update}`}</span>
            </small>
          </div>
        </div>
      </div>
    );

    return content;
  }
}

export default PriceForm;
