import React, { Component } from 'react';
// import {ajaxWrapper} from 'functions';
// import settings from 'base/settings.js';
// import {Navbar} from 'library';

class TruckStopNavLink extends React.Component<any, any> {
  constructor(props) {
    super(props);
    this.navigate = this.navigate.bind(this);
  }

  navigate() {
    // window.history.pushState({}, this.props.name, this.props.link)
    // this.props.setGlobalState("Links", {pathname: this.props.link});
  }

  render() {
    const linkclassname = 'nav-item ' + this.props.name;
    return (
      <li className={linkclassname}>
        <a className="nav-link" onClick={this.navigate}>
          {this.props.name}
        </a>
      </li>
    );
  }
}

export default TruckStopNavLink;
