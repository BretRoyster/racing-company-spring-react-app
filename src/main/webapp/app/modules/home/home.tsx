import './home.scss';

import React from 'react';
import { Link, Redirect } from 'react-router-dom';

import { connect } from 'react-redux';
import { Row, Col, Alert } from 'reactstrap';

import { IRootState } from 'app/shared/reducers';
import { getSession } from 'app/shared/reducers/authentication';

export interface IHomeProp extends StateProps, DispatchProps {}

export class Home extends React.Component<IHomeProp> {
  componentDidMount() {
    this.props.getSession();
  }

  render() {
    const { account, isAuthenticated } = this.props;
    if (!isAuthenticated) {
      return <Redirect to={{ pathname: '/login' }} />;
    }
    return <Redirect to={{ pathname: '/entity/truck-stop' }} />;
    //     return (
    //       <Row>
    //         {
    //           <Col md="12">
    //             {
    //               // <h2>Welcome Sharon and Sanjay!</h2>
    //               // <h3>This is a Spring Boot Java back-end app / with an initial ReactJS FE built with the JHipster tool!</h3>
    //               // <br />
    //               // <p>
    //               //   <b>Please NOTE:</b> I'd update the default styles here to match your current app - OR - We could just wire up your current
    //               //   React app as your end-user's client at first.
    //               // </p>
    //               // <p>Either way it shouldn't be more than a few hours to port all of that over.</p>
    //               // <p>I'd also get rid of the bowtie icons and the pacman guy on initial load (hopefully that's obvious)</p>
    //               // <p>
    //               //   You might see errors pop up as I'm getting the DB connected now - but cool thing about that... you already have FE logic that
    //               //   will tell your user if the site is experiencing errors, etc.{' '}
    //               //   <i>
    //               //     (there's lots of little things like that you are getting here for 'free', there's probably things implemented here now that
    //               //     you could possibly want in the future including elastic search engine capabiliies, easy support of internationalization,
    //               //     distributed caching, - ad nauseum)
    //               //   </i>
    //               // </p>
    //               // <p>
    //               //   Its like tapping into a web app development environment with full-stack pre-builts ready to go based on what you need - then
    //               //   we can spend the most time building your actual business logic instead of dealing with configuring tools and misconfigured
    //               //   framework issues, etc.
    //               // </p>
    //               // <p>
    //               //   Right now your user models are in place - along with registering and logging in - with a bit more time I'll show you how fast
    //               //   I can add front-to-back data modals and the generated admin interfaces, etc.
    //               // </p>
    //               // <p>Verification emails might not be hooked up right now though.</p>
    //               // <p>- Bret</p>
    //             }
    //           </Col>
    //
    //           // <Col md="9">
    //
    //           //   <h2>Welcome, Java Hipster!</h2>
    //           //   <p className="lead">This is your homepage</p>
    //           //   {account && account.login ? (
    //           //     <div>
    //           //       <Alert color="success">You are logged in as user {account.login}.</Alert>
    //           //     </div>
    //           //   ) : (
    //           //     <div>
    //           //       <Alert color="warning">
    //           //         If you want to
    //           //         <Link to="/login" className="alert-link">
    //           //           {' '}
    //           //           sign in
    //           //         </Link>
    //           //         , you can try the default accounts:
    //           //         <br />- Administrator (login=&quot;admin&quot; and password=&quot;admin&quot;)
    //           //         <br />- User (login=&quot;user&quot; and password=&quot;user&quot;).
    //           //       </Alert>
    //
    //           //       <Alert color="warning">
    //           //         You do not have an account yet?&nbsp;
    //           //         <Link to="/register" className="alert-link">
    //           //           Register a new account
    //           //         </Link>
    //           //       </Alert>
    //           //     </div>
    //           //   )}
    //           //   <p>If you have any question on JHipster:</p>
    //
    //           //   <ul>
    //           //     <li>
    //           //       <a href="https://www.jhipster.tech/" target="_blank" rel="noopener noreferrer">
    //           //         JHipster homepage
    //           //       </a>
    //           //     </li>
    //           //     <li>
    //           //       <a href="http://stackoverflow.com/tags/jhipster/info" target="_blank" rel="noopener noreferrer">
    //           //         JHipster on Stack Overflow
    //           //       </a>
    //           //     </li>
    //           //     <li>
    //           //       <a href="https://github.com/jhipster/generator-jhipster/issues?state=open" target="_blank" rel="noopener noreferrer">
    //           //         JHipster bug tracker
    //           //       </a>
    //           //     </li>
    //           //     <li>
    //           //       <a href="https://gitter.im/jhipster/generator-jhipster" target="_blank" rel="noopener noreferrer">
    //           //         JHipster public chat room
    //           //       </a>
    //           //     </li>
    //           //     <li>
    //           //       <a href="https://twitter.com/java_hipster" target="_blank" rel="noopener noreferrer">
    //           //         follow @java_hipster on Twitter
    //           //       </a>
    //           //     </li>
    //           //   </ul>
    //
    //           //   <p>
    //           //     If you like JHipster, do not forget to give us a star on{' '}
    //           //     <a href="https://github.com/jhipster/generator-jhipster" target="_blank" rel="noopener noreferrer">
    //           //       Github
    //           //     </a>
    //           //     !
    //           //   </p>
    //           // </Col>
    //           // <Col md="3" className="pad">
    //           //   <span className="hipster rounded" />
    //           // </Col>
    //         }
    //       </Row>
    //     );
  }
}

const mapStateToProps = storeState => ({
  account: storeState.authentication.account,
  isAuthenticated: storeState.authentication.isAuthenticated
});

const mapDispatchToProps = { getSession };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Home);
