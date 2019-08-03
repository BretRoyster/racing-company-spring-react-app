import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import RacingCompany from './racing-company';
import RacingCompanyDetail from './racing-company-detail';
import RacingCompanyUpdate from './racing-company-update';
import RacingCompanyDeleteDialog from './racing-company-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RacingCompanyUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RacingCompanyUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RacingCompanyDetail} />
      <ErrorBoundaryRoute path={match.url} component={RacingCompany} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={RacingCompanyDeleteDialog} />
  </>
);

export default Routes;
