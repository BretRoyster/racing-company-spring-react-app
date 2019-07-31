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
    var linkclassname = 'nav-item ' + this.props.name;
    return (
      <li className={linkclassname}>
        <a className="nav-link" onClick={this.navigate}>
          {this.props.name}
        </a>
      </li>
    );
  }
}

class TruckStopNav extends React.Component<any, any> {
  render() {
    var links = [['/dashboard/', 'Set Price'], ['/settings/', 'Profile']];
    var page_title = 'Truck Stop Dashboard';

    var linkHTML = [];
    for (var index in links) {
      linkHTML.push(<TruckStopNavLink name={links[index][1]} link={links[index][0]} setGlobalState={this.props.setGlobalState} />);
    }

    return (
      <div className="navbar-wrap-container">
        <div className="topnavbar-list" id={this.props.addmoreclass}>
          <div className="pagetitlenav-name">{page_title}</div>
          <ul className="navbar-nav mr-4 topnavbar-menu">{linkHTML}</ul>
        </div>
      </div>
    );
  }
}

export default TruckStopNav;
