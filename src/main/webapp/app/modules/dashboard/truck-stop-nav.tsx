import React, { Component } from 'react';
import TruckStopNavLink from './truck-stop-nav-link';

class TruckStopNav extends React.Component<any, any> {
  render() {
    const links = [['/dashboard/', 'Set Price'], ['/settings/', 'Profile']];
    const page_title = 'Pit Stop Dashboard';

    const linkHTML = [];
    links.forEach((elem, index) => {
      linkHTML.push(<TruckStopNavLink name={links[index][1]} link={links[index][0]} setGlobalState={this.props.setGlobalState} />);
    });

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
