import './dashboard.scss';
import React, { Component } from 'react';
import { Alert, Modal, ModalHeader, Button } from 'reactstrap';
import { AvForm, AvField } from 'availity-reactstrap-validation';
import TruckStopNav from './truck-stop-nav';

/*
 *
   Sorry code reviewer! This is a failed project for a potential client that couldn't pay!

   - See: getStory method below for the "story"

   ---
   Notes on original code...
   ---

   - 'ajaxWrapper' & 'format_date' were references which I did not migrate

   - I'm not at all confident in the original logic, aside from it seems to work as the potential client expected (but they complained of bugs).

   - The code-style is horrible. (again - beside making what they had work - I didn't fix that)

   - Original Native Base elements were replaced with 'reactstrap' and 'availity-reactstrap-validation' versions

 */

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
      subRoute: '#pricing',
      callout: false,
      storyIndex: 0
    };
  }

  componentDidMount() {
    this.refreshData();
    setInterval(this.refreshData, 300000);

    // For Demo
    setTimeout(() => {
      this.setState({ callout: true });
    }, 1000);
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
    //     to: '',
    //     from: '',
    //     subject: 'Truckstop Changed Their Pricing',
    //     text: 'The truckstop ' + this.state.name + ' changed their pricing to ' + automatic + '. The new partner price is: ' + partner_price
    //   },
    //   console.log
    // );
  };

  render() {
    // For Demo
    const { storyIndex } = this.state;
    const story = this.getStory(storyIndex);
    const hasNextStory = !!this.getStory(storyIndex + 1);

    // START ORIGINAL (mostly)
    const truckstopname_div = (
      <div className="truckstopname-div">
        {/* <h4>{this.state.name}</h4>  */}
        <h4>Bret's Pit Stop</h4>
        <p>
          {/* {this.state.city}, {this.state.state} */}
          Rogers, AR
        </p>
      </div>
    );
    let partner_price = (
      <div className="partnerprice-list">
        <p>Gas Rate: </p>
        <div>
          <small>$</small>
          <span>{this.state.base_price}</span>
        </div>
      </div>
    );
    let price_text = 'Set this as the Gas Rate:';
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
          <p>Additional Taxes:</p>
          <div>
            <small>$</small>
            <span>{this.state.opis_price.toFixed(2)}</span>
          </div>
        </div>
      );
      partner_price = (
        <div className="partnerprice-list">
          <p>Gas Rate: </p>
          <div>
            <small>$</small>
            <span>{(parseFloat(this.state.base_price) + parseFloat(this.state.opis_price)).toFixed(2)}</span>
          </div>
        </div>
      );
      price_text = 'The Gas Rate is cost plus what you add to this field:';
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
                <h3>Set Gas Rate</h3>
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
        {/* // END ORIGINAL */}

        {/* Demo Site */}
        {this.state.callout ? (
          <div className="callout">
            <div className="callout-header">{storyIndex > 0 ? `(...continued...)` : 'Story Time!'}</div>
            <span className="closebtn" onClick={this.closeCallout}>
              ×
            </span>
            <div className="callout-container">
              <p>
                {story}
                <br />
                <br />
                <Button color="danger" onClick={this.continueStory(hasNextStory)}>
                  {hasNextStory ? 'Continue' : 'Thanks for reading!'}
                </Button>
              </p>
            </div>
          </div>
        ) : null}
      </div>
    );

    return content;
  }

  /*
      For Demo site
  */

  closeCallout = () => {
    this.setState({ callout: false });
  };

  continueStory = hasNextStory => () => {
    if (hasNextStory) {
      this.setState(prevState => ({ storyIndex: prevState.storyIndex + 1 }));
    } else {
      this.closeCallout();
    }
  };

  getStory = index => {
    const continued = `<TODO: Story Continued>`;
    const gitHubMessage = (
      <>
        Checkout this component and its folder{' '}
        <a href="javascript:void()" target="_blank">
          here on GitHub
        </a>{' '}
        (links open in a new tabs).
      </>
    );
    switch (index) {
      case 0:
        return (
          <>
            <strong>Short version:</strong> Repurposed (unpaid) work for a potential client as a portfolio demo.
            <br />
            <br />
            => Please note that the state of this page is largely as I left it when we broke ties (ie. unfinished).
            <br />
            <br />
            => Colors, verbiage, and logos have been changed to respect their privacy/copyright (ie. they were not a racing company).
          </>
        );
      case 1:
        return (
          <>
            <strong>The goal on this page:</strong>
            <br />
            <br />
            1) The potential client had an existing ReactJS FE app that they wanted to "retain" as part of this new system that I was
            constructing.
            <br />
            <br />
            2) I refactored their JS ES5 component(s) into TypeScript ES6.
            <br />
            <small>
              <i>(also cleaned it up fast to pass type checks and the linting in my build process)</i>
            </small>
          </>
        );
      case 2:
        return (
          <>
            <strong>The goal on this page (cont):</strong>
            <br />
            <br />
            3) The sub-navbar 'states' work, but I did not migrate over the 'Profile' page; a simple input form to manage the 'Pit Stop'
            details.
            <br />
            <br />
            <small>This page is also not 'wired' to the new Spring Boot back-end & data model, but I might change that...</small>
          </>
        );
      case 3:
        return (
          <>
            <strong>Most interesting problem:</strong>
            <br />
            <br />
            {gitHubMessage}
            <br />
            <br />
            <i>
              You are looking at{' '}
              <a href="javascript:void(0)" target="_blank">
                dashboard.tsx
              </a>
            </i>
            <br />
            <br />
            The challenge was to 'quickly' port the styles (ie. css) of the migrated components without altering the global styles of the
            new ReactJS front-end.
          </>
        );
      case 4:
        return (
          <>
            <strong>Most interesting problem (cont):</strong>
            <br />
            <br />
            If you inspect the files:
            <ul>
              <li>
                <a href="javascript:void(0)" target="_blank">
                  dashboard.scss
                </a>
              </li>
              <li>
                <a href="javascript:void(0)" target="_blank">
                  ported-styles.scss
                </a>
              </li>
              <li>
                <a href="javascript:void(0)" target="_blank">
                  ported-styles-override.scss
                </a>
              </li>
            </ul>
            You'll see that{' '}
            <a href="javascript:void(0)" target="_blank">
              dashboard.scss
            </a>{' '}
            just imports the ported styles (completely unaltered) and then imports{' '}
            <a href="javascript:void(0)" target="_blank">
              ported-styles-override.scss
            </a>{' '}
            to fix compatibility issues with the new global styles.
            {/* <br /><br /><small>Part of the idea here was that this 'ugliness' could eventually be factored out with this separation</small> */}
            {/* <br /><br /><small>{gitHubMessage}</small> */}
          </>
        );
      case 5:
        return (
          <>
            <strong>Most interesting problem (cont):</strong>
            <br />
            <br />
            Additionally if you inspect{' '}
            <a href="javascript:void(0)" target="_blank">
              ported-styles.scss
            </a>{' '}
            you'll see that the entire file is guarded by '#ported-styles' & '.navbar-wrap-container'
            <small>
              <br />
              <br />
              (ie. the ported styles are only applied as per that specific css id or that specific css class).
              <br />
              <br />
              Also if the client desired additional pages from the older app, it would be very easy to style them by adding more 'guard' ids
              or classes.
            </small>
            {/* <br /><br />This isolated those styles so that new ReactJS Front-end would not be impacted by the older version "dropped in" to the new app. */}
            {/* <br /><br />(pretty simple - but fast and effective - and again - isolated to potentially be factored out down the line) */}
          </>
        );
      case 6:
        return (
          <>
            <strong>Most interesting problem (cont):</strong>
            <br />
            <br />
            This effectively "isolated" those styles so that new ReactJS Front-end would not be impacted by the older version's (unaltered)
            "dropped in" styles.
            <br />
            <br />
            <small>
              Pretty simple - and very cost effective for the task - also ideal if there's a future desire to factor-out the old styles.
            </small>
          </>
        );
      default:
        return null;
    }
  };
}

export default PriceForm;
