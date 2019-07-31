import './footer.scss';

import React from 'react';

import { Col, Row } from 'reactstrap';
import appConfig from 'app/config/constants';

const Footer = props => (
  <div className="footer page-content">
    <Row>
      <Col md="12">
        <a href="https://www.mudflapinc.com/merchant-terms/" target="_new">
          Merchant Terms of Use
        </a>
        <br />© Mudflap 2019
        <span className="navbar-version">v. {appConfig.VERSION}</span>
      </Col>
    </Row>
  </div>
);

export default Footer;
