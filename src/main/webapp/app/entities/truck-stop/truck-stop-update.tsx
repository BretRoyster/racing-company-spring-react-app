import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntity, updateEntity, createEntity, reset } from './truck-stop.reducer';
import { ITruckStop } from 'app/shared/model/truck-stop.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ITruckStopUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface ITruckStopUpdateState {
  isNew: boolean;
  ownerId: string;
}

export class TruckStopUpdate extends React.Component<ITruckStopUpdateProps, ITruckStopUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      ownerId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getUsers();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { truckStopEntity } = this.props;
      const entity = {
        ...truckStopEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/truck-stop');
  };

  render() {
    const { truckStopEntity, users, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="racingCompanyWebApp.truckStop.home.createOrEditLabel">Create or edit a TruckStop</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : truckStopEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="truck-stop-id">ID</Label>
                    <AvInput id="truck-stop-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="nameLabel" for="truck-stop-name">
                    Name
                  </Label>
                  <AvField
                    id="truck-stop-name"
                    type="text"
                    name="name"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="basePriceLabel" for="truck-stop-basePrice">
                    Base Price
                  </Label>
                  <AvField
                    id="truck-stop-basePrice"
                    type="string"
                    className="form-control"
                    name="basePrice"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' },
                      min: { value: 0, errorMessage: 'This field should be at least 0.' },
                      number: { value: true, errorMessage: 'This field should be a number.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="opisPriceLabel" for="truck-stop-opisPrice">
                    Opis Price
                  </Label>
                  <AvField
                    id="truck-stop-opisPrice"
                    type="string"
                    className="form-control"
                    name="opisPrice"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' },
                      min: { value: 0, errorMessage: 'This field should be at least 0.' },
                      number: { value: true, errorMessage: 'This field should be a number.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="streetLabel" for="truck-stop-street">
                    Street
                  </Label>
                  <AvField
                    id="truck-stop-street"
                    type="text"
                    name="street"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="cityLabel" for="truck-stop-city">
                    City
                  </Label>
                  <AvField
                    id="truck-stop-city"
                    type="text"
                    name="city"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="stateLabel" for="truck-stop-state">
                    State
                  </Label>
                  <AvField
                    id="truck-stop-state"
                    type="text"
                    name="state"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="zipCodeLabel" for="truck-stop-zipCode">
                    Zip Code
                  </Label>
                  <AvField
                    id="truck-stop-zipCode"
                    type="text"
                    name="zipCode"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="racingCodeLabel" for="truck-stop-racingCode">
                    Racing Code
                  </Label>
                  <AvField id="truck-stop-racingCode" type="text" name="racingCode" />
                </AvGroup>
                <AvGroup>
                  <Label for="truck-stop-owner">Owner</Label>
                  <AvInput id="truck-stop-owner" type="select" className="form-control" name="ownerId" required>
                    {users
                      ? users.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.login}
                          </option>
                        ))
                      : null}
                  </AvInput>
                  <AvFeedback>This field is required.</AvFeedback>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/truck-stop" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">Back</span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp; Save
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  users: storeState.userManagement.users,
  truckStopEntity: storeState.truckStop.entity,
  loading: storeState.truckStop.loading,
  updating: storeState.truckStop.updating,
  updateSuccess: storeState.truckStop.updateSuccess
});

const mapDispatchToProps = {
  getUsers,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(TruckStopUpdate);
