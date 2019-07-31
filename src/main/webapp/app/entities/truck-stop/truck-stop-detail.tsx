import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './truck-stop.reducer';
import { ITruckStop } from 'app/shared/model/truck-stop.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ITruckStopDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class TruckStopDetail extends React.Component<ITruckStopDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { truckStopEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            TruckStop [<b>{truckStopEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">Name</span>
            </dt>
            <dd>{truckStopEntity.name}</dd>
            <dt>
              <span id="basePrice">Base Price</span>
            </dt>
            <dd>{truckStopEntity.basePrice}</dd>
            <dt>
              <span id="opisPrice">Opis Price</span>
            </dt>
            <dd>{truckStopEntity.opisPrice}</dd>
            <dt>
              <span id="street">Street</span>
            </dt>
            <dd>{truckStopEntity.street}</dd>
            <dt>
              <span id="city">City</span>
            </dt>
            <dd>{truckStopEntity.city}</dd>
            <dt>
              <span id="state">State</span>
            </dt>
            <dd>{truckStopEntity.state}</dd>
            <dt>
              <span id="zipCode">Zip Code</span>
            </dt>
            <dd>{truckStopEntity.zipCode}</dd>
            <dt>
              <span id="mudflapCode">Mudflap Code</span>
            </dt>
            <dd>{truckStopEntity.mudflapCode}</dd>
            <dt>Owner</dt>
            <dd>{truckStopEntity.ownerLogin ? truckStopEntity.ownerLogin : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/truck-stop" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/truck-stop/${truckStopEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ truckStop }: IRootState) => ({
  truckStopEntity: truckStop.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(TruckStopDetail);
