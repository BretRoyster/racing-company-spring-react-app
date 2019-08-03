import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './racing-company.reducer';
import { IRacingCompany } from 'app/shared/model/racing-company.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IRacingCompanyDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class RacingCompanyDetail extends React.Component<IRacingCompanyDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { racingCompanyEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            RacingCompany [<b>{racingCompanyEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">Name</span>
            </dt>
            <dd>{racingCompanyEntity.name}</dd>
            <dt>
              <span id="gasPrice">Gas Price</span>
            </dt>
            <dd>{racingCompanyEntity.gasPrice}</dd>
            <dt>
              <span id="servicePrice">Service Price</span>
            </dt>
            <dd>{racingCompanyEntity.servicePrice}</dd>
            <dt>
              <span id="street">Street</span>
            </dt>
            <dd>{racingCompanyEntity.street}</dd>
            <dt>
              <span id="city">City</span>
            </dt>
            <dd>{racingCompanyEntity.city}</dd>
            <dt>
              <span id="state">State</span>
            </dt>
            <dd>{racingCompanyEntity.state}</dd>
            <dt>
              <span id="zipCode">Zip Code</span>
            </dt>
            <dd>{racingCompanyEntity.zipCode}</dd>
            <dt>
              <span id="racingCode">Racing Code</span>
            </dt>
            <dd>{racingCompanyEntity.racingCode}</dd>
            <dt>Owner</dt>
            <dd>{racingCompanyEntity.ownerLogin ? racingCompanyEntity.ownerLogin : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/racing-company" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/racing-company/${racingCompanyEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ racingCompany }: IRootState) => ({
  racingCompanyEntity: racingCompany.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(RacingCompanyDetail);
