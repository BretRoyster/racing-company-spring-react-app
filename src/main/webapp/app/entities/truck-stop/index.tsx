import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import TruckStop from './truck-stop';
import TruckStopDetail from './truck-stop-detail';
import TruckStopUpdate from './truck-stop-update';
import TruckStopDeleteDialog from './truck-stop-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={TruckStopUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={TruckStopUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TruckStopDetail} />
      <ErrorBoundaryRoute path={match.url} component={TruckStop} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={TruckStopDeleteDialog} />
  </>
);

export default Routes;
