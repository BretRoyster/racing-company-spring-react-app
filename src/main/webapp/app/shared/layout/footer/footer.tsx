import './footer.scss';

import React from 'react';

import { Col, Row } from 'reactstrap';
import appConfig from 'app/config/constants';

const Footer = props => (
  <div className="footer page-content">
    <Row>
      <Col md="12">
        {
          // target="_new"
        }
        <a href="javascript:void()">Merchant Terms of Use</a>
        <br />© Racing Company 2019
        <br />
        Hey! This isn't a real company (just FYI)
        <span className="navbar-version">v. {appConfig.VERSION}</span>
      </Col>
    </Row>
  </div>
);

export default Footer;
