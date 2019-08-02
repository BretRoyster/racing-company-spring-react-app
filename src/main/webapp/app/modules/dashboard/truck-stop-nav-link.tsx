import React, { Component } from 'react';

class TruckStopNavLink extends React.Component<any, any> {
  constructor(props) {
    super(props);
  }

  navigate = () => {
    this.props.setDashboardRoute(this.props.link);
  };

  render() {
    return (
      <li className={`nav-item ${this.props.css}`}>
        <a className="nav-link" onClick={this.navigate}>
          {this.props.name}
        </a>
      </li>
    );
  }
}

export default TruckStopNavLink;
