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
import { getEntity, updateEntity, createEntity, reset } from './racing-company.reducer';
import { IRacingCompany } from 'app/shared/model/racing-company.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IRacingCompanyUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IRacingCompanyUpdateState {
  isNew: boolean;
  ownerId: string;
}

export class RacingCompanyUpdate extends React.Component<IRacingCompanyUpdateProps, IRacingCompanyUpdateState> {
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
      const { racingCompanyEntity } = this.props;
      const entity = {
        ...racingCompanyEntity,
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
    this.props.history.push('/entity/racing-company');
  };

  render() {
    const { racingCompanyEntity, users, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="racingCompanyWebApp.racingCompany.home.createOrEditLabel">Create or edit a RacingCompany</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : racingCompanyEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="racing-company-id">ID</Label>
                    <AvInput id="racing-company-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="nameLabel" for="racing-company-name">
                    Name
                  </Label>
                  <AvField
                    id="racing-company-name"
                    type="text"
                    name="name"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="gasPriceLabel" for="racing-company-gasPrice">
                    Gas Price
                  </Label>
                  <AvField
                    id="racing-company-gasPrice"
                    type="string"
                    className="form-control"
                    name="gasPrice"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' },
                      min: { value: 0, errorMessage: 'This field should be at least 0.' },
                      number: { value: true, errorMessage: 'This field should be a number.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="servicePriceLabel" for="racing-company-servicePrice">
                    Service Price
                  </Label>
                  <AvField
                    id="racing-company-servicePrice"
                    type="string"
                    className="form-control"
                    name="servicePrice"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' },
                      min: { value: 0, errorMessage: 'This field should be at least 0.' },
                      number: { value: true, errorMessage: 'This field should be a number.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="streetLabel" for="racing-company-street">
                    Street
                  </Label>
                  <AvField
                    id="racing-company-street"
                    type="text"
                    name="street"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="cityLabel" for="racing-company-city">
                    City
                  </Label>
                  <AvField
                    id="racing-company-city"
                    type="text"
                    name="city"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="stateLabel" for="racing-company-state">
                    State
                  </Label>
                  <AvField
                    id="racing-company-state"
                    type="text"
                    name="state"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="zipCodeLabel" for="racing-company-zipCode">
                    Zip Code
                  </Label>
                  <AvField
                    id="racing-company-zipCode"
                    type="text"
                    name="zipCode"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="racingCodeLabel" for="racing-company-racingCode">
                    Racing Code
                  </Label>
                  <AvField id="racing-company-racingCode" type="text" name="racingCode" />
                </AvGroup>
                <AvGroup>
                  <Label for="racing-company-owner">Owner</Label>
                  <AvInput id="racing-company-owner" type="select" className="form-control" name="ownerId" required>
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
                <Button tag={Link} id="cancel-save" to="/entity/racing-company" replace color="info">
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
  racingCompanyEntity: storeState.racingCompany.entity,
  loading: storeState.racingCompany.loading,
  updating: storeState.racingCompany.updating,
  updateSuccess: storeState.racingCompany.updateSuccess
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
)(RacingCompanyUpdate);
