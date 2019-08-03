import './footer.scss';

import React from 'react';

import { Col, Row } from 'reactstrap';
import appConfig from 'app/config/constants';

const Footer = props => (
  <div className="footer page-content">
    <Row>
      <Col md="12">
        © 2019 | Bret Royster & RabTrade LLC
        <br />
        <span className="navbar-version">v. {appConfig.VERSION}</span>
      </Col>
    </Row>
  </div>
);

export default Footer;
