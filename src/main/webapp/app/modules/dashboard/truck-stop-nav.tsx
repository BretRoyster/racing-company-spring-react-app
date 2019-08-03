import React, { Component } from 'react';
import TruckStopNavLink from './truck-stop-nav-link';

class TruckStopNav extends React.Component<any, any> {
  render() {
    const links = [
      {
        label: 'Set Price',
        route: '#pricing',
        css: 'pricing'
      },
      {
        label: 'Profile',
        route: '#details',
        css: 'details'
      }
    ];
    const page_title = 'Pit Stop Dashboard';

    const linkHTML = [];
    links.forEach((elem, index) => {
      const { label, route, css } = links[index];
      linkHTML.push(
        <TruckStopNavLink
          key={label}
          name={label}
          link={route}
          css={this.props.subRoute === route ? css : ''}
          setDashboardRoute={this.props.setDashboardRoute}
        />
      );
    });

    return (
      <div className="navbar-wrap-container">
        <div className="topnavbar-list">
          <div className="pagetitlenav-name">{page_title}</div>
          <ul className="navbar-nav mr-4 topnavbar-menu" style={{ width: 185 }}>
            {linkHTML}
          </ul>
        </div>
      </div>
    );
  }
}

export default TruckStopNav;
